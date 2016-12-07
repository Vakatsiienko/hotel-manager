package com.vaka.web.servlet;

import com.vaka.context.ApplicationContext;
import com.vaka.context.config.ApplicationContextConfig;
import com.vaka.context.config.PersistenceConfig;
import com.vaka.web.controller.MainController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Iaroslav on 11/29/2016.
 */
public class MainServlet extends HttpServlet {
    private MainController mainController;

    @Override
    public void init() throws ServletException {
        super.init();
        ApplicationContext.getInstance()
                .init(new ApplicationContextConfig(), new PersistenceConfig());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/".equals(req.getRequestURI())) {
            getMainController().toRoot(req, resp);
        } else super.doGet(req, resp);
    }

    public MainController getMainController() {
        if (mainController == null) {
            synchronized (this) {
                if (mainController == null) {
                    mainController = ApplicationContext.getInstance().getBean(MainController.class);
                }
            }
        }
        return mainController;
    }
}
