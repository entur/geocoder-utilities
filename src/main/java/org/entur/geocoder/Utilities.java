package org.entur.geocoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utilities {

    public static boolean isValidFile(Path path) {
        try {
            return Files.isRegularFile(path) && !Files.isHidden(path);
        } catch (IOException e) {
            return false;
        }
    }
}
