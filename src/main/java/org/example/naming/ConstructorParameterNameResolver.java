package org.example.naming;

import java.lang.reflect.Parameter;

public interface ConstructorParameterNameResolver {

    String resolve(final Parameter constructorParameter);
}
