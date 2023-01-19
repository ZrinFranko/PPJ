//package hr.fer.ppj.lab2;

public class LexerToken {
	
	private final String value;
	private final LexerTokenType type;
	private final int row;
	
	public LexerToken(String value, LexerTokenType type,int row) {
		this.value = value;
		this.type = type;
		this.row = row;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public LexerTokenType getLexerTokenType() {
		return this.type;
	}
	
	public int getRow() {
		return this.row;
	}
	
	public String toString() {
		return getLexerTokenType().name() + " " + getRow() + " " + getValue();
	}

}
