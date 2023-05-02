package de.cfranzen.archsonar.resources;

import org.apache.tika.mime.MediaType;

import java.io.InputStream;
import java.net.URI;

public interface Resource {

    URI uri();

    InputStream openInputStream();

    MediaType type();
}