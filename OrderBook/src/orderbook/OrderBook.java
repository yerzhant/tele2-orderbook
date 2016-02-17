/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orderbook;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author yerzhan
 */
public class OrderBook {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                Processor p = new Processor(args[0]);
                p.process();
                p.showResults();
            } catch (ParserConfigurationException | SAXException | IOException ex) {
                Logger.getLogger(OrderBook.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Usage: java -jar orderbook.jar <file-with-orders.xml>");
        }
    }
}
