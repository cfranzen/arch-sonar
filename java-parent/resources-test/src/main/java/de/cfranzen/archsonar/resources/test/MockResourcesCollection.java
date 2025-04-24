package de.cfranzen.archsonar.resources.test;

import de.cfranzen.archsonar.resources.Resource;
import de.cfranzen.archsonar.resources.ResourcesCollection;

import java.util.*;
import java.util.stream.Stream;

public class MockResourcesCollection implements ResourcesCollection {

    private final List<Resource> resources;

    private MockResourcesCollection(final List<Resource> resources) {
        this.resources = resources;
    }

    public static MockResourcesCollection empty() {
        return new MockResourcesCollection(Collections.emptyList());
    }

    public static MockResourcesCollection of(final Collection<? extends Resource> resources) {
        if (resources == null || resources.isEmpty()) {
            throw new IllegalArgumentException("At least one resource must be specified");
        }
        return new MockResourcesCollection(new ArrayList<>(resources));
    }

    public static MockResourcesCollection of(final Resource... resources) {
        if (resources == null || resources.length == 0) {
            throw new IllegalArgumentException("At least one resource must be specified");
        }
        return new MockResourcesCollection(Arrays.asList(resources));
    }

    @Override
    public Stream<Resource> resources() {
        return resources.stream();
    }
}
