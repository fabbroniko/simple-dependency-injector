package org.example.target.multi;

import org.example.annotation.Component;

@Component
public record DependsOnMulti(Animal cat, Animal dog) {
}
