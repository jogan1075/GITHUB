package com.webcontrol.captura;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Sincronizacion extends Activity {

	Vibrator Vibrador;
	private DataBaseManager Manager;
	Cursor CursorSincronizacion, CursorSincronizacionINTRANET;
	String Rut, METHOD_NAME, SOAP_ACTION, NAMESPACE, URL, UsuarioWs,
			ContraseniaWs, Autentificacion, EstadoSync, TipoSync,
			TipoSincronizacion, Idioma, Toast1, Toast2, Toast3, Toast4, Toast5,
			Toast6, Toast7, Toast8, Toast9, Toast10, EstadoSyncIntranet,
			TipoSyncIntranet;
	int Progreso = -1;
	String METHOD_NAMEINTRANET, SOAP_ACTIONINTRANET, NAMESPACEINTRANET,
			URLINTRANET, UsuarioWsINTRANET, ContraseniaWsINTRANET,
			AutentificacionINTRANET, IdSyncINTRANET;
	private Cursor CursorWs, CursorWsIntranet;

	Button BtnSyncInicial, BtnSyncInicioDia, BtnSyncFinDia, BtnRegresar;
			
	ProgressBar progress;
	Button btnIntranet;
	TextView TxtSincronizacion;
	private HomeKeyLocker mHomeKeyLocker;

	String tipo_Conexion3gWIFI = "";
	String tipo = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.sincronizacion_layout);

		mHomeKeyLocker = new HomeKeyLocker();
		mHomeKeyLocker.lock(this);
		Manager = new DataBaseManager(this);
		Vibrador = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);

		progress = (ProgressBar) findViewById(R.id.progressSincronizacion);

		TxtSincronizacion = (TextView) findViewById(R.id.textViewSincronizacion);

		BtnSyncInicial = (Button) findViewById(R.id.buttonSyncInicial);
	
		BtnRegresar = (Button) findViewById(R.id.buttonMenuConfigRegresar);
		btnIntranet = (Button) findViewById(R.id.buttonIntranet);



		Cursor foto = Manager.CursorHABILITAR_FOTOS();
		if (foto.moveToFirst()) {
			Manager.ActualizarHABILITAR_FOTOS("NO");
		} else {
			Manager.InsertarHABILITAR_FOTOS("NO");
		}
		Cursor CursorIdioma = Manager.CursorIdioma();

		if (CursorIdioma.moveToFirst()) {
			Idioma = CursorIdioma.getString(0);
		}

		// 14 TOAST
		if (Idioma.equals("ESPANIOL")) {
			Textos("Sincronización Inicial Internet", "Regresar",
					"Configure el Servicio Web", "Error: aún faltan ",
					" por sincronizar", "Sincronización Finalizada",
					"Sincronizando Guardias... Restan ", " Registros",
					"Sincronizando Personas... Restan ",
					"Sincronizando Vehículos... Restan ",
					"Sincronizacion Finalizada con éxito",
					"Sincronizando Log... Restan ");
		} else if (Idioma.equals("INGLES")) {
			Textos("Initial Synchronization", "Back",
					"Configure the Web Service", "Error: ",
					" Still Missing Sync", "Synchronization Completed",
					"Synchronizing Guards ... Remaining  ", " Records",
					"Synchronizing Personas... Remain ",
					"Synchronizing Vehicles ... Remaining ",
					"Synchronization Completed successfully",
					"Synchronizing Log ... Remaining");
		} else if (Idioma.equals("PORTUGUES")) {
			Textos("Sincronização Inicial", "Voltar",
					"Configurar o Serviço Web", "Erro: ",
					" Sincronização Ainda Está Faltando",
					"Sincronização Concluída",
					"Guardas Sincronizando ... Restantes ", " Registros",
					"Pessoas Sincronizando ... Restantes ",
					"Veículos Sincronizando ... Restantes ",
					"Sincronização concluída com sucesso",
					"Log Sincronizando ... Restantes ");
		}

		CursorSincronizacion = Manager.CursorSincronizacionPersona();

		if (!(CursorSincronizacion.moveToFirst())) {
			
			Manager.InsertarDatosSincronizacion("0", "0", "1", "1", "0", 0,
			"OFF", "INICIAL");
//			Manager.InsertarDatosSincronizacion("0", "0", "440", "557486", "115304", 0,
//					"OFF", "INICIAL");

			EstadoSync = "OFF";
			TipoSync = "INICIAL";
		} else {
			do {
				EstadoSync = CursorSincronizacion.getString(3);
				TipoSync = CursorSincronizacion.getString(4);

			} while (CursorSincronizacion.moveToNext());
		}
		CursorSincronizacionINTRANET = Manager
				.CursorSincronizacionPersonaIntranet();

		if (!(CursorSincronizacionINTRANET.moveToFirst())) {
			Manager.InsertarDatosSincronizacionIntranet("0", "0", "1", "1",
					"0", 0, "OFF", "INICIAL");

			EstadoSyncIntranet = "OFF";
			TipoSyncIntranet = "INICIAL";
		} else {
			do {
				EstadoSyncIntranet = CursorSincronizacionINTRANET.getString(3);
				TipoSyncIntranet = CursorSincronizacionINTRANET.getString(4);

			} while (CursorSincronizacionINTRANET.moveToNext());
		}

		if ((TipoSync.equals("INICIAL")) && (EstadoSync.equals("OFF"))) {
			BtnSyncInicial.setTextColor(Color.WHITE);
			BtnSyncInicial.setEnabled(true);

		}

		CursorWs = Manager.CursorConfig();

		if (CursorWs.moveToFirst()) {
			do {
				NAMESPACE = CursorWs.getString(1);
				URL = CursorWs.getString(2);
				UsuarioWs = CursorWs.getString(3); // desarrollo
				ContraseniaWs = CursorWs.getString(4); // Desa1.
				Autentificacion = CursorWs.getString(5);
			} while (CursorWs.moveToNext());

		} else {
			Toast.makeText(getApplicationContext(), Toast1, Toast.LENGTH_LONG)
					.show();
			Intent i = new Intent(this, Configuracion.class);
			finish();
			startActivity(i);

		}

		CursorWsIntranet = Manager.CursorConfigIntranet();
		if (CursorWsIntranet.moveToFirst()) {
			NAMESPACEINTRANET = CursorWsIntranet.getString(1);
			URLINTRANET = CursorWsIntranet.getString(2);
			UsuarioWsINTRANET = CursorWsIntranet.getString(3); // desarrollo
			ContraseniaWsINTRANET = CursorWsIntranet.getString(4); // Desa1.
			AutentificacionINTRANET = CursorWsIntranet.getString(5);
		}

		Cursor cursorTipoConexion = Manager.CursorTipoConexion();
		if (cursorTipoConexion.moveToFirst()) {
			tipo = cursorTipoConexion.getString(0);

			if (tipo.equalsIgnoreCase("Internet")) {
				btnIntranet.setVisibility(View.INVISIBLE);
			
			} else {
				BtnSyncInicial.setVisibility(View.INVISIBLE);
				
			}
		}

	}

	private void Textos(String TextoSyncInicial, String TextoRegresar,
			String TextoToast1, String TextoToast2, String TextoToast3,
			String TextoToast4, String TextoToast5, String TextoToast6,
			String TextoToast7, String TextoToast8, String TextoToast9,
			String TextoToast10) {

		BtnSyncInicial.setText(TextoSyncInicial);
		BtnRegresar.setText(TextoRegresar);
		Toast1 = TextoToast1;
		Toast2 = TextoToast2;
		Toast3 = TextoToast3;
		Toast4 = TextoToast4;
		Toast5 = TextoToast5;
		Toast6 = TextoToast6;
		Toast7 = TextoToast7;
		Toast8 = TextoToast8;
		Toast9 = TextoToast9;
		Toast10 = TextoToast10;
	}

	public void SincronizacionInicial(View v)

	{
		Vibrador.vibrate(80);
		if (isOnline()) {
			Button BotonLocal = (Button) v;

			TipoSincronizacion = TipoSync;
			BotonLocal.setTextColor(Color.GRAY);
			BotonLocal.setEnabled(false);
			btnIntranet.setTextColor(Color.GRAY);
			btnIntranet.setEnabled(false);
			BtnRegresar.setTextColor(Color.GRAY);
			BtnRegresar.setEnabled(false);
		
			Manager.InsertarDatosLog("","Inicio Sincronización Inicial ",
					getFecha(), getHora());

//			Manager.InsertarDatosLog("Sincronización Vigilantes Iniciada ",
//					getFecha(), getHora());

			new HiloSincronizarGuardiaInicial().execute();

		} else {
			Toast.makeText(getApplicationContext(),
					"Error Conexión a Internet", Toast.LENGTH_SHORT).show();
		}

	}



	public void SincronizacionInicialIntranet(View v) {
		Vibrador.vibrate(80);
		if (isOnline()) {
			Button BotonLocal = (Button) v;

			TipoSincronizacion = TipoSync;
			BotonLocal.setTextColor(Color.GRAY);
			BotonLocal.setEnabled(false);
			BtnRegresar.setTextColor(Color.GRAY);
			BtnRegresar.setEnabled(false);
			BtnSyncInicial.setTextColor(Color.GRAY);
			BtnSyncInicial.setEnabled(false);
		

			
				Manager.InsertarDatosLog("",
						"Inicio Sincronización Inicial Intranet", getFecha(),
						getHora());

		

				new HiloSincronizarGuardiaInicial().execute();

		} else {
			Toast.makeText(getApplicationContext(),
					"Error Conexión a Internet", Toast.LENGTH_SHORT).show();
		}
	}

	

	public void Regresar(View v) {
		Vibrador.vibrate(80);
		Intent i = new Intent(this, ConfiguracionAvanzada.class);
		finish();
		startActivity(i);
	}

	public String getIMEI() {
		TelephonyManager phonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String id = phonyManager.getDeviceId();
		if (id == null) {
			id = "not available";
		}

		int phoneType = phonyManager.getPhoneType();
		switch (phoneType) {
		case TelephonyManager.PHONE_TYPE_NONE:
			return "NONE: " + id;
		}

		return id;
	}

	@Override
	public void onBackPressed() {

	}

	public boolean isOnline() {
		boolean enabled = true;

		ConnectivityManager connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();

		if ((info == null || !info.isConnected() || !info.isAvailable())) {
			enabled = false;
		}
		return enabled;
	}

	private String getFecha() {
		String FechaHora = getDateTime();
		String Fecha = FechaHora.substring(0, 10);
		String Hora = FechaHora.substring(11, FechaHora.length());
		return Fecha;
	}

	private String getHora() {
		String FechaHora = getDateTime();
		String Fecha = FechaHora.substring(0, 10);
		String Hora = FechaHora.substring(11, FechaHora.length());
		return Hora;
	}

	private String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_HOME) {
			// Log.i("Home Button","Clicked");
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return false;
	}

	/********************************************* HILOS INTERNET *****************************************************/

	private class HiloSincronizarGuardiaInicial extends
			AsyncTask<Void, Void, Void> {
		String UID = "";
		String EstadoUID = "";
		String IdFuncionario = "";
		String Nombres = "";
		String Apellidos = "";
		String NombreEmpresa = "";
		String IdEmpresa = "";
		String Autorizacion = "";
		String Estado = "";
		String Mensaje = "";
		String Imagen = "";
		int ContadorMax = 0;
		int ContadorErrores;
		String _Respuesta;
		JSONObject _RespuestaJson;
		Exception _Exception = null;
		String Restantes="";
		String IdSync = "";

		@Override
		protected void onPreExecute() {
			
			Cursor CursorTipoConexion = Manager.CursorTipoConexion();
			if (CursorTipoConexion.moveToFirst()) {
				tipo_Conexion3gWIFI = CursorTipoConexion.getString(0);
			}
			if (tipo_Conexion3gWIFI.equalsIgnoreCase("INTERNET")) {
				Cursor CursorSincronizacion = Manager.CursorSincronizacionGuardia();

				if (CursorSincronizacion.moveToFirst()) {
					do {
						IdSync = CursorSincronizacion.getString(0);
						ContadorErrores = CursorSincronizacion.getInt(1);

					} while (CursorSincronizacion.moveToNext());

				}
			} else if (tipo_Conexion3gWIFI.equalsIgnoreCase("INTRANET")) {
				Cursor CursorSincronizacion = Manager
						.CursorSincronizacionGuardiaIntranet();

				if (CursorSincronizacion.moveToFirst()) {
					do {
						IdSync = CursorSincronizacion.getString(0);
						ContadorErrores = CursorSincronizacion.getInt(1);

					} while (CursorSincronizacion.moveToNext());

				}
			}
			
			
//			CursorSincronizacion = Manager.CursorSincronizacionGuardia();
//
//			if (CursorSincronizacion.moveToFirst()) {
//				do {
//					IdSync = CursorSincronizacion.getString(0);
//					ContadorErrores = CursorSincronizacion.getInt(1);
//
//				} while (CursorSincronizacion.moveToNext());
//
//			}
		}

		@Override
		protected Void doInBackground(Void... params) {

			METHOD_NAME = "WSSincronizarGuardiaInicial";
			SOAP_ACTION = NAMESPACE + "WSSincronizarGuardiaInicial";
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

			request.addProperty("idsincronizacion", IdSync);
			request.addProperty("imei", getIMEI());

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

				UID = _RespuestaJson.getString("UID");
				EstadoUID = _RespuestaJson.getString("EstadoUID");
				IdFuncionario = _RespuestaJson.getString("IdFuncionario"); // 081493251
				Nombres = _RespuestaJson.getString("Nombres"); // MIGUEL ANTONIO
				Apellidos = _RespuestaJson.getString("Apellidos");
				NombreEmpresa = _RespuestaJson.getString("NombreEmpresa");
				IdEmpresa = _RespuestaJson.getString("IdEmpresa");
				Autorizacion = _RespuestaJson.getString("Autorizacion");
				Mensaje = _RespuestaJson.getString("Mensaje");
				IdSync = _RespuestaJson.getString("idsincronizacion");
				Restantes = _RespuestaJson.getString("restantes");

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

			if (!Restantes.equals("null")) {

				if (_Exception == null) {
					TxtSincronizacion.setText(Toast5 + Restantes + Toast6);

					if (Integer.parseInt(IdSync) != 0) {

						Manager.ActualizarSincronizacionGuardia(IdSync, 0,
								"ON", "INICIAL");

						int id = Manager.devolveridGuardia(IdFuncionario);
						Cursor CursorFuncionarioGuardia;
						CursorFuncionarioGuardia = Manager
								.BuscarGuardiaId(IdFuncionario);// 081493251
						if (CursorFuncionarioGuardia.moveToFirst()) {
							// actualizamos
							Manager.ActualizarDataGuardia(UID, EstadoUID,
									IdFuncionario, Nombres, Apellidos,
									NombreEmpresa, IdEmpresa, Autorizacion,
									Imagen, id);
						} else {
							Manager.InsertarDatosGuardia(UID, EstadoUID,
									IdFuncionario, Nombres, Apellidos,
									NombreEmpresa, IdEmpresa, Autorizacion,
									Imagen);
						}
						//
					} else {
						if(!IdSync.equals("0")){
						Manager.ActualizarSincronizacionGuardia(IdSync, 0,
								"OFF", "INICIAL");
						}

					}
					new HiloSincronizarGuardiaInicial().execute();
				} else {
					if (ContadorErrores >= 5) {

						Manager.ActualizarContadorSincronizacion(0, "OFF",
								"INICIAL");
						BtnSyncInicial.setTextColor(Color.WHITE);
						BtnSyncInicial.setEnabled(true);
//						Manager.InsertarDatosLog(
//								"Error: Sincronización Inicial Vigilantes ",
//								getFecha(), getHora());

						BtnRegresar.setTextColor(Color.WHITE);
						BtnRegresar.setEnabled(true);

						progress.setVisibility(View.INVISIBLE);
						TxtSincronizacion.setText(Toast2 + Restantes
								+ Toast3);

					} else {
						if (TipoSincronizacion.equals("INICIAL")) {
							Manager.ActualizarContadorSincronizacion(
									(ContadorErrores + 1), "ON", "INICIAL");
						}

						new HiloSincronizarGuardiaInicial().execute();
					}
				}
			} else {
				Toast.makeText(getApplicationContext(),
						"Fin Sincronización Vigilantes", Toast.LENGTH_SHORT)
						.show();
				new HiloSincronizarGuardiaPendientes().execute();
			}
			

		}

	}

	private class HiloSincronizacionPersonasInicial extends
			AsyncTask<Void, Void, Void> {

		String UID = "";
		String EstadoUID = "";
		String IdFuncionario = "";
		String Nombres = "";
		String Apellidos = "";
		String NombreEmpresa = "";
		String IdEmpresa = "";
		String Ost = "", CCosto = "", TipoPase = "";
		String Autorizacion = "";
		String FechaConsulta = "";
		String AutorizacionConductor = "";
		String Estado = "";
		String Mensaje = "";
		String Imagen = "";
		int ContadorMax = 0;
		int ContadorErrores;
		String Restantes="";
		String Respuesta="";
		JSONObject RespuestaJson;
		Exception Exception = null;
		String IdSync = "";

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// super.onPreExecute();

			Cursor CursorTipoConexion = Manager.CursorTipoConexion();
			if (CursorTipoConexion.moveToFirst()) {
				tipo_Conexion3gWIFI = CursorTipoConexion.getString(0);
			}


			if (tipo_Conexion3gWIFI.equalsIgnoreCase("INTERNET")) {
				Cursor CursorSincronizacion = Manager
						.CursorSincronizacionPersona();

				if (CursorSincronizacion.moveToFirst()) {
					do {
						IdSync = CursorSincronizacion.getString(1);
						ContadorErrores = CursorSincronizacion.getInt(2);

					} while (CursorSincronizacion.moveToNext());

				}				
			} else {
				Cursor CursorSincronizacion = Manager
						.CursorSincronizacionPersonaIntranet();

				if (CursorSincronizacion.moveToFirst()) {
					do {
						IdSync = CursorSincronizacion.getString(1);
						ContadorErrores = CursorSincronizacion.getInt(2);

					} while (CursorSincronizacion.moveToNext());

				}

			}
//			Cursor CursorSincronizacionPersona = Manager
//					.CursorSincronizacionPersona();
//
//			if (CursorSincronizacionPersona.moveToFirst()) {
//				do {
//					IdSync = CursorSincronizacionPersona.getString(1);
//
//					ContadorErrores = CursorSincronizacionPersona.getInt(2);
//
//				} while (CursorSincronizacionPersona.moveToNext());
//			}

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			METHOD_NAME = "WSSincronizaInicial";
			SOAP_ACTION = NAMESPACE + "WSSincronizaInicial";
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

			request.addProperty("idsincronizacion", IdSync);
			request.addProperty("imei", getIMEI());

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

				Respuesta = resultado.toString();
				RespuestaJson = new JSONObject(Respuesta);

				UID = RespuestaJson.getString("UID");
				EstadoUID = RespuestaJson.getString("EstadoUID");
				IdFuncionario = RespuestaJson.getString("IdFuncionario");
				Nombres = RespuestaJson.getString("Nombres");
				Apellidos = RespuestaJson.getString("Apellidos");
				NombreEmpresa = RespuestaJson.getString("NombreEmpresa");
				IdEmpresa = RespuestaJson.getString("IdEmpresa");
				Autorizacion = RespuestaJson.getString("Autorizacion");
				FechaConsulta = RespuestaJson.getString("FechaConsulta");
				AutorizacionConductor = RespuestaJson
						.getString("AutorizacionConductor");
				Estado = RespuestaJson.getString("Estado");
				Mensaje = RespuestaJson.getString("Mensaje");
				Imagen = RespuestaJson.getString("Imagen");
				Ost = RespuestaJson.getString("Ost");
				TipoPase = RespuestaJson.getString("TipoPase");
				CCosto = RespuestaJson.getString("CCosto");
				// ContadorMax = RespuestaJson.getInt("max");
				IdSync = RespuestaJson.getString("idsincronizacion");
				Restantes = RespuestaJson.getString("restantes");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Exception = e;
				// e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				Exception = e;
				// e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Exception = e;
				//
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub

			if (!Restantes.equals("null")) {

				int datosrestantes = (Integer.parseInt(Restantes));

				if (Exception == null) {
					TxtSincronizacion
							.setText("Sincronizando Personas.... Restan "
									+ datosrestantes + " Registros");

					if (Integer.parseInt(IdSync) != 0) {
						if (TipoSincronizacion.equals("INICIAL")) {
							Manager.ActualizarSincronizacionPersona(
									IdFuncionario, IdSync, 0, "ON", "INICIAL");
						}

						Cursor CursorFuncionario = Manager
								.BuscarFuncionarioId(IdFuncionario);
						if (CursorFuncionario.moveToFirst()) {
							int id = Manager
									.devolveridFuncionario(IdFuncionario);
							Manager.ActualizarDataFuncionario(UID, EstadoUID,
									IdFuncionario, Nombres, Apellidos,
									NombreEmpresa, IdEmpresa, Ost, CCosto,
									TipoPase, Imagen, Autorizacion,
									AutorizacionConductor, FechaConsulta, id);
						} else {
							Manager.InsertarDatosFuncionario(UID, EstadoUID,
									IdFuncionario, Nombres, Apellidos,
									NombreEmpresa, IdEmpresa, Ost, CCosto,
									TipoPase, Imagen, Autorizacion,
									AutorizacionConductor, FechaConsulta);
						}

					} else {
						if(!IdSync.equals("0")){
						Manager.ActualizarSincronizacionPersona(IdFuncionario,
								IdSync, 0, "OFF", "INICIAL");
						}

					}
					new HiloSincronizacionPersonasInicial().execute();
				} else {
					if (ContadorErrores >= 5) {

//						Manager.InsertarDatosLog(
//								"Error: Sincronización Inicial Personas ",
//								getFecha(), getHora());
						Manager.ActualizarContadorSincronizacion(0, "OFF",
								"INICIAL");
						BtnSyncInicial.setTextColor(Color.WHITE);
						BtnSyncInicial.setEnabled(true);

						BtnRegresar.setTextColor(Color.WHITE);
						BtnRegresar.setEnabled(true);

						TxtSincronizacion
								.setText("Sincronizando Personas.... Restan "
										+ datosrestantes + " Registros");

					} else {

						Manager.ActualizarContadorSincronizacion(
								(ContadorErrores + 1), "ON", "INICIAL");

						new HiloSincronizacionPersonasInicial().execute();
					}
				}
			} else {

				Toast.makeText(getApplicationContext(),
						"Fin Sincronización Personas", Toast.LENGTH_SHORT)
						.show();
				
				new HiloSincronizarPersonasPendientes().execute();
			}

		}

	}

	private class HiloSincronizarVehiculos extends AsyncTask<Void, Void, Void> {
		String UID = "";
		String EstadoUID = "";
		String IdVehiculo = "";
		String Marca = "";
		String Modelo = "";
		String AnioVehiculo = "";
		String TipoVehiculo = "";
		String AutorizacionVehiculo = "";
		String FechaConsulta = "";
		String ROLEmpresa = "";
		String NombreEmpresa = "";
		String Fecha = "";
		String Estado = "";
		String Mensaje = "";
		int ContadorMax = 0;
		String Restantes="";
		String idsync = "";

		int ContadorErrores;
		String _Respuesta="";
		JSONObject _RespuestaJson;
		Exception _Exception = null;

		@Override
		protected void onPreExecute() {

			
			Cursor CursorTipoConexion = Manager.CursorTipoConexion();
			if (CursorTipoConexion.moveToFirst()) {
				tipo_Conexion3gWIFI = CursorTipoConexion.getString(0);
			}
			if (tipo_Conexion3gWIFI.equalsIgnoreCase("INTERNET")) {
				Cursor CursorSincronizacion = Manager
						.CursorSincronizacionVehiculo();

				if (CursorSincronizacion.moveToFirst()) {
					do {
						idsync = CursorSincronizacion.getString(1);
						ContadorErrores = CursorSincronizacion.getInt(2);

					} while (CursorSincronizacion.moveToNext());

				}
			} else {
				Cursor CursorSincronizacion = Manager
						.CursorSincronizacionVehiculoIntranet();

				if (CursorSincronizacion.moveToFirst()) {
					do {
						idsync = CursorSincronizacion.getString(1);
						ContadorErrores = CursorSincronizacion.getInt(2);

					} while (CursorSincronizacion.moveToNext());

				}
			}
//			Cursor CursorSincronizacion = Manager
//					.CursorSincronizacionVehiculo();
//
//			if (CursorSincronizacion.moveToFirst()) {
//				do {
//					idsync = CursorSincronizacion.getString(1);
//					ContadorErrores = CursorSincronizacion.getInt(2);
//
//				} while (CursorSincronizacion.moveToNext());
//			}
		}

		@Override
		protected Void doInBackground(Void... params) {

			METHOD_NAME = "WSSincronizarVehiculosInicial";
			SOAP_ACTION = NAMESPACE + "WSSincronizarVehiculosInicial";
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

			request.addProperty("IdSincronizacion", idsync);
			request.addProperty("imei", getIMEI());

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

				UID = _RespuestaJson.getString("UID");
				EstadoUID = _RespuestaJson.getString("EstadoUID");
				IdVehiculo = _RespuestaJson.getString("IdVehiculo");
				Marca = _RespuestaJson.getString("Marca");
				Modelo = _RespuestaJson.getString("Modelo");
				AnioVehiculo = _RespuestaJson.getString("AnioVehiculo");
				TipoVehiculo = _RespuestaJson.getString("TipoVehiculo");
				AutorizacionVehiculo = _RespuestaJson.getString("Autorizacion");
				ROLEmpresa = _RespuestaJson.getString("ROLEmpresa");
				NombreEmpresa = _RespuestaJson.getString("NombreEmpresa");
				FechaConsulta = _RespuestaJson.getString("Fecha");
				Estado = _RespuestaJson.getString("Estado");
				Mensaje = _RespuestaJson.getString("Mensaje");
				Restantes = _RespuestaJson.getString("restantes");
				idsync = _RespuestaJson.getString("idsincronizacion");

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

			if (!Restantes.equals("null")) {

				int datosrestantes = (Integer.parseInt(Restantes));

				if (_Exception == null) {
					TxtSincronizacion
							.setText("Sincronizando Vehiculos.... Restan "
									+ datosrestantes + " Registros");
					if (Integer.parseInt(idsync) != 0) {

						Manager.ActualizarSincronizacionVehiculo(IdVehiculo,
								idsync, 0, "ON", "INICIAL");

						Cursor CursorVehiculo = Manager
								.BuscarVehiculoId(IdVehiculo);
						if (CursorVehiculo.moveToFirst()) {
							int identificadorvehiculo = CursorVehiculo
									.getInt(0);
							// actualizamos
							Manager.ActualizarDataVehiculo(
									identificadorvehiculo, UID, EstadoUID,
									IdVehiculo, Marca, Modelo, AnioVehiculo,
									TipoVehiculo, NombreEmpresa, ROLEmpresa,
									"0", AutorizacionVehiculo, FechaConsulta,
									Mensaje);
						} else {
							Manager.InsertarDatosVehiculo(UID, EstadoUID,
									IdVehiculo, Marca, Modelo, AnioVehiculo,
									TipoVehiculo, NombreEmpresa, ROLEmpresa,
									"0", AutorizacionVehiculo, FechaConsulta,
									Mensaje);
						}
						
					} else {
						if(!idsync.equals("0")){
						Manager.ActualizarSincronizacionVehiculo(IdVehiculo,
								idsync, 0, "OFF", "INICIAL");
						}

					}
					new HiloSincronizarVehiculos().execute();
				} else {
					if (ContadorErrores >= 5) {

						Manager.ActualizarContadorSincronizacion(0, "OFF",
								"INICIAL");
						BtnSyncInicial.setTextColor(Color.WHITE);
						BtnSyncInicial.setEnabled(true);
//						Manager.InsertarDatosLog(
//								"Error: Sincronización Inicial Vehiculos",
//								getFecha(), getHora());

						BtnRegresar.setTextColor(Color.WHITE);
						BtnRegresar.setEnabled(true);

						TxtSincronizacion
								.setText("Sincronizando Vehiculos.... Restan "
										+ datosrestantes + " Registros");

					} else {
						if (TipoSincronizacion.equals("INICIAL")) {
							Manager.ActualizarContadorSincronizacion(
									(ContadorErrores + 1), "ON", "INICIAL");
						}

						
					}
				}
			} else {
				Toast.makeText(getApplicationContext(),
						"Fin Sincronización Vehiculos", Toast.LENGTH_SHORT)
						.show();

				new HiloSincronizarVehiculosPendientes().execute();
				
			}
	
		}

	}

	

	/********************************************* HILOS PERSONAS PENDIENTES *****************************************************/

	private class HiloSincronizarPersonasPendientes extends
			AsyncTask<Void, Void, Void> {
		String UID = "";
		String EstadoUID = "";
		String IdFuncionario = "";
		String Nombres = "";
		String Apellidos = "";
		String NombreEmpresa = "";
		String IdEmpresa = "";
		String Autorizacion = "";
		String FechaConsulta = "";
		String AutorizacionConductor = "";
		String Estado = "";
		String Mensaje = "", Restantes = "";
		String Imagen = "";
		String Ost = "", CCosto = "", TipoPase = "";
		int ContadorMax = 0;
		int ContadorErrores;
		String _Respuesta="";
		String id = "";

		JSONObject _RespuestaJson;
		Exception _Exception = null;

		@Override
		protected void onPreExecute() {

			
			Cursor CursorTipoConexion = Manager.CursorTipoConexion();
			if (CursorTipoConexion.moveToFirst()) {
				tipo_Conexion3gWIFI = CursorTipoConexion.getString(0);
			}


			if (tipo_Conexion3gWIFI.equalsIgnoreCase("INTERNET")) {
				Cursor CursorSincronizacion = Manager
						.CursorSincronizacionPersona();

				if (CursorSincronizacion.moveToFirst()) {
					do {
						id = CursorSincronizacion.getString(1);
						ContadorErrores = CursorSincronizacion.getInt(2);

					} while (CursorSincronizacion.moveToNext());

				}				
			} else {
				Cursor CursorSincronizacion = Manager
						.CursorSincronizacionPersonaIntranet();

				if (CursorSincronizacion.moveToFirst()) {
					do {
						id = CursorSincronizacion.getString(1);
						ContadorErrores = CursorSincronizacion.getInt(2);

					} while (CursorSincronizacion.moveToNext());

				}

			}
//			Cursor CursorSincronizacionPersona = Manager
//					.CursorSincronizacionPersona();
//
//			if (CursorSincronizacionPersona.moveToFirst()) {
//				do {
//					id = CursorSincronizacionPersona.getString(1);
//
//					ContadorErrores = CursorSincronizacionPersona.getInt(2);
//
//				} while (CursorSincronizacionPersona.moveToNext());
//			}
		}

		@Override
		protected Void doInBackground(Void... params) {

			METHOD_NAME = "WSSincronizar";
			SOAP_ACTION = NAMESPACE + "WSSincronizar";
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

			request.addProperty("idsincronizacion", id);
			request.addProperty("imei", getIMEI());

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

				UID = _RespuestaJson.getString("UID");
				EstadoUID = _RespuestaJson.getString("EstadoUID");
				IdFuncionario = _RespuestaJson.getString("IdFuncionario");
				Nombres = _RespuestaJson.getString("Nombres"); // MIGUEL ANTONIO
				Apellidos = _RespuestaJson.getString("Apellidos");
				NombreEmpresa = _RespuestaJson.getString("NombreEmpresa");
				IdEmpresa = _RespuestaJson.getString("IdEmpresa");
				Autorizacion = _RespuestaJson.getString("Autorizacion");
				FechaConsulta = _RespuestaJson.getString("FechaConsulta");
				AutorizacionConductor = _RespuestaJson
						.getString("AutorizacionConductor");
				Estado = _RespuestaJson.getString("Estado");
				Mensaje = _RespuestaJson.getString("Mensaje");
				Imagen = _RespuestaJson.getString("Imagen");
				Ost = _RespuestaJson.getString("Ost");
				TipoPase = _RespuestaJson.getString("TipoPase");
				CCosto = _RespuestaJson.getString("CCosto");
				// ContadorMax = _RespuestaJson.getInt("max");
				id = _RespuestaJson.getString("idsincronizacion");
				Restantes = _RespuestaJson.getString("restantes");

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

			if (!Restantes.equals("null")) {

				int datosrestantes = (Integer.parseInt(Restantes));

				if (_Exception == null) {
					TxtSincronizacion
							.setText("Sincronizando Personas.... Restan "
									+ datosrestantes + " Registros");

					if (Integer.parseInt(id) != 0) {
						if (TipoSincronizacion.equals("INICIAL")) {
							Manager.ActualizarSincronizacionPersona(
									IdFuncionario, id, 0, "ON", "INICIAL");
						}

						Cursor CursorFuncionario = Manager
								.BuscarFuncionarioId(IdFuncionario);
						if (CursorFuncionario.moveToFirst()) {

							int id = Manager
									.devolveridFuncionario(IdFuncionario);
							Manager.ActualizarDataFuncionario(UID, EstadoUID,
									IdFuncionario, Nombres, Apellidos,
									NombreEmpresa, IdEmpresa, Ost, CCosto,
									TipoPase, Imagen, Autorizacion,
									AutorizacionConductor, FechaConsulta, id);
						} else {
							Manager.InsertarDatosFuncionario(UID, EstadoUID,
									IdFuncionario, Nombres, Apellidos,
									NombreEmpresa, IdEmpresa, Ost, CCosto,
									TipoPase, Imagen, Autorizacion,
									AutorizacionConductor, FechaConsulta);
						}

					} else {
						if(!id.equals("0")){
						Manager.ActualizarSincronizacionPersona(IdFuncionario,
								id, 0, "OFF", "INICIAL");
						}

					}
					new HiloSincronizarPersonasPendientes().execute();
				} else {
					if (ContadorErrores >= 5) {

//						Manager.InsertarDatosLog(
//								"Error: Sincronización Inicial Personas ",
//								getFecha(), getHora());
						Manager.ActualizarContadorSincronizacion(0, "OFF",
								"INICIAL");
						BtnSyncInicial.setTextColor(Color.WHITE);
						BtnSyncInicial.setEnabled(true);

						BtnRegresar.setTextColor(Color.WHITE);
						BtnRegresar.setEnabled(true);

						TxtSincronizacion
								.setText("Sincronizando Personas.... Restan "
										+ datosrestantes + " Registros");

					} else {

						Manager.ActualizarContadorSincronizacion(
								(ContadorErrores + 1), "ON", "INICIAL");

						new HiloSincronizarPersonasPendientes().execute();
					}
				}
			} else {

				Toast.makeText(getApplicationContext(),
						"Fin Sincronización Personas", Toast.LENGTH_SHORT)
						.show();
				new HiloSincronizarVehiculos().execute();

			}

		}

	}

	public class HiloSincronizarVehiculosPendientes extends
			AsyncTask<Void, Void, Void> {
		String UID = "";
		String EstadoUID = "";
		String IdVehiculo = "";
		String Marca = "";
		String Modelo = "";
		String AnioVehiculo = "";
		String TipoVehiculo = "";
		String AutorizacionVehiculo = "";
		String FechaConsulta = "";
		String ROLEmpresa = "";
		String NombreEmpresa = "";
		String Fecha = "";
		String Estado = "";
		String Mensaje = "";
		int ContadorMax = 0;
		String Restantes="";
		String id = "";

		int ContadorErrores;
		String _Respuesta="";
		JSONObject _RespuestaJson;
		Exception _Exception = null;

		String Rut, IdSync,		 EstadoSync,
				TipoSync, TipoSincronizacion, Idioma, Toast1, Toast2, Toast3,
				Toast4, Toast5, Toast6, Toast7, Toast8, Toast9, Toast10;

		String tipo_Conexion3gWIFI = "";

		@Override
		protected void onPreExecute() {
			

			Cursor CursorTipoConexion = Manager.CursorTipoConexion();
			if (CursorTipoConexion.moveToFirst()) {
				tipo_Conexion3gWIFI = CursorTipoConexion.getString(0);
			}
			if (tipo_Conexion3gWIFI.equalsIgnoreCase("INTERNET")) {
				Cursor CursorSincronizacion = Manager
						.CursorSincronizacionVehiculo();

				if (CursorSincronizacion.moveToFirst()) {
					do {
						id = CursorSincronizacion.getString(1);
						ContadorErrores = CursorSincronizacion.getInt(2);

					} while (CursorSincronizacion.moveToNext());

				}
			} else {
				Cursor CursorSincronizacion = Manager
						.CursorSincronizacionVehiculoIntranet();

				if (CursorSincronizacion.moveToFirst()) {
					do {
						id = CursorSincronizacion.getString(1);
						ContadorErrores = CursorSincronizacion.getInt(2);

					} while (CursorSincronizacion.moveToNext());

				}
			}
//			Cursor CursorSincronizacion = Manager
//					.CursorSincronizacionVehiculo();
//
//			if (CursorSincronizacion.moveToFirst()) {
//				do {
//					id = CursorSincronizacion.getString(1);
//					ContadorErrores = CursorSincronizacion.getInt(2);
//
//				} while (CursorSincronizacion.moveToNext());
//			}
		}

		@Override
		protected Void doInBackground(Void... params) {

			METHOD_NAME = "WSSincronizarVehiculos";
			SOAP_ACTION = NAMESPACE + "WSSincronizarVehiculos";
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

			request.addProperty("IdSincronizacion", id);
			request.addProperty("imei", getIMEI());

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

				UID = _RespuestaJson.getString("UID");
				EstadoUID = _RespuestaJson.getString("EstadoUID");
				IdVehiculo = _RespuestaJson.getString("IdVehiculo");
				Marca = _RespuestaJson.getString("Marca");
				Modelo = _RespuestaJson.getString("Modelo");
				AnioVehiculo = _RespuestaJson.getString("AnioVehiculo");
				TipoVehiculo = _RespuestaJson.getString("TipoVehiculo");
				AutorizacionVehiculo = _RespuestaJson.getString("Autorizacion");
				ROLEmpresa = _RespuestaJson.getString("ROLEmpresa");
				NombreEmpresa = _RespuestaJson.getString("NombreEmpresa");
				FechaConsulta = _RespuestaJson.getString("Fecha");
				Estado = _RespuestaJson.getString("Estado");
				Mensaje = _RespuestaJson.getString("Mensaje");
				Restantes = _RespuestaJson.getString("restantes");
				IdSync = _RespuestaJson.getString("idsincronizacion");

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
			if (!Restantes.equals("null")) {

				int datosrestantes = (Integer.parseInt(Restantes));

				if (_Exception == null) {
					TxtSincronizacion
							.setText("Sincronizando Vehiculos.... Restan "
									+ datosrestantes + " Registros");
					if (Integer.parseInt(id) != 0) {

						Manager.ActualizarSincronizacionVehiculo(IdVehiculo,
								id, 0, "ON", "INICIAL");

						Cursor CursorVehiculo = Manager
								.BuscarVehiculoId(IdVehiculo);
						if (CursorVehiculo.moveToFirst()) {
							int identificadorvehiculo = CursorVehiculo
									.getInt(0);
							// actualizamos
							Manager.ActualizarDataVehiculo(
									identificadorvehiculo, UID, EstadoUID,
									IdVehiculo, Marca, Modelo, AnioVehiculo,
									TipoVehiculo, NombreEmpresa, ROLEmpresa,
									"0", AutorizacionVehiculo, FechaConsulta,
									Mensaje);
						} else {
							Manager.InsertarDatosVehiculo(UID, EstadoUID,
									IdVehiculo, Marca, Modelo, AnioVehiculo,
									TipoVehiculo, NombreEmpresa, ROLEmpresa,
									"0", AutorizacionVehiculo, FechaConsulta,
									Mensaje);
						}
					
					} else {
						if(!id.equals("0")){
						Manager.ActualizarSincronizacionVehiculo(IdVehiculo,
								id, 0, "OFF", "INICIAL");

						}
					}
					new HiloSincronizarVehiculosPendientes().execute();
				} else {
					if (ContadorErrores >= 5) {

						Manager.ActualizarContadorSincronizacion(0, "OFF",
								"INICIAL");
						BtnSyncInicial.setTextColor(Color.WHITE);
						BtnSyncInicial.setEnabled(true);
//						Manager.InsertarDatosLog(
//								"Error: Sincronización Inicial Vehiculos",
//								getFecha(), getHora());

						BtnRegresar.setTextColor(Color.WHITE);
						BtnRegresar.setEnabled(true);

						TxtSincronizacion
								.setText("Sincronizando Vehiculos.... Restan "
										+ datosrestantes + " Registros");

					} else {
						if (TipoSincronizacion.equals("INICIAL")) {
							Manager.ActualizarContadorSincronizacion(
									(ContadorErrores + 1), "ON", "INICIAL");
						}

						new HiloSincronizarVehiculosPendientes().execute();
					}
				}
			} else {
				Toast.makeText(getApplicationContext(),
						"Fin Sincronización Vehiculos Pendientes",
						Toast.LENGTH_SHORT).show();

				TxtSincronizacion.setVisibility(View.INVISIBLE);

				BtnSyncInicial.setEnabled(true);
				BtnSyncInicial.setTextColor(Color.WHITE);

				btnIntranet.setEnabled(true);
				btnIntranet.setTextColor(Color.WHITE);

				BtnRegresar.setEnabled(true);
				BtnRegresar.setTextColor(Color.WHITE);

				Cursor foto = Manager.CursorHABILITAR_FOTOS();
				if (foto.moveToFirst()) {
					Manager.ActualizarHABILITAR_FOTOS("SI");
				} else {
					Manager.InsertarHABILITAR_FOTOS("SI");
				}

				startActivity(new Intent(Sincronizacion.this,
						ConfiguracionAvanzada.class));
				finish();

			}

		}
	}

	private class HiloSincronizarGuardiaPendientes extends
			AsyncTask<Void, Void, Void> {
		String UID = "";
		String EstadoUID = "";
		String IdFuncionario = "";
		String Nombres = "";
		String Apellidos = "";
		String NombreEmpresa = "";
		String IdEmpresa = "";
		String Autorizacion = "";
		String Estado = "";
		String Mensaje = "";
		String Imagen = "";
		int ContadorMax = 0;
		int ContadorErrores;
		String _Respuesta="";
		JSONObject _RespuestaJson;
		Exception _Exception = null;
		String Restantes="";
		String IdSync = "";

		@Override
		protected void onPreExecute() {
			
			Cursor CursorTipoConexion = Manager.CursorTipoConexion();
			if (CursorTipoConexion.moveToFirst()) {
				tipo_Conexion3gWIFI = CursorTipoConexion.getString(0);
			}
			if (tipo_Conexion3gWIFI.equalsIgnoreCase("INTERNET")) {
				Cursor CursorSincronizacion = Manager.CursorSincronizacionGuardia();

				if (CursorSincronizacion.moveToFirst()) {
					do {
						IdSync = CursorSincronizacion.getString(0);
						ContadorErrores = CursorSincronizacion.getInt(1);

					} while (CursorSincronizacion.moveToNext());

				}
			} else if (tipo_Conexion3gWIFI.equalsIgnoreCase("INTRANET")) {
				Cursor CursorSincronizacion = Manager
						.CursorSincronizacionGuardiaIntranet();

				if (CursorSincronizacion.moveToFirst()) {
					do {
						IdSync = CursorSincronizacion.getString(0);
						ContadorErrores = CursorSincronizacion.getInt(1);

					} while (CursorSincronizacion.moveToNext());

				}
			}
			
//			CursorSincronizacion = Manager.CursorSincronizacionGuardia();
//
//			if (CursorSincronizacion.moveToFirst()) {
//				do {
//					IdSync = CursorSincronizacion.getString(0);
//					ContadorErrores = CursorSincronizacion.getInt(1);
//
//				} while (CursorSincronizacion.moveToNext());
//
//			}
		}

		@Override
		protected Void doInBackground(Void... params) {

			METHOD_NAME = "WSSincronizarGuardia";
			SOAP_ACTION = NAMESPACE + "WSSincronizarGuardia";
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

			request.addProperty("idsincronizacion", IdSync);
			request.addProperty("imei", getIMEI());

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

				UID = _RespuestaJson.getString("UID");
				EstadoUID = _RespuestaJson.getString("EstadoUID");
				IdFuncionario = _RespuestaJson.getString("IdFuncionario"); // 081493251
				Nombres = _RespuestaJson.getString("Nombres"); // MIGUEL ANTONIO
				Apellidos = _RespuestaJson.getString("Apellidos");
				NombreEmpresa = _RespuestaJson.getString("NombreEmpresa");
				IdEmpresa = _RespuestaJson.getString("IdEmpresa");
				Autorizacion = _RespuestaJson.getString("Autorizacion");
				Mensaje = _RespuestaJson.getString("Mensaje");

				// Imagen = _RespuestaJson.getString("imagen");

				IdSync = _RespuestaJson.getString("idsincronizacion");
				Restantes = _RespuestaJson.getString("restantes");

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

			if (!Restantes.equals("null")) {

				int datosrestantes = (Integer.parseInt(Restantes));

				if (_Exception == null) {
					TxtSincronizacion.setText(Toast5 + datosrestantes + Toast6);

					if (Integer.parseInt(IdSync) != 0) {

						Manager.ActualizarSincronizacionGuardia(IdSync, 0,
								"ON", "INICIAL");

						int id = Manager.devolveridGuardia(IdFuncionario);
						Cursor CursorFuncionarioGuardia;
						CursorFuncionarioGuardia = Manager
								.BuscarGuardiaId(IdFuncionario);// 081493251
						if (CursorFuncionarioGuardia.moveToFirst()) {
							// actualizamos
							Manager.ActualizarDataGuardia(UID, EstadoUID,
									IdFuncionario, Nombres, Apellidos,
									NombreEmpresa, IdEmpresa, Autorizacion,
									Imagen, id);
						} else {
							Manager.InsertarDatosGuardia(UID, EstadoUID,
									IdFuncionario, Nombres, Apellidos,
									NombreEmpresa, IdEmpresa, Autorizacion,
									Imagen);
						}
						//
					} else {
						if(!IdSync.equals("0")){
						Manager.ActualizarSincronizacionGuardia(IdSync, 0,
								"OFF", "INICIAL");
						}

					}
					new HiloSincronizarGuardiaPendientes().execute();
				} else {
					if (ContadorErrores >= 5) {

						Manager.ActualizarContadorSincronizacion(0, "OFF",
								"INICIAL");
						BtnSyncInicial.setTextColor(Color.WHITE);
						BtnSyncInicial.setEnabled(true);
//						Manager.InsertarDatosLog(
//								"Error: Sincronización Inicial Vigilantes ",
//								getFecha(), getHora());

						BtnRegresar.setTextColor(Color.WHITE);
						BtnRegresar.setEnabled(true);

						progress.setVisibility(View.INVISIBLE);
						TxtSincronizacion.setText(Toast2 + datosrestantes
								+ Toast3);

					} else {
						if (TipoSincronizacion.equals("INICIAL")) {
							Manager.ActualizarContadorSincronizacion(
									(ContadorErrores + 1), "ON", "INICIAL");
						}

						new HiloSincronizarGuardiaPendientes().execute();
					}
				}
			} else {
				Toast.makeText(getApplicationContext(),
						"Fin Sincronización Vigilantes Pendientes", Toast.LENGTH_SHORT)
						.show();
				
				Cursor foto = Manager.CursorHABILITAR_FOTOS();
				if (foto.moveToFirst()) {
					Manager.ActualizarHABILITAR_FOTOS("SI");
				} else {
					Manager.InsertarHABILITAR_FOTOS("SI");
				}
				new HiloSincronizacionPersonasInicial().execute();
			}
			
		}

	}

}