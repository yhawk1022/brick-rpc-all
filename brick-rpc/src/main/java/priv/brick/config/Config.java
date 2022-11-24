package priv.brick.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import priv.brick.client.LocalConfigDiscovery;
import priv.brick.client.ServiceDiscovery;
import priv.brick.exception.BrickException;
import priv.brick.server.ServiceProvider;
import priv.brick.server.ServiceProviderImpl;
import priv.brick.util.SingletonFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;

/**
 * @author : pangyan
 * @date : 2022/11/20
 * @description :
 */
@Slf4j
public class Config {

    static ServiceProvider serviceProvider;
    static ServiceDiscovery serviceDiscovery;

    static {
        serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
        serviceDiscovery = SingletonFactory.getInstance(LocalConfigDiscovery.class);
    }
    private static boolean isInit = false;

    public static void init(){
        URL resource = Thread.currentThread().getContextClassLoader().getResource("brick_config.json");
        if (resource == null){
            throw new BrickException("未发现brick_config.json文件");
        }

        init(resource.getPath());
    }

    public static void init(String configPath){
        if (isInit){
            log.error("config has been config");
            return;
        }
        String config;
        try {
            config = FileUtils.readFileToString(new File(configPath));
        } catch (IOException e) {
            throw new BrickException("读取brick_config.json文件异常");
        }
        JSONObject configJSON = JSON.parseObject(config);

        JSONArray remoteServices = configJSON.getJSONObject("clientConfig").getJSONArray("services");
        if (remoteServices != null){
            for (Object server : remoteServices) {
                JSONObject jsonObject = (JSONObject)JSON.toJSON(server);
                serviceDiscovery.registry(jsonObject.getString("service"),
                        new InetSocketAddress(jsonObject.getString("host"),jsonObject.getInteger("port")));
            }
            log.info("client config ready!");
        }

        JSONObject serverConfig = configJSON.getJSONObject("serverConfig");
        if (serverConfig!=null){
            String[] services = serverConfig.getJSONArray("services").toArray(String.class);
            serviceProvider.addService(services);
            serviceProvider.setPort(serverConfig.getInteger("port"));
            log.info("server config ready!");
        }
    }



}
