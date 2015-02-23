package com.webcontrol.captura;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import Hilos.Guardia;
import Hilos.SubirDatos;
import Metodos.MetodosGenerales;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.webcontrol.captura.R.drawable;

public class inicio extends Activity {

	public static final String MIME_TEXT_PLAIN = "text/plain";
	public static final String TAG = "NfcDemo";
	private NfcAdapter mNfcAdapter;
	private DataBaseManager Manager;

	private Cursor CursorPantalla, CursorEstadoChoferPasajero, CursorConexion,
			CursorSincronizacion, CursorSincronizacionIntranet;
	private Handler handler = new Handler();
	Button BtnPersonaIngreso, BtnPersonaSalida, BtnVehiculoIngreso,
			BtnVehiculoSalida, BtnManual, BtnConfigAnularFinalizar;
	LinearLayout LlPersona, LlVehiculo, LlChoferPasajero;
	String SentidoChoferPasajero = "", Pantalla, EstadoSync, TipoSync, Idioma,
			Txt1, Txt2, Txt3, Txt4, Txt5, Txt6, Txt7, Txt8, Txt9, Txt10, Txt11,
			Toast1, Toast2, EstadoSyncIntranet, TipoSyncIntranet;
	private HomeKeyLocker mHomeKeyLocker;
	Bundle bundle;
	Vibrator Vibrador;
	private Cursor IniciarServicio;
	Cursor CursorDatosGuardia;
	TextView txtVigitante;
	String tituloDialogo;
	String mensajeDialogo;
	Cursor ConfigAppAcceso;
	String camaraAcceso = "", NfcAcceso = "";
	LinearLayout layoutCamara;
	static int contadorNfc = 0;
	int nfc = 0;
	Camera cam = null;
	String EstadoSincronizacion = "";
	String EjecutarSoloCamara = "";
	Cursor SubirInfo;
	String estadoSubirInfo = "";
	String Colorfoto = "";
	ImageView ImagenSincronizando;
	Cursor VerificarSync;
	
