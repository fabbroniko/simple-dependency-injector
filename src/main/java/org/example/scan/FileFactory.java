package org.example.scan;

import java.io.File;
import java.net.URL;

public interface FileFactory {

    File create(final URL url);
}
