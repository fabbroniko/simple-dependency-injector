package org.example.target.util;

import org.example.annotation.Component;

@Component
public class WithDependencyTestClass {

    private final NoDependenciesTestClass dependency;

    public WithDependencyTestClass(final NoDependenciesTestClass dependency) {
        this.dependency = dependency;
    }

    public NoDependenciesTestClass getDependency() {
        return dependency;
    }
}
