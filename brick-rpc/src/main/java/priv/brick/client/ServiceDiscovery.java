package priv.brick.client;

import priv.brick.protocol.Request;

import java.net.InetSocketAddress;

/**
 * @author : pangyan
 * @date : 2022/11/14
 * @description :
 */
public interface ServiceDiscovery {

    InetSocketAddress discovery(Request request);


    void registry(String service, InetSocketAddress inetSocketAddress);

}
