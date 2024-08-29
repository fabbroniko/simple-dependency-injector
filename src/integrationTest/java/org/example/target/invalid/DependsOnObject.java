package org.example.target.invalid;

import org.example.annotation.Component;

@Component
public record DependsOnObject(Object nonComponent) {
}
