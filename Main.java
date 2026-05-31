import java.util.Arrays;
import java.util.Random;

public class Main {

    // Рівень 1: Наївна рекурсія
    public static long fibNaive(int n) {
        if (n <= 1) return n;
        return fibNaive(n - 1) + fibNaive(n - 2);
    }

    // Рівень 2: Мемоізація
    public static long fibMemo(int n, long[] memo) {
        if (n <= 1) return n;
        if (memo[n] != -1) return memo[n];
        memo[n] = fibMemo(n - 1, memo) + fibMemo(n - 2, memo);
        return memo[n];
    }

    // Рівень 3: Оптимальний компроміс (Ітеративний підхід)
    public static long fibIterative(int n) {
        if (n <= 1) return n;
        long prev2 = 0;
        long prev1 = 1;
        long current = 0;
        for (int i = 2; i <= n; i++) {
            current = prev1 + prev2;
            prev2 = prev1;
            prev1 = current;
        }
        return current;
    }


    // ЧАСТИНА 2: ПОЛІНОМІАЛЬНЕ ХЕШУВАННЯ

    public static int polynomialHash(String s, int P, int M) {
        long hash = 0;
        int L = s.length();
        for (int i = 0; i < L; i++) {
            hash = (hash * P + s.charAt(i)) % M;
        }
        return (int) hash;
    }

    // Допоміжний метод для генерації випадкових слів
    public static String generateRandomWord(Random random, int length) {
        StringBuilder word = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = (char) ('a' + random.nextInt(26));
            word.append(c);
        }
        return word.toString();
    }

    // Допоміжний метод для точного замірювання використаної пам'яті
    public static long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    public static void main(String[] args) {
        System.out.println("ТЕСТУВАННЯ ЧАСТИНИ 1 (Числа Фібоначчі)");

        // Для наївної рекурсії беремо невелике N (наприклад, 40), бо далі буде зависати
        int nNaive = 40;
        int nLarge = 50; // Для мемоізації та ітерацій (можна і більше, наприклад 60 або 70)

        // 1. Замір для Наївної рекурсії
        System.gc();
        long memBefore = getUsedMemory();
        long startTime = System.nanoTime();

        long resNaive = fibNaive(nNaive);

        long endTime = System.nanoTime();
        long memAfter = getUsedMemory();
        System.out.printf("Наївна рекурсія (N=%d): Результат = %d | Час = %d нс | Пам'ять = %d байт\n",
                nNaive, resNaive, (endTime - startTime), Math.max(0, memAfter - memBefore));

        // 2. Замір для Мемоізації
        long[] memo = new long[nLarge + 1];
        Arrays.fill(memo, -1);

        System.gc();
        memBefore = getUsedMemory();
        startTime = System.nanoTime();

        long resMemo = fibMemo(nLarge, memo);

        endTime = System.nanoTime();
        memAfter = getUsedMemory();
        System.out.printf("Мемоізація (N=%d):     Результат = %d | Час = %d нс | Пам'ять = %d байт\n",
                nLarge, resMemo, (endTime - startTime), Math.max(0, memAfter - memBefore));

        // 3. Замір для Ітеративного підходу
        System.gc();
        memBefore = getUsedMemory();
        startTime = System.nanoTime();

        long resIter = fibIterative(nLarge);

        endTime = System.nanoTime();
        memAfter = getUsedMemory();
        System.out.printf("Ітеративний (N=%d):     Результат = %d | Час = %d нс | Пам'ять = %d байт\n\n",
                nLarge, resIter, (endTime - startTime), Math.max(0, memAfter - memBefore));


        System.out.println("ТЕСТУВАННЯ ЧАСТИНИ 2 (Хешування)");

        int wordsCount = 1000;
        int P = 31;   // Множник
        int M = 997;  // Розмір таблиці (просте число)

        String[] words = new String[wordsCount];
        Random random = new Random(42); // фіксований seed для стабільності тестів

        // Генеруємо 1000 унікальних випадкових слів
        for (int i = 0; i < wordsCount; i++) {
            words[i] = generateRandomWord(random, 5 + random.nextInt(6)); // довжина від 5 до 10
        }

        // Масив для підрахунку частоти потрапляння в кожен хеш-бакет
        int[] hashCounts = new int[M];
        for (String word : words) {
            int hash = polynomialHash(word, P, M);
            hashCounts[hash]++;
        }

        // Рахуємо колізії
        int totalCollisions = 0;
        for (int count : hashCounts) {
            if (count > 1) {
                totalCollisions += (count - 1);
            }
        }

        System.out.println("Згенеровано слів: " + wordsCount);
        System.out.println("Кількість колізій (Поліноміальне хешування): " + totalCollisions);
    }
}