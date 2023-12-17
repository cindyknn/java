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
public class Book {
    
    public static final int PROGRESS = 0;
    public static final int COMPLETE = 1;
    
    private String title;
    private int numPages;
    private String beg;
    private String fin;
    private int pagesRead;
    private int status;
    
    /**
     *
     * @param name
     * @param begin
     * @param pages
     * @param finish
     */
    public Book(String name, int pages, String begin, String finish){
        //this(title, pages, beg);
        title = name;
        numPages = pages;
        beg = begin;
        fin = finish;
        pagesRead = 0;
        status = PROGRESS;
    }
    
    public void setBookTitle(String book) {
        title = book;
    }

    public void setNumPages(int pages) {
        numPages = pages;
    }

    public void setDateBeg(String begin) {
        beg = begin;
    }

    public void setDateFin(String finish) {
        fin = finish;
    }
    
    public void setBookFin(){
        if(pagesRead == numPages){
            status = COMPLETE;
        }
    }
    
    public void addPagesRead(int add){
        pagesRead += add;
    }
    
    
    public String getBookTitle() {
        return title;
    }

    public int getNumPages() {
        return numPages;
    }

    public String getDateBeg() {
        return beg;
    }

    public String getDateFin() {
        return fin;
    }
    
    public int getPagesRead(){
        return pagesRead;
    }
    
    public int getStatus(){
        return status;
    }
    
    
    
}
