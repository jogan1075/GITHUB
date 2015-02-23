package Hilos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.EditText;

import com.webcontrol.captura.DataBaseManager;
import com.webcontrol.captura.MetodosCompartidos;

public class HiloConexionInternet extends AsyncTask<String, Void, String> {

	String _NAMESPACE = "", _URL = "", _METHOD_NAME = "", _SOAP_ACTION = "",
			_Autentificacion = "", _UsuarioWs = "", _ContraseniaWs = "", Autentificacion="";

	
	DataBaseManager Manager;
	Context c;
	Activity activity;
	ProgressDialog _Progress;
	String _Respuesta;
	Exception _Exception;
	// Cursor CursorWs;
	MetodosCompartidos metodos;
	EditText txtURL, txtUSUARIO, txtCONTRASEÑA;

	public HiloConexionInternet(Context contexto, String NAMESPACE, String URL,
			String AUTORIZACION, String USUARIOWS, String PASSWS,
			EditText txturl, EditText txtusuario, EditText txtcontraseña, String autentificacion) {

		this.c = contexto;
		this._NAMESPACE = NAMESPACE;
		this._URL = URL;
		this._Autentificacion = AUTORIZACION;
		this._UsuarioWs = USUARIOWS;
		this._ContraseniaWs = PASSWS;

		txtURL = txturl;
		txtUSUARIO = txtusuario;
		txtCONTRASEÑA = txtcontraseña;
		Autentificacion = autentificacion;
		metodos = new MetodosCompartidos(c);
		Manager = new DataBaseManager(c);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		_Progress = ProgressDialog.show(c, "Validando Conexión",
				"Un Momento...", true);
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		_METHOD_NAME = "WsValidarConexion";
		_SOAP_ACTION = _NAMESPACE + _METHOD_NAME;

		try {
			SoapObject request = new SoapObject(_NAMESPACE, _METHOD_NAME);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER12); // la version de WS 1.1
			envelope.dotNet = true; // estamos utilizando .net
			envelope.setOutputSoapObject(request);
			HttpTransportSE transporte = new HttpTransportSE(_URL);
			if (_Autentificacion.equalsIgnoreCase("si")) {
				List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
				String Conexion = _UsuarioWs + ":" + _ContraseniaWs;
				headerList.add(new HeaderProperty("Authorization",
						"Basic "
								+ org.kobjects.base64.Base64.encode(Conexion
										.getBytes())));
				transporte.call(_SOAP_ACTION, envelope, headerList);

			} else {
				transporte.call(_SOAP_ACTION, envelope);
			}

			SoapPrimitive resultado = (SoapPrimitive) envelope.getResponse();
			_Respuesta = resultado.toString();
		} catch (XmlPullParserException e) {
			_Exception = e;
		} catch (IOException e) {
			_Exception = e;
		} catch (RuntimeException e) {

			_Exception = e;

		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		// super.onPostExecute(result);
		if (_Exception == null) {

			String NameSpace = "http://webservice.webcontrol.cl/";
			String Url = txtURL.getText().toString();
			String Usuario = txtUSUARIO.getText().toString();
			String Contrasenia = txtCONTRASEÑA.getText().toString();

			Cursor cursor = Manager.CursorConfig();
			if (cursor.moveToFirst()) {
				Manager.ActualizarConfigWs(NameSpace, Url, Usuario,
						Contrasenia, Autentificacion);
			} else {
				Manager.InsertarDatosConfig("", NameSpace, Url, Usuario,
						Contrasenia, Autentificacion);

			}
			Cursor VerEstado = Manager.CursorEstadoConexionWs();
			if (VerEstado.moveToFirst()) {
				Manager.ActualizarEstadoConexionWs("SI");
			} else {
				Manager.InsertarEstadoConexionWs("SI");
			}

			// pDialog.setMessage("Validando Conexión... \nConexion WS Internet Correcta");
			_Progress.setMessage("Validando Conexiones... \n Conexión Internet Correcta");
			Cursor cursorConfig = Manager.CursorConfigApp();
			if (!cursorConfig.moveToFirst()) {
				Manager.InsertarConfigApp("SI", "SI", "SI", "SI");
			}
			Cursor SubirInfo = Manager.CursorSincronizacionLista();
			if (SubirInfo.moveToFirst()) {
				Manager.ActualizarSincronizacionLista("SI");
			} else {
				Manager.InsertarSincronizacionLista("SI");
			}
//			new Conexion().execute(NameSpace, EdTxtUrlIntranet.getText()
//					.toString(), EdTxtUsuarioIntranet.getText().toString(),
//					EdTxtContraseniaIntranet.getText().toString());

		} else {
			_Progress.dismiss();
			// mostrarDialogo(ErrorInternet, "");
//			AlertDialog dialogo = dialogoConfirmacionErroInternet("Error de Conexión WS Internet", "¿Desea Continuar?");
//			dialogo.show();
		}
	}

}
