package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class MainLibFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;

    public MainLibFrame() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(240, 242, 245);
                Color color2 = new Color(220, 225, 235);
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);

                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.fillOval(w - 200, -50, 300, 300);

                g2d.setColor(new Color(207, 216, 220, 100));
                g2d.fillOval(-100, h - 200, 400, 400);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        MemberPanel memberPanel = new MemberPanel();
        BookPanel bookPanel = new BookPanel();
        LoanPanel loanPanel = new LoanPanel();
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setOpaque(false);
        contentPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        contentPanel.add(memberPanel, "MEMBERS");
        contentPanel.add(bookPanel, "BOOKS");
        contentPanel.add(loanPanel, "LOANS");

        view.components.Sidebar sidebar = new view.components.Sidebar(e -> {
            String cmd = e.getActionCommand();
            cardLayout.show(contentPanel, cmd);
            if (cmd.equals("LOANS")) {
                loanPanel.refreshData();
            }
        });

        sidebar.addMenuButton("Members", "MEMBERS");
        sidebar.addMenuButton("Books", "BOOKS");
        sidebar.addMenuButton("Issue / Return", "LOANS");

        backgroundPanel.add(sidebar, java.awt.BorderLayout.WEST);
        backgroundPanel.add(contentPanel, java.awt.BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new MainLibFrame().setVisible(true);
        });
    }
}
