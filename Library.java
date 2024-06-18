import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Library extends JFrame {
    private List<Book> books;
    private List<Member> members;
    private List<Loan> loans;
    private List<Loan> loanHistory;
    private JTextArea displayArea;

    public Library() {
        books = new ArrayList<>();
        members = new ArrayList<>();
        loans = new ArrayList<>();
        loanHistory = new ArrayList<>();

        loadBooks();
        loadMembers();
        loadLoans();
        loadLoanHistory();

        setTitle("Library Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Custom look and feel
        UIManager.put("Panel.background", new ColorUIResource(Color.DARK_GRAY));
        UIManager.put("OptionPane.background", new ColorUIResource(Color.DARK_GRAY));
        UIManager.put("Button.background", new ColorUIResource(new Color(45, 45, 45)));
        UIManager.put("Button.foreground", new ColorUIResource(new Color(0, 255, 255)));
        UIManager.put("Button.border", new EmptyBorder(10, 10, 10, 10));
        UIManager.put("TextArea.background", new ColorUIResource(Color.BLACK));
        UIManager.put("TextArea.foreground", new ColorUIResource(new Color(0, 255, 255)));
        UIManager.put("TextArea.caretForeground", new ColorUIResource(Color.WHITE));
        UIManager.put("ScrollPane.border", new EmptyBorder(10, 10, 10, 10));
        UIManager.put("Table.background", new ColorUIResource(Color.BLACK));
        UIManager.put("Table.foreground", new ColorUIResource(new Color(0, 255, 255)));
        UIManager.put("Table.gridColor", new ColorUIResource(Color.GRAY));
        UIManager.put("TableHeader.background", new ColorUIResource(new Color(45, 45, 45)));
        UIManager.put("TableHeader.foreground", new ColorUIResource(new Color(0, 255, 255)));
        UIManager.put("TableHeader.cellBorder", new EmptyBorder(5, 5, 5, 5));

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(9, 1, 5, 5)); // 9 rows, 1 column, with gaps between buttons

        JButton searchButton = createCustomButton("Search Book");
        searchButton.addActionListener(e -> searchBook());
        buttonPanel.add(searchButton);

        JButton borrowButton = createCustomButton("Borrow Book");
        borrowButton.addActionListener(e -> borrowBook());
        buttonPanel.add(borrowButton);

        JButton returnButton = createCustomButton("Return Book");
        returnButton.addActionListener(e -> returnBook());
        buttonPanel.add(returnButton);

        JButton viewHistoryButton = createCustomButton("View Loan History");
        viewHistoryButton.addActionListener(e -> viewLoanHistory());
        buttonPanel.add(viewHistoryButton);

        JButton viewBooksButton = createCustomButton("View Books");
        viewBooksButton.addActionListener(e -> viewBooks());
        buttonPanel.add(viewBooksButton);

        JButton viewMembersButton = createCustomButton("View Members");
        viewMembersButton.addActionListener(e -> viewMembers());
        buttonPanel.add(viewMembersButton);

        JButton addMemberButton = createCustomButton("Add Member");
        addMemberButton.addActionListener(e -> addMember());
        buttonPanel.add(addMemberButton);

        JButton addBookButton = createCustomButton("Add Book");
        addBookButton.addActionListener(e -> addBook());
        buttonPanel.add(addBookButton);

        JButton viewBorrowedBookButton = createCustomButton("View Borrowed 3Books");
        viewBorrowedBookButton.addActionListener(e -> viewBorrowedBooks());
        buttonPanel.add(viewBorrowedBookButton);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(buttonPanel, BorderLayout.WEST);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createCustomButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Verdana", Font.BOLD, 14));
        button.setBackground(new Color(45, 45, 45));
        button.setForeground(new Color(0, 255, 255));
        button.setBorder(new EmptyBorder(10, 10, 10, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadBooks() {
        try (BufferedReader reader = new BufferedReader(new FileReader("books.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                books.add(new Book(data[0], data[1], data[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMembers() {
        try (BufferedReader reader = new BufferedReader(new FileReader("members.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                members.add(new Member(data[0], data[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadLoans() {
        try (BufferedReader reader = new BufferedReader(new FileReader("loans.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                loans.add(new Loan(data[0], data[1], data[2], data[3], Integer.parseInt(data[4])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadLoanHistory() {
        try (BufferedReader reader = new BufferedReader(new FileReader("loan_history.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                loanHistory.add(new Loan(data[0], data[1], data[2], data[3], Integer.parseInt(data[4])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBooks() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("books.csv"))) {
            for (Book book : books) {
                writer.println(book.getId() + "," + book.getTitle() + "," + book.getAuthor());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveMembers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("members.csv"))) {
            for (Member member : members) {
                writer.println(member.getId() + "," + member.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveLoans() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("loans.csv"))) {
            for (Loan loan : loans) {
                writer.println(loan.getBookId() + "," + loan.getMemberId() + "," + loan.getDate() + "," + loan.getReturnDate() + "," + loan.getPeriod());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveLoanHistory() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("loan_history.csv"))) {
            for (Loan loan : loanHistory) {
                writer.println(loan.getBookId() + "," + loan.getMemberId() + "," + loan.getDate() + "," + loan.getReturnDate() + "," + loan.getPeriod());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void searchBook() {
        String[] bookTitles = books.stream().map(Book::getTitle).toArray(String[]::new);
        String bookTitle = (String) JOptionPane.showInputDialog(null, "Select a book:", "Search Book",
                JOptionPane.QUESTION_MESSAGE, null, bookTitles, bookTitles[0]);

        if (bookTitle != null) {
            Book selectedBook = books.stream().filter(book -> book.getTitle().equals(bookTitle)).findFirst().orElse(null);

            if (selectedBook != null) {
                displayArea.setText("Search Results:\n" + selectedBook);
            }
        }
    }

    private void borrowBook() {
        String[] bookTitles = books.stream().map(Book::getTitle).toArray(String[]::new);
        String bookTitle = (String) JOptionPane.showInputDialog(null, "Select a book:", "Borrow Book",
                JOptionPane.QUESTION_MESSAGE, null, bookTitles, bookTitles[0]);

        if (bookTitle != null) {
            Book selectedBook = books.stream().filter(book -> book.getTitle().equals(bookTitle)).findFirst().orElse(null);

            if (selectedBook != null) {
                JComboBox<String> memberDropdown = new JComboBox<>(members.stream().map(Member::getName).toArray(String[]::new));
                int memberOption = JOptionPane.showOptionDialog(null, memberDropdown, "Select Member",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

                if (memberOption == JOptionPane.OK_OPTION) {
                    String memberName = (String) memberDropdown.getSelectedItem();
                    Member member = members.stream().filter(m -> m.getName().equals(memberName)).findFirst().orElse(null);

                    if (member != null) {
                        String periodStr = JOptionPane.showInputDialog("Enter borrowing period in days:");
                        int period = Integer.parseInt(periodStr);
                        LocalDateTime now = LocalDateTime.now();
                        String date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        String returnDate = now.plusDays(period).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        Loan newLoan = new Loan(selectedBook.getId(), member.getId(), date, returnDate, period);
                        loans.add(newLoan);
                        loanHistory.add(newLoan);
                        member.incrementLoanCount();
                        saveLoans();
                        saveLoanHistory();
                        saveMembers();
                        displayArea.setText("Book borrowed successfully on " + date + " by " + member.getName() + " (ID: " + member.getId() + ")! Return by " + returnDate);
                    } else {
                        displayArea.setText("Member not found!");
                    }
                }
            }
        }
    }

    private void returnBook() {
        String[] bookTitles = loans.stream()
                .map(loan -> getBookTitleById(loan.getBookId()))
                .distinct()
                .toArray(String[]::new);
        String bookTitle = (String) JOptionPane.showInputDialog(null, "Select a book to return:", "Return Book",
                JOptionPane.QUESTION_MESSAGE, null, bookTitles, bookTitles[0]);

        if (bookTitle != null) {
            Book selectedBook = books.stream().filter(book -> book.getTitle().equals(bookTitle)).findFirst().orElse(null);

            if (selectedBook != null) {
                List<Loan> loansForBook = loans.stream()
                        .filter(loan -> loan.getBookId().equals(selectedBook.getId()))
                        .collect(Collectors.toList());

                if (loansForBook.size() > 1) {
                    JComboBox<String> memberDropdown = new JComboBox<>(loansForBook.stream()
                            .map(loan -> getMemberNameById(loan.getMemberId()))
                            .toArray(String[]::new));
                    int option = JOptionPane.showOptionDialog(null, memberDropdown, "Select Member",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

                    if (option == JOptionPane.OK_OPTION) {
                        String memberName = (String) memberDropdown.getSelectedItem();
                        Member member = members.stream()
                                .filter(m -> m.getName().equals(memberName))
                                .findFirst()
                                .orElse(null);

                        if (member != null) {
                            Loan loanToRemove = loansForBook.stream()
                                    .filter(loan -> loan.getMemberId().equals(member.getId()))
                                    .findFirst()
                                    .orElse(null);

                            if (loanToRemove != null) {
                                loans.remove(loanToRemove);
                                member.decrementLoanCount();
                                saveLoans();
                                saveMembers();
                                displayArea.setText("Book returned successfully by " + member.getName() + "!");
                            } else {
                                displayArea.setText("Loan not found for member!");
                            }
                        }
                    }
                } else {
                    Loan loanToRemove = loansForBook.get(0);
                    Member member = members.stream()
                            .filter(m -> m.getId().equals(loanToRemove.getMemberId()))
                            .findFirst()
                            .orElse(null);

                    if (member != null) {
                        loans.remove(loanToRemove);
                        member.decrementLoanCount();
                        saveLoans();
                        saveMembers();
                        displayArea.setText("Book returned successfully by " + member.getName() + "!");
                    } else {
                        displayArea.setText("Member not found!");
                    }
                }
            }
        }
    }

    private void viewLoanHistory() {
        String[] columnNames = {"Book ID", "Book Title", "Member ID", "Member Name", "Date", "Return Date", "Period"};
        Object[][] data = loanHistory.stream()
                .map(loan -> new Object[]{
                        loan.getBookId(),
                        getBookTitleById(loan.getBookId()),
                        loan.getMemberId(),
                        getMemberNameById(loan.getMemberId()),
                        loan.getDate(),
                        loan.getReturnDate(),
                        loan.getPeriod()
                })
                .toArray(Object[][]::new);

        JTable table = new JTable(new DefaultTableModel(data, columnNames));
        JOptionPane.showMessageDialog(null, new JScrollPane(table), "Loan History", JOptionPane.PLAIN_MESSAGE);
    }

    private void viewBorrowedBooks() {
        String[] columnNames = {"Book Title", "Member Name", "Time to Deadline"};
        Object[][] data = loans.stream()
                .map(loan -> {
                    LocalDateTime returnDateTime = LocalDateTime.parse(loan.getReturnDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    Duration duration = Duration.between(LocalDateTime.now(), returnDateTime);
                    long days = duration.toDays();
                    long hours = duration.minusDays(days).toHours();
                    String timeToDeadline = days + " days, " + hours + " hours";

                    return new Object[]{
                            getBookTitleById(loan.getBookId()),
                            getMemberNameById(loan.getMemberId()),
                            timeToDeadline
                    };
                })
                .toArray(Object[][]::new);

        JTable table = new JTable(new DefaultTableModel(data, columnNames));
        JOptionPane.showMessageDialog(null, new JScrollPane(table), "Borrowed Books", JOptionPane.PLAIN_MESSAGE);
    }

    private String getBookTitleById(String bookId) {
        return books.stream().filter(book -> book.getId().equals(bookId)).map(Book::getTitle).findFirst().orElse("Unknown");
    }

    private String getMemberNameById(String memberId) {
        return members.stream().filter(member -> member.getId().equals(memberId)).map(Member::getName).findFirst().orElse("Unknown");
    }

    private void viewBooks() {
        String[] columnNames = {"ID", "Title", "Author"};
        Object[][] data = books.stream()
                .map(book -> new Object[]{book.getId(), book.getTitle(), book.getAuthor()})
                .toArray(Object[][]::new);

        JTable table = new JTable(new DefaultTableModel(data, columnNames));
        JOptionPane.showMessageDialog(null, new JScrollPane(table), "Books List", JOptionPane.PLAIN_MESSAGE);
    }

    private void viewMembers() {
        String[] columnNames = {"ID", "Name", "Loan Count"};
        Object[][] data = members.stream()
                .map(member -> new Object[]{member.getId(), member.getName(), member.getLoanCount()})
                .toArray(Object[][]::new);

        JTable table = new JTable(new DefaultTableModel(data, columnNames));
        JOptionPane.showMessageDialog(null, new JScrollPane(table), "Members List", JOptionPane.PLAIN_MESSAGE);
    }

    private void addMember() {
        String memberName = JOptionPane.showInputDialog("Enter member name:");
        if (memberName != null) {
            String memberId = generateRandomId(6);
            Member newMember = new Member(memberId, memberName);
            members.add(newMember);
            saveMembers();
            displayArea.setText("Member added successfully!\n" + newMember);
        }
    }

    private void addBook() {
        String bookTitle = JOptionPane.showInputDialog("Enter book title:");
        String bookAuthor = JOptionPane.showInputDialog("Enter book author:");
        if (bookTitle != null && bookAuthor != null) {
            String bookId = generateRandomId(6);
            Book newBook = new Book(bookId, bookTitle, bookAuthor);
            books.add(newBook);
            saveBooks();
            displayArea.setText("Book added successfully!\n" + newBook);
        }
    }

    private String generateRandomId(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // generates a digit between 0-9
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Library::new);
    }
}


