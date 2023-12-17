package Backend;

public class Ship
{
    // Copy over your Ship class here
    public static final int UNSET = -1;
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    
    private int row;
    private int column;
    private int length;
    private int direction;
    
    public Ship(int theLength){
        length = theLength;
        row = UNSET;
        column = UNSET;
        direction = UNSET;
    }
    
    public boolean isLocationSet(){
        if(row >= 0 && column >= 0){
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isDirectionSet(){
        if(direction > UNSET) {
            return true;
        } else {
            return false;
        }
    }
    
    public void setLocation(int theRow, int col){
        row = theRow;
        column = col;
    }
    
    public void setDirection(int direct){
        direction = direct;
    }
    
    public int getRow(){
        return row;
    }
    
    public int getCol(){
        return column;
    }
    
    public int getLength(){
        return length;
    }
    
    public int getDirection(){
        return direction;
    }
    
    private String directionToString(){
        if(direction == HORIZONTAL){
            return "horizontal";
        }
        else if(direction == VERTICAL){
            return "vertical";
        }
        else{
            return "unset direction";
        }
    }
    
    private String locationToString(){
        if(row==UNSET && column==UNSET){
            return "(unset location)";
        }
        return "("+row+", "+column+")";
    }
    
    public String toString(){
        return directionToString()+" ship of length "+getLength()+" at "+locationToString();
    }
}