package org.example.target.interfaced;

import org.example.annotation.Component;

@Component
public class DependsOnInterface {

    private final SampleInterface sampleInterface;

    public DependsOnInterface(final SampleInterface sampleInterface) {
        this.sampleInterface = sampleInterface;
    }

    public SampleInterface getSampleInterface() {
        return sampleInterface;
    }
}
