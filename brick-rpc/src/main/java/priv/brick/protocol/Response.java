package priv.brick.protocol;

import lombok.Data;
import priv.brick.common.ResponseCodeEnum;

import java.io.Serializable;

/**
 * @author : pangyan
 * @date : 2022/11/06
 * @description :
 */
@Data
public class Response<T> implements Serializable {

    private String requestId;

    private Integer code;

    private String message;

    private T data;


    public static <T> Response<T> success(String requestId, T data) {
        Response<T> response = new Response<>();
        response.setRequestId(requestId);
        response.setCode(ResponseCodeEnum.SUCCESS.getCode());
        response.setMessage(ResponseCodeEnum.SUCCESS.getMessage());
        if (null != data) {
            response.setData(data);
        }
        return response;
    }

    public static <T> Response<T> fail() {
        Response<T> response = new Response<>();
        response.setCode(ResponseCodeEnum.FAILURE.getCode());
        response.setMessage(ResponseCodeEnum.FAILURE.getMessage());
        return response;
    }
}
