package priv.brick.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : pangyan
 * @date : 2022/11/12
 * @description :
 */
@Getter
@AllArgsConstructor
public enum ResponseCodeEnum {

    SUCCESS(200, "SUCCESS"),
    FAILURE(500, "FAILURE");
    private final int code;
    private final String message;
}
