package de.cfranzen.archsonar.resources;

public interface ResourceDecorator extends Resource {

    Resource delegate();
}
