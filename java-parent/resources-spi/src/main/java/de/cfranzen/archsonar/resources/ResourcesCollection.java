package de.cfranzen.archsonar.resources;

import java.util.stream.Stream;

public interface ResourcesCollection {

    Stream<Resource> resources();
}