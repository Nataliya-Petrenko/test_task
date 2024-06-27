/*
Максимальне число в файлі
Мінімальне число в файлі
Медіану
    (парна кількість елементів -> півсума двох сусідніх значень.
    Тобто наприклад, у наборі {1, 8, 14, 19} медіаною буде 11 (бо 0.5*(8+14)=11).)
Середнє арифметичне значення
* Найбільшу послідовність чисел (які ідуть один за одним), яка збільшується (опціонально)
* Найбільшу послідовність чисел (які ідуть один за одним), яка зменшується (опціонально)

час не більше ніж за 40 секунд
 */

import java.util.PriorityQueue;

public class Util {
//    public static void main(String[] args) {
//        System.out.println(maxValue(-5, 6));
//        System.out.println(minValue(-5, 6));
//    }

    public static int maxValue(final int first, final int second) {
        if (first >= second) {
            return first;
        } else {
            return second;
        }
    }

    public static int minValue(final int first, final int second) {
        if (first >= second) {
            return second;
        } else {
            return first;
        }
    }

    public static void streamMed(int array[]) {
        int n = array.length;
        // Declaring two min heap
        PriorityQueue<Double> greaterValues = new PriorityQueue<>();
        PriorityQueue<Double> smallerValues = new PriorityQueue<>();
        for (int i = 0; i < n; i++) {

            // Negation for treating it as max heap
            smallerValues.add(-1.0 * array[i]); // Negate array[i] and add to s to simulate max-heap behavior.
            greaterValues.add(-1.0 * smallerValues.poll()); // Move the largest element
            // from s to g to keep s as a max-heap with the smaller half and g as a min-heap with the larger half.
            if (greaterValues.size() > smallerValues.size()) // Balance the heaps:
                // if g has more elements than s, move the smallest element from g back to s.
                smallerValues.add(-1.0 * greaterValues.poll());

        }

        if (greaterValues.size() != smallerValues.size())
            System.out.println(-1.0 * smallerValues.peek());
        else
            System.out.println((greaterValues.peek() - smallerValues.peek()) / 2);

    }
}