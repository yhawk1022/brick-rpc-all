package priv.brick.codec.compress;

import priv.brick.exception.BrickException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @date : 2022/11/20
 * @author : pangyan
 * @description :
 */
public class GzipCompressor implements Compressor{


    @Override
    public byte[] compress(byte[] bytes) {
        if (bytes == null) {
            throw new BrickException("bytes is null");
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(out)) {
            gzip.write(bytes);
            gzip.flush();
            gzip.finish();
            return out.toByteArray();
        } catch (IOException e) {
            throw new BrickException("gzip compress error", e);
        }
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        if (bytes == null) {
            throw new BrickException("bytes is null");
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             GZIPInputStream gunzip = new GZIPInputStream(new ByteArrayInputStream(bytes))) {
            byte[] buffer = new byte[4096];
            int n;
            while ((n = gunzip.read(buffer)) > -1) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new BrickException("gzip decompress error", e);
        }
    }

    public static void main(String[] args) {
        GzipCompressor gzipCompressor = new GzipCompressor();
        String s = "GzipCompressorGzipCompressorGzipCompressorGzipCompressorGzipCompressorGzipCompressorGzipCompressorGzipCompressorGzipCompressor";
        System.out.println(s.getBytes().length);

        byte[] compress = gzipCompressor.compress(s.getBytes(StandardCharsets.UTF_8));
        System.out.println(compress.length);

        byte[] decompress = gzipCompressor.decompress(compress);
        System.out.println(decompress.length);
        System.out.println(new String(decompress));
    }
}
