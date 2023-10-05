package ru.smartjava.backend.entity;

import lombok.*;

@AllArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class FileItem {

    String filename;
    Integer size;
}
