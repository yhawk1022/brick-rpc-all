package priv.brick.server;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : pangyan
 * @date : 2022/11/18
 * @description :
 */
@Slf4j
public class ServiceProviderImpl implements ServiceProvider{

    private static Map<String, Class<?>> serviceMap = new ConcurrentHashMap<>();

    private static Integer PORT = null;


    @Override
    public void addService(String... serviceNames) {
        List<String> serviceNameList = Lists.newArrayList(serviceNames);
        try {
            for (String serviceName : serviceNameList) {
                log.info("server load class {}", serviceName);
                Class<?> serviceClass = ClassLoader.getSystemClassLoader().loadClass(serviceName);
                Class<?>[] interfaces = serviceClass.getInterfaces();
                serviceMap.put(interfaces[0].getCanonicalName(), serviceClass);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<?> getService(String serviceName) {
        Class<?> serviceClazz = serviceMap.get(serviceName);
        if (serviceClazz == null){
            throw new RuntimeException(serviceName + "not found!");
        }
        return serviceClazz;
    }

    @Override
    public void setPort(int port) {
        PORT = port;
    }

    @Override
    public int getPort() {
        return PORT;
    }

    public static void main(String[] args) {
        ServiceProviderImpl serviceProvider = new ServiceProviderImpl();
        serviceProvider.addService("priv.brick.server.ServiceProviderImpl");
        System.out.println(serviceMap);
    }
}
