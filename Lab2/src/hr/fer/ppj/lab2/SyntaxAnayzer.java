//package hr.fer.ppj.lab2;

import java.util.*;

public class SyntaxAnayzer {

	private Deque<LexerToken> inputTokens;
	private StringBuilder syntaxTree;

	public SyntaxAnayzer(String inputText) {
		if(inputText == null) throw new NullPointerException("Input cannot be null");	
		inputTokens = getTokens(inputText.split("\n"));
		syntaxTree = new StringBuilder();
		try {
			syntaxStart();
		}catch(SyntaxAnalyzerException sae) {
			syntaxTree = new StringBuilder();
			if(inputTokens.isEmpty())
				syntaxTree.append("err kraj");
			else
				syntaxTree.append("err ").append(inputTokens.peek().toString());
		}
	}

	private void syntaxStart() {
		syntaxTree.append("<program>").append("\n");
		if(inputTokens.isEmpty()) {
			listaNaredbi(1);
		}
		if(checkNextToken(new LexerTokenType[]{LexerTokenType.IDN,LexerTokenType.KR_ZA})) {
			listaNaredbi(1);
			return;
		}
		throw new SyntaxAnalyzerException("Invalid starting lexer token!");

	}

	private void listaNaredbi(int currentDepth) {
		addOutputRows(currentDepth,"<lista_naredbi>");
		if(inputTokens.isEmpty() || checkNextToken(new LexerTokenType[]{LexerTokenType.KR_AZ})){
			addOutputRows(currentDepth+1,"$");
			return;
		}
		if(checkNextToken(new LexerTokenType[]{LexerTokenType.IDN,LexerTokenType.KR_ZA})) {
			naredba(currentDepth+1);
			listaNaredbi(currentDepth+1);
			return;
		}

		throw new SyntaxAnalyzerException();
	}

	private void naredba(int depth) {
		addOutputRows(depth,"<naredba>");
		if(!(inputTokens.isEmpty())) {
			if(checkNextToken(new LexerTokenType[]{LexerTokenType.IDN})) { 
				naredbaPridruzivanja(depth+1);
				return;
			}
			if(checkNextToken(new LexerTokenType[]{LexerTokenType.KR_ZA})) {
				zaPetlja(depth+1);
				return;
			}
		}
		throw new SyntaxAnalyzerException();

	}

	private void naredbaPridruzivanja(int depth) {
		addOutputRows(depth,"<naredba_pridruzivanja>");
		if(inputTokens.isEmpty())
			throw new SyntaxAnalyzerException();
		if(checkNextToken(new LexerTokenType[]{LexerTokenType.IDN})) {
			addOutputRows(depth+1, inputTokens.remove().toString());
			if(!checkNextToken(new LexerTokenType[]{LexerTokenType.OP_PRIDRUZI}))
				throw new SyntaxAnalyzerException();
			addOutputRows(depth+1, inputTokens.remove().toString());
			E(depth+1);
			return;
		}
		throw new SyntaxAnalyzerException();

	}

	private void zaPetlja(int depth) {
		addOutputRows(depth,"<za_petlja>");
		
		if(checkNextToken(new LexerTokenType[]{LexerTokenType.KR_ZA})) {
			addOutputRows(depth+1, inputTokens.remove().toString());
			if(!checkNextToken(new LexerTokenType[]{LexerTokenType.IDN}))
				throw new SyntaxAnalyzerException();
			addOutputRows(depth+1, inputTokens.remove().toString());
			if(!checkNextToken(new LexerTokenType[]{LexerTokenType.KR_OD}))
				throw new SyntaxAnalyzerException();
			addOutputRows(depth+1, inputTokens.remove().toString());
			E(depth+1);
			if(!checkNextToken(new LexerTokenType[]{LexerTokenType.KR_DO}))
				throw new SyntaxAnalyzerException();
			addOutputRows(depth+1, inputTokens.remove().toString());
			E(depth+1);
			listaNaredbi(depth+1);
			if(!checkNextToken(new LexerTokenType[]{LexerTokenType.KR_AZ}))
				throw new SyntaxAnalyzerException();
			addOutputRows(depth+1, inputTokens.remove().toString());
			return;
		}
		throw new SyntaxAnalyzerException();
	}

	private void E(int depth) {
		addOutputRows(depth,"<E>");
		if(checkNextToken(new LexerTokenType[]{LexerTokenType.IDN,LexerTokenType.BROJ,LexerTokenType.OP_PLUS,LexerTokenType.OP_MINUS,LexerTokenType.L_ZAGRADA})) {
			T(depth+1);
			eLista(depth+1);
			return;
		}
		throw new SyntaxAnalyzerException();

	}

