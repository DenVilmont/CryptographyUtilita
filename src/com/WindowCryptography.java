package com;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.nio.file.Paths;

import static com.Utils.*;
import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;

public class WindowCryptography {

    private JPanel contentPane;
    private String fileDirectory = "";
    private Log log = Log.getInstance();
    private JLabel lblFileDirectory;
    private JTextField textFieldCryptoKey;

    public JPanel getContentPane(){
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout());

        contentPane.add(log.getTextAreaLogPane(), BorderLayout.SOUTH);

        JPanel cryptoPanel = new JPanel();
        GroupLayout layout = new GroupLayout(cryptoPanel);
        cryptoPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        cryptoPanel.setBorder(BorderFactory.createTitledBorder("Шифрование"));
        contentPane.add(cryptoPanel, BorderLayout.CENTER);

        JButton btnViewAlphabet = new JButton("Посмотреть текущий алфавит");
        btnViewAlphabet.addActionListener(e -> JOptionPane.showMessageDialog(null, cryptoDefaultAlphabet, "Используемый алфавит шифрования", JOptionPane.INFORMATION_MESSAGE));

        JButton btnChooseFile = new JButton("Выбрать файл");
        btnChooseFile.addActionListener(e -> btnChooseFileAction());

        lblFileDirectory = new JLabel("");
        lblFileDirectory.setVerticalAlignment(JLabel.BOTTOM);
        lblFileDirectory.setHorizontalAlignment(JLabel.LEFT);

        JLabel lblCryptoKey = new JLabel("Ключ");
        lblCryptoKey.setVerticalAlignment(JLabel.BOTTOM);
        lblCryptoKey.setHorizontalAlignment(JLabel.LEFT);
        textFieldCryptoKey = new JTextField();


        JButton btnEncode = new JButton("Зашифровать");
        btnEncode.addActionListener(e -> encrypt("encode"));

        JButton btnDecode = new JButton("Расшифровать");
        btnDecode.addActionListener(e -> encrypt("decode"));


        layout.setHorizontalGroup(layout.createParallelGroup()
                .addComponent(btnViewAlphabet)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(btnChooseFile)
                        .addComponent(lblFileDirectory, DEFAULT_SIZE,200,PREFERRED_SIZE)
                )
                .addGroup(layout.createSequentialGroup()
                        .addComponent(textFieldCryptoKey, DEFAULT_SIZE,50,PREFERRED_SIZE)
                        .addComponent(lblCryptoKey)
                )
                .addGroup(layout.createSequentialGroup()
                        .addComponent(btnEncode)
                        .addComponent(btnDecode)
                )
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(btnViewAlphabet)
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(btnChooseFile)
                        .addComponent(lblFileDirectory)
                )
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(textFieldCryptoKey)
                        .addComponent(lblCryptoKey)
                )
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(btnEncode)
                        .addComponent(btnDecode)
                )
        );

        return contentPane;
    }

    private void btnChooseFileAction() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (!fileDirectory.isEmpty()){
            String currentDirectory = Paths.get(fileDirectory).getParent().toString();
            fileChooser.setCurrentDirectory(new File(currentDirectory));
        }
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Texting files", "doc","docx","txt");
        fileChooser.setFileFilter(filter);


        int selection = fileChooser.showOpenDialog(contentPane);
        File selectedFile = fileChooser.getSelectedFile();
        if (!selectedFile.exists()){
            selection = JFileChooser.ERROR_OPTION;
        }

        if(selection==JFileChooser.APPROVE_OPTION){
            fileDirectory = selectedFile.getAbsolutePath();
            lblFileDirectory.setText(selectedFile.getName());
            log.write("Выбран файл:  " + fileDirectory);
        }else if (selection==JFileChooser.CANCEL_OPTION){
            log.write("Выбор файла отменен.");
        }else if (selection==JFileChooser.ERROR_OPTION){
            log.write("Что-то пошло не так. Произошла ошибка при выборе файла или файл не существует.");
        }

    }


    private void encrypt(String typeEncrypt) {
        String strKey = textFieldCryptoKey.getText();
        File fileIn = new File(fileDirectory);

        /*Создаем файл с измененным именем*/
        String fileName = fileIn.getName().substring(0, fileIn.getName().lastIndexOf('.'));
        String fileExtension = fileIn.getName().substring(fileIn.getName().lastIndexOf('.')+1);
        String typeFile = typeEncrypt.equals("encode") ? "_Encoded." : "_Decoded.";
        File fileOut = new File(fileIn.getParent() + File.separator + fileName + typeFile + fileExtension);

        try(FileReader fr = new FileReader(fileIn);
            BufferedReader reader = new BufferedReader(fr);
            FileWriter writer = new FileWriter(fileOut)){


            if (strKey.isEmpty()) throw new NumberFormatException();
            int cryptoKey = Integer.parseInt(strKey);
            if (typeEncrypt.equals("decode")){
                cryptoKey = -cryptoKey;
            }

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
