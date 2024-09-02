package org.example.naming;

import org.example.annotation.Component;

public class AnnotationBasedNameResolver implements QualifyingNameResolver {

    @Override
    public String resolveFor(final Class<?> target) {
        final Component targetAnnotation = target.getAnnotation(Component.class);
        String qualifyingName = null;
        if (targetAnnotation != null) {
            qualifyingName = targetAnnotation.value();
        }

        if (qualifyingName == null || qualifyingName.isBlank()) {
            qualifyingName = target.getSimpleName();
            char[] nameChars = qualifyingName.toCharArray();
            nameChars[0] = Character.toLowerCase(nameChars[0]);
            qualifyingName = new String(nameChars);
        }

        return qualifyingName;
    }
}
