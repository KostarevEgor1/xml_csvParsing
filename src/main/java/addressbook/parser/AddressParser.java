package addressbook.parser;

import addressbook.model.Address;
import java.nio.file.Path;
import java.util.function.Consumer;

public interface AddressParser {
    void parse(Path path, Consumer<Address> consumer);
}
