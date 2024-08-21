package org.example.scan;

public class ContentFactoryImpl implements ContentFactory {

    @Override
    public DirectoryContent createDirectory() {
        return new SubDirectoryContent(this);
    }

    @Override
    public DirectoryContent createFile() {
        return new FileDirectoryContent(new DefaultClassLoaderWrapper());
    }
}
