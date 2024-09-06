package org.example.scan;

public class DefaultContentFactory implements ContentFactory {

    private final FileFactory fileFactory;
    private final ResourceLocator jarResourceLocator;

    public DefaultContentFactory(final FileFactory fileFactory,
                                 final ResourceLocator jarResourceLocator) {
        this.fileFactory = fileFactory;
        this.jarResourceLocator = jarResourceLocator;
    }

    @Override
    public FileSystemContent createDirectory() {
        return new DirectoryContent(this, fileFactory);
    }

    @Override
    public FileSystemContent createFile() {
        return new FileContent(new DefaultClassLoaderWrapper(), fileFactory);
    }

    @Override
    public FileSystemContent createJar() {
        return new JarContent(fileFactory, jarResourceLocator);
    }
}
