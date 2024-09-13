package com.fabbroniko.sdi.naming;

public interface QualifierResolver<T> {

    String resolve(final T target);
}
