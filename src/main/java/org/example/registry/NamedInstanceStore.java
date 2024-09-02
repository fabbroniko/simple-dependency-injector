package org.example.registry;

public record NamedInstanceStore(String qualifyingName, State state, Object instance) implements NamedInstance {
}
