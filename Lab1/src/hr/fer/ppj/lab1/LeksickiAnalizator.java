//package hr.fer.ppj.lab1;
import java.io.*;
import java.util.stream.Collectors;

public class LeksickiAnalizator {

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
		LexicAnalyzer lA = new LexicAnalyzer(sb.toString());
		System.out.println(lA.generateOutput());

	}

}
