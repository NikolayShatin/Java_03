//1.	Написать метод, которому в качестве аргумента передается не пустой одномерный целочисленный массив.
// Метод должен вернуть новый массив, который получен путем вытаскивания из исходного массива элементов,
// идущих после последней четверки. Входной массив должен содержать хотя бы одну четверку, иначе в методе необходимо
// выбросить RuntimeException. Написать набор тестов для этого метода (по 3-4 варианта входных данных).
//        Вх: [ 1 2 4 4 2 3 4 1 7 ] -> вых: [ 1 7 ].
//2.	Написать метод, который проверяет состав массива из чисел 1 и 4. Если в нем нет хоть одной четверки или единицы,
// то метод вернет false; Написать набор тестов для этого метода (по 3-4 варианта входных данных).
//        [ 1 1 1 4 4 1 4 4 ] -> true
//        [ 1 1 1 1 1 1 ] -> false
//        [ 4 4 4 4 ] -> false
//        [ 1 4 4 1 1 4 3 ] -> false


import java.util.Arrays;

public class ArrayApp {

    public boolean isOneOrFour(int[] array) { // проверяет состав массива из чисел 1 и 4
        int count_4 = 0;
        int count_1 = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != 4 && array[i] != 1) {
                return false;
            }
            if (array[i] == 1) {
                count_1++;
            }

            if (array[i] == 4) {
                count_4++;
            }

        }

        return count_1 > 0 && count_4 > 0;
    }

    public int[] splitArray(int[] array) { // метод, отделяющий часть, идущую после значения 4
        if (array.length == 0) {
            throw new NullPointerException();
        }
        for (int i = array.length - 1; i >= 0; i--) {

            if (array[i] == 4) {
                int[] temp = new int[array.length - i - 1];
                System.arraycopy(array, i + 1, temp, 0, array.length - i - 1);
                array = temp;
                return array;
            }
        }
        throw new RuntimeException();
    }
}
