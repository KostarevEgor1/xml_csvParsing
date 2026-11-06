package addressbook.parser;

import addressbook.model.Address;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class XmlAddressParser implements AddressParser {
    @Override
    public void parse(Path path, Consumer<Address> consumer) {
        XMLInputFactory factory = XMLInputFactory.newFactory();

        try (InputStream input = Files.newInputStream(path)) {
            XMLStreamReader reader = factory.createXMLStreamReader(input);
            try {
                while (reader.hasNext()) {
                    if (reader.next() == XMLStreamConstants.START_ELEMENT
                            && reader.getLocalName().equals("item")) {
                        Address address = AddressFactory.create(
                                reader.getAttributeValue(null, "city"),
                                reader.getAttributeValue(null, "street"),
                                reader.getAttributeValue(null, "house"),
                                reader.getAttributeValue(null, "floor"),
                                "в строке " + reader.getLocation().getLineNumber());
                        consumer.accept(address);
                    }
                }
            } finally {
                reader.close();
            }
        } catch (IOException exception) {
            throw new UncheckedIOException("Не удалось прочитать XML-файл: " + path, exception);
        } catch (XMLStreamException exception) {
            throw new IllegalArgumentException("Некорректный XML: " + exception.getMessage(), exception);
        }
    }
}
