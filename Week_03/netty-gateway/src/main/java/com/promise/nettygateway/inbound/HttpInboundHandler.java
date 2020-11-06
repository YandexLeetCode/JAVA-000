package com.promise.nettygateway.inbound;

import com.promise.nettygateway.threadpool.HttpThreadPool;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by yandex on 2020/11/1.
 */
@Slf4j
public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        boolean active = ctx.channel().isActive();
        log.info("通道:{},状态:{}", ctx.channel().id(),active);
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            HttpThreadPool.service(ctx, msg);
        } catch (Exception ex) {
            log.error("HttpInboundHandler:channelRead: " + ex);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if (!channel.isActive()) {
            log.error("###########客户端:{},网络发生异常,断开了连接!\n",channel.remoteAddress().toString());
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
}
