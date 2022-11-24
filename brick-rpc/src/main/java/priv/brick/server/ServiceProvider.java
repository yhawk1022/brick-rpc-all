package priv.brick.server;

/**
 * @author : pangyan
 * @date : 2022/11/15
 * @description :
 */
public interface ServiceProvider {

    void addService(String... serviceNames);

    Class<?> getService(String serviceName);

    void setPort(int port);

    int getPort();
}
