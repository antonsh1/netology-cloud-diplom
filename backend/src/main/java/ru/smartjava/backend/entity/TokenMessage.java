package ru.smartjava.backend.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import ru.smartjava.backend.config.Constants;

@AllArgsConstructor
@Getter
@RequiredArgsConstructor
@ToString
public class TokenMessage {

    @JsonAlias({ Constants.authTokenName })
    @SerializedName(Constants.authTokenName)
    private String authtoken;

}
