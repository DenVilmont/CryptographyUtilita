package com;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.Utils.*;
import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;

public class WindowStatisticAnalysis extends JFrame {
    private Log log = Log.getInstance();
    private String fileDirectoryEncoded = "";
    private String fileDirectoryStatistic = "";
    private JLabel lblFileDirectoryEncoded;
    private JLabel lblFileDirectoryStatistic;
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
        bruteforcePanel.setBorder(BorderFactory.createTitledBorder("Статистический анализ"));
        contentPane.add(bruteforcePanel, BorderLayout.CENTER);

        JButton btnViewAlphabet = new JButton("Посмотреть текущий алфавит");
        btnViewAlphabet.addActionListener(e -> JOptionPane.showMessageDialog(null, cryptoDefaultAlphabet, "Используемый алфавит шифрования", JOptionPane.INFORMATION_MESSAGE));

        JButton btnChooseFileEncoded = new JButton("Выбрать зашифрованный файл");
        btnChooseFileEncoded.addActionListener(e -> btnChooseFileAction("encoded"));

        lblFileDirectoryEncoded = new JLabel("");
        lblFileDirectoryEncoded.setVerticalAlignment(JLabel.BOTTOM);
        lblFileDirectoryEncoded.setHorizontalAlignment(JLabel.LEFT);

        JButton btnChooseFileStatistic = new JButton("Выбрать файл статистики");
        btnChooseFileStatistic.addActionListener(e -> btnChooseFileAction("statistic"));

        lblFileDirectoryStatistic = new JLabel("");
        lblFileDirectoryStatistic.setVerticalAlignment(JLabel.BOTTOM);
        lblFileDirectoryStatistic.setHorizontalAlignment(JLabel.LEFT);

        JButton btnBruteforce = new JButton("Анализировать");
        btnBruteforce.addActionListener(e -> analize());

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addComponent(btnViewAlphabet)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(btnChooseFileEncoded)
                        .addComponent(lblFileDirectoryEncoded, DEFAULT_SIZE, 200, PREFERRED_SIZE)
                )
                .addGroup(layout.createSequentialGroup()
                        .addComponent(btnChooseFileStatistic)
                        .addComponent(lblFileDirectoryStatistic, DEFAULT_SIZE, 200, PREFERRED_SIZE)
                )
                .addComponent(btnBruteforce)
        );

        layout.setVerticalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(btnViewAlphabet)
                        .addGroup(layout.createParallelGroup(BASELINE)
                                .addComponent(btnChooseFileEncoded)
                                .addComponent(lblFileDirectoryEncoded)
                        )
                        .addGroup(layout.createParallelGroup(BASELINE)
                                .addComponent(btnChooseFileStatistic)
                                .addComponent(lblFileDirectoryStatistic)
                        )
                        .addComponent(btnBruteforce)
                )
        );

        return contentPane;
    }

    private void btnChooseFileAction(String type) {
        String fileDirectory = "";
        if (type.equals("encoded")){
            fileDirectory = fileDirectoryEncoded;
        }else if (type.equals("statistic")){
            fileDirectory = fileDirectoryStatistic;
        }
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
            if (type.equals("encoded")){
                fileDirectoryEncoded = selectedFile.getAbsolutePath();
                lblFileDirectoryEncoded.setText(selectedFile.getName());
                log.write("Выбран зашифрованный файл:  " + selectedFile.getAbsolutePath());
            }else if (type.equals("statistic")){
                fileDirectoryStatistic = selectedFile.getAbsolutePath();
                lblFileDirectoryStatistic.setText(selectedFile.getName());
                log.write("Выбран файл для статистики:  " + selectedFile.getAbsolutePath());
            }
        } else if (selection == JFileChooser.CANCEL_OPTION) {
            log.write("Выбор файла отменен.");
        } else if (selection == JFileChooser.ERROR_OPTION) {
            log.write("Что-то пошло не так. Произошла ошибка при выборе файла или файл не существует.");
        }
    }


    private void analize() {
        if (fileDirectoryEncoded.isEmpty() || fileDirectoryStatistic.isEmpty()){
            JOptionPane.showMessageDialog(null, cryptoDefaultAlphabet, "Выберите зашифрованный файл и файл для статистического анализа", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String strEncoded = "";
        String strStatistic = "";

        try {
            strEncoded = readFile(fileDirectoryEncoded);
            strStatistic = readFile(fileDirectoryStatistic);
        } catch (IOException e) {
            log.write(e.getMessage());
        }

        List<Map.Entry<Character, Integer>> letrasContadasEncoded = countLetras(strEncoded);
        List<Map.Entry<Character, Integer>> letrasContadasStatistic = countLetras(strStatistic);

        HashMap<Character,Character> resultMap = new HashMap<>();
        for (int i = 0; i < letrasContadasEncoded.size(); i++) {
            resultMap.put(letrasContadasEncoded.get(i).getKey(),
                    (i < letrasContadasStatistic.size() ? letrasContadasStatistic.get(i).getKey() : letrasContadasEncoded.get(i).getKey()));
        }

        File fileIn = new File(fileDirectoryEncoded);
        String fileName = fileIn.getName().substring(0, fileIn.getName().lastIndexOf('.'));
        String fileExtension = fileIn.getName().substring(fileIn.getName().lastIndexOf('.') + 1);
        File fileOut = new File(fileIn.getParent() + File.separator + fileName + "_Statistic_decode." + fileExtension);


        try(FileReader fr = new FileReader(fileIn);
            BufferedReader reader = new BufferedReader(fr);
            FileWriter writer = new FileWriter(fileOut)){

            String line = reader.readLine();
            while (line != null) {
                char[] chars = line.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    chars[i] = resultMap.get(chars[i]);
                }
                writer.write(new String(chars) + System.lineSeparator());
                line = reader.readLine();
            }
            log.write("Расшифрованный файл записан   " + fileOut.getAbsolutePath());

        } catch (Exception e) {
            log.write(e.getMessage());
        }

    }

}
