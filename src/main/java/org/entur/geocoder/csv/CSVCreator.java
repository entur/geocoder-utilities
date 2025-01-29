package org.entur.geocoder.csv;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import java.io.*;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.entur.geocoder.model.PeliasDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CSVCreator {

  private static final Logger LOGGER = LoggerFactory.getLogger(
    CSVCreator.class
  );

  public static InputStream create(Stream<PeliasDocument> peliasDocuments) {
    LOGGER.debug("Creating CSV file for pelias documents");

    try {
      File file = File.createTempFile("output", "csv");
      Path path = file.toPath();
      try (Writer writer = new FileWriter(path.toString())) {
        StatefulBeanToCsv<PeliasDocument> sbc =
          new StatefulBeanToCsvBuilder<PeliasDocument>(writer)
            .withQuotechar('\'')
            .withEscapechar('\\')
            .withSeparator(';')
            .build();

        sbc.write(peliasDocuments);
        return new FileInputStream(file);
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}
