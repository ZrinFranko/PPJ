//package hr.fer.ppj.lab1;
public class LexerToken {
	
	private final String value;
	private final TokenType type;
	private final int row;
	
	public LexerToken(String value, TokenType type,int row) {
		this.value = value;
		this.type = type;
		this.row = row;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public TokenType getTokenType() {
		return this.type;
	}
	
	public int getRow() {
		return this.row;
	}
	
	public String toString() {
		return getTokenType().name() + " " + getRow() + " " + getValue();
	}

}
