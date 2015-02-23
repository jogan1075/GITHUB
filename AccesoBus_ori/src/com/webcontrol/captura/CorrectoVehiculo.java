package com.webcontrol.captura;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CorrectoVehiculo extends Activity {

	String _Id, _Autorizacion = "NO", _Lectura, _Mensaje, _TipoDato, _Sentido,
			Idioma;
	Cursor CursorVehiculo;
	private DataBaseManager Manager;

	String FechaConsulta, UID, Patente, Marca, Modelo, Anio, TipoVehiculo,
			AutorizacionVehiculo, Mensaje, NombreEmpresa, IdEmpresa, Imagen;

	MediaPlayer mpCorrecto, mpIncorrecto;
	ProgressBar progress;
	Vibrator Vibrador;
	ImageView verimagen1;
	Button BtnPause;
	private HomeKeyLocker mHomeKeyLocker;
	TextView TxtNombre, TxtNombreFuncionario, TxtId, TxtIdUsuario,
			TxtIdEmpresa, TxtNombreEmpresa, TxtAutorizacion,
			TxtAutorizacionConductor, TxtFecha, TxtMensaje, TxtEmpresa,
			TxtEmpresa1;

	String Txt1, Txt2, Txt3, Txt4, Txt5, Txt6, Txt7, Txt8, Txt9, Txt10;
	int Pause;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.correcto_vehiculo);

		Manager = new DataBaseManager(this);

		mpCorrecto = MediaPlayer.create(getApplicationContext(), R.raw.ok);
		mpIncorrecto = MediaPlayer.create(getApplicationContext(), R.raw.error);
		Vibrador = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);

		verimagen1 = (ImageView) findViewById(R.id.imageView1);
		TxtNombre = (TextView) findViewById(R.id.textViewNombre);
		TxtNombreFuncionario = (TextView) findViewById(R.id.textViewNombreFuncionario);
		TxtId = (TextView) findViewById(R.id.textViewId);
		TxtIdUsuario = (TextView) findViewById(R.id.textViewIdUsuario);
		TxtIdEmpresa = (TextView) findViewById(R.id.textViewIdEmpresa);
		TxtEmpresa = (TextView) findViewById(R.id.textView5);
		TxtEmpresa1 = (TextView) findViewById(R.id.textView7);
		TxtNombreEmpresa = (TextView) findViewById(R.id.textViewNombreEmpresa);
		TxtAutorizacion = (TextView) findViewById(R.id.textViewAutorizacion);
		TxtAutorizacionConductor = (TextView) findViewById(R.id.textViewAutorizacionConductor);
		TxtFecha = (TextView) findViewById(R.id.textViewFecha);
		TxtMensaje = (TextView) findViewById(R.id.textViewMensaje);
		progress = (ProgressBar) findViewById(R.id.progressBarPause);
		BtnPause = (Button) findViewById(R.id.buttonPause);

		
		HomeKeyLocker mHomeKeyLocker = new HomeKeyLocker();
		mHomeKeyLocker.lock(this);
		
		
		Bundle b = getIntent().getExtras();
		_Lectura = b.getString("Lectura");
		_Id = b.getString("Id");
		_Mensaje = b.getString("Mensaje");
		_TipoDato = b.getString("TipoDato"); // IdFuncionario, IdChofer,
												// IdVehiculo - UIDFuncionario,
												// UIDChofer, UIDVehiculo
		_Autorizacion = b.getString("Autorizacion");
		_Sentido = b.getString("Sentido");

		Textos("Autorización Ingreso: ", "Autorización Salida: ", "Mensaje: ",
				"Autorización Conductor: ", "Última Consulta: ", "Vehículo",
				"Placa", "Autorización Ingreso: ", "Autorización Salida: ",
				"Volver", "Nombre:", "DNI:", "Empresa:", "RUC:", "Pausar");

		if (_Autorizacion == null || _Autorizacion.equalsIgnoreCase("null")) {
			_Autorizacion = "NO";
		}

		if ((_TipoDato.equals("IdVehiculo"))
				|| (_TipoDato.equals("UIDVehiculo"))) {
			if ((_TipoDato.equals("IdVehiculo"))) {
				CursorVehiculo = Manager.BuscarVehiculoId(_Id);
			}

			else {
				CursorVehiculo = Manager.BuscarVehiculoUID(_Id);
			}

			if (CursorVehiculo.moveToFirst()) {
				// Toast.makeText(this, _Id, Toast.LENGTH_SHORT).show();
				do {
					Patente = CursorVehiculo.getString(3);
					Marca = CursorVehiculo.getString(4);
					Modelo = CursorVehiculo.getString(5);
					Anio = CursorVehiculo.getString(6);
					TipoVehiculo = CursorVehiculo.getString(7);
					NombreEmpresa = CursorVehiculo.getString(8);
					IdEmpresa = CursorVehiculo.getString(9);
					Imagen = CursorVehiculo.getString(10);
					AutorizacionVehiculo = CursorVehiculo.getString(11);
					FechaConsulta = CursorVehiculo.getString(12);
					Mensaje = CursorVehiculo.getString(13);

				} while (CursorVehiculo.moveToNext());
			}

			if (AutorizacionVehiculo.equals("SI")) {
				Manager.InsertarDatosChoferPasajero("1", Patente, _Sentido);// inserta
																			// para
																			// saber
																			// que
																			// despues
																			// viene
																			// chofer
																			// y
																			// pasajeros
			}

			if (((Marca.equals("No Existe") && (Modelo.equals("No Existe") && (NombreEmpresa
					.equals("No Existe") && (IdEmpresa.equals("No Existe"))))))
					|| ((Marca.equals("null") && (Modelo.equals("null") && (NombreEmpresa
							.equals("null") && (IdEmpresa.equals("null"))))))) {
				if (_TipoDato.equals("IdVehiculo")) {
					Manager.EliminarDatosVehiculoId(_Id);
				} else {
					Manager.EliminarDatosVehiculoUID(_Id);
					// Toast.makeText(this, "Eliminado",
					// Toast.LENGTH_SHORT).show();
				}

				// Toast.makeText(this, "Eliminado", Toast.LENGTH_SHORT).show();
			}
			TxtNombre.setText("Vehículo");
			TxtNombreFuncionario.setText(Marca + " " + Modelo);
			// TxtId.setText(Txt7);

//			if(_TipoDato.equals("IdVehiculo")){
				if (_Id.length() > 5) {
					TxtIdUsuario.setText(Patente.substring(0, 3) + "-"
							+ _Id.substring(3, 6));
				}else{
					TxtIdUsuario.setText(Patente);
				}
//			}else{
//				
//			}
			

			TxtNombreEmpresa.setText(NombreEmpresa);
			TxtIdEmpresa.setText(IdEmpresa);

			if (_Sentido.equals("In")) {
				TxtAutorizacion.setText(Txt8 + AutorizacionVehiculo);
			} else {
				TxtAutorizacion.setText(Txt9 + AutorizacionVehiculo);
			}

			TxtMensaje.setText(Txt3 + Mensaje);
			TxtFecha.setText(Txt5 + FechaConsulta);

		}
		
		if (_Autorizacion.equals("SI")) {
			progress.setMax(12);
			progress.setProgress(12);

		} else {
			progress.setMax(16);
			progress.setProgress(16);
		}

		Pause = 0;
	
		new Thread(new Task()).start();
	}

	private void Textos(String Texto1, String Texto2, String Texto3,
			String Texto4, String Texto5, String Texto6, String Texto7,
			String Texto8, String Texto9, String Texto10, String Texto11,
			String Texto12, String Texto13, String Texto14, String Texto15) {
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
		TxtNombre.setText(Texto11);
		// TxtId.setText(Texto12);
		TxtEmpresa.setText(Texto13);
		TxtEmpresa1.setText(Texto14);
		BtnPause.setText(Texto15);
	}
	
	@Override
	public void onBackPressed() {

	}

	public void Pause(View v) {
		Vibrador.vibrate(80);
		progress.setVisibility(View.INVISIBLE);
		Button Btn = (Button) v;
		Btn.setText(Txt10);
		Btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Vibrador.vibrate(80);
				finish();
			}
		});
		Pause = 1;
	}

	class Task implements Runnable {
		@Override
		public void run() {
			if (_Autorizacion.equals("SI")) {
				for (int i = 11; i >= 0; i--) {
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (Pause == 0) {
						progress.incrementProgressBy(-1);
					}
				}
			} else {
				for (int i = 15; i >= 0; i--) {
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (Pause == 0) {
						progress.incrementProgressBy(-1);
					}
				}
			}

			if (Pause == 0) {
				finish();
				startActivity(new Intent(getApplicationContext(), inicio.class));
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
