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


public class Persona extends AsyncTask<Void, Void, Void> {

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
	String Respuesta;
	JSONObject RespuestaJson;
	Exception Exception = null;

	String Rut, IdSync, METHOD_NAME, SOAP_ACTION, NAMESPACE, URL, UsuarioWs,
			ContraseniaWs, Autentificacion, EstadoSync, TipoSync,
			TipoSincronizacion, Idioma, Toast1, Toast2, Toast3, Toast4, Toast5,
			Toast6, Toast7, Toast8, Toast9, Toast10;

	MetodosGenerales util;
	private DataBaseManager Manager;
	Context c;
	String tipo_Conexion3gWIFI;
ImageView img;
	
	public Persona(Context contexto, ImageView imagenFoto) {
		img= imagenFoto;
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
		// TODO Auto-generated method stub
		// super.onPreExecute();
		Cursor VerColorFoto = Manager.CursorCambiarFoto();
		if (VerColorFoto.moveToFirst()) {

			Manager.ActualizarColorFoto("azul");

		} else {
			Manager.InsertarColorFoto("azul");
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
		

	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		if (util.isOnline(c)) {
			METHOD_NAME = "WSSincronizar";
			SOAP_ACTION = NAMESPACE + "WSSincronizar";
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

			request.addProperty("idsincronizacion", IdSync);
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

				Respuesta = resultado.toString();
				RespuestaJson = new JSONObject(Respuesta);

				UID = RespuestaJson.getString("UID");
				EstadoUID = RespuestaJson.getString("EstadoUID");
				IdFuncionario = RespuestaJson.getString("IdFuncionario");
				Nombres = RespuestaJson.getString("Nombres"); // MIGUEL
																// ANTONIO
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
		} else {

		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub

		if(!Restantes.isEmpty()){
		if (!Restantes.equals("null")) {

			int datosrestantes = (Integer.parseInt(Restantes));

			if (Exception == null) {
				

				if (Integer.parseInt(IdSync) != 0) {
				
						Manager.ActualizarSincronizacionPersona(
								IdFuncionario, IdSync, 0, "ON", "INICIAL");
				

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
						
						Manager.InsertarDatosLog(IdFuncionario,
								"Persona Actualizado",
								util.getFecha(), util.getHora());
					} else {
						Manager.InsertarDatosFuncionario(UID, EstadoUID,
								IdFuncionario, Nombres, Apellidos,
								NombreEmpresa, IdEmpresa, Ost, CCosto,
								TipoPase, Imagen, Autorizacion,
								AutorizacionConductor, FechaConsulta);
						
						Manager.InsertarDatosLog(IdFuncionario,
								"Persona Actualizado",
								util.getFecha(), util.getHora());
					}

				} else {
					if(!IdSync.equals("0")){
					Manager.ActualizarSincronizacionPersona(IdFuncionario,
							IdSync, 0, "OFF", "INICIAL");
					}

				}
				new Persona(c,img).execute();
			} else {
				if (ContadorErrores >= 5) {

//					Manager.InsertarDatosLog(
//							"Error: Sincronización Inicial Personas ",
//							getFecha(), getHora());
					Manager.ActualizarContadorSincronizacion(0, "OFF",
							"INICIAL");
				

					

				} else {

					Manager.ActualizarContadorSincronizacion(
							(ContadorErrores + 1), "ON", "INICIAL");

					new Persona(c,img).execute();
				}
			}
		} else {

			
			new Vehiculos(c,img).execute();

		}
		}else{
			new Vehiculos(c,img).execute();
		}


	}

}
