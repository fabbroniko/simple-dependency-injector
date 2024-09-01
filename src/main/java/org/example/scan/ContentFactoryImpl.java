package org.example.scan;

public class ContentFactoryImpl implements ContentFactory {

    @Override
    public FileSystemContent createDirectory() {
        return new DirectoryContent(this);
    }

    @Override
    public FileSystemContent createFile() {
        return new FileContent(new DefaultClassLoaderWrapper());
    }

    @Override
    public FileSystemContent createJar() {
        return new JarContent();
    }
}
