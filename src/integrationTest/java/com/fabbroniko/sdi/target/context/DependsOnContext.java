package com.fabbroniko.sdi.target.context;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.context.ApplicationContext;

@Component
public record DependsOnContext(ApplicationContext applicationContext) {
}
