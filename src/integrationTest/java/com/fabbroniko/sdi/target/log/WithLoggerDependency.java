package com.fabbroniko.sdi.target.log;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Configuration;
import com.fabbroniko.ul.Logger;
import com.fabbroniko.ul.manager.JdkLogManager;

@Component
@Configuration(componentScan = "com.fabbroniko.sdi.target.log", logger = JdkLogManager.class)
public record WithLoggerDependency(Logger logger) {
}
