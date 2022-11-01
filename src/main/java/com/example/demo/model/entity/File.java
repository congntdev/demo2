package com.example.demo.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Table
@Entity
@Data
@Accessors(chain = true)
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String originalName;
    private String url;
    private Long size;
    private String type;
}
