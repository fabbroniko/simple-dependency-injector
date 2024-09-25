package com.fabbroniko.sdi.target.circular;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Configuration;

@Component
@Configuration(componentScan = "com.fabbroniko.sdi.target.circular")
public record FirstCircularDependency(SecondCircularDependency dependency) {
}
