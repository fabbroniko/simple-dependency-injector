package org.example.scan;

import org.example.exception.ComponentScanException;

import java.net.MalformedURLException;
import java.net.URL;

public class StringToUrlResourceLocator implements ResourceLocator {

    @Override
    public URL locate(final String target) {
        try {
            return new URL(target);
        } catch (final MalformedURLException e) {
            throw new ComponentScanException();
        }
    }
}