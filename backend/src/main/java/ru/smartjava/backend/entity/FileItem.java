package ru.smartjava.backend.entity;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class FileItem {

    String filename;
    Integer size;
}
