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

public class UpdateData extends Service {
	Vibrator Vibrador;
	private DataBaseManager Manager;
	Cursor CursorSincronizacion;
	String Rut, IdSync, METHOD_NAME, SOAP_ACTION, NAMESPACE, URL, UsuarioWs,
			ContraseniaWs, Autentificacion, EstadoSync, TipoSync,
			TipoSincronizacion, Idioma, Toast1, Toast2, Toast3, Toast4, Toast5,
			Toast6, Toast7, Toast8, Toast9, Toast10;
	String tipo_Conexion3gWIFI;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Manager = new DataBaseManager(getApplicationContext());
		Cursor CursorTipoConexion = Manager.CursorTipoConexion();
		if (CursorTipoConexion.moveToFirst()) {
			tipo_Conexion3gWIFI = CursorTipoConexion.getString(0);
		}

		if (tipo_Conexion3gWIFI.equalsIgnoreCase("INTERNET")) {
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
				Cursor ControlAccesoLocal = Manager
						.CursorSincronizacionControlAccesoLocal();
				CursorSincronizacion = Manager.CursorSincronizacionLista();

				if (CursorSincronizacion.moveToFirst()
						|| ControlAccesoLocal.moveToFirst()) {
					new HiloSincronizarControlAcceso().execute();
				}
			}
		} else if (tipo_Conexion3gWIFI.equalsIgnoreCase("INTRANET")){
			Cursor CursorWs = Manager.CursorConfigIntranet();

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
				Cursor ControlAccesoLocal = Manager	.CursorSincronizacionControlAccesoLocal();
				CursorSincronizacion = Manager.CursorSincronizacionLista();

				if (CursorSincronizacion.moveToFirst()
						|| ControlAccesoLocal.moveToFirst()) {
					new HiloSincronizarControlAcceso().execute();
				}
			}
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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

	private class HiloSincronizarControlAcceso extends
			AsyncTask<Void, Void, Void> {
		String _Respuesta;
		String Retorno;
		int Id;
		String UID;
		String IdFuncionario;
		String Conductor;
		String Transporte;
		String CodError;
		String IdLocal;
		String Sentido;
		String CentroCosto;
		String OST;
		String TipoPase;
		String EstadoConexion;
		String Fecha;
		String Hora;
		String Sincronizacion;
		String idEmpresa;
		int ContadorAntiguo = -1;
		int ContadorActual = 0;
		int ContadorErrores;

		JSONObject _RespuestaJson;
		Exception _Exception = null;

		@Override
		protected void onPreExecute() {

			Manager.ActualizarColorFoto("BLANCO");

			CursorSincronizacion = Manager.CursorSincronizacionVehiculo();

			if (CursorSincronizacion.moveToFirst()) {
				do {
					// IdSync = CursorSincronizacion.getString(1);
					ContadorErrores = CursorSincronizacion.getInt(2);
				} while (CursorSincronizacion.moveToNext());

			}

			Cursor ControlAccesoLocal = Manager
					.CursorSincronizacionControlAccesoLocal();
			// CN_IDCONTROL, CN_UIDCONTROL, CN_IDFUNCIONARIOCONTROL,
			// CN_CONDUCTOR, CN_TRANSPORTE, CN_CODIGOERROR, CN_IDLOCAL,
			// CN_SENTIDO, CN_CENTROCOSTO, CN_OST, CN_TIPOPASE,
			// CN_ESTADOCONEXION, CN_FECHACONTROL, CN_HORACONTROL,
			// CN_SINCRONIZACION
			if (ControlAccesoLocal.moveToFirst()) {
				do {

					Id = ControlAccesoLocal.getInt(0);
					UID = ControlAccesoLocal.getString(1);
					IdFuncionario = ControlAccesoLocal.getString(2);
					idEmpresa = ControlAccesoLocal.getString(3);
					Conductor = ControlAccesoLocal.getString(4);
					Transporte = ControlAccesoLocal.getString(5);
					CodError = ControlAccesoLocal.getString(6);
					IdLocal = ControlAccesoLocal.getString(7);
					Sentido = ControlAccesoLocal.getString(8);
					CentroCosto = ControlAccesoLocal.getString(9);
					OST = ControlAccesoLocal.getString(10);
					TipoPase = ControlAccesoLocal.getString(11);
					EstadoConexion = ControlAccesoLocal.getString(12);
					Fecha = ControlAccesoLocal.getString(13);
					Hora = ControlAccesoLocal.getString(14);
					Sincronizacion = ControlAccesoLocal.getString(15);

					ContadorActual = ContadorActual + 1;

				} while (ControlAccesoLocal.moveToNext());
			} else {
				Id = 0;
			}

		}

		@Override
		protected Void doInBackground(Void... params) {
			if (Id != 0) {
				METHOD_NAME = "WsInsetarRegistroAcceso";
				SOAP_ACTION = NAMESPACE + "WsInsetarRegistroAcceso";
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

				request.addProperty("rut", IdFuncionario);
				request.addProperty("fecha", Fecha);
				request.addProperty("hora", Hora);
				request.addProperty("rutempresa", idEmpresa); // agregar
				request.addProperty("patente", Transporte);
				request.addProperty("error", CodError);
				request.addProperty("local", Manager.TraerLocal());
				request.addProperty("inout", Sentido.toUpperCase());
				request.addProperty("centrocosto", CentroCosto);
				request.addProperty("ost", OST);
				request.addProperty("tipopase", TipoPase);
				request.addProperty("division", Manager.TraerDivision()); // agregar
				request.addProperty("conductor", Conductor);
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
					_Respuesta = resultado.toString();
					_RespuestaJson = new JSONObject(_Respuesta);
					Retorno = _RespuestaJson.getString("retorno");
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
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			if (_Exception == null) {
				if (Id != 0) {
					if (Retorno.equals("1")) {
						if (ContadorAntiguo == -1) {

							ContadorAntiguo = ContadorActual;
							// Progreso = datosrestantes;
						}

						// borro el dato enviado y paso al siguiente
						Manager.ActualizarContadorSincronizacion(0, "ON",
								"FINDIA");
						Manager.EliminarDatosControlAccesoLocal(Integer
								.toString(Id));
						// Manager.EliminarDatosLog(Integer.toString(Id));
						if (isOnline()) {
							new HiloSincronizarControlAcceso().execute();
						}

					} else {
						// reintento con la misma data

						if (ContadorErrores >= 5) {

							Manager.ActualizarContadorSincronizacion(0, "OFF",
									"INCREMENTAL");

							// Toast.makeText(
							// getApplicationContext(),
							// Toast2 + Integer.toString(ContadorActual)
							// + Toast3, Toast.LENGTH_LONG).show();
						} else {
							Manager.ActualizarContadorSincronizacion(
									(ContadorErrores + 1), "ON", "FINDIA");
							new HiloSincronizarControlAcceso().execute();
						}
					}
				} else {
					if (isOnline()) {
						new HiloSincronizarLog().execute();
					}

					// Manager.ActualizarSincronizacionVehiculoFin("OFF",
					// "INICIODIA");
					// progress.setVisibility(View.INVISIBLE);
					// TxtSincronizacion.setText("Sincronizacion Finalizada");
					// Toast.makeText(getApplicationContext(),
					// "Sincronizacion Finalizada", Toast.LENGTH_LONG).show();
				}
			} else {
				if (ContadorErrores >= 5) {

					Manager.ActualizarContadorSincronizacion(0, "OFF",
							"INCREMENTAL");

					// (getApplicationContext(),
					// Toast2 + Integer.toString(ContadorActual) + Toast3,
					// Toast.LENGTH_LONG).show();
				} else {
					Manager.ActualizarContadorSincronizacion(
							(ContadorErrores + 1), "ON", "FINDIA");
					new HiloSincronizarControlAcceso().execute();
				}
			}
		}
	}

	private class HiloSincronizarLog extends AsyncTask<Void, Void, Void> {
		String _Respuesta;
		String Retorno;
		String Descripcion;
		String Fecha;
		String Hora;
		int ContadorAntiguo = -1;
		int ContadorActual = 0;
		int ContadorErrores;
		int Id;
		JSONObject _RespuestaJson;
		Exception _Exception = null;

		@Override
		protected void onPreExecute() {
			CursorSincronizacion = Manager.CursorSincronizacionVehiculo();

			if (CursorSincronizacion.moveToFirst()) {
				do {
					// IdSync = CursorSincronizacion.getString(1);
					ContadorErrores = CursorSincronizacion.getInt(2);
				} while (CursorSincronizacion.moveToNext());

			}

			Cursor CursorLog = Manager.CursorLog();

			if (CursorLog.moveToFirst()) {
				do {
					Id = CursorLog.getInt(0);
					Descripcion = CursorLog.getString(1);
					Fecha = CursorLog.getString(2);
					Hora = CursorLog.getString(3);
					ContadorActual = ContadorActual + 1;

				} while (CursorLog.moveToNext());
			} else {
				Id = 0;
				Descripcion = "Sin Datos";
				Fecha = "2014/06/20";
				Hora = "12:37:29";
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			if (Id != 0) {
				METHOD_NAME = "WSLOGS";
				SOAP_ACTION = NAMESPACE + "WSLOGS";
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

				request.addProperty("descripcion", Descripcion);
				request.addProperty("fecha", Fecha);
				request.addProperty("hora", Hora);
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
					_Respuesta = resultado.toString();
					_RespuestaJson = new JSONObject(_Respuesta);
					Retorno = _RespuestaJson.getString("retorno");
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
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			if (_Exception == null) {
				if (Id != 0) {
					if (Retorno.equals("1")) {
						if (ContadorAntiguo == -1) {

							ContadorAntiguo = ContadorActual;
							// Progreso = datosrestantes;
						}

						// borro el dato enviado y paso al siguiente
						Manager.ActualizarContadorSincronizacion(0, "ON",
								"FINDIA");
//						Manager.EliminarDatosLog(Integer.toString(Id));
						if (isOnline()) {
							new HiloSincronizarLog().execute();
						}

					} else {
						// reintento con la misma data

						if (ContadorErrores >= 5) {

							Manager.ActualizarContadorSincronizacion(0, "OFF",
									"INCREMENTAL");

							// Toast.makeText(
							// getApplicationContext(),
							// Toast2 + Integer.toString(ContadorActual)
							// + Toast3, Toast.LENGTH_LONG).show();
						} else {
							Manager.ActualizarContadorSincronizacion(
									(ContadorErrores + 1), "ON", "FINDIA");
							if (isOnline())
								new HiloSincronizarLog().execute();
						}
					}
				} else {

					Manager.ActualizarSincronizacionVehiculoFin("OFF",
							"INICIODIA");

					Manager.ActualizarColorFoto("VERDE");

				}
			} else {
				if (ContadorErrores >= 5) {

					Manager.ActualizarContadorSincronizacion(0, "OFF",
							"INCREMENTAL");

					// Toast.makeText(getApplicationContext(),
					// Toast2 + Integer.toString(ContadorActual) + Toast3,
					// Toast.LENGTH_LONG).show();
				} else {
					Manager.ActualizarContadorSincronizacion(
							(ContadorErrores + 1), "ON", "FINDIA");
					if (isOnline())
						new HiloSincronizarLog().execute();
				}
			}
		}
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
