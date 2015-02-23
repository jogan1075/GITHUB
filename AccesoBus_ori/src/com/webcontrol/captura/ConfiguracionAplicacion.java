package com.webcontrol.captura;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

public class ConfiguracionAplicacion extends Activity implements
		OnCheckedChangeListener, OnClickListener {
	String TextoCamara, TextoNFC, TextoLog;
	Switch switchCamara, switchNFC, switchLog, switchSincronizacion;
	DataBaseManager Manager;
	String Idioma;
	TextView CamaraConfig, NFCConfig, LogConfig, TextoArribaConfig,
			SincronizacionConfig;
	Button Aceptar, Cancelar;
	Vibrator vibrator;
	Cursor CursorAPP;
	String ConfigCamara, ConfigNfc, ConfigLog;
	String HabCamara, HabNFC, HabLog, HabSinc;
	Cursor VerEstado, ComprobarServicio;
	String estadoServicio = "";
	String SiNoServicio = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_configuracion_aplicacion);
		Manager = new DataBaseManager(this);

		CamaraConfig = (TextView) findViewById(R.id.camaraConfig);
		NFCConfig = (TextView) findViewById(R.id.NFCConfig);
		LogConfig = (TextView) findViewById(R.id.LogConfig);
		TextoArribaConfig = (TextView) findViewById(R.id.textoArribaConfig);
		SincronizacionConfig = (TextView) findViewById(R.id.txtSincronizacionConfigApp);

		switchCamara = (Switch) findViewById(R.id.switch1);
		switchCamara.setOnCheckedChangeListener(this);
		switchNFC = (Switch) findViewById(R.id.switch2);
		switchNFC.setOnCheckedChangeListener(this);
		switchLog = (Switch) findViewById(R.id.switch3);
		switchLog.setOnCheckedChangeListener(this);
		switchSincronizacion = (Switch) findViewById(R.id.switch4);
		switchSincronizacion.setOnCheckedChangeListener(this);

		Aceptar = (Button) findViewById(R.id.buttonGuardarConfigApp);
		Aceptar.setOnClickListener(this);
		Cancelar = (Button) findViewById(R.id.buttonCancelarConfigApp);
		Cancelar.setOnClickListener(this);

		HomeKeyLocker mHomeKeyLocker = new HomeKeyLocker();
		mHomeKeyLocker.lock(this);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		Cursor CursorIdioma = Manager.CursorIdioma();

		if (CursorIdioma.moveToFirst()) {
			Idioma = CursorIdioma.getString(0);
		}

		if (Idioma.equals("ESPANIOL")) {
			Textos("Lectura Cámara", "Lectura NFC", "Ver Log", "Apag.", "Enc.",
					"Cancelar", "Guardar", "Configuraciones \nControl Acceso",
					"Sincronización");
		} else if (Idioma.equals("INGLES")) {
			Textos("Reading Camera", "Reading NFC ", "Show Log", "Off", "On",
					"Cancel", "Save", "Access Control \nSettings",
					"Synchronization");
		} else if (Idioma.equals("PORTUGUES")) {
			Textos("Reading Câmara", "Leia NFC", "Veja o Log", "Desligar",
					"Ligar", "Cancelar", "Salvar",
					"Configurações de \ncontrole de acesso", "Sincronização");
		}

		CursorAPP = Manager.CursorConfigApp();
		if (CursorAPP.moveToFirst()) {
			do {
				ConfigCamara = CursorAPP.getString(0);
				ConfigNfc = CursorAPP.getString(1);
				ConfigLog = CursorAPP.getString(2);
				HabSinc = CursorAPP.getString(3);
			} while (CursorAPP.moveToNext());
		}
		if (ConfigCamara.equalsIgnoreCase("SI")) {
			switchCamara.setChecked(true);
			HabCamara = "SI";
		} else {
			switchCamara.setChecked(false);
			HabCamara = "NO";
		}
		if (ConfigNfc.equalsIgnoreCase("SI")) {
			switchNFC.setChecked(true);
			HabNFC = "SI";
		} else {
			switchNFC.setChecked(false);
			HabNFC = "NO";
		}
		if (ConfigLog.equalsIgnoreCase("SI")) {
			switchLog.setChecked(true);
			HabLog = "SI";
		} else {
			switchLog.setChecked(false);
			HabLog = "NO";
		}
		if (HabSinc.equalsIgnoreCase("SI")) {
			switchSincronizacion.setChecked(true);
			estadoServicio = "SI";
		} else {
			switchSincronizacion.setChecked(false);
			estadoServicio = "NO";
		}

		// ComprobarServicio = Manager.CursorEstadoConexionWs();
		// if (ComprobarServicio.moveToFirst()) {
		// do {
		// SiNoServicio = ComprobarServicio.getString(0);
		// } while (ComprobarServicio.moveToNext());
		// }

	}

	public void Textos(String txtCamara, String txtNFC, String txtLog,
			String Apagado, String Encendido, String cancelar, String aceptar,
			String textArriba, String txtSinc) {
		CamaraConfig.setText(txtCamara);
		NFCConfig.setText(txtNFC);
		LogConfig.setText(txtLog);
		switchCamara.setTextOn(Encendido);
		switchCamara.setTextOff(Apagado);
		switchNFC.setTextOn(Encendido);
		switchNFC.setTextOff(Apagado);
		switchLog.setTextOn(Encendido);
		switchLog.setTextOff(Apagado);
		Aceptar.setText(aceptar);
		Cancelar.setText(cancelar);
		TextoArribaConfig.setText(textArriba);
		SincronizacionConfig.setText(txtSinc);
		switchSincronizacion.setTextOff(Apagado);
		switchSincronizacion.setTextOn(Encendido);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		switch (buttonView.getId()) {
		case R.id.switch1:
			if (buttonView.isChecked()) {
				vibrator.vibrate(80);
				HabCamara = "SI";

			} else {
				vibrator.vibrate(80);
				HabCamara = "NO";
			}

			break;

		case R.id.switch2:
			if (buttonView.isChecked()) {
				vibrator.vibrate(80);
				HabNFC = "SI";
			} else {
				vibrator.vibrate(80);
				HabNFC = "NO";
			}

			break;
		case R.id.switch3:
			if (buttonView.isChecked()) {
				vibrator.vibrate(80);
				HabLog = "SI";
			} else {
				vibrator.vibrate(80);
				HabLog = "NO";
			}

			break;
		case R.id.switch4:
			if (buttonView.isChecked()) {
				vibrator.vibrate(80);
				estadoServicio = "SI";
			} else {
				vibrator.vibrate(80);
				estadoServicio = "NO";
			}

			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.buttonGuardarConfigApp:
			// Toast.makeText(getApplicationContext(), "Guardado",
			// Toast.LENGTH_SHORT).show();
			vibrator.vibrate(80);

			Cursor VerConfig = Manager.CursorConfigApp();
			if (VerConfig.moveToFirst()) {
				Manager.ActualizarConfigApp(HabCamara, HabNFC, HabLog,
						estadoServicio);
			} else {
				Manager.InsertarConfigApp(HabCamara, HabNFC, HabLog,
						estadoServicio);
			}

			// if (HabNFC.equalsIgnoreCase("NO")){
			// Cursor cambiar = Manager.cursorContador();
			// if (cambiar.moveToFirst()) {
			// Manager.ActualizarContador("NO");
			// } else {
			// Manager.InsertarContador("NO");
			// }
			// }
			// else{
			// Cursor cambiar = Manager.cursorContador();
			// if (cambiar.moveToFirst()) {
			// Manager.ActualizarContador("SI");
			// } else {
			// Manager.InsertarContador("SI");
			// }
			// }

			VerEstado = Manager.CursorEstadoConexionWs();
			if (VerEstado.moveToFirst()) {
				Manager.ActualizarEstadoConexionWs(estadoServicio);
			} else {
				Manager.InsertarEstadoConexionWs(estadoServicio);
			}

			finish();
			startActivity(new Intent(getApplicationContext(),
					ConfiguracionAvanzada.class));
			break;

		case R.id.buttonCancelarConfigApp:
			vibrator.vibrate(80);
			finish();
			startActivity(new Intent(getApplicationContext(),
					ConfiguracionAvanzada.class));
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
