package com.fabbroniko.sdi.naming;

public class QualifierValidator implements Validator {

    @Override
    public boolean isValid(final String name) {
        return name != null && !name.isBlank();
    }
}
