package com.example.demo.sevice;

import com.example.demo.model.entity.File;
import com.example.demo.model.response.UploadFileResponse;
import com.example.demo.repository.FileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.apache.bcel.classfile.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

@Service
public class FileServiceImpl implements IFileService{

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private FileRepository repository;

    @Value("${storage.file.url}")
    private  String storageLocation;

    @Override
    public UploadFileResponse uploadFile(MultipartFile file) throws Exception {
        if (file == null || file.getOriginalFilename() == null) {
            throw new Exception("File is not null");
        }
        String fileName = StringUtils
                .cleanPath(Integer.valueOf(LocalDateTime.now().getNano()).toString())
                + ".png";
        try {
            // Check if the file's name contains invalid character
            if (fileName.contains("..")) {
                throw new Exception("Sorry! Filename contains invalid path sequence" + fileName);
            }

            //Copy file to the target location (Replacing existing  file with the same name)
            Path dir = Paths.get(storageLocation);
            Path targetLocation = dir.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            File newFile = new File()
                    .setName(fileName)
                    .setOriginalName(file.getOriginalFilename())
                    .setSize(file.getSize())
                    .setType(file.getContentType())
                    .setUrl(storageLocation + fileName);
            repository.save(newFile);
            return mapper.convertValue(newFile, UploadFileResponse.class);
        } catch (IOException ex) {
            throw new Exception("Could not store file" + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public Resource loadFile(Long id) throws Exception {
        try {
            File file = repository.findById(id).orElseThrow(() ->
                    new Exception("File is not found with id: " + id)
            );
            Path filePath = Paths.get(storageLocation).resolve(file.getName()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new Exception("File not found " + file.getName());
            }
        } catch (Exception e) {
            throw new Exception("Load file fail");
        }
    }
}
