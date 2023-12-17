package Backend;

public class Grid
{
    // Copy over your Grid class here
    private Location[][] grid;

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    public static final int NUM_ROWS = 10;
    public static final int NUM_COLS = 10;
    
    public Grid(){
        grid = new Location[NUM_ROWS][NUM_COLS];
        for(int i=0; i<grid.length; i++){
            for(int j=0; j<grid[0].length; j++){
                grid[i][j] = new Location();
            }
        }
    }
    
    // Mark a hit in this location by calling the markHit method
    // on the Location object.  
    public void markHit(int row, int col){
        grid[row][col].markHit();
    }

    // Mark a miss on this location.    
    public void markMiss(int row, int col){
        grid[row][col].markMiss();
    }
    
    // Set the status of this location object.
    public void setStatus(int row, int col, int status){
        grid[row][col].setStatus(status);
    }
    
    // Get the status of this location in the grid  
    public int getStatus(int row, int col){
        return grid[row][col].getStatus();
    }
    
    // Return whether or not this Location has already been guessed.
    public boolean alreadyGuessed(int row, int col){
        return !(grid[row][col].isUnguessed());
    } 
    
    // Set whether or not there is a ship at this location to the val   
    public void setShip(int row, int col, boolean val){
        grid[row][col].setShip(val);
    }
    
    // Return whether or not there is a ship here   
    public boolean hasShip(int row, int col){
        return grid[row][col].hasShip();
    }
    
    
    // Get the Location object at this row and column position
    public Location get(int row, int col){
        return grid[row][col];
    }
    
    // Return the number of rows in the Grid
    public int numRows(){
        return grid.length;
    }
    
    // Return the number of columns in the grid
    public int numCols(){
        return grid[0].length;
    }
    
    //public boolean inBounds(int row, int col)
    
    public boolean canPlaceShip(Ship s){
        int row = s.getRow();
        int col = s.getCol();
        int length = s.getLength();
        if(s.getDirection() == HORIZONTAL){
            if(col+length>10){
                return false;
            }
            for(int i = 0; i<length; i++){
                if(hasShip(row,col+i)){
                    return false;
                }
            }
        }
        if(s.getDirection() == VERTICAL){
            if(row+length>10){
                return false;
            }
            for(int i = 0; i<length; i++){
                if(hasShip(row+i,col)){
                    return false;
                }
            }
        }
        addShip(s);
        return true;
    }
    
    public void addShip(Ship s){
        int row = s.getRow();
        int col = s.getCol();
        if(s.getDirection()==HORIZONTAL){
            for(int i=0; i<s.getLength(); i++){
                setShip(row,col+i,true);
            }
        }
        if(s.getDirection()==VERTICAL){
            for(int i=0; i<s.getLength(); i++){
                setShip(row+i,col,true);
            }
        }
    }
    
    public void printStatus(){
        String header = "  ";
        for(int i=1; i<=grid[0].length; i++){
            if(i==grid[0].length){
                header+=""+i;
            }else{
                header += ""+i+" ";
            }
        }
        System.out.println(header);
        for(int i=65; i<75; i++){
            String row = ""+ (char)i+" ";
            for(int j=0; j<grid[0].length; j++){
                if(grid[i-65][j].getStatus()==0){
                    row += "- ";
                } else if(grid[i-65][j].getStatus()==1){
                    row += "X ";
                } else{
                    row += "O ";
                }
            }
            System.out.println(row);
        }
    }
    
    
    public void printShips(){
        String header = "  ";
        for(int i=1; i<=10; i++){
            if(i==grid[0].length){
                header+=""+i;
            } else{
                header += ""+i+" ";
            }
        }
        System.out.println(header);
        for(int i=65; i<75; i++){
            String row = ""+ (char)i+" ";
            for(int j=0; j<grid[0].length; j++){
                if(grid[i-65][j].hasShip()){
                    row += "X ";
                } else{
                    row += "- ";
                }
            }
            System.out.println(row);
        }
    }

    public String[][] getGridStatus() {
        String[][] statusGrid = new String[10][11];
        for(int r=0; r<10; r++){
            statusGrid[r][0] = ""+ (char)(r+65);
            for(int c=0; c<grid[0].length; c++){
                if(grid[r][c].getStatus()==0){
                    statusGrid[r][c+1] = "-";
                } else if(grid[r][c].getStatus()==1){
                    statusGrid[r][c+1] = "X";
                } else{
                    statusGrid[r][c+1] = "O";
                }
            }
        }
        return statusGrid;
    }
    
    public String[][] getGridShips() {
        String[][] shipsGrid = new String[10][11];
        for(int r=0; r<10; r++){
            shipsGrid[r][0] = ""+ (char)(r+65);
            for(int c=0; c<grid[0].length; c++){
                if(grid[r][c].hasShip()){
                    shipsGrid[r][c+1] = "X";
                } else{
                    shipsGrid[r][c+1] = "-";
                }
            }
        }
        return shipsGrid;
    }
}