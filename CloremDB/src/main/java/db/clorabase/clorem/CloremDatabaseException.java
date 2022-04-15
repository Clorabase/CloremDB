package db.clorabase.clorem;

public class CloremDatabaseException extends RuntimeException {
    Reasons reason;

    public CloremDatabaseException(String message,Reasons reason){
        super(message);
        this.reason = reason;
    }


    public Reasons getReason() {
        return reason;
    }
}
