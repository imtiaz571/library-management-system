package view.components;

import view.theme.ModernTheme;
import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {

    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setForeground(Color.WHITE);
        setBackground(ModernTheme.PRIMARY_COLOR);
        setFont(ModernTheme.BUTTON_FONT);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public RoundedButton(String text, Color bgColor) {
        this(text);
        setBackground(bgColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        super.paintComponent(g);
        g2.dispose();
    }
}
