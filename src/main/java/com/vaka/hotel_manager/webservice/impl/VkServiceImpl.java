package com.vaka.hotel_manager.webservice.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.vaka.hotel_manager.util.exception.ApplicationException;
import com.vaka.hotel_manager.util.exception.AuthenticationException;
import com.vaka.hotel_manager.webservice.VkService;
import com.vaka.hotel_manager.webservice.jsonobject.ResponseHolder;
import com.vaka.hotel_manager.webservice.jsonobject.VkAuthRequest;
import com.vaka.hotel_manager.webservice.jsonobject.VkUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Iaroslav on 1/31/2017.
 */
public class VkServiceImpl implements VkService {
    private static final Logger LOG = LoggerFactory.getLogger(VkServiceImpl.class);
    private static final String ACCESS_TOKEN_URL = "https://oauth.vk.com/access_token?client_id=%s&client_secret=%s&redirect_uri=%s&code=%s";
    private static final String SIGN_UP_VK_URI = "%s/signup-vk";
    private static final String VK_CLIENT_ID = "5544247";
    private static final String VK_CLIENT_SECRET = "i1jmjnJjFSWtAly1iY6X";
    private static final String METHOD_USERS_GET = "https://api.vk.com/method/users.get?access_token=%s";
    private static final String SERVER_ADDRESS = "http://109.86.28.43:8081";
    private final ObjectMapper mapper = new ObjectMapper();
    private final ObjectReader reader = mapper.reader();
    private final JsonFactory jsonFactory = mapper.getFactory();


    @Override
    public VkAuthRequest signIn(String code) {

        String url = String.format(ACCESS_TOKEN_URL, VK_CLIENT_ID, VK_CLIENT_SECRET, String.format(SIGN_UP_VK_URI, SERVER_ADDRESS), code);
        try {
            HttpURLConnection con = getConnection(url, "GET");
            return reader.readValue(jsonFactory.createParser(con.getInputStream()),
                    new TypeReference<VkAuthRequest>() {});
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
    public VkUserInfo getSignUpInfo(String token) {
        String url = String.format(METHOD_USERS_GET, token);
        try {
            HttpURLConnection con = getConnection(url, "GET");
            ResponseHolder<VkUserInfo[]> arr = reader.readValue(jsonFactory.createParser(con.getInputStream()),
                    new TypeReference<ResponseHolder<VkUserInfo[]>>() {});
            return arr.getResponse()[0];
        } catch (IOException e) {
            LOG.debug(e.getMessage(), e);
            throw new ApplicationException(e);
        }
    }

    private HttpURLConnection getConnection(String url, String requestMethod) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(requestMethod);
        return con;
    }
}
