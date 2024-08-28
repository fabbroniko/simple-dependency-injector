package org.example.registry;

public record BaseInstance(State state, Object instance) implements Instance {
}
