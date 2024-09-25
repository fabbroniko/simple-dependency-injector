package com.fabbroniko.sdi.target.qualifier;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Configuration;
import com.fabbroniko.sdi.annotation.Qualifier;

@Component
@Configuration(componentScan = "com.fabbroniko.sdi.target.qualifier")
public record DependsOnMulti(@Qualifier("cat") Animal animal, @Qualifier("dog") Animal secondAnimal) {
}
