/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orderbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
class Processor extends DefaultHandler {

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

    private final HashMap<String, Orders> bookOrders = new HashMap<>();

    Processor(String fileName) {
        this.fileName = fileName;
    }

    void process() throws ParserConfigurationException, SAXException, FileNotFoundException, IOException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        SAXParser sp = spf.newSAXParser();
        XMLReader xr = sp.getXMLReader();
        xr.setContentHandler(this);
        try (FileInputStream is = new FileInputStream(fileName)) {
            xr.parse(new InputSource(is));
        }
    }

    void showResults() {
        for (Map.Entry<String, Orders> e : bookOrders.entrySet()) {
            System.out.println("Order book: " + e.getKey());
            e.getValue().printOut();
            System.out.println("");
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (Actions.valueOf(localName)) {
            case AddOrder:
                Integer id = Integer.valueOf(attributes.getValue(ATTR_ORDER_ID));
                String book = attributes.getValue(ATTR_BOOK);
                Operations operation = Operations.valueOf(attributes.getValue(ATTR_OPERATION));
                Double price = Double.valueOf(attributes.getValue(ATTR_PRICE));
                Integer volume = Integer.valueOf(attributes.getValue(ATTR_VOLUME));

                try {
                    add(book, operation, price, volume, id);
                } catch (OrdersException ex) {
                    throw new SAXException(ex);
                }

                break;

            case DeleteOrder:
                id = Integer.valueOf(attributes.getValue(ATTR_ORDER_ID));
                book = attributes.getValue(ATTR_BOOK);

                try {
                    delete(book, id);
                } catch (OrdersException ex) {
                    throw new SAXException(ex);
                }

                break;

            case Orders:
                break;

            default:
                throw new AssertionError();
        }
    }

    private void add(String book, Operations operation, Double price, Integer volume, Integer id) throws OrdersException {
        if (!bookOrders.containsKey(book)) {
            bookOrders.put(book, new Orders());
        }

        Orders o = bookOrders.get(book);

        switch (operation) {
            case BUY:
                o.addToBids(id, price, volume);
                break;

            case SELL:
                o.addToAsks(id, price, volume);
                break;

            default:
                throw new AssertionError();
        }
    }

    private void delete(String book, Integer id) throws OrdersException {
        bookOrders.get(book).delete(id);
    }
}
