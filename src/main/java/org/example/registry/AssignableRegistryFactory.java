package org.example.registry;

import java.util.HashMap;

public class AssignableRegistryFactory implements RegistryFactory {

    @Override
    public Registry create() {
        return new AssignableRegistry(new HashMap<>(), new StoreInstanceFactory());
    }
}
