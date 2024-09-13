package com.fabbroniko.sdi.registry;

public record ImmutableInstance(State state, Object instance) implements Instance {
}
