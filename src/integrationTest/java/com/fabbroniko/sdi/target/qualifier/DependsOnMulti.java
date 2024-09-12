package com.fabbroniko.sdi.target.qualifier;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Qualifier;

@Component
public record DependsOnMulti(@Qualifier("cat") Animal animal, @Qualifier("dog") Animal secondAnimal) {
}
