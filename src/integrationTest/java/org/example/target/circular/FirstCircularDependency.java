package org.example.target.circular;

import org.example.annotation.Component;

@Component
public class FirstCircularDependency {

    public FirstCircularDependency(final SecondCircularDependency dependency) {
    }
}
