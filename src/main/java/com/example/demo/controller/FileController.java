package com.example.demo.controller;

import com.example.demo.model.entity.File;
import com.example.demo.model.response.UploadFileResponse;
import com.example.demo.sevice.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private IFileService service;

    @PostMapping
    ResponseEntity<UploadFileResponse> upload(@RequestParam("file")MultipartFile file) throws Exception{
        return ResponseEntity.ok(service.uploadFile(file));
    }

    @GetMapping("/{id}")
    ResponseEntity<Resource> getFile(@PathVariable Long id, HttpServletRequest request) throws Exception {
        Resource resource = service.loadFile(id);
        String contentType = null;
        try {
            contentType = request.getServletContext()
                    .getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ignored) {

        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                + resource.getFilename() + "\"")
                                .body(resource);
    }
}
