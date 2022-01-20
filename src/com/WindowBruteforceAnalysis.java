package com;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;

import static com.Utils.*;
import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;

public class WindowBruteforceAnalysis extends JFrame {
    private Log log = Log.getInstance();
    private String fileDirectory = "";
    private JLabel lblFileDirectory;
    private JPanel contentPane;

    public JPanel getContentPane() {
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout());

        contentPane.add(log.getTextAreaLogPane(), BorderLayout.SOUTH);

        JPanel bruteforcePanel = new JPanel();
        GroupLayout layout = new GroupLayout(bruteforcePanel);
        bruteforcePanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        bruteforcePanel.setBorder(BorderFactory.createTitledBorder("Брутфорс шифра"));
        contentPane.add(bruteforcePanel, BorderLayout.CENTER);

        JButton btnViewAlphabet = new JButton("Посмотреть текущий алфавит");
        btnViewAlphabet.addActionListener(e -> JOptionPane.showMessageDialog(null, cryptoDefaultAlphabet, "Используемый алфавит шифрования", JOptionPane.INFORMATION_MESSAGE));

        JButton btnChooseFile = new JButton("Выбрать файл");
        btnChooseFile.addActionListener(e -> btnChooseFileAction());

        lblFileDirectory = new JLabel("");
        lblFileDirectory.setVerticalAlignment(JLabel.BOTTOM);
        lblFileDirectory.setHorizontalAlignment(JLabel.LEFT);

        JButton btnBruteforce = new JButton("Подобрать ключ");
        btnBruteforce.addActionListener(e -> bruteforce());

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addComponent(btnViewAlphabet)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(btnChooseFile)
                        .addComponent(lblFileDirectory, DEFAULT_SIZE, 200, PREFERRED_SIZE)
                )
                .addComponent(btnBruteforce)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(btnViewAlphabet)
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(btnChooseFile)
                        .addComponent(lblFileDirectory)
                )
                .addComponent(btnBruteforce)
        );

        return contentPane;
    }

    private void btnChooseFileAction() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (!fileDirectory.isEmpty()) {
            String currentDirectory = Paths.get(fileDirectory).getParent().toString();
            fileChooser.setCurrentDirectory(new File(currentDirectory));
        }
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Texting files", "doc", "docx", "txt");
        fileChooser.setFileFilter(filter);


        int selection = fileChooser.showOpenDialog(contentPane);
        File selectedFile = fileChooser.getSelectedFile();
        if (!selectedFile.exists()) {
            selection = JFileChooser.ERROR_OPTION;
        }

        if (selection == JFileChooser.APPROVE_OPTION) {
            fileDirectory = selectedFile.getAbsolutePath();
            lblFileDirectory.setText(selectedFile.getName());
            log.write("Выбран файл:  " + fileDirectory);
        } else if (selection == JFileChooser.CANCEL_OPTION) {
            log.write("Выбор файла отменен.");
        } else if (selection == JFileChooser.ERROR_OPTION) {
            log.write("Что-то пошло не так. Произошла ошибка при выборе файла или файл не существует.");
        }
    }

    private void bruteforce() {
        File fileIn = new File(fileDirectory);

        /*Создаем файл с измененным именем*/
        String fileName = fileIn.getName().substring(0, fileIn.getName().lastIndexOf('.'));
        String fileExtension = fileIn.getName().substring(fileIn.getName().lastIndexOf('.') + 1);
        File fileOut = new File(fileIn.getParent() + File.separator + fileName + "_Bruteforce_decode." + fileExtension);

        HashMap<Integer, Integer> result = new HashMap<>();

        try (FileReader fr = new FileReader(fileIn);
             BufferedReader reader = new BufferedReader(fr)) {

            int minKey = 0;
            int maxkey = cryptoDefaultAlphabet.length() - 1;
            RusWords rusWords = RusWords.getInstance();

            String line = reader.readLine();
            StringBuilder strEncoded = new StringBuilder();
            int count = 0;
            while (line != null || count < 100) {
                strEncoded.append(" ").append(line);
                line = reader.readLine();
                count++;
            }

            for (int key = minKey; key <= maxkey; key++) {
                char[] chars = strEncoded.toString().toCharArray();
                for (int j = 0; j < chars.length; j++) {
                    chars[j] = encryptChar(chars[j], key);
                }
                String strDencoded = new String(chars);

                /*Анализируем адекватность пробного текста*/
                String[] words = strDencoded.split(" ");
                result.put(key, 0);
                for (String word : words) {
                    if (rusWords.contains(word)) {
                        result.put(key, result.get(key) + 1);
                    }
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        int cryptoKey = 0;
        int count = 0;
        for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
            if (entry.getValue() > count){
                count = entry.getValue();
                cryptoKey = entry.getKey();
            }
        }
        log.write("Подобранный ключ: " + cryptoKey);


        try(FileReader fr = new FileReader(fileIn);
            BufferedReader reader = new BufferedReader(fr);
            FileWriter writer = new FileWriter(fileOut)){

            String line = reader.readLine();
            while (line != null) {
                char[] chars = line.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    chars[i] = encryptChar(chars[i], cryptoKey);
                }
                writer.write(new String(chars) + System.lineSeparator());
                line = reader.readLine();
            }
            log.write("Файл успешно расшифрован:  " + fileOut.getAbsolutePath());

        } catch (NumberFormatException e) {
            log.write("Error! Ключ пустой или не является числом.");
        } catch (FileNotFoundException e) {
            log.write("Error! Файл не найден");
            log.write(e.getMessage());
        } catch (IOException e) {
            log.write(e.getMessage());
        }
    }

}
