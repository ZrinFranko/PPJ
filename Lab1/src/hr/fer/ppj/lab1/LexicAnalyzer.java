//package hr.fer.ppj.lab1;

import java.util.*;

public class LexicAnalyzer {

	Collection<LexerToken> tokens;	
	private char[] data;
	private int currentIndex;
	private int rowCount;

	public LexicAnalyzer(String data) {
		if(data == null) throw new NullPointerException("Input cannot be null");
		this.data = data.toCharArray();
		this.currentIndex = 0;
		this.rowCount = 1;
		this.tokens = new ArrayList<LexerToken>();
		tokenizeString();
	}
	
	public String generateOutput() {
		StringBuilder sb = new StringBuilder();
		for(LexerToken token : tokens) {
			sb.append(token.toString()).append("\n");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	private void tokenizeString() {
		while(true) {
			LexerToken temp = nextToken();
			if(temp == null) return; 
			tokens.add(temp);
		}
	}
	
	private LexerToken nextToken() {
		String tokenValue = null;
		
		while(currentIndex < data.length && Character.isWhitespace(data[currentIndex])) {
			if(data[currentIndex] == '\n') rowCount++;
			currentIndex++;
		}
		if(currentIndex >= data.length) return null;
		
		if(data[currentIndex] == '/' && data[currentIndex+1] == '/') {
			while(data[currentIndex] != '\n') currentIndex++;
			currentIndex++;
			rowCount++;
		}
		
		if(Character.isLetter(data[currentIndex])) {
			tokenValue = findTheWord();
			if(isKeyWord(tokenValue)) {
				return new LexerToken(tokenValue,whichKeyWord(tokenValue),rowCount);
			}
			return new LexerToken(tokenValue,TokenType.IDN,rowCount);
		}else if(Character.isDigit(data[currentIndex])) {
			tokenValue = findTheNumber();
			return new LexerToken(tokenValue,TokenType.BROJ,rowCount);
		}else {
			if(isAritSymbol(data[currentIndex])) {
				tokenValue = String.valueOf(data[currentIndex]);
				currentIndex++;
				return new LexerToken(tokenValue,symbolType(data[currentIndex-1]),rowCount);
			}
			return null;
		}
	}
	
	private String findTheNumber() {
		StringBuilder sb = new StringBuilder();
		
		while(data.length > currentIndex && !Character.isWhitespace(data[currentIndex])) {
			if(Character.isDigit(data[currentIndex])) sb.append(data[currentIndex++]);
			else break;
		}
		return sb.toString();
	}

	private String findTheWord() {
		StringBuilder sb = new StringBuilder();
		int i = 1;
		sb.append(data[currentIndex]);
		
		while(data.length > (currentIndex+i) && !Character.isWhitespace(data[currentIndex + i])) {
			if(Character.isLetterOrDigit(data[currentIndex+i])) {
				sb.append(data[currentIndex+i]);
				i++;
			}else break;
		}
		currentIndex += i;
		return sb.toString();
	}

	private boolean isKeyWord(String word) {
		if(word.length() != 2) return false;
		if(word.equals("za")) return true;
		if(word.equals("az")) return true;
		if(word.equals("od")) return true;
		if(word.equals("do")) return true;
		return false;
	}
	
	private TokenType whichKeyWord(String word) {
		if(word.equals("za")) return TokenType.KR_ZA;
		if(word.equals("az")) return TokenType.KR_AZ;
		if(word.equals("od")) return TokenType.KR_OD;
		if(word.equals("do")) return TokenType.KR_DO;
		return null;
		
	}
	
	private boolean isAritSymbol(char c) {
		if(c == '/') return true;
		if(c == '(') return true;
		if(c == ')') return true;
		if(c == '+') return true;
		if(c == '-') return true;
		if(c == '*') return true;
		if(c == '=') return true;
		return false;
	}
	
	private TokenType symbolType(char c) {
		if(c == '/') return TokenType.OP_DIJELI;
		if(c == '(') return TokenType.L_ZAGRADA;
		if(c == ')') return TokenType.D_ZAGRADA;
		if(c == '+') return TokenType.OP_PLUS;
		if(c == '-') return TokenType.OP_MINUS;
		if(c == '*') return TokenType.OP_PUTA;
		return TokenType.OP_PRIDRUZI;
	}
}
