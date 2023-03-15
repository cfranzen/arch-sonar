package de.cfranzen.archsonar.resources.filesystem

import de.cfranzen.archsonar.resources.Resource
import de.cfranzen.archsonar.resources.ResourcesCollection
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.isRegularFile
import kotlin.streams.asSequence

class PathResourcesCollection(
    paths: Collection<Path>
) : ResourcesCollection {

    constructor(vararg paths: Path) : this(paths.toList())

    private val resources: Set<Resource>

    init {
        resources = paths.flatMap { path -> Files.walk(path).asSequence() }
            .filter { it.isRegularFile() }
            .map { PathResource(it) }
            .toSet()
    }

    override fun iterator(): Iterator<Resource> = resources.iterator()
}