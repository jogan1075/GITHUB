package Hilos;

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
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.webcontrol.captura.DataBaseManager;

public class Logs extends AsyncTask<Void, Void, Void> {
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

	String Rut, IdSync, METHOD_NAME, SOAP_ACTION, NAMESPACE, URL, UsuarioWs,
			ContraseniaWs, Autentificacion, EstadoSync, TipoSync,
			TipoSincronizacion, Idioma, Toast1, Toast2, Toast3, Toast4, Toast5,
			Toast6, Toast7, Toast8, Toast9, Toast10;

	MetodosGenerales util;
	private DataBaseManager Manager;
	Context c;

	public Logs(Context contexto) {
		c = contexto;
		util = new MetodosGenerales();
		Manager = new DataBaseManager(c);
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
	protected void onPreExecute() {

	

		Cursor CursorSincronizacion = Manager.CursorSincronizacionVehiculo();

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
			request.addProperty("imei", util.getIMEI(c));

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
					Manager.ActualizarContadorSincronizacion(0, "ON", "FINDIA");
					//Manager.EliminarDatosLog(Integer.toString(Id));

					new Logs(c).execute();

				} else {
					// reintento con la misma data

					if (ContadorErrores >= 5) {

						Manager.ActualizarContadorSincronizacion(0, "OFF",
								"INCREMENTAL");

					} else {
						Manager.ActualizarContadorSincronizacion(
								(ContadorErrores + 1), "ON", "FINDIA");
						new Logs(c).execute();
					}
				}
			} else {

				Manager.ActualizarSincronizacionVehiculoFin("OFF", "INICIODIA");

			}
		} else {
			if (ContadorErrores >= 5) {

				Manager.ActualizarContadorSincronizacion(0, "OFF",
						"INCREMENTAL");

			} else {
				Manager.ActualizarContadorSincronizacion((ContadorErrores + 1),
						"ON", "FINDIA");
				new Logs(c).execute();
			}
		}
		//new SubirDatos(c).execute();
	}
}
