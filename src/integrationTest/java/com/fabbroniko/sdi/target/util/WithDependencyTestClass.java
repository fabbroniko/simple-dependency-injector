package com.fabbroniko.sdi.target.util;

import com.fabbroniko.sdi.annotation.Component;

@Component
public record WithDependencyTestClass(NoDependenciesTestClass dependency) {
}
