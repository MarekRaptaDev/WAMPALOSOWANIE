package org.example;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;

public class RoundedButton extends JButton {
    private final String text;
    private final int radius;


    public RoundedButton(String text, int radius) {
        super(text);
        this.text = text;
        this.radius = radius;
        setContentAreaFilled(false);
        setBorder(new RoundedBorder(radius));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (getModel().isPressed()) {
            g.setColor(getBackground().darker());
        } else {
            g.setColor(getBackground());
        }
        g.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);


        g.setColor(getForeground());
        g.setFont(getFont());


        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() + textHeight) / 2 - fm.getDescent();


        g.drawString(text, x, y);
    }


    static class RoundedBorder extends AbstractBorder {
        private final int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(Color.WHITE);
            g.fillRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Zaokrąglony Przycisk");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);


        RoundedButton button = new RoundedButton("Zaokrąglony Przycisk", 30);
        button.setBackground(Color.RED);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 18));

        frame.setLayout(new FlowLayout());
        frame.add(button);
        frame.setVisible(true);
    }

}
