package priv.brick.client;

import io.netty.channel.Channel;
import lombok.Data;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : pangyan
 * @date : 2022/11/06
 * @description :
 */
@Data
public class ChannelPool {

    private Map<String, Channel> pool;

    private static volatile ChannelPool INSTANCE;

    public ChannelPool(){
        pool = new ConcurrentHashMap<>();
    }

    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        Channel channel = pool.get(key);
        if (channel != null && channel.isActive()) {
            return channel;
        } else {
            pool.remove(key);
        }
        return null;
    }

    public void putChannel(InetSocketAddress inetSocketAddress, Channel channel) {
        String key = inetSocketAddress.toString();
        pool.put(key, channel);
    }


}
