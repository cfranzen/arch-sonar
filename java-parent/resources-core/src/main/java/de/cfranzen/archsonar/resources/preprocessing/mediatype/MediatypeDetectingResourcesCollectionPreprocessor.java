package de.cfranzen.archsonar.resources.preprocessing.mediatype;

import de.cfranzen.archsonar.resources.ResourcesCollection;
import de.cfranzen.archsonar.resources.preprocessing.ResourcesCollectionPreprocessor;
import lombok.extern.log4j.Log4j2;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;

@Log4j2
public class MediatypeDetectingResourcesCollectionPreprocessor implements ResourcesCollectionPreprocessor {

    private final Detector detector = new DefaultDetector();

    @Override
    public ResourcesCollection preprocess(final ResourcesCollection collection) {
        if (collection instanceof MediatypeDetectingResourcesCollection) {
            return collection;
        } else {
            return new MediatypeDetectingResourcesCollection(detector, collection);
        }

    }
}
