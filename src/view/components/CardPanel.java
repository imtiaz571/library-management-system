package view.components;

import view.theme.ModernTheme;
import javax.swing.*;
import java.awt.*;
import java.awt.BasicStroke;

public class CardPanel extends JPanel {

    public CardPanel() {
        setOpaque(false);
        setBackground(ModernTheme.GLASS_PANEL);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Glass Background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

        // Glass Strike Border (Subtle white outline)
        g2.setColor(ModernTheme.GLASS_BORDER);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);

        g2.dispose();
        super.paintComponent(g); // Paint children
    }
}
