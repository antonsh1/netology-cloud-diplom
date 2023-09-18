package ru.smartjava.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;


public enum EnumRole {
    UPLOAD,
    DOWNLOAD,
    RENAME;

}
