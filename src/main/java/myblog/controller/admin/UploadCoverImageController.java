package myblog.controller.admin;

import lombok.extern.slf4j.Slf4j;
import myblog.config.Constants;
import myblog.util.Result;
import myblog.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author FANG
 */
@Controller
@RequestMapping("/admin")
@Slf4j(topic = "UploadCoverImageController")
public class UploadCoverImageController {

    @RequestMapping(value = "/upload/file", method = RequestMethod.POST)
    @ResponseBody
    public Result uploadCoverImage(@RequestParam("file") MultipartFile file,
                                   HttpServletRequest request) throws URISyntaxException, MalformedURLException {
        if (file.isEmpty()) {
            return ResultGenerator.genFailResult("该文件为空文件");
        }
        // 获取带后缀名的 原文件名 --> 例如：book.txt
        String originalFileName = file.getOriginalFilename();
        String suffixName = null;
        if (originalFileName.contains(".")) {
            suffixName = originalFileName.substring(originalFileName.lastIndexOf("."));
        } else {
            suffixName = "";
        }
        // 生成通用文件名
        Random r = new Random();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
        StringBuilder tempFileName = new StringBuilder();
        tempFileName.append(simpleDateFormat.format(new Date())).append(r.nextInt(100)).append(suffixName);
        String newFileName = tempFileName.toString();
        File fileDirectory = new File(Constants.FILE_UPLOAD_DICTIONARY);
        File destFile = new File(Constants.FILE_UPLOAD_DICTIONARY + "\\" + newFileName);
        try {
            if (!fileDirectory.exists()) {
                if (!fileDirectory.mkdirs()) {
                    throw new IOException("文件夹创建失败,路径为：" + fileDirectory);
                }
            }
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Result result = ResultGenerator.genSuccessResult();
        // URI可能出错 即 返回的请求路径可能出现问题
        result.setData("/coverImg/" + newFileName);
        return result;
    }
}
