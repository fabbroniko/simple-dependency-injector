package com.fabbroniko.sdi.registry;

public interface InstanceFactory {

    Instance createProcessing();

    Instance createCompleted(final Object instance);
}
