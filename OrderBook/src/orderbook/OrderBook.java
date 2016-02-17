/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orderbook;

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
            Processor p = new Processor(args[0]);
            p.process();
            p.showResults();
        } else {
            System.out.println("Usage: java -jar orderbook.jar <file-with-orders.xml>");
        }
    }
}
