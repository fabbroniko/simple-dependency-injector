package org.example.naming;

public class ClassBasedQualifierResolver implements QualifierResolver<Class<?>> {

    @Override
    public String resolve(final Class<?> target) {
        String qualifier = target.getSimpleName();
        char[] qualifierCharacters = qualifier.toCharArray();
        qualifierCharacters[0] = Character.toLowerCase(qualifierCharacters[0]);
        qualifier = new String(qualifierCharacters);

        return qualifier;
    }
}
