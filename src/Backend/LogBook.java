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
public class LogBook {

    private ArrayList<String> logDates;
    private ArrayList<Integer> logPages;
    private ArrayList<Integer> logMins;

    private int pagesGoal;
    private int timeGoal;

    public LogBook() {
        logDates = new ArrayList<String>();
        logPages = new ArrayList<Integer>();
        logMins = new ArrayList<Integer>();

        pagesGoal = 10;
        timeGoal = 10;
    }

    public ArrayList<String> getDates() {
        return logDates;
    }

    public ArrayList<Integer> getPages() {
        return logPages;
    }

    public ArrayList<Integer> getMins() {
        return logMins;
    }

    public int getPagesGoal() {
        return pagesGoal;
    }

    public int getTimeGoal() {
        return timeGoal;
    }

    public int getTotPages() {
        int totPages = 0;
        for (int i = 0; i < logPages.size(); i++) {
            totPages += logPages.get(i);
        }
        return totPages;
    }

    public int getTotMins() {
        int totMins = 0;
        for (int i = 0; i < logMins.size(); i++) {
            totMins += logMins.get(i);
        }
        return totMins;
    }

    public void logDates(String date) {
        logDates.add(date);
    }

    public void logMins(int time) {
        logMins.add(time);
    }

    public void logPages(int pages) {
        logPages.add(pages);
    }

    public void setPagesGoal(int p) {
        pagesGoal = p;
    }

    public void setTimeGoal(int t) {
        timeGoal = t;
    }

}
