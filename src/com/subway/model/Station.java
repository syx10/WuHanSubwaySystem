package com.subway.model;

import java.util.HashSet;
import java.util.Set;

public class Station {
	    private String name;
	    private Set<String> lines = new HashSet<>();

	    public Station(String name) {
	        this.name = name;
	    }

	    public void addLine(String line) {
	        lines.add(line);
	    }

	    // Getters
	    public String getName() { return name; }
	    public Set<String> getLines() { return lines; }

	    @Override
	    public String toString() {
	        return "<" + name + ", " + lines + ">";
	    }
	

}
