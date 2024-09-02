package com.heima.redis.config;

import com.google.protobuf.GeneratedMessageV3;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class ProtoBufRedisSerializer implements RedisSerializer<Object> {

    private static final Charset ASCII = Charset.forName("ASCII");

    // 用来标记类名长度的byte数组长度
    private static final int MARK_WORD_LENGTH = 4;
    private Class<byte[]> BYTE_ARRAY_CLASS = byte[].class;
    private byte[] EMPTY_BYTE = new byte[0];

    /**
     * 合并多个byte数组，采用System.arraycopy
     *
     * @param byte0 需合并的数组
     * @param bytes 变长的byte数组
     * @return 合并后的新数组
     */
    private static byte[] merge(byte[] byte0, byte[]... bytes) {
        if (bytes == null || bytes.length == 0) {
            return byte0;
        }
        int len = byte0.length;
        for (byte[] bytes1 : bytes) {
            len += bytes1.length;
        }
        byte[] combined = new byte[len];
        System.arraycopy(byte0, 0, combined, 0, byte0.length);
        int idx = byte0.length;
        for (byte[] bytes1 : bytes) {
            System.arraycopy(bytes1, 0, combined, idx, bytes1.length);
            idx += bytes1.length;
        }
        return combined;
    }

    @Override
    public byte[] serialize(java.lang.Object object) throws SerializationException {
        if (null == object || !(object instanceof GeneratedMessageV3)) {
            return EMPTY_BYTE;
        }
        byte[] result = ((GeneratedMessageV3) object).toByteArray();
        String className = object.getClass().getName();
        byte[] classNameBytes = className.getBytes(ASCII);
        int len = classNameBytes.length;
        byte[] markWord = ByteBuffer.allocate(MARK_WORD_LENGTH).putInt(len).array();
        return merge(markWord, classNameBytes, result);
    }

    @Override
    public java.lang.Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        int len = ByteBuffer.wrap(bytes, 0, MARK_WORD_LENGTH).getInt();
        byte[] classNameBytes = new byte[len];
        System.arraycopy(bytes, MARK_WORD_LENGTH, classNameBytes, 0, len);
        String className = new String(classNameBytes, ASCII);
        byte[] protoBufByte = new byte[bytes.length - len - MARK_WORD_LENGTH];
        System.arraycopy(bytes, MARK_WORD_LENGTH + len, protoBufByte, 0, protoBufByte.length);
        try {
            Class<?> clazz = Class.forName(className);
            if (GeneratedMessageV3.class.isAssignableFrom(clazz)) {
                Method method = clazz.getDeclaredMethod("parseFrom", BYTE_ARRAY_CLASS);
                if (method != null) {
                    method.setAccessible(true);
                    return method.invoke(null, (Object) protoBufByte);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}