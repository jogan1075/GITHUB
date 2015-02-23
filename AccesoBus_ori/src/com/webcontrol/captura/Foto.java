package com.webcontrol.captura;

import java.io.ByteArrayInputStream;

import org.json.JSONObject;

import Metodos.MetodosGenerales;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Foto extends Activity {

	private ProgressDialog pDialog;
	private ListView list;
	String foto = "";
	String rut = "";
	String _Respuesta = "";
	JSONObject _RespuestaJson;
	SimpleCursorAdapter mAdapter;
	ListView mListView;
	ImageView verimagen1;
	byte[] ImagenByte;
	String Imagen = "";
	Vibrator Vibrador;
	private TextView textNombre;
	private TextView textApellido;

	String IdSync, METHOD_NAME, SOAP_ACTION, NAMESPACE, URL, UsuarioWs,
			ContraseniaWs, Autentificacion, EstadoSync, TipoSync,
			TipoSincronizacion, Idioma, Toast1, Toast2, Toast3, Toast4, Toast5,
			Toast6, Toast7, Toast8, Toast9, Toast10;
	String tipo_Conexion3gWIFI = "";
	MetodosGenerales util;
	private DataBaseManager Manager;
	Context c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_foto);

		Bundle recibir = this.getIntent().getExtras();
		String rut = recibir.getString("Rut_usuario");
		Vibrador = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
		verimagen1 = (ImageView) findViewById(R.id.imageView2);
		// Calling async task to get json
		HomeKeyLocker mHomeKeyLocker = new HomeKeyLocker();
		mHomeKeyLocker.lock(this);
		// pasar variable al hilo
		new HiloFoto(this, rut).execute();
		
		Button volver= (Button)findViewById(R.id.btnVolverFoto);
		
		volver.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Vibrador.vibrate(80);
				finish();
			}
		});
	}

	private class HiloFoto extends AsyncTask<Void, Void, Void> {

		private String rut;

		public HiloFoto(Context contexto, String Rut) {
			c = contexto;
			util = new MetodosGenerales();
			Manager = new DataBaseManager(c);
			rut = Rut;
		}

		// String rut="177163392";
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(Foto.this);
			pDialog.setMessage("Espere un momento...");
			pDialog.setCancelable(false);
			pDialog.show();

//			Cursor CursorTipoConexion = Manager.CursorTipoConexion();
//			if (CursorTipoConexion.moveToFirst()) {
//				tipo_Conexion3gWIFI = CursorTipoConexion.getString(0);
//			}
//
//			if (tipo_Conexion3gWIFI.equalsIgnoreCase("INTERNET")) {
//				Cursor CursorWs = Manager.CursorConfig();
//
//				if (CursorWs.moveToFirst()) {
//					do {
//						NAMESPACE = CursorWs.getString(1);
//						URL = CursorWs.getString(2);
//						UsuarioWs = CursorWs.getString(3); // desarrollo
//						ContraseniaWs = CursorWs.getString(4); // Desa1.
//						Autentificacion = CursorWs.getString(5);
//					} while (CursorWs.moveToNext());
//
//				}
//
//			} else if (tipo_Conexion3gWIFI.equalsIgnoreCase("INTRANET")) {
//				Cursor CursorWs = Manager.CursorConfigIntranet();
//
//				if (CursorWs.moveToFirst()) {
//					do {
//						NAMESPACE = CursorWs.getString(1);
//						URL = CursorWs.getString(2);
//						UsuarioWs = CursorWs.getString(3); // desarrollo
//						ContraseniaWs = CursorWs.getString(4); // Desa1.
//						Autentificacion = CursorWs.getString(5);
//					} while (CursorWs.moveToNext());
//
//				}
//
//			}
		}

		@Override
		protected Void doInBackground(Void... arg0) {

//			METHOD_NAME = "WSFotoFuncionario";
//			SOAP_ACTION = NAMESPACE + "WSFotoFuncionario";
//
//			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
//			request.addProperty("Rut", rut);
//			request.addProperty("imei", util.getIMEI(c));
//
//			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
//					SoapEnvelope.VER11); // la version de WS 1.1
//			envelope.dotNet = true; // estamos utilizando .net
//			envelope.setOutputSoapObject(request);
//			HttpTransportSE transporte = new HttpTransportSE(URL);
//
//			try {
//
//				if (Autentificacion.equals("SI")) {
//					List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
//					String Conexion = UsuarioWs + ":" + ContraseniaWs;
//					headerList.add(new HeaderProperty("Authorization", "Basic "
//							+ org.kobjects.base64.Base64.encode(Conexion
//									.getBytes())));
//					transporte.call(SOAP_ACTION, envelope, headerList);
//				} else {
//					transporte.call(SOAP_ACTION, envelope);
//				}
//				SoapPrimitive resultado = (SoapPrimitive) envelope
//						.getResponse();
//
//				_Respuesta = resultado.toString();
//				_RespuestaJson = new JSONObject(_Respuesta);
//
//				rut = _RespuestaJson.getString("Rut");
//				Imagen = _RespuestaJson.getString("Foto");
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			
			Cursor foto = Manager.TraerImagenporRut(rut);
			if(foto.moveToFirst()){
				Imagen = foto.getString(0);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			
			if (Imagen.equals("0") || Imagen.equals("null") || Imagen == null) // o
																				// null
			{
				verimagen1.setImageResource(R.drawable.nofoto);
			} else {
				ImagenByte = Base64.decode(Imagen, Base64.DEFAULT);
				Bitmap Imagenfoto = ImagenBitmap(ImagenByte);
				if (pDialog.isShowing()){
					pDialog.dismiss();
				}
				verimagen1.setImageBitmap(Imagenfoto);

				
			}
//			Toast.makeText(Foto.this, "Foto Perfil Cargada", Toast.LENGTH_SHORT)
//					.show();

		}

	}

	private Bitmap ImagenBitmap(byte[] Imagen) {

		byte[] ImagenEntranteEnByts = Imagen;
		ByteArrayInputStream imageStream = new ByteArrayInputStream(
				ImagenEntranteEnByts);
		Bitmap ImagenNueva = BitmapFactory.decodeStream(imageStream);
		return ImagenNueva;
	}
}
