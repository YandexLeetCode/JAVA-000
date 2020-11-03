package com.promise.nettygateway.inbound;

import com.promise.nettygateway.threadpool.HttpThreadPool;
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
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            HttpThreadPool.run(ctx, msg);
        } catch (Exception ex) {
            log.error("HttpInboundHandler:channelRead: " + ex);
        }
    }
}
