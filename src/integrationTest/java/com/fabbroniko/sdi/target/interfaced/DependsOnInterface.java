package com.fabbroniko.sdi.target.interfaced;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Configuration;

@Component
@Configuration(componentScan = "com.fabbroniko.sdi.target.interfaced")
public record DependsOnInterface(SampleInterface sampleInterface) {
}
