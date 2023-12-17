package Backend;

public class Location
{
    // Copy over your Location class here
    public static final int UNGUESSED = 0;
    public static final int HIT = 1;
    public static final int MISSED = 2;
    
    private boolean ship;
    private int status;
    
    public Location(){
        status = UNGUESSED;
        ship = false;
    }
    
    public boolean checkMiss(){
        return status==MISSED;
    }
    
    public boolean checkHit(){
        if(ship && status == UNGUESSED){
            status = HIT;
            return true;
        } else{
            status = MISSED;
            return false;
        }
    }
    
    public boolean isUnguessed(){
        return status==UNGUESSED;
    }
    
    public void markHit(){
        status = HIT;
    }
    
    public void markMiss(){
        status = MISSED;
    }
    
    public boolean hasShip(){
        return ship;
    }
    
    public void setShip(boolean val){
        ship = val;
    }
    
    public void setStatus(int theStatus){
        status = theStatus;
    }
    
    public int getStatus(){
        return status;
    }
}