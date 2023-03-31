package de.cfranzen.archsonar.resources;

import java.util.Set;

public interface SoftwareSystem {

    Set<SoftwareSubSystem> subSystems();

    Set<Module> modules();
}
