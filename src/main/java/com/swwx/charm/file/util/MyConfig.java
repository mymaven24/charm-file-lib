package com.swwx.charm.file.util;

import com.swwx.charm.file.constant.MyConstant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by whl on 15/12/17.
 */
public class MyConfig {

    private static Properties prop = new Properties();

    private static final String CONFIG_FILE = "config.properties";

    static {

        InputStream io = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE);

        try {
            prop.load(io);
        } catch (IOException e) {
            System.err.println("load config from " + CONFIG_FILE + " failed.");
            e.printStackTrace();
        }
    }

    public static String getString(String key) {
        return prop.getProperty(key);
    }

    public static String getAccessKeyId() {
        return System.getenv(MyConstant.accessKeyId);
    }

    public static String getAccessKeySecret() {
        return System.getenv(MyConstant.accessKeySecret);
    }

    public static String getEndPoint() {
        return System.getenv(MyConstant.endPoint) == null ? System.getProperty(MyConstant.endPoint) :
                System.getenv(MyConstant.endPoint);
    }

    public static String getBucketName() {
        return System.getenv(MyConstant.bucketName) == null ? System.getProperty(MyConstant.bucketName) :
                System.getenv(MyConstant.bucketName);
    }

    public static String getTmpAttachmentBucketName() {
        return System.getenv(MyConstant.tmpAttachmentBucketName) == null ?
                System.getProperty(MyConstant.tmpAttachmentBucketName) :
                System.getenv(MyConstant.tmpAttachmentBucketName);
    }
}
