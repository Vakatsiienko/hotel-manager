package com.vaka.hotel_manager.web.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Iaroslav on 12/9/2016.
 */
public class HttpServletRequestWrapper {
    private final HttpServletRequest request;

    public HttpServletRequestWrapper(HttpServletRequest request) {
        this.request = request;
    }

    public String getParameter(String name) {
        return request.getParameter(name);
    }

    public void setAttribute(String name, Object attribute) {
        request.setAttribute(name, attribute);
    }
    public RequestDispatcher getRequestDispatcher(String path){
        return request.getRequestDispatcher(path);
    }

}
