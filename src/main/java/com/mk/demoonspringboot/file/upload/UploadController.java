package com.mk.demoonspringboot.file.upload;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.druid.support.json.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.*;

@Slf4j
@RestController("上传控制器")
public class UploadController {

    @PostMapping(value = "/single")
    public ResponseEntity<String> singleUpload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("file receive {}", file.getOriginalFilename());

        // 检查文件内容是否为空
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("no file input");
        }

        // 原始文件名
        String fileName = file.getOriginalFilename();
        log.info("filename {}", fileName);
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<Map<String,Object>> all = reader.readAll();
        all.forEach(map -> {
            log.info(JSONUtils.toJSONString(map));
        });
        return ResponseEntity.ok("");

    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        Set<String> set = new HashSet<>();
        set.add("c");
        set.add("d");
        list.retainAll(set);
        System.out.println(list);
    }
}
