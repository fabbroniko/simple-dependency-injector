package org.example.naming;

public interface QualifierResolver<T> {

    String resolve(final T target);
}
