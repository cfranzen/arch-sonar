package de.cfranzen.archsonar.components;

import java.util.Set;

public interface SoftwareSystem {

    Set<SoftwareSubSystem> subSystems();

    Set<Module> modules();
}
