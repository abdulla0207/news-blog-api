package com.company.controller;

import com.company.service.AttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/attach")
public class AttachController {

    private final AttachService attachService;

    @Autowired
    public AttachController(AttachService attachService){
        this.attachService=attachService;
    }

    @PostMapping("/")
    public ResponseEntity<?> saveFile(@RequestParam("file") MultipartFile multipartFile){
        String response = attachService.saveFile(multipartFile);

        return ResponseEntity.ok(response);
    }
}
