package view;

import model.Member;
import model.MembershipType;
import repository.FileRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class MemberPanel extends JPanel {
    private JTextField txtId, txtName, txtEmail;
    private JComboBox<MembershipType> cmbType;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Member> memberList;
    private FileRepository<Member> repository;
    private final String DATA_FILE = "members.ser";

    public MemberPanel() {
        repository = new FileRepository<>(DATA_FILE);
        memberList = repository.load();
        if (memberList == null)
            memberList = new ArrayList<>();

        setOpaque(false);
        setLayout(new BorderLayout(20, 20));
        initComponents();
        loadTableData();
    }

    private void initComponents() {
        JLabel header = new JLabel("Member Management");
        header.setFont(view.theme.ModernTheme.HEADER_FONT);
        header.setForeground(view.theme.ModernTheme.TEXT_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        JPanel contentGrid = new JPanel(new GridLayout(1, 2, 20, 0));
        contentGrid.setOpaque(false);
        view.components.CardPanel formCard = new view.components.CardPanel();
        formCard.setLayout(new BorderLayout());
        formCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formLayout = new JPanel(new GridLayout(5, 1, 10, 10));
        formLayout.setOpaque(false);

        formLayout.add(createLabeledField("Member ID (Auto):", txtId = new JTextField()));
        txtId.setEditable(false);

        formLayout.add(createLabeledField("Name:", txtName = new JTextField()));
        formLayout.add(createLabeledField("Email:", txtEmail = new JTextField()));

        JPanel typePanel = new JPanel(new BorderLayout());
        typePanel.setOpaque(false);
        typePanel.add(new JLabel("Membership Type:"), BorderLayout.NORTH);
        cmbType = new JComboBox<>(MembershipType.values());
        cmbType.setBackground(Color.WHITE);
        typePanel.add(cmbType, BorderLayout.CENTER);
        formLayout.add(typePanel);

        formCard.add(formLayout, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton btnAdd = new view.components.RoundedButton("Add Member", view.theme.ModernTheme.PRIMARY_COLOR);
        JButton btnEdit = new view.components.RoundedButton("Update", view.theme.ModernTheme.SECONDARY_COLOR);
        JButton btnDelete = new view.components.RoundedButton("Delete", view.theme.ModernTheme.TEXT_SECONDARY);
        JButton btnClear = new view.components.RoundedButton("Clear", view.theme.ModernTheme.TEXT_SECONDARY);

        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        formCard.add(btnPanel, BorderLayout.CENTER);
        contentGrid.add(formCard);

        view.components.CardPanel tableCard = new view.components.CardPanel();
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = { "ID", "Name", "Email", "Type" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setShowVerticalLines(false);
        table.getTableHeader().setFont(view.theme.ModernTheme.BUTTON_FONT);
        table.getTableHeader().setBackground(new Color(240, 240, 240));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        tableCard.add(scrollPane, BorderLayout.CENTER);
        contentGrid.add(tableCard);

        add(contentGrid, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> addMember());
        btnEdit.addActionListener(e -> updateMember());
        btnDelete.addActionListener(e -> deleteMember());
        btnClear.addActionListener(e -> clearForm());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                fillFormFromSelection();
            }
        });
    }

    private JPanel createLabeledField(String labelText, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(view.theme.ModernTheme.STANDARD_FONT);
        lbl.setForeground(view.theme.ModernTheme.TEXT_SECONDARY);
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private void fillFormFromSelection() {
        int row = table.getSelectedRow();
        txtId.setText(tableModel.getValueAt(row, 0).toString());
        txtName.setText(tableModel.getValueAt(row, 1).toString());
        txtEmail.setText(tableModel.getValueAt(row, 2).toString());
        cmbType.setSelectedItem(tableModel.getValueAt(row, 3));
    }

    private void addMember() {
        int id = memberList.isEmpty() ? 1 : memberList.get(memberList.size() - 1).getMemberId() + 1;
        String name = txtName.getText();
        String email = txtEmail.getText();
        MembershipType type = (MembershipType) cmbType.getSelectedItem();

        if (name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        Member member = new Member(id, name, email, type);
        memberList.add(member);
        repository.save(memberList);
        loadTableData();
        clearForm();
    }

    private void updateMember() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a member to update.");
            return;
        }

        int id = Integer.parseInt(txtId.getText());
        String name = txtName.getText();
        String email = txtEmail.getText();
        MembershipType type = (MembershipType) cmbType.getSelectedItem();

        for (Member m : memberList) {
            if (m.getMemberId() == id) {
                m.setName(name);
                m.setEmail(email);
                m.setType(type);
                break;
            }
        }
        repository.save(memberList);
        loadTableData();
        clearForm();
    }

    private void deleteMember() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a member to delete.");
            return;
        }

        int id = Integer.parseInt(txtId.getText());
        memberList.removeIf(m -> m.getMemberId() == id);
        repository.save(memberList);
        loadTableData();
        clearForm();
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtEmail.setText("");
        cmbType.setSelectedIndex(0);
        table.clearSelection();
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        for (Member m : memberList) {
            tableModel.addRow(new Object[] { m.getMemberId(), m.getName(), m.getEmail(), m.getType() });
        }
    }
}
