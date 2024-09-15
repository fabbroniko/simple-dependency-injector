package com.fabbroniko.sdi.scan;

public interface ContentFactory {

    FileSystemContent createDirectory();

    FileSystemContent createFile();

    FileSystemContent createJar();
}
