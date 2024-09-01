package org.example.scan;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class URIFileFactory implements FileFactory {

    @Override
    public File create(final URL url) {
        try {
            if(url.getProtocol().equals("jar")) {
                String tmp = url.toString();
                tmp = tmp.substring(0, tmp.indexOf("!"));
                tmp = tmp.substring(4);
                System.out.println("@@@@@@@@@@@@@@@@@ " + tmp);

                return new File(new URL(tmp).toURI());
            }
            return new File(url.toURI());
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
