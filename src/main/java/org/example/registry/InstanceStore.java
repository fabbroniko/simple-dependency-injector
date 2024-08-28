package org.example.registry;

public record InstanceStore(State state, Object instance) implements Instance {
}
