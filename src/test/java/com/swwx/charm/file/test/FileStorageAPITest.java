package com.swwx.charm.file.test;

import com.swwx.charm.file.FileStorageAPI;
import com.swwx.charm.file.UploadParam;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;

/**
 * Created by whl on 15/12/17.
 */
public class FileStorageAPITest {

    @BeforeClass
    public static void beforeClass(){


        System.setProperty("FILE_SERVICE_OSS_BUCKETNAME", "zhulebei-test");
        System.setProperty("FILE_SERVICE_OSS_ENDPOINT", "http://oss-cn-beijing.aliyuncs.com");
        System.setProperty("FILE_SERVICE_OSS_TMP_ATTACHMENT_BUCKETNAME", "zhulebei-tmp-dev");
    }

    @Test
    public void testUpload(){

        UploadParam param = new UploadParam();

        param.setFileName("lianlianpay.html");
        param.setFileLength(100);
        param.setInput(getBytes("/Users/whl/Desktop/lianlianpay.html"));

        FileStorageAPI.uploadFile(param);

    }


    @Test
    public void testGet(){
        byte[] data = FileStorageAPI.downloadFile("lianlianpay.html");

        getFile(data, "/Users/whl/Desktop/abc123.html");
    }


    private static byte[] getBytes(String filePath){
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static void getFile(byte[] bfile, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            file = new File(fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
