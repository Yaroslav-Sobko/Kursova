package com.example.kursova;

public class Cargo {
	private double length;
	private double height;

	public Cargo(double length, double height) {
		this.length = length;
		this.height = height;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}
}
