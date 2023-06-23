package de.cfranzen.archsonar.components.java.detector;

import de.cfranzen.archsonar.components.detector.ComponentDetectorFactory;

class JavaComponentDetectorFactory implements ComponentDetectorFactory {
    @Override
    public JavaComponentDetector create() {
        return new JavaComponentDetector();
    }
}
