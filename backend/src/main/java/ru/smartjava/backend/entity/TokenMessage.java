package ru.smartjava.backend.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import ru.smartjava.backend.config.Constants;

@AllArgsConstructor
@Getter
public class TokenMessage {

    @SerializedName(Constants.authTokenName)
    String authtoken;

}
