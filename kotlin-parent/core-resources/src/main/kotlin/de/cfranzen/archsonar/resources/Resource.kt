package de.cfranzen.archsonar.resources

import java.io.InputStream
import java.net.URI

interface Resource {

    val uri: URI

    fun openInputStream(): InputStream
}