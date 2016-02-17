/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orderbook;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    void addToBids(Integer id, Double price, Integer volume) {//throws OrdersException {
//        if (bids.containsKey(id)) {
//            throw new OrdersException("Bids already contains the key " + id);
//        }

        final Set<Order> toBeRemoved = new HashSet<>();

        final Order order = new Order(id, price, volume);

        for (Order o : sortedAsks.headSet(order, true)) {
            if (volume >= o.volume) {
                volume -= o.volume;
                toBeRemoved.add(o);
            } else {
                o.volume -= volume;
                volume = 0;
            }

            if (volume <= 0) {
                break;
            }
        }

        for (Order o : toBeRemoved) {
            asks.remove(o.id);
            sortedAsks.remove(o);
        }

        if (volume > 0) {
            order.volume = volume;
            bids.put(id, order);
            sortedBids.add(order);
        }
    }

    void addToAsks(Integer id, Double price, Integer volume) {//throws OrdersException {
//        if (asks.containsKey(id)) {
//            throw new OrdersException("Asks already contains the key " + id);
//        }

        final Set<Order> toBeRemoved = new HashSet<>();

        final Order order = new Order(id, price, volume);

        for (Order o : sortedBids.headSet(order, true)) {
            if (volume >= o.volume) {
                volume -= o.volume;
                toBeRemoved.add(o);
            } else {
                o.volume -= volume;
                volume = 0;
            }

            if (volume <= 0) {
                break;
            }
        }

        for (Order o : toBeRemoved) {
            bids.remove(o.id);
            sortedBids.remove(o);
        }

        if (volume > 0) {
            order.volume = volume;
            asks.put(id, order);
            sortedAsks.add(order);
        }
    }

    void delete(Integer id) {//throws OrdersException {
//        if (bids.containsKey(id) && asks.containsKey(id)) {
//            throw new OrdersException("The key " + id + " exists in both lists");
//        }

        if (bids.containsKey(id)) {
            sortedBids.remove(bids.remove(id));
        } else if (asks.containsKey(id)) {
            sortedAsks.remove(asks.remove(id));
        }
    }

    void printOut() {
        System.out.println("BID             ASK");
        System.out.println("Volume@Price  - Volume@Price");

        final List<String> l = new ArrayList<>();

        for (Order o : sortedBids) {
            l.add(String.format("%6d@%6.2f", o.volume, o.price));
        }

        int i = 0;
        for (Order o : sortedAsks) {
            String askData = String.format(" - %6d@%6.2f", o.volume, o.price);

            if (i < sortedBids.size()) {
                l.set(i, l.get(i) + askData);
            } else {
                l.add("    ---------" + askData);
            }

            i++;
        }

        if (sortedBids.size() > sortedAsks.size()) {
            for (int j = sortedAsks.size(); j < l.size(); j++) {
                l.set(j, l.get(j) + " -     ---------");
            }
        }

        for (String s : l) {
            System.out.println(s);
        }
    }

    private class Order {

        Integer id;
        Double price;
        Integer volume;

        public Order(Integer id, Double price, Integer volume) {
            this.id = id;
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
