package com.promise.nettygateway.outbound;

import com.promise.nettygateway.filter.NettyHttpRequestFilter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by yandex on 2020/11/2.
 */
@Slf4j
public class NettyHttpClient {

    /**
     * 通过 Netty 发送 get 请求
     * 至于 Http 中其他方式 后期 补充
     * @param url
     * @param host
     * @param port
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    public void sendRequest(final ChannelHandlerContext requestCtx, final String url, final String host, final int port) throws InterruptedException, URISyntaxException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                    ch.pipeline().addLast(new HttpResponseDecoder());
                    // 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
                    ch.pipeline().addLast(new HttpRequestEncoder());
                    ch.pipeline().addLast(new NettyHttpClientOutboundHandler(requestCtx));
                }
            });
            // 启动 客户端
            Channel channel = bootstrap.connect(host,port).sync().channel();
            // 使用 经过 router 之后 分配的 请求 Api Name
            URI uri = new URI(url);
            log.info("Netty Http Client Send Request : {}",uri.toString());
            FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath());

            // 构建 HTTP
            request.headers().set(HttpHeaderNames.HOST, host);
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

            // 发送前  增加 自定义头 属性
            NettyHttpRequestFilter nettyHttpRequestFilter = new NettyHttpRequestFilter();
            nettyHttpRequestFilter.filter(request,null);

            // 发送
            channel.writeAndFlush(request);

            // 等待 server close connection
            channel.closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
        }
    }

}