	private void eLista(int depth) {
		addOutputRows(depth,"<E_lista>");
		if(checkNextToken(new LexerTokenType[]{LexerTokenType.OP_PLUS})) {
			addOutputRows(depth+1, inputTokens.remove().toString());
			E(depth+1);
			return;
		}
		if(checkNextToken(new LexerTokenType[]{LexerTokenType.OP_MINUS})) {
			addOutputRows(depth+1, inputTokens.remove().toString());
			E(depth+1);
			return;
		}
		if(inputTokens.isEmpty() || checkNextToken(new LexerTokenType[]{LexerTokenType.IDN,LexerTokenType.KR_ZA,LexerTokenType.KR_DO,LexerTokenType.KR_AZ,LexerTokenType.D_ZAGRADA})) {
			addOutputRows(depth+1, "$");
			return;
		}
		throw new SyntaxAnalyzerException();
	}

	private void T(int depth) {
		addOutputRows(depth,"<T>");
		if(checkNextToken(new LexerTokenType[]{LexerTokenType.IDN,LexerTokenType.BROJ,LexerTokenType.OP_PLUS,LexerTokenType.OP_MINUS,LexerTokenType.L_ZAGRADA})) {
			P(depth+1);
			tLista(depth+1);
			return;
		}
		throw new SyntaxAnalyzerException();

	}

	private void tLista(int depth) {
		addOutputRows(depth,"<T_lista>");
		if(checkNextToken(new LexerTokenType[]{LexerTokenType.OP_PUTA})) {
			addOutputRows(depth+1, inputTokens.remove().toString());
			T(depth+1);
			return;
		}
		if(checkNextToken(new LexerTokenType[]{LexerTokenType.OP_DIJELI})) {
			addOutputRows(depth+1, inputTokens.remove().toString());
			T(depth+1);
			return;
		}
		if(inputTokens.isEmpty() || checkNextToken(new LexerTokenType[]{LexerTokenType.IDN,LexerTokenType.KR_ZA,LexerTokenType.KR_DO,LexerTokenType.KR_AZ,LexerTokenType.OP_PLUS,LexerTokenType.OP_MINUS,LexerTokenType.D_ZAGRADA})) {
			addOutputRows(depth+1, "$");
			return;
		}
		throw new SyntaxAnalyzerException();

	}

	private void P(int depth) {
		addOutputRows(depth,"<P>");
		if(checkNextToken(new LexerTokenType[]{LexerTokenType.IDN})) {
			addOutputRows(depth+1, inputTokens.remove().toString());
			return;
		}
		if(checkNextToken(new LexerTokenType[]{LexerTokenType.BROJ})) {
			addOutputRows(depth+1, inputTokens.remove().toString());
			return;
		}
		if(checkNextToken(new LexerTokenType[]{LexerTokenType.OP_PLUS})) {
			addOutputRows(depth+1, inputTokens.remove().toString());
			return;
		}
		if(checkNextToken(new LexerTokenType[]{LexerTokenType.OP_MINUS})) {
			addOutputRows(depth+1, inputTokens.remove().toString());
			return;
		}
		if(checkNextToken(new LexerTokenType[]{LexerTokenType.L_ZAGRADA})) {
			addOutputRows(depth+1, inputTokens.remove().toString());
			E(depth+1);
			if(!checkNextToken(new LexerTokenType[]{LexerTokenType.D_ZAGRADA}))
				throw new SyntaxAnalyzerException();
			addOutputRows(depth+1, inputTokens.remove().toString());		
			return;
		}
		throw new SyntaxAnalyzerException();

	}

	private void addOutputRows(int currentDepth, String string) {
		for(int i = 0; i < currentDepth; i++)
			syntaxTree.append(" ");
		syntaxTree.append(string).append("\n");
	}

	public String getOutput() {
		return syntaxTree.toString();
	}

	private Deque<LexerToken> getTokens(String[] lines) {
		Deque<LexerToken> temp = new LinkedList<>();
		for(String line : lines) {
			String[] elements = line.split(" ");
			if(elements.length != 3) throw new SyntaxAnalyzerException("Wrong definition of a lexer token!");
			if(findType(elements[0]) == null) throw new SyntaxAnalyzerException("Invalid definition of a lexer token!");
			temp.add(new LexerToken(elements[2],findType(elements[0]),Integer.parseInt(elements[1])));
		}
		return temp;
	}

	private LexerTokenType findType(String possibleType) {
		LexerTokenType[] types = LexerTokenType.values();
		for(LexerTokenType type : types) {
			if(possibleType.equalsIgnoreCase(type.name())) return type;
		}
		throw new SyntaxAnalyzerException("Selected token type is not defined in the grammar structure: " + possibleType);
	}
	
	private boolean checkNextToken(LexerTokenType[] types) {
		if(inputTokens.isEmpty()) return false;
		for(LexerTokenType type : types) {
			if(type.equals(inputTokens.peek().getLexerTokenType())) return true;
		}	
		return false;
	}

}
