package org.example.registry;

import java.util.HashMap;

public class SimpleRegistryFactory implements RegistryFactory {

    @Override
    public Registry create() {
        return new SimpleRegistry(new HashMap<>());
    }
}
