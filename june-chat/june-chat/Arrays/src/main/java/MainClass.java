import java.util.Arrays;

public class MainClass {

    public static void main(String[] args) {
        int[] array1 = {1, 2, 4, 4, 2, 3, 4, 1, 7,};
        int[] array2 = {4, 1, 2, 3, 3, 5, 6, 7};
        int[] array3 = {1, 1, 4, 1, 1, 1, 1, 1};

        ArrayApp arrayApp = new ArrayApp();

        System.out.println(Arrays.toString(arrayApp.splitArray(array2)));
        System.out.println(arrayApp.isOneOrFour(array3));
    }

}
