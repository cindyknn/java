package GUI;

import Backend.Bookshelf;
import Backend.Book;
import Backend.LogBook;
import Backend.QuoteList;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author student
 */
public class MainFrame extends javax.swing.JFrame {

    private Bookshelf shelf;
    private LogBook log;
    private QuoteList quotes;
   
    private String filePath;
    
    /**
     * Creates new form PrototypeUI
     */
    public MainFrame() {
        guiOptions();
        setDefaultFilePath();
        
        
        shelf = new Bookshelf();
        log = new LogBook();
        quotes = new QuoteList();
        
        load();
        initComponents();
        refreshPanels();
    }
    
    // Set options, look, and feel of GUI
    private void guiOptions()
    {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null,
                    ex);
        }        
    }

    
    
    // **** All Methods for file I/O are written here **** //
    // *************************************************** //
    
    private void setDefaultFilePath(){
        String dir = System.getProperty("user.dir");
        filePath = dir + "/bookData.txt";
    }
        
    //private Bookshelf shelf;
    //private LogBook log;
    //private QuoteList quotes;

    public void load() {
        try {
            // Load file and read info to RAM from file
            BufferedReader loadFile;
            loadFile = new BufferedReader(new FileReader(
                    filePath));
            
            
            // Create a string to read in data from the file
            String input;
            
            /** Read in from text file 2 lines for each athlete while there are 
             * still lines to be read in
             * First line is their personal info (first, last, number, position)
             * Second line is their goal data (0, 3, 1, 0, 1, etc.)
             */ 
            
            input = loadFile.readLine();
            int goalB = Integer.parseInt(input);
            shelf.setBooksGoal(goalB);
            
            while (!((input = loadFile.readLine()).equals("=============================="))) {
                
                String[] values = input.split(",");
                String title = values[0];
                int pages = Integer.parseInt(values[1]);
                String dateB = values[2];
                String dateF = values[3];
                shelf.addBook(new Book(title, pages, dateB, dateF));
            }
            
            input = loadFile.readLine();
            int goalP = Integer.parseInt(input);
            log.setPagesGoal(goalP);
            input = loadFile.readLine();
            int goalT = Integer.parseInt(input);
            log.setTimeGoal(goalT);
            
            
            while (!((input = loadFile.readLine()).equals("=============================="))) {
                String[] values = input.split(",");
                String date = values[0];
                int pages = Integer.parseInt(values[1]);
                int time = Integer.parseInt(values[2]);
                log.logDates(date);
                log.logPages(pages);
                log.logMins(time);
            }    
            
            while (!((input = loadFile.readLine()) == null)) {
                String[] values = input.split(",");
                String title = values[0];
                String quote = values[1];
                quotes.addQuoteTitle(title);
                quotes.addQuote(quote);
            }    
            
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error trying to "
                    + "load file: " + ex,
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    
    public void save() {
        // Output the data to a text file
        try (PrintWriter writer = new PrintWriter(new File(filePath))) {
            // Loop through players and write their information to the file
            // Names on the first line, goals on the second line
            int goalB = shelf.getBooksGoal();
            writer.println(goalB);
            ArrayList<Book> books = shelf.getBooks();
            for (int n = 0; n < books.size(); n++) {
                Book cur = books.get(n);
                writer.println(cur.getBookTitle() + "," + cur.getNumPages() + "," + cur.getDateBeg() + "," + cur.getDateFin());
            }
            
            writer.println("==============================");
            int goalP = log.getPagesGoal();
            int goalT = log.getTimeGoal();
            writer.println(goalP);
            writer.println(goalT);
            for (int n = 0; n < log.getDates().size(); n++) {
                writer.println(log.getDates().get(n) + "," +  log.getPages().get(n) + "," + log.getMins().get(n));
            }
            
            writer.println("==============================");
            for (int n = 0; n < quotes.getTotQuotes(); n++) {
                writer.println(quotes.getTitleQuote().get(n) + "," + quotes.getQuotes().get(n));
            }
            
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error trying to load file: " + ex,
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    
    // ************* Methods which change/update the GUI ************* //
      
    protected void refreshPanels() {
        drawBookshelf();
        drawPagesTime();
        drawQuotes();
        refreshBarLabels();
        save();
    }
    
    
    private void refreshBarLabels(){
        totalBooksLabel.setText("Total Books: " + shelf.getNumBooks() + " /");
        totalPagesLabel.setText("Total Pages: " + log.getTotPages() + " /");
        totalTimeLabel.setText("Total Reading Time: " + log.getTotMins() + " /");
        
        booksProgressBar.setMaximum(shelf.getBooksGoal());
        booksProgressBar.setValue(shelf.getNumBooks());
        
        pagesProgressBar.setMaximum(log.getPagesGoal());
        pagesProgressBar.setValue(log.getTotPages());
        
        timeProgressBar.setMaximum(log.getTimeGoal());
        timeProgressBar.setValue(log.getTotMins());
    }
    
    
    private void drawBookshelf() {
        // Get Roster Header
        String[] bookshelfHeader = {
            "Title", "Pages", "Date Began", "Date Finished"
        };

        // Get data for table
        String[][] bookShelfData = new String[shelf.getNumBooks()][bookshelfHeader.length];
        // Populate table with data
        for (int x = 0; x < bookShelfData.length; x++) {
            // Athlete Info
            bookShelfData[x][0] = shelf.getBooks().get(x).getBookTitle();
            bookShelfData[x][1] = String.valueOf(shelf.getBooks().get(x).getNumPages());
            bookShelfData[x][2] = shelf.getBooks().get(x).getDateBeg();
            bookShelfData[x][3] = shelf.getBooks().get(x).getDateFin();
            
        }
        
        //Create JTable with all our data
        bookshelfTable.setModel(new InfoTableModel(bookShelfData, bookshelfHeader));

        bookshelfTable.getColumnModel().getColumn(0).setMinWidth(100);
        bookshelfTable.getColumnModel().getColumn(1).setMinWidth(25);
        bookshelfTable.getColumnModel().getColumn(2).setMinWidth(100);
    }
    
    private void drawPagesTime() {
        // Get Roster Header
        String[] pagesTimeHeader = {
            "Date", "Pages", "Reading Time"
        };

        // Get data for table
        String[][] pagesTimeData = new String[(log.getDates()).size()][pagesTimeHeader.length];
        // Populate table with data
        for (int x = 0; x < pagesTimeData.length; x++) {
            // Athlete Info
            pagesTimeData[x][0] = log.getDates().get(x);
            pagesTimeData[x][1] = String.valueOf(log.getPages().get(x));
            pagesTimeData[x][2] = String.valueOf(log.getMins().get(x));
            
        }
        
        //Create JTable with all our data
        pagesTimeTable.setModel(new InfoTableModel(pagesTimeData, pagesTimeHeader));

        pagesTimeTable.getColumnModel().getColumn(0).setMinWidth(100);
        pagesTimeTable.getColumnModel().getColumn(1).setMinWidth(25);
        pagesTimeTable.getColumnModel().getColumn(2).setMinWidth(100);
    }
    
    
    private void drawQuotes(){
        String[] quoteHeader = {
            "Title", "Quote"
        };

        // Get data for table
        String[][] quoteData = new String[(quotes.getTotQuotes())][quoteHeader.length];
        // Populate table with data
        for (int x = 0; x < quoteData.length; x++) {
            // Athlete Info
            quoteData[x][0] = quotes.getTitleQuote().get(x);
            quoteData[x][1] = String.valueOf(quotes.getQuotes().get(x));
            
        }
        
        //Create JTable with all our data
        quotesTable.setModel(new InfoTableModel(quoteData, quoteHeader));

        quotesTable.getColumnModel().getColumn(0).setMinWidth(25);
        quotesTable.getColumnModel().getColumn(1).setMinWidth(200);
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        addBook = new javax.swing.JButton();
        logPages = new javax.swing.JButton();
        addQuote = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        timeProgressBar = new javax.swing.JProgressBar();
        totalBooksLabel = new javax.swing.JLabel();
        totalPagesLabel = new javax.swing.JLabel();
        booksProgressBar = new javax.swing.JProgressBar();
        totalTimeLabel = new javax.swing.JLabel();
        pagesProgressBar = new javax.swing.JProgressBar();
        jLabel11 = new javax.swing.JLabel();
        BookGoalText = new javax.swing.JTextField();
        PagesGoalText = new javax.swing.JTextField();
        TimeGoalText = new javax.swing.JTextField();
        bookshelfPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        bookshelfTable = new javax.swing.JTable();
        sortT = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        pagesTimeTable = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        quotesTable = new javax.swing.JTable();
        sortT1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        addBook.setText("Add a Book!");
        addBook.setFocusable(false);
        addBook.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addBook.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBookActionPerformed(evt);
            }
        });

        logPages.setText("Log your Time & Pages!");
        logPages.setFocusable(false);
        logPages.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        logPages.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        logPages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logPagesActionPerformed(evt);
            }
        });

        addQuote.setText("Add a Quote!");
        addQuote.setFocusable(false);
        addQuote.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addQuote.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addQuote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addQuoteActionPerformed(evt);
            }
        });

        jLabel10.setText("Welcome back to your Reading Log!");

        timeProgressBar.setStringPainted(true);

        totalBooksLabel.setText("Total Books");

        totalPagesLabel.setText("Total Pages");

        booksProgressBar.setMaximum(shelf.getBooksGoal());
        booksProgressBar.setStringPainted(true);

        totalTimeLabel.setText("Total Reading Minutes");

        pagesProgressBar.setStringPainted(true);

        jLabel11.setText("Your PROGRESS");

        BookGoalText.setText("10");
        BookGoalText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BookGoalTextActionPerformed(evt);
            }
        });

        PagesGoalText.setText("10");
        PagesGoalText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PagesGoalTextActionPerformed(evt);
            }
        });

        TimeGoalText.setText("10");
        TimeGoalText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TimeGoalTextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addComponent(addBook))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(booksProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(totalBooksLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(BookGoalText, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(logPages)
                        .addGap(102, 102, 102)
                        .addComponent(addQuote))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(totalPagesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(PagesGoalText, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel10)
                            .addComponent(pagesProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(60, 60, 60)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(timeProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(totalTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TimeGoalText, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(9, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(345, 345, 345)
                .addComponent(jLabel11)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addBook)
                    .addComponent(logPages)
                    .addComponent(addQuote))
                .addGap(60, 60, 60)
                .addComponent(jLabel11)
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalBooksLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalPagesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BookGoalText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PagesGoalText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TimeGoalText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(timeProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pagesProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(booksProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Home", jPanel1);

        jLabel3.setText("Your Bookshelf");

        bookshelfTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        bookshelfTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bookshelfTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(bookshelfTable);

        sortT.setText("Sort by Title");
        sortT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortTActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bookshelfPanelLayout = new javax.swing.GroupLayout(bookshelfPanel);
        bookshelfPanel.setLayout(bookshelfPanelLayout);
        bookshelfPanelLayout.setHorizontalGroup(
            bookshelfPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bookshelfPanelLayout.createSequentialGroup()
                .addGroup(bookshelfPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bookshelfPanelLayout.createSequentialGroup()
                        .addGap(0, 6, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 803, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(bookshelfPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 684, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(bookshelfPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sortT)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        bookshelfPanelLayout.setVerticalGroup(
            bookshelfPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bookshelfPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(sortT)
                .addContainerGap(102, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Bookshelf", bookshelfPanel);

        jLabel8.setText("Your LogBook");

        pagesTimeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        pagesTimeTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pagesTimeTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(pagesTimeTable);
        if (pagesTimeTable.getColumnModel().getColumnCount() > 0) {
            pagesTimeTable.getColumnModel().getColumn(0).setHeaderValue("Title 1");
            pagesTimeTable.getColumnModel().getColumn(1).setHeaderValue("Title 2");
            pagesTimeTable.getColumnModel().getColumn(2).setHeaderValue("Title 3");
            pagesTimeTable.getColumnModel().getColumn(3).setHeaderValue("Title 4");
        }

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 803, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 684, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(162, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("LogBook", jPanel6);

        jLabel4.setText("Your Favorite Quotes");

        quotesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "null", "Title 2"
            }
        ));
        quotesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                quotesTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(quotesTable);

        sortT1.setText("Sort by Title");
        sortT1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortT1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 803, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 684, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sortT1))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(sortT1)
                .addContainerGap(99, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("QuoteList", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void logPagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logPagesActionPerformed
        // TODO add your handling code here:
        JDialog dialog = new JDialog(this, true);
        dialog.add(new LogPagesTimePanel(this));
        dialog.pack();
        dialog.setVisible(true);
        refreshPanels();
    }//GEN-LAST:event_logPagesActionPerformed

    // **** Methods button functions **** //
    
    private void addBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBookActionPerformed
        // TODO add your handling code here:
        JDialog dialog = new JDialog(this, true);
        dialog.add(new AddBookPanel(this));
        dialog.pack();
        dialog.setVisible(true);
        refreshPanels();
    }//GEN-LAST:event_addBookActionPerformed

    private void BookGoalTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BookGoalTextActionPerformed
        // TODO add your handling code here:
        int input = Integer.parseInt(BookGoalText.getText());
        shelf.setBooksGoal(input);
        refreshPanels();
    }//GEN-LAST:event_BookGoalTextActionPerformed

    private void PagesGoalTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PagesGoalTextActionPerformed
        // TODO add your handling code here:
        int input = Integer.parseInt(PagesGoalText.getText());
        log.setPagesGoal(input);
        refreshPanels();
    }//GEN-LAST:event_PagesGoalTextActionPerformed

    private void TimeGoalTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TimeGoalTextActionPerformed
        // TODO add your handling code here:
        int input = Integer.parseInt(TimeGoalText.getText());
        log.setTimeGoal(input);
        refreshPanels();
    }//GEN-LAST:event_TimeGoalTextActionPerformed

    private void addQuoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addQuoteActionPerformed
        // TODO add your handling code here:
        JDialog dialog = new JDialog(this, true);
        dialog.add(new QuotesPanel(this));
        dialog.pack();
        dialog.setVisible(true);
        refreshPanels();
    }//GEN-LAST:event_addQuoteActionPerformed

    private void sortTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortTActionPerformed
        // TODO add your handling code here:
        for (int end = shelf.getBooks().size() - 1; end > 0; end--) {
            int max = 0;
            // Find index value of Max
            for (int x = 1; x <= end; x++) {
                if (shelf.getBooks().get(x).getBookTitle().toLowerCase().compareTo(
                        shelf.getBooks().get(max).getBookTitle().toLowerCase()) > 0) {
                    max = x;
                }
            }
            // Swap Max with End
            Book temp = shelf.getBooks().get(max);
            shelf.getBooks().set(max, shelf.getBooks().get(end));
            shelf.getBooks().set(end, temp);
        }
        
        refreshPanels();
    }//GEN-LAST:event_sortTActionPerformed

    private void sortT1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortT1ActionPerformed
        // TODO add your handling code here:
        for (int end = quotes.getTitleQuote().size() - 1; end > 0; end--) {
            int max = 0;
            // Find index value of Max
            for (int x = 1; x <= end; x++) {
                if (quotes.getTitleQuote().get(x).toLowerCase().compareTo(
                        quotes.getTitleQuote().get(max).toLowerCase()) > 0) {
                    max = x;
                }
            }
            // Swap Max with End
            String temp = quotes.getTitleQuote().get(max);
            quotes.getTitleQuote().set(max, quotes.getTitleQuote().get(end));
            quotes.getTitleQuote().set(end, temp);
            
            String tempo = quotes.getQuotes().get(max);
            quotes.getQuotes().set(max, quotes.getQuotes().get(end));
            quotes.getQuotes().set(end, tempo);
        }
        
        refreshPanels();
    }//GEN-LAST:event_sortT1ActionPerformed

    private void editBook(int book, int type) {
        switch (type) {
            case 0:
                String title = JOptionPane.showInputDialog(this, "Edit book title for \n"
                        + shelf.getBooks().get(book).getBookTitle(), shelf.getBooks().get(book).getBookTitle());
                shelf.getBooks().get(book).setBookTitle(title);
                break;
            case 1:
                 try {
                    int pages = Integer.parseInt(JOptionPane.showInputDialog(this, "Edit number of pages for \n"
                        + shelf.getBooks().get(book).getBookTitle(), shelf.getBooks().get(book).getNumPages()));
                    shelf.getBooks().get(book).setNumPages(pages);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter an number.",
                            "Invalid Data",
                            JOptionPane.ERROR_MESSAGE);
                }
                break;
                
            case 2:
                String beg = JOptionPane.showInputDialog(this, "Edit date began for \n"
                        + shelf.getBooks().get(book).getBookTitle(), shelf.getBooks().get(book).getDateBeg());
                if(beg.equals("")){
                    beg = " ";
                }
        
                shelf.getBooks().get(book).setDateBeg(beg);
                break;
            case 3:
                String fin = JOptionPane.showInputDialog(this, "Edit date finished for \n"
                        + shelf.getBooks().get(book).getBookTitle(), shelf.getBooks().get(book).getDateFin());
                if(fin.equals("")){
                    fin = " ";
                }
                shelf.getBooks().get(book).setDateFin(fin);
                break;
            default:
                break;
        }
        refreshPanels();
    }

    private void editLog(int logE, int type) {
        switch (type) {
            case 0:
                String date = JOptionPane.showInputDialog(this, "Edit date for log entry on \n"
                        + log.getDates().get(logE), log.getDates().get(logE));
                if(date.equals("")){
                    date = " ";
                }
                log.getDates().set(logE,date);
                break;
            case 1:
                try {
                    int pages = Integer.parseInt(JOptionPane.showInputDialog(this, "Edit number of pages for log entry on\n"
                        + log.getDates().get(logE), log.getPages().get(logE)));
                    log.getPages().set(logE,pages);                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter an number.",
                            "Invalid Data",
                            JOptionPane.ERROR_MESSAGE);
                }
                break;
                
            case 2:
                try {
                    int time = Integer.parseInt(JOptionPane.showInputDialog(this, "Edit reading time for log entry on\n"
                        + log.getDates().get(logE), log.getMins().get(logE)));
                    log.getMins().set(logE,time);                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter an number.",
                            "Invalid Data",
                            JOptionPane.ERROR_MESSAGE);
                }
                break;
            default:
                break;
        }
        refreshPanels();
    }
    
    private void editQuote(int quoteE, int type) {
        switch (type) {
            case 0:
                String title = JOptionPane.showInputDialog(this, "Edit book title for quote from book\n"
                        + quotes.getTitleQuote().get(quoteE), quotes.getTitleQuote().get(quoteE));
                if(title.equals("")){
                    title = " ";
                }
                quotes.getTitleQuote().set(quoteE, title);
                break;
            case 1:
                String quote = JOptionPane.showInputDialog(this, "Edit quote for quote from book\n"
                        + quotes.getTitleQuote().get(quoteE), quotes.getQuotes().get(quoteE));
                if(quote.equals("")){
                    quote = " ";
                }
                quotes.getQuotes().set(quoteE, quote);
                break;
            
            default:
                break;
        }
        refreshPanels();
    }
    
    private void bookshelfTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookshelfTableMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() > 1) {
            int book = bookshelfTable.getSelectedRow();
            int type = bookshelfTable.getSelectedColumn();

            editBook(book, type);
        }
    }//GEN-LAST:event_bookshelfTableMouseClicked

    private void pagesTimeTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pagesTimeTableMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() > 1) {
            int logE = pagesTimeTable.getSelectedRow();
            int type = pagesTimeTable.getSelectedColumn();

            editLog(logE, type);
        }
    }//GEN-LAST:event_pagesTimeTableMouseClicked

    private void quotesTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_quotesTableMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() > 1) {
            int quoteE = quotesTable.getSelectedRow();
            int type = quotesTable.getSelectedColumn();

            editQuote(quoteE, type);
        }
    }//GEN-LAST:event_quotesTableMouseClicked

    
    // **** Methods which will manipulate the data in the Database **** //
    
    public ArrayList getTitleArr(){
        ArrayList<String> titles = new ArrayList<String>();
        for(int i =0; i<shelf.getNumBooks(); i++){
            titles.add(shelf.getBooks().get(i).getBookTitle());
        }
        return titles;
    }
    
    
    public void addBook(String book, int pages, String beg, String fin) {
        if(beg.equals("")){
            beg = " ";
        }
        if(fin.equals("")){
            fin = " ";
        }
        shelf.addBook(new Book(book, pages, beg, fin));
    }
    
    public void deleteBook(int index) {
        shelf.getBooks().remove(index);
        refreshPanels();
    }
    
    
    public void logDatesMinsPages(String date, int mins, int pages){
        if(date.equals("")){
            date = " ";
        }
        log.logDates(date);
        log.logMins(mins);
        log.logPages(pages);
        refreshPanels();
    }
    
    public void addQuote(String book, String quote){
        if(book.equals("")){
            book = " ";
        }
        if(quote.equals("")){
            quote = " ";
        }
        quotes.addQuote(quote);
        quotes.addQuoteTitle(book);
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField BookGoalText;
    private javax.swing.JTextField PagesGoalText;
    private javax.swing.JTextField TimeGoalText;
    private javax.swing.JButton addBook;
    private javax.swing.JButton addQuote;
    private javax.swing.JProgressBar booksProgressBar;
    private javax.swing.JPanel bookshelfPanel;
    private javax.swing.JTable bookshelfTable;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton logPages;
    private javax.swing.JProgressBar pagesProgressBar;
    private javax.swing.JTable pagesTimeTable;
    private javax.swing.JTable quotesTable;
    private javax.swing.JButton sortT;
    private javax.swing.JButton sortT1;
    private javax.swing.JProgressBar timeProgressBar;
    private javax.swing.JLabel totalBooksLabel;
    private javax.swing.JLabel totalPagesLabel;
    private javax.swing.JLabel totalTimeLabel;
    // End of variables declaration//GEN-END:variables
}
