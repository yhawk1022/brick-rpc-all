package priv.brick.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : pangyan
 * @date : 2022/11/05
 * @description :
 */
@Getter
@AllArgsConstructor
public enum CompressSupport {
    GZIP((byte) 1);

    byte code;
}
