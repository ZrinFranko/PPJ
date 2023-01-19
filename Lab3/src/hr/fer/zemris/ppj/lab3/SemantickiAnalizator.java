package hr.fer.zemris.ppj.lab3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class SemantickiAnalizator {

	
	public static void main(String[] args) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		String currentLine = null;		
		try {
			while((currentLine = reader.readLine()) != null) {
				sb.append(currentLine).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sb.deleteCharAt(sb.length()-1);
		SemanticAnalyzer sA = new SemanticAnalyzer(sb.toString());
		System.out.println(sA.generateOutput().trim());
	}
}
