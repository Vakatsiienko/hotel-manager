package com.vaka.hotel_manager.service.impl;

import com.vaka.hotel_manager.service.VkService;
import com.vaka.hotel_manager.util.JSONParser;
import com.vaka.hotel_manager.util.exception.ApplicationException;
import com.vaka.hotel_manager.util.exception.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by Iaroslav on 1/31/2017.
 */
public class VkServiceImpl implements VkService {
    private static final Logger LOG = LoggerFactory.getLogger(VkServiceImpl.class);
    private static final String ACCESS_TOKEN_URL = "https://oauth.vk.com/access_token?client_id=%s&client_secret=%s&redirect_uri=%s&code=%s";
    private static final String SIGN_UP_VK_URI = "%s/users/signup-vk";
    private static final String VK_CLIENT_ID = "5544247";
    private static final String VK_CLIENT_SECRET = "i1jmjnJjFSWtAly1iY6X";
    private static final String METHOD_USERS_GET = "https://api.vk.com/method/users.get?fields=%s&access_token=%s";
    private static final String SERVER_ADDRESS = "http://109.86.28.43:8081";

    @Override
    public Map<String, String> signIn(String code) {
        String url = String.format(ACCESS_TOKEN_URL, VK_CLIENT_ID, VK_CLIENT_SECRET, String.format(SIGN_UP_VK_URI, SERVER_ADDRESS), code);
        try {
            return get(url);
        } catch (IOException e) {
            if (e.getMessage().startsWith("Server returned HTTP response code: 401"))
                throw new AuthenticationException("Vk authentication fail");
            else {
                LOG.debug(e.getMessage(), e);
                throw new ApplicationException("Internal server error");
            }
        }
    }

    @Override
    public Map<String, String> getSignUpInfo(String token) {
        String url = String.format(METHOD_USERS_GET, "contacts", token);
        try {
            return get(url);
        } catch (IOException e) {
            LOG.debug(e.getMessage(), e);
            throw new ApplicationException("Internal server error");
        }
    }

    private Map<String, String> get(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        return new JSONParser().parse(con.getInputStream());
    }
}
