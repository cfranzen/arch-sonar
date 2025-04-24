package de.cfranzen.archsonar.resources.preprocessing.mediatype;

import de.cfranzen.archsonar.resources.Resource;
import de.cfranzen.archsonar.resources.ResourceDecorator;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.tika.mime.MediaType;

import java.io.InputStream;
import java.net.URI;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode
@ToString
class MediatypeAwareResource implements ResourceDecorator {

    private final Resource delegate;

    private final MediaType mediaType;

    @Override
    public URI uri() {
        return delegate.uri();
    }

    @Override
    public InputStream openInputStream() {
        return delegate.openInputStream();
    }

    @Override
    public MediaType type() {
        return mediaType;
    }

    @Override
    public Resource delegate() {
        return delegate;
    }
}
