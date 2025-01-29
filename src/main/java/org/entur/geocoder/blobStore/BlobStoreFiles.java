package org.entur.geocoder.blobStore;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class BlobStoreFiles {

  private final List<File> files = new ArrayList<>();

  public void add(File file) {
    files.add(file);
  }

  public void add(Collection<File> files) {
    this.files.addAll(files);
  }

  public List<File> getFiles() {
    return files;
  }

  public static class File {

    private String name;
    private Date created;
    private Date updated;
    private Long fileSize;
    private String referential;
    private Long providerId;
    private Format format;
    private String url;

    public File(String name, Date created, Date updated, Long fileSize) {
      super();
      this.name = name;
      this.created = created;
      this.updated = updated;
      this.fileSize = fileSize;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Date getUpdated() {
      return updated;
    }

    public void setUpdated(Date updated) {
      this.updated = updated;
    }

    public Long getFileSize() {
      return fileSize;
    }

    public void setFileSize(Long fileSize) {
      this.fileSize = fileSize;
    }

    public Date getCreated() {
      return created;
    }

    public void setCreated(Date created) {
      this.created = created;
    }

    public String getReferential() {
      return referential;
    }

    public void setReferential(String referential) {
      this.referential = referential;
    }

    public Format getFormat() {
      return format;
    }

    public void setFormat(Format format) {
      this.format = format;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public Long getProviderId() {
      return providerId;
    }

    public void setProviderId(Long providerId) {
      this.providerId = providerId;
    }

    public String getFileNameOnly() {
      if (name == null || name.endsWith("/")) {
        return null;
      }

      return Paths.get(name).getFileName().toString();
    }

    @Override
    public String toString() {
      return (
        "File [name=" +
        name +
        ", created=" +
        created +
        ", updated=" +
        updated +
        ", fileSize=" +
        fileSize +
        "]"
      );
    }

    public enum Format {
      NETEX,
      GTFS,
      GRAPH,
      UNKNOWN,
    }
  }
}
