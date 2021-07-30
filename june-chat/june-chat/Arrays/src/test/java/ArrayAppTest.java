import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class ArrayAppTest {
    private ArrayApp arrayApp; // объявим класс и будем инициализировать новый объект при старте каждого теста


    @ParameterizedTest
    @MethodSource("params1")
    @DisplayName("Тестирование метода splitArray на обработку входных данных")
    void inputData(int[] input, int[] result) {
        arrayApp = new ArrayApp();

        Assertions.assertArrayEquals(result, arrayApp.splitArray(input));


    }


    @Test
    @DisplayName("Тестирование метода splitArray на ожидаемые исключения") // здесь пытался поймать исключения, но не вышло
    public void exceptionData() {
        arrayApp = new ArrayApp();
        int[] c_input = {};
        int[] d_input = {1, 2, 9, 9, 2, 3, 9, 1, 7};

        Throwable thrown = assertThrows(RuntimeException.class, () -> {
           arrayApp.splitArray(c_input);
           arrayApp.splitArray(d_input);
        });
//        System.out.println(thrown.getMessage().toUpperCase(Locale.ROOT));

       // assertNotNull(thrown.getMessage());


    }


    @ParameterizedTest
    @MethodSource("params2")
    @DisplayName("Тестирование метода isOneOrFour на обработку входных данных")
    public void isOneOrFourTest(int[] input, boolean output) {
        arrayApp = new ArrayApp();
        Assertions.assertEquals(output, arrayApp.isOneOrFour(input));
    }


    private static Stream<Arguments> params1() {
        List<Arguments> argumentsList = new ArrayList<>();
        int[] a_input = {1, 2, 4, 4, 2, 3, 4, 1, 7};
        int[] a_output = {1, 7};
        int[] b_input = {1, 2, 4, 4, 2, 3, 4, 1, 4};
        int[] b_output = {};
        int[] e_input = {4, 2, 9, 9, 2, 3, 9, 1, 7};
        int[] e_output = {2, 9, 9, 2, 3, 9, 1, 7};

        argumentsList.add(Arguments.arguments(a_input, a_output));
        argumentsList.add(Arguments.arguments(b_input, b_output));
        argumentsList.add(Arguments.arguments(e_input, e_output));
        return argumentsList.stream();
    }


    private static Stream<Arguments> params2() {
        List<Arguments> argumentsList = new ArrayList<>();
        int[] a_input = {1, 1, 1, 4, 4, 1, 4, 4};
        boolean a_output = true;
        int[] b_input = {1, 1, 1, 1, 1, 1};
        boolean b_output = false;
        int[] e_input = {4, 4, 4, 4, 4, 4, 4, 4, 4};
        boolean e_output = false;
        int[] f_input = {1, 4, 4, 1, 1, 4, 3};
        boolean f_output = false;

        argumentsList.add(Arguments.arguments(a_input, a_output));
        argumentsList.add(Arguments.arguments(b_input, b_output));
        argumentsList.add(Arguments.arguments(e_input, e_output));
        argumentsList.add(Arguments.arguments(f_input, f_output));
        return argumentsList.stream();
    }





}

//[ 1 2 4 4 2 3 4 1 7 ], [1 7 ]
//[ 1 2 4 4 2 3 4 1 4 ], [  ]
//[  ], NullPointerException()
//[ 1 2 9 9 2 3 9 1 7 ], RuntimeException()
//[ 4 2 9 9 2 3 9 1 7 ], [ 2 9 9 2 3 9 1 7 ]

//        [ 1 1 1 4 4 1 4 4 ] -> true
//        [ 1 1 1 1 1 1 ] -> false
//        [ 4 4 4 4 ] -> false
//        [ 1 4 4 1 1 4 3 ] -> false
