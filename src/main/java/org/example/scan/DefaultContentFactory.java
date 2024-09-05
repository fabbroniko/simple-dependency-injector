package org.example.scan;

public class DefaultContentFactory implements ContentFactory {

    @Override
    public FileSystemContent createDirectory() {
        return new DirectoryContent(this);
    }

    @Override
    public FileSystemContent createFile() {
        return new FileContent(new DefaultClassLoaderWrapper());
    }
}
