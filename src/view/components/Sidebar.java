package view.components;

import view.theme.ModernTheme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Sidebar extends JPanel {
    private List<JButton> buttons;
    private ActionListener navigationListener;

    public Sidebar(ActionListener navigationListener) {
        this.navigationListener = navigationListener;
        this.buttons = new ArrayList<>();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(ModernTheme.GLASS_SIDEBAR);
        setOpaque(false); // Important for custom painting
        setPreferredSize(new Dimension(260, 0));
        setBorder(BorderFactory.createEmptyBorder(40, 25, 40, 25));

        // Logo / Title area
        JLabel titleLabel = new JLabel("LMS");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setForeground(ModernTheme.PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(titleLabel);

        add(Box.createVerticalStrut(60));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Glass Background for Sidebar
        g2.setColor(getBackground());
        g2.fillRoundRect(10, 10, getWidth() - 20, getHeight() - 20, 30, 30);

        super.paintComponent(g);
        g2.dispose();
    }

    public void addMenuButton(String text, String command) {
        JButton btn = new JButton(text);
        btn.setActionCommand(command);
        btn.addActionListener(e -> {
            highlightButton(btn);
            navigationListener.actionPerformed(e);
        });

        // Style
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(ModernTheme.SUBHEADER_FONT);
        btn.setForeground(ModernTheme.TEXT_SECONDARY);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(btn);
        add(Box.createVerticalStrut(10));
        buttons.add(btn);

        if (buttons.size() == 1) {
            highlightButton(btn); // Default select first
        }
    }

    private void highlightButton(JButton selected) {
        for (JButton b : buttons) {
            if (b == selected) {
                b.setForeground(ModernTheme.PRIMARY_COLOR);
                b.setFont(ModernTheme.SUBHEADER_FONT); // Bold
            } else {
                b.setForeground(ModernTheme.TEXT_SECONDARY);
                // b.setFont(ModernTheme.STANDARD_FONT);
            }
        }
    }
}
