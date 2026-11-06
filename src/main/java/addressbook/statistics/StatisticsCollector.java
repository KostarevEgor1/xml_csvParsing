package addressbook.statistics;

import addressbook.model.Address;
import java.util.HashMap;
import java.util.Map;

public class StatisticsCollector {
    private final Map<Address, Integer> occurrences = new HashMap<>();
    private final Map<String, Map<Integer, Integer>> buildingsByCity = new HashMap<>();

    public void add(Address address) {
        int count = occurrences.merge(address, 1, Integer::sum);
        if (count > 1) {
            return;
        }

        Map<Integer, Integer> floors = buildingsByCity.computeIfAbsent(
                address.city(), city -> createFloorCounts());
        floors.merge(address.floor(), 1, Integer::sum);
    }

    public Map<Address, Integer> getDuplicates() {
        Map<Address, Integer> duplicates = new HashMap<>();
        for (var entry : occurrences.entrySet()) {
            if (entry.getValue() > 1) {
                duplicates.put(entry.getKey(), entry.getValue());
            }
        }
        return duplicates;
    }

    public Map<String, Map<Integer, Integer>> getBuildingsByCity() {
        return buildingsByCity;
    }

    private Map<Integer, Integer> createFloorCounts() {
        Map<Integer, Integer> floors = new HashMap<>();
        for (int floor = 1; floor <= 5; floor++) {
            floors.put(floor, 0);
        }
        return floors;
    }
}
