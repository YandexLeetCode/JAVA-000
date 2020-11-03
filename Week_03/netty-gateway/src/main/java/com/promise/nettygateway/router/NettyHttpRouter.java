package com.promise.nettygateway.router;

import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by yandex on 2020/11/3.
 */
@Slf4j
public class NettyHttpRouter implements HttpEndpointRouter{

    /**
     * 此处 传递进来请求对象实例,等后面 需要拓展时候 再做处理
     * */
    private FullHttpRequest httpRequest;
    public NettyHttpRouter(FullHttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    /**
     * 根据 某种算法 再根据 请求的 header 信息 , 从 endpoints 中返回 需要访问的 后台 URL
     * @param endpoints
     * @return
     */
    @Override
    public String route(List<String> endpoints) {
        try {
            // 为了简单 此处我们 使用  随机 函数 随便获取,此时 测试 endpoints 中 都是相同的 请求地址
            if (!endpoints.isEmpty()) {
               int random_index = (int) (Math.random()*endpoints.size());
               log.info("经过 随机 路由 返回 请求地址序号为:{},对应地址为:{}",random_index,endpoints.get(random_index));
               return endpoints.get(random_index);
            }
        } catch (Exception ex) {
            log.error("NettyHttpRouter:route:" + ex);
        }

        return "";
    }
}
