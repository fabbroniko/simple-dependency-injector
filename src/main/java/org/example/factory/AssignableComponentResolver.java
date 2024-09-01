package org.example.factory;

import org.example.annotation.Component;
import org.example.exception.InvalidDependencyException;

import java.util.Set;

public class AssignableComponentResolver implements ComponentResolver {

    private final Set<Class<?>> scannedComponents;

    public AssignableComponentResolver(Set<Class<?>> scannedComponents) {
        this.scannedComponents = scannedComponents;
    }

    @Override
    public Class<?> resolve(final Class<?> target) {
        return resolve(target, qualifyingName(target));
    }

    @Override
    public Class<?> resolve(final Class<?> target, final String qualifyingName) {
        if (target.isInterface()) {
            return scannedComponents.stream()
                .filter(target::isAssignableFrom)
                .filter(component -> qualifyingName(component).equals(qualifyingName))
                .findAny()
                .orElseThrow(InvalidDependencyException::new);
        }

        return target;
    }

    private String qualifyingName(final Class<?> target) {
        String qualifyingName = target.getAnnotation(Component.class).value();
        // TODO delegate naming
        if (qualifyingName.isBlank()) {
            qualifyingName = target.getSimpleName();
            char[] nameChars = qualifyingName.toCharArray();
            nameChars[0] = Character.toLowerCase(nameChars[0]);
            qualifyingName = new String(nameChars);
        }

        return qualifyingName;
    }
}
