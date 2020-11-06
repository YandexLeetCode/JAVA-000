package com.promise.nettygateway.outbound;

import com.promise.nettygateway.constant.HttpGateWayConstant;
import com.promise.nettygateway.threadpool.HttpThreadPool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by yandex on 2020/11/2.
 */
@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /** 接收 http client 返回的结果 */
    private static final Map<Long, LinkedBlockingQueue<String>> CLIENT_RESULT_MAP = new ConcurrentHashMap<>();

    static Map<Integer, CopyOnWriteArrayList<Channel>> coreChannel = new ConcurrentHashMap<>();

    /**
     * 回收 多余的 连接
     * @param channels
     * @param channel
     */
    public synchronized void deleteChannel(CopyOnWriteArrayList<Channel> channels, Channel channel) {
        if (!channels.isEmpty()) {
            channels.removeIf(ch -> ch.equals(channel));
        }
    }

    /**
     * 超时等待 判断 是否存在不活跃 连接 将其删除
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("[客户端心跳监测发送] 通道编号: {}",ctx.channel().id());

        Channel channel = ctx.channel();
        if ( evt instanceof IdleStateEvent ) {
            if (channel.isActive()) {
                /** 如果客户端发送 心跳检查,说明当前无业务请求,释放通道数为设定的 核心数*/
                int poolHash = NettyHttpClientPool.getPoolHash(channel);
                CopyOnWriteArrayList<Channel> channels = coreChannel.get(poolHash);
                channels = channels == null ? new CopyOnWriteArrayList<>() : channels;
                channels.add(channel);
                if (channels.stream().filter(Channel::isActive).count() > HttpGateWayConstant.CORE_CONNECTION) {
                    log.info(" 关闭 CORE_CONNECTIONS 范围之外的Channel通道: {}",channel.id());
                    deleteChannel(channels,channel);
                    channel.close();
                }
                coreChannel.put(poolHash, channels);
            }
        } else {
           super.userEventTriggered(ctx, evt);
        }

    }

    /**
     * 发送数据到服务端
     * @param message
     * @param channel
     * @return
     */
    public Object sendMessage(Object message, Channel channel) {
        LinkedBlockingQueue<String> linked = new LinkedBlockingQueue<>(1);

        // get channel store global unique value
        Long randomId = channel.attr(AttributeKey.<Long>valueOf(HttpGateWayConstant.RANDOM_KEY)).get();
        CLIENT_RESULT_MAP.put(randomId, linked);

        channel.writeAndFlush(message);
        //  返回信息有可能会变化
        String res = null;
        try {
            res = CLIENT_RESULT_MAP.get(randomId).poll(3, TimeUnit.MINUTES);
            // 从 阻塞队列中 删除 本次通信的数据
            CLIENT_RESULT_MAP.remove(randomId);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return res;

    }

    /**
     * 打印 异常
     * @param ctx
     * @param cause
     * @throws Exception
     */
   @Override
   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       super.exceptionCaught(ctx, cause);
       Channel channel = ctx.channel();
       if (!channel.isActive()) {
           log.error("###########服务端:{},网络发生异常,断开了连接!\n",channel.remoteAddress().toString());
           log.error("错误原因:{}",cause.getCause().toString());
           ctx.close();
       } else {
           ctx.fireExceptionCaught(cause);
           /**
            * 暂时关闭 断开连接
            */
           ctx.close();
           log.error("{}: 异常,栈信息 {}",ctx.channel().remoteAddress().toString(),cause);
       }
   }

    /**
     * 读取服务端返回的数据，同时 存到 阻塞队列中
     * @param channelHandlerContext
     * @param msg
     * @throws Exception
     */
   @Override
   public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
     if ( msg instanceof HttpResponse) {
         HttpResponse response = (HttpResponse) msg;
         log.info("Content-type:{}",response.headers().get(CONTENT_TYPE));
     }

     if (msg instanceof HttpContent ) {
         HttpContent content = (HttpContent) msg;
         ByteBuf buf = content.content();
         Long randomId = channelHandlerContext.channel().attr(AttributeKey.<Long>valueOf(HttpGateWayConstant.RANDOM_KEY)).get();
         log.info("接收到第三方服务返回数据:{}",buf.toString(CharsetUtil.UTF_8));
         LinkedBlockingQueue<String>  linked = CLIENT_RESULT_MAP.get(randomId);
         linked.add(buf.toString(CharsetUtil.UTF_8));
     }
   }

    /**
     *
     * @param ctx
     */
    @Override
   public void channelReadComplete(ChannelHandlerContext ctx) {
        boolean active = ctx.channel().isActive();
        log.info("通道:{},状态:{}", ctx.channel().id(),active);
   }


}
