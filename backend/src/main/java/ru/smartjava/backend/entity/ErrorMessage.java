package ru.smartjava.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
public class ErrorMessage {

    Integer id;
    String message;
}
