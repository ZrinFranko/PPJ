//package hr.fer.ppj.lab2;

import java.io.*;

public class SintaksniAnalizator {

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
		SyntaxAnayzer sA = new SyntaxAnayzer(sb.toString());
		System.out.println(sA.getOutput());

	}

}
