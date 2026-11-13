package addressbook.parser;

import addressbook.model.Address;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CsvAddressParser implements AddressParser {
    private static final List<String> HEADERS = List.of("city", "street", "house", "floor");
    private static final CSVFormat FORMAT = CSVFormat.DEFAULT.builder()
            .setDelimiter(';')
            .setHeader()
            .setSkipHeaderRecord(true)
            .get();

    @Override
    public void parse(Path path, Consumer<Address> consumer) {
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
                CSVParser parser = FORMAT.parse(reader)) {
            List<String> headers = parser.getHeaderNames();
            if (headers.size() != HEADERS.size() || !headers.containsAll(HEADERS)) {
                throw new IllegalArgumentException("CSV должен содержать столбцы city, street, house, floor");
            }

            for (CSVRecord record : parser) {
                consumer.accept(toAddress(record));
            }
        } catch (IOException exception) {
            throw new UncheckedIOException("Не удалось прочитать CSV-файл: " + path, exception);
        }
    }

    private Address toAddress(CSVRecord record) {
        if (!record.isConsistent()) {
            throw new IllegalArgumentException("Неверное количество полей в записи " + record.getRecordNumber());
        }

        return AddressFactory.create(record.get("city"), record.get("street"),
                record.get("house"), record.get("floor"), "в записи " + record.getRecordNumber());
    }
}
