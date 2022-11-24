package priv.brick.proxy;

import lombok.extern.slf4j.Slf4j;
import priv.brick.client.NettyClient;
import priv.brick.protocol.Request;
import priv.brick.protocol.Response;
import priv.brick.util.SingletonFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author : pangyan
 * @date : 2022/11/24
 * @description : 2022/11/05
 */
@Slf4j
public class JDKProxyHandler implements InvocationHandler {

    private final NettyClient client = SingletonFactory.getInstance(NettyClient.class);

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("invoked method: [{}]", method.getName());
        Request request = new Request();
        request.setRequestId(UUID.randomUUID().toString());
        request.setInterfaceName(method.getDeclaringClass().getCanonicalName());
        request.setMethodName(method.getName());
        request.setParameters(args);
        request.setParamTypes(method.getParameterTypes());

        CompletableFuture<Response<Object>> completableFuture = (CompletableFuture<Response<Object>>) client.sendRequest(request);
        Response<Object> rpcResponse = completableFuture.get();
        return rpcResponse.getData();
    }
}
