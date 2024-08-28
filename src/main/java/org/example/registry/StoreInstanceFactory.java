package org.example.registry;

public class StoreInstanceFactory implements InstanceFactory {

    @Override
    public Instance createProcessing() {
        return new InstanceStore(State.PROCESSING, null);
    }

    @Override
    public Instance createCompleted(final Object instance) {
        return new InstanceStore(State.COMPLETE, instance);
    }
}
