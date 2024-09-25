package com.fabbroniko.sdi.target.util;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Configuration;

@Component
@Configuration(componentScan = "com.fabbroniko.sdi.target.util")
public record WithDependencyTestClass(NoDependenciesTestClass dependency) {
}
