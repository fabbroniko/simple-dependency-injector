package org.example.scan;

public interface ContentFactory {

    FileSystemContent createDirectory();

    FileSystemContent createFile();

    FileSystemContent createJar();
}
