package org.example.registry;

public class ImmutableInstanceFactory implements InstanceFactory {

    @Override
    public Instance createProcessing() {
        return new ImmutableInstance(State.PROCESSING, null);
    }

    @Override
    public Instance createCompleted(final Object instance) {
        return new ImmutableInstance(State.COMPLETE, instance);
    }
}
