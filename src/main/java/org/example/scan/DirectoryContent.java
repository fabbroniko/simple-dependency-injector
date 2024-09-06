package org.example.scan;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;

public class DirectoryContent implements FileSystemContent {

    private final ContentFactory contentFactory;
    private final FileFactory fileFactory;

    public DirectoryContent(final ContentFactory contentFactory,
                            final FileFactory fileFactory) {

        this.contentFactory = contentFactory;
        this.fileFactory = fileFactory;
    }

    @Override
    public Set<Class<?>> getClasses(final String inPackage, final URL resource) {
        return getClasses(inPackage, fileFactory.create(resource));
    }

    @Override
    public Set<Class<?>> getClasses(String inPackage, File directory) {
        final File[] content = requireNonNull(directory.listFiles());
        final Stream<Class<?>> directoryStream = stream(content)
            .filter(File::isDirectory)
            .map(currentFile -> contentFactory.createDirectory().getClasses(inPackage + "." + currentFile.getName(), currentFile))
            .flatMap(Collection::stream);
        final Stream<Class<?>> fileStream = stream(content)
            .filter(file -> !file.isDirectory())
            .map(currentFile -> contentFactory.createFile().getClasses(inPackage, currentFile))
            .flatMap(Collection::stream);

        return Stream.concat(directoryStream, fileStream).collect(Collectors.toSet());
    }
}