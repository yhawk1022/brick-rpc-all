package priv.brick;

import priv.brick.client.NettyClient;
import priv.brick.config.Config;
import priv.brick.server.NettyServer;

/**
 * @author : pangyan
 * @date : 2022/11/05
 * @description :
 */
public class Application {

    public static void main(String[] args) {
        Config.init();
        new NettyServer().start();
    }
}
