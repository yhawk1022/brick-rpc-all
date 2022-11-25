package priv.brick.proxy;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : pangyan
 * @date :  2022/11/05
 * @description :
 */
public class ProxyFactory {

    private static Map<String, Object> proxyCache = new ConcurrentHashMap<>();


    public static <T> T proxy(Class clazz) {
        String canonicalName = clazz.getCanonicalName();
        Object proxy = null;
        if (proxyCache.containsKey(canonicalName)) {
            proxy = proxyCache.get(canonicalName);
        }
        if (proxy == null) {
            proxy = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, new JDKProxyHandler());
            if (proxy != null) {
                proxyCache.put(canonicalName, proxy);
            }
        }
        return (T)proxy;
    }

}
