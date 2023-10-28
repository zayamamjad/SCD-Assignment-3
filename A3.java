import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

 class Book 
 {
    private int id;
    private String title;
    private String author;
    private int publicationYear;
    private int popularity;
    private double cost;

    public Book(int id, String title, String author, int publicationYear, int popularity, double cost) 
    {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.popularity = popularity;
        this.cost = cost;
    }

    public Object getTitle() {
        return title;
    }

    public Object getAuthor() {
        return author;
    }

    public Object getId() {
        return id;
    }

    public Object getPublicationYear() {
        return publicationYear;
    }

    public Object getPopularity() {
        return popularity;
    }

    public Object getPrice() {
        return cost;
    }

    public void setTitle(String newTitle) {
        this.title=newTitle;
    }

    public void setAuthor(String newAuthor) {
        this.author=newAuthor;
    }

    public void setPublicationYear(int newYear) {
        this.publicationYear=newYear;
    }

    public void setPrice(double newPrice) {
        this.cost=newPrice;
    }

    public void increasePopularity() 
    {
        this.popularity++;
    }
}

public class A3 
{
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Book> books;
    private int hoveredRow = -1;
    class CustomRenderer extends DefaultTableCellRenderer 
    {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (isSelected) {
            c.setBackground(Color.CYAN);
        } else if (table.isRowSelected(row)) {
            c.setBackground(Color.CYAN);
        } else if (row == hoveredRow) {
            c.setBackground(Color.LIGHT_GRAY);
        } else {
            c.setBackground(Color.WHITE);
        }

        return c;
    }
    
    
}


class CustomEditor extends AbstractCellEditor implements TableCellEditor {
    private JButton button;
    private int selectedRow;

    public CustomEditor() {
        button = new JButton();
        button.addActionListener(e -> {
            fireEditingStopped();
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        button.setText(value.toString());
        selectedRow = row;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        // Return the value associated with the cell
        return button.getText();
    }
}

    public A3() {
        frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);

        tableModel = new DefaultTableModel(0, 7);
        tableModel.setColumnIdentifiers(new Object[]{"ID", "Title", "Author", "Publication Year", "Popularity", "Cost", "Read"});
        table = new JTable(tableModel);

        table.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor());

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel();
        frame.add(footerPanel, BorderLayout.SOUTH);
       
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.addMouseListener(new MouseAdapter() 
        {
            public void mousePressed(MouseEvent e) 
            {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) 
                {
                    table.setRowSelectionInterval(row, row);
                }
            }
        });

        JButton addButton = new JButton("Add Book");
        JButton editButton = new JButton("Edit Book");
        JButton deleteButton = new JButton("Delete Book");
        JButton viewPopularityButton = new JButton("View Popularity");

        footerPanel.add(addButton);
        footerPanel.add(editButton);
        footerPanel.add(deleteButton);
        footerPanel.add(viewPopularityButton);
        
    
        frame.addWindowListener(new WindowAdapter() 
        {
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(frame, "Do you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) 
                {
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }
                else
                {
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog addBookDialog = new JDialog(frame, "Add New Book", true);
                addBookDialog.setLayout(new GridLayout(6, 2));
        
                JTextField titleField = new JTextField();
                JTextField authorField = new JTextField();
                JTextField yearField = new JTextField();
                JTextField priceField = new JTextField();
                JTextArea contentArea = new JTextArea(); 
        
                addBookDialog.add(new JLabel("Book Title:"));
                addBookDialog.add(titleField);
                addBookDialog.add(new JLabel("Author:"));
                addBookDialog.add(authorField);
                addBookDialog.add(new JLabel("Publication Year:"));
                addBookDialog.add(yearField);
                addBookDialog.add(new JLabel("Price:"));
                addBookDialog.add(priceField);
                addBookDialog.add(new JLabel("Content:"));
                addBookDialog.add(new JScrollPane(contentArea));
        
                JButton submitButton = new JButton("Add");
                addBookDialog.add(submitButton);
        
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String title = titleField.getText();
                        String author = authorField.getText();
                        int year = Integer.parseInt(yearField.getText());
                        double price = Double.parseDouble(priceField.getText());
                        String content = contentArea.getText(); // Get the book content
        
                        if (title.isEmpty() || author.isEmpty()) {
                            JOptionPane.showMessageDialog(addBookDialog, "Title and Author fields cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            int newId = -1;
                            for (Book book : books) {
                                int bookId = (int) book.getId();
                                if (bookId > newId) {
                                    newId = bookId;
                                }
                            }
                            newId++;
                            Book newBook = new Book(newId, title, author, year, 0, price);
                            books.add(newBook);
                            tableModel.addRow(new Object[]{newId, title, author, year, 0, price, "Read"});
        
                            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("books.txt", true)))) 
                            {
                                writer.println(newId + "," + title + "," + author + "," + year + ",0," + price);
                            }
                             catch (IOException ex) {
                                ex.printStackTrace();
                            }
        
                            // Create a new .txt file with book content
                            try (PrintWriter contentWriter = new PrintWriter(title + ".txt")) {
                                contentWriter.println(content);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
        
                            addBookDialog.dispose();
                        }
                    }
                });
        
                addBookDialog.setSize(400, 300);
                addBookDialog.setVisible(true);
            }
        });
        


