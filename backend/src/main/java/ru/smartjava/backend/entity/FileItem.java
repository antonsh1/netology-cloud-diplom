package ru.smartjava.backend.entity;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class FileItem {

    String filename;
    Integer size;
}
