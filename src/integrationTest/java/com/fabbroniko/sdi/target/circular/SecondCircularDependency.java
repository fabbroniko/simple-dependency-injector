package com.fabbroniko.sdi.target.circular;

import com.fabbroniko.sdi.annotation.Component;

@Component
public record SecondCircularDependency(FirstCircularDependency dependency) {
}
