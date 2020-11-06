package com.promise.nettygateway.outbound;

import com.promise.nettygateway.constant.HttpGateWayConstant;
import com.promise.nettygateway.threadpool.HttpThreadPool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
public class NettyHttpClientOutboundHandler extends ChannelInboundHandlerAdapter {

    /** 接收 http client 返回的结果 */
    private static final Map<Long, LinkedBlockingQueue<String>> CLIENT_RESULT_MAP = new ConcurrentHashMap<>();

    // TODO 此处有可能改动
    private  ChannelHandlerContext requestCtx;
    NettyHttpClientOutboundHandler(ChannelHandlerContext ctx){
        this.requestCtx = ctx;
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

   @Override
   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
     cause.printStackTrace();
     ctx.close();
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

         //TODO 此处有可能改动 拼接 返回的数据
         HttpThreadPool.responseRun(this.requestCtx,buf.toString(CharsetUtil.UTF_8));
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
