package com.promise.nettygateway.outbound;

import com.promise.nettygateway.threadpool.HttpThreadPool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

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

    private  ChannelHandlerContext requestCtx;
    NettyHttpClientOutboundHandler(ChannelHandlerContext ctx){
        this.requestCtx = ctx;
    }
   @Override
   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
     cause.printStackTrace();
     ctx.close();
   }

   @Override
   public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
     if ( msg instanceof HttpResponse) {
         HttpResponse response = (HttpResponse) msg;
         log.info("Content-type:{}",response.headers().get(CONTENT_TYPE));
     }

     if (msg instanceof HttpContent ) {
         HttpContent content = (HttpContent) msg;
         ByteBuf buf = content.content();
         log.info("接收到第三方服务返回数据:{}",buf.toString(CharsetUtil.UTF_8));
         // 拼接 返回的数据
         HttpThreadPool.responseRun(this.requestCtx,buf.toString(CharsetUtil.UTF_8));
     }
   }
}
