package com.photograph_u.util;

import com.photograph_u.exception.MapToBeanConvertException;
import com.photograph_u.exception.Md5ContentException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class CommUtils {

    private static ThreadLocal<DateFormat> dateFormatThreadLocal = new ThreadLocal<>();

    //获取线程安全的日期与时间格式
    public static DateFormat getDateTimeFormat(String format) {
        DateFormat dateFormat = dateFormatThreadLocal.get();
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat(format);
            dateFormatThreadLocal.set(dateFormat);
        }
        return dateFormat;
    }

    //获取线程安全的日期时间
    public static String getDateTime() {
        return getDateTimeFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    //将下划线的名字换成驼峰
    public static String changeUnderlineToHumpName(String name) {
        StringBuilder resultStr = new StringBuilder();
        String[] words = name.split("_");
        resultStr.append(words[0]);
        for (int i = 1; i < words.length; i++) {
            String word = words[i].substring(0, 1).toUpperCase() + words[i].substring(1);
            resultStr.append(word);
        }
        return resultStr.toString();
    }

    //将Map转化成Bean
    public static <T, V> T convertMapToBean(Map<String, V> map, Class<T> clazz) {
        class MyConverter implements Converter {
            @Override
            public <T> T convert(Class<T> aClass, Object o) {
                if ((o == null) || ("".equals(o.toString().trim()))) {
                    return null;
                }
                try {
                    return (T) getDateTimeFormat("yyyy-MM-dd").parse(o.toString());
                } catch (ParseException e) {
                    throw new MapToBeanConvertException("MapToBean转化出错");
                }
            }
        }
        ConvertUtils.register(new MyConverter(), Date.class);
        try {
            T object = clazz.newInstance();
            for (Map.Entry<String, V> entry : map.entrySet()) {
                String key = entry.getKey();
                V value = entry.getValue();
                BeanUtils.setProperty(object, CommUtils.changeUnderlineToHumpName(key), value);
            }
            return object;
        } catch (Exception e) {
            throw new MapToBeanConvertException("MapToBean转化出错", e);
        }
    }

    //md5加密
    public static String getMd5Content(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(content.getBytes());
            StringBuffer buffer = new StringBuffer();
            for (byte b : result) {
                int number = b & 0xff;
                String hex = Integer.toHexString(number);
                if (hex.length() == 1) {
                    buffer.append(0);
                }
                buffer.append(hex);
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new Md5ContentException("md5加密时出错", e);
        }
    }

    public static String getUuidCode() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
