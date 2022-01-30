package com;


import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Log {
    private JTextArea textAreaLog;
    private JScrollPane scrollTextAreaLogPane;
    private static Log instance;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSS");

    private Log(){
        textAreaLog = new JTextArea();
        textAreaLog.setLineWrap(true);
        textAreaLog.setWrapStyleWord(true);
        textAreaLog.setText("");
        scrollTextAreaLogPane = new JScrollPane(textAreaLog);
        scrollTextAreaLogPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollTextAreaLogPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollTextAreaLogPane.setPreferredSize(new Dimension(800, 150));
        scrollTextAreaLogPane.setBorder(BorderFactory.createTitledBorder("Логирование"));
    }

    public static Log getInstance(){
        if (instance == null){ instance = new Log(); }
        return instance;
    }
    public JScrollPane getTextAreaLogPane(){
        return scrollTextAreaLogPane;
    }

    public void write(String str){
        Date date = Calendar.getInstance().getTime();
        textAreaLog.append(sdf.format(date) + " -> " + str);
        textAreaLog.append(System.lineSeparator());
    }
    public void write(Integer i){
        write(String.valueOf(i));
    }
}

