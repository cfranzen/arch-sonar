package de.cfranzen.archsonar.resources.preprocessing.mediatype;

import de.cfranzen.archsonar.resources.Resource;
import de.cfranzen.archsonar.resources.ResourcesCollection;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.mime.MediaType;

import java.io.IOException;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Log4j2
class MediatypeDetectingResourcesCollection implements ResourcesCollection {

    private final Detector detector;

    private final ResourcesCollection delegate;

    @Override
    public Stream<Resource> resources() {
        return delegate.resources().map(this::wrap);
    }

    private Resource wrap(final Resource res) {
        val mediaType = detectMediaType(res);
        if (mediaType == res.type()) {
            return res;
        } else {
            return new MediatypeAwareResource(res, mediaType);
        }
    }

    private MediaType detectMediaType(final Resource res) {
        try (final TikaInputStream is = TikaInputStream.get(res.openInputStream())) {
            val metadata = new Metadata();
            metadata.set(TikaCoreProperties.RESOURCE_NAME_KEY, getFileName(res));

            return detector.detect(is, metadata);
        } catch (IOException | RuntimeException e) {
            log.warn("Unable to detect media type of resource {}", res, e);
            return MediaType.OCTET_STREAM;
        }
    }

    private String getFileName(final Resource res) {
        val resourcePath = res.uri().toString();
        val slashIndex = resourcePath.lastIndexOf('/');
        return resourcePath.substring(slashIndex + 1);
    }
}
