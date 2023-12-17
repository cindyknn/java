package Backend;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;

/**
 *
 * @author student
 */
public class Bookshelf {

    private ArrayList<Book> books;
    private int booksGoal;

    public Bookshelf() {
        books = new ArrayList<Book>();
        booksGoal = 10;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public int getNumBooks() {
        return books.size();
    }

    public int getBooksGoal() {
        return booksGoal;
    }

    public void addBook(Book newBook) {
        books.add(newBook);
    }

    public void setBooksGoal(int b) {
        booksGoal = b;
    }

}
