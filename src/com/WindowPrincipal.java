package com;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


public class WindowPrincipal extends JFrame{
    private final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    private final int width = 800;
    private final int height = 500;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        "javax.swing.plaf.metal.MetalLookAndFeel");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            new WindowPrincipal().setVisible(true);
        });
    }

    public WindowPrincipal(){
        super();
        configurationWindow();
        openCryptography();
    }

    private void configurationWindow() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds((dimension.width-width)/2,(dimension.height-height)/2, width, height);
        setTitle("CryptographyUtilita");
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnMenuVentanas = new JMenu("Меню");
        menuBar.add(mnMenuVentanas);

        JMenuItem mntmCryptography = new JMenuItem("Шифрование");
        mnMenuVentanas.add(mntmCryptography);
        mntmCryptography.addActionListener(e -> openCryptography());

        JMenu mnMenuCryptanalysis = new JMenu("Криптоанализ");
        mnMenuVentanas.add(mnMenuCryptanalysis);

        JMenuItem mntmBruteforceAnalysis = new JMenuItem("Брутфорс");
        mnMenuCryptanalysis.add(mntmBruteforceAnalysis);
        mntmBruteforceAnalysis.addActionListener(e -> openBruteforceAnalysis());

        JMenuItem mntmStatisticAnalysis = new JMenuItem("Статистический анализ");
        mnMenuCryptanalysis.add(mntmStatisticAnalysis);
        mntmStatisticAnalysis.addActionListener(e -> openStatisticAnalysis());

    }

    private void openCryptography() {
        if (getTitle().equals("CryptographyUtilita::Cryptography"))
            return;
        WindowCryptography windowCrypto = new WindowCryptography();
        setContentPane(windowCrypto.getContentPane());
        setTitle("CryptographyUtilita::Cryptography");
        revalidate();
    }
    private void openBruteforceAnalysis() {
        if (getTitle().equals("CryptographyUtilita::BruteforceAnalysis"))
            return;
        WindowBruteforceAnalysis windowBruteforce = new WindowBruteforceAnalysis();
        setContentPane(windowBruteforce.getContentPane());
        setTitle("CryptographyUtilita::BruteforceAnalysis");
        revalidate();
    }


    private void openStatisticAnalysis() {
        if (getTitle().equals("CryptographyUtilita::StatisticAnalysis"))
            return;
        WindowStatisticAnalysis statisticAnalysis = new WindowStatisticAnalysis();
        setContentPane(statisticAnalysis.getContentPane());
        setTitle("CryptographyUtilita::StatisticAnalysis");
        revalidate();
    }
}
