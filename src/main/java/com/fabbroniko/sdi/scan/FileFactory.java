package com.fabbroniko.sdi.scan;

import java.io.File;
import java.net.URL;

public interface FileFactory {

    File create(final URL url);
}
