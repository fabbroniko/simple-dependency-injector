package com.fabbroniko.sdi.annotation;

import com.fabbroniko.ul.manager.LogManager;
import com.fabbroniko.ul.manager.NoLogManager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Configuration {

    String componentScan() default "";

    Class<? extends LogManager> logger() default NoLogManager.class;
}
