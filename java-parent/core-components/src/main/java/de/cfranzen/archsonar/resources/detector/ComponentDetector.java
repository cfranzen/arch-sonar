package de.cfranzen.archsonar.resources.detector;

import de.cfranzen.archsonar.resources.ResourcesCollection;

public interface ComponentDetector {

    DetectedComponents detect(ResourcesCollection resources);
}
