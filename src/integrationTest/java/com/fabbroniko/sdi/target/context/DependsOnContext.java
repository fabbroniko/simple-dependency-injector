package com.fabbroniko.sdi.target.context;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Configuration;
import com.fabbroniko.sdi.context.ApplicationContext;

@Component
@Configuration(componentScan = "com.fabbroniko.sdi.target.context")
public record DependsOnContext(ApplicationContext applicationContext) {
}
