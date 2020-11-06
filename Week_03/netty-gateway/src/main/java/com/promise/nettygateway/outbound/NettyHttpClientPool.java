package com.promise.nettygateway.outbound;

import com.promise.nettygateway.constant.HttpGateWayConstant;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;


/**
 * Netty 连接池
 * Created by yandex on 2020/11/5.
 */
@Slf4j
public class NettyHttpClientPool {

    /** volatile 保证 线程之间 可见 单例模式*/
    volatile private static NettyHttpClientPool nettyHttpClientPool;

    /** Key 是连接 后台服务的 地址 */
    public ChannelPoolMap<URI, FixedChannelPool> httpClientPoolMap;

    final EventLoopGroup workerGroup = new NioEventLoopGroup();
    final Bootstrap bootstrap = new Bootstrap();

    /** 此处 因为后台服务只有一个,所以 这里面  就定死 只有一个后台请求地址, 反正 这里面是 map 后面有不同请求地址,就直接循环 增加到对应map中即可*/
    private static List<String> urls = Arrays.asList("http://localhost:8808/test");

    /** 请求地址与连接池 对应的 map */
    volatile private static Map<URI, FixedChannelPool> pools = new ConcurrentHashMap<>(2);

    /** 存放 请求列表的 数组 */
    volatile private static List<URI> uriList;

    /** 构造函数 首先初始化 连接池 */
    private NettyHttpClientPool() {
        build();
    }

    /**
     *   获取连接池的实例
     * @return
     */
    public static NettyHttpClientPool getInstance() {
        if (nettyHttpClientPool == null) {
            synchronized (NettyHttpClientPool.class) {
                if (nettyHttpClientPool == null) {
                    nettyHttpClientPool = new NettyHttpClientPool();
                }
            }
        }
    }
    /**
     *  初始化 连接池
     */
    public void build() {
        log.info("NettyHttpClientPool 初始化...");
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true);
        /** 实例化 Channel Pool */
        httpClientPoolMap = new AbstractChannelPoolMap<URI, FixedChannelPool>() {

            @Override
            protected FixedChannelPool newPool(URI uri) {
                return new FixedChannelPool(bootstrap.remoteAddress(uri.getHost(),uri.getPort()), new NettyChannelPoolHandler(), HttpGateWayConstant.MAX_CONNECTIONS);
            }
        };
        /** 解析 服务端请求列表 */
        getServerAddress(urls);

        /** 将 请求地址与 连接池 绑定*/
        for (URI uri : uriList){
            pools.put(uri, httpClientPoolMap.get(uri));
        }
    }

    /**
     * 根据随机数 获取 channel,给每个Channel 设置随机值标签
     * @param random
     * @return
     */
    public Channel getChannel(long random) {
        int retry  = 0;
        Channel channel = null;
        try {
           Long poolIndex = random % pools.size();
           URI uri = uriList.get(poolIndex.intValue());
           FixedChannelPool pool = pools.get(uri);
           Future<Channel> future = pool.acquire();
           channel = future.get();
           AttributeKey<Long> randomID = AttributeKey.valueOf(HttpGateWayConstant.RANDOM_KEY);
           channel.attr(randomID).set(random);
        } catch (ExecutionException ex) {
            log.error("Get Future Failed : " + ex.getMessage());
            /** 每个池 尝试获取2次 */
            int count = 2;
            if (retry < uriList.size() * count) {
                retry++;
                return getChannel(++random);
            } else {
                log.error("经过2次尝试,没有获取到channel连接的server, server list [{}]",uriList);
                throw new RuntimeException("没有可以获取到channel连接的server");
            }
        }catch (InterruptedException ex) {
            log.error("getChannel:InterruptedException",ex);
        }
        return channel;
    }

    /**
     * 回收 channel ,要保证 释放 获取的 随机值 与 getChannel 随机值是同一个,才能从同一个 pool 中释放资源
     * @param channel
     */
    public static void release(Channel channel) {
        long random = channel.attr(AttributeKey.<Long>valueOf(HttpGateWayConstant.RANDOM_KEY)).get();
        channel.flush();
        Long poolIndex = random % pools.size();
        pools.get(uriList.get(poolIndex.intValue())).release(channel);
    }

    /**
     * 获取线程池的hash 值
     * @param channel
     * @return
     */
    public static  int getPoolHash(Channel channel) {
        long random = channel.attr(AttributeKey.<Long>valueOf(HttpGateWayConstant.RANDOM_KEY)).get();
        Long poolIndex = random % pools.size();
        return System.identityHashCode(pools.get(uriList.get(poolIndex.intValue())));
    }
    /**
     * 根据 后端请求的 urls 将其分别存入 连接 数组中
     * @param urls
     */
   public void getServerAddress(List<String> urls) {
        if (urls.isEmpty()) {
            throw new RuntimeException(" Server Connection Url 为空");
        }
        for (String url : urls) {
            try {
                URI uri = new URI(url);
                uriList.add(uri);
            } catch (URISyntaxException e) {
               throw new RuntimeException(e);
            }
        }
   }

}
