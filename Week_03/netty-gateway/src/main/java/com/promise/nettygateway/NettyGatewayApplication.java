package com.promise.nettygateway;

import com.promise.nettygateway.constant.HttpGateWayConstant;
import com.promise.nettygateway.inbound.HttpInboundServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@Slf4j
@SpringBootApplication
public class NettyGatewayApplication implements ApplicationRunner {


    public static void main(String[] args) {
        SpringApplication.run(NettyGatewayApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String proxyPort = System.getProperty("proxyPort","8888");

        /**
         * http://localhost:8088/api/hello ==> 真实的 URL
         * http://localhost:8888/api/hello ==> 对外虚拟 URL
         */

        int port = Integer.parseInt(proxyPort);
        log.info("{} {} starting...", HttpGateWayConstant.GATEWAY_NAME,HttpGateWayConstant.GATEWAY_VERSION);
        HttpInboundServer server = new HttpInboundServer();
        try {
            server.run();
        } catch (Exception ex) {
            log.error("HttpInboundServer run failed : " + ex);
        }
    }
}
