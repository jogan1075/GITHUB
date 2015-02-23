package com.webcontrol.captura;


public class Lista_entrada_ControlAcceso {
	//private int idImagen; 
	private String _IdFuncionario; 
	private String _Patente;
	private String _CodError; 
	private String _Fecha;
	private String _Hora;


		  
	//public Lista_entrada (int idImagen, String textoEncima, String textoDebajo) {
	//CN_IDFUNCIONARIOCONTROL,CN_TRANSPORTE,CN_CODIGOERROR,CN_FECHACONTROL,CN_HORACONTROL
	public Lista_entrada_ControlAcceso (String IdFuncionario, String Patente, String CodError, String Fecha, String Hora) {
    
	    this._IdFuncionario = IdFuncionario;
	    this._Patente = Patente;
	    this._CodError = CodError;
	    this._Fecha = Fecha;
	    this._Hora = Hora;
	    
	}
	
	public String get_IdFuncionario() { 
				
	    return _IdFuncionario; 
	}
	
	public String get_Patente() { 

	    return _Patente; 
	}
	
	public String get_CodError() {
		
	    return _CodError; 
	}
	
	public String get_Fecha() { 
				
	    return _Fecha; 
	}
	
	public String get_Hora() { 
		
	    return _Hora; 
	}
		
	
}
