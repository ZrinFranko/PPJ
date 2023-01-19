package hr.fer.zemris.ppj.lab3;

import java.util.*;

public class SemanticAnalyzer {

	private static String[] inputLines;
	private List<Variable> variables;
	private int currentIndex;
	private StringBuilder output;
	private boolean errorFound;
	
	//Testiranje jednog problema
	private int currentDepth;

	public SemanticAnalyzer(String input) {
		if(input == null) throw new NullPointerException("Input must not be null");
		this.inputLines = input.split("\n");
		variables = new ArrayList<Variable>();
		currentIndex = 0;
		output = new StringBuilder();
		errorFound = false;
		
		currentDepth = 0;
		
		findVariableUsability();
	}


	private void findVariableUsability() {
		
		while(currentIndex < inputLines.length) {
			String line = inputLines[currentIndex].trim();

			if(line.equalsIgnoreCase("<za_petlja>")) {
				currentIndex++;
				zaPetlja();
			}
			if(line.equalsIgnoreCase("<naredba_pridruzivanja>")) {
				currentIndex++;
				varInit();
			}
			
			if(errorFound)
				return;
			else
				currentIndex++;

		}
	}
	
	private void varInit() {
		boolean foundEqSign = false;
		
		while(currentIndex < inputLines.length) {
			String line = inputLines[currentIndex].trim();
			
			if(line.equalsIgnoreCase("<lista_naredbi>"))
				break;
			else {
				if(line.contains("IDN")) {
					String[] lineEl = line.split(" ");
					int index = checkExists(lineEl[2]);
					//desna strana
					if(foundEqSign) {						
						if(index != -1) {
							if(variables.get(index).getInitialization() == Integer.parseInt(lineEl[1])) {
								output.append("err " + lineEl[1] + " " + lineEl[2]);
								errorFound = true;
								break;
							}
							output.append(lineEl[1] + " " + variables.get(index).getInitialization() + " " + lineEl[2]).append("\n");
						}else {
							output.append("err " + lineEl[1] + " " + lineEl[2]);
							errorFound = true;
							break;
						}
					}
					//lijeva strana
					else {
						if(index == -1) {
							variables.add(new Variable(lineEl[2],Integer.parseInt(lineEl[1]),currentDepth));
						}				
					}					
				}else if(line.contains("OP_PRIDRUZI"))
					foundEqSign = true;
			}	
			currentIndex++;
		}
	}
	
	private void zaPetlja() {
		boolean initializationComplete = false;
		boolean foundIteree = false;
		currentDepth++;
		
		while(currentIndex < inputLines.length) {
			String line = inputLines[currentIndex].trim();
			if(line.contains("KR_AZ")) {
				variables.removeIf(v -> v.getDepth() == currentDepth);
				break;
			}else {
				if(line.equalsIgnoreCase("<lista_naredbi>")) {
					initializationComplete = true;
				}
				if(line.contains("IDN")) {
					String[] lineEl = line.split(" ");
					int index = checkExists(lineEl[2]);
					
					if(initializationComplete) {
						if(index != -1) {
							output.append(lineEl[1] + " " + variables.get(index).getInitialization() + " " + lineEl[2]).append("\n");
						}else {
							variables.add(new Variable(lineEl[2],Integer.parseInt(lineEl[1]),currentDepth));
						}	
					}else {
						if(foundIteree) {
							if(index == -1) {
								output.append("err " + lineEl[1] + " " + lineEl[2]);
								errorFound = true;
								break;
							}else {
								if(variables.get(index).getInitialization() == Integer.parseInt(lineEl[1])) {
									output.append("err " + lineEl[1] + " " + lineEl[2]);
									errorFound = true;
									break;
								}
								output.append(lineEl[1] + " " + variables.get(index).getInitialization() + " " + lineEl[2]).append("\n");
							}
						}else {
							if(index != -1) {
								if(!checkDepth(variables.get(index),currentDepth))
									variables.add(new Variable(lineEl[2],Integer.parseInt(lineEl[1]),currentDepth));
								else {
									output.append(lineEl[1] + " " + variables.get(index).getInitialization() + " " + lineEl[2]).append("\n");
								}
							}else {
								variables.add(new Variable(lineEl[2],Integer.parseInt(lineEl[1]),currentDepth));
							}
							foundIteree = true;
						}												
					}
				}else if(line.equalsIgnoreCase("<naredba_pridruzivanja>")) {
					varInit();
				}
				if(line.equalsIgnoreCase("<za_petlja>")) {
					currentIndex++;
					zaPetlja();
				}
				
			}
			if(errorFound)
				return;
			currentIndex++;
		}
		
		currentDepth--;
	}

	// Returns the index of the variable if it already exists and -1 if not 
	private int checkExists(String varName) {
		
		for(int i = variables.size() -1 ; i >= 0 ; i--) {
			if(variables.get(i).getName().equals(varName))
				return i;
		}
		return -1;
	}
	
	private boolean checkDepth(Variable var,int depth) {
		if(var.getDepth() == depth) {
			return true;
		}
		return false;
	}
	

	public String generateOutput() {
		return output.toString();
	}

}
