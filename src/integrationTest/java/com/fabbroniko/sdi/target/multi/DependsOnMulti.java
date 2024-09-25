package com.fabbroniko.sdi.target.multi;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Configuration;

@Component
@Configuration(componentScan = "com.fabbroniko.sdi.target.multi")
public record DependsOnMulti(Animal cat, Animal dog) {
}
