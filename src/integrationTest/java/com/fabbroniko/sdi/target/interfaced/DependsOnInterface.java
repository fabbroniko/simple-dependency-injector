package com.fabbroniko.sdi.target.interfaced;

import com.fabbroniko.sdi.annotation.Component;

@Component
public record DependsOnInterface(SampleInterface sampleInterface) {
}
