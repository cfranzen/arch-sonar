package de.cfranzen.archsonar.components.detector;

import de.cfranzen.archsonar.resources.ResourcesCollection;

public interface ComponentDetector {

    DetectedComponents detect(ResourcesCollection resources);
}
