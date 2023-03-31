package de.cfranzen.archsonar.resources;

import java.util.Set;

public interface SoftwareSubSystem {

    Set<SoftwareSubSystem> subSystems();

    Set<Module> modules();
}
