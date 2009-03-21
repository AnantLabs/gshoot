package com.vinci.gshoot;

import com.vinci.gshoot.watchdog.TimeScheduleService;
import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;


public class StartUpListener extends ContextLoaderListener {
    private TimeScheduleService timeScheduleService;
    private Logger logger = Logger.getLogger(StartUpListener.class);

    public void contextInitialized(ServletContextEvent event) {
        try {
            super.contextInitialized(event);
            System.out.println("---------------Start GShoot");
            logger.info("---------------Start GShoot");
            ServletContext servletContext = event.getServletContext();
            WebApplicationContext
                    context = (WebApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

            timeScheduleService = (TimeScheduleService) context.getBean("timeScheduleService");
            logger.info("Start watch dog service...");
            timeScheduleService.start();
            logger.info("Watch dog service started.");
            logger.info("---------------GShoot started.");

        } catch (Throwable e) {
            logger.error("---------------GShoot start failed: " + e.getMessage(), e);
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent event) {
        logger.info("---------------Stop GShoot");
        logger.info("Stop watch dog service.");
        timeScheduleService.stop();

        super.contextDestroyed(event);
        logger.info("---------------GShoot stopped.");
    }
}
