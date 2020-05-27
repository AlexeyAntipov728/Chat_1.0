package lesson1;

import java.util.ArrayList;

public class Box<T extends Fruit> {
    private ArrayList<T> box = new ArrayList<T>();

    public Box() {

    }

    public float getWeight() {
        float weight = 0f;
        for (T o : box) {
            weight += o.getWeight();
        }
        return weight;
    }

    public boolean compare(Box anotherBox) {
        if (getWeight() == anotherBox.getWeight()) return true;
        return false;
    }

    public void addFruit(T fruit, int volume) {
        for (int i = 0; i < volume; i++) {
            box.add(fruit);
        }
    }

    public void placer(Box<T> anotherBox) {
        anotherBox.box.addAll(box);
        box.clear();
    }
}


