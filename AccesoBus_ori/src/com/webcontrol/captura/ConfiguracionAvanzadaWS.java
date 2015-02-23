package com.webcontrol.captura;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
//import com.webcontrol.controlbus.ConfiguracionPrincipal;

public class ConfiguracionAvanzadaWS extends Activity {

	Vibrator Vibrador;
	private DataBaseManager Manager;
	Cursor CursorWs;
	Cursor CursorWSIntranet;
	String NameSpace, Url, Usuario, Contrasenia, Autentificacion, Idioma;
	String NameSpaceIntranet, UrlIntranet, UsuarioIntranet,
			ContraseniaIntranet, AutentificacionIntranet;
	EditText EdTxtUrl, EdTxtUsuario, EdTxtContrasenia;
	EditText EdTxtUrlIntranet, EdTxtUsuarioIntranet, EdTxtContraseniaIntranet;
	TextView TxtUsuarioWs, TxtContraseniaWS, txtURL;
	TextView TxtUsuarioWsIntranet, TxtContraseniaWSIntranet, txtURLINTRANET;
	Button BtnCancelar, BtnGuardar;
	ToggleButton TBAutentificacion;
	ToggleButton TBAutentificacionIntranet;
	ProgressDialog pDialog;
	String txtValidarConexiones;
	String txtvalidarInternet;
	String txtvalidarIntranet;
	String ErrorInternet;
	String ErrorIntranet;
	String MensajeError;
	String si;
	String no;
	String CorrectoInternetEIntranet;
	String ValidarConexionInternetEIntranet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.configuracionavanzadaws_layout);
		Vibrador = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);

		Manager = new DataBaseManager(this);
		CursorWs = Manager.CursorConfig();
		CursorWSIntranet = Manager.CursorConfigIntranet();
		EdTxtUrl = (EditText) findViewById(R.id.editTextUrlWS);
		EdTxtUsuario = (EditText) findViewById(R.id.editTextUsuarioWS);
		EdTxtContrasenia = (EditText) findViewById(R.id.editTextContraseniaWS);
		TxtUsuarioWs = (TextView) findViewById(R.id.textViewUsuarioWs);
		TxtContraseniaWS = (TextView) findViewById(R.id.textViewContraseniaWS);
		TxtUsuarioWsIntranet = (TextView) findViewById(R.id.textViewUsuarioWs2);
		TxtContraseniaWSIntranet = (TextView) findViewById(R.id.textViewContraseniaWS2);

		txtURL = (TextView) findViewById(R.id.textViewUrlWS);
		txtURLINTRANET = (TextView) findViewById(R.id.textViewUrlWS2);

		EdTxtUrlIntranet = (EditText) findViewById(R.id.editTextUrlWS2);
		EdTxtUsuarioIntranet = (EditText) findViewById(R.id.editTextUsuarioWS2);
		EdTxtContraseniaIntranet = (EditText) findViewById(R.id.editTextContraseniaWS2);

		TBAutentificacionIntranet = (ToggleButton) findViewById(R.id.togglebuttonAutentificacion2);

		BtnCancelar = (Button) findViewById(R.id.buttonCancelar);
		BtnGuardar = (Button) findViewById(R.id.buttonGuardar);
		TBAutentificacion = (ToggleButton) findViewById(R.id.togglebuttonAutentificacion);
		



		Cursor CursorIdioma = Manager.CursorIdioma();

		if (CursorIdioma.moveToFirst()) {
			Idioma = CursorIdioma.getString(0);
		}

		if (Idioma.equals("ESPANIOL")) {
			Textos("Autenticación WS Activada", "Autenticación WS Desactivada",
					"USUARIO WS", "CONTRASEÑA WS", "Cancelar", "Guardar",
					"Autenticación WS Intranet Activada",
					"Autenticación WS Intranet Desactivada",
					"USUARIO WS INTRANET", "CONTRASEÑA WS INTRANET", "URL WS",
					"URL Intranet", "Validando Conexiones...",
					"Validando Conexiones... \n Conexión Internet Correcta",
					"Validando Conexiones... \n Conexión Intranet Correcta",
					"Error de Conexión WS Internet",
					"Error de Conexión WS Intranet",
					"Conexiones WS Internet e Intranet Correctas",
					"Válide Las Conexiones de Internet e Intranet",
					"¿Desea Continuar?", "Si", "No");
		} else if (Idioma.equals("INGLES")) {
			Textos("WS Authentication Enabled", "WS Authentication Disabled",
					"USER WS", "PASSWORD WS", "Cancel", "Save",
					"WS Authentication Enabled Intranet",
					"WS Intranet Authentication disabled", "INTRANET USER WS",
					"PASSWORD WS INTRANET", "WS URL", "Intranet URL",
					"Validating Connections...",
					"Validating Connections... \nRight Internet Connection",
					"Validating Connections... \nRight Intranet Connection",
					"Internet Connection Error WS",
					"Intranet Connection Error WS",
					"Internet and Intranet Connections Correct WS",
					"Validate Connections Internet and Intranet",
					"Do you want to continue ?", "Yes", "No");
		} else if (Idioma.equals("PORTUGUES")) {
			Textos("Autenticação Ativado", "Autenticação Off", "USUÁRIO",
					"SENHA", "Cancelar", "Salvar",
					"Autenticação WS Ativado Intranet",
					"WS Intranet Autenticação Off", "WS INTRANET USUÁRIO",
					"SENHA WS INTRANET", "WS URL", "URL Intranet",
					"validando Conexões...",
					"validando Conexões... \nConexão Internet Direito",
					"validando Conexões... \nDireito Connection Intranet",
					"WS erro de conexão Internet",
					"WS erro de conexão Intranet",
					"Internet e Intranet Conexões WS correta",
					"Validar conexões de Internet e Intranet",
					"Você quer continuar?", "Sim", "Não");
		}

		if (CursorWs.moveToFirst()) {
			do {
				NameSpace = CursorWs.getString(1);
				Url = CursorWs.getString(2);
				Usuario = CursorWs.getString(3);
				Contrasenia = CursorWs.getString(4);
				Autentificacion = CursorWs.getString(5);

			} while (CursorWs.moveToNext());

			EdTxtUrl.setText(Url);
			EdTxtUsuario.setText(Usuario);
			EdTxtContrasenia.setText(Contrasenia);
		} else {
			Autentificacion = "NO";
		}
		if (CursorWSIntranet.moveToFirst()) {
			do {
				NameSpaceIntranet = CursorWSIntranet.getString(1);
				UrlIntranet = CursorWSIntranet.getString(2);
				UsuarioIntranet = CursorWSIntranet.getString(3);
				ContraseniaIntranet = CursorWSIntranet.getString(4);
				AutentificacionIntranet = CursorWSIntranet.getString(5);

			} while (CursorWSIntranet.moveToNext());

			EdTxtUrlIntranet.setText(UrlIntranet);
			EdTxtUsuarioIntranet.setText(UsuarioIntranet);
			EdTxtContraseniaIntranet.setText(ContraseniaIntranet);
		} else {
			AutentificacionIntranet = "NO";
		}
		if (Autentificacion.equals("SI")) {
			TBAutentificacion.setChecked(true);
			EdTxtUsuario.setEnabled(true);
			EdTxtContrasenia.setEnabled(true);
		} else {
			TBAutentificacion.setChecked(false);
			EdTxtUsuario.setEnabled(false);
			EdTxtContrasenia.setEnabled(false);
		}
		if (AutentificacionIntranet.equals("SI")) {
			TBAutentificacionIntranet.setChecked(true);
			EdTxtUsuario.setEnabled(true);
			EdTxtContrasenia.setEnabled(true);
		} else {
			TBAutentificacionIntranet.setChecked(false);
			EdTxtUsuario.setEnabled(false);
			EdTxtContrasenia.setEnabled(false);
		}
		pDialog = new ProgressDialog(ConfiguracionAvanzadaWS.this);
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setMessage(txtValidarConexiones);
		pDialog.setCancelable(false);
		pDialog.setMax(100);
	}

	public void BtnAutentificacion(View v) {
		// Is the toggle on?
		boolean on = ((ToggleButton) v).isChecked();

		Vibrador.vibrate(80);

		if (on) {
			Autentificacion = "SI";
			EdTxtUsuario.setEnabled(true);

			EdTxtContrasenia.setEnabled(true);

		} else {
			Autentificacion = "NO";
			EdTxtUsuario.setEnabled(false);
			EdTxtContrasenia.setEnabled(false);

		}
	}

	public void BtnAutentificacionIntranet(View v) {
		// Is the toggle on?
		boolean on = ((ToggleButton) v).isChecked();

		Vibrador.vibrate(80);

		if (on) {
			AutentificacionIntranet = "SI";
			EdTxtUsuarioIntranet.setEnabled(true);
			EdTxtContraseniaIntranet.setEnabled(true);

		} else {
			AutentificacionIntranet = "NO";
			EdTxtUsuarioIntranet.setEnabled(false);
			EdTxtContraseniaIntranet.setEnabled(false);

		}
	}

	private void Textos(String TextoAutentificacionOn,
			String TextoAutentificacionOff, String TextoUsuario,
			String TextoContrasenia, String TextoCAncelar, String TextoGuardar,
			String txtAutenticacionIntranet, String txtNOAutenticacionIntranet,
			String usuarioIntranet, String passIntranet, String url,
			String urlIntranet, String validando, String valindandointernet,
			String valindandoIntranet, String ErrorConexionInternet,
			String ErrorConexionIntranet, String ConexionCorrecta,
			String ValidarConexioness, String pregunta, String SiYes,
			String NoNo) {
		TBAutentificacion.setTextOn(TextoAutentificacionOn);
		TBAutentificacion.setTextOff(TextoAutentificacionOff);
		TBAutentificacionIntranet.setTextOn(txtAutenticacionIntranet);
		TBAutentificacionIntranet.setTextOff(txtNOAutenticacionIntranet);
		TxtUsuarioWs.setText(TextoUsuario);
		TxtContraseniaWS.setText(TextoContrasenia);
		TxtUsuarioWsIntranet.setText(usuarioIntranet);
		TxtContraseniaWSIntranet.setText(passIntranet);
		BtnCancelar.setText(TextoCAncelar);
		BtnGuardar.setText(TextoGuardar);
		txtURL.setText(url);
		txtURLINTRANET.setText(urlIntranet);
		txtValidarConexiones = validando;
		txtvalidarInternet = valindandointernet;
		txtvalidarIntranet = valindandoIntranet;
		ErrorInternet = ErrorConexionInternet;
		ErrorIntranet = ErrorConexionIntranet;
		CorrectoInternetEIntranet = ConexionCorrecta;
		ValidarConexionInternetEIntranet = ValidarConexioness;
		MensajeError = pregunta;
		si = SiYes;
		no = NoNo;
	}

	public void BtnGuardar(View v) {
		Vibrador.vibrate(80);
		if (isOnline(this)){
			new HiloValidacionWS().execute("http://webservice.webcontrol.cl/",
					EdTxtUrl.getText().toString(), EdTxtUsuario.getText()
							.toString(), EdTxtContrasenia.getText().toString());
			
//			new Conexion().execute("http://webservice.webcontrol.cl/",
//			EdTxtUrl.getText().toString(), EdTxtUsuario.getText()
//					.toString(), EdTxtContrasenia.getText().toString());
			
//			new HiloValidarConexion(this, "http://webservice.webcontrol.cl/", EdTxtUrl.getText().toString(), Autentificacion,  EdTxtUsuario.getText()
//					.toString(), EdTxtContrasenia.getText().toString()).execute();
		}
		else{
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Error de Conexión a Internet");
			alertDialog.setMessage("Revise Su Conexión e Intente Nuevamente");
			alertDialog.setButton("Aceptar", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			});
			alertDialog.setCancelable(false);
			alertDialog.show();
		}
		// new Conexion().execute(NAMESPACE, URL, USUARIO, PASS, TIPOCONEXION);
	}

	
	public boolean isOnline(Context context) {
		boolean enabled = true;

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();

		if ((info == null || !info.isConnected() || !info.isAvailable())) {
			enabled = false;
		}
		return enabled;
	}


	private class HiloValidarConexion extends AsyncTask<String, Void, String> {
		String _NAMESPACE="", _URL="", _METHOD_NAME="", _SOAP_ACTION="", _Autentificacion="",
				_UsuarioWs="", _ContraseniaWs="";
		
		DataBaseManager Manager;
		Context context;
		Activity activity;
		ProgressDialog _Progress;
		String _Respuesta;
		Exception _Exception;
		//Cursor CursorWs;
		MetodosCompartidos metodos;

		public HiloValidarConexion(Context contexto, 
				String NAMESPACE, String URL, String AUTORIZACION,
				String USUARIOWS, String PASSWS) {
			this.context = contexto;
			
			this._NAMESPACE = NAMESPACE;
			this._URL = URL;
			this._Autentificacion = AUTORIZACION;
			this._UsuarioWs = USUARIOWS;
			this._ContraseniaWs = PASSWS;
			metodos = new MetodosCompartidos(context);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
//			_Progress = ProgressDialog.show(context, "Validando Conexión",
//					"Un Momento...", true);
		

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
			super.onPostExecute(result);
			if (_Exception == null) {

				NameSpace = "http://webservice.webcontrol.cl/";
				Url = EdTxtUrl.getText().toString();
				Usuario = EdTxtUsuario.getText().toString();
				Contrasenia = EdTxtContrasenia.getText().toString();

				if (CursorWs.moveToFirst()) {
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
				pDialog.setMessage(txtvalidarInternet);
				Cursor cursorConfig = Manager.CursorConfigApp();
				if (!cursorConfig.moveToFirst()) {
					Manager.InsertarConfigApp("SI", "SI", "SI", "SI");
				}
				Cursor SubirInfo = Manager.CursorSincronizacionLista();
				if (SubirInfo.moveToFirst()) {
					Manager.ActualizarSincronizacionLista("SI");
				}
				else{
					Manager.InsertarSincronizacionLista("SI");
				}
				new Conexion().execute(NameSpace, EdTxtUrlIntranet.getText()
						.toString(), EdTxtUsuarioIntranet.getText().toString(),
						EdTxtContraseniaIntranet.getText().toString());
			
			} else {
				pDialog.dismiss();
				// mostrarDialogo(ErrorInternet, "");
				AlertDialog dialogo = dialogoConfirmacionErroInternet(
						ErrorInternet, MensajeError);
				dialogo.show();
			}
		}

	}
	
	private class HiloValidacionWS extends AsyncTask<String, Void, String> {
		
		String _Respuesta;
		JSONObject _RespuestaJson;
		Exception _Exception = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			
			String METHOD_NAME = "WsValidarConexion";
			String SOAP_ACTION = params[0]  + METHOD_NAME;

			try {
				SoapObject request = new SoapObject(params[0] , METHOD_NAME);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11); 
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE transporte = new HttpTransportSE(params[1]);
				if (Autentificacion.equalsIgnoreCase("si")) {
					List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
					String Conexion = params[2] + ":" + params[3];
					headerList.add(new HeaderProperty("Authorization",
							"Basic "
									+ org.kobjects.base64.Base64.encode(Conexion
											.getBytes())));
					transporte.call(SOAP_ACTION, envelope, headerList);

				} else {
					transporte.call(SOAP_ACTION, envelope);
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
		protected void onPostExecute(String aVoid) {
			if (_Exception == null) {

				NameSpace = "http://webservice.webcontrol.cl/";
				Url = EdTxtUrl.getText().toString();
				Usuario = EdTxtUsuario.getText().toString();
				Contrasenia = EdTxtContrasenia.getText().toString();

				if (CursorWs.moveToFirst()) {
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
				pDialog.setMessage(txtvalidarInternet);
				Cursor cursorConfig = Manager.CursorConfigApp();
				if (!cursorConfig.moveToFirst()) {
					Manager.InsertarConfigApp("SI", "SI", "SI", "SI");
				}
				Cursor SubirInfo = Manager.CursorSincronizacionLista();
				if (SubirInfo.moveToFirst()) {
					Manager.ActualizarSincronizacionLista("SI");
				}
				else{
					Manager.InsertarSincronizacionLista("SI");
				}
				new Conexion().execute(NameSpace, EdTxtUrlIntranet.getText()
						.toString(), EdTxtUsuarioIntranet.getText().toString(),
						EdTxtContraseniaIntranet.getText().toString());
			
			} else {
				pDialog.dismiss();
				// mostrarDialogo(ErrorInternet, "");
				AlertDialog dialogo = dialogoConfirmacionErroInternet(
						ErrorInternet, MensajeError);
				dialogo.show();
			}
		}
	}

	private AlertDialog dialogoConfirmacionErroInternet(String titulo,
			String mensaje) {
		// Instanciamos un nuevo AlertDialog Builder y le asociamos titulo y
		// mensaje
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(titulo);
		alertDialogBuilder.setMessage(mensaje);
		DialogInterface.OnClickListener listenerOk = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				NameSpace = "http://webservice.webcontrol.cl/";
				Url = EdTxtUrl.getText().toString();
				Usuario = EdTxtUsuario.getText().toString();
				Contrasenia = EdTxtContrasenia.getText().toString();
				if (CursorWs.moveToFirst()) {
					Manager.ActualizarConfigWs(NameSpace, Url, Usuario,
							Contrasenia, Autentificacion);
				} else {
					Manager.InsertarDatosConfig("", NameSpace, Url, Usuario,
							Contrasenia, Autentificacion);

				}

				Cursor VerEstado = Manager.CursorEstadoConexionWs();
				if (VerEstado.moveToFirst()) {
					Manager.ActualizarEstadoConexionWs("NO");
				} else {
					Manager.InsertarEstadoConexionWs("NO");
				}

				new Conexion().execute(NameSpace, EdTxtUrlIntranet.getText()
						.toString(), EdTxtUsuarioIntranet.getText().toString(),
						EdTxtContraseniaIntranet.getText().toString());
			}
		};

		// Creamos un nuevo OnClickListener para el boton Cancelar
		DialogInterface.OnClickListener listenerCancelar = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		};

		// Asignamos los botones positivo y negativo a sus respectivos listeners
		alertDialogBuilder.setPositiveButton(si, listenerOk);
		alertDialogBuilder.setNegativeButton(no, listenerCancelar);
		alertDialogBuilder.setIcon(R.drawable.ic_launcher);
		alertDialogBuilder.setCancelable(false);

		return alertDialogBuilder.create();
	}

	private AlertDialog dialogoConfirmacionIntranet(String titulo,
			String mensaje) {
		// Instanciamos un nuevo AlertDialog Builder y le asociamos titulo y
		// mensaje
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(titulo);
		alertDialogBuilder.setMessage(mensaje);
		DialogInterface.OnClickListener listenerOk = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				NameSpaceIntranet = "http://webservice.webcontrol.cl/";
				UrlIntranet = EdTxtUrlIntranet.getText().toString();
				UsuarioIntranet = EdTxtUsuarioIntranet.getText().toString();
				ContraseniaIntranet = EdTxtContraseniaIntranet.getText()
						.toString();
				// pDialog.setMessage("Validando Conexión... \n Conexion WS Intranet Correcta");

				if (CursorWSIntranet.moveToFirst()) {
					Manager.ActualizarConfigWsIntranet(NameSpaceIntranet,
							UrlIntranet, UsuarioIntranet, ContraseniaIntranet,
							AutentificacionIntranet);
				} else {
					Manager.InsertarDatosConfigIntranet("", NameSpaceIntranet,
							UrlIntranet, UsuarioIntranet, ContraseniaIntranet,
							AutentificacionIntranet);

				}
				Intent i = new Intent(ConfiguracionAvanzadaWS.this,
						ConfiguracionAvanzada.class);
				finish();
				startActivity(i);
			}
		};

		// Creamos un nuevo OnClickListener para el boton Cancelar
		DialogInterface.OnClickListener listenerCancelar = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		};

		// Asignamos los botones positivo y negativo a sus respectivos listeners
		alertDialogBuilder.setPositiveButton(si, listenerOk);
		alertDialogBuilder.setNegativeButton(no, listenerCancelar);
		alertDialogBuilder.setIcon(R.drawable.ic_launcher);
		alertDialogBuilder.setCancelable(false);

		return alertDialogBuilder.create();
	}

	@SuppressWarnings("deprecation")
	public void mostrarDialogo(String titulo, String mensaje) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(titulo);
		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// aquí puedes añadir funciones
				// Toast.makeText(getApplicationContext(),
				// "Comprendido",Toast.LENGTH_SHORT).show();
			}
		});
		alertDialog.setIcon(R.drawable.ic_launcher);
		alertDialog.show();
		alertDialog.setCancelable(false);
	}

	@SuppressWarnings("deprecation")
	public void mostrarDialogoCorrecto(String titulo, String mensaje) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(titulo);
		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// aquí puedes añadir funciones
				// Toast.makeText(getApplicationContext(),
				// "Comprendido",Toast.LENGTH_SHORT).show();
				Intent i = new Intent(ConfiguracionAvanzadaWS.this,
						ConfiguracionAvanzada.class);
				finish();
				startActivity(i);

			}
		});
		alertDialog.setIcon(R.drawable.ic_launcher);
		alertDialog.show();
		alertDialog.setCancelable(false);
	}

	public class Conexion extends AsyncTask<String, Void, String> {
		//String METHOD_NAME = "WSDivision";
		String _Respuesta;
		Exception _Exception = null;
		String tipo_Conexion;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog.setMessage(txtvalidarIntranet);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			// tipo_Conexion = params[4];
			//tareaLarga();
			String METHOD_NAME = "WsValidarConexion";
			String SOAP_ACTION = params[0]  + METHOD_NAME;

			try {
				SoapObject request = new SoapObject(params[0] , METHOD_NAME);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11); // la version de WS 1.1
				envelope.dotNet = true; // estamos utilizando .net
				envelope.setOutputSoapObject(request);
				HttpTransportSE transporte = new HttpTransportSE(params[1]);
				if (Autentificacion.equalsIgnoreCase("si")) {
					List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
					String Conexion = params[2] + ":" + params[3];
					headerList.add(new HeaderProperty("Authorization",
							"Basic "
									+ org.kobjects.base64.Base64.encode(Conexion
											.getBytes())));
					transporte.call(SOAP_ACTION, envelope, headerList);

				} else {
					transporte.call(SOAP_ACTION, envelope);
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
			return _Respuesta;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (_Exception == null) {
				NameSpaceIntranet = "http://webservice.webcontrol.cl/";
				UrlIntranet = EdTxtUrlIntranet.getText().toString();
				UsuarioIntranet = EdTxtUsuarioIntranet.getText().toString();
				ContraseniaIntranet = EdTxtContraseniaIntranet.getText()
						.toString();
				// pDialog.setMessage("Validando Conexión... \n Conexion WS Intranet Correcta");
				pDialog.setMessage(txtvalidarIntranet);
				if (CursorWSIntranet.moveToFirst()) {
					Manager.ActualizarConfigWsIntranet(NameSpaceIntranet,
							UrlIntranet, UsuarioIntranet, ContraseniaIntranet,
							AutentificacionIntranet);
				} else {
					Manager.InsertarDatosConfigIntranet("", NameSpaceIntranet,
							UrlIntranet, UsuarioIntranet, ContraseniaIntranet,
							AutentificacionIntranet);

				}
				// Cursor VerEstado = Manager.CursorEstadoConexionWs();
				// if (!VerEstado.moveToFirst()) {
				// Manager.InsertarEstadoConexionWs("CORRECTO");
				// }
				Cursor cursorConfig = Manager.CursorConfigApp();
				if (!cursorConfig.moveToFirst()) {
					Manager.InsertarConfigApp("NO", "SI", "SI", "SI");
				}

				pDialog.dismiss();
				mostrarDialogoCorrecto(CorrectoInternetEIntranet, "");

			} else if (_Exception != null) {

				pDialog.dismiss();
				// mostrarDialogo(ErrorIntranet, "");
				AlertDialog dialogo = dialogoConfirmacionIntranet(
						ErrorIntranet, MensajeError);
				dialogo.show();
			}
		}
	}

	public void BtnCancelar(View v) {
		Vibrador.vibrate(80);
		finish();
		startActivity(new Intent(getApplicationContext(),
				ConfiguracionAvanzada.class));
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	   if(keyCode == KeyEvent.KEYCODE_HOME)
	    {
	    return true;
	    }
	   if(keyCode==KeyEvent.KEYCODE_BACK)
	   {
		   return false;
	   }
	   return super.onKeyDown(keyCode, event);
	}


}
