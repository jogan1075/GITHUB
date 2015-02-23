package com.webcontrol.captura;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import android.widget.Toast;

public class ConfiguracionAvanzada extends Activity {
	Cursor CursorWs;
	private Vibrator Vibrador;
	DataBaseManager Manager;
	String NameSpace, Url, Usuario, Contrasenia, Autentificacion;
	private String FechaHora, Fecha, Hora, Idioma, Toast1;
	private Button Btn1, Btn2, Btn3, Btn4, Btn5, BtnRegresar, BtnBasica,
			btnSalir, btnIdioma;
	String EstadoLog = "";
	Cursor DeshabilitarLog;
	String tituloDialogo, mensajeDialogo, SiDialogo, NoDialogo;
	String textoBotonSalir;
	String nombreGuardiaDefecto;
	String apellidoGuardiaDefecto;
	String errorWSdeNuevo;
	String idiomaql;
	String noInternet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.configuracionavanzada_layout);
		Manager = new DataBaseManager(this);
		Vibrador = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);

		Btn1 = (Button) findViewById(R.id.button1);
		Btn2 = (Button) findViewById(R.id.button2);
		Btn3 = (Button) findViewById(R.id.button3);
		Btn4 = (Button) findViewById(R.id.button4);
		Btn5 = (Button) findViewById(R.id.button5);
		btnIdioma = (Button) findViewById(R.id.button7);
		BtnRegresar = (Button) findViewById(R.id.buttonRegresar);
		BtnBasica = (Button) findViewById(R.id.buttonMenuBasica);
		btnSalir = (Button) findViewById(R.id.button6);

		HomeKeyLocker mHomeKeyLocker = new HomeKeyLocker();
		mHomeKeyLocker.lock(this);

		Cursor CursorIdioma = Manager.CursorIdioma();

		CursorWs = Manager.CursorConfig();
		if (CursorIdioma.moveToFirst()) {
			Idioma = CursorIdioma.getString(0);
		}

		if (Idioma.equals("ESPANIOL")) {
			Textos("Conexión WS", "Información del Dispositivo",
					"Sincronización", "Iniciar Guardia", "Configuración App",
					"Regresar", "Básica", "Configure el Servicio Web",
					"Saliendo de la Aplicación", "¿Está Seguro/a?", "Si", "No",
					"Salir", "Iniciado", "Manualmente",
					"Error de Conexión WS Intente Nuevamente", "Idioma",
					"Error Conexion Internet");
		} else if (Idioma.equals("INGLES")) {
			Textos("WS Connection", "Device Information", "Mobile Connection",
					"Start Guard", "App settings", "Back", "Basic",
					"Configure the Web Service", "Leaving the Application",
					"Are you sure ?", "Yes", "No", "Leave", "Originally",
					"Manually", "WS Connection Error Try Again", "Language",
					"Erro de ligação à Internet");
		} else if (Idioma.equals("PORTUGUES")) {
			Textos("Conexão SW", "Informações Sobre o Dispositivo",
					"Conexão Móvel", "Iniciar Guarda",
					"Configurações de aplicativos", "Voltar", "Básico",
					"Configurar o Serviço Web", "Saindo da Aplicação",
					"Você tem certeza ?", "Sim", "Não", "Deixar",
					"Originalmente", "Manualmente",
					"WS Erro de conexão Try Again", "Linguagem",
					"Erro de ligação à Internet");
		}

		FechaHora = getDateTime();
		Fecha = FechaHora.substring(0, 10);
		Hora = FechaHora.substring(11, FechaHora.length());

		DeshabilitarLog = Manager.CursorConfigApp();
		if (DeshabilitarLog.moveToFirst()) {
			do {
				EstadoLog = DeshabilitarLog.getString(2);
			} while (DeshabilitarLog.moveToNext());
		}

		if (CursorWs.moveToFirst()) {
			do {
				NameSpace = CursorWs.getString(1);
				Url = CursorWs.getString(2);
				Usuario = CursorWs.getString(3);
				Contrasenia = CursorWs.getString(4);
				Autentificacion = CursorWs.getString(5);

			} while (CursorWs.moveToNext());
		}
	}

	private void Textos(String Texto1, String Texto2, String Texto3,
			String Texto4, String Texto5, String TextoRegresar,
			String TextoBasica, String TextoToast1, String titulo,
			String mensaje, String si, String no, String mensajeBoton,
			String nombre, String apellido, String wsError, String idioma,
			String conexionnull) {

		Btn1.setText(Texto1);
		Btn2.setText(Texto2);
		Btn3.setText(Texto3);
		Btn4.setText(Texto4);
		Btn5.setText(Texto5);
		BtnRegresar.setText(TextoRegresar);
		BtnBasica.setText(TextoBasica);
		Toast1 = TextoToast1;
		tituloDialogo = titulo;
		mensajeDialogo = mensaje;
		SiDialogo = si;
		NoDialogo = no;
		btnSalir.setText(mensajeBoton);
		nombreGuardiaDefecto = nombre;
		apellidoGuardiaDefecto = apellido;
		errorWSdeNuevo = wsError;
		btnIdioma.setText(idioma);
		noInternet = conexionnull;
	}

	public void ConfigIdioma(View v) {
		Vibrador.vibrate(80);
		Intent i = new Intent(this, ConfigIdioma.class);
		finish();
		startActivity(i);
	}

	public void BtnConexionWs(View v) {
		Vibrador.vibrate(80);
		Intent i = new Intent(this, ConfiguracionAvanzadaWS.class);
		finish();
		startActivity(i);
	}

	public void BtnInfoDispAvan(View v) {
		Vibrador.vibrate(80);

		// Cursor CursorWs = Manager.CursorConfig();
		//
		// if (CursorWs.moveToFirst()) {
		// if (isOnline()) {
		// new ComprobacionConexionParaMenu().execute(NameSpace, Url,
		// Usuario, Contrasenia, Autentificacion, "InfoCelu");
		// } else {
		// Toast.makeText(getApplicationContext(), noInternet,
		// Toast.LENGTH_SHORT).show();
		// }
		//
		// } else {
		// Toast.makeText(getApplicationContext(), Toast1, Toast.LENGTH_LONG)
		// .show();
		// }

		Intent i = new Intent(this, InformacionDispositivoAvanzado.class);
		finish();
		startActivity(i);
	}

	public void BtnConfigBasica(View v) {
		Vibrador.vibrate(80);
		Intent i = new Intent(this, Configuracion.class);
		finish();
		startActivity(i);
	}

	public void btnConfiguracionApp(View v) {
		Vibrador.vibrate(80);
		// LaunchComponent ("com.teamviewer.quicksupport.market",
		// "QuickSupport");
		// Cursor CursorWs = Manager.CursorConfig();
		//
		// if (CursorWs.moveToFirst()) {
		// if (isOnline()) {
		// new ComprobacionConexionParaMenu().execute(NameSpace, Url,
		// Usuario, Contrasenia, Autentificacion, "ConfigApp");
		//
		// } else {
		// Toast.makeText(getApplicationContext(), noInternet,
		// Toast.LENGTH_SHORT).show();
		// }
		// } else {
		// Toast.makeText(getApplicationContext(), Toast1, Toast.LENGTH_LONG)
		// .show();
		// }

		Intent i = new Intent(this, ConfiguracionAplicacion.class);
		finish();
		startActivity(i);

	}

	public boolean isOnline() {
		boolean enabled = true;

		ConnectivityManager connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();

		if ((info == null || !info.isConnected() || !info.isAvailable())) {
			enabled = false;
		}
		return enabled;
	}

	public void InicioGuardia(View v) {
		Vibrador.vibrate(80);
//		Cursor CursorWs = Manager.CursorConfig();
//		if (CursorWs.moveToFirst()) {
//			if (isOnline()) {
//				new ComprobacionConexionParaMenu()
//						.execute(NameSpace, Url, Usuario, Contrasenia,
//								Autentificacion, "IniciarGuardia");
//			} else {
//				finish();
//				startActivity(new Intent(getApplicationContext(), inicio.class));
//			}
//		} else {
//			Toast.makeText(getApplicationContext(), Toast1, Toast.LENGTH_LONG)
//					.show();
//		}
		
		Cursor CursorDatosGuardia = Manager
				.CursorDevolverDatosGuardia();
		if (CursorDatosGuardia.moveToFirst()) {
			int idguardia = Manager.devolverIdGuardia();
			Manager.ActualizarDatosGuardiaconid(
					nombreGuardiaDefecto, apellidoGuardiaDefecto,
					idguardia);

		} else {
			Manager.InsertarDatosGuardia(nombreGuardiaDefecto,
					apellidoGuardiaDefecto);

		}

		Manager.ActualizarPantalla("PersonaIngreso");
		finish();
		startActivity(new Intent(getApplicationContext(),
				inicio.class));

	}

	// public void BtnConexionMovil(View v) {
	// Vibrador.vibrate(80);
	// Cursor CursorWs = Manager.CursorConfig();
	//
	// if (CursorWs.moveToFirst()) {
	// if (isOnline()) {
	// new ComprobacionConexionParaMenu().execute(NameSpace, Url,
	// Usuario, Contrasenia, Autentificacion, "ConexionMovil");
	// } else {
	// Toast.makeText(getApplicationContext(), noInternet,
	// Toast.LENGTH_SHORT).show();
	// }
	//
	// } else {
	// Toast.makeText(getApplicationContext(), Toast1, Toast.LENGTH_LONG)
	// .show();
	// }
	//
	// }

	public void btnSincronizarInicial(View v) {
		Vibrador.vibrate(80);
		Intent i = new Intent(this, Sincronizacion.class);
		finish();
		startActivity(i);
	}

	public void Regresar(View v) {
		Vibrador.vibrate(80);
		Intent i = new Intent(this, inicio.class);
		finish();
		startActivity(i);
	}

	public void btnSalirApp(View v) {
		Vibrador.vibrate(80);
		AlertDialog dialogo = dialogoConfirmacion(tituloDialogo,
				mensajeDialogo, "");
		dialogo.show();

	}

	private String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_HOME) {
			// Log.i("Home Button","Clicked");
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return false;
	}

	private AlertDialog dialogoConfirmacion(String titulo, String mensaje,
			final String direccion) {
		// Instanciamos un nuevo AlertDialog Builder y le asociamos titulo y
		// mensaje
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(titulo);
		alertDialogBuilder.setMessage(mensaje);

		// Creamos un nuevo OnClickListener para el boton OK que realice la
		// conexion
		DialogInterface.OnClickListener listenerOk = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				finish();
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				startActivity(intent);
			}
		};

		// Creamos un nuevo OnClickListener para el boton Cancelar
		DialogInterface.OnClickListener listenerCancelar = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		};

		// Asignamos los botones positivo y negativo a sus respectivos listeners
		alertDialogBuilder.setPositiveButton(SiDialogo, listenerOk);
		alertDialogBuilder.setNegativeButton(NoDialogo, listenerCancelar);
		alertDialogBuilder.setIcon(R.drawable.ic_launcher);
		alertDialogBuilder.setCancelable(false);

		return alertDialogBuilder.create();
	}

	public class ComprobacionConexionParaMenu extends
			AsyncTask<String, Void, String> {
		String _Respuesta;
		JSONObject _RespuestaJson;
		Exception _Exception = null;
		String resultado_json;
		String IniciarPantalla;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			IniciarPantalla = params[5];
			// String METHOD_NAME = "WSSincronizar";
			String METHOD_NAME = "WsValidarConexion";
			String SOAP_ACTION = params[0] + METHOD_NAME;
			SoapObject request = new SoapObject(params[0], METHOD_NAME);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // la version de WS 1.1
			envelope.dotNet = true; // estamos utilizando .net
			envelope.setOutputSoapObject(request);
			HttpTransportSE transporte = new HttpTransportSE(params[1]);

			try {
				if (Autentificacion.equals("SI")) {
					// *****
					List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
					String Conexion = params[2] + ":" + params[3];
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
			if (_Exception == null) {

				if (IniciarPantalla.equalsIgnoreCase("InfoCelu")) {
//					Intent i = new Intent(ConfiguracionAvanzada.this,
//							InformacionDispositivoAvanzado.class);
//					finish();
//					startActivity(i);
				} 
//				else if (IniciarPantalla.equalsIgnoreCase("ConexionMovil")) {
//					Vibrador.vibrate(80);
//					Manager.ActualizarConexion("Movil", 0, getDateTime());
//
//					FechaHora = getDateTime();
//					Fecha = FechaHora.substring(0, 10);
//					Hora = FechaHora.substring(11, FechaHora.length());
//					if (EstadoLog.equalsIgnoreCase("SI")) {
//						Manager.InsertarDatosLog(
//								"CONEXION MOVIL ACTIVADA DE MANERA MANUAL",
//								Fecha, Hora);
//					}
//				} 
				else if (IniciarPantalla.equalsIgnoreCase("IniciarGuardia")) {
					Cursor CursorDatosGuardia = Manager
							.CursorDevolverDatosGuardia();
					if (CursorDatosGuardia.moveToFirst()) {
						int idguardia = Manager.devolverIdGuardia();
						Manager.ActualizarDatosGuardiaconid(
								nombreGuardiaDefecto, apellidoGuardiaDefecto,
								idguardia);

					} else {
						Manager.InsertarDatosGuardia(nombreGuardiaDefecto,
								apellidoGuardiaDefecto);

					}

					Manager.ActualizarPantalla("PersonaIngreso");
					finish();
					startActivity(new Intent(getApplicationContext(),
							inicio.class));
				} 
				else if (IniciarPantalla.equalsIgnoreCase("ConfigApp")) {

					Intent i = new Intent(ConfiguracionAvanzada.this,
							ConfiguracionAplicacion.class);
					finish();
					startActivity(i);
				}
			} else {
				Toast.makeText(getApplicationContext(), errorWSdeNuevo,
						Toast.LENGTH_LONG).show();
				finish();
				startActivity(new Intent(getApplicationContext(),
						ConfiguracionAvanzadaWS.class));
			}

		}

	}
}