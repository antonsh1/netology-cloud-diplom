package ru.smartjava.backend.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FileToRename {

    @NotNull
    @NotBlank
    String filename;
}
