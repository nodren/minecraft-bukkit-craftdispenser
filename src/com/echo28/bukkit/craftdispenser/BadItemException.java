package com.echo28.bukkit.craftdispenser;

public class BadItemException extends Exception {
	private static final long serialVersionUID = -8554878845310676532L;

	public BadItemException() {
		super();
	}

	public BadItemException(String message) {
		super(message);
	}

	public BadItemException(Throwable cause) {
		super(cause);
	}

	public BadItemException(String message, Throwable cause) {
		super(message, cause);
	}

}
