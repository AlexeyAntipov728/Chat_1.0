package lesson1;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        //    Задание 1. Написать метод, который меняет два элемента массива местами (массив может быть любого ссылочного типа);
        Integer arr1[] = {1, 2, 3, 4, 5, 6, 7};
        String arr2[] = {"A", "B", "C"};
        changePosition(arr1, 1, 4);
        changePosition(arr2, 0, 2);


        //        Задание 2. Написать метод, который преобразует массив в ArrayList;
        String[] arr = {"A", "B", "C", "D"};
        toList(arr);


        //        Задание 3. Задача:
        //  a. Даны классы Fruit, Apple extends Fruit, Orange extends Fruit;
        //  b. Класс Box, в который можно складывать фрукты. Коробки условно сортируются по типу фрукта, поэтому в одну коробку нельзя сложить и яблоки, и апельсины;
        //  c. Для хранения фруктов внутри коробки можно использовать ArrayList;
        //  d. Сделать метод getWeight(), который высчитывает вес коробки.
        //     Задать вес одного фрукта и их количество: вес яблока – 1.0f, апельсина – 1.5f (единицы измерения не важны);
        //  e. Внутри класса Box сделать метод compare(), который позволяет сравнить текущую коробку с той, которую подадут в compare() в качестве параметра.
        //     true – если их массы равны, false в противоположном случае. Можно сравнивать коробки с яблоками и апельсинами;
        //  f. Написать метод, который позволяет пересыпать фрукты из текущей коробки в другую. Помним про сортировку фруктов: нельзя яблоки высыпать в коробку с апельсинами.
        //     Соответственно, в текущей коробке фруктов не остается, а в другую перекидываются объекты, которые были в первой;
        //  g. Не забываем про метод добавления фрукта в коробку.

//        Создали коробки под фрукты
        Box<Orange> orange = new Box<>();
        Box<Orange> orange2 = new Box<>();
        Box<Apple> apple = new Box<>();
        Box<Apple> apple2 = new Box<>();

//        Добавляем фрукты в коробку
        orange.addFruit(new Orange(), 10);
        orange2.addFruit(new Orange(), 20);
        apple.addFruit(new Apple(), 5);
        apple2.addFruit(new Apple(), 15);

//        Получаем вес наполненных коробок
        System.out.println("Box 1: " + orange.getWeight());
        System.out.println("Box 2: " + orange2.getWeight());
        System.out.println("Box 3: " + apple.getWeight());
        System.out.println("Box 4: " + apple2.getWeight());

//      Сравниваем коробки по весу
        System.out.println("Box 1 equals box 3: " + orange.compare(apple));
        System.out.println("Box 2 equals box 4: " + orange2.compare(apple2));

//      Перемещаем из одной коробки в другую
        orange.placer(orange2);
        apple.placer(apple2);
        System.out.println("Box 1: " + orange.getWeight());
        System.out.println("Box 2: " + orange2.getWeight());
        System.out.println("Box 3: " + apple.getWeight());
        System.out.println("Box 4: " + apple2.getWeight());
    }

    public static void changePosition(Object[] arr, int n1, int n2) {
        System.out.println("Было: " + Arrays.toString(arr));
        Object obj = arr[n1];
        arr[n1] = arr[n2];
        arr[n2] = obj;
        System.out.println("Стало: " + Arrays.toString(arr));
    }

    public static <T> void toList(T[] arr) {
        ArrayList<T> conversion = new ArrayList<T>(Arrays.asList(arr));
        System.out.println("Результат конвертации: " + conversion);
    }
}

