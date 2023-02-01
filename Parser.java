import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Дегтярев Евгений
 * @version 1.0
 */
public class Parser {
    /**
     * Собирает информацию из записей в указанном файле
     *
     * @param file анализируемый файл
     * @return структура "ключ-значение", содержащая в качестве ключа IP адреса пользователей, а в качестве значения - трафик пользователя в байтах
     */
    public static HashMap<String, Integer> startParsing(File file) {
        ArrayList<String> lines = readFile(file);
        HashMap<String, Integer> result = new HashMap<>();
        for (String line : lines) {
            String user = (match(line, Pattern.compile("\\d+[.]\\d+[.]\\d+[.]\\d+"), 0));
            int traffic = Integer.parseInt(match(line, Pattern.compile("\\s\\d+\\s"), 1).replace(" ", ""));
            if (result.containsKey(user)) {
                result.replace(user, result.get(user) + traffic);
            } else {
                result.put(user, traffic);
            }
        }
        return result;
    }

    /**
     * Возвращает подстроку, которая соответствует указанному паттерну
     *
     * @param str     входная строка
     * @param pattern паттерн регулярного выражения
     * @param index   индекс возвращаемой подстроки
     * @return найденная подстрока или пустая строка
     */
    private static String match(String str, Pattern pattern, int index) {
        Matcher matcher = pattern.matcher(str);
        int currentIndex = 0;
        while (matcher.find()) {
            if (currentIndex == index) {
                return matcher.group();
            }
            currentIndex++;
        }
        return "";
    }

    /**
     * Построчно читает файл
     *
     * @param file анализируемый файл
     * @return массив строк файла
     */
    private static ArrayList<String> readFile(File file) {
        ArrayList<String> result = new ArrayList<>();
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                result.add(currentLine);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("Записей пройдено: %s\n", result.size());
        return result;
    }
}
