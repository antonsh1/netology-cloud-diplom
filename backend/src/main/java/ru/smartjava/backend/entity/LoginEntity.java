package ru.smartjava.backend.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.smartjava.backend.config.Constants;

@Getter
@Setter
@RequiredArgsConstructor
public class LoginEntity {

    @SerializedName(Constants.loginField)
    String login;
    @SerializedName(Constants.passwordField)
    String password;
}
