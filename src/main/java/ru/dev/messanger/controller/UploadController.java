package ru.dev.messanger.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.UUID;

@Controller
public class UploadController {

    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping("/upload")
    public String upload(
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        String resultFilename = "";

        if (file != null && !file.getOriginalFilename().isEmpty()) {

            String uuidFile = UUID.randomUUID().toString();
            resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "\\" + resultFilename));

        }

        return  new Gson().toJson(new String[]{"true", resultFilename});
    }

}
