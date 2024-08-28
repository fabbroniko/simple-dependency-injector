package org.example.target.circular;

import org.example.annotation.Component;

@Component
public record SecondCircularDependency(FirstCircularDependency dependency) {
}
