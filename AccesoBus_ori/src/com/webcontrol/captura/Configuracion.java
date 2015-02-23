package com.webcontrol.captura;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import Metodos.MetodosGenerales;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

public class Configuracion extends Activity implements OnCheckedChangeListener {

	private Vibrator Vibrador;
	private DataBaseManager Manager;
	private String FechaHora, Fecha, Hora, Idioma;
	private HomeKeyLocker mHomeKeyLocker;

	Button BtnSincronizacion, BtnFinGuardia, BtnVerLog, BtnVerControlAcceso,
			BtnInformacionDispositivo, BtnConfigIdioma, BtnRegresar,
			BtnAvanzado;
	Cursor CursorPantalla;
	String Pantalla;
	Cursor DeshabilitarLog;

	String EstadoLog = "";
	String tipoConexion, TresG, wifi;
	Switch switchConecction;
	TextView txt;
	Vibrator vibrator;
	WifiManager wifiManager;
	Method dataConnSwitchmethod_ON;
	Method dataConnSwitchmethod_OFF;
	Class telephonyManagerClass;
	Object ITelephonyStub;
	Class ITelephonyClass;
	String tipo_Conexion3gWIFI = "";
	String NombreConexionql;
	boolean currentFocus;
	TextView recordatorio;
	// To keep track of activity's foreground/background status
	boolean isPaused;

