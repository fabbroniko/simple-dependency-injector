package org.example.target.interfaced;

import org.example.annotation.Component;

@Component
public record DependsOnInterface(SampleInterface sampleInterface) {
}
