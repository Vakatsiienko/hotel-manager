package com.vaka.hotel_manager.webservice.jsonobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@ToString
@Getter
@Setter
public class VkAuthRequest {

    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "expires_in")
    private Integer expiresIn;

    @JsonProperty(value = "user_id")
    private Integer userId;

    @JsonProperty(value = "email")
    private String email;
}
