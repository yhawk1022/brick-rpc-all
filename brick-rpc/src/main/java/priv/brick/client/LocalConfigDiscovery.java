package priv.brick.client;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.checkerframework.checker.units.qual.C;
import priv.brick.protocol.Request;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : pangyan
 * @date : 2022/11/11
 * @description :
 */
@Slf4j
public class LocalConfigDiscovery implements  ServiceDiscovery{

    public static Map<String, InetSocketAddress> connectMap = new ConcurrentHashMap<>();


    @Override
    public InetSocketAddress discovery(Request request) {
        return connectMap.get(request.getInterfaceName());
    }

    @Override
    public void registry(String service, InetSocketAddress inetSocketAddress) {
        connectMap.putIfAbsent(service, inetSocketAddress);
    }

}
