package com.github.yuruki.camel.runner;

import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApp {

    private static Logger log = LoggerFactory.getLogger(MainApp.class);

    public static void main(String... args) throws Exception {
        Main main = new CamelRunnerMain();
        main.enableHangupSupport();
        main.run(args);
    }

}

