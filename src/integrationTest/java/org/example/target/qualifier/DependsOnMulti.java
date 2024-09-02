package org.example.target.qualifier;

import org.example.annotation.Component;
import org.example.annotation.Qualifier;

@Component
public record DependsOnMulti(@Qualifier("cat") Animal animal, @Qualifier("dog") Animal secondAnimal) {
}
