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
import android.widget.ImageView;

import com.webcontrol.captura.DataBaseManager;
import com.webcontrol.captura.R;

public class SubirDatos extends AsyncTask<Void, Void, Void> {
	String _Respuesta = "";
	String Retorno = "";
	int Id = 0;
	String UID = "";
	String IdFuncionario = "";
	String Conductor = "";
	String Transporte = "";
	String CodError = "";
	String IdLocal = "";
	String Sentido = "";
	String CentroCosto = "";
	String OST = "";
	String TipoPase = "";
	String EstadoConexion = "";
	String Fecha = "";
	String Hora = "";
	String rutEmpresa = "";
	String Division="";
	String Sincronizacion = "";
	int ContadorAntiguo = -1;
	int ContadorActual = 0;
	int ContadorErrores;

	JSONObject _RespuestaJson;
	Exception _Exception = null;

	String Rut, IdSync, METHOD_NAME, SOAP_ACTION, NAMESPACE, URL, UsuarioWs,
			ContraseniaWs, Autentificacion, EstadoSync, TipoSync,
			TipoSincronizacion, Idioma, Toast1, Toast2, Toast3, Toast4, Toast5,
			Toast6, Toast7, Toast8, Toast9;
	boolean resp = false;

	MetodosGenerales util;
	private DataBaseManager Manager;
	Context c;
	String tipo_Conexion3gWIFI;

	ImageView img;

	public SubirDatos(Context contexto, ImageView imagenFoto) {
		img = imagenFoto;
		c = contexto;
		util = new MetodosGenerales();
		Manager = new DataBaseManager(c);
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

		} else {
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

		}
	}

	@Override
	protected void onPreExecute() {

		Cursor ControlAccesoLocal = Manager
				.CursorSincronizacionControlAccesoLocal();

		if (ControlAccesoLocal.moveToFirst()) {
			do {
				Id = ControlAccesoLocal.getInt(0);
				UID = ControlAccesoLocal.getString(1);
				IdFuncionario = ControlAccesoLocal.getString(2);
				rutEmpresa = ControlAccesoLocal.getString(3);
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
				Cursor c = Manager.CursorConfig();
				if(c.moveToFirst()){
				Division = c.getString(7);
				}
				
			} while (ControlAccesoLocal.moveToNext());
		} else {
			resp = true;
		}

	}

	@Override
	protected Void doInBackground(Void... params) {
		if (!resp) {
			METHOD_NAME = "WsInsetarRegistroAcceso";
			SOAP_ACTION = NAMESPACE + "WsInsetarRegistroAcceso";
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

			request.addProperty("rut", IdFuncionario);
			request.addProperty("fecha", Fecha);
			request.addProperty("hora", Hora);
			request.addProperty("rutempresa", rutEmpresa); // agregar
			request.addProperty("patente", Transporte);
			request.addProperty("error", CodError);
			request.addProperty("local", IdLocal);
			request.addProperty("inout", Sentido.toUpperCase());
			request.addProperty("centrocosto", CentroCosto);
			request.addProperty("ost", OST);
			request.addProperty("tipopase", TipoPase);
			request.addProperty("division", Division); // agregar
			request.addProperty("conductor", Conductor);
			request.addProperty("imei", util.getIMEI(c));

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER12); // la version de WS 1.1
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

		if (!_Respuesta.isEmpty()) {
			if (_Exception == null) {

				if (Retorno.equals("1")) {

					Manager.EliminarDatosControlAccesoLocal(Integer
							.toString(Id));

				} else {
					// reintento con la misma data

					if (ContadorErrores >= 5) {

						Manager.ActualizarContadorSincronizacion(0, "OFF",
								"INCREMENTAL");

					} else {
						Manager.ActualizarContadorSincronizacion(
								(ContadorErrores + 1), "ON", "FINDIA");
						new SubirDatos(c, img).execute();
					}

				}
				new SubirDatos(c, img).execute();
			} else {
				if (ContadorErrores >= 5) {

					Manager.ActualizarContadorSincronizacion(0, "OFF",
							"INCREMENTAL");

				} else {
					Manager.ActualizarContadorSincronizacion(
							(ContadorErrores + 1), "ON", "FINDIA");
					new SubirDatos(c, img).execute();
				}

				

			}
		}else{
			Cursor VerColorFoto = Manager.CursorCambiarFoto();
			if (VerColorFoto.moveToFirst()) {

				Manager.ActualizarColorFoto("verde");
				img.setImageResource(R.drawable.verde);
			} else {
				Manager.InsertarColorFoto("verde");
				img.setImageResource(R.drawable.verde);
			}
		}

	}
}