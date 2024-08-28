package org.example.registry;

public interface InstanceFactory {

    Instance createProcessing();

    Instance createCompleted(final Object instance);
}