editButton.addActionListener(new ActionListener() 
{
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) 
        {
            Book selectedBook = books.get(selectedRow);

            JFrame editFrame = new JFrame("Edit Book");
            editFrame.setSize(400, 200);
            editFrame.setLayout(new GridLayout(5, 2));

            JTextField titleField = new JTextField(selectedBook.getTitle().toString());
            JTextField authorField = new JTextField(selectedBook.getAuthor().toString());
            JTextField yearField = new JTextField(String.valueOf(selectedBook.getPublicationYear()));
            JTextField priceField = new JTextField(String.valueOf(selectedBook.getPrice()));

            JButton editButton = new JButton("Edit");
            editButton.addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e) 
                {
                    String newTitle = titleField.getText();
                    String newAuthor = authorField.getText();
                    int newYear = Integer.parseInt(yearField.getText());
                    double newPrice = Double.parseDouble(priceField.getText());

                    selectedBook.setTitle(newTitle);
                    selectedBook.setAuthor(newAuthor);
                    selectedBook.setPublicationYear(newYear);
                    selectedBook.setPrice(newPrice);

                    try (PrintWriter writer = new PrintWriter(new FileWriter("books.txt", false))) 
                    {
                        
                        for (Book book : books) 
                        {
                            writer.println(
                                book.getId() + "," +
                                book.getTitle() + "," +
                                book.getAuthor() + "," +
                                book.getPublicationYear() + "," +
                                book.getPopularity() + "," +
                                book.getPrice()
                            );
                        }
                    } catch (IOException ex) 
                    {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Error saving book data", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.setRowCount(0);
                    editFrame.dispose();
                    updateTable();
                }
            });

            editFrame.add(new JLabel("Book Title:"));
            editFrame.add(titleField);
            editFrame.add(new JLabel("Author:"));
            editFrame.add(authorField);
            editFrame.add(new JLabel("Publication Year:"));
            editFrame.add(yearField);
            editFrame.add(new JLabel("Price:"));
            editFrame.add(priceField);
            editFrame.add(new JLabel()); 
            editFrame.add(editButton);
            editFrame.setVisible(true);
        } 
        else 
        {
            JOptionPane.showMessageDialog(frame, "Select a book to edit.");
        }
    }
});


        deleteButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) 
                {
                    books.remove(selectedRow);
                    try (PrintWriter writer = new PrintWriter(new FileWriter("books.txt", false))) 
                    {
                        for (Book book : books) 
                        {
                            writer.println(
                                book.getId() + "," +
                                book.getTitle() + "," +
                                book.getAuthor() + "," +
                                book.getPublicationYear() + "," +
                                book.getPopularity() + "," +
                                book.getPrice()
                            );
                        }
                    } 
                    catch (IOException ex) 
                    {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Error saving book data", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.setRowCount(0);
                    updateTable();
                } 
                else 
                {
                    JOptionPane.showMessageDialog(frame, "Select a book to delete.");
                }
            }
        });

        viewPopularityButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                int bookCount = books.size();
                int[] popularityArray = new int[bookCount];
                String[] bookNamesArray = new String[bookCount];
                for (int i = 0; i < bookCount; i++) 
                {
                    Book book = books.get(i);
                    popularityArray[i] = (int) book.getPopularity();
                    bookNamesArray[i] = book.getTitle().toString();
                }
                int n=popularityArray.length;
                for (int i = 0; i < n - 1; i++) 
                {
                    for (int j = 0; j < n - i - 1; j++) 
                    {
                        if (popularityArray[j] > popularityArray[j + 1]) 
                        {
                            int temp = popularityArray[j];
                            popularityArray[j] = popularityArray[j + 1];
                            popularityArray[j + 1] = temp;

                            String temp2=bookNamesArray[j];
                            bookNamesArray[j] = bookNamesArray[j + 1];
                            bookNamesArray[j + 1] = temp2;
                        }
                    }
                }
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("anythingelse.csv"))) 
                {
                    writer.write("Book Name,Popularity Count\n");
                    for (int i=0;i<n;i++) 
                    {
                        writer.write(bookNamesArray[i]+ "," + popularityArray[i] + "\n");
                    }
                } 
                catch (IOException ee) 
                {
                    ee.printStackTrace();
                }
                try 
                {
                    ProcessBuilder processBuilder = new ProcessBuilder("python", "barchart.py");
                    Process process = processBuilder.start();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        

        loadBooksFromTextFile(); 
        updateTable(); 
        frame.setVisible(true);
        table.setDefaultRenderer(Object.class, new CustomRenderer());
        table.setDefaultEditor(Object.class, new CustomEditor());
        

table.addMouseMotionListener(new MouseMotionAdapter() {
    @Override
    public void mouseMoved(MouseEvent e) {
        int row = table.rowAtPoint(e.getPoint());
        if (row != hoveredRow) {
            hoveredRow = row;
            table.repaint();
        }
    }
});

        
    }
    private void loadBooksFromTextFile() 
    {
        books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("books.txt"))) 
        {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    int id = Integer.parseInt(parts[0].trim());
                    String title = parts[1].trim();
                    String author = parts[2].trim();
                    int publicationYear = Integer.parseInt(parts[3].trim());
                    int popularity = Integer.parseInt(parts[4].trim());
                    double cost = Double.parseDouble(parts[5].trim());
                    
                    books.add(new Book(id, title, author, publicationYear, popularity, cost));
                }
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    private void updateTable() 
    {
        for (Book book : books) {
            tableModel.addRow(new Object[]{book.getId(), book.getTitle(), book.getAuthor(), book.getPublicationYear(), book.getPopularity(), book.getPrice(), "Read"});
        }
    }
    class ButtonRenderer extends JButton implements TableCellRenderer 
    {
        public ButtonRenderer() 
        {
            setOpaque(true);
        }
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Read");
            return this;
        }
    }
    class ButtonEditor extends DefaultCellEditor 
    {
        private JButton button;
        public ButtonEditor() 
        {
            super(new JCheckBox());
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e) 
                {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) 
                    {
                        Book selectedBook = books.get(selectedRow);
                        String bookTitle = (String) selectedBook.getTitle();
                        selectedBook.increasePopularity();
                        String fileName = bookTitle + ".txt"; 
                        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) 
                        {
                            String line;
                            StringBuilder content = new StringBuilder();
                            while ((line = reader.readLine()) != null) 
                            {
                                content.append(line).append("\n");
                            }
                        
                            JTextArea textArea = new JTextArea(content.toString());
                            textArea.setWrapStyleWord(true);
                            textArea.setLineWrap(true);
                            textArea.setCaretPosition(0);
                            textArea.setEditable(false);
                        
                            JScrollPane scrollPane = new JScrollPane(textArea);
                            scrollPane.setPreferredSize(new Dimension(620, 420)); 
                        
                            JFrame frame = new JFrame("Read Book: " + bookTitle);
                            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                            
                            frame.addWindowListener(new WindowAdapter() 
                            {
                                @Override
                                public void windowClosing(WindowEvent e) 
                                {
                                    int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to close this book?", "Close Confirmation", JOptionPane.YES_NO_OPTION);
                                    if (result == JOptionPane.YES_OPTION) 
                                    {
                                        frame.dispose();
                                    }
                                }
                            });
                        
                            frame.add(scrollPane);
                            frame.pack();
                            frame.setVisible(true);
                        } 
                        catch (IOException ex) 
                        {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Error reading the book content", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
        }
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) 
        {
            if (isSelected) 
            {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } 
            else 
            {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            button.setText("Read");
            return button;
        }
    }

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(new Runnable() 
        {
            @Override
            public void run() {
                new A3();
            }
        });
    }
}
