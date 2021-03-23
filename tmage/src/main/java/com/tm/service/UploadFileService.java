package com.tm.service;

import com.tm.bean.CommonResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UploadFileService {

    @Value("${user.key}")
    private String key;

    @Value("${storage.path}")
    private String storagePath;

    public CommonResult uploadBase64File(String base64File, String fileName) throws IOException {

        Path storageFullPath = Paths.get(this.storagePath, fileName);
        File localFile = new File(storageFullPath.toString());
        if(!localFile.exists()) {
            localFile.createNewFile();
        }
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        byte[] b = null;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            b = decoder.decodeBuffer(replaceEnter(base64File));
            fos = new FileOutputStream(localFile);
            bos = new BufferedOutputStream(fos);
            bos.write(b);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new CommonResult(200,"上传成功！",null);
    }
    public static String replaceEnter(String str){
        String reg ="[\n-\r]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }
}
