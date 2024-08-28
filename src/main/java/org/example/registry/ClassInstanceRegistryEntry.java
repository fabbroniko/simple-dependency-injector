package org.example.registry;

public record ClassInstanceRegistryEntry(Class<?> clazz, Instance instance) implements RegistryEntry {
}
