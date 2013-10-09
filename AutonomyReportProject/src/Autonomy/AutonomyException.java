package Autonomy;


public class AutonomyException extends Exception {
	private String message;

	public AutonomyException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {return message;}
	public void setMessage(String message) {this.message = message;}

	
}
