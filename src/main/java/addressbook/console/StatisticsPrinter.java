package addressbook.console;

import addressbook.model.Address;
import addressbook.statistics.StatisticsCollector;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;

public class StatisticsPrinter {
    public void print(StatisticsCollector statistics, long elapsedNanos) {
        printDuplicates(statistics.getDuplicates());
        printBuildings(statistics.getBuildingsByCity());
        System.out.printf(Locale.ROOT, "Время обработки файла: %.3f с%n%n",
                elapsedNanos / 1_000_000_000.0);
    }

    private void printDuplicates(Map<Address, Integer> duplicates) {
        System.out.println("Дублирующиеся записи:");
        if (duplicates.isEmpty()) {
            System.out.println("Дубликатов нет.");
            return;
        }

        List<Address> addresses = new ArrayList<>(duplicates.keySet());
        addresses.sort(Comparator.comparing(Address::city)
                .thenComparing(Address::street)
                .thenComparing(Address::house)
                .thenComparingInt(Address::floor));

        for (Address address : addresses) {
            System.out.printf("%s, %s, дом %s, этажность %d — количество появлений: %d%n",
                    address.city(), address.street(), address.house(), address.floor(), duplicates.get(address));
        }
    }

    private void printBuildings(Map<String, Map<Integer, Integer>> buildingsByCity) {
        System.out.println("Количество домов по этажности без учёта дубликатов:");
        if (buildingsByCity.isEmpty()) {
            System.out.println("Записей нет.");
            return;
        }

        for (var entry : new TreeMap<>(buildingsByCity).entrySet()) {
            StringJoiner counts = new StringJoiner(", ");
            for (int floor = 1; floor <= 5; floor++) {
                counts.add(floor + " эт.: " + entry.getValue().get(floor));
            }
            System.out.println(entry.getKey() + ": " + counts);
        }
    }
}
