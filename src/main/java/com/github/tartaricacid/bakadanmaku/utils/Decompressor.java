package com.github.tartaricacid.bakadanmaku.utils;


import com.github.tartaricacid.bakadanmaku.brotli.dec.BrotliInputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.Inflater;

public final class Decompressor {

    public interface DecompressMethod {
        byte[] decompress(byte[] data) throws Exception;
    }

    public static final DecompressMethod DECOMPRESS_ZLIB = data -> {
        byte[] output;

        Inflater inflater = new Inflater();
        inflater.reset();
        inflater.setInput(data);

        try (ByteArrayOutputStream o = new ByteArrayOutputStream(data.length)) {
            byte[] buf = new byte[1024];
            while (!inflater.finished()) {
                int i = inflater.inflate(buf);
                o.write(buf, 0, i);
            }
            output = o.toByteArray();
        } finally {
            inflater.end();
        }

        return output;
    };

    public static final DecompressMethod DECOMPRESS_BROTLI = data -> {
        try (
                BrotliInputStream bis = new BrotliInputStream(new ByteArrayInputStream(data));
                ByteArrayOutputStream bos = new ByteArrayOutputStream()
        ) {
            byte[] buf = new byte[1024];
            int len;
            while (-1 != (len = bis.read(buf))) {
                bos.write(buf, 0, len);
            }
            bos.flush();
            return bos.toByteArray();
        }
    };

    private static final DecompressMethod[] DECOMPRESS_METHODS = {
            DECOMPRESS_ZLIB,
            DECOMPRESS_BROTLI
    };

    /**
     * 根据现有的解压缩方法，尝试解压缩给予的字节数组。
     *
     * @param data 需要解压缩的字节数组。
     * @return 解压缩后的字节数组，若解压缩方法全部无效则返回原数组。
     */
    public static byte[] tryDecompress(byte[] data) {
        for (DecompressMethod method : DECOMPRESS_METHODS) {
            try {
                return method.decompress(data);
            } catch (Exception ignored) {

            }
        }

        // failed to decompress
        return data;
    }

    /**
     * 根据基于的解压缩方法去解压缩字节数组。
     *
     * @param data   要解压缩的字节数组。
     * @param method 要使用的方法。
     * @return 返回解压缩后的字节数组，若解压缩失败则返回原数组。
     */
    public static byte[] decompress(byte[] data, DecompressMethod method) {
        try {
            return method.decompress(data);
        } catch (Exception e) {
            return data;
        }
    }
}
