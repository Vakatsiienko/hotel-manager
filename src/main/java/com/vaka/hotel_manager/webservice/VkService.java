package com.vaka.hotel_manager.webservice;

import com.vaka.hotel_manager.webservice.jsonobjects.VkAuthRequest;
import com.vaka.hotel_manager.webservice.jsonobjects.VkUserInfo;

/**
 * Created by Iaroslav on 1/31/2017.
 */
public interface VkService {

    VkAuthRequest signIn(String code);

    VkUserInfo getSignUpInfo(String token);
}
