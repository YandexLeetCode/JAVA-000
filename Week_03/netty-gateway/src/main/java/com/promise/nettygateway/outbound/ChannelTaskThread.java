package com.promise.nettygateway.outbound;

import com.promise.nettygateway.filter.NettyHttpRequestFilter;
import com.promise.nettygateway.threadpool.HttpThreadPool;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

@Slf4j
public class ChannelTaskThread implements Callable<String> {
    final NettyHttpClientPool nettyHttpClientPool = NettyHttpClientPool.getInstance();

    /** 发送数据的 body 与 请求 的URL */
    private String message;
    private URI uri;

    public ChannelTaskThread (URI uri, String message) {
        this.message = message;
        this.uri = uri;
    }
    /**
     * 客户端连接 server 发送数据
     * @return
     * @throws Exception
     */
    @Override
    public String call() throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        long random = Long.parseLong(df.format(new Date())) * 1000000 + Math.round(Math.random() * 1000000);

        Channel channel = nettyHttpClientPool.getChannel(random);
        log.info("在连接池中获取到Channel: " + channel.id());

        // 获取到 channel  之后 组织需要发送的数据,发送到 三方服务

        log.info("Netty Http Client Send Request : {}",uri.toString());
        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath());

        // 构建 HTTP
        request.headers().set(HttpHeaderNames.HOST, uri.getHost());
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

        // 发送前  增加 自定义头 属性,这块 还需要优化
        NettyHttpRequestFilter nettyHttpRequestFilter = new NettyHttpRequestFilter();
        nettyHttpRequestFilter.filter(request,null);

        NettyClientHandler httpClientHandler = channel.pipeline().get(NettyClientHandler.class);
        ChannelId id = channel.id();
        log.info("发送 随机码:[{}], 请求 和 通道 ID:[{}]",random, id);


        String serverMsg = (String) httpClientHandler.sendMessage(request, channel);
        NettyHttpClientPool.release(channel);
        // 将 返回的结果 返回出去
        return serverMsg;
    }
}
