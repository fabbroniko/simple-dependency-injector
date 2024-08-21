package org.example.scan;

import java.net.URL;

public interface ResourceLocator {

    URL locate(final String targetPackage);
}
