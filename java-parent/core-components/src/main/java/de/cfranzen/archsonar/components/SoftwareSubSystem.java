package de.cfranzen.archsonar.components;

import java.util.Set;

public interface SoftwareSubSystem {

    Set<SoftwareSubSystem> subSystems();

    Set<Module> modules();
}
