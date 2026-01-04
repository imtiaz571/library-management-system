package view;

import model.Book;
import model.Category;
import repository.FileRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class BookPanel extends JPanel {
    private JTextField txtId, txtTitle, txtAuthor;
    private JComboBox<Category> cmbCategory;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Book> bookList;
    private FileRepository<Book> repository;
    private final String DATA_FILE = "books.ser";

    public BookPanel() {
        repository = new FileRepository<>(DATA_FILE);
        bookList = repository.load();
        if (bookList == null)
            bookList = new ArrayList<>();

        setOpaque(false);
        setLayout(new BorderLayout(20, 20));
        initComponents();
        loadTableData();
    }

    private void initComponents() {
        JLabel header = new JLabel("Book Management");
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

        formLayout.add(createLabeledField("Book ID (Auto):", txtId = new JTextField()));
        txtId.setEditable(false);

        formLayout.add(createLabeledField("Title:", txtTitle = new JTextField()));
        formLayout.add(createLabeledField("Author:", txtAuthor = new JTextField()));

        JPanel catPanel = new JPanel(new BorderLayout());
        catPanel.setOpaque(false);
        catPanel.add(new JLabel("Category:"), BorderLayout.NORTH);
        cmbCategory = new JComboBox<>(Category.values());
        cmbCategory.setBackground(Color.WHITE);
        catPanel.add(cmbCategory, BorderLayout.CENTER);
        formLayout.add(catPanel);

        formCard.add(formLayout, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton btnAdd = new view.components.RoundedButton("Add Book", view.theme.ModernTheme.PRIMARY_COLOR);
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

        String[] columns = { "ID", "Title", "Author", "Category" };
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

        btnAdd.addActionListener(e -> addBook());
        btnEdit.addActionListener(e -> updateBook());
        btnDelete.addActionListener(e -> deleteBook());
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
        txtTitle.setText(tableModel.getValueAt(row, 1).toString());
        txtAuthor.setText(tableModel.getValueAt(row, 2).toString());
        cmbCategory.setSelectedItem(tableModel.getValueAt(row, 3));
    }

    private void addBook() {
        String title = txtTitle.getText();
        String author = txtAuthor.getText();
        Category category = (Category) cmbCategory.getSelectedItem();

        if (title.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        int id = bookList.isEmpty() ? 1001 : bookList.get(bookList.size() - 1).getBookId() + 1;

        Book book = new Book(id, title, author, category);
        bookList.add(book);
        repository.save(bookList);
        loadTableData();
        clearForm();
    }

    private void updateBook() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a book to update.");
            return;
        }

        int id = Integer.parseInt(txtId.getText());
        String title = txtTitle.getText();
        String author = txtAuthor.getText();
        Category category = (Category) cmbCategory.getSelectedItem();

        for (Book b : bookList) {
            if (b.getBookId() == id) {
                b.setTitle(title);
                b.setAuthor(author);
                b.setCategory(category);
                break;
            }
        }
        repository.save(bookList);
        loadTableData();
        clearForm();
    }

    private void deleteBook() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a book to delete.");
            return;
        }

        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        bookList.removeIf(b -> b.getBookId() == id);
        repository.save(bookList);
        loadTableData();
        clearForm();
    }

    private void clearForm() {
        txtId.setText("");
        txtTitle.setText("");
        txtAuthor.setText("");
        cmbCategory.setSelectedIndex(0);
        table.clearSelection();
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        for (Book b : bookList) {
            tableModel.addRow(new Object[] { b.getBookId(), b.getTitle(), b.getAuthor(), b.getCategory() });
        }
    }
}
