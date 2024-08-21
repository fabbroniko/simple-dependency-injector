package org.example.scan;

public interface ContentFactory {

    DirectoryContent createDirectory();

    DirectoryContent createFile();
}
