package com.promise.nettygateway.router;

import java.util.List;

/**
 * Created by yandex on 2020/11/1.
 */
public interface HttpEndpointRouter {
    String route(List<String> endpoints);
}
