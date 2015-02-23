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
import android.os.Vibrator;

import com.webcontrol.captura.DataBaseManager;



public class VerificarCambios extends AsyncTask<Void, Void, Void>{

	Vibrator Vibrador;
	private DataBaseManager Manager;

	MetodosGenerales metodos;
	
	String IdSync = "", METHOD_NAME = "", SOAP_ACTION = "", NAMESPACE = "",
			URL = "", UsuarioWs = "", ContraseniaWs = "", Autentificacion = "",
			EstadoSync = "", TipoSync = "", TipoSincronizacion = "";
	String tipo_Conexion3gWIFI = "";
	int ContadorErrores;
	String rut;
	Context c;
	
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
	String _Respuesta;	
	JSONObject _RespuestaJson;
	Exception _Exception = null;
	
	public VerificarCambios(Context contexto){
		
		
		this.c=contexto;
		metodos = new MetodosGenerales();
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
		}

	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		
		Cursor CursorSincronizacion = Manager.CursorSincronizacionPersona();

		if (CursorSincronizacion.moveToFirst()) {
			do {
				rut = CursorSincronizacion.getString(1);
				ContadorErrores = CursorSincronizacion.getInt(2);

				
			} while (CursorSincronizacion.moveToNext());
		}
		
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		METHOD_NAME = "WSSincronizar";
		SOAP_ACTION = NAMESPACE + "WSSincronizar";
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

		request.addProperty("idsincronizacion", rut);
		request.addProperty("imei", metodos.getIMEI(c));

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
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		//int datosrestantes = (ContadorMax - Integer.parseInt(IdSync));
		if (_Exception == null && IdSync != null && !IdSync.equalsIgnoreCase("null")) {
			
			

			if (Integer.parseInt(IdSync) != 0) {
				if (TipoSincronizacion.equals("INICIAL")) {
					Manager.ActualizarSincronizacionPersona(IdFuncionario,
							IdSync, 0, "ON", "INICIAL");
				}

				Cursor CursorFuncionario = Manager.BuscarFuncionarioId(IdFuncionario);
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
				new VerificarCambios(c).execute();
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
			
				

				new VerificarCambios(c).execute();
			}
		}
	}

}
