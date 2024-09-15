package com.fabbroniko.sdi.scan;

public class DefaultContentFactory implements ContentFactory {

    private final FileFactory fileFactory;
    private final ResourceLocator jarResourceLocator;
    private final ClassLoaderWrapper classLoaderWrapper;
    private final JarFileFactory jarFileFactory;

    public DefaultContentFactory(final FileFactory fileFactory,
                                 final ResourceLocator jarResourceLocator,
                                 final ClassLoaderWrapper classLoaderWrapper,
                                 final JarFileFactory jarFileFactory) {
        this.fileFactory = fileFactory;
        this.jarResourceLocator = jarResourceLocator;
        this.classLoaderWrapper = classLoaderWrapper;
        this.jarFileFactory = jarFileFactory;
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
        return new JarContent(fileFactory, jarResourceLocator, classLoaderWrapper, jarFileFactory);
    }
}
