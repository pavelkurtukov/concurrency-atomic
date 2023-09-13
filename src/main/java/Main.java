import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static AtomicInteger beauty3 = new AtomicInteger(0); // Кол-во красивых слов длиной 3
    static AtomicInteger beauty4 = new AtomicInteger(0); // Кол-во красивых слов длиной 4
    static AtomicInteger beauty5 = new AtomicInteger(0); // Кол-во красивых слов длиной 5

    public static void main(String[] args) throws InterruptedException {
        // Генерируем слова
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        // Подсчёт полиндромов (слов, читающихся слева направо и справа налево одинаково)
        Runnable polyndromeCheck = () -> {
            for (String text : texts) {
                String reverseText = new StringBuilder(text).reverse().toString();
                if (text.equals(reverseText)) {
                    increaseCounter(text.length());
                }
            }
        };

        // Подсчёт слов, состоящих только из одной повторяющейся буквы
        Runnable singleLetterCheck = () -> {
            for (String text : texts) {
                String reverseText = new StringBuilder(text).reverse().toString();
                if (text.replace(text.substring(0, 1), "").length() == 0) {
                    increaseCounter(text.length());
                }
            }
        };

        // Подсчёт слов с буквами по возрастанию
        Runnable ascendingLettersCheck = () -> {
            for (String text : texts) {
                int i = 0;
                boolean isAscendingLetters = false;
                while(i < text.length() - 1) {
                    if (text.charAt(i) <= text.charAt(i + 1))
                        isAscendingLetters = true;
                    else{
                        isAscendingLetters = false;
                        break;
                    }
                    i++;
                }
                if (isAscendingLetters) {
                    increaseCounter(text.length());
                }
            }
        };

        // Определяем потоки
        Thread polyndromCheckThread = new Thread(polyndromeCheck);
        Thread singleLetterCheckThread = new Thread(singleLetterCheck);
        Thread ascendingLettersCheckThread = new Thread(ascendingLettersCheck);

        // Стартуем потоки
        polyndromCheckThread.start();
        singleLetterCheckThread.start();
        ascendingLettersCheckThread.start();

        // Ждём окончания потоков
        polyndromCheckThread.join();
        singleLetterCheckThread.join();
        ascendingLettersCheckThread.join();

        // Выводим результат
        System.out.printf("Красивых слов с длиной 3: %d шт\n", beauty3.intValue());
        System.out.printf("Красивых слов с длиной 4: %d шт\n", beauty4.intValue());
        System.out.printf("Красивых слов с длиной 5: %d шт\n", beauty5.intValue());
    }

    // Генератор слов
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    // Инкремент счётчика красивых слов в зависимости от длины слова
    private static void increaseCounter(int length) {
        if (length == 3) beauty3.getAndIncrement();
        if (length == 4) beauty4.getAndIncrement();
        if (length == 5) beauty5.getAndIncrement();
    }
}
