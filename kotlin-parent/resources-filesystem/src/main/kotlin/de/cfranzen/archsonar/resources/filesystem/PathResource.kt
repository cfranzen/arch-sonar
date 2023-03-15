package de.cfranzen.archsonar.resources.filesystem

import de.cfranzen.archsonar.resources.Resource
import java.io.InputStream
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path

class PathResource(private val path: Path) : Resource {

    override val uri: URI = path.toAbsolutePath().toUri()

    override fun openInputStream(): InputStream {
        return Files.newInputStream(path)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PathResource
        if (uri != other.uri) return false
        return true
    }

    override fun hashCode(): Int {
        return uri.hashCode()
    }

    override fun toString(): String {
        return "PathResource($uri)"
    }


}