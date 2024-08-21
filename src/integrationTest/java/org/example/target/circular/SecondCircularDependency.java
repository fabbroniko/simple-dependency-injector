package org.example.target.circular;

import org.example.annotation.Component;

@Component
public class SecondCircularDependency {

    public SecondCircularDependency(final FirstCircularDependency dependency) {
    }
}
