package org.example.scan;

import java.io.File;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;

public class DirectoryContent implements FileSystemContent {

    private final ContentFactory contentFactory;

    public DirectoryContent(final ContentFactory contentFactory) {

        this.contentFactory = contentFactory;
    }

    @Override
    public Set<Class<?>> getClasses(final String inPackage, final File directory) {
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