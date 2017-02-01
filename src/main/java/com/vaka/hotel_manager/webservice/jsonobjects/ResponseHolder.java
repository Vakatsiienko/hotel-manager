package com.vaka.hotel_manager.webservice.jsonobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Iaroslav on 2/1/2017.
 */
@NoArgsConstructor
@ToString
@Getter
@Setter
public class ResponseHolder<T> {

    @JsonProperty(value = "response")
    private T response;
}
