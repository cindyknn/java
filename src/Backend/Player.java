package Backend;

import java.util.ArrayList;

public class Player {

    // Copy over your Player class here
    private ArrayList<Ship> ships;
    private ArrayList<Ship> liveShips;
    private Grid my;
    private Grid guess;
    private int shipsPlaced;
    private int totalHitsTaken;
    private int totalHitsDelivered;

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int UNGUESSED = 0;
    public static final int HIT = 1;
    public static final int MISSED = 2;
    public static final int[] SHIP_LENGTHS = {2, 3, 3, 4, 5};
    private static final int MAX_HITS = 17;

    public Player() {
        ships = new ArrayList<Ship>();
        liveShips = new ArrayList<Ship>();
        for (int i = 0; i < 5; i++) {
            ships.add(new Ship(SHIP_LENGTHS[i]));
            liveShips.add(new Ship(SHIP_LENGTHS[i]));
        }
        shipsPlaced = 0;
        my = new Grid();
        guess = new Grid();
    }

    public Ship getLiveShips(int i) {
        return liveShips.get(i);
    }

    public Ship getShip(int i) {
        return ships.get(i);
    }

    public int getShipLength() {
        return (ships.get(shipsPlaced)).getLength();
    }

    // Print your ships on the grid
    public void printMyShips() {
        my.printShips();
    }

    // Print opponent guesses
    public void printOpponentGuesses() {
        my.printStatus();
    }

    // Print your guesses
    public void printMyGuesses() {
        guess.printStatus();
    }

    public int getRandomColGuess() {
        return Randomizer.nextInt(0, 9);
    }

    public int getRandomRowGuess() {
        return Randomizer.nextInt(0, 9);
    }

    public int getRandomDir() {
        return Randomizer.nextInt(0, 1);
    }

    // Record a guess from the opponent
    public boolean recordOpponentGuess(int row, int col) {
        Location oppGuess = my.get(row, col);
        if (oppGuess.hasShip()) {
            oppGuess.markHit();
            totalHitsDelivered++;
            return true;
        } else {
            oppGuess.markMiss();
            return false;
        }
    }
    
    public int match(int row, int col, Player other){
        for (int i = 0; i < ships.size(); i++) {
            Ship cur = other.getShip(i);       
            int len = cur.getLength();
            int r = cur.getRow();
            int c = cur.getCol();
            int dir = cur.getDirection();
            for (int l = 0; l < len; l++) {
                if (dir == HORIZONTAL) {
                
                    int ro = r;
                    int co = c + l;
                    //System.out.println("("+ro +", "+co+")" + "("+row +", "+col+")" );
                    if(ro == row && co == col){
                        return i;
                    }
                }
                if (dir == VERTICAL) {
                
                    int ro = r + l;
                    int co = c;
                    //System.out.println("("+ro +", "+co+")" + "("+row +", "+col+")" );
                    if(ro == row && co == col){
                        return i;
                    }
                }
            }
    }
        return 0;
    }
    

    public int hasSunk(int row, int col, Player other){
        Ship sink = other.getShip(match(row, col, other));
        int len = sink.getLength();
        //System.out.println("len: "+len);
        int r = sink.getRow();
        int c = sink.getCol();
        int dir = sink.getDirection();
        //boolean hit = true;
        int curHits = 0;
        for (int l = 0; l < len; l++) {
            if (dir == HORIZONTAL) {
            
                int status = (guess.get(r,c+l)).getStatus();
                System.out.println(status);
                if(status == HIT){
                    //hit = false;
                    curHits++;
                    
                }
            }
        
            if (dir == VERTICAL) {
                int status = (guess.get(r+l,c)).getStatus();
                System.out.println(status);

                if(status == HIT){
                    //hit = false;
                    curHits++;
                    
                }
            }
        }
        
        System.out.println("curHits: "+curHits);
        if(curHits == len){
            return len;
        }
        return 0;
        /*
        for (int i = 0; i < liveShips.size(); i++) {
            int currentHits = 0;
            Ship cur = liveShips.get(i);
            int len = cur.getLength();
            int row = cur.getRow();
            int col = cur.getCol();
            int dir = cur.getDirection();
            System.out.println("len: " + len);
            System.out.println("dir: " + dir);
            boolean hit = false;
            if (dir == HORIZONTAL) {
                for (int l = 0; l < len; l++) {
                    hit = (guess.getStatus(row, col + l) == HIT);
                    System.out.println("(" + row + ", " + (col + l) + ")");
                    if (hit) {
                        currentHits++;
                    }
                }
            }
            if (dir == VERTICAL) {
                for (int l = 0; l < len; l++) {
                    System.out.println("(" + (row+l) + ", " + (col)+")");
                    hit = (guess.getStatus(row + l, col) == HIT);
                    if (hit) {
                        currentHits++;
                        System.out.println("currentHits: " + currentHits );
                    }
                }
            }
            
            if(currentHits == len){
                return (liveShips.remove(i)).getLength();                
            }
        }
        return 0;
        */
    }

    public void initializeShipsRandomly() {
        for (int i = 0; i < 5; i++) {
            int dir = getRandomDir();
            int col = getRandomColGuess();
            int row = getRandomRowGuess();
            Ship s = getShip(i);
            s.setLocation(row, col);
            s.setDirection(dir);
            while (addShip(s) == false) {
                dir = getRandomDir();
                col = getRandomColGuess();
                row = getRandomRowGuess();
                s.setLocation(row, col);
                s.setDirection(dir);
            }
            chooseShipLocation(s, row, col, dir);
            ships.set(i, s);
            liveShips.set(i, s);
        }
    }

    public boolean addShip(Ship s) {
        if (my.canPlaceShip(s)) {
            shipsPlaced++;
            return true;
        } else {
            return false;
        }
    }

    public boolean addShip(int row, int col, int dir) {
        Ship s = new Ship(SHIP_LENGTHS[shipsPlaced]);
        s.setDirection(dir);
        s.setLocation(row, col);
        return addShip(s);
    }

    public void chooseShipLocation(Ship s, int row, int col, int direction) {
        if (shipsPlaced < 5) {
            s.setLocation(row, col);
            s.setDirection(direction);
            //System.out.println(s);
            shipsPlaced++;
            my.addShip(s);
        }
    }

    public boolean hasWon() {
        return totalHitsDelivered == MAX_HITS;

    }

    public boolean makeGuess(int row, int col, Player other) {
        if (guess.alreadyGuessed(row, col)) {
            return false;
        }
        boolean val = guess.hasShip(row, col);
        markGuess(row, col, val);
        return val;
        //false if already guessed the location, use mark guess return if hit
    }

    public void markGuess(int row, int col, boolean val) {
        if (val) {
            guess.markHit(row, col);
            totalHitsDelivered++;
            
        } else {
            guess.markMiss(row, col);
            
        }
    }

    public Grid getGrid() {
        return my;
    }

    public int getNumShipsAdded() {
        return shipsPlaced;
    }

    public boolean alreadyGuessed(int row, int col) {
        return guess.alreadyGuessed(row, col);
    }

    public void setOpponentGrid(Grid grid) {
        guess = grid;
    }
}