	MetodosGenerales util;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.inicio);
		LinearLayout linearDatosGuardia = (LinearLayout) findViewById(R.id.LinearLayoutMenuarriba);

		linearDatosGuardia.setVisibility(View.INVISIBLE);
		TextView nombreApellidoGuardia = (TextView) findViewById(R.id.txtNombreApellidosGuardia);
		Manager = new DataBaseManager(this);
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		layoutCamara = (LinearLayout) findViewById(R.id.LinearLayoutMenuCaptura);
		ImagenSincronizando = (ImageView) findViewById(R.id.imagenColorVerdeAzulRojo);

		HomeKeyLocker mHomeKeyLocker = new HomeKeyLocker();
		mHomeKeyLocker.lock(this);

		util = new MetodosGenerales();
		final ImageView imagebateria = (ImageView) findViewById(R.id.imagenBateria);

		final TextView bateria = (TextView) findViewById(R.id.txtbateria);

		BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context ctxt, Intent intent) {
				int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
				if (level == 100 || level > 80) {
					imagebateria.setImageResource(drawable.bateriafull);
				} else if (level == 80 || level > 50) {
					imagebateria.setImageResource(drawable.bateria80);
				} else if (level == 50 || level > 25) {
					imagebateria.setImageResource(drawable.bateria50);
				} else if (level == 25 || level > 10) {
					imagebateria.setImageResource(drawable.bateria25);
				} else if (level == 10 || level < 10) {
					imagebateria.setImageResource(drawable.bateria5);
				}

				bateria.setText(String.valueOf(level) + "%");
			}
		};
		this.registerReceiver(mBatInfoReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));

		bundle = getIntent().getExtras();
		CursorDatosGuardia = Manager.CursorDevolverDatosGuardia();
		if (CursorDatosGuardia.moveToFirst()) {
			do {
				nombreApellidoGuardia.setText(CursorDatosGuardia.getString(0)
						+ " " + CursorDatosGuardia.getString(1));
			} while (CursorDatosGuardia.moveToNext());
			linearDatosGuardia.setVisibility(View.VISIBLE);

			Pantalla = "PersonaIngreso";

			// String idioma = SeleccionarIdioma();
			// if (idioma.equalsIgnoreCase("espaniol")){
			// nombreApellidoGuardia.setText()
			// }else if (idioma.equalsIgnoreCase("INGLES")){
			// nombreApellidoGuardia.setText()
			// }
			// else{
			// nombreApellidoGuardia.setText();
			// }
		} else {
			Cursor CursorPantalla = Manager.BuscarPantalla();
			if (!CursorPantalla.moveToFirst()) {
				Manager.InsertarDatosPantalla("Guardia");
				Pantalla = "Guardia";
			}

		}

		VerificarSync = Manager.VerificarSync();
		if (VerificarSync.moveToFirst()) {
			// new Guardia(getApplicationContext()).execute();
			// startService(new Intent(getBaseContext(), UpdateData.class));
		}

		Cursor VerColorFoto = Manager.CursorCambiarFoto();
		if (VerColorFoto.moveToFirst()) {
			Colorfoto = VerColorFoto.getString(0);
			Manager.ActualizarColorFoto("verde");
		} else {
			Manager.InsertarColorFoto("verde");
		}

		ConfigAppAcceso = Manager.CursorConfigApp();
		if (ConfigAppAcceso.moveToFirst()) {
			do {
				camaraAcceso = ConfigAppAcceso.getString(0);
				// NfcAcceso = ConfigAppAcceso.getString(1);
			} while (ConfigAppAcceso.moveToNext());

		}
		if (camaraAcceso.equalsIgnoreCase("NO")) {
			layoutCamara.setVisibility(View.INVISIBLE);
			layoutCamara.setEnabled(false);
		} else {
			layoutCamara.setVisibility(View.VISIBLE);
			layoutCamara.setEnabled(true);
		}

		// startService(new Intent(getBaseContext(), UpdateData.class));

		Vibrador = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
		txtVigitante = (TextView) findViewById(R.id.txtVigilante);
		BtnPersonaIngreso = (Button) findViewById(R.id.buttonPersonaIngreso1);
		BtnPersonaSalida = (Button) findViewById(R.id.buttonPersonaSalida);
		BtnVehiculoIngreso = (Button) findViewById(R.id.buttonVehiculoIngreso);
		BtnVehiculoSalida = (Button) findViewById(R.id.buttonVehiculoSalida);
		BtnManual = (Button) findViewById(R.id.buttonMenuIngresoManual);
		BtnConfigAnularFinalizar = (Button) findViewById(R.id.buttonMenuConfig);

		LlPersona = (LinearLayout) findViewById(R.id.LinearLayoutPersona);
		LlVehiculo = (LinearLayout) findViewById(R.id.LinearLayoutVehiculo);
		LlChoferPasajero = (LinearLayout) findViewById(R.id.LinearLayoutChoferPasajero);

		if (!isOnline2()) {
			Cursor verFoto = Manager.CursorCambiarFoto();
			if (verFoto.moveToFirst()) {
				Manager.ActualizarColorFoto("rojo");

			} else {

				Manager.InsertarColorFoto("rojo");

			}
		} else {
			Cursor verFoto = Manager.CursorCambiarFoto();
			if (verFoto.moveToFirst()) {
				Manager.ActualizarColorFoto("verde");

			}

		}
		Cursor CursorIdioma = Manager.CursorIdioma();

		if (CursorIdioma.moveToFirst()) {
			Idioma = CursorIdioma.getString(0);
		} else {
			Idioma = SeleccionarIdioma();
		}

		if (Idioma.equals("ESPANIOL")) {
			Textos("Ingreso Manual", "Vigilante", "Anular",
					"Conductor Ingreso", "Conductor Salida", "Finalizar",
					"Pasajero Ingreso", "Pasajero Salida", "", "",
					"Salida Manual", "", "", "", "NFC desactivado",
					"Este dispositivo no soporta NFC.", "Vigilante:",
					"NFC desactivado", "Haga click Para Habilitarlo");
		} else if (Idioma.equals("INGLES")) {
			Textos("Manual Check In", "Guard", "Annular", "Drive Check In",
					"Drive Check Out", "Finish", "Passenger Check In",
					"Passenger Check Out", "", "", "Manual Check Out", "", "",
					"", "NFC disabled", "This device does not support NFC.",
					"Guard:", "NFC disabled", "Click to enable it");
		} else if (Idioma.equals("PORTUGUES")) {
			Textos("Junte Manual", "Guarda", "Anular", "Verifique na Unidade",
					"Confira Unidade", "Concluir", " Verifique na Passageiros",
					"Confira Passageiros", "", "", "Saída Manual", "", "", "",
					"Desactivado NFC", "Este dispositivo não suporta NFC.",
					"Guarda:", "Desactivado NFC", "Clique para habilitá-lo");
		}

		CursorConexion = Manager.CursorConexion();

		if (!(CursorConexion.moveToFirst())) {
			Manager.InsertarDatosConexion(getDateTime()); // inserta conexion
															// movil
		} else {
			String TipoConexion = CursorConexion.getString(0);
			String FechaHoraAntigua = CursorConexion.getString(2);

			String FechaHoraNueva = getDateTime();
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date DateTimeOld = null;
			Date DateTimeNow = null;
			try {
				DateTimeOld = dateFormat.parse(FechaHoraAntigua);
				DateTimeNow = dateFormat.parse(FechaHoraNueva);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			long totalTime = DateTimeNow.getTime() - DateTimeOld.getTime();

			if ((totalTime >= 300000) && (TipoConexion.equals("Local"))) {
				Manager.ActualizarConexion("Movil", 0, getDateTime());
				// Toast.makeText(getApplicationContext(),
				// "Cambiado a movil despues de 5 minutos",
				// Toast.LENGTH_LONG).show();
			}

			// String DiferenciaTiempo = "" + totalTime;
		}

		CursorPantalla = Manager.BuscarPantalla();

		if (CursorPantalla.moveToFirst()) {
			do {
				Pantalla = CursorPantalla.getString(0);

			} while (CursorPantalla.moveToNext());

		} else {
			Manager.InsertarDatosPantalla("Guardia");
			Pantalla = "Guardia";
		}

		if (Pantalla.equals("PersonaIngreso")) {
			PersonaIngreso2();
		} else if (Pantalla.equals("PersonaSalida")) {
			PersonaSalida2();
		} else if (Pantalla.equals("VehiculoIngreso")) {
			VehiculoIngreso2();
		} else if (Pantalla.equals("VehiculoSalida")) {
			VehiculoSalida2();
		} else if (Pantalla.equals("Chofer")) {
			Chofer();
			// BtnConfigAnularFinalizar.setText("Anular");
		} else if (Pantalla.equals("Pasajero")) {
			Pasajero();
			// BtnConfigAnularFinalizar.setText("Finalizar");
		} else if (Pantalla.equals("Guardia")) {
			Guardia();
			// BtnConfigAnularFinalizar.setText("Finalizar");
		}

		if (mNfcAdapter == null) {
			// Stop here, we definitely need NFC
			Toast.makeText(this, Toast2, Toast.LENGTH_LONG).show();
			finish();

			return;

		}

		if (!mNfcAdapter.isEnabled()) {
			// Toast.makeText(this, Toast1, Toast.LENGTH_LONG).show();
			NFCError(this, tituloDialogo, mensajeDialogo, "habilitar");
		}
		CursorSincronizacion = Manager.CursorSincronizacionPersona();

		if (CursorSincronizacion.moveToFirst()) {
			do {
				EstadoSync = CursorSincronizacion.getString(3);
				TipoSync = CursorSincronizacion.getString(4);
			} while (CursorSincronizacion.moveToNext());
		} else {
			 Manager.InsertarDatosSincronizacion("0", "0", "1", "1", "0", 0,
			 "OFF", "INICIAL");

//			Manager.InsertarDatosSincronizacion("0", "0", "440", "557486", "115304", 0,
//					"OFF", "INICIAL");
			
			
			EstadoSync = "OFF";
			TipoSync = "INICIAL";
		}

		CursorSincronizacionIntranet = Manager
				.CursorSincronizacionPersonaIntranet();

		if (CursorSincronizacionIntranet.moveToFirst()) {
			do {
				EstadoSyncIntranet = CursorSincronizacionIntranet.getString(3);
				TipoSyncIntranet = CursorSincronizacionIntranet.getString(4);
			} while (CursorSincronizacionIntranet.moveToNext());
		} else {
			Manager.InsertarDatosSincronizacionIntranet("0", "0", "1", "1",
					"0", 0, "OFF", "INICIAL");
			EstadoSync = "OFF";
			TipoSync = "INICIAL";
		}

		if ((TipoSync.equals("INCREMENTAL")) && (EstadoSync.equals("OFF"))) {
			// verificar si existe un hilo corriendo, si no existe se ejecuta el
			// hilo y este sincronizará.
		}

		Cursor foto1 = Manager.CursorHABILITAR_FOTOS();
		if (foto1.moveToFirst()) {
			if (foto1.getString(1).equals("SI")) {
				// colorfoto();

				VerificarSync = Manager.VerificarSync();
				if (VerificarSync.moveToFirst()) {

					Cursor c = Manager.CursorSincronizacionControlAccesoLocal();
					if (c.moveToFirst()) {
						SubirData();
					} else {
						ConsultarCambios();
					}

				}
			}
		}

		handleIntent(getIntent());

	}

	public void SubirData() {
		ImagenSincronizando.setImageResource(R.drawable.blanco);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				inicio.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						new SubirDatos(getApplicationContext(),
								ImagenSincronizando).execute();
					}
				});
			}
		}, 30000, 60000);
	}

	public void ConsultarCambios() {

		ImagenSincronizando.setImageResource(R.drawable.azul);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			Boolean resp = true;
			@Override
			public void run() {
				// TODO Auto-generated method stub
				inicio.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (resp) {
							
							Cursor c = Manager.CursorLog();
							if(c.moveToFirst()){
								Manager.EliminarDatosLog2(util.getFecha());
							}
							
							Manager.InsertarDatosLog("", "Sincronizacion Inciada.", util.getFecha(), util.getHora());
							new Guardia(getApplicationContext(),
									ImagenSincronizando).execute();

							resp = false;
						}
					}
				});
			}
		}, 30000, 60000);

	}

	// public void colorfoto() {
	//
	// Timer timer = new Timer();
	// timer.scheduleAtFixedRate(new TimerTask() {
	// public void run() {
	// new CambiarFoto(ImagenSincronizando, inicio.this)
	// .execute();
	// }
	// }, 5000, 5000);
	//
	// }

	public void turnOnFlashLight() {
		try {
			if (getPackageManager().hasSystemFeature(
					PackageManager.FEATURE_CAMERA_FLASH)) {
				cam = Camera.open();
				Parameters p = cam.getParameters();
				p.setFlashMode(Parameters.FLASH_MODE_TORCH);
				cam.setParameters(p);
				cam.startPreview();
			}
		} catch (Exception e) {
		}
	}

	public void turnOffFlashLight() {
		try {
			if (getPackageManager().hasSystemFeature(
					PackageManager.FEATURE_CAMERA_FLASH)) {
				cam.stopPreview();
				cam.release();
				cam = null;
			}
		} catch (Exception e) {
		}
	}

	public void flashcamara(View v) {

		boolean on = ((ToggleButton) v).isChecked();
		if (on) {
			turnOnFlashLight();

			v.setBackgroundResource(R.drawable.linton);

		} else {
			turnOffFlashLight();
			v.setBackgroundResource(R.drawable.lintoff);
		}
	}

	@SuppressWarnings("deprecation")
	public void NFCError(final Context context, String titulo, String mensaje,
			final String accion) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(titulo);
		alertDialog.setMessage(mensaje);
		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			@SuppressLint("NewApi")
			public void onClick(DialogInterface dialog, int which) {
				// aquí puedes añadir funciones
				if (accion.equalsIgnoreCase("Habilitar")) {

					// Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
					// startActivity(intent);
					if (!mNfcAdapter.isNdefPushEnabled()) {
						context.startActivity(new Intent(
								Settings.ACTION_NFC_SETTINGS));
					}

				} else if (accion.equalsIgnoreCase("Cerrar")) {
					finish();
				}
			}
		});
		alertDialog.setIcon(R.drawable.ic_launcher);
		alertDialog.show();
		alertDialog.setCancelable(false);
	}

	private String SeleccionarIdioma() {
		String Idioma1 = getResources().getConfiguration().locale
				.getDisplayName();

		if (Idioma1.toUpperCase().contains("ESPAÑOL")) {
			Idioma1 = "ESPANIOL";
		} else if (Idioma1.toUpperCase().contains("ENGLISH")) {
			Idioma1 = "INGLES";
		} else if (Idioma1.toUpperCase().contains("PORTUGUÊS")) {
			Idioma1 = "PORTUGUES";
		} else {
			Idioma1 = "ESPANIOL";
		}

		Manager.InsertarDatosIdioma(Idioma1);

		return Idioma1;
	}

	private void Textos(String Texto1, String Texto2, String Texto3,
			String Texto4, String Texto5, String Texto6, String Texto7,
			String Texto8, String Texto9, String Texto10, String Texto11,
			String Texto12, String Texto13, String Texto14, String TextoToast1,
			String TextoToast2, String TextoVigitante,
			String TextoTituloDialogo, String TextoMensajeDialogo) {
		Txt1 = Texto1;
		Txt2 = Texto2;
		Txt3 = Texto3;
		Txt4 = Texto4;
		Txt5 = Texto5;
		Txt6 = Texto6;
		Txt7 = Texto7;
		Txt8 = Texto8;
		Txt9 = Texto9;
		Txt10 = Texto10;
		Txt11 = Texto11;
		Toast1 = TextoToast1;
		Toast2 = TextoToast2;
		BtnPersonaIngreso.setText(Texto9);
		BtnPersonaSalida.setText(Texto10);
		BtnVehiculoIngreso.setText(Texto13);
		BtnVehiculoSalida.setText(Texto14);
		BtnManual.setText(Texto1);
		BtnConfigAnularFinalizar.setText(Texto12);
		txtVigitante.setText(TextoVigitante);
		tituloDialogo = TextoTituloDialogo;
		mensajeDialogo = TextoMensajeDialogo;
	}

	private void Guardia() {

		BtnManual.setText(Txt1);
		LlVehiculo.setVisibility(View.INVISIBLE);
		LlChoferPasajero.setBackgroundResource(R.drawable.vigilante);

		LlPersona.setBackgroundColor(0x00000000);
		BtnPersonaSalida.setBackgroundColor(0x00000000);
		BtnPersonaIngreso.setBackgroundColor(0x00000000);

		BtnPersonaIngreso.setEnabled(false);
		BtnPersonaSalida.setEnabled(false);
		BtnPersonaSalida.setText("");

		BtnPersonaIngreso.setText(Txt2);
		BtnPersonaIngreso.setTextColor(Color.RED);

	}

	private void Chofer() {

		BtnConfigAnularFinalizar.setText(Txt3);
		BtnConfigAnularFinalizar.setBackgroundColor(Color.BLACK);
		if ((Pantalla.equals("Pasajero")) || (Pantalla.equals("Chofer"))) {
			CursorEstadoChoferPasajero = Manager.BuscarEstadoChoferPasajero();
			if (CursorEstadoChoferPasajero.moveToFirst()) // SI -> VAN DENTRO DE
															// UN VEHICULO
			{
				do {
					SentidoChoferPasajero = CursorEstadoChoferPasajero
							.getString(2);
				} while (CursorEstadoChoferPasajero.moveToNext());
			}
		}
		// LlPersona.setVisibility(View.INVISIBLE);
		LlVehiculo.setVisibility(View.INVISIBLE);
		LlChoferPasajero.setBackgroundResource(R.drawable.chofer);

		LlPersona.setBackgroundColor(0x00000000);
		BtnPersonaSalida.setBackgroundColor(0x00000000);
		BtnPersonaIngreso.setBackgroundColor(0x00000000);

		BtnPersonaIngreso.setEnabled(false);
		BtnPersonaSalida.setEnabled(false);
		BtnPersonaSalida.setText("");

		if (SentidoChoferPasajero.equals("In")) {
			BtnPersonaIngreso.setText(Txt4);
			BtnPersonaIngreso.setTextColor(Color.YELLOW);
		} else {
			BtnPersonaIngreso.setText(Txt5);
			BtnPersonaIngreso.setTextColor(Color.RED);
		}

		if (!isOnline2()) {
			Cursor verFoto = Manager.CursorCambiarFoto();
			if (verFoto.moveToFirst()) {
				Manager.ActualizarColorFoto("rojo");
			} else {
				Manager.InsertarColorFoto("rojo");
			}
		} else {
			Cursor verFoto = Manager.CursorCambiarFoto();
			if (verFoto.moveToFirst()) {
				Manager.ActualizarColorFoto("verde");
			} else {
				Manager.InsertarColorFoto("verde");
			}
			// new CambiarColorFoto().execute();
		}
	}

	private void Pasajero() {

		BtnConfigAnularFinalizar.setText(Txt6);
		BtnConfigAnularFinalizar.setBackgroundColor(Color.BLACK);
		if ((Pantalla.equals("Pasajero")) || (Pantalla.equals("Chofer"))) {
			CursorEstadoChoferPasajero = Manager.BuscarEstadoChoferPasajero();
			if (CursorEstadoChoferPasajero.moveToFirst()) // SI -> VAN DENTRO DE
															// VEHIC
			{
				do {
					SentidoChoferPasajero = CursorEstadoChoferPasajero
							.getString(2);
				} while (CursorEstadoChoferPasajero.moveToNext());
			}
		}
		// LlPersona.setVisibility(View.INVISIBLE);
		LlVehiculo.setVisibility(View.INVISIBLE);
		LlChoferPasajero.setBackgroundResource(R.drawable.pasajero);

		LlPersona.setBackgroundColor(0x00000000);
		BtnPersonaSalida.setBackgroundColor(0x00000000);
		BtnPersonaIngreso.setBackgroundColor(0x00000000);

		BtnPersonaIngreso.setEnabled(false);
		BtnPersonaSalida.setEnabled(false);
		BtnPersonaSalida.setText("");

		if (SentidoChoferPasajero.equals("In")) {
			BtnPersonaIngreso.setText(Txt7);
			BtnPersonaIngreso.setTextColor(Color.YELLOW);
		} else {
			BtnPersonaIngreso.setText(Txt8);
			BtnPersonaIngreso.setTextColor(Color.RED);
		}
		if (!isOnline2()) {
			Cursor verFoto = Manager.CursorCambiarFoto();
			if (verFoto.moveToFirst()) {
				Manager.ActualizarColorFoto("rojo");

			} else {

				Manager.InsertarColorFoto("rojo");

			}
		} else {
			Cursor verFoto = Manager.CursorCambiarFoto();
			if (verFoto.moveToFirst()) {
				Manager.ActualizarColorFoto("verde");

			} else {

				Manager.InsertarColorFoto("verde");

			}
		}
		// new CambiarColorFoto().execute();

	}

	public void PersonaIngreso(View v) {
		Manager.ActualizarPantalla("PersonaIngreso");
		Vibrador.vibrate(80);
		// colorfoto();
		PersonaIngreso2();
	}

	private void PersonaIngreso2() {
		LlVehiculo.setVisibility(View.VISIBLE);
		// LlChoferPasajero.setBackgroundColor(0x00000000);
		// LlPersona.setBackgroundResource(R.drawable.trabajador);
		// LinearLayout LinearLayoutPersonaSalida = (LinearLayout)
		// findViewById(R.id.LinearLayoutPersonaSalida);
		// LinearLayoutPersonaSalida.setBackgroundColor(Color.parseColor("#777777"));
		BtnPersonaIngreso.setEnabled(true);
		BtnPersonaSalida.setEnabled(true);
		// BtnPersonaIngreso.setText(Txt9);
		// BtnPersonaSalida.setText(Txt10);

		BtnManual.setText(Txt1);
		// BtnPersonaIngreso.setTextColor(Color.YELLOW);
		// BtnPersonaIngreso.setBackground(R.drawable.i);
		BtnPersonaIngreso.setBackgroundResource(R.drawable.ingresando2on);
		BtnPersonaSalida.setBackgroundResource(R.drawable.saliendo2off);
		BtnVehiculoIngreso.setBackgroundResource(R.drawable.entraauto_off);
		BtnVehiculoSalida.setBackgroundResource(R.drawable.saleauto_off);
		// #00000000
		// BtnPersonaIngreso.setBackgroundResource(R.drawable.ingresando2); //
		// Transparente
		//
		// //BtnPersonaSalida.setTextColor(Color.BLACK);
		// BtnPersonaSalida.setBackgroundColor(0xBF585858);
		// // Plomo transparente
		//
		// //BtnVehiculoIngreso.setTextColor(Color.BLACK);
		// BtnVehiculoIngreso.setBackgroundColor(0xBF585858); // Plomo
		// transparente
		//
		// //BtnVehiculoSalida.setTextColor(Color.BLACK);
		// BtnVehiculoSalida.setBackgroundColor(0xBF585858); // Plomo
		// transparente
		if (!isOnline2()) {
			Cursor verFoto = Manager.CursorCambiarFoto();
			if (verFoto.moveToFirst()) {
				Manager.ActualizarColorFoto("rojo");

			} else {

				Manager.InsertarColorFoto("rojo");

			}
		} else {
			Cursor verFoto = Manager.CursorCambiarFoto();
			if (verFoto.moveToFirst()) {
				Manager.ActualizarColorFoto("verde");

			} else {

				Manager.InsertarColorFoto("verde");

			}
		}
		// colorfoto();
	}

	public void VehiculoIngreso(View v) {
		Manager.ActualizarPantalla("VehiculoIngreso");
		Vibrador.vibrate(80);
		// colorfoto();
		VehiculoIngreso2();
	}

	private void VehiculoIngreso2() {
		BtnManual.setText(Txt1);
		BtnPersonaIngreso.setBackgroundResource(R.drawable.ingresando2off);
		BtnPersonaSalida.setBackgroundResource(R.drawable.saliendo2off);
		BtnVehiculoIngreso.setBackgroundResource(R.drawable.entraauto_on);
		BtnVehiculoSalida.setBackgroundResource(R.drawable.saleauto_off);
		// BtnVehiculoIngreso.setTextColor(Color.YELLOW);
		// BtnVehiculoIngreso.setBackgroundColor(0x00000000);// #00000000
		// // Transparente
		//
		// BtnPersonaSalida.setTextColor(Color.BLACK);
		// BtnPersonaSalida.setBackgroundColor(0xBF585858); // Plomo
		// transparente
		//
		// BtnPersonaIngreso.setTextColor(Color.BLACK);
		// BtnPersonaIngreso.setBackgroundColor(0xBF585858); // Plomo
		// transparente
		//
		// BtnVehiculoSalida.setTextColor(Color.BLACK);
		// BtnVehiculoSalida.setBackgroundColor(0xBF585858); // Plomo
		// transparente
		if (!isOnline2()) {
			Cursor verFoto = Manager.CursorCambiarFoto();
			if (verFoto.moveToFirst()) {
				Manager.ActualizarColorFoto("rojo");

			} else {

				Manager.InsertarColorFoto("rojo");

			}
		} else {
			Cursor verFoto = Manager.CursorCambiarFoto();
			if (verFoto.moveToFirst()) {
				Manager.ActualizarColorFoto("verde");

			} else {

				Manager.InsertarColorFoto("verde");

			}
		}
		// colorfoto();
	}

	public void PersonaSalida(View v) {
		Manager.ActualizarPantalla("PersonaSalida");
		Vibrador.vibrate(80);
		// colorfoto();
		PersonaSalida2();
	}

	private void PersonaSalida2() {
		BtnManual.setText(Txt11);
		BtnPersonaIngreso.setBackgroundResource(R.drawable.ingresando2off);
		BtnPersonaSalida.setBackgroundResource(R.drawable.saliendo2on);
		BtnVehiculoIngreso.setBackgroundResource(R.drawable.entraauto_off);
		BtnVehiculoSalida.setBackgroundResource(R.drawable.saleauto_off);
		// BtnPersonaSalida.setTextColor(Color.RED);
		// BtnPersonaSalida.setBackgroundColor(0x00000000);// #00000000
		// Transparente
		// BtnPersonaSalida.setBackgroundResource(R.drawable.saliendo2);
		//
		// //BtnPersonaIngreso.setTextColor(Color.BLACK);
		// BtnPersonaIngreso.setBackgroundResource(R.drawable.ingresando2); //
		// Plomo transparente
		//
		// //BtnVehiculoIngreso.setTextColor(Color.BLACK);
		// BtnVehiculoIngreso.setBackgroundColor(0xBF585858); // Plomo
		// transparente
		//
		// //BtnVehiculoSalida.setTextColor(Color.BLACK);
		// BtnVehiculoSalida.setBackgroundColor(0xBF585858); // Plomo
		// transparente
		if (!isOnline2()) {
			Cursor verFoto = Manager.CursorCambiarFoto();
			if (verFoto.moveToFirst()) {
				Manager.ActualizarColorFoto("rojo");

			} else {

				Manager.InsertarColorFoto("rojo");

			}
		} else {
			Cursor verFoto = Manager.CursorCambiarFoto();
			if (verFoto.moveToFirst()) {
				Manager.ActualizarColorFoto("verde");

			} else {

				Manager.InsertarColorFoto("verde");

			}
		}
		// colorfoto();
	}

	public void VehiculoSalida(View v) {
		// colorfoto();
		Manager.ActualizarPantalla("VehiculoSalida");
		Vibrador.vibrate(80);
		VehiculoSalida2();
	}

	private void VehiculoSalida2() {
		BtnManual.setText(Txt11);
		BtnPersonaIngreso.setBackgroundResource(R.drawable.ingresando2off);
		BtnPersonaSalida.setBackgroundResource(R.drawable.saliendo2off);
		BtnVehiculoIngreso.setBackgroundResource(R.drawable.entraauto_off);
		BtnVehiculoSalida.setBackgroundResource(R.drawable.saleauto_on);
		// BtnVehiculoSalida.setTextColor(Color.RED);
		// BtnVehiculoSalida.setBackgroundColor(0x00000000);// #00000000
		// // Transparente
		//
		// BtnPersonaIngreso.setTextColor(Color.BLACK);
		// BtnPersonaIngreso.setBackgroundColor(0xBF585858); // Plomo
		// transparente
		//
		// BtnVehiculoIngreso.setTextColor(Color.BLACK);
		// BtnVehiculoIngreso.setBackgroundColor(0xBF585858); // Plomo
		// transparente
		//
		// BtnPersonaSalida.setTextColor(Color.BLACK);
		// BtnPersonaSalida.setBackgroundColor(0xBF585858); // Plomo
		// transparente
		if (!isOnline2()) {
			Cursor verFoto = Manager.CursorCambiarFoto();
			if (verFoto.moveToFirst()) {
				Manager.ActualizarColorFoto("rojo");

			} else {

				Manager.InsertarColorFoto("rojo");

			}
		} else {
			Cursor verFoto = Manager.CursorCambiarFoto();
			if (verFoto.moveToFirst()) {
				Manager.ActualizarColorFoto("verde");

			} else {

				Manager.InsertarColorFoto("verde");

			}
		}
		// colorfoto();
	}

	public void IngresarManual(View v) {
		Vibrador.vibrate(80);
		// colorfoto();
		EnviarData("Manual", "Manual");
	}

	public void EnviarData(String TipoIngreso, String Data) {
		CursorPantalla = Manager.BuscarPantalla();

		if (CursorPantalla.moveToFirst()) {
			do {
				Pantalla = CursorPantalla.getString(0);

			} while (CursorPantalla.moveToNext());

		} else {
			Manager.InsertarDatosPantalla("Guardia");
			Pantalla = "Guardia";
		}
		Intent IngresoManual = new Intent(this, MainActivity.class);

		if (Pantalla.equals("PersonaIngreso")) {
			IngresoManual.putExtra("Sentido", "In");
			IngresoManual.putExtra("Lectura", "Funcionario");
		} else if (Pantalla.equals("PersonaSalida")) {
			IngresoManual.putExtra("Sentido", "Out");
			IngresoManual.putExtra("Lectura", "Funcionario");
		} else if (Pantalla.equals("VehiculoIngreso")) {
			IngresoManual.putExtra("Sentido", "In");
			IngresoManual.putExtra("Lectura", "Vehiculo");
		} else if (Pantalla.equals("VehiculoSalida")) {
			IngresoManual.putExtra("Sentido", "Out");
			IngresoManual.putExtra("Lectura", "Vehiculo");
		} else if (Pantalla.equals("Chofer")) {
			IngresoManual.putExtra("Sentido", SentidoChoferPasajero);
			IngresoManual.putExtra("Lectura", "Chofer");
		} else if (Pantalla.equals("Pasajero")) {
			IngresoManual.putExtra("Sentido", SentidoChoferPasajero);
			IngresoManual.putExtra("Lectura", "Pasajero");
		} else if (Pantalla.equals("Guardia")) {
			IngresoManual.putExtra("Sentido", "In");
			IngresoManual.putExtra("Lectura", "Guardia");
		}

		IngresoManual.putExtra("TipoIngreso", TipoIngreso);
		IngresoManual.putExtra("Data", Data);
		finish();
		startActivity(IngresoManual);
	}

	@Override
	public void onBackPressed() {

	}

	public void LecturaCamara(View v) {
		 Vibrador.vibrate(80);
		 contadorNfc = 3;
		 turnOffFlashLight();
		 //colorfoto();
		 startActivity(new Intent(getApplicationContext(), Escanear.class));

	}

	private void RetornoCamara() {
		String Lectura = "";

		if (bundle != null && contadorNfc == 3) {

			String rut = bundle.getString("rut").toUpperCase();
			String tipo = bundle.getString("tipo");
			if (tipo.equalsIgnoreCase("pdf417")) {
				String primerCaracterRut = rut.substring(0, 1);
				if (primerCaracterRut.equalsIgnoreCase("1")
						|| primerCaracterRut.equalsIgnoreCase("2")
						|| primerCaracterRut.equalsIgnoreCase("3")) {
					Lectura = rut.substring(0, 9);
				} else {
					Lectura = rut.substring(0, 8);
				}

			} else if (tipo.toUpperCase().equalsIgnoreCase("QR CODE")) {
				int a = rut.indexOf("RUN=");
				int b = rut.indexOf("&TYPE");
				String valor = rut.substring(a + 4, b);
				String sacarguion = valor.replace("-", "").trim();
				Lectura = sacarguion.replace("&", "");

			} else {
				Lectura = rut;
			}

			EnviarData("ID", Lectura);
		}

	}

	public void Configurar(View v) {
		Vibrador.vibrate(80);
		String TextoBtn = BtnConfigAnularFinalizar.getText().toString();

		if ((TextoBtn.equalsIgnoreCase("")) || (TextoBtn.equalsIgnoreCase(""))) {
			Intent Configuracion = new Intent(this, Configuracion.class);
			startActivity(Configuracion);
		}

		else if (TextoBtn.equalsIgnoreCase("Finalizar")) {
			Manager.ActualizarPantalla("PersonaIngreso");
			Manager.EliminarDatosChoferPasajero();
			Vibrador.vibrate(80);
			finish();
			Intent Inicio = new Intent(this, inicio.class);
			startActivity(Inicio);
		} else if (TextoBtn.equalsIgnoreCase("Anular")) {
			Manager.ActualizarPantalla("PersonaIngreso");
			Manager.EliminarDatosChoferPasajero();
			Vibrador.vibrate(80);
			finish();
			Intent Inicio = new Intent(this, inicio.class);
			startActivity(Inicio);
		}

		else {

			Manager.ActualizarPantalla("PersonaIngreso");
			Manager.EliminarDatosChoferPasajero();
			Vibrador.vibrate(80);
			finish();
			Intent Inicio = new Intent(this, inicio.class);
			startActivity(Inicio);
		}
		if (!isOnline2()) {
			Cursor verFoto = Manager.CursorCambiarFoto();
			if (verFoto.moveToFirst()) {
				Manager.ActualizarColorFoto("rojo");

			} else {

				Manager.InsertarColorFoto("rojo");

			}
		} else {
			Cursor verFoto = Manager.CursorCambiarFoto();
			if (verFoto.moveToFirst()) {
				Manager.ActualizarColorFoto("verde");

			} else {

				Manager.InsertarColorFoto("verde");

			}
		}

	}

	private void LlamarIntencion(String UID) {
		if ((Pantalla.equals("Pasajero")) || (Pantalla.equals("Chofer"))) {
			CursorEstadoChoferPasajero = Manager.BuscarEstadoChoferPasajero();
			if (CursorEstadoChoferPasajero.moveToFirst()) // SI -> VAN DENTRO DE
															// UN VEHICULO
			{
				do {
					SentidoChoferPasajero = CursorEstadoChoferPasajero
							.getString(2);
				} while (CursorEstadoChoferPasajero.moveToNext());
			}
		}

		EnviarData("UID", UID);

	}

	@Override
	protected void onResume() {
		super.onResume();

		/**
		 * It's important, that the activity is in the foreground (resumed).
		 * Otherwise an IllegalStateException is thrown.
		 */
		CursorPantalla = Manager.BuscarPantalla();
		if (CursorPantalla.moveToFirst()) {
			do {
				Pantalla = CursorPantalla.getString(0);

			} while (CursorPantalla.moveToNext());

		} else {
			Manager.InsertarDatosPantalla("PersonaIngreso");
			Pantalla = "Guardia";
		}

		if (Pantalla.equals("PersonaIngreso")) {
			PersonaIngreso2();
		} else if (Pantalla.equals("PersonaSalida")) {
			PersonaSalida2();
		} else if (Pantalla.equals("VehiculoIngreso")) {
			VehiculoIngreso2();
		} else if (Pantalla.equals("VehiculoSalida")) {
			VehiculoSalida2();
		} else if (Pantalla.equals("Chofer")) {
			Chofer();
			// BtnConfigAnularFinalizar.setText("Anular"); duplicado
		} else if (Pantalla.equals("Pasajero")) {
			Pasajero();
			// BtnConfigAnularFinalizar.setText("Finalizar"); duplicado
		} else if (Pantalla.equals("Guardia")) {
			Guardia();
			// BtnConfigAnularFinalizar.setText("Ingreso Manual");

		}
		// setupForegroundDispatch(this, mNfcAdapter);
	}

	@Override
	protected void onPause() {
		/**
		 * Call this before onPause, otherwise an IllegalArgumentException is
		 * thrown as well.
		 */

		stopForegroundDispatch(this, mNfcAdapter);

		super.onPause();
	}

	@Override
	protected void onNewIntent(Intent intent) {

		handleIntent(intent);

	}

	private void handleIntent(Intent intent) {
		Cursor NFCCursor = Manager.CursorConfigApp();
		if (NFCCursor.moveToFirst()) {
			do {
				// camaraAcceso = ConfigAppAcceso.getString(0);
				NfcAcceso = NFCCursor.getString(1);
			} while (NFCCursor.moveToNext());

		}

		if (NfcAcceso.equalsIgnoreCase("SI")) {
			String action = intent.getAction();
			if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

				String type = intent.getType();
				if (MIME_TEXT_PLAIN.equals(type)) {

					Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
					new NdefReaderTask().execute(tag);

				} else {
					Log.d(TAG, "Wrong mime type: " + type);
				}
			}

			if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

				// In case we would still use the Tech Discovered Intent

				Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				String[] techList = tag.getTechList();
				String searchedTech = Ndef.class.getName();

				for (String tech : techList) {
					if (searchedTech.equals(tech)) {
						new NdefReaderTask().execute(tag);
						break;
					}
				}
			}

			if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
				byte[] byte_id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);

				// Toast.makeText(inicio.this,"TagID: " + byte_id.toString(),
				// Toast.LENGTH_SHORT).show();
				Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				// Toast.makeText(getApplicationContext(), "UID: " +
				// // bin2hex(tag.getId()), Toast.LENGTH_SHORT).show();
				// // Toast.makeText(getApplicationContext(), "UID: " +
				// // ByteArrayToHexString(tag.getId()).toString(),
				// // Toast.LENGTH_SHORT).show();
				//
				Toast.makeText(getApplicationContext(),
						"UID: " + getHexString(tag.getId()).toString(),
						Toast.LENGTH_SHORT).show();
				LlamarIntencion(getHexString(tag.getId()));

			}
		}

		if (camaraAcceso.equalsIgnoreCase("SI")) {

			RetornoCamara();
		}

	}

	public static String getHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}

		int largo = (result.length()) / 2;
		String resultado = "";
		for (int j = largo; j > 0; j--) {
			if (j != largo) {
				resultado = resultado
						+ result.substring(((j * 2) - 2), ((j * 2)));
			}

		}
		return resultado.toUpperCase();
	}

	/**
	 * @param activity
	 *            The corresponding {@link Activity} requesting the foreground
	 *            dispatch.
	 * @param adapter
	 *            The {@link NfcAdapter} used for the foreground dispatch.
	 */
	public static void setupForegroundDispatch(final Activity activity,
			NfcAdapter adapter) {
		final Intent intent = new Intent(activity.getApplicationContext(),
				activity.getClass());
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		final PendingIntent pendingIntent = PendingIntent.getActivity(
				activity.getApplicationContext(), 0, intent, 0);

		IntentFilter[] filters = new IntentFilter[1];
		String[][] techList = new String[][] {};

		// Notice that this is the same filter as in our manifest.
		filters[0] = new IntentFilter();
		filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
		filters[0].addCategory(Intent.CATEGORY_DEFAULT);
		try {
			filters[0].addDataType(MIME_TEXT_PLAIN);
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("Check your mime type.");
		}

		adapter.enableForegroundDispatch(activity, pendingIntent, filters,
				techList);
	}

	/**
	 * @param activity
	 *            The corresponding {@link BaseActivity} requesting to stop the
	 *            foreground dispatch.
	 * @param adapter
	 *            The {@link NfcAdapter} used for the foreground dispatch.
	 */
	public static void stopForegroundDispatch(final Activity activity,
			NfcAdapter adapter) {
		adapter.disableForegroundDispatch(activity);
	}

	private String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public boolean isOnline() {
		boolean enabled = true;
		String _TipoConexion;
		Cursor _CursorConexion;
		_CursorConexion = Manager.CursorConexion();

		if (_CursorConexion.moveToFirst()) {
			do {
				_TipoConexion = _CursorConexion.getString(0);
			} while (_CursorConexion.moveToNext());
		} else {
			_TipoConexion = "Movil";
		}

		if (_TipoConexion.equals("Local")) {
			enabled = false;
		} else {
			ConnectivityManager connectivityManager = (ConnectivityManager) this
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivityManager.getActiveNetworkInfo();

			if ((info == null || !info.isConnected() || !info.isAvailable())) {
				enabled = false;
			}
		}
		return enabled;
	}

	public boolean isOnline2() {
		boolean enabled = true;

		ConnectivityManager connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();

		if ((info == null || !info.isConnected() || !info.isAvailable())) {
			enabled = false;
		}
		return enabled;
	}

	class Task implements Runnable {
		@Override
		public void run() {
			try {
				Thread.sleep(300000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (isOnline()) {
				// si la sincronizacion no esta iniciada
				// si existen datos a sincronizar
				// primero si existen modificaciones de los usuarios y/o
				// usuarios nuevos, luego control de acceso local
				// sincronizar
			} else {
				new Thread(new Task()).start();
			}
		}
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

}