package hr.fer.zemris.ppj.lab3;

import java.util.*;

public class Variable {
	
	private String name;
	private int initialization;
	private boolean global;
	
	private int depth;
	
	public Variable(String name,int initialization, int depth) {
		this.name = name;
		this.initialization = initialization;
		this.depth = depth;
	}

	public String getName() {
		return name;
	}

	public int getInitialization() {
		return initialization;
	}
	
	public void setNewInitialization(int newLine) {
		this.initialization = newLine;
	}
	
	public int getDepth() {
		return depth;
	}
	
	@Override
	public String toString() {
		return name + " " + initialization + " " + (global ? "global" : "local");
	}
}
