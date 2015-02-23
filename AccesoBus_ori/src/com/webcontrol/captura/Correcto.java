package com.webcontrol.captura;

import java.io.ByteArrayInputStream;

import Metodos.MetodosGenerales;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Correcto extends Activity {

	private DataBaseManager Manager;
	private Cursor CursorFuncionario, CursorVehiculo, CursorGuardia;

	String Rut, RutFuncionario, NombreFuncionario, ApellidoFuncionario,
			NombreEmpresa, IdEmpresa, Imagen, Autorizacion,
			AutorizacionConductor;
	String FechaConsulta, UID, Patente, Marca, Modelo, Anio, TipoVehiculo,
			AutorizacionVehiculo, Mensaje;
	String IdGuardia, NombreGuardia, ApellidoGuardia, AutorizacionGuardia;
	String _Id, _Autorizacion = "NO", _Lectura, _Mensaje, _TipoDato, _Sentido,
			Idioma;
	String Txt1, Txt2, Txt3, Txt4, Txt5, Txt6, Txt7, Txt8, Txt9, Txt10;

	TextView TxtNombre, TxtNombreFuncionario, TxtId, TxtIdUsuario,
			TxtIdEmpresa, TxtNombreEmpresa, TxtAutorizacion,
			TxtAutorizacionConductor, TxtFecha, TxtMensaje, TxtEmpresa,
			TxtEmpresa1;
	ImageView verimagen1;
	Button BtnPause;
	int Pause;
	byte[] ImagenByte;

	ImageButton imgbtn;
	MediaPlayer mpCorrecto, mpIncorrecto;
	ProgressBar progress;
	Vibrator Vibrador;
	private HomeKeyLocker mHomeKeyLocker;
	String ost, ccosto, tipopasefun;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.correcto_layout);

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
		//imgbtn = (ImageButton) findViewById(R.id.imgbutton);

		HomeKeyLocker mHomeKeyLocker = new HomeKeyLocker();
		mHomeKeyLocker.lock(this);

		final MetodosGenerales util = new MetodosGenerales();

		Bundle b = getIntent().getExtras();
		_Lectura = b.getString("Lectura");
		_Id = b.getString("Id");
		_Mensaje = b.getString("Mensaje");
		_TipoDato = b.getString("TipoDato"); // IdFuncionario, IdChofer,
												// IdVehiculo - UIDFuncionario,
												// UIDChofer, UIDVehiculo
		_Autorizacion = b.getString("Autorizacion");
		_Sentido = b.getString("Sentido");

		if (_Autorizacion == null || _Autorizacion.equalsIgnoreCase("null")) {
			_Autorizacion = "NO";
		}
		Cursor CursorIdioma = Manager.CursorIdioma();

		if (CursorIdioma.moveToFirst()) {
			Idioma = CursorIdioma.getString(0);
		}

		if (Idioma.equals("ESPANIOL")) {
			Textos("Autorización Ingreso: ", "Autorización Salida: ",
					"Mensaje: ", "Autorización Conductor: ",
					"Última Consulta: ", "Vehículo", "Placa",
					"Autorización Ingreso: ", "Autorización Salida: ",
					"Volver", "Nombre:", "DNI:", "Empresa:", "RUC:", "Pausar");
		} else if (Idioma.equals("INGLES")) {
			Textos("Join Authorization: ", "Authorization Check out: ",
					"Message: ", "Authorization Driver: ", "Last Check: ",
					"Vehicle", "Plate", "Join Authorization: ",
					"Authorization Departure: ", "Back", "Name:", "DNI:",
					"Enterprise:", "RUC:", "Pause");
		} else if (Idioma.equals("PORTUGUES")) {
			Textos("Junte-se a autorização: ", "Autorização Confira: ",
					"Mensagem: ", "Motorista de autorização: ",
					"Última Consulta: ", "Veículo", "Placa",
					"Junte-se a autorização: ", "Autorização de saída: ",
					"Voltar", "Nome:", "DNI:", "Empresa:", "RUC:", "Pausa");
		}

		if (_Autorizacion.equals("SI")) {
			mpCorrecto.start();
			TxtAutorizacion.setTextColor(Color.GREEN);
			Vibrador.vibrate(250);
		} else {
			mpIncorrecto.start();
			TxtAutorizacion.setTextColor(Color.RED);
			Vibrador.vibrate(450);
		}

		if ((_TipoDato.equals("IdFuncionario"))
				|| (_TipoDato.equals("IdChofer"))
				|| (_TipoDato.equals("UIDFuncionario"))
				|| (_TipoDato.equals("UIDChofer"))) {
			if ((_TipoDato.equals("IdFuncionario"))
					|| (_TipoDato.equals("IdChofer"))) {
				CursorFuncionario = Manager.BuscarFuncionarioId(_Id);
			} else {
				CursorFuncionario = Manager.BuscarFuncionarioUID(_Id);
			}

			if (CursorFuncionario.moveToFirst()) {
				do {
					UID = CursorFuncionario.getString(0);
					Rut = CursorFuncionario.getString(2); // id
					NombreFuncionario = CursorFuncionario.getString(3);
					ApellidoFuncionario = CursorFuncionario.getString(4);
					NombreEmpresa = CursorFuncionario.getString(5);
					IdEmpresa = CursorFuncionario.getString(6);
					// ost = CursorFuncionario.getString(7);
					// ccosto = CursorFuncionario.gets
					Imagen = CursorFuncionario.getString(10);
					Autorizacion = CursorFuncionario.getString(11);
					AutorizacionConductor = CursorFuncionario.getString(12);
					FechaConsulta = CursorFuncionario.getString(13);

				} while (CursorFuncionario.moveToNext());
			}
			if (Imagen.equals("0") || Imagen.equals("null") || Imagen == null) // o
																				// null
			{
				verimagen1.setImageResource(R.drawable.nofoto);
			} else {
				
				ImagenByte = Base64.decode(Imagen, Base64.DEFAULT);
				Bitmap Imagenfoto = ImagenBitmap(ImagenByte);
				verimagen1.setImageBitmap(Imagenfoto);

			}

			verimagen1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Vibrador.vibrate(80);
					if (util.isOnline(getApplicationContext())) {
						
										
						
						   Intent a = new Intent(Correcto.this, Foto.class);
						   a.putExtra("Rut_usuario",Rut);
						   startActivity(a);
						   
					} else {
						Toast.makeText(getApplicationContext(),
								"Sin Conexion a Internet", Toast.LENGTH_SHORT)
								.show();
					}
				}
			});

			TxtNombreFuncionario.setText(NombreFuncionario + " "
					+ ApellidoFuncionario);

			if (_TipoDato.contains("UID")) {
				TxtIdUsuario.setText(Rut);
			} else {
				TxtIdUsuario.setText(_Id);
			}

			TxtNombreEmpresa.setText(NombreEmpresa);
			TxtIdEmpresa.setText(IdEmpresa);
			if (_Sentido.equals("In")) {
				if (Autorizacion.equalsIgnoreCase("NA")) {
					TxtAutorizacion.setText(Txt1 + "SI");
				} else {
					TxtAutorizacion.setText(Txt1 + Autorizacion);
				}

			} else {
				if (Autorizacion.equalsIgnoreCase("NA")) {
					TxtAutorizacion.setText(Txt2 + "SI");
				} else {
					TxtAutorizacion.setText(Txt2 + Autorizacion);
				}

			}

			TxtMensaje.setText(Txt3 + _Mensaje);
			TxtAutorizacionConductor.setText(Txt4 + AutorizacionConductor);
			TxtFecha.setText(Txt5 + FechaConsulta);

			if ((NombreFuncionario.equals("No Existe") && (ApellidoFuncionario
					.equals("No Existe") && (NombreEmpresa.equals("No Existe") && (IdEmpresa
					.equals("No Existe")))))
					|| (NombreFuncionario.equals("null") && (ApellidoFuncionario
							.equals("null") && (NombreEmpresa.equals("null") && (IdEmpresa
							.equals("null")))))) {
				if (_TipoDato.contains("UID")) {
					// Toast.makeText(this, "Eliminado",
					// Toast.LENGTH_SHORT).show();
					Manager.EliminarDatosFuncionarioUID(_Id);
				} else {
					Manager.EliminarDatosFuncionarioId(_Id);
				}

				// Toast.makeText(this, "Eliminado", Toast.LENGTH_SHORT).show();
			}

		} else if ((_TipoDato.equals("IdVehiculo"))
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
			TxtNombre.setText(Txt6);
			TxtNombreFuncionario.setText(Marca + " " + Modelo);
			TxtId.setText(Txt7);

			if (_TipoDato.equals("IdVehiculo")) {
				TxtIdUsuario.setText(_Id);
			} else {
				TxtIdUsuario.setText(Patente);
			}

			TxtNombreEmpresa.setText(NombreEmpresa);
			TxtIdEmpresa.setText(IdEmpresa);

			if (_Sentido.equals("In")) {
				TxtAutorizacion.setText(Txt8 + AutorizacionVehiculo);
			} else {
				TxtAutorizacion.setText(Txt9 + AutorizacionVehiculo);
			}

			TxtMensaje.setText(Txt3 + Mensaje);
			TxtFecha.setText(Txt5 + FechaConsulta);
			//aca
			// Toast.makeText(getApplicationContext(), TipoVehiculo,
			// Toast.LENGTH_LONG).show();
			// Toast.makeText(getApplicationContext(), Marca +Modelo + Anio +
			// AutorizacionVehiculo + Mensaje, Toast.LENGTH_LONG).show();

			if (Imagen.equals("0")) {
				if (TipoVehiculo.toUpperCase().equals("BUS")) {
					verimagen1.setImageResource(R.drawable.bus);
				} else // if (TipoVehiculo.toUpperCase().equals("CAMIONETA"))
				{
					verimagen1.setImageResource(R.drawable.camioneta);
				}
			} else {
				ImagenByte = Base64.decode(Imagen, Base64.DEFAULT);
				Bitmap Imagenfoto = ImagenBitmap(ImagenByte);
				verimagen1.setImageBitmap(Imagenfoto);
			}
		}

		else if ((_TipoDato.equals("IdGuardia"))
				|| (_TipoDato.equals("UIDGuardia"))) {
			if ((_TipoDato.equals("IdGuardia"))) {
				CursorGuardia = Manager.BuscarGuardiaId(_Id);
			}

			else {
				CursorGuardia = Manager.BuscarGuardiaUID(_Id);
			}

			if (CursorGuardia.moveToFirst()) {
				// Toast.makeText(this, _Id, Toast.LENGTH_SHORT).show();
				do {
					IdGuardia = CursorGuardia.getString(2);
					NombreGuardia = CursorGuardia.getString(3);
					ApellidoGuardia = CursorGuardia.getString(4);
					NombreEmpresa = CursorGuardia.getString(5);
					IdEmpresa = CursorGuardia.getString(6);
					AutorizacionGuardia = CursorGuardia.getString(7);
					Imagen = CursorGuardia.getString(8);
					// FechaConsulta = CursorVehiculo.getString(12);
					// Mensaje = CursorVehiculo.getString(13);

				} while (CursorGuardia.moveToNext());

				if (!NombreGuardia.equalsIgnoreCase("No Existe")
						|| !ApellidoGuardia.equalsIgnoreCase("No Existe")) {
					Cursor CursorDatosGuardia = Manager
							.CursorDevolverDatosGuardia();
					if (CursorDatosGuardia.moveToFirst()) {
						Manager.ActualizarDatosGuardia(NombreGuardia,
								ApellidoGuardia);
					} else {
						Manager.InsertarDatosGuardia(NombreGuardia,
								ApellidoGuardia);
					}

				}

			}
			if (AutorizacionGuardia.equals("SI")) {
				// Manager.InsertarDatosChoferPasajero("1", Patente,
				// _Sentido);//inserta para saber que despues viene chofer y
				// pasajeros
				TxtNombreFuncionario.setText(NombreGuardia + " "
						+ ApellidoGuardia);
				// TxtId.setText("Placa");
				Manager.ActualizarPantalla("PersonaIngreso");
			}

			if (((NombreGuardia.equals("No Existe") && (ApellidoGuardia
					.equals("No Existe") && (NombreEmpresa.equals("No Existe") && (IdEmpresa
					.equals("No Existe"))))))
					|| ((NombreGuardia.equals("null") && (ApellidoGuardia
							.equals("null") && (NombreEmpresa.equals("null") && (IdEmpresa
							.equals("null"))))))) {
				if (_TipoDato.equals("IdGuardia")) {
					Manager.EliminarDatosGuardiaId(_Id);
				} else {
					Manager.EliminarDatosGuardiaUID(_Id);
					// Toast.makeText(this, "Eliminado",
					// Toast.LENGTH_SHORT).show();
				}

				// Toast.makeText(this, "Eliminado", Toast.LENGTH_SHORT).show();
			}
			// TxtNombre.setText("Vehículo");

			if (_TipoDato.equals("IdGuardia")) {
				TxtIdUsuario.setText(_Id);
			} else {
				TxtIdUsuario.setText(IdGuardia);
			}

			TxtNombreEmpresa.setText(NombreEmpresa);
			TxtIdEmpresa.setText(IdEmpresa);

			if (_Sentido.equals("In")) {
				TxtAutorizacion.setText(Txt8 + AutorizacionGuardia);
			} else {
				TxtAutorizacion.setText(Txt9 + AutorizacionGuardia);
			}

			// TxtMensaje.setText("Mensaje: " + Mensaje);
			// TxtFecha.setText("Última Consulta: " + FechaConsulta);
			// Toast.makeText(getApplicationContext(), TipoVehiculo,
			// Toast.LENGTH_LONG).show();
			// Toast.makeText(getApplicationContext(), Marca +Modelo + Anio +
			// AutorizacionVehiculo + Mensaje, Toast.LENGTH_LONG).show();

			if (Imagen.equals("0") || Imagen.equals("null")
					|| Imagen.toUpperCase().equals("NO")) // o null
			{
				verimagen1.setImageResource(R.drawable.nofoto);
			} else {
				ImagenByte = Base64.decode(Imagen, Base64.DEFAULT);
				Bitmap Imagenfoto = ImagenBitmap(ImagenByte);
				verimagen1.setImageBitmap(Imagenfoto);
			}
		}

		if (_Autorizacion.equals("SI")) {
			progress.setMax(12);
			progress.setProgress(12);

		} else {
			progress.setMax(16);
			progress.setProgress(16);
		}

		Pause = 0;
		// Toast.makeText(getApplicationContext(), NombreFuncionario +
		// ApellidoFuncionario + NombreEmpresa + IdEmpresa ,
		// Toast.LENGTH_LONG).show();
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
		TxtId.setText(Texto12);
		TxtEmpresa.setText(Texto13);
		TxtEmpresa1.setText(Texto14);
		BtnPause.setText(Texto15);
	}

	private Bitmap ImagenBitmap(byte[] Imagen) {

		byte[] ImagenEntranteEnByts = Imagen;
		ByteArrayInputStream imageStream = new ByteArrayInputStream(
				ImagenEntranteEnByts);
		Bitmap ImagenNueva = BitmapFactory.decodeStream(imageStream);
		return ImagenNueva;
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
