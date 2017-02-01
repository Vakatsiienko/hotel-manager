package com.vaka.hotel_manager.service;

import java.util.Map;

/**
 * Created by Iaroslav on 1/31/2017.
 */
public interface VkService {

    Map<String, String> signIn(String code);

    Map<String, String> getSignUpInfo(String token);
}
