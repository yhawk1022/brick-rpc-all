package priv.brick.exception;

/**
 * @author : pangyan
 * @date : 2022/11/07
 * @description :
 */
public class BrickException extends RuntimeException{

    public BrickException(String msg, Throwable e){
        super(msg,e);
    }

    public BrickException(String msg){
        super(msg);
    }
}
