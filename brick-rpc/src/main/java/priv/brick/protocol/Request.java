package priv.brick.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : pangyan
 * @date : 2022/11/06
 * @description :
 */
@Data
public class Request implements Serializable {

    private String requestId;

    private String interfaceName;

    private String methodName;

    private Object[] parameters;

    private Class<?>[] paramTypes;
}
