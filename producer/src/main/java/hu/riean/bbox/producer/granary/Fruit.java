/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.riean.bbox.producer.granary;

/**
 *
 * @author riean
 */
public class Fruit {

    String name;
    int price;

    public Fruit() {
    }

    public String getName() {
        return name;
    }

    public Fruit setName(String name) {
        this.name = name;
        return this;
    }

    public int getPrice() {
        return price;
    }

    public Fruit setPrice(int price) {
        this.price = price;
        return this;
    }

    @Override
    public String toString() {
        return "Fruit{" + "name=" + name + ", price=" + price + '}';
    }
    

}
