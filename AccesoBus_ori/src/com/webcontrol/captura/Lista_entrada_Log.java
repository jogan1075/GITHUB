package com.webcontrol.captura;

public class Lista_entrada_Log {

	private String _Descripcion;
	private String _Fecha;
	private String _Hora;
	private String _Rut;

	public Lista_entrada_Log(String Descripcion, String Rut, String Fecha,
			String Hora) {

		this._Descripcion = Descripcion;
		this._Rut = Rut;
		this._Fecha = Fecha;
		this._Hora = Hora;

	}

	public String get_Descripcion() {
		return _Descripcion;
	}

	public void set_Descripcion(String _Descripcion) {
		this._Descripcion = _Descripcion;
	}

	public String get_Fecha() {
		return _Fecha;
	}

	public void set_Fecha(String _Fecha) {
		this._Fecha = _Fecha;
	}

	public String get_Hora() {
		return _Hora;
	}

	public void set_Hora(String _Hora) {
		this._Hora = _Hora;
	}

	public String get_Rut() {
		return _Rut;
	}

	public void set_Rut(String _Rut) {
		this._Rut = _Rut;
	}

	

}
