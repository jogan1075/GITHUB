package com.webcontrol.captura;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import Metodos.MetodosGenerales;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	TextView TxtTitulo, TxtRespuesta, Identificador;
	EditText EditTxtRut;
	Button BtnCWS, BtnRegresar;
	Exception exception = null;
	JSONObject RespuestaJson;
	private DataBaseManager Manager;
	private Cursor CursorFuncionario, CursorFuncionarioPost, CursorVehiculo,
			CursorGuardia, CursorEstadoChoferPasajero, CursorConexion,
			CursorWs;
	int contadorErroresSolicitud;
	String _Sentido, _Lectura, _TipoIngreso, _Data, Consulta, Local, Division;
	String UID, EstadoUID, IdFuncionario, NombreFuncionario,
			ApellidoFuncionario, NombreEmpresa, AutorizacionFuncionario;
	String Ost = "", CCosto = "", TipoPase = "", IdEmpresa = "";
	String AutorizacionVehiculo, FechaConsulta, AutorizacionConductor, Estado,
			Mensaje, Imagen, Patente, IdUsuario, RutGuardia;
	String IdVehiculo, Marca, Modelo, AnioVehiculo, TipoVehiculo, FechaHora,
			Fecha, Hora, SentidoChoferPasajero, TipoConexion,
			AutorizacionGuardia;
	String NAMESPACE, URL, METHOD_NAME, SOAP_ACTION, Autentificacion,
			UsuarioWs, ContraseniaWs, Idioma, Txt1, Txt2, Txt3, Txt4, Txt5,
			Txt6, Txt7, Txt8, Txt9, Txt10, Txt11, Txt12, Toast1, Toast2,
			Toast3, Toast4, Toast5;
	int ContadorConexion;
	private HomeKeyLocker mHomeKeyLocker;
	String consulta1, consulta2;
	Vibrator Vibrador;
	private ProgressDialog progress;
	MediaPlayer mpAviso;

	MetodosGenerales util;
	String textoPantalla;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		Bundle b = getIntent().getExtras();
		_Sentido = b.getString("Sentido");
		_Lectura = b.getString("Lectura");
		_TipoIngreso = b.getString("TipoIngreso");
		_Data = b.getString("Data");
		

