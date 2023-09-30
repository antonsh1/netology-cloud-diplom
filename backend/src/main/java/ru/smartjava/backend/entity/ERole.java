package ru.smartjava.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@Entity
public class ERole {

    private enum EnumRole {
        UPLOAD,
        DOWNLOAD,
        RENAME,
        DELETE

    }

    @Id
    @Enumerated(EnumType.STRING)
    @Size(max = 100)
//    @Column(length = 20, unique = true)
    private EnumRole name;

    public String getName() {
        return name.toString();
    }
}
