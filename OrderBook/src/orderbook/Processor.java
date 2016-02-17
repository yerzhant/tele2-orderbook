/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orderbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author yerzhan
 */
public class Processor extends DefaultHandler {

    private static final String ATTR_BOOK = "book";
    private static final String ATTR_OPERATION = "operation";
    private static final String ATTR_PRICE = "price";
    private static final String ATTR_VOLUME = "volume";
    private static final String ATTR_ORDER_ID = "orderId";

    private enum Actions {

        AddOrder,
        DeleteOrder,
        Orders
    }

    private enum Operations {

        SELL,
        BUY
    }

    private final String fileName;

    public Processor(String fileName) {
        this.fileName = fileName;
    }

    public void process() throws ParserConfigurationException, SAXException, FileNotFoundException, IOException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        SAXParser sp = spf.newSAXParser();
        XMLReader xr = sp.getXMLReader();
        xr.setContentHandler(this);
        xr.parse(new InputSource(new FileInputStream(fileName)));
    }

    public void showResults() {

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (Actions.valueOf(localName)) {
            case AddOrder:
                int orderId = Integer.valueOf(attributes.getValue(ATTR_ORDER_ID));
                String book = attributes.getValue(ATTR_BOOK);
                Operations operation = Operations.valueOf(attributes.getValue(ATTR_OPERATION));
                double price = Double.valueOf(attributes.getValue(ATTR_PRICE));
                int volume = Integer.valueOf(attributes.getValue(ATTR_VOLUME));

                add(book, operation, price, volume, orderId);

                break;

            case DeleteOrder:
                orderId = Integer.valueOf(attributes.getValue(ATTR_ORDER_ID));
                book = attributes.getValue(ATTR_BOOK);

                delete(book, orderId);

                break;

            case Orders:
                break;

            default:
                throw new AssertionError();
        }
    }

    private void add(String book, Operations operation, double price, int volume, int orderId) {

    }

    private void delete(String book, int orderId) {

    }
}
