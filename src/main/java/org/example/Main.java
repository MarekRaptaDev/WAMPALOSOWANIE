package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    //private static final String nazwa = "Wampa2024Grudzien.db";

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Wampirkowa maszyna losująca");
        DBConn db = new DBConn("zestawy.db");

        frame.setSize(1300, 730);
        frame.setMinimumSize(new Dimension(1000, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setIconImage(new ImageIcon("wampirek.png").getImage());


        BufferedImage originalImage = ImageIO.read(new File("wampirek.png"));
        BufferedImage scaledImage = IconsFunctions.scaleImage(originalImage, 300, 225);


        JLabel title = new JLabel("WAMPIRKOWA MASZYNA LOSUJĄCA", new ImageIcon(scaledImage), JLabel.CENTER);
        title.setHorizontalTextPosition(JLabel.CENTER);
        title.setVerticalTextPosition(JLabel.BOTTOM);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setVerticalAlignment(JLabel.TOP);
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel firstRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        firstRow.setBackground(Color.WHITE);
        firstRow.add(title);

        // Środkowy panel z przyciskami
        JPanel secondRow = new JPanel(new GridLayout(1, 2));
        secondRow.setBackground(Color.WHITE);

        JPanel premPanel = new JPanel(new GridBagLayout());
        premPanel.setBackground(Color.WHITE);

        JPanel nonPremPanel = new JPanel(new GridBagLayout());
        nonPremPanel.setBackground(Color.WHITE);


        RoundedButton premB = new RoundedButton("Oddał", 50, Color.RED, Color.WHITE);
        premB.setFont(new Font("Arial", Font.BOLD, 18));
        premB.setPreferredSize(new Dimension(220, 60));


        RoundedButton nonPremB = new RoundedButton("Nie oddał", 50, Color.BLUE, Color.WHITE);
        nonPremB.setFont(new Font("Arial", Font.BOLD, 18));
        nonPremB.setPreferredSize(new Dimension(220, 60));

        premPanel.add(premB);
        nonPremPanel.add(nonPremB);

        secondRow.add(premPanel);
        secondRow.add(nonPremPanel);


        JPanel thirdRow = new JPanel(new GridBagLayout());
        thirdRow.setBackground(Color.WHITE);

        BufferedImage nzsImage = ImageIO.read(new File("nzs.png"));
        JButton nzsButton = new JButton();
        nzsButton.setBackground(Color.WHITE);
        nzsButton.setBorder(BorderFactory.createEmptyBorder());
        BufferedImage scaledNzsImage = IconsFunctions.scaleImage(nzsImage, 250, 140);
        nzsButton.setIcon(new ImageIcon(scaledNzsImage));
        thirdRow.add(nzsButton);


        frame.add(firstRow, BorderLayout.NORTH);
        frame.add(secondRow, BorderLayout.CENTER);
        frame.add(thirdRow, BorderLayout.SOUTH);


        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == premB) {
                    db.connect();
                    db.showRows("test", 1);
                    db.disconect();
                } else if (e.getSource() == nonPremB) {
                    db.connect();
                    db.showRows("test", 0);
                    db.disconect();
                } else if (e.getSource() == nzsButton) {
                    db.connect();
                    db.NZS("test");
                    db.disconect();
                }
            }
        };

        premB.addActionListener(buttonListener);
        nonPremB.addActionListener(buttonListener);
        nzsButton.addActionListener(buttonListener);

        frame.setVisible(true);
    }


    static class RoundedButton extends JButton {
        private final int radius;
        private final Color backgroundColor;
        private final Color textColor;

        public RoundedButton(String text, int radius, Color backgroundColor, Color textColor) {
            super(text);
            this.radius = radius;
            this.backgroundColor = backgroundColor;
            this.textColor = textColor;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);


            setForeground(textColor);
            setBackground(backgroundColor);
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (getModel().isPressed()) {
                g.setColor(backgroundColor.darker());
            } else {
                g.setColor(backgroundColor);
            }
            g.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);


            g.setColor(getForeground());
            g.setFont(getFont());


            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(getText());
            int textHeight = fm.getHeight();
            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() + textHeight) / 2 - fm.getDescent();


            g.drawString(getText(), x, y);
        }
    }
}
