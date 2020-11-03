package com.promise.nettygateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * Created by yandex on 2020/11/3.
 */
public class NettyHttpRequestFilter implements HttpRequestFilter{
    private final static String HTTP_HEADER = "PROMISE";
    /**
     * 过滤器 作用 就是在 向后台真正URL 发送请求时候,在fullHttpRequest 里面 header 增加一个固定的 key value
     * @param fullHttpRequest
     * @param ctx
     */
    @Override
    public void filter(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx) {
        if (fullHttpRequest != null) {
            fullHttpRequest.headers().set("NIO",HTTP_HEADER);
        }
    }
}