//		HomeKeyLocker mHomeKeyLocker = new HomeKeyLocker();
//		mHomeKeyLocker.lock(this);

		Vibrador = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
		mpAviso = MediaPlayer.create(getApplicationContext(), R.raw.aviso);

		progress = new ProgressDialog(this);
		progress.setMessage(Toast1);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setIndeterminate(true);

		BtnCWS = (Button) findViewById(R.id.buttonCWS);
		BtnRegresar = (Button) findViewById(R.id.buttonRegresar);
		TxtTitulo = (TextView) findViewById(R.id.textViewTitulo);
		EditTxtRut = (EditText) findViewById(R.id.editText1);
		Identificador = (TextView) findViewById(R.id.camaraConfig);
		Manager = new DataBaseManager(this);

		util = new MetodosGenerales();
		Cursor BuscarPantalla = Manager.BuscarPantalla();
		if (BuscarPantalla.moveToFirst()) {
			textoPantalla = BuscarPantalla.getString(0);
		}
		if (textoPantalla.equalsIgnoreCase("VehiculoSalida")) {
			Identificador.setText("Ingrese Placa: ");
		} else if (textoPantalla.equalsIgnoreCase("PersonaSalida")) {
			Identificador.setText("Ingrese DNI: ");
		} else if (textoPantalla.equalsIgnoreCase("VehiculoIngreso")) {
			Identificador.setText("Ingrese Placa: ");
		} else if (textoPantalla.equalsIgnoreCase("PersonaIngreso")) {
			Identificador.setText("Ingrese DNI: ");
		}
		CursorWs = Manager.CursorConfig();

		if (CursorWs.moveToFirst()) {
			do {
				NAMESPACE = CursorWs.getString(1);
				URL = CursorWs.getString(2);
				UsuarioWs = CursorWs.getString(3); // desarrollo
				ContraseniaWs = CursorWs.getString(4); // Desa1.
				Autentificacion = CursorWs.getString(5);
				Division = CursorWs.getString(7);
				Local = CursorWs.getString(8);
			} while (CursorWs.moveToNext());

			if (_TipoIngreso.equalsIgnoreCase("UID")) {
				
				
				Consulta = _Data;
				WS();
			} else if (_TipoIngreso.equalsIgnoreCase("ID")) {
				Consulta = _Data;
				WS();
			}

		} else {
			Toast.makeText(getApplicationContext(), "Configure Webservice",
					Toast.LENGTH_LONG).show();
			finish();

			Intent ConfWS = new Intent(this, inicio.class);
			startActivity(ConfWS);

		}
	}

	public void BtnWS(View v) {
		// progress.show();
		Vibrador.vibrate(80);
		// OCULTAR TECLADO
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(EditTxtRut.getWindowToken(),
				0);

		Consulta = EditTxtRut.getText().toString().toUpperCase();
		EditTxtRut.setText("");

		if (!Consulta.contains(" ")) {
			if (!Consulta.equals("")) {
				WS();
			} else {
				Toast.makeText(getApplicationContext(), "Ingrese Datos",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "No Ingrese Espacios",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void WS() {
		
		String UID="", RUT="";
		FechaHora = util.getDateTime();
		String Fecha1 = FechaHora.substring(0, 10);
		Fecha = Fecha1.substring(0, 4) + Fecha1.substring(5, 7)
				+ Fecha1.substring(8, 10);
		Hora = FechaHora.substring(11, FechaHora.length());
		progress.show();
		if ((_Lectura.equals("Funcionario")) || (_Lectura.equals("Pasajero"))
				|| (_Lectura.equals("Chofer"))) {
			CursorEstadoChoferPasajero = Manager.BuscarEstadoChoferPasajero();

			if (CursorEstadoChoferPasajero.moveToFirst()) // SI -> VAN DENTRO DE
															// UN VEHICULO
			{
				// rescato la patente y despues al llamar el servicio web le
				// paso la lectura mas la patente
				do {
					Patente = CursorEstadoChoferPasajero.getString(1);
					SentidoChoferPasajero = CursorEstadoChoferPasajero
							.getString(2);
				} while (CursorEstadoChoferPasajero.moveToNext());
			} else {
				if (_Sentido.equals("In")) {
					// PATENTE PEATON INGRESO ZZ9999
					Patente = "ZZ9999";
				} else if (_Sentido.equals("Out")) {
					// PATENTE PEATON SALIDA ZZ9998
					Patente = "ZZ9998";
				}
			}
			
			Cursor util1 = Manager.CursorConsultaUtil(Consulta);
			if(util1.moveToFirst()){
				Manager.ActualizarUTIL(Consulta);
			}else{
				Manager.InsertarUTIL(Consulta);
			}

			if ((_TipoIngreso.equals("Manual")) || (_TipoIngreso.equals("ID")))// este
																				// si
			{
				CursorFuncionario = Manager.BuscarFuncionarioId(Consulta);
			} else {
				CursorFuncionario = Manager.BuscarFuncionarioUID(Consulta);
			}

			

			if (CursorFuncionario.moveToFirst()) {

				progress.dismiss();

				do {
					EstadoUID = CursorFuncionario.getString(1);
					IdFuncionario = CursorFuncionario.getString(2);
					AutorizacionFuncionario = CursorFuncionario.getString(11);
					AutorizacionConductor = CursorFuncionario.getString(12);
					TipoPase = CursorFuncionario.getString(9);
					Ost = CursorFuncionario.getString(7);
					CCosto = CursorFuncionario.getString(8);
					IdEmpresa = CursorFuncionario.getString(6);
				} while (CursorFuncionario.moveToNext());

				// Toast.makeText(getApplicationContext(), IdFuncionario,
				// Toast.LENGTH_LONG).show();
				if ((_TipoIngreso.equals("Manual"))
						|| (_TipoIngreso.equals("ID"))) {
					Manager.ActualizarFechaConsultaFuncionario(Consulta,
							util.getDateTime());// actualizando fecha de
												// consulta
				} else {
					Manager.ActualizarFechaConsultaFuncionario(IdFuncionario,
							util.getDateTime());
				}

				Intent Correcto = new Intent(getApplicationContext(),
						Correcto.class);
				Correcto.putExtra("Lectura", "BBDD");
				Correcto.putExtra("Id", Consulta);
				Correcto.putExtra("Mensaje", "Mensaje");
				Correcto.putExtra("Sentido", _Sentido);
				// Correcto.putExtra("TipoDato", "IdFuncionario" );

				if ((_Lectura.equals("Funcionario"))
						|| (_Lectura.equals("Pasajero"))) {
					if ((_TipoIngreso.equals("Manual"))
							|| (_TipoIngreso.equals("ID"))) {
						Correcto.putExtra("TipoDato", "IdFuncionario");
					} else {
						Correcto.putExtra("TipoDato", "UIDFuncionario");
					}

					if ((AutorizacionFuncionario.equals("NO"))
							|| (AutorizacionFuncionario.equals("XX"))) {
						Correcto.putExtra("Autorizacion", "NO");

						if ((_TipoIngreso.equals("Manual"))
								|| (_TipoIngreso.equals("ID"))) {
							if (_Lectura.equals("Funcionario")) {
								// USUARIO NO AUTORIZADO INGRESO 198 -
								// SALIDA 209}
								if (_Sentido.equals("In")) {
									// Toast.makeText(getApplicationContext(),
									// "CodError: 198" + " Rut: " + Consulta
									// , Toast.LENGTH_LONG).show();
									
									Cursor c = Manager
											.CursorConsultarControlAcceso(
													Consulta, "ERR", "198",
													Fecha, Hora, Local);
									if (!c.moveToFirst()) {
									Manager.InsertarDatosControlAcceso("",
											Consulta, IdEmpresa, "N", Patente,
											"198", Local,
											"ERR", CCosto,
											Ost, TipoPase, "BBDD", Fecha, Hora,
											"0");
									}
								} else {
									// Toast.makeText(getApplicationContext(),
									// "CodError: 209" +" Rut: " + Consulta
									// , Toast.LENGTH_LONG).show();
									
									Cursor c = Manager.CursorConsultarControlAcceso(Consulta, "ERR", "209", Fecha, Hora, Local);
									if(!c.moveToFirst()){
									Manager.InsertarDatosControlAcceso("",
											Consulta, IdEmpresa, "N", Patente,
											"209", Local,
											"ERR", CCosto,
											Ost, TipoPase, "BBDD", Fecha, Hora,
											"0");
									}
								}

							} else if (_Lectura.equals("Pasajero")) {
								// PASAJERO NO AUTORIZADO INGRESO 189 -
								// SALIDA 210
								if (_Sentido.equals("In")) {
									// Toast.makeText(getApplicationContext(),
									// "CodError: 189" + " Rut: " + Consulta
									// , Toast.LENGTH_LONG).show();
									
									Cursor c = Manager.CursorConsultarControlAcceso(Consulta, "ERR", "189", Fecha, Hora, Local);
									if(!c.moveToFirst()){
									Manager.InsertarDatosControlAcceso("",
											Consulta, IdEmpresa, "N", Patente,
											"189", Local,
											"ERR", CCosto,
											Ost, TipoPase, "BBDD", Fecha, Hora,
											"0");
									}
								} else {
									// Toast.makeText(getApplicationContext(),
									// "CodError: 210" + " Rut: " + Consulta
									// , Toast.LENGTH_LONG).show();
									Cursor c = Manager.CursorConsultarControlAcceso(Consulta, "ERR", "210", Fecha, Hora, Local);
									if(!c.moveToFirst()){
									
									Manager.InsertarDatosControlAcceso("",
											Consulta, IdEmpresa, "N", Patente,
											"210", Local,
											"ERR", CCosto,
											Ost, TipoPase, "BBDD", Fecha, Hora,
											"0");
									}
								}
							}
						} else // si se ha ingresado un UID -- modificar
						{
							if (_Lectura.equals("Funcionario")) {
								// USUARIO NO AUTORIZADO INGRESO 198 -
								// SALIDA 209}
								if (_Sentido.equals("In")) {
									// Toast.makeText(getApplicationContext(),
									// "CodError: 198" + " - UID: " +
									// Consulta + " - Rut: " + IdFuncionario
									// , Toast.LENGTH_LONG).show();
									Cursor c = Manager.CursorConsultarControlAcceso(IdFuncionario, "ERR", "198", Fecha, Hora, Local);
									if(!c.moveToFirst()){
									Manager.InsertarDatosControlAcceso(
											Consulta, IdFuncionario, IdEmpresa,
											"N", Patente, "198", Local,
											"ERR", CCosto,
											Ost, TipoPase, "BBDD", Fecha, Hora,
											"0");
									}
								} else {
									// Toast.makeText(getApplicationContext(),
									// "CodError: 209" + " - UID: " +
									// Consulta + " - Rut: " + IdFuncionario
									// , Toast.LENGTH_LONG).show();
									Cursor c = Manager.CursorConsultarControlAcceso(IdFuncionario, "ERR", "209", Fecha, Hora, Local);
									if(!c.moveToFirst()){
									
									Manager.InsertarDatosControlAcceso(
											Consulta, IdFuncionario, IdEmpresa,
											"N", Patente, "209", Local,
											"ERR", CCosto,
											Ost, TipoPase, "BBDD", Fecha, Hora,
											"0");
									}
								}

							} else if (_Lectura.equals("Pasajero")) {
								// PASAJERO NO AUTORIZADO INGRESO 189 -
								// SALIDA 210
								if (_Sentido.equals("In")) {
									// Toast.makeText(getApplicationContext(),
									// "CodError: 189" + " - UID: " +
									// Consulta + " - Rut: " + IdFuncionario
									// , Toast.LENGTH_LONG).show();
									
									Cursor c = Manager.CursorConsultarControlAcceso(IdFuncionario, "ERR", "189", Fecha, Hora, Local);
									if(!c.moveToFirst()){
									
									
									Manager.InsertarDatosControlAcceso(
											Consulta, IdFuncionario, IdEmpresa,
											"N", Patente, "189", Local,
											"ERR", CCosto,
											Ost, TipoPase, "BBDD", Fecha, Hora,
											"0");
									}
									
								} else {
									// Toast.makeText(getApplicationContext(),
									// "CodError: 210" + " - UID: " +
									// Consulta + " - Rut: " + IdFuncionario
									// , Toast.LENGTH_LONG).show();
									
//									String UID, String IdFuncionario, String IdEmpresa, 
//									String Conductor, String Transporte, String CodigoError,
//									String IdLocal, String Sentido, String CentroCosto, String OST,
//									String TipoPase, String EstadoConexion, String FechaControl,
//									String HoraControl, String Sincronizacion
									
									Cursor c = Manager.CursorConsultarControlAcceso(IdFuncionario, "ERR", "210", Fecha, Hora, Local);
									if(!c.moveToFirst()){
									
											Manager.InsertarDatosControlAcceso(
											Consulta, IdFuncionario, IdEmpresa,
											"N", Patente, "210", Local,
											"ERR", CCosto,
											Ost, TipoPase, "BBDD", Fecha, Hora,
											"0");
									}
								}
							}
						}// --

					} else {
						Correcto.putExtra("Autorizacion", "SI");

						if ((_TipoIngreso.equals("Manual"))
								|| (_TipoIngreso.equals("ID")))// si se
																// ingreso
																// rut
						{
							if (_Lectura.equals("Funcionario")) {
								// USUARIO AUTORIZADO INGRESO SALIDA RUT
								
								Cursor c = Manager.CursorConsultarControlAcceso(Consulta, _Sentido.toUpperCase(), "", Fecha, Hora, Local);
								if(!c.moveToFirst()){
								Manager.InsertarDatosControlAcceso("",
										Consulta, IdEmpresa, "N", Patente, "",
										Local, _Sentido.toUpperCase(), CCosto,
										Ost, TipoPase, "BBDD", Fecha, Hora, "0");
								}
							} else if (_Lectura.equals("Pasajero")) {
								// PASAJERO AUTORIZADO INGRESO SALIDA RUT
								// Toast.makeText(getApplicationContext(),
								// "Sentido: " + _Sentido + " - Rut: " +
								// Consulta , Toast.LENGTH_LONG).show();
								
								Cursor c = Manager.CursorConsultarControlAcceso(Consulta, _Sentido.toUpperCase(), "", Fecha, Hora, Local);
								if(!c.moveToFirst()){
								Manager.InsertarDatosControlAcceso("",
										Consulta, IdEmpresa, "N", Patente, "",
										Local, _Sentido.toUpperCase(), CCosto,
										Ost, TipoPase, "BBDD", Fecha, Hora, "0");
								}

							}
						} else // si se ingreso UID
						{
							if (_Lectura.equals("Funcionario")) {
								// USUARIO AUTORIZADO INGRESO SALIDA UID
								// Toast.makeText(getApplicationContext(),
								// "Sentido: " + _Sentido + " - UID: " +
								// Consulta + " - Rut: " + IdFuncionario ,
								// Toast.LENGTH_LONG).show();
								
								
								Cursor c = Manager.CursorConsultarControlAcceso(IdFuncionario, _Sentido.toUpperCase(), "", Fecha, Hora, Local);
								if(!c.moveToFirst()){
								Manager.InsertarDatosControlAcceso(Consulta,
										IdFuncionario, IdEmpresa, "N", Patente,
										"", Local, _Sentido.toUpperCase(),
										CCosto, Ost, TipoPase, "BBDD", Fecha,
										Hora, "0");
								}

							} else if (_Lectura.equals("Pasajero")) {
								// PASAJERO AUTORIZADO INGRESO SALIDA UID
								// Toast.makeText(getApplicationContext(),
								// "Sentido: " + _Sentido + " - UID: " +
								// Consulta + " - Rut: " + IdFuncionario ,
								// Toast.LENGTH_LONG).show();
								
								Cursor c = Manager.CursorConsultarControlAcceso(IdFuncionario, _Sentido.toUpperCase(), "", Fecha, Hora, Local);
								if(!c.moveToFirst()){
								Manager.InsertarDatosControlAcceso(Consulta,
										IdFuncionario, IdEmpresa, "N", Patente,
										"", Local, _Sentido.toUpperCase(),
										CCosto, Ost, TipoPase, "BBDD", Fecha,
										Hora, "0");
								}

							}
						}
					}
				}

				else if (_Lectura.equals("Chofer")) {
					if ((_TipoIngreso.equals("Manual"))
							|| (_TipoIngreso.equals("ID"))) {
						Correcto.putExtra("TipoDato", "IdChofer");
					} else {
						Correcto.putExtra("TipoDato", "UIDChofer");
					}

					if ((AutorizacionConductor.equals("SI"))
							&& (AutorizacionFuncionario.equals("SI"))) {
						Correcto.putExtra("Autorizacion", "SI");
						Intent Pasajero = new Intent(getApplicationContext(),
								inicio.class);
						Manager.ActualizarPantalla("Pasajero");
						// Pasajero.putExtra("Sentido", _Sentido);

						// CONTROL DE ACCESO, CONDUCTOR AUTORIZADO INGRESO,
						// SENTIDO
						if ((_TipoIngreso.equals("Manual"))
								|| (_TipoIngreso.equals("ID"))) {
							// Toast.makeText(getApplicationContext(),
							// "Sentido: " + _Sentido + " - Rut: " +
							// Consulta , Toast.LENGTH_LONG).show();
							
							Cursor c = Manager.CursorConsultarControlAcceso(Consulta, _Sentido.toUpperCase(), "", Fecha, Hora, Local);
							if(!c.moveToFirst()){
							Manager.InsertarDatosControlAcceso("", Consulta,
									IdEmpresa, "S", Patente, "", Local,
									_Sentido.toUpperCase(), CCosto, Ost,
									TipoPase, "BBDD", Fecha, Hora, "0");
							}
						} else// si es por UID
						{
							// Toast.makeText(getApplicationContext(),
							// "Sentido: " + _Sentido + " - UID: " +
							// Consulta + " - Rut: " + IdFuncionario ,
							// Toast.LENGTH_LONG).show();
							
							Cursor c = Manager.CursorConsultarControlAcceso(IdFuncionario, _Sentido.toUpperCase(), "", Fecha, Hora, Local);
							if(!c.moveToFirst()){
							Manager.InsertarDatosControlAcceso(Consulta,
									IdFuncionario, IdEmpresa, "S", Patente, "",
									Local, _Sentido.toUpperCase(), CCosto, Ost,
									TipoPase, "BBDD", Fecha, Hora, "0");
							}
						}

						finish();
						startActivity(Pasajero);

					} else if (((AutorizacionConductor.equals("SI")) && (AutorizacionFuncionario
							.equals("NO")))
							|| ((AutorizacionConductor.equals("SI")) && (AutorizacionFuncionario
									.equals("XX")))) {
						// CHOFER INGRESO: USUARIO NO AUTORIZADO, LICENCIA
						// AUTORIZADA (510), nuevo
						// CHOFER SALIDA: USUARIO NO AUTORIZADO, LICENCIA
						// AUTORIZADA (512) crear

						if (_Sentido.equals("In")) {
							if ((_TipoIngreso.equals("Manual"))
									|| (_TipoIngreso.equals("ID"))) {
								// Toast.makeText(getApplicationContext(),
								// "CodError: 510" + " - Rut: " + Consulta ,
								// Toast.LENGTH_LONG).show();
								
								Cursor c = Manager.CursorConsultarControlAcceso(Consulta, "ERR", "510", Fecha, Hora, Local);
								if(!c.moveToFirst()){
								Manager.InsertarDatosControlAcceso("",
										Consulta, IdEmpresa, "S", Patente,
										"510", Local, "ERR",
										CCosto, Ost, TipoPase, "BBDD", Fecha,
										Hora, "0");
								}
							} else // UID
							{
								// Toast.makeText(getApplicationContext(),
								// "CodError: 510" + " - UID: " + Consulta +
								// " - Rut: " + IdFuncionario ,
								// Toast.LENGTH_LONG).show();
								Cursor c = Manager.CursorConsultarControlAcceso(IdFuncionario, "ERR", "510", Fecha, Hora, Local);
								if(!c.moveToFirst()){
								Manager.InsertarDatosControlAcceso(Consulta,
										IdFuncionario, IdEmpresa, "S", Patente,
										"510", Local, "ERR",
										CCosto, Ost, TipoPase, "BBDD", Fecha,
										Hora, "0");
								}
							}

						} else {
							if ((_TipoIngreso.equals("Manual"))
									|| (_TipoIngreso.equals("ID"))) {
								// Toast.makeText(getApplicationContext(),
								// "CodError: 512" + " - Rut: " + Consulta ,
								// Toast.LENGTH_LONG).show();
								
								Cursor c = Manager.CursorConsultarControlAcceso(Consulta, "ERR", "512", Fecha, Hora, Local);
								if(!c.moveToFirst()){
								Manager.InsertarDatosControlAcceso("",
										Consulta, IdEmpresa, "S", Patente,
										"512", Local, "ERR",
										CCosto, Ost, TipoPase, "BBDD", Fecha,
										Hora, "0");
								}
							} else// UID
							{
								// Toast.makeText(getApplicationContext(),
								// "CodError: 512" + " - UID: " + Consulta +
								// " - Rut: " + IdFuncionario ,
								// Toast.LENGTH_LONG).show();
								
								Cursor c = Manager.CursorConsultarControlAcceso(IdFuncionario, "ERR", "512", Fecha, Hora, Local);
								if(!c.moveToFirst()){
								Manager.InsertarDatosControlAcceso(Consulta,
										IdFuncionario, IdEmpresa, "S", Patente,
										"512", Local, "ERR",
										CCosto, Ost, TipoPase, "BBDD", Fecha,
										Hora, "0");
								}
							}
						}

						// ---

					} else if ((AutorizacionConductor.equals("NO"))
							&& (AutorizacionFuncionario.equals("SI"))) {
						// CHOFER INGRESO: USUARIO AUTORIZADO, LICENCIA NO
						// AUTORIZADA (509), nuevo
						// CHOFER SALIDA: USUARIO AUTORIZADO, LICENCIA NO
						// AUTORIZADA (511) nuevo

						if (_Sentido.equals("In")) {
							if ((_TipoIngreso.equals("Manual"))
									|| (_TipoIngreso.equals("ID"))) {
								// Toast.makeText(getApplicationContext(),
								// "CodError: 509" + " - Rut: " + Consulta ,
								// Toast.LENGTH_LONG).show();
								
								Cursor c = Manager.CursorConsultarControlAcceso(Consulta, "ERR", "509", Fecha, Hora, Local);
								if(!c.moveToFirst()){
								Manager.InsertarDatosControlAcceso("",
										Consulta, IdEmpresa, "S", Patente,
										"509", Local, "ERR",
										CCosto, Ost, TipoPase, "BBDD", Fecha,
										Hora, "0");
								}
							} else // UID
							{
								// Toast.makeText(getApplicationContext(),
								// "CodError: 509" + " - UID: " + Consulta +
								// " - Rut: " + IdFuncionario ,
								// Toast.LENGTH_LONG).show();
								Cursor c = Manager.CursorConsultarControlAcceso(IdFuncionario, "ERR", "509", Fecha, Hora, Local);
								if(!c.moveToFirst()){
								Manager.InsertarDatosControlAcceso(Consulta,
										IdFuncionario, IdEmpresa, "S", Patente,
										"509", Local, "ERR",
										CCosto, Ost, TipoPase, "BBDD", Fecha,
										Hora, "0");
								}
							}

						} else {
							if ((_TipoIngreso.equals("Manual"))
									|| (_TipoIngreso.equals("ID"))) {
								// Toast.makeText(getApplicationContext(),
								// "CodError: 511" + " - Rut: " + Consulta ,
								// Toast.LENGTH_LONG).show();
								
								Cursor c = Manager.CursorConsultarControlAcceso(Consulta, "ERR", "511", Fecha, Hora, Local);
								if(!c.moveToFirst()){
								Manager.InsertarDatosControlAcceso("",
										Consulta, IdEmpresa, "S", Patente,
										"511", Local, "ERR",
										CCosto, Ost, TipoPase, "BBDD", Fecha,
										Hora, "0");
								}
							} else// UID
							{
								// Toast.makeText(getApplicationContext(),
								// "CodError: 511" + " - UID: " + Consulta +
								// " - Rut: " + IdFuncionario ,
								// Toast.LENGTH_LONG).show();
								
								Cursor c = Manager.CursorConsultarControlAcceso(IdFuncionario, "ERR", "511", Fecha, Hora, Local);
								if(!c.moveToFirst()){
								Manager.InsertarDatosControlAcceso(Consulta,
										IdFuncionario, IdEmpresa, "S", Patente,
										"511", Local, "ERR",
										CCosto, Ost, TipoPase, "BBDD", Fecha,
										Hora, "0");
								}
							}
						}

						// ---
					} else if (((AutorizacionConductor.equals("NO")) && (AutorizacionFuncionario
							.equals("NO")))
							|| ((AutorizacionConductor.equals("NO")) && (AutorizacionFuncionario
									.equals("XX")))) {
						// CHOFER INGRESO: USUARIO NO AUTORIZADO, LICENCIA
						// NO AUTORIZADA (513)------
						// CHOFER SALIDA: USUARIO NO AUTORIZADO, LICENCIA NO
						// AUTORIZADA (514) ------
						if (_Sentido.equals("In")) {
							if ((_TipoIngreso.equals("Manual"))
									|| (_TipoIngreso.equals("ID"))) {
								// Toast.makeText(getApplicationContext(),
								// "CodError: 513" + " - Rut: " + Consulta ,
								// Toast.LENGTH_LONG).show();
								Cursor c = Manager.CursorConsultarControlAcceso(Consulta, "ERR", "513", Fecha, Hora, Local);
								if(!c.moveToFirst()){
								Manager.InsertarDatosControlAcceso("",
										Consulta, IdEmpresa, "S", Patente,
										"513", Local, "ERR",
										CCosto, Ost, TipoPase, "BBDD", Fecha,
										Hora, "0");
								}
							} else // UID
							{
								// Toast.makeText(getApplicationContext(),
								// "CodError: 513" + " - UID: " + Consulta +
								// " - Rut: " + IdFuncionario ,
								// Toast.LENGTH_LONG).show();
								
								Cursor c = Manager.CursorConsultarControlAcceso(IdFuncionario, "ERR", "513", Fecha, Hora, Local);
								if(!c.moveToFirst()){
								Manager.InsertarDatosControlAcceso(Consulta,
										IdFuncionario, IdEmpresa, "S", Patente,
										"513", Local, "ERR",
										CCosto, Ost, TipoPase, "BBDD", Fecha,
										Hora, "0");
								}
							}

						} else {
							if ((_TipoIngreso.equals("Manual"))
									|| (_TipoIngreso.equals("ID"))) {
								// Toast.makeText(getApplicationContext(),
								// "CodError: 514" + " - Rut: " + Consulta ,
								// Toast.LENGTH_LONG).show();
								
								Cursor c = Manager.CursorConsultarControlAcceso(Consulta, "ERR", "514", Fecha, Hora, Local);
								if(!c.moveToFirst()){
								Manager.InsertarDatosControlAcceso("",
										Consulta, IdEmpresa, "S", Patente,
										"514", Local, "ERR",
										CCosto, Ost, TipoPase, "BBDD", Fecha,
										Hora, "0");
								}
							} else// UID
							{
								// Toast.makeText(getApplicationContext(),
								// "CodError: 514" + " - UID: " + Consulta +
								// " - Rut: " + IdFuncionario ,
								// Toast.LENGTH_LONG).show();
								
								Cursor c = Manager.CursorConsultarControlAcceso(IdFuncionario, "ERR", "514", Fecha, Hora, Local);
								if(!c.moveToFirst()){
								Manager.InsertarDatosControlAcceso(Consulta,
										IdFuncionario, IdEmpresa, "S", Patente,
										"514", Local, "ERR",
										CCosto, Ost, TipoPase, "BBDD", Fecha,
										Hora, "0");
								}
							}

						}
						Correcto.putExtra("Autorizacion", "NO");
					}

				}

				if (!(_TipoIngreso.equals("Manual"))) {
					finish();
				}

				startActivity(Correcto);

			} else {
				progress.dismiss();
				finish();
				Manager.InsertarDatosFuncionario("No Existe", "No Existe",
						Consulta, "No Existe", "No Existe", "No Existe",
						"No Existe", "No Existe", "No Existe", "No Existe",
						"0", "NO", "NO", "No Existe");
				
				
				startActivity(new Intent(getApplicationContext(),
						NoExiste.class));

			}
			// else {
			// s
			// if (isOnline()) {
			// METHOD_NAME = "WsDataFuncionario";
			// SOAP_ACTION = NAMESPACE + "WsDataFuncionario";
			//
			// if ((_TipoIngreso.equals("Manual"))
			// || (_TipoIngreso.equals("ID"))) {
			// ConectarWsDataFuncionario(Consulta, "IdFuncionario");
			// } else {
			// ConectarWsDataFuncionario(Consulta, "UIDFuncionario");
			// }
			// }
			// } else {
			// 2
			// // insertar log de consulta de usuario que no existe en la
			// // bbdd
			// progress.dismiss();
			//
			// Intent Correcto = new Intent(getApplicationContext(),
			// Correcto.class);
			// Correcto.putExtra("Lectura", "BBDD");
			// Correcto.putExtra("Id", Consulta);
			// Correcto.putExtra("Mensaje", "Mensaje");
			// Correcto.putExtra("Sentido", _Sentido);
			// // Correcto.putExtra("TipoDato", "IdFuncionario" );
			//
			// if ((_Lectura.equals("Funcionario"))
			// || (_Lectura.equals("Pasajero"))) {
			// if ((_TipoIngreso.equals("Manual"))
			// || (_TipoIngreso.equals("ID"))) {
			// Correcto.putExtra("TipoDato", "IdFuncionario");
			// } else {
			// Correcto.putExtra("TipoDato", "UIDFuncionario");
			// }
			//
			// if ((_TipoIngreso.equals("Manual"))
			// || (_TipoIngreso.equals("ID"))) {
			// if (_Lectura.equals("Funcionario")) {
			// // USUARIO NO AUTORIZADO INGRESO 198 - SALIDA
			// // 209}
			// if (_Sentido.equals("In")) {
			// Manager.InsertarDatosControlAcceso("",
			// Consulta, "N", Patente, "198",
			// Local, _Sentido.toUpperCase(), "CentroCosto",
			// "OST", "TipoPase", "BBDD", Fecha,
			// Hora, "0");
			// Manager.InsertarDatosFuncionario(
			// "No Existe", "No Existe", Consulta,
			// "No Existe", "No Existe",
			// "No Existe", "No Existe", "0",
			// "NO", "NO", "No Existe");
			//
			// } else {
			// Manager.InsertarDatosControlAcceso("",
			// Consulta, "N", Patente, "209",
			// Local, _Sentido.toUpperCase(), "CentroCosto",
			// "OST", "TipoPase", "BBDD", Fecha,
			// Hora, "0");
			// Manager.InsertarDatosFuncionario(
			// "No Existe", "No Existe", Consulta,
			// "No Existe", "No Existe",
			// "No Existe", "No Existe", "0",
			// "NO", "NO", "No Existe");
			// }
			//
			// } else if (_Lectura.equals("Pasajero")) {
			// // PASAJERO NO AUTORIZADO INGRESO 189 - SALIDA
			// // 210
			// if (_Sentido.equals("In")) {
			// Manager.InsertarDatosControlAcceso("",
			// Consulta, "N", Patente, "189",
			// Local, _Sentido.toUpperCase(), "CentroCosto",
			// "OST", "TipoPase", "BBDD", Fecha,
			// Hora, "0");
			// Manager.InsertarDatosFuncionario(
			// "No Existe", "No Existe", Consulta,
			// "No Existe", "No Existe",
			// "No Existe", "No Existe", "0",
			// "NO", "NO", "No Existe");
			// } else {
			// Manager.InsertarDatosControlAcceso("",
			// Consulta, "N", Patente, "210",
			// Local, _Sentido.toUpperCase(), "CentroCosto",
			// "OST", "TipoPase", "BBDD", Fecha,
			// Hora, "0");
			// Manager.InsertarDatosFuncionario(
			// "No Existe", "No Existe", Consulta,
			// "No Existe", "No Existe",
			// "No Existe", "No Existe", "0",
			// "NO", "NO", "No Existe");
			// }
			// }
			// } else // si se ha ingresado un UID
			// {
			// if (_Lectura.equals("Funcionario")) {
			// // USUARIO NO AUTORIZADO INGRESO 198 - SALIDA
			// // 209}
			// if (_Sentido.equals("In")) {
			// Manager.InsertarDatosControlAcceso(
			// Consulta, "No Existe", "N",
			// Patente, "198", Local,
			// _Sentido.toUpperCase(), "CentroCosto", "OST",
			// "TipoPase", "BBDD", Fecha, Hora,
			// "0");
			// Manager.InsertarDatosFuncionario(Consulta,
			// "No Existe", "No Existe",
			// "No Existe", "No Existe",
			// "No Existe", "No Existe", "0",
			// "NO", "NO", "No Existe");
			// } else {
			// Manager.InsertarDatosControlAcceso(
			// Consulta, "No Existe", "N",
			// Patente, "209", Local,
			// _Sentido.toUpperCase(), "CentroCosto", "OST",
			// "TipoPase", "BBDD", Fecha, Hora,
			// "0");
			// Manager.InsertarDatosFuncionario(Consulta,
			// "No Existe", "No Existe",
			// "No Existe", "No Existe",
			// "No Existe", "No Existe", "0",
			// "NO", "NO", "No Existe");
			// }
			//
			// } else if (_Lectura.equals("Pasajero")) {
			// // PASAJERO NO AUTORIZADO INGRESO 189 - SALIDA
			// // 210
			// if (_Sentido.equals("In")) {
			// Manager.InsertarDatosControlAcceso(
			// Consulta, "No Existe", "N",
			// Patente, "189", Local,
			// _Sentido.toUpperCase(), "CentroCosto", "OST",
			// "TipoPase", "BBDD", Fecha, Hora,
			// "0");
			// Manager.InsertarDatosFuncionario(Consulta,
			// "No Existe", "No Existe",
			// "No Existe", "No Existe",
			// "No Existe", "No Existe", "0",
			// "NO", "NO", "No Existe");
			// } else {
			// Manager.InsertarDatosControlAcceso(
			// Consulta, "No Existe", "N",
			// Patente, "210", Local,
			// _Sentido.toUpperCase(), "CentroCosto", "OST",
			// "TipoPase", "BBDD", Fecha, Hora,
			// "0");
			// Manager.InsertarDatosFuncionario(Consulta,
			// "No Existe", "No Existe",
			// "No Existe", "No Existe",
			// "No Existe", "No Existe", "0",
			// "No Existe", "No Existe",
			// "No Existe");
			// }
			// }
			// }
			//
			// }
			//
			// else if (_Lectura.equals("Chofer")) {
			// if ((_TipoIngreso.equals("Manual"))
			// || (_TipoIngreso.equals("ID"))) {
			// Correcto.putExtra("TipoDato", "IdChofer");
			// } else {
			// Correcto.putExtra("TipoDato", "UIDChofer");
			// }
			//
			// // CHOFER INGRESO: USUARIO NO AUTORIZADO, LICENCIA NO
			// // AUTORIZADA (513)------
			// // CHOFER SALIDA: USUARIO NO AUTORIZADO, LICENCIA NO
			// // AUTORIZADA (514) ------
			// if (_Sentido.equals("In")) {
			// if ((_TipoIngreso.equals("Manual"))
			// || (_TipoIngreso.equals("ID"))) {
			// Manager.InsertarDatosControlAcceso("",
			// Consulta, "S", Patente, "513",
			// Local, _Sentido.toUpperCase(), "CentroCosto",
			// "OST", "TipoPase", "BBDD", Fecha, Hora,
			// "0");
			// Manager.InsertarDatosFuncionario("No Existe",
			// "No Existe", Consulta, "No Existe",
			// "No Existe", "No Existe", "No Existe",
			// "0", "NO", "NO", "No Existe");
			// } else // UID
			// {
			// Manager.InsertarDatosControlAcceso(Consulta,
			// "No Existe", "S", Patente, "513",
			// Local, _Sentido.toUpperCase(), "CentroCosto",
			// "OST", "TipoPase", "BBDD", Fecha, Hora,
			// "0");
			// Manager.InsertarDatosFuncionario(Consulta,
			// "No Existe", "No Existe", "No Existe",
			// "No Existe", "No Existe", "No Existe",
			// "0", "NO", "NO", "No Existe");
			// }
			//
			// } else {
			// if ((_TipoIngreso.equals("Manual"))
			// || (_TipoIngreso.equals("ID"))) {
			// Manager.InsertarDatosControlAcceso("",
			// Consulta, "S", Patente, "514",
			// Local, _Sentido.toUpperCase(), "CentroCosto",
			// "OST", "TipoPase", "BBDD", Fecha, Hora,
			// "0");
			// Manager.InsertarDatosFuncionario("No Existe",
			// "No Existe", Consulta, "No Existe",
			// "No Existe", "No Existe", "No Existe",
			// "0", "NO", "NO", "No Existe");
			// } else// UID
			// {
			// Manager.InsertarDatosControlAcceso(Consulta,
			// "No Existe", "S", Patente, "514",
			// Local, _Sentido.toUpperCase(), "CentroCosto",
			// "OST", "TipoPase", "BBDD", Fecha, Hora,
			// "0");
			// Manager.InsertarDatosFuncionario(Consulta,
			// "No Existe", "No Existe", "No Existe",
			// "No Existe", "No Existe", "No Existe",
			// "0", "NO", "NO", "No Existe");
			// }
			// }
			// }
			//
			// if (!(_TipoIngreso.equals("Manual"))) {
			// finish();
			// }
			//
			// Correcto.putExtra("Autorizacion", "NO");
			// startActivity(Correcto);
			//
			// // Toast.makeText(this, "Sin Conexion a Internet",
			// // Toast.LENGTH_LONG).show();
			// }
			// }
		} else if (_Lectura.equals("Vehiculo")) {

			if ((_TipoIngreso.equals("Manual")) || (_TipoIngreso.equals("ID"))) {
				CursorVehiculo = Manager.BuscarVehiculoId(Consulta);
			} else {
				CursorVehiculo = Manager.BuscarVehiculoUID(Consulta);
			}

			if (CursorVehiculo.moveToFirst()) {
				IdVehiculo = Integer.toString(CursorVehiculo.getInt(0));

				progress.dismiss();
				// consulto directamente a la bbdd
				// CN_ID2,CN_UIDVEHICULO, CN_ESTADOUIDVEHICULO,
				// CN_IDVEHICULO, CN_MARCA, CN_MODELO, CN_ANIO,
				// CN_TIPOVEHICULO, CN_NOMBREEMPRESAVEHICULO,
				// CN_IDEMPRESAVEHICULO, CN_IMAGENVEHICULO,
				// CN_AUTORIZACIONVEHICULO, CN_FECHACONSULTAVEHICULO,
				// CN_MENSAJEVEHICULO
				do {
					AutorizacionVehiculo = CursorVehiculo.getString(11);
					Mensaje = CursorVehiculo.getString(13);
				} while (CursorVehiculo.moveToNext());

				// actualizar la fecha de consulta
				Manager.ActualizarFechaConsultaVehiculo(IdVehiculo,
						util.getDateTime());

				if (AutorizacionVehiculo.equals("SI")) {
					Intent Correcto = new Intent(this, CorrectoVehiculo.class);
					Correcto.putExtra("Lectura", "BBDD");
					Correcto.putExtra("Id", Consulta);
					Correcto.putExtra("Sentido", _Sentido);

					if ((_TipoIngreso.equals("Manual"))
							|| (_TipoIngreso.equals("ID"))) {
						Correcto.putExtra("TipoDato", "IdVehiculo");
					} else {
						Correcto.putExtra("TipoDato", "UIDVehiculo");
					}

					Correcto.putExtra("Autorizacion", "SI");
					Correcto.putExtra("Mensaje", Mensaje);
					Intent Chofer = new Intent(this, inicio.class);
					Manager.ActualizarPantalla("Chofer");
					// Chofer.putExtra("Sentido", _Sentido);

					finish();

					startActivity(Chofer);
					startActivity(Correcto);
				} else {
					// Toast.makeText(this, "No Autorizado",
					// Toast.LENGTH_SHORT).show();

					Intent Correcto = new Intent(this, CorrectoVehiculo.class);
					Correcto.putExtra("Lectura", "BBDD");
					Correcto.putExtra("Id", Consulta);

					if ((_TipoIngreso.equals("Manual"))
							|| (_TipoIngreso.equals("ID"))) {
						Correcto.putExtra("TipoDato", "IdVehiculo");
					} else {
						Correcto.putExtra("TipoDato", "UIDVehiculo");
					}

					Correcto.putExtra("Autorizacion", "NO");
					Correcto.putExtra("Mensaje", Mensaje);
					Correcto.putExtra("Sentido", _Sentido);

					if (!(_TipoIngreso.equals("Manual"))) {
						finish();
					}
					startActivity(Correcto);
				}

			} else {

				progress.dismiss();

				Intent Correcto = new Intent(this, CorrectoVehiculo.class);
				Correcto.putExtra("Lectura", "BBDD");
				Correcto.putExtra("Id", Consulta);

				if ((_TipoIngreso.equals("Manual"))
						|| (_TipoIngreso.equals("ID"))) {
					Correcto.putExtra("TipoDato", "IdVehiculo");
				} else {
					Correcto.putExtra("TipoDato", "UIDVehiculo");
				}

				Correcto.putExtra("Autorizacion", "NO");
				Correcto.putExtra("Mensaje", "No Existe");
				Correcto.putExtra("Sentido", _Sentido);

				if ((_TipoIngreso.equals("Manual"))
						|| (_TipoIngreso.equals("ID"))) {
					Manager.InsertarDatosVehiculo("No Existe", "No Existe",
							Consulta, "No Existe", "No Existe", "No Existe",
							"No Existe", "No Existe", "No Existe", "0", "NO",
							"No Existe", "No Autorizado");
				} else {
					Manager.InsertarDatosVehiculo(Consulta, "No Existe",
							"No Existe", "No Existe", "No Existe", "No Existe",
							"No Existe", "No Existe", "No Existe", "0", "NO",
							"No Existe", "No Autorizado");
				}

				if (!(_TipoIngreso.equals("Manual"))) {
					finish();
				}
				startActivity(Correcto);
				// Toast.makeText(this, "Sin Conexion a Internet",
				// Toast.LENGTH_LONG).show();

			}
		} else if (_Lectura.equals("Guardia")) {
			// Toast.makeText(getApplicationContext(), "Guardia",
			// Toast.LENGTH_LONG).show();

			if ((_TipoIngreso.equals("Manual")) || (_TipoIngreso.equals("ID"))) {
				CursorGuardia = Manager.BuscarGuardiaId(Consulta);
			} else {
				CursorGuardia = Manager.BuscarGuardiaUID(Consulta);
			}

			if (CursorGuardia.moveToFirst()) {
				RutGuardia = CursorGuardia.getString(2);//

				progress.dismiss();
				// consulto directamente a la bbdd
				// CN_ID2,CN_UIDVEHICULO, CN_ESTADOUIDVEHICULO,
				// CN_IDVEHICULO, CN_MARCA, CN_MODELO, CN_ANIO,
				// CN_TIPOVEHICULO, CN_NOMBREEMPRESAVEHICULO,
				// CN_IDEMPRESAVEHICULO, CN_IMAGENVEHICULO,
				// CN_AUTORIZACIONVEHICULO, CN_FECHACONSULTAVEHICULO,
				// CN_MENSAJEVEHICULO
				do {
					AutorizacionGuardia = CursorGuardia.getString(7);
					// Mensaje = CursorGuardia.getString(13);
				} while (CursorGuardia.moveToNext());

				// actualizar la fecha de consulta
				// Manager.ActualizarFechaConsultaVehiculo(IdVehiculo,
				// getDateTime());

				if (AutorizacionGuardia.equals("SI")) {
					Intent Correcto = new Intent(this, Correcto.class);
					Correcto.putExtra("Lectura", "BBDD");
					Correcto.putExtra("Id", Consulta);
					Correcto.putExtra("Sentido", _Sentido);

					if ((_TipoIngreso.equals("Manual"))
							|| (_TipoIngreso.equals("ID"))) {
						Correcto.putExtra("TipoDato", "IdGuardia");
					} else {
						Correcto.putExtra("TipoDato", "UIDGuardia");
					}

					Correcto.putExtra("Autorizacion", "SI");
					Correcto.putExtra("Mensaje", "Mensaje"); // no hay
																// mensaje
					Manager.ActualizarPantalla("PersonaIngreso");
//					Manager.InsertarDatosLog("Inicio Guardia: " + Consulta,
//							Fecha, Hora);
					Intent Inicio = new Intent(this, inicio.class);

					// Chofer.putExtra("Sentido", _Sentido);

					finish();

					// startActivity(Inicio);
					startActivity(Correcto);
				} else {
					// Toast.makeText(this, "No Autorizado",
					// Toast.LENGTH_SHORT).show();

					Intent Correcto = new Intent(this, Correcto.class);
					Correcto.putExtra("Lectura", "BBDD");
					Correcto.putExtra("Id", Consulta);

					if ((_TipoIngreso.equals("Manual"))
							|| (_TipoIngreso.equals("ID"))) {
						Correcto.putExtra("TipoDato", "IdGuardia");
					} else {
						Correcto.putExtra("TipoDato", "UIDGuardia");
					}

					Correcto.putExtra("Autorizacion", "NO");
					Correcto.putExtra("Mensaje", "Mensaje");
					Correcto.putExtra("Sentido", _Sentido);

					if (!(_TipoIngreso.equals("Manual"))) {
						finish();
					}
					startActivity(Correcto);
				}

			} else {

				progress.dismiss();

				Intent Correcto = new Intent(this, Correcto.class);
				Correcto.putExtra("Lectura", "BBDD");
				Correcto.putExtra("Id", Consulta);

				if ((_TipoIngreso.equals("Manual"))
						|| (_TipoIngreso.equals("ID"))) {
					Correcto.putExtra("TipoDato", "IdGuardia");
				} else {
					Correcto.putExtra("TipoDato", "UIDGuardia");
				}

				Correcto.putExtra("Autorizacion", "NO");
				Correcto.putExtra("Mensaje", "No Existe");
				Correcto.putExtra("Sentido", _Sentido);

				if ((_TipoIngreso.equals("Manual"))
						|| (_TipoIngreso.equals("ID"))) {
					Manager.InsertarDatosGuardia("No Existe", "No Existe",
							Consulta, "No Existe", "No Existe", "No Existe",
							"No Existe", "NO", "0");
				} else {
					Manager.InsertarDatosGuardia(Consulta, "No Existe",
							"No Existe", "No Existe", "No Existe", "No Existe",
							"No Existe", "NO", "0");
				}

				if (!(_TipoIngreso.equals("Manual"))) {
					finish();
				}
				startActivity(Correcto);
				// Toast.makeText(this, "Sin Conexion a Internet",
				// Toast.LENGTH_LONG).show();

			}
		}

	}

	// ERROR WS

	public void ConectarWsDataFuncionario(String Data, String TipoData) {
		final String data = Data;
		final String tipodata = TipoData;
		final String tipodata2;

		if (tipodata.contains("UID")) {
			tipodata2 = "UID";
		} else {
			tipodata2 = tipodata;
		}

		Thread nt = new Thread() {
			String res;

			@Override
			public void run() {

				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
				request.addProperty("data", data.toUpperCase());
				request.addProperty("tipodata", tipodata2.toUpperCase());
				request.addProperty("sentido", _Sentido.toUpperCase());

				// data, tipodata, sentido, Fecha, Hora, Patente, Local,
				// Division, Conductor

				if ((METHOD_NAME.equals("WsDataFuncionario"))
						|| (METHOD_NAME.equals("WsAutorizacionFuncionario"))) {

					request.addProperty("fecha", Fecha);
					request.addProperty("hora", Hora);
					request.addProperty("patente", Patente);

					request.addProperty("local", Local);
					request.addProperty("division", Division);

					if (_Lectura.equals("Chofer")) {
						request.addProperty("conductor", "S");
					} else {
						request.addProperty("conductor", "N");
					}

				}

				request.addProperty("imei", util.getIMEI(MainActivity.this));

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11); // la version de WS 1.1
				envelope.dotNet = true; // estamos utilizando .net

				envelope.setOutputSoapObject(request);

				HttpTransportSE transporte = new HttpTransportSE(URL);

				try {
					// Enviando los datos, siempre deve ir en try catch

					if (Autentificacion.equals("SI")) {
						// *****
						List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
						String Conexion = UsuarioWs + ":" + ContraseniaWs;
						headerList.add(new HeaderProperty("Authorization",
								"Basic "
										+ org.kobjects.base64.Base64
												.encode(Conexion.getBytes())));
						transporte.call(SOAP_ACTION, envelope, headerList);
						// /*****
					} else {
						transporte.call(SOAP_ACTION, envelope);
					}

					SoapPrimitive resultado = (SoapPrimitive) envelope
							.getResponse();

					res = resultado.toString();
					RespuestaJson = new JSONObject(res);

					if (METHOD_NAME.equals("WsAutorizacionFuncionario")) {
						EstadoUID = RespuestaJson.getString("EstadoUID");
						AutorizacionFuncionario = RespuestaJson
								.getString("AutorizacionFuncionario");
						FechaConsulta = RespuestaJson
								.getString("FechaConsulta");
						AutorizacionConductor = RespuestaJson
								.getString("AutorizacionConductor");
						Estado = RespuestaJson.getString("Estado");
						Mensaje = RespuestaJson.getString("Mensaje");
					} else if (METHOD_NAME.equals("WsDataFuncionario")) {
						UID = RespuestaJson.getString("UID");
						EstadoUID = RespuestaJson.getString("EstadoUID");
						IdFuncionario = RespuestaJson
								.getString("IdFuncionario");
						NombreFuncionario = RespuestaJson
								.getString("NombreFuncionario");
						ApellidoFuncionario = RespuestaJson
								.getString("ApellidoFuncionario");
						NombreEmpresa = RespuestaJson
								.getString("NombreEmpresa");
						IdEmpresa = RespuestaJson.getString("IdEmpresa");
						AutorizacionFuncionario = RespuestaJson
								.getString("AutorizacionFuncionario");
						FechaConsulta = RespuestaJson
								.getString("FechaConsulta");
						AutorizacionConductor = RespuestaJson
								.getString("AutorizacionConductor");
						Estado = RespuestaJson.getString("Estado");
						Mensaje = RespuestaJson.getString("Mensaje");
						Imagen = RespuestaJson.getString("Imagen");
					} else if (METHOD_NAME.equals("WsDataVehiculo")) {
						// Todos los datos del vehiculo
						UID = RespuestaJson.getString("UID");
						EstadoUID = RespuestaJson.getString("EstadoUID");
						IdVehiculo = RespuestaJson.getString("IdVehiculo");
						Marca = RespuestaJson.getString("Marca");
						Modelo = RespuestaJson.getString("Modelo");
						AnioVehiculo = RespuestaJson.getString("AnioVehiculo");
						TipoVehiculo = RespuestaJson.getString("TipoVehiculo");
						AutorizacionVehiculo = RespuestaJson
								.getString("Autorizacion");
						IdEmpresa = RespuestaJson.getString("ROLEmpresa");
						NombreEmpresa = RespuestaJson
								.getString("NombreEmpresa");
						Estado = RespuestaJson.getString("Estado");
						Mensaje = RespuestaJson.getString("Mensaje");
						FechaConsulta = RespuestaJson.getString("Fecha");
						Imagen = "NO";

					} else if (METHOD_NAME.equals("WsAutorizacionVehiculo")) {
						EstadoUID = RespuestaJson.getString("EstadoUID");
						AutorizacionVehiculo = RespuestaJson
								.getString("Autorizacion");
						FechaConsulta = RespuestaJson.getString("Fecha");
						Estado = RespuestaJson.getString("Estado");
						Mensaje = RespuestaJson.getString("Mensaje");

					} else if (METHOD_NAME.equals("WSDataGuardia")) {
						// "nombres":"No Existe","apellidos":"No Existe","nombreempresa":"No Existe","idempresa":"No Existe","autorizacion":"No Existe","mensaje":"No Existe","imagen":"No Existe"}
						UID = RespuestaJson.getString("uid");
						EstadoUID = RespuestaJson.getString("estadouid");
						IdFuncionario = RespuestaJson
								.getString("idfuncionario");
						NombreFuncionario = RespuestaJson.getString("nombres");
						ApellidoFuncionario = RespuestaJson
								.getString("apellidos");
						NombreEmpresa = RespuestaJson
								.getString("nombreempresa");
						IdEmpresa = RespuestaJson.getString("idempresa");
						AutorizacionGuardia = RespuestaJson
								.getString("autorizacion");
						Mensaje = RespuestaJson.getString("mensaje");
						Imagen = RespuestaJson.getString("imagen");
						Estado = "OK";
					} else if (METHOD_NAME.equals("WSAutorizacionGuardia")) {
						AutorizacionGuardia = RespuestaJson
								.getString("autorizacion");
						EstadoUID = RespuestaJson.getString("estadouid");
						Estado = "OK";
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					exception = e;
					progress.dismiss();
					// e.printStackTrace();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					exception = e;
					progress.dismiss();
					// e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					exception = e;
					progress.dismiss();
				}

				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						// TODO Auto-generated method stub

						if (exception == null) {
							Manager.ActualizarConexion("Movil", 0,
									util.getDateTime()); // actualizo la
															// conexion
							progress.dismiss();

							if (Estado.equals("ERROR")) {

								Toast.makeText(getApplicationContext(),
										Mensaje, Toast.LENGTH_LONG).show();
							} else {

								if ((tipodata.equals("IdFuncionario"))
										|| (tipodata.equals("UIDFuncionario"))) {

									if (METHOD_NAME
											.equals("WsAutorizacionFuncionario")) {
										if ((_TipoIngreso.equals("Manual"))
												|| (_TipoIngreso.equals("ID"))) {
											Manager.ActualizarAutorizacionFuncionario(
													Consulta, EstadoUID,
													AutorizacionFuncionario,
													FechaConsulta,
													AutorizacionConductor);
										} else {
											CursorFuncionarioPost = Manager
													.BuscarFuncionarioUID(Consulta);

											if (CursorFuncionarioPost
													.moveToFirst()) {
												do {
													IdUsuario = CursorFuncionarioPost
															.getString(2);
												} while (CursorFuncionarioPost
														.moveToNext());

											}

											// Cambiar consulta por id de
											// usuario
											Manager.ActualizarAutorizacionFuncionario(
													IdUsuario, EstadoUID,
													AutorizacionFuncionario,
													FechaConsulta,
													AutorizacionConductor);
										}

									} else {

										if (!Imagen.equals("NO")) {
											if ((_TipoIngreso.equals("Manual"))
													|| (_TipoIngreso
															.equals("ID"))) {
												// Toast.makeText(getApplicationContext(),
												// "UID :" + UID,
												// Toast.LENGTH_LONG).show();
												Manager.InsertarDatosFuncionario(
														UID,
														EstadoUID,
														Consulta,
														NombreFuncionario,
														ApellidoFuncionario,
														NombreEmpresa,
														IdEmpresa,
														Ost,
														CCosto,
														TipoPase,
														Imagen,
														AutorizacionFuncionario,
														AutorizacionConductor,
														FechaConsulta);
											} else {
												// Toast.makeText(getApplicationContext(),
												// "Rut: " + IdFuncionario,
												// Toast.LENGTH_LONG).show();
												Manager.InsertarDatosFuncionario(
														Consulta,
														EstadoUID,
														IdFuncionario,
														NombreFuncionario,
														ApellidoFuncionario,
														NombreEmpresa,
														IdEmpresa,
														Ost,
														CCosto,
														TipoPase,
														Imagen,
														AutorizacionFuncionario,
														AutorizacionConductor,
														FechaConsulta);
											}

										} else {
											if ((_TipoIngreso.equals("Manual"))
													|| (_TipoIngreso
															.equals("ID"))) {
												// Toast.makeText(getApplicationContext(),
												// "Consulta Manual: " +
												// Consulta,
												// Toast.LENGTH_LONG).show();
												Manager.InsertarDatosFuncionario(
														UID,
														EstadoUID,
														Consulta,
														NombreFuncionario,
														ApellidoFuncionario,
														NombreEmpresa,
														IdEmpresa,
														Ost,
														CCosto,
														TipoPase,
														"0",
														AutorizacionFuncionario,
														AutorizacionConductor,
														FechaConsulta);
											} else {
												// Toast.makeText(getApplicationContext(),
												// "Consulta UID: " + Consulta,
												// Toast.LENGTH_LONG).show();
												Manager.InsertarDatosFuncionario(
														Consulta,
														EstadoUID,
														IdFuncionario,
														NombreFuncionario,
														ApellidoFuncionario,
														NombreEmpresa,
														IdEmpresa,
														Ost,
														CCosto,
														TipoPase,
														"0",
														AutorizacionFuncionario,
														AutorizacionConductor,
														FechaConsulta);
											}

										}
									}

									Intent Correcto = new Intent(
											getApplicationContext(),
											Correcto.class);
									Correcto.putExtra("Lectura", "WS");
									Correcto.putExtra("Id", Consulta);
									Correcto.putExtra("Mensaje", Mensaje);
									Correcto.putExtra("Sentido", _Sentido);
									// Correcto.putExtra("TipoDato",
									// "IdFuncionario" );

									if (_Lectura.equals("Pasajero")) {
										Manager.ActualizarPantalla("Pasajero");
										if (_TipoIngreso.equals("Manual")) {
											Intent Pasajero2 = new Intent(
													getApplicationContext(),
													inicio.class);
											startActivity(Pasajero2);
										}
									}

									if ((_Lectura.equals("Funcionario"))
											|| (_Lectura.equals("Pasajero"))) {
										if ((_TipoIngreso.equals("Manual"))
												|| (_TipoIngreso.equals("ID"))) {
											Correcto.putExtra("TipoDato",
													"IdFuncionario");
										} else {
											Correcto.putExtra("TipoDato",
													"UIDFuncionario");
										}

										if ((_TipoIngreso.equals("Manual"))
												|| (_TipoIngreso.equals("ID"))) {
											if (AutorizacionFuncionario
													.equals("NO")
													|| (AutorizacionFuncionario
															.toUpperCase()
															.equals("NO EXISTE"))
													|| (AutorizacionFuncionario
															.equals("XX"))) {
												Correcto.putExtra(
														"Autorizacion", "NO");
											} else {
												Correcto.putExtra(
														"Autorizacion", "SI");
											}
										} else {
											if (AutorizacionFuncionario
													.equals("NO")
													|| (EstadoUID.equals("NO"))
													|| (AutorizacionFuncionario
															.toUpperCase()
															.equals("NO EXISTE"))
													|| (AutorizacionFuncionario
															.equals("XX"))) {
												Correcto.putExtra(
														"Autorizacion", "NO");// aca
											} else {
												Correcto.putExtra(
														"Autorizacion", "SI");
											}
										}

										if (!(_TipoIngreso.equals("Manual"))) {
											finish();
										}

									}

									else if (_Lectura.equals("Chofer")) {
										if ((_TipoIngreso.equals("Manual"))
												|| (_TipoIngreso.equals("ID"))) {
											Correcto.putExtra("TipoDato",
													"IdChofer");
										} else {
											Correcto.putExtra("TipoDato",
													"UIDChofer");
										}

										if ((AutorizacionConductor.equals("SI"))
												&& (AutorizacionFuncionario
														.equals("SI"))) {
											Correcto.putExtra("Autorizacion",
													"SI");
											Manager.ActualizarPantalla("Pasajero");

											if (_TipoIngreso.equals("Manual")) {
												Intent Pasajero = new Intent(
														getApplicationContext(),
														inicio.class);
												startActivity(Pasajero);
											}
											finish();
										} else {
											if (!(_TipoIngreso.equals("Manual"))) {
												finish();
											}
											Correcto.putExtra("Autorizacion",
													"NO");
											// Intent Chofer = new
											// Intent(getApplicationContext(),
											// inicio.class);
											Manager.ActualizarPantalla("Chofer");
											// Pasajero.putExtra("Sentido",
											// _Sentido);

											finish();
											// startActivity(Chofer);
										}

									}

									startActivity(Correcto);
								}

								else if ((tipodata.equals("IdVehiculo"))
										|| (tipodata.equals("UIDVehiculo"))) {
									if (METHOD_NAME
											.equals("WsAutorizacionVehiculo")) {

										Manager.ActualizarAutorizacionVehiculo(
												IdVehiculo, EstadoUID,
												AutorizacionVehiculo,
												FechaConsulta, Mensaje);
									} else {
										if (Imagen.equals("NO")
												|| Imagen.toUpperCase().equals(
														"NO EXISTE")) {

											if (tipodata.equals("UIDVehiculo")) {

												Manager.InsertarDatosVehiculo(
														Consulta, EstadoUID,
														IdVehiculo, Marca,
														Modelo, AnioVehiculo,
														TipoVehiculo,
														NombreEmpresa,
														IdEmpresa, "0",
														AutorizacionVehiculo,
														FechaConsulta, Mensaje);
											} else {
												Manager.InsertarDatosVehiculo(
														UID, EstadoUID,
														Consulta, Marca,
														Modelo, AnioVehiculo,
														TipoVehiculo,
														NombreEmpresa,
														IdEmpresa, "0",
														AutorizacionVehiculo,
														FechaConsulta, Mensaje);
											}

										} else {
											if (tipodata.equals("UIDVehiculo")) {
												Manager.InsertarDatosVehiculo(
														Consulta, EstadoUID,
														IdVehiculo, Marca,
														Modelo, AnioVehiculo,
														TipoVehiculo,
														NombreEmpresa,
														IdEmpresa, Imagen,
														AutorizacionVehiculo,
														FechaConsulta, Mensaje);
											} else {
												Manager.InsertarDatosVehiculo(
														UID, EstadoUID,
														Consulta, Marca,
														Modelo, AnioVehiculo,
														TipoVehiculo,
														NombreEmpresa,
														IdEmpresa, Imagen,
														AutorizacionVehiculo,
														FechaConsulta, Mensaje);
											}

										}
									}

									Intent Correcto = new Intent(
											getApplicationContext(),
											Correcto.class);
									Correcto.putExtra("Lectura", "WS");
									Correcto.putExtra("Id", Consulta);
									Correcto.putExtra("Mensaje", Mensaje);
									Correcto.putExtra("Sentido", _Sentido);

									if (tipodata.equals("UIDVehiculo")) {
										Correcto.putExtra("TipoDato",
												"UIDVehiculo");
									} else {
										Correcto.putExtra("TipoDato",
												"IdVehiculo");
									}

									if ((AutorizacionVehiculo.equals("NO"))
											|| (AutorizacionVehiculo
													.equals("null"))
											|| (AutorizacionVehiculo
													.toUpperCase()
													.equals("NO EXISTE"))) {
										Correcto.putExtra("Autorizacion", "NO");

										if (!(_TipoIngreso.equals("Manual"))) {
											finish();
										}
									} else {
										Correcto.putExtra("Autorizacion", "SI");
										Manager.ActualizarPantalla("Chofer");

										finish();

										if (_TipoIngreso.equals("Manual")) {
											Intent Chofer = new Intent(
													getApplicationContext(),
													inicio.class);
											startActivity(Chofer);
										}
									}

									startActivity(Correcto);
								}

								else if ((tipodata.equals("IdGuardia"))
										|| (tipodata.equals("UIDGuardia"))) {
									if (METHOD_NAME
											.equals("WSAutorizacionGuardia")) {
										if (tipodata.equals("UIDGuardia")) {
											Cursor CursorIdGuaridiaUID = Manager
													.CursorIdGuradiaUID(data);
											String Id = "";

											if (CursorIdGuaridiaUID
													.moveToFirst()) {
												do {
													Id = CursorIdGuaridiaUID
															.getString(0);
												} while (CursorIdGuaridiaUID
														.moveToNext());

											}
											Manager.ActualizarAutorizacionGuardiaUID(
													Id, EstadoUID,
													AutorizacionGuardia);
										} else {
											Manager.ActualizarAutorizacionGuardiaId(
													data, EstadoUID,
													AutorizacionGuardia);
										}

									} else {

										if (Imagen.equals("NO")
												|| Imagen.equals("No Existe")) {
											if (tipodata.equals("UIDGuardia")) {
												Manager.InsertarDatosGuardia(
														Consulta, EstadoUID,
														IdFuncionario,
														NombreFuncionario,
														ApellidoFuncionario,
														NombreEmpresa,
														IdEmpresa,
														AutorizacionGuardia,
														"0");
											} else {
												Manager.InsertarDatosGuardia(
														UID, EstadoUID,
														Consulta,
														NombreFuncionario,
														ApellidoFuncionario,
														NombreEmpresa,
														IdEmpresa,
														AutorizacionGuardia,
														"0");
											}

										} else {
											if (tipodata.equals("UIDGuardia")) {
												Manager.InsertarDatosGuardia(
														Consulta, EstadoUID,
														IdFuncionario,
														NombreFuncionario,
														ApellidoFuncionario,
														NombreEmpresa,
														IdEmpresa,
														AutorizacionGuardia,
														Imagen);
											} else {
												Manager.InsertarDatosGuardia(
														UID, EstadoUID,
														Consulta,
														NombreFuncionario,
														ApellidoFuncionario,
														NombreEmpresa,
														IdEmpresa,
														AutorizacionGuardia,
														Imagen);
											}

										}
									}

									Intent Correcto = new Intent(
											getApplicationContext(),
											Correcto.class);
									Correcto.putExtra("Lectura", "WS");
									Correcto.putExtra("Id", Consulta);
									Correcto.putExtra("Mensaje", Mensaje);
									Correcto.putExtra("Sentido", _Sentido);

									if (tipodata.equals("UIDGuardia")) {
										Correcto.putExtra("TipoDato",
												"UIDGuardia");
									} else {
										Correcto.putExtra("TipoDato",
												"IdGuardia");
									}

									if ((AutorizacionGuardia.equals("NO"))
											|| (AutorizacionGuardia
													.equals("null"))
											|| (AutorizacionGuardia
													.toUpperCase()
													.equals("NO EXISTE"))
											|| (AutorizacionGuardia
													.equals("XX"))) {
										Correcto.putExtra("Autorizacion", "NO");

										if (!(_TipoIngreso.equals("Manual"))) {
											finish();
										}
									} else {
										Correcto.putExtra("Autorizacion", "SI");
										Manager.ActualizarPantalla("PersonaIngreso");
//										Manager.InsertarDatosLog(
//												"Inicio Guardia: " + Consulta,
//												Fecha, Hora);

										finish();

										if (_TipoIngreso.equals("Manual")) {
											Intent inicio = new Intent(
													getApplicationContext(),
													inicio.class);
											startActivity(inicio);
										}
									}

									startActivity(Correcto);
								}
							}
						} else {
							if (!(_TipoIngreso.equals("Manual"))) {
								finish();
							}
							// dejar log

							CursorConexion = Manager.CursorConexion();
							if (CursorConexion.moveToFirst()) {
								do {
									TipoConexion = CursorConexion.getString(0);
									ContadorConexion = CursorConexion.getInt(1);
								} while (CursorConexion.moveToNext());
							} else {
								Manager.InsertarDatosConexion(util
										.getDateTime());
							}

							ContadorConexion = ContadorConexion + 1;
							if (ContadorConexion >= 3) {
								// Toast.makeText(getApplicationContext(),
								// "Cambiando conexion a Local",
								// Toast.LENGTH_SHORT).show();
								TipoConexion = "Local"; // Local Movil

								FechaHora = util.getDateTime();
								Fecha = FechaHora.substring(0, 10);
								Hora = FechaHora.substring(11,
										FechaHora.length());

//								Manager.InsertarDatosLog(
//										"Conexión con el servidor perdida, cambiado a BBDD local",
//										Fecha, Hora);
								// llamo a un hilo para que verifique que vuelva
								// la conexion
								Manager.ActualizarConexion(TipoConexion,
										ContadorConexion, util.getDateTime());
								new BuscarTask().execute();
							} else {
								TipoConexion = "Movil";
							}

							Manager.ActualizarConexion(TipoConexion,
									ContadorConexion, util.getDateTime());

							// contadorErroresSolicitud++;
							// if (contadorErroresSolicitud ==3){
							mpAviso.start();
							Toast.makeText(getApplicationContext(), Toast5,
									Toast.LENGTH_LONG).show();
							// }
							// else{
							// ConectarWsDataFuncionario(consulta1, consulta2);
							// }
						}
					}
				});
			}
		};

		nt.start();
	}

	public void Atras(View v) {
		Intent Inicio = new Intent(this, inicio.class);
		Vibrador.vibrate(80);
		// if ((_Lectura.equals("Funcionario")) ||
		// (_Lectura.equals("Vehiculo")))
		// {
		//
		// }
		if (_Lectura.equals("Pasajero")) {
			Manager.ActualizarPantalla("Pasajero");
		}

		if (_Lectura.equals("Chofer")) {
			Manager.ActualizarPantalla("Chofer");
		}
		finish();
		startActivity(Inicio);
	}

	public class BuscarTask extends AsyncTask<Void, Void, Void> {
		String _Conexion;
		String _TipoConexion = "";
		String _Respuesta;
		JSONObject _RespuestaJson;
		Exception _Exception = null;

		@Override
		protected void onPreExecute() {
			// mpAviso.start();

			CursorWs = Manager.CursorConfig();

			if (CursorWs.moveToFirst()) {
				do {
					NAMESPACE = CursorWs.getString(1);
					URL = CursorWs.getString(2);
					UsuarioWs = CursorWs.getString(3); // desarrollo
					ContraseniaWs = CursorWs.getString(4); // Desa1.
					Autentificacion = CursorWs.getString(5);
				} while (CursorWs.moveToNext());
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Dormmir 2 min
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			CursorConexion = Manager.CursorConexion();

			if (CursorConexion.moveToFirst()) {
				do {
					_TipoConexion = CursorConexion.getString(0);
				} while (CursorConexion.moveToNext());
			} else {
				_TipoConexion = "Movil";
			}

			METHOD_NAME = "WsValidarConexion";
			SOAP_ACTION = NAMESPACE + "WsValidarConexion";
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // la version de WS 1.1
			envelope.dotNet = true; // estamos utilizando .net
			envelope.setOutputSoapObject(request);
			HttpTransportSE transporte = new HttpTransportSE(URL);

			try {
				if (Autentificacion.equals("SI")) {
					// *****
					List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
					String Conexion = UsuarioWs + ":" + ContraseniaWs;
					headerList.add(new HeaderProperty("Authorization", "Basic "
							+ org.kobjects.base64.Base64.encode(Conexion
									.getBytes())));
					transporte.call(SOAP_ACTION, envelope, headerList);
					// /*****
				} else {
					transporte.call(SOAP_ACTION, envelope);
				}

				SoapPrimitive resultado = (SoapPrimitive) envelope
						.getResponse();

				_Respuesta = resultado.toString();
				_RespuestaJson = new JSONObject(_Respuesta);
				_Conexion = _RespuestaJson.getString("variable");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				_Exception = e;
				// e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				_Exception = e;
				// e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				_Exception = e;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			if (_TipoConexion.equals("Local")) {
				FechaHora = util.getDateTime();
				Fecha = FechaHora.substring(0, 10);
				Hora = FechaHora.substring(11, FechaHora.length());

				if (_Exception != null) {
					Manager.ActualizarConexion("Local", 0, util.getDateTime());
//					Manager.InsertarDatosLog(
//							"Imposible reconectar con el Servidor, Mantiene conexión BBDD Local",
//							Fecha, Hora);
					// me llamo a mi mismo para esperar otros 60 segundos
					new BuscarTask().execute();
				} else {
					if (_Conexion.equals("SI")) {
//						Manager.InsertarDatosLog(
//								"Conexión con el servidor Retomada, Cambiado a Conexión Móvil",
//								Fecha, Hora);
						// Toast.makeText(getApplicationContext(),
						// "Retomando Conexion Movil",
						// Toast.LENGTH_LONG).show();
						Manager.ActualizarConexion("Movil", 0,
								util.getDateTime());
					} else {
//						Manager.InsertarDatosLog(
//								"Imposible reconectar con el Servidor, Mantiene conexión BBDD Local",
//								Fecha, Hora);
						Manager.ActualizarConexion("Local", 0,
								util.getDateTime());
						// me llamo a mi mismo para esperar otros 60 segundos
						new BuscarTask().execute();
					}

				}
			}
		}
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	   if(keyCode == KeyEvent.KEYCODE_HOME)
	    {
	    // Log.i("Home Button","Clicked");
	    }
	   if(keyCode==KeyEvent.KEYCODE_BACK)
	   {
	        finish();
	   }
	 return false;
	}

}
