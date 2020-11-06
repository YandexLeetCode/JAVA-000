package com.promise.nettygateway.outbound;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

@Slf4j
public class ChannelTaskThread implements Callable<String> {
    final NettyHttpClientPool nettyHttpClientPool = NettyHttpClientPool.getInstance();
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

        NettyHttpClientHand

    }
}
