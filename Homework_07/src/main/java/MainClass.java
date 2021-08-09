import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainClass {

    public static void main(String[] args) {
        JazzBand jazzBand = new JazzBand("Magic Orchestra"); // создадим новую группу

        Player player1 = new Player("Eric Dolphy", "saxophone"); // создадим новых музыкантов
        Player player2 = new Player("Erroll Garner", "piano");
        Player player3 = new Player("John Scofield", "guitar");
        Player player4 = new Player("Buddy Rich", "drums");
        Player player5 = new Player("Charles Mingus", "bass");

        jazzBand.addPlayerToBand(player1); // объединим музыкантов в группу
        jazzBand.addPlayerToBand(player2);
        jazzBand.addPlayerToBand(player3);
        jazzBand.addPlayerToBand(player4);
        jazzBand.addPlayerToBand(player5);

        jazzBand.bandInfo(); // выведем информацию о музыкантах

        Random random = new Random();

        while (!jazzBand.disBand()) { // запустим жизненный цикл группы
            jazzBand.playConcert(random.nextInt(100) + 100); // отыграем концерт
            System.out.println("*********************************");
            jazzBand.createNewAlbum(); // запишем новый альбом
            jazzBand.playConcert(random.nextInt(100) + 100); // отыграем концерт
            System.out.println("*********************************");
            jazzBand.giveInterview(); // дадим интервью
            jazzBand.goTouring(random.nextInt(5) + 1, random.nextInt(50) + 50);// отправим группу в тур
        }


        start(JazzBand.class); // запускаем тестирование

    }


    public static void start(Class<JazzBand> testClass) { // метод запускающий тесты

        JazzBand jazzBand1 = new JazzBand("Proxy");
        Class jazzBandClass = JazzBand.class;

        Method[] methods = testClass.getDeclaredMethods();
        //System.out.println(Arrays.toString(methods));
        int beforeSuite = 0;
        int afterSuite = 0;

        for (Method method : methods) {
            if (method.getAnnotation(BeforeSuite.class) != null) { //здесь почему-то компилятор подчеркивает ошибку "Cannot access BeforeSuite", но все работает
                beforeSuite++;
            }

            //System.out.println(Arrays.toString(method.getDeclaredAnnotations()));

            if (method.getAnnotation(AfterSuite.class) != null) { //здесь почему-то компилятор подчеркивает ошибку "Cannot access BeforeSuite", но все работает
                afterSuite++;
            }
        }


        if (beforeSuite != 1 || afterSuite != 1) { // проверяем, что аннотации встречаются 1 раз
            throw new RuntimeException();
        }


        for (Method method : methods) {// протестируем методы с аннтоацией BeforeSuite
            if (method.getAnnotation(BeforeSuite.class) != null) {
                method.setAccessible(true);
                try {
                    System.out.println("Протестирован метод " + method.getName() + Arrays.toString(method.getParameterTypes()) + ". Результат работы: " + method.invoke(jazzBand1));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }


        for (int i = 0; i < methods.length; i++) { // протестируем методы с аннтоацией Test в порядке увеличения value
            for (int j = 0; j < methods.length; j++) {
                if (methods[j].getAnnotation(AfterSuite.class) == null && methods[j].getAnnotation(BeforeSuite.class) == null && methods[j].getAnnotation(Test.class).value() == i) {
                    System.out.println("Протестирован метод " + methods[j].getName() + Arrays.toString(methods[j].getParameterTypes()));
                }
            }
        }


        for (Method method : methods) { // протестируем методы с аннтоацией AfterSuite
            if (method.getAnnotation(AfterSuite.class) != null) {
                method.setAccessible(true);
                try {
                    System.out.println("Протестирован метод " + method.getName() + Arrays.toString(method.getParameterTypes()) + ". Результат работы: " + method.invoke(jazzBand1));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }


    }

}
