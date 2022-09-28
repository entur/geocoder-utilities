package org.entur.geocoder.csv;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.entur.geocoder.model.PeliasDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class CSVReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSVCreator.class);

    public static Stream<PeliasDocument> read(Path csvFilePath) {
        LOGGER.debug("Reading pelias documents from " + csvFilePath);
        try {
            // Intentionally not closing the reader, since we are using the stream further in the process.
            Reader reader = Files.newBufferedReader(csvFilePath);
            CsvToBean<PeliasDocument> cb = new CsvToBeanBuilder<PeliasDocument>(reader)
                    .withType(PeliasDocument.class)
                    .withSeparator(';')
                    .build();
            return cb.stream();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
