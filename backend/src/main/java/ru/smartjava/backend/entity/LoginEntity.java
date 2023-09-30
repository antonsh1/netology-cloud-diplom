package ru.smartjava.backend.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.smartjava.backend.config.Constants;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class LoginEntity {

    @SerializedName(Constants.loginField)
    private String login;
    @SerializedName(Constants.passwordField)
    private String password;
}
