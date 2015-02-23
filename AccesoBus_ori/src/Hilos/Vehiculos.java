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

public class Vehiculos extends AsyncTask<Void, Void, Void> {
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

	int ContadorErrores;
	String _Respuesta;
	JSONObject _RespuestaJson;
	Exception _Exception = null;

	String Rut, IdSync, METHOD_NAME, SOAP_ACTION, NAMESPACE, URL, UsuarioWs,
			ContraseniaWs, Autentificacion, EstadoSync, TipoSync,
			TipoSincronizacion, Idioma, Toast1, Toast2, Toast3, Toast4, Toast5,
			Toast6, Toast7, Toast8, Toast9, Toast10;

	MetodosGenerales util;
	private DataBaseManager Manager;
	Context c;
	String tipo_Conexion3gWIFI = "";
ImageView img;
	public Vehiculos(Context contexto, ImageView imagenfoto) {
		
		img = imagenfoto;
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

		Cursor VerColorFoto = Manager.CursorCambiarFoto();
		if (VerColorFoto.moveToFirst()) {

			Manager.ActualizarColorFoto("azul");

		} else {
			Manager.InsertarColorFoto("azul");
		}
		if (tipo_Conexion3gWIFI.equalsIgnoreCase("INTERNET")) {
			Cursor CursorSincronizacion = Manager
					.CursorSincronizacionVehiculo();

			if (CursorSincronizacion.moveToFirst()) {
				do {
					IdSync = CursorSincronizacion.getString(1);
					ContadorErrores = CursorSincronizacion.getInt(2);

				} while (CursorSincronizacion.moveToNext());

			}
		} else {
			Cursor CursorSincronizacion = Manager
					.CursorSincronizacionVehiculoIntranet();

			if (CursorSincronizacion.moveToFirst()) {
				do {
					IdSync = CursorSincronizacion.getString(1);
					ContadorErrores = CursorSincronizacion.getInt(2);

				} while (CursorSincronizacion.moveToNext());

			}
		}
	}

	@Override
	protected Void doInBackground(Void... params) {

		METHOD_NAME = "WSSincronizarVehiculos";
		SOAP_ACTION = NAMESPACE + "WSSincronizarVehiculos";
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

		request.addProperty("IdSincronizacion", IdSync);
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
				headerList.add(new HeaderProperty("Authorization",
						"Basic "
								+ org.kobjects.base64.Base64.encode(Conexion
										.getBytes())));
				transporte.call(SOAP_ACTION, envelope, headerList);
				// /*****
			} else {
				transporte.call(SOAP_ACTION, envelope);
			}

			SoapPrimitive resultado = (SoapPrimitive) envelope.getResponse();

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
		
		if(!Restantes.isEmpty()){
		if (!Restantes.equals("null")) {

			int datosrestantes = (Integer.parseInt(Restantes));

			if (_Exception == null) {
				
				if (Integer.parseInt(IdSync) != 0) {

					Manager.ActualizarSincronizacionVehiculo(IdVehiculo,
							IdSync, 0, "ON", "INICIAL");

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
						
						Manager.InsertarDatosLog(IdVehiculo,
								"Vehiculo Actualizado",
								util.getFecha(), util.getHora());
					} else {
						Manager.InsertarDatosVehiculo(UID, EstadoUID,
								IdVehiculo, Marca, Modelo, AnioVehiculo,
								TipoVehiculo, NombreEmpresa, ROLEmpresa,
								"0", AutorizacionVehiculo, FechaConsulta,
								Mensaje);
						
						Manager.InsertarDatosLog(IdVehiculo,
								"Vehiculo Actualizado",
								util.getFecha(), util.getHora());
					}
					// new HiloSincronizarGuardiaInicial().execute();
				} else {
					if(!IdSync.equals("0")){
					Manager.ActualizarSincronizacionVehiculo(IdVehiculo,
							IdSync, 0, "OFF", "INICIAL");

					}
				}
				new Vehiculos(c,img).execute();
			} else {
				if (ContadorErrores >= 5) {

					Manager.ActualizarContadorSincronizacion(0, "OFF",
							"INICIAL");
			

				} else {
					if (TipoSincronizacion.equals("INICIAL")) {
						Manager.ActualizarContadorSincronizacion(
								(ContadorErrores + 1), "ON", "INICIAL");
					}

					new Vehiculos(c,img).execute();
				}
			}
		} else {
			
			Cursor VerColorFoto = Manager.CursorCambiarFoto();
			if (VerColorFoto.moveToFirst()) {

				Manager.ActualizarColorFoto("verde");
				img.setImageResource(R.drawable.verde);
			} else {
				Manager.InsertarColorFoto("verde");
				img.setImageResource(R.drawable.verde);
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
			

		try {
			finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}

	}
}