	Handler collapseNotificationHandler;
	String MovilOn;
	String WifiOn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		 getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.configuracion_layout);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		HomeKeyLocker mHomeKeyLocker = new HomeKeyLocker();
		mHomeKeyLocker.lock(this);
		recordatorio = (TextView) findViewById(R.id.txtRecordatorio);
		Manager = new DataBaseManager(this);
		txt = (TextView) findViewById(R.id.txtTipoConexion);
		switchConecction = (Switch) findViewById(R.id.switchConexionInternet);
		switchConecction.setOnCheckedChangeListener(this);
		Vibrador = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
		BtnSincronizacion = (Button) findViewById(R.id.button1);
		BtnFinGuardia = (Button) findViewById(R.id.button2);
		BtnVerLog = (Button) findViewById(R.id.button3);
		BtnVerControlAcceso = (Button) findViewById(R.id.button4);
		BtnInformacionDispositivo = (Button) findViewById(R.id.button5);
		BtnRegresar = (Button) findViewById(R.id.buttonRegresar);
		BtnAvanzado = (Button) findViewById(R.id.buttonAvanzado);
		
		MetodosGenerales util = new MetodosGenerales();
		
		TextView version = (TextView)findViewById(R.id.txtversion);
		version.setText("Version:  "+ util.devolverVersionApp(this));

		Cursor CursorIdioma = Manager.CursorIdioma();

		if (CursorIdioma.moveToFirst()) {
			Idioma = CursorIdioma.getString(0);
		}

		if (Idioma.equals("ESPANIOL")) {
			Textos("Configuraciones \nControl Acceso", "Finalizar Vigilante",
					"Ver Log", "Ver Control Acceso Local",
					"Información del Dispositivo", "", "Regresar", "Avanzado",
					"Tipo de Conexión", "Internet", "Intranet",
					"Internet desactivado, Activando Intranet...",
					"Intranet desactivado, Activando Internet...",
					"Nota: Deslice para conectar a Internet o Intranet");
		} else if (Idioma.equals("INGLES")) {
			Textos("Access Control \nSettings", "Finish Guard", "View Log",
					"View Local Control Access", "Device Information", "",
					"Back", "Advanced", "Connection Type", "Internet",
					"Intranet", "Internet off, activating Intranet...",
					"Intranet off, activating Internet...",
					"Note : Push to connect to the Internet or Intranet");
		} else if (Idioma.equals("PORTUGUES")) {
			Textos("Configurações de \ncontrole de acesso", "Concluir Guarda",
					"Ver Registo", "Ver Local Controle de Acesso",
					"Informações Sobre o Dispositivo", "", "Voltar",
					"Avançado", "Tipo de conexão", "Internet", "Intranet",
					"Internet off , ativando Intranet...",
					"Intranet off , ativando Internet...",
					"Nota: Empurre para se conectar à Internet ou Intranet");
		}

		CursorPantalla = Manager.BuscarPantalla();
		if (CursorPantalla.moveToFirst()) {
			do {
				Pantalla = CursorPantalla.getString(0);

			} while (CursorPantalla.moveToNext());

			if (Pantalla.equals("Guardia")) {
				BtnFinGuardia.setTextColor(Color.GRAY);
				BtnFinGuardia.setEnabled(false);

			}
		}
		DeshabilitarLog = Manager.CursorConfigApp();
		if (DeshabilitarLog.moveToFirst()) {
			do {
				EstadoLog = DeshabilitarLog.getString(2);
			} while (DeshabilitarLog.moveToNext());
		}
		if (EstadoLog.equalsIgnoreCase("NO")) {
			BtnVerLog.setEnabled(false);
			BtnVerLog.setTextColor(Color.GRAY);
		} else {
			BtnVerLog.setEnabled(true);
			BtnVerLog.setTextColor(Color.WHITE);
		}
		FechaHora = getDateTime();
		Fecha = FechaHora.substring(0, 10);
		Hora = FechaHora.substring(11, FechaHora.length());
		Cursor CursorTipoConexion = Manager.CursorTipoConexion();
		if (CursorTipoConexion.moveToFirst()) {
			tipo_Conexion3gWIFI = CursorTipoConexion.getString(0);
		}
		if (tipo_Conexion3gWIFI.equalsIgnoreCase("INTRANET")) {
			switchConecction.setChecked(false);
			NombreConexionql = "INTRANET";
		} else {
			switchConecction.setChecked(true);
			NombreConexionql = "INTERNET";
		}
	}

	public void Sincronizacion(View v) {
		Vibrador.vibrate(80);
		Intent i = new Intent(this, Sincronizacion.class);
		finish();
		startActivity(i);
	}

	public void VerControlAcceso(View v) {
		Vibrador.vibrate(80);
		Intent i = new Intent(this, VerControlAcceso.class);
		finish();
		startActivity(i);
	}

	public void VerLog(View v) {
		Vibrador.vibrate(80);
		Intent i = new Intent(this, VerLog.class);
		finish();
		startActivity(i);
	}

	public void InformacionDispositivo(View v) {
		Vibrador.vibrate(80);
		Intent i = new Intent(this, InformacionDispositivo.class);
		finish();
		startActivity(i);
	}

	// public void ConfigIdioma(View v) {
	// Vibrador.vibrate(80);
	// Intent i = new Intent(this, ConfigIdioma.class);
	// finish();
	// startActivity(i);
	// }

	public void Regresar(View v) {
		Vibrador.vibrate(80);

		Cursor CursorTipoConexion = Manager.CursorTipoConexion();
		if (CursorTipoConexion.moveToFirst()) {
			Manager.ActualizarTipoConexion(NombreConexionql);
		} else {
			Manager.InsertarTipoConexion(NombreConexionql);
		}

		Intent i = new Intent(this, inicio.class);
		finish();
		startActivity(i);
	}

	public void Avanzada(View v) {
		Vibrador.vibrate(80);
		Cursor CursorTipoConexion = Manager.CursorTipoConexion();
		if (CursorTipoConexion.moveToFirst()) {
			Manager.ActualizarTipoConexion(NombreConexionql);
		} else {
			Manager.InsertarTipoConexion(NombreConexionql);
		}
		Intent i = new Intent(this, ConfiguracionAvanzadacontrasenia.class);
		finish();
		startActivity(i);
	}

	public void FinGuardia(View v) {
		Vibrador.vibrate(80);
		Manager.ActualizarPantalla("Guardia");
//		Manager.InsertarDatosLog("Fin Vigilante", Fecha, Hora);

		BtnFinGuardia.setTextColor(Color.GRAY);
		BtnFinGuardia.setEnabled(false);

		Manager.EliminarGuardia();

	}

	public AlertDialog dialogoConfirmacion(String titulo, String mensaje,
			String accion) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				Configuracion.this);
		alertDialogBuilder.setTitle(titulo);
		alertDialogBuilder.setMessage(mensaje);

		DialogInterface.OnClickListener listenerOk = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		};

		DialogInterface.OnClickListener listenerCancelar = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		};

		alertDialogBuilder.setPositiveButton("Si", listenerOk);
		alertDialogBuilder.setNegativeButton("No", listenerCancelar);
		alertDialogBuilder.setIcon(R.drawable.ic_launcher);
		alertDialogBuilder.setCancelable(false);

		return alertDialogBuilder.create();
	}

	public void Textos(String TextoSincronizacion, String TextoFinGuardia,
			String TextoVerLog, String TextoVerControlAcceso,
			String TextoInformacionDispositivo, String TextoConfigIdioma,
			String TextoRegresar, String TextoAvanzado, String conexionTipo,
			String tresGe, String WifiConexion, String wifiON, String movilON,
			String recordar) {
		BtnSincronizacion.setText(TextoSincronizacion);

		BtnFinGuardia.setText(TextoFinGuardia);
		BtnVerLog.setText(TextoVerLog);
		BtnVerControlAcceso.setText(TextoVerControlAcceso);
		BtnInformacionDispositivo.setText(TextoInformacionDispositivo);
		// BtnConfigIdioma.setText(TextoConfigIdioma);
		BtnRegresar.setText(TextoRegresar);
		BtnAvanzado.setText(TextoAvanzado);
		tipoConexion = conexionTipo;
		TresG = tresGe;
		wifi = WifiConexion;
		switchConecction.setTextOn(tresGe);
		switchConecction.setTextOff(WifiConexion);
		txt.setText(conexionTipo);
		MovilOn = movilON;
		WifiOn = wifiON;
		recordatorio.setText(recordar);
	}

	@Override
	public void onBackPressed() {

	}

	private String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	private void setMobileDataEnabled(Context context, boolean enabled)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, ClassNotFoundException,
			NoSuchMethodException, NoSuchFieldException {
		final ConnectivityManager conman = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final Class conmanClass = Class.forName(conman.getClass().getName());
		final Field iConnectivityManagerField = conmanClass
				.getDeclaredField("mService");
		iConnectivityManagerField.setAccessible(true);
		final Object iConnectivityManager = iConnectivityManagerField
				.get(conman);
		final Class iConnectivityManagerClass = Class
				.forName(iConnectivityManager.getClass().getName());
		final Method setMobileDataEnabledMethod = iConnectivityManagerClass
				.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
		setMobileDataEnabledMethod.setAccessible(true);

		setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		switch (buttonView.getId()) {
		case R.id.switchConexionInternet:
			if (buttonView.isChecked()) {
				vibrator.vibrate(80);

				NombreConexionql = "INTERNET";
				// setMobileDataEnabled(getApplicationContext(), true);

				// wifiManager.setWifiEnabled(false);
				Cursor CursorTipoConexion = Manager.CursorTipoConexion();
				if (CursorTipoConexion.moveToFirst()) {
					Manager.ActualizarTipoConexion("INTERNET");
				} else {
					Manager.InsertarTipoConexion("INTERNET");
				}

//				Toast.makeText(getApplicationContext(), MovilOn,
//						Toast.LENGTH_SHORT).show();
			} else {
				vibrator.vibrate(80);

				// setMobileDataEnabled(getApplicationContext(), false);
				// wifiManager.setWifiEnabled(true);
				NombreConexionql = "INTRANET";
				Cursor CursorTipoConexion = Manager.CursorTipoConexion();
				if (CursorTipoConexion.moveToFirst()) {
					Manager.ActualizarTipoConexion("INTRANET");
				} else {
					Manager.InsertarTipoConexion("INTRANET");
				}
//
//				Toast.makeText(getApplicationContext(), WifiOn,
//						Toast.LENGTH_SHORT).show();
			}

			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	   if(keyCode == KeyEvent.KEYCODE_HOME)
	    {
	    // Log.i("Home Button","Clicked");
	    }
	   if(keyCode==KeyEvent.KEYCODE_BACK)
	   {
	        finish();
	   }
	 return false;
	}


}
