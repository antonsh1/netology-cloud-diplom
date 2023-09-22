package ru.smartjava.backend.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenMessage {

    @SerializedName("auth-token")
    String authtoken;
}
