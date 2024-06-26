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

package org.nataliia_petrenko;

public class Main {
    public static void main(String[] args) {
        System.out.println(maxValue(-5, 6));
        System.out.println(minValue(-5, 6));
    }

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
}