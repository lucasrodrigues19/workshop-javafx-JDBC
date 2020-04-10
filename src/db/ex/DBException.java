package db.ex;

public class DBException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7601041630558064857L;

	public DBException(String msg) {
		super(msg);
	}
}
