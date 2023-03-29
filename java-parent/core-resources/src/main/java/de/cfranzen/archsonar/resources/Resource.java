package de.cfranzen.archsonar.resources;

import java.io.InputStream;
import java.net.URI;

public interface Resource {

    URI uri();

    InputStream openInputStream();
}