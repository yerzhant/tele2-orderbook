/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orderbook;

import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

/**
 *
 * @author yerzhan
 */
class Orders {

    private final HashMap<Integer, Order> bids = new HashMap<>();
    private final HashMap<Integer, Order> asks = new HashMap<>();

    private final TreeSet<Order> sortedBids = new TreeSet<>(new BidComparator());
    private final TreeSet<Order> sortedAsks = new TreeSet<>(new AskComparator());

    void addToBids(Integer id, Double price, Integer volume) throws OrdersException {
        if (bids.containsKey(id)) {
            throw new OrdersException("Bids already contains the key " + id);
        }

        final Order o = new Order(price, volume);

        bids.put(id, o);
        sortedBids.add(o);
    }

    void addToAsks(Integer id, Double price, Integer volume) throws OrdersException {
        if (asks.containsKey(id)) {
            throw new OrdersException("Asks already contains the key " + id);
        }

        final Order o = new Order(price, volume);

        asks.put(id, o);
        sortedAsks.add(o);
    }

    void delete(Integer id) throws OrdersException {
        if (bids.containsKey(id) && asks.containsKey(id)) {
            throw new OrdersException("The key " + id + " exists in both lists");
        }

        if (bids.containsKey(id)) {
            sortedBids.remove(bids.remove(id));
        } else {
            sortedAsks.remove(asks.remove(id));
        }
    }

    private class Order {

        Double price;
        Integer volume;

        public Order(Double price, Integer volume) {
            this.price = price;
            this.volume = volume;
        }
    }

    private class BidComparator implements Comparator<Order> {

        @Override
        public int compare(Order o1, Order o2) {
            return o2.price.compareTo(o1.price);
        }
    }

    private class AskComparator implements Comparator<Order> {

        @Override
        public int compare(Order o1, Order o2) {
            return o1.price.compareTo(o2.price);
        }
    }
}
