package com.fabbroniko.sdi.scan;

import java.net.URL;

public interface ContentSelector {

    FileSystemContent select(final URL resourceLocation);
}
