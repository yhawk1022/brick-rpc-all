package priv.brick.codec.compress;

/**
 * @author : pangyan
 * @date : 2022/11/19
 * @description :
 */
public interface Compressor {


    byte[] compress(byte[] bytes);

    byte[] decompress(byte[] bytes);
}
