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

public class Guardia extends AsyncTask<Void, Void, Void> {
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

	String Rut, IdSync, METHOD_NAME, SOAP_ACTION, NAMESPACE, URL, UsuarioWs,
			ContraseniaWs, Autentificacion, EstadoSync, TipoSync,
			TipoSincronizacion, Idioma, Toast1, Toast2, Toast3, Toast4, Toast5,
			Toast6, Toast7, Toast8, Toast9, Toast10;
	String tipo_Conexion3gWIFI = "";
	MetodosGenerales util;
	private DataBaseManager Manager;
	Context c;
ImageView img;
	
	
	public Guardia(Context contexto,ImageView imagenfoto){
		
		img=imagenfoto;
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

		} else if (tipo_Conexion3gWIFI.equalsIgnoreCase("INTRANET")) {
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
			img.setImageResource(R.drawable.azul);
		} else {
			Manager.InsertarColorFoto("azul");
			img.setImageResource(R.drawable.azul);
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

	}

	@Override
	protected Void doInBackground(Void... params) {

		METHOD_NAME = "WSSincronizarGuardia";
		SOAP_ACTION = NAMESPACE + "WSSincronizarGuardia";
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

		request.addProperty("idsincronizacion", IdSync);
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

		
		if(!Restantes.isEmpty()){
			
		
			if (!Restantes.equals("null")) {

			
			//	int datosrestantes = (Integer.parseInt(Restantes));

				if (_Exception == null) {
					
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
							
							Manager.InsertarDatosLog(IdFuncionario,
									"Guardia Actualizado",
									util.getFecha(), util.getHora());
						} else {
							Manager.InsertarDatosGuardia(UID, EstadoUID,
									IdFuncionario, Nombres, Apellidos,
									NombreEmpresa, IdEmpresa, Autorizacion,
									Imagen);
							Manager.InsertarDatosLog(IdFuncionario,
									"Guardia Insertado",
									util.getFecha(), util.getHora());
						}
						//
					} else {
						if (!IdSync.equals("0")) {
							Manager.ActualizarSincronizacionGuardia(IdSync, 0,
									"OFF", "INICIAL");
						}

					}
					new Guardia(c,img).execute();
				} else {
					if (ContadorErrores >= 5) {

						Manager.ActualizarContadorSincronizacion(0, "OFF",
								"INICIAL");
						//
						// Manager.InsertarDatosLog(
						// "Error: Sincronización Inicial Vigilantes ",
						// getFecha(), getHora());

					} else {
						if (TipoSincronizacion.equals("INICIAL")) {
							Manager.ActualizarContadorSincronizacion(
									(ContadorErrores + 1), "ON", "INICIAL");
						}

						new Guardia(c,img).execute();
					}
				}
				
			} else {

				new Persona(c,img).execute();
			}
		
		}else{
			new Persona(c,img).execute();
		}

	}
}
