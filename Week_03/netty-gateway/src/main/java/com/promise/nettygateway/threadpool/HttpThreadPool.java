package com.promise.nettygateway.threadpool;

import com.promise.nettygateway.outbound.NettyHttpClient;
import com.promise.nettygateway.router.NettyHttpRouter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by yandex on 2020/11/1.
 */
@Slf4j
public class HttpThreadPool {
    /**
     * 测试机器 6 核 考虑 CPU 充分利用,这里 分配 五倍的 线程数
     */
    private static ExecutorService executorService = Executors.newFixedThreadPool(30);

    private static List<String> endpoints  = Arrays.asList("http://localhost:8808/test","http://localhost:8808/test","http://localhost:8808/test","http://localhost:8808/test","http://localhost:8808/test","http://localhost:8808/test");
    /**
     *
     * 对外处理线程
     * @param ctx
     * @param msg
     */
    public static void run(ChannelHandlerContext ctx, Object msg) {
       try {
           executorService.execute(() -> service(ctx, msg));
       } catch ( Exception ex) {
           log.error("HttpThreadPool:run " + ex);
       }
    }

    /**
     * 三方返回 异步线程调用返回
     * @param ctx
     * @param msg
     */
    public static void responseRun(ChannelHandlerContext ctx,Object msg) {
        try {
            executorService.execute(() -> handleResponse(ctx, (String) msg));
        } catch ( Exception ex) {
            log.error("HttpThreadPool:responseRun " + ex);
        }
    }
    /**
     * 如果 前端 访问的方式不是我们想要的 就直接返回
     * @param ctx
     * @param msg
     */
    private static void handleResponse( final ChannelHandlerContext ctx, final String msg)
    {
        FullHttpResponse response = null;
       try {
          response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(msg.getBytes())) ;
          response.headers().set("Content-Type", "application/json");
          response.headers().setInt("Content-Length", msg.getBytes().length);
       } catch (Exception ex) {
           log.error("HttpThreadPool:handleResponse: =>> " + ex);
           response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
           exceptionCaught(ctx,ex);
       } finally {
           if (ctx.channel().isActive()) {
               response.headers().set(CONNECTION, KEEP_ALIVE);
               ctx.writeAndFlush(response);
           } else {
               ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
           }
       }
    }

    /**
     * 出现异常 打日志 关闭 连接
     * @param ctx
     * @param cause
     */
    private static void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private static void service(ChannelHandlerContext ctx, Object msg) {
        try {
            log.info("接收到请求 数据 : {}",msg);
            if ( msg instanceof FullHttpRequest ) {
                FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
                // 根据请求的 URL 方法名判断  根据 router 进行分发
                String url = new NettyHttpRouter(fullHttpRequest).route(endpoints);
                // 拿着 url 分割 获取 ip 与 port
                URI uri = new URI(url);
                NettyHttpClient nettyHttpClient = new NettyHttpClient();
                nettyHttpClient.sendRequest(ctx,url,uri.getHost(),uri.getPort());
                log.info("sendRequest Over");
            } else {
                handleResponse(ctx, "not support data type");
            }
        } catch ( Exception ex ){
            log.error("HttpThreadPool:service " + ex);
        }
    }
}
