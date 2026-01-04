package view;

import model.Book;
import model.Member;
import model.Loan;
import repository.FileRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class LoanPanel extends JPanel {
    private JTextField txtLoanId;
    private JComboBox<Member> cmbMember;
    private JComboBox<Book> cmbBook;
    private JSpinner spinIssueDate, spinReturnDate;
    private JTable table;
    private DefaultTableModel tableModel;

    private List<Loan> loanList;
    private List<Member> memberList;
    private List<Book> bookList;

    private FileRepository<Loan> loanRepo;
    private FileRepository<Member> memberRepo;
    private FileRepository<Book> bookRepo;

    private final String LOAN_FILE = "loans.ser";
    private final String MEMBER_FILE = "members.ser";
    private final String BOOK_FILE = "books.ser";

    public LoanPanel() {
        loanRepo = new FileRepository<>(LOAN_FILE);
        memberRepo = new FileRepository<>(MEMBER_FILE);
        bookRepo = new FileRepository<>(BOOK_FILE);

        refreshData();

        setOpaque(false);
        setLayout(new BorderLayout(20, 20));
        initComponents();
        loadTableData();
    }

    public void refreshData() {
        loanList = loanRepo.load();
        if (loanList == null)
            loanList = new ArrayList<>();

        memberList = memberRepo.load();
        if (memberList == null)
            memberList = new ArrayList<>();

        bookList = bookRepo.load();
        if (bookList == null)
            bookList = new ArrayList<>();

        if (cmbMember != null) {
            cmbMember.removeAllItems();
            for (Member m : memberList)
                cmbMember.addItem(m);
        }
        if (cmbBook != null) {
            cmbBook.removeAllItems();
            for (Book b : bookList)
                cmbBook.addItem(b);
        }
    }

    private void initComponents() {
        JLabel header = new JLabel("Issue / Return Books");
        header.setFont(view.theme.ModernTheme.HEADER_FONT);
        header.setForeground(view.theme.ModernTheme.TEXT_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        JPanel contentGrid = new JPanel(new GridLayout(1, 2, 20, 0));
        contentGrid.setOpaque(false);

        view.components.CardPanel formCard = new view.components.CardPanel();
        formCard.setLayout(new BorderLayout());
        formCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formLayout = new JPanel(new GridLayout(6, 1, 10, 10));
        formLayout.setOpaque(false);

        formLayout.add(createLabeledField("Loan ID (Auto):", txtLoanId = new JTextField()));
        txtLoanId.setEditable(false);

        JPanel memPanel = new JPanel(new BorderLayout());
        memPanel.setOpaque(false);
        memPanel.add(new JLabel("Member:"), BorderLayout.NORTH);
        cmbMember = new JComboBox<>();
        cmbMember.setBackground(Color.WHITE);
        for (Member m : memberList)
            cmbMember.addItem(m);
        memPanel.add(cmbMember, BorderLayout.CENTER);
        formLayout.add(memPanel);

        JPanel bookPanel = new JPanel(new BorderLayout());
        bookPanel.setOpaque(false);
        bookPanel.add(new JLabel("Book:"), BorderLayout.NORTH);
        cmbBook = new JComboBox<>();
        cmbBook.setBackground(Color.WHITE);
        for (Book b : bookList)
            cmbBook.addItem(b);
        bookPanel.add(cmbBook, BorderLayout.CENTER);
        formLayout.add(bookPanel);

        formLayout.add(createLabeledField("Issue Date:", spinIssueDate = new JSpinner(new SpinnerDateModel())));
        spinIssueDate.setEditor(new JSpinner.DateEditor(spinIssueDate, "yyyy-MM-dd"));

        formLayout.add(createLabeledField("Return Date:", spinReturnDate = new JSpinner(new SpinnerDateModel())));
        spinReturnDate.setEditor(new JSpinner.DateEditor(spinReturnDate, "yyyy-MM-dd"));

        formCard.add(formLayout, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton btnIssue = new view.components.RoundedButton("Issue", view.theme.ModernTheme.PRIMARY_COLOR);
        JButton btnUpdate = new view.components.RoundedButton("Update", view.theme.ModernTheme.SECONDARY_COLOR);
        JButton btnDelete = new view.components.RoundedButton("Return", view.theme.ModernTheme.TEXT_SECONDARY);
        JButton btnClear = new view.components.RoundedButton("Clear", view.theme.ModernTheme.TEXT_SECONDARY);
        JButton btnRefresh = new view.components.RoundedButton("Sync", view.theme.ModernTheme.TEXT_SECONDARY);

        btnPanel.add(btnIssue);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);
        btnPanel.add(btnRefresh);

        formCard.add(btnPanel, BorderLayout.CENTER);
        contentGrid.add(formCard);

        view.components.CardPanel tableCard = new view.components.CardPanel();
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = { "Loan ID", "Member", "Book", "Issue Date", "Return Date" };
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

        btnIssue.addActionListener(e -> addLoan());
        btnUpdate.addActionListener(e -> updateLoan());
        btnDelete.addActionListener(e -> deleteLoan());
        btnClear.addActionListener(e -> clearForm());
        btnRefresh.addActionListener(e -> {
            refreshData();
            loadTableData();
            JOptionPane.showMessageDialog(this, "Data refreshed from files.");
        });

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
        txtLoanId.setText(tableModel.getValueAt(row, 0).toString());

        String memberStr = tableModel.getValueAt(row, 1).toString();
        for (int i = 0; i < cmbMember.getItemCount(); i++) {
            if (cmbMember.getItemAt(i).toString().equals(memberStr)) {
                cmbMember.setSelectedIndex(i);
                break;
            }
        }

        String bookStr = tableModel.getValueAt(row, 2).toString();
        for (int i = 0; i < cmbBook.getItemCount(); i++) {
            if (cmbBook.getItemAt(i).toString().equals(bookStr)) {
                cmbBook.setSelectedIndex(i);
                break;
            }
        }

        spinIssueDate.setValue(tableModel.getValueAt(row, 3));
        spinReturnDate.setValue(tableModel.getValueAt(row, 4));
    }

    private void addLoan() {
        if (cmbMember.getSelectedItem() == null || cmbBook.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select Member and Book.");
            return;
        }

        int id = loanList.isEmpty() ? 1 : loanList.get(loanList.size() - 1).getLoanId() + 1;
        Member member = (Member) cmbMember.getSelectedItem();
        Book book = (Book) cmbBook.getSelectedItem();
        Date issueDate = (Date) spinIssueDate.getValue();
        Date returnDate = (Date) spinReturnDate.getValue();

        Loan loan = new Loan(id, member.getMemberId(), book.getBookId(), issueDate, returnDate);
        loanList.add(loan);
        loanRepo.save(loanList);
        loadTableData();
        clearForm();
    }

    private void updateLoan() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a loan record to update.");
            return;
        }

        int id = Integer.parseInt(txtLoanId.getText());
        Member member = (Member) cmbMember.getSelectedItem();
        Book book = (Book) cmbBook.getSelectedItem();
        Date issueDate = (Date) spinIssueDate.getValue();
        Date returnDate = (Date) spinReturnDate.getValue();

        for (Loan l : loanList) {
            if (l.getLoanId() == id) {
                l.setMemberId(member.getMemberId());
                l.setBookId(book.getBookId());
                l.setIssueDate(issueDate);
                l.setReturnDate(returnDate);
                break;
            }
        }
        loanRepo.save(loanList);
        loadTableData();
        clearForm();
    }

    private void deleteLoan() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a loan record to delete.");
            return;
        }

        int id = Integer.parseInt(txtLoanId.getText());
        loanList.removeIf(l -> l.getLoanId() == id);
        loanRepo.save(loanList);
        loadTableData();
        clearForm();
    }

    private void clearForm() {
        txtLoanId.setText("");
        if (cmbMember.getItemCount() > 0)
            cmbMember.setSelectedIndex(0);
        if (cmbBook.getItemCount() > 0)
            cmbBook.setSelectedIndex(0);
        spinIssueDate.setValue(new Date());
        spinReturnDate.setValue(new Date());
        table.clearSelection();
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        for (Loan l : loanList) {
            String memName = "Unknown (" + l.getMemberId() + ")";
            for (Member m : memberList) {
                if (m.getMemberId() == l.getMemberId()) {
                    memName = m.toString();
                    break;
                }
            }

            String bookTitle = "Unknown (" + l.getBookId() + ")";
            for (Book b : bookList) {
                if (b.getBookId() == l.getBookId()) {
                    bookTitle = b.toString();
                    break;
                }
            }

            tableModel.addRow(new Object[] { l.getLoanId(), memName, bookTitle, l.getIssueDate(), l.getReturnDate() });
        }
    }
}
