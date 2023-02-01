import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Дегтярев Евгений
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) {
        try {
            startWorking();
            JOptionPane.showMessageDialog(null, "Программа успешно завершила свою работу");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, String.format("Программа завершила свою работу с ошибкой: %s%n", ex.getMessage()));
        }
    }

    /**
     * Метод, в котором содержится основное управление выполнением
     */
    private static void startWorking() {
        File selectedFile = openFileDialog("log", "Open");
        if (selectedFile == null) {
            throw new RuntimeException("Файл не был выбран");
        }
        HashMap<String, Integer> hashMap = Parser.startParsing(selectedFile);
        HashMap<String, Integer> result = new HashMap<>();
        int maxValue = 0;
        String keyMaxValue = null;
        for (int i = 0; i < 10; i++) {
            for (String key : hashMap.keySet()) {
                if (hashMap.get(key) > maxValue) {
                    maxValue = hashMap.get(key);
                    keyMaxValue = key;
                }
            }
            System.out.printf("%s\t%s bytes\n", keyMaxValue, maxValue);
            result.put(keyMaxValue, maxValue);
            hashMap.remove(keyMaxValue);
            maxValue = 0;
            keyMaxValue = null;
        }
        selectedFile = openFileDialog("txt", "Save");
        if (selectedFile == null) {
            throw new RuntimeException("Путь сохранения не был выбран");
        }
        saveResult(selectedFile, result);
    }

    /**
     * Создает диалоговое окно для выбора файла
     *
     * @return Выбранный файл или null, если файл не выбран
     */
    private static File openFileDialog(String extension, String approveButtonText) {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.removeChoosableFileFilter(jFileChooser.getChoosableFileFilters()[0]);
        jFileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().contains("." + extension);
            }

            @Override
            public String getDescription() {
                return extension;
            }
        });
        if (jFileChooser.showDialog(null, approveButtonText) == JFileChooser.APPROVE_OPTION) {
            return jFileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    /**
     * Сохраняет результат в текстовый файл
     *
     * @param file файл, в который записывается результат
     * @param map  сохраняемая структура
     */
    private static void saveResult(File file, Map<String, Integer> map) {
        try (FileWriter fileWriter = new FileWriter(file);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            for (String key : map.keySet()) {
                bufferedWriter.write(String.format("%s\t%s bytes\n", key, map.get(key)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}