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
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.ImageButton;

import com.webcontrol.captura.DataBaseManager;

public class VerFotoFuncionario extends AsyncTask<String, Void, String> {

	ImageButton foto;
	DataBaseManager Manager;
	String rut, imagen;
	Context context;
	Exception _Exception = null;
	String METHOD_NAME, SOAP_ACTION, NAMESPACE, URL, UsuarioWs, ContraseniaWs,
			Autentificacion;
	String tipo_Conexion3gWIFI = "";
	byte[] ImagenByte;
	String _Respuesta = "";
	JSONObject _RespuestaJson;
	MetodosGenerales util;
	private ProgressDialog pDialog;

	public VerFotoFuncionario(String rut, ImageButton imagen, Context contexto) {

		this.rut = rut;
		this.foto = imagen;
		this.context = contexto;
		Manager = new DataBaseManager(context);

		util = new MetodosGenerales();

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub

//		pDialog = new ProgressDialog(context);
//		pDialog.setMessage("Favor Espere...");
//		pDialog.setCancelable(false);
//		pDialog.show();
		
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
			Cursor CursorWs1 = Manager.CursorConfigIntranet();

			if (CursorWs1.moveToFirst()) {
				do {
					NAMESPACE = CursorWs1.getString(1);
					URL = CursorWs1.getString(2);
					UsuarioWs = CursorWs1.getString(3); // desarrollo
					ContraseniaWs = CursorWs1.getString(4); // Desa1.
					Autentificacion = CursorWs1.getString(5);
				} while (CursorWs1.moveToNext());

			}
		}

	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub

		METHOD_NAME = "WSFotoFuncionario";
		SOAP_ACTION = NAMESPACE + "WSFotoFuncionario";
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

		request.addProperty("Rut", rut);
		request.addProperty("imei", util.getIMEI(context));

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

			rut = _RespuestaJson.getString("Rut");
			imagen = _RespuestaJson.getString("Foto");

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
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
//		if (pDialog.isShowing())
//			pDialog.dismiss();
		
		 if (imagen.equals("0") || imagen.equals("null") || imagen == null) // o null
		 {
			 foto.setImageResource(0x7f02001d);
		 }
		 else
		 {
			 ImagenByte = Base64.decode(imagen, Base64.DEFAULT);
				Bitmap Imagenfoto = util.ImagenBitmap(ImagenByte);
				foto.setImageBitmap(Imagenfoto);
			
		 }
		
	}

}
