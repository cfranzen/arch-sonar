package de.cfranzen.archsonar.components.java.detector.source;

import de.cfranzen.archsonar.resources.Resource;
import de.cfranzen.archsonar.resources.support.ResourceUtils;

import javax.tools.SimpleJavaFileObject;
import java.io.IOException;

class SourceResourceJavaFileObject extends SimpleJavaFileObject {

    private final Resource resource;

    SourceResourceJavaFileObject(final Resource resource) {
        super(resource.uri(), Kind.SOURCE);
        this.resource = resource;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return ResourceUtils.readContentAsString(resource);
    }
}
