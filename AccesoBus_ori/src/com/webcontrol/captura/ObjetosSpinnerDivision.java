package com.webcontrol.captura;

public class ObjetosSpinnerDivision {
	
	String id;
	String nombre;
	//Constructor
	public ObjetosSpinnerDivision(String id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}
	
	@Override
	public String toString() {
		return nombre;
	}
	public String getId() {
		return id;
	}
}