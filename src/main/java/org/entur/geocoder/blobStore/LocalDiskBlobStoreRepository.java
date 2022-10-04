/*
 *
 *  * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 *  * the European Commission - subsequent versions of the EUPL (the "Licence");
 *  * You may not use this work except in compliance with the Licence.
 *  * You may obtain a copy of the Licence at:
 *  *
 *  *   https://joinup.ec.europa.eu/software/page/eupl
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the Licence is distributed on an "AS IS" basis,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the Licence for the specific language governing permissions and
 *  * limitations under the Licence.
 *  *
 *
 */

package org.entur.geocoder.blobStore;

import org.entur.geocoder.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple file-based blob store for testing purpose.
 */
public non-sealed class LocalDiskBlobStoreRepository implements BlobStoreRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalDiskBlobStoreRepository.class);

    private final String baseFolder;
    private String containerName;

    public LocalDiskBlobStoreRepository(String baseFolder) {
        this.baseFolder = baseFolder;
    }

    private String getContainerFolder() {
        return baseFolder + File.separator + containerName;
    }

    private String prependBasePath(String containerName) {
        return baseFolder + File.separator + containerName;
    }

    @Override
    public boolean existBlob(String objectName) {
        LOGGER.debug("existBlob called in local-disk blob store on {}", objectName);
        var path = Paths.get(getContainerFolder()).resolve(objectName);
        return path.toFile().exists();
    }

    @Override
    public InputStream getBlob(String objectName) {
        LOGGER.debug("get blob called in local-disk blob store on {}", objectName);
        var path = Paths.get(getContainerFolder()).resolve(objectName);
        if (!path.toFile().exists()) {
            LOGGER.debug("getBlob(): File not found in local-disk blob store: {} ", path);
            return null;
        }
        LOGGER.debug("getBlob(): File found in local-disk blob store: {} ", path);
        try {
            // converted as ByteArrayInputStream so that Camel stream cache can reopen it
            // since ByteArrayInputStream.close() does nothing
            return new ByteArrayInputStream(Files.readAllBytes(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void uploadBlob(String objectName, InputStream inputStream) {
        LOGGER.debug("Upload blob called in local-disk blob store on {}", objectName);
        try {
            var localPath = Paths.get(objectName);
            var parentDirectory = localPath.getParent();
            var folder = parentDirectory == null ? Paths.get(getContainerFolder()) : Paths.get(getContainerFolder()).resolve(parentDirectory);
            Files.createDirectories(folder);

            var fullPath = Paths.get(getContainerFolder()).resolve(localPath);
            Files.deleteIfExists(fullPath);

            Files.copy(inputStream, fullPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setBucketName(String bucketName) {
        this.containerName = bucketName;
    }

    @Override
    public InputStream getLatestBlob(String prefix) {
        if (Paths.get(getContainerFolder(), prefix).toFile().isDirectory()) {
            try (var paths = Files.walk(Paths.get(getContainerFolder(), prefix))) {
                return paths.filter(Utilities::isValidFile).findFirst().map(path -> {
                    try {
                        return Files.newInputStream(path);
                    } catch (IOException e) {
                        return null;
                    }
                }).orElseThrow();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public BlobStoreFiles listBlobStoreFiles(String prefix) {
        return listBlobs(List.of(prefix));
    }

    public BlobStoreFiles listBlobs(Collection<String> prefixes) {

        var blobStoreFiles = new BlobStoreFiles();
        for (var prefix : prefixes) {
            Path path = Paths.get(this.getContainerFolder()).resolve(prefix);
            if (path.toFile().isDirectory()) {
                try (var walk = Files.walk(path)) {
                    var result = walk.filter(Utilities::isValidFile)
                            .map(x -> new BlobStoreFiles.File(
                                    Paths.get(this.getContainerFolder()).relativize(x).toString(),
                                    new Date(),
                                    new Date(),
                                    x.toFile().length())
                            ).collect(Collectors.toList());
                    blobStoreFiles.add(result);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        return blobStoreFiles;
    }

    @Override
    public void copyBlob(String sourceObjectName, String targetFolder, String targetObjectName) {
        Path sourceLocalPath = Paths.get(sourceObjectName);
        Path sourceFullPath = Paths.get(getContainerFolder()).resolve(sourceLocalPath);
        Path targetLocalPath = Paths.get(targetObjectName);
        Path targetFullPath = Paths.get(prependBasePath(targetFolder)).resolve(targetLocalPath);
        try {

            // create target parent directories if missing
            Path parentDirectory = targetLocalPath.getParent();
            Path folder = parentDirectory == null ? Paths.get(targetFolder) : Paths.get(targetFolder).resolve(parentDirectory);
            Files.createDirectories(folder);

            Files.deleteIfExists(targetFullPath);

            Files.copy(sourceFullPath, targetFullPath);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
