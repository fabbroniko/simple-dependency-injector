package com.fabbroniko.sdi.target.multi;

import com.fabbroniko.sdi.annotation.Component;

@Component
public record DependsOnMulti(Animal cat, Animal dog) {
}
