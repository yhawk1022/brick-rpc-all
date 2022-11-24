package priv.brick;

import priv.brick.config.Config;
import priv.brick.server.NettyServer;
import priv.brick.util.SingletonFactory;

/**
 * @author : pangyan
 * @date : 2022/11/05
 * @description :
 */
public class ServerTest {

    public static void main(String[] args) {
        Config.init();
        NettyServer server = SingletonFactory.getInstance(NettyServer.class);
        server.start();
    }
}
