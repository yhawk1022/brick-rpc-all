package priv.brick.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : pangyan
 * @date : 2022/11/05
 * @description :
 */
@AllArgsConstructor
@Getter
public enum SerializeSupport {

    Protostuff((byte) 1);

    byte code;
}
