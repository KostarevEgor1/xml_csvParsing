package addressbook.parser;

import addressbook.model.Address;

final class AddressFactory {
    private AddressFactory() {
    }

    static Address create(String city, String street, String house, String floorValue, String location) {
        if (city == null || city.isBlank() || street == null || street.isBlank()
                || house == null || house.isBlank()) {
            throw new IllegalArgumentException("Пустое поле адреса " + location);
        }

        int floor;
        try {
            floor = Integer.parseInt(floorValue);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Этажность должна быть целым числом " + location, exception);
        }
        if (floor < 1 || floor > 5) {
            throw new IllegalArgumentException("Этажность должна быть от 1 до 5 " + location);
        }

        return new Address(city, street, house, floor);
    }
}
