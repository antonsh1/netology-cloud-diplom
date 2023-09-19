package ru.smartjava.backend.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TokenMessage {

    @SerializedName("auth-token")
    String authtoken;
}
