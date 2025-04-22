package de.cfranzen.archsonar.resources;

public interface ResourcesCollectionDecorator extends ResourcesCollection {

    ResourcesCollection delegate();
}