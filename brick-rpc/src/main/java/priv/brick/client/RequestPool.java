package priv.brick.client;

import priv.brick.protocol.Response;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : pangyan
 * @date : 2022/11/13
 * @description :
 */
public class RequestPool {

    private static final Map<String, CompletableFuture<Response>> POOL = new ConcurrentHashMap<>();

    public void put(String requestId, CompletableFuture<Response> future) {
        POOL.put(requestId, future);
    }

    public void complete(Response Response) {
        CompletableFuture<Response> future = POOL.remove(Response.getRequestId());
        if (null != future) {
            future.complete(Response);
        } else {
            throw new IllegalStateException();
        }
    }
}
