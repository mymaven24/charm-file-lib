package com.swwx.charm.file;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.swwx.charm.file.exception.*;
import com.swwx.charm.file.util.MyConfig;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by whl on 15/12/17.
 */
public class FileStorageAPI {

    private final static Logger log = LoggerFactory.getLogger(FileStorageAPI.class);

    public static String uploadFileTemporary(UploadParam uploadDTO)
            throws FilenameIsBlankException, UploadException,
            FileSuffixIsBlankException {
        return upload(uploadDTO, MyConfig.getTmpAttachmentBucketName());
    }

    public static String uploadFile(UploadParam uploadDTO)
            throws FilenameIsBlankException, UploadException, FileSuffixIsBlankException {
        return upload(uploadDTO, MyConfig.getBucketName());
    }

    private static String upload(UploadParam uploadDTO, String bucketName) {
        if (StringUtils.isBlank(uploadDTO.getFileName())) {
            throw new FilenameIsBlankException();
        }

        if (StringUtils.indexOf(uploadDTO.getFileName(), ".") == -1) {
            throw new FileSuffixIsBlankException();
        }

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(uploadDTO.getFileLength());

            PutObjectResult result = createOSSClient().putObject(bucketName,
                    uploadDTO.getFileName(),
                    new ByteArrayInputStream(uploadDTO.getInput()), metadata);

            log.info("上传文件的名称为：{}，OSS返回文件ETag为：{}", uploadDTO.getFileName(),
                    result.getETag());

        } catch (Exception e) {
            log.error("upload file "+uploadDTO.getFileName()+" to "+bucketName+" failed.", e);
            throw new UploadException();
        }

        return uploadDTO.getFileName();
    }

    public static byte[] downloadFile(String fileName) throws FileUrlIsBlankException, GetFileFailedException {
        return download(fileName, MyConfig.getBucketName());
    }

    public static byte[] downloadTemporaryFile(String fileName)
            throws FileUrlIsBlankException, GetFileFailedException {
        return download(fileName, MyConfig.getTmpAttachmentBucketName());
    }

    private static byte[] download(String fileName, String bucketName) {
        byte[] bytes = new byte[0];
        OSSObject obj = null;
        InputStream is = null;
        try {
            obj = createOSSClient().getObject(bucketName, fileName);

            is = obj.getObjectContent();

            bytes = IOUtils.toByteArray(is);

        } catch (Exception e) {
            log.error("down file " + fileName + " from " + bucketName + "failed.", e);
            throw new GetFileFailedException();
        } finally {
            IOUtils.closeQuietly(is);
        }

        return bytes;
    }

    private static OSSClient createOSSClient() {
        return new OSSClient(MyConfig.getEndPoint(), MyConfig.getAccessKeyId(),
                MyConfig.getAccessKeySecret());
    }

    public static boolean exists(String fileName) throws GetFileInfoFailedException {
        return exists(fileName,MyConfig.getBucketName());
    }

    public static boolean existsTemporaryFile(String fileName)
            throws GetFileInfoFailedException {
        return exists(fileName,MyConfig.getTmpAttachmentBucketName());
    }

    public static boolean exists(String fileName, String bucketName)
            throws GetFileInfoFailedException {

        try {
            createOSSClient().getObjectMetadata(bucketName, fileName);
        } catch (Exception e) {
            if (e instanceof OSSException) {
                OSSException oe = (OSSException) e;
                if (StringUtils.equals("NoSuchKey", oe.getErrorCode())) {
                    return false;
                } else {
                    exception(e);
                }
            } else {
                exception(e);
            }
        }

        return true;
    }

    private static void exception(Exception e) throws GetFileInfoFailedException {
        e.printStackTrace();
        log.error("从OSS上获取文件信息失败！", e);
        throw new GetFileInfoFailedException();
    }
}
