package addressbook.console;

import addressbook.parser.AddressParser;
import addressbook.parser.CsvAddressParser;
import addressbook.parser.XmlAddressParser;
import addressbook.statistics.StatisticsCollector;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Scanner;

public class ConsoleApplication {
    public void run() {
        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            while (true) {
                System.out.print("Введите путь к CSV/XML-файлу или exit для выхода: ");
                if (!scanner.hasNextLine()) {
                    return;
                }

                String input = scanner.nextLine().strip();
                if (input.equalsIgnoreCase("exit")) {
                    return;
                }

                try {
                    processFile(Path.of(input));
                } catch (IllegalArgumentException | UncheckedIOException exception) {
                    System.out.println("Ошибка: " + exception.getMessage());
                }
            }
        }
    }

    private void processFile(Path path) {
        AddressParser parser = selectParser(path);
        StatisticsCollector statistics = new StatisticsCollector();
        parser.parse(path, statistics::add);
        System.out.println("Файл обработан. Количество городов: " + statistics.getBuildingsByCity().size());
    }

    private AddressParser selectParser(Path path) {
        String filename = path.toString().toLowerCase(Locale.ROOT);
        if (filename.endsWith(".csv")) {
            return new CsvAddressParser();
        }
        if (filename.endsWith(".xml")) {
            return new XmlAddressParser();
        }
        throw new IllegalArgumentException("Поддерживаются только файлы CSV и XML");
    }
}
