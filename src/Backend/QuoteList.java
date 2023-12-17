/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Backend;

import java.util.ArrayList;

/**
 *
 * @author student
 */
public class QuoteList {

    private ArrayList<String> quotes;
    private ArrayList<String> titleQuote;

    public QuoteList() {

        quotes = new ArrayList<String>();
        titleQuote = new ArrayList<String>();

    }

    public ArrayList<String> getQuotes() {
        return quotes;
    }

    public int getTotQuotes() {
        return quotes.size();
    }

    public ArrayList<String> getTitleQuote() {
        return titleQuote;
    }

    public void addQuote(String quote) {
        quotes.add(quote);
    }

    public void addQuoteTitle(String title) {
        titleQuote.add(title);
    }

}
