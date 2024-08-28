package org.example.target.util;

import org.example.annotation.Component;

@Component
public record WithDependencyTestClass(NoDependenciesTestClass dependency) {
}
