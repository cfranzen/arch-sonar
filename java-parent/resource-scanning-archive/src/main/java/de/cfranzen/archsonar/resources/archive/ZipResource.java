package de.cfranzen.archsonar.resources.archive;

import de.cfranzen.archsonar.resources.Resource;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.val;
import org.apache.tika.mime.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@EqualsAndHashCode(of = "uri")
@ToString(of = "uri")
public class ZipResource implements Resource {

    private final Path zipFile;

    private final ZipEntry entry;

    private final URI uri;

    ZipResource(final Path zipFile, final ZipEntry entry) {
        this.zipFile = zipFile.toAbsolutePath();
        this.entry = entry;
        this.uri = URI.create("jar:" + this.zipFile.toUri() + "!/" + entry.getName());
    }

    @Override
    public URI uri() {
        return uri;
    }

    @Override
    public InputStream openInputStream() {
        try {
            // TODO: This approach might be slow for ZIP files with many entries. Should we use ZipFile instead?
            val stream = new ZipInputStream(Files.newInputStream(zipFile, StandardOpenOption.READ));

            ZipEntry currentEntry;
            while ((currentEntry = stream.getNextEntry()) != null) {
                if (currentEntry.getName().equals(entry.getName())) {
                    return stream;
                }
            }
            throw new IllegalStateException("Unable to find entry in ZIP archive: " + uri);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to open ZipInputStream for " + uri, e);
        }
    }

    @Override
    public MediaType type() {
        return MediaType.OCTET_STREAM;
    }
}