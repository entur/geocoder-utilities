package org.entur.geocoder.csv;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.entur.geocoder.model.PeliasDocument;
import org.entur.geocoder.model.PeliasDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class CSVReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSVCreator.class);

    public static PeliasDocumentList read(Path csvFilePath) {

        LOGGER.debug("Reading pelias documents from " + csvFilePath);

        try (Reader reader = Files.newBufferedReader(csvFilePath)) {
            CsvToBean<PeliasDocument> cb = new CsvToBeanBuilder<PeliasDocument>(reader)
                    .withType(PeliasDocument.class)
                    .withSeparator(';')
                    .withEscapeChar('\\')
                    .withQuoteChar('\'')
                    .build();

            return (PeliasDocumentList) cb.parse();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
