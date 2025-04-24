package de.cfranzen.archsonar.resources.support;

import de.cfranzen.archsonar.resources.Resource;
import lombok.val;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class ResourceUtils {

    private ResourceUtils() {
        // Do not instantiate utils class
    }

    public static byte[] readContent(final Resource resource) throws IOException {
        try (val is = resource.openInputStream()) {
            val result = new ByteArrayOutputStream();
            val buffer = new byte[1024];
            for (int length; (length = is.read(buffer)) != -1; ) {
                result.write(buffer, 0, length);
            }
            return result.toByteArray();
        }
    }

    public static String readContentAsString(final Resource resource) throws IOException {
        try (val is = resource.openInputStream()) {
            val result = new ByteArrayOutputStream();
            val buffer = new byte[1024];
            for (int length; (length = is.read(buffer)) != -1; ) {
                result.write(buffer, 0, length);
            }
            return result.toString(StandardCharsets.UTF_8);
        }
    }
}
