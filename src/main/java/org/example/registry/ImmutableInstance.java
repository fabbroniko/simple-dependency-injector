package org.example.registry;

public record ImmutableInstance(State state, Object instance) implements Instance {
}
