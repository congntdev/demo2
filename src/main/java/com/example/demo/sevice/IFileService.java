package com.example.demo.sevice;

import com.example.demo.model.response.UploadFileResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {

    UploadFileResponse uploadFile(MultipartFile file) throws  Exception;

    Resource loadFile(Long id) throws Exception;
}
