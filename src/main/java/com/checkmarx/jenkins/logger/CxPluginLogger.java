package com.checkmarx.jenkins.logger;


import hudson.WebAppMain;
import hudson.model.BuildListener;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


/**
 * Created by: zoharby.
 * Date: 25/01/2017.
 */
public class CxPluginLogger implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String INFO_PRE_TEXT = "[Checkmarx] - [info] - ";
    private static final String ERROR_PRE_TEXT = "[Checkmarx] - [ERROR] - ";

    private static final Logger STATIC_LOGGER;
    private LoggingDevice loggingDevice;

    static {
        STATIC_LOGGER = Logger.getLogger(WebAppMain.class.getName());
    }

    public CxPluginLogger() {
        this.loggingDevice = new JavaLoggingDevice();
    }

    public CxPluginLogger(BuildListener listener) {
        this.loggingDevice = new ListenerLoggingDevice(listener);
    }

    public void info(String message) {
       loggingDevice.info(message);
    }

    public void info(List<String> messages){
        for (String msg:messages){
            loggingDevice.info(msg);
        }
    }

    public void error(String message) {
        loggingDevice.error(message);
    }

    public void error(String message, Throwable error) {
        loggingDevice.error( message+"\n\n"+ Arrays.toString(error.getStackTrace())+"\n\n");
    }

    private class JavaLoggingDevice implements LoggingDevice{

        public JavaLoggingDevice() {
        }

        @Override
        public void info(String message) {
            STATIC_LOGGER.info(INFO_PRE_TEXT + message);
        }

        @Override
        public void error(String message) {
            STATIC_LOGGER.info(ERROR_PRE_TEXT + message);
        }
    }

    private class ListenerLoggingDevice implements LoggingDevice{

        private BuildListener listener;

        public ListenerLoggingDevice(BuildListener listener) {
            this.listener = listener;
        }

        @Override
        public void info(String message) {
            this.listener.getLogger().println(INFO_PRE_TEXT + message);
        }

        @Override
        public void error(String message) {
            this.listener.getLogger().println(ERROR_PRE_TEXT + message);
        }
    }
}
