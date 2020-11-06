package com.promise.nettygateway.outbound;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Created by yandex on 2020/11/5.
 */
@Slf4j
public class NettyChannelPoolHandler implements ChannelPoolHandler {

    @Override
    public void channelReleased(Channel channel) throws Exception {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER);
        log.info("|| ==> 回收Channel. ID: {}", channel.id());
    }

    @Override
    public void channelAcquired(Channel channel) throws Exception {
        log.info("|| ==> 获取Channel. ID: {}", channel.id());
    }

    @Override
    public void channelCreated(Channel channel) throws Exception {
        log.info("|| ==> 创建Channel. ID: {} ,Channel REAL HASH: {}",channel.id(), System.identityHashCode(channel));
        SocketChannel socketChannel = (SocketChannel) channel;
        socketChannel.config().setKeepAlive(true);
        socketChannel.config().setTcpNoDelay(true);
        socketChannel.pipeline().addLast(new HttpResponseDecoder())
                .addLast(new HttpRequestEncoder())
                .addLast(new IdleStateHandler(0, 0, 10, TimeUnit.SECONDS))
                .addLast(new NettyHttpClientOutboundHandler())
    }
}
