package net.stellar.mining.utils;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RewardStorage<E> {

    private final NavigableMap<Double, E> map;
    @SuppressWarnings("unused")
    private Random random = null;
    private double total;

    public RewardStorage() {
        this(new Random());
    }

    public RewardStorage(Random random) {
        this.map = new TreeMap<>();
        this.total = 0.0D;
        this.random = random;
    }

    public int size() {
        return this.map.size();
    }

    public void add(double weight, E result) {
        if (weight > 0.0D) {
            this.total += weight;
            this.map.put(this.total, result);
        }
    }

    public void remove() {
        map.clear();
    }

    public double randomChance() {
        return Math.random() * 100.0D;
    }

    public E next() {
        if(total > 0) {
            double value = Math.random() * this.total;
            return this.map.higherEntry(value).getValue();
        } else {
            return null;
        }
    }

    public E get(int i) {
        if(i >= 0 && i < this.map.size()) {
            Object obj = this.map.keySet().toArray()[i];
            return this.map.get(obj);
        } else {
            return null;
        }
    }

    public void destroy() {
        this.random = null;
        this.map.clear();
        this.total = 0.0;
    }
}
