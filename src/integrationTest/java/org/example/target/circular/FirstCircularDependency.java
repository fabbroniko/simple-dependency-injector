package org.example.target.circular;

import org.example.annotation.Component;

@Component
public record FirstCircularDependency(SecondCircularDependency dependency) {
}
