package com.example.demo.model.response;

import lombok.Data;

@Data
public class UploadFileResponse {
    private Long id;
    private String name;
    private String originalName;
    private String url;
    private Long size;
    private String type;
}
