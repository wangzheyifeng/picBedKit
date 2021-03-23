package com.tm.controller;


import com.tm.bean.CommonResult;
import com.tm.service.UploadFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


@RestController
public class UploadImageController {

    @Value("${user.key}")
    private String key;

    @Value("${storage.path}")
    private String storagePath;

    @Value("${browse.path}")
    private String browsePath;

    @Resource
    UploadFileService uploadFileService;

    @RequestMapping("/upload/images")
    public CommonResult uploadImage(@RequestParam(value = "file") MultipartFile file, String key) throws IOException {
        Map<String, Object> result = new HashMap<>();
        if(!isKeyRight(key)) {
            return new CommonResult(403,"key验证失败",null);
        }
        try{
            String fileName = file.getOriginalFilename();
            Path storageFullPath = Paths.get(this.storagePath, fileName);
            File localFile = new File(storageFullPath.toString());
            if(!localFile.exists()) {
                localFile.createNewFile();
            }
            file.transferTo(localFile);
            result.put("path",browsePath+"/"+fileName);
            return new CommonResult(200,"上传成功",result);
        }catch (Exception e){
            return new CommonResult(500,"上传失败",null);
        }
    }

    @RequestMapping("/upload/base64")
    public CommonResult uploadBase64File(String image, String fileName, String key) throws IOException {
        if(!isKeyRight(key)) {
            return new CommonResult(403,"key验证失败",null);
        }
        return uploadFileService.uploadBase64File(image, fileName);
    }

    private Boolean isKeyRight(String key) {
        return this.key.equals(key);
    }
}
