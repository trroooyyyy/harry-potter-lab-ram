package edu.pro;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws IOException {

        // Старт моніторингу часу
        LocalDateTime start = LocalDateTime.now();

        // Старт моніторингу пам'яті
        Runtime runtime = Runtime.getRuntime();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

        // Читаємо файл
        String content = new String(Files.readAllBytes(Paths.get("src/edu/pro/txt/harry.txt")));

        // Очищуємо текст і переводимо в нижній регістр
        content = content.replaceAll("[^A-Za-z ]"," ").toLowerCase(Locale.ROOT);

        // Ділимо текст на слова
        String[] words = content.split(" +"); // ~400 000

        // Сортуємо масив слів
        Arrays.sort(words);

        // Створюємо масив унікальних слів і їх частот
        String[] distinctWords = new String[words.length];
        int[] frequencies = new int[words.length];
        int uniqueWordCount = 0;

        // Підраховуємо частоти слів
        for (int i = 0; i < words.length; i++) {
            if (i == 0 || !words[i].equals(words[i - 1])) {
                distinctWords[uniqueWordCount] = words[i];
                frequencies[uniqueWordCount] = 1;
                uniqueWordCount++;
            } else {
                frequencies[uniqueWordCount - 1]++;
            }
        }

        // Обрізаємо масив до фактичного розміру
        distinctWords = Arrays.copyOf(distinctWords, uniqueWordCount);
        frequencies = Arrays.copyOf(frequencies, uniqueWordCount);

        // Сортуємо за частотою
        for (int i = 0; i < uniqueWordCount - 1; i++) {
            for (int j = i + 1; j < uniqueWordCount; j++) {
                if (frequencies[i] < frequencies[j]) {
                    // Міняємо місцями слова і частоти
                    String tempWord = distinctWords[i];
                    distinctWords[i] = distinctWords[j];
                    distinctWords[j] = tempWord;

                    int tempFreq = frequencies[i];
                    frequencies[i] = frequencies[j];
                    frequencies[j] = tempFreq;
                }
            }
        }

        // Виводимо 30 найчастіших слів
        for (int i = 0; i < 30 && i < uniqueWordCount; i++) {
            System.out.println(distinctWords[i] + " " + frequencies[i]);
        }


        // Кінець моніторингу часу
        LocalDateTime finish = LocalDateTime.now();

        // Кінець моніторингу пам'яті
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = memoryAfter - memoryBefore;

        System.out.println("------");
        System.out.println(ChronoUnit.MILLIS.between(start, finish) + " ms");

        // Виведення використаної пам'яті
        System.out.println("Memory used: " + memoryUsed / (1024 * 1024) + " MB");

        /* старий код. По замірам працює повільніше та займає приблизно в 10 разів більше оперативної пам'яті

        String distinctString = " ";

        for (int i = 0; i < words.length ; i++) {
            if (!distinctString.contains(words[i])){
                distinctString+= words[i] + " ";
            }
        }

        String[] distincts = distinctString.split(" ");
        int[] freq = new int[distincts.length];

        for (int i = 0; i < distincts.length ; i++) {
            int count = 0;
            for (int j = 0; j < words.length ; j++) {
                if (distincts[i].equals(words[j])) {
                    count++;
                }
            }
            freq[i] = count;
        }

        for (int i = 0; i < distincts.length ; i++) { // 5 000
            distincts[i] += " " + freq[i];
        }

        Arrays.sort(distincts, Comparator.comparing(str
                -> Integer.valueOf(str.replaceAll("[^0-9]", ""))));

        for (int i = 0; i < 30; i++) {
            System.out.println(distincts[distincts.length - 1 - i]);
        }

         */
    }
}
