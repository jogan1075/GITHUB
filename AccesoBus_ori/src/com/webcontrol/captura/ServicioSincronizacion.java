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

import Metodos.MetodosGenerales;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Vibrator;
import android.telephony.TelephonyManager;

public class ServicioSincronizacion extends Service{

	Vibrator Vibrador;
	private DataBaseManager Manager;

	MetodosGenerales metodos;
	
	String IdSync = "", METHOD_NAME = "", SOAP_ACTION = "", NAMESPACE = "",
			URL = "", UsuarioWs = "", ContraseniaWs = "", Autentificacion = "",
			EstadoSync = "", TipoSync = "", TipoSincronizacion = "";
	String tipo_Conexion3gWIFI = "";
	int ContadorErrores;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Manager = new DataBaseManager(getApplicationContext());

		Cursor CursorWs = Manager.CursorConfig();

		if (CursorWs.moveToFirst()) {
			do {
				NAMESPACE = CursorWs.getString(1);
				URL = CursorWs.getString(2);
				UsuarioWs = CursorWs.getString(3); // desarrollo
				ContraseniaWs = CursorWs.getString(4); // Desa1.
				Autentificacion = CursorWs.getString(5);
			} while (CursorWs.moveToNext());

		}
		if (isOnline()) {

			Cursor CursorSincronizacion = Manager.CursorSincronizacionPersona();

			if (CursorSincronizacion.moveToFirst()) {
				do {
					IdSync = CursorSincronizacion.getString(1);
					ContadorErrores = CursorSincronizacion.getInt(2);

					new HiloSincronizarPersonas(IdSync).execute();
				} while (CursorSincronizacion.moveToNext());
			}

//			Cursor CursorSincronizacion1 = Manager
//					.CursorSincronizacionVehiculo();
//
//			if (CursorSincronizacion1.moveToFirst()) {
//				do {
//					IdSync = CursorSincronizacion1.getString(1);
//					ContadorErrores = CursorSincronizacion1.getInt(2);
//					
//					new Vehiculos(IdSync).execute();
//
//				} while (CursorSincronizacion1.moveToNext());
//
//			}
		}

		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		  System.out.println("El servicio a Terminado");
	}
	
	
	/***********************************************************HILOS**********************************************************/
	
	private class HiloSincronizarPersonas extends AsyncTask<Void, Void, Void> {
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
		String Mensaje = "";
		String Imagen = "";
		String Ost = "", CCosto = "", TipoPase = "";
		int ContadorMax = 0;
		int ContadorErrores;
		String _Respuesta;
		String id="";
		
		JSONObject _RespuestaJson;
		Exception _Exception = null;

		public HiloSincronizarPersonas(String idsync) {
			// this.context = contexto;
			// this.activity = actividad;
			this.id = idsync;

		}
		public HiloSincronizarPersonas() {
			// TODO Auto-generated constructor stub
		}
		@Override
		protected void onPreExecute() {
			Cursor CursorWs = Manager.CursorConfig();

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
			//int datosrestantes = (ContadorMax - Integer.parseInt(IdSync));
			if (_Exception == null && IdSync != null && !IdSync.equalsIgnoreCase("null")) {
				
				

				if (Integer.parseInt(IdSync) != 0) {
					if (TipoSincronizacion.equals("INICIAL")) {
						Manager.ActualizarSincronizacionPersona(IdFuncionario,
								IdSync, 0, "ON", "INICIAL");
					}
//					else if (TipoSincronizacion.equals("INICIODIA")) {
//						Manager.ActualizarSincronizacionPersona(IdFuncionario,
//								IdSync, 0, "ON", "INICIODIA");
//					}

					Cursor CursorFuncionario;
					CursorFuncionario = Manager
							.BuscarFuncionarioId(IdFuncionario);
					if (CursorFuncionario.moveToFirst()) {

						int id = Manager.devolveridFuncionario(IdFuncionario);
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
					new HiloSincronizarPersonas(IdSync).execute();
				}else
				{
					if(!IdSync.equals("0")){
					Manager.ActualizarSincronizacionPersona(IdFuncionario,
							IdSync, 0, "OFF", "INICIAL");
					}
				}

			} else {
				if (ContadorErrores >= 5) {
					
						Manager.ActualizarContadorSincronizacion(0, "OFF",
								"INICIAL");
				} else {
					
						Manager.ActualizarContadorSincronizacion(
								(ContadorErrores + 1), "ON", "INICIAL");
				
					

					new HiloSincronizarPersonas(IdSync).execute();
				}
			}
		}
		
	}
	
	

	/****************************************************************************************************************************/
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
}
