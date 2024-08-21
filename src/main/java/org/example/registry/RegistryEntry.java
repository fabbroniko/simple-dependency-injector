package org.example.registry;

public interface RegistryEntry {

    Class<?> clazz();

    Object instance();
}
