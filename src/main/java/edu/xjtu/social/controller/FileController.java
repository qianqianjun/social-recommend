package edu.xjtu.social.controller;

import edu.xjtu.social.domain.util.ResponseInfo;
import edu.xjtu.social.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileController {
    @Autowired
    FileService fileService;

    @PostMapping("/upload")
    @ResponseBody
    public ResponseInfo upload(@RequestParam("file") MultipartFile file){
        try{
            return new ResponseInfo("上传成功",true,fileService.storeFile(file));
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseInfo("上传失败",false,null);
    }
}
