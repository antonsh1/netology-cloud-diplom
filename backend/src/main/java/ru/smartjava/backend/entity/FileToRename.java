package ru.smartjava.backend.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class FileToRename {

    @NotNull
    @NotBlank
    private String filename;
}
