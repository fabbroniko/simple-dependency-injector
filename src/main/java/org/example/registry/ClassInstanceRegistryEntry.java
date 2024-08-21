package org.example.registry;

public record ClassInstanceRegistryEntry(Class<?> clazz, Object instance) implements RegistryEntry {
}
