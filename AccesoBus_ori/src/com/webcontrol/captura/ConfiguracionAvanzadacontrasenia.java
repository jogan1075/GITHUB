package com.webcontrol.captura;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ConfiguracionAvanzadacontrasenia extends Activity {

	Vibrator Vibrador;
	TextView Txt1;
	EditText EdTxtContrasenia;
	Button BtnIngresar, BtnRegresar, BtnAvanzado;
	DataBaseManager Manager;
	String Idioma;
	private HomeKeyLocker mHomeKeyLocker;
	CheckBox mostrarContrasenia;
	String ContraseñaMala;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.configuracionavanzadacontrasenia_layout);

		EdTxtContrasenia = (EditText) findViewById(R.id.editTextPass);
		
		//EdTxtContrasenia.setText("Desa1.");
		
		Txt1 = (TextView) findViewById(R.id.camaraConfig);
		BtnIngresar = (Button) findViewById(R.id.buttonIngresar);
		BtnRegresar = (Button) findViewById(R.id.buttonRegresar);
		BtnAvanzado = (Button) findViewById(R.id.buttonAvanzado);

		mostrarContrasenia = (CheckBox) findViewById(R.id.mostrarPassConfig);
		mostrarContrasenia
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						Vibrador.vibrate(80);
						if (isChecked) {

							EdTxtContrasenia
									.setTransformationMethod(HideReturnsTransformationMethod
											.getInstance());
						} else {

							EdTxtContrasenia
									.setTransformationMethod(PasswordTransformationMethod
											.getInstance());
						}
					}
				});
		Manager = new DataBaseManager(this);

		Cursor CursorIdioma = Manager.CursorIdioma();

		if (CursorIdioma.moveToFirst()) {
			Idioma = CursorIdioma.getString(0);
		}

		if (Idioma.equals("ESPANIOL")) {
			Textos("Ingrese Contraseña", "Ingresar", "Regresar", "Básica",
					"Mostrar Contraseña", "Contraseña Inválida");
		} else if (Idioma.equals("INGLES")) {
			Textos("Enter Password", "Sign In", "Back", "Basic",
					"Show Password", "Invalid Password");
		} else if (Idioma.equals("PORTUGUES")) {
			Textos("Digite a Senha", "Entrar", "Voltar", "Básico",
					"Mostrar Senha", "Senha inválida");
		}

		mHomeKeyLocker = new HomeKeyLocker();
		// mHomeKeyLocker.lock(this);
		Vibrador = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
	}

	private void Textos(String Texto1, String textoIngresar,
			String TextoRegresar, String TextoAvanzado, String mostrarPass,
			String passInvalida) {
		Txt1.setText(Texto1);
		BtnIngresar.setText(textoIngresar);
		BtnRegresar.setText(TextoRegresar);
		BtnAvanzado.setText(TextoAvanzado);
		mostrarContrasenia.setText(mostrarPass);
		ContraseñaMala = passInvalida;
	}

	public void Regresar(View v) {
		Vibrador.vibrate(80);
		Intent i = new Intent(this, inicio.class);
		finish();
		startActivity(i);
	}

	public void Basica(View v) {
		Vibrador.vibrate(80);
		Intent i = new Intent(this, Configuracion.class);
		finish();
		startActivity(i);
	}

	public void BtnContrasenia(View v) {
		Vibrador.vibrate(80);
		String Pass = EdTxtContrasenia.getText().toString();
		if (Pass.equals("Desa1.")) {
			Intent i = new Intent(this, ConfiguracionAvanzada.class);
			finish();
			startActivity(i);
		} else {
			Toast.makeText(getApplicationContext(), ContraseñaMala,
					Toast.LENGTH_SHORT).show();
		}
		
		ocultarTeclado(v);
	}

	private void ocultarTeclado(View v) {
		// TODO Auto-generated method stub
		InputMethodManager tecladoVirtual = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE );
		tecladoVirtual.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			// Do Code Here
			// If want to block just return false
			return false;
		}
		if (keyCode == KeyEvent.KEYCODE_HOME) {
			// Do Code Here
			// If want to block just return false
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// metodos.devolverVibrador(this, 80);
	//
	// if (keyCode == KeyEvent.KEYCODE_BACK && repetir == 0) {
	//
	// Toast.makeText(getApplicationContext(),
	// "Presione Nuevamente Para Salir", 100).show();
	// repetir = 1;
	// return true;
	// }
	// if (keyCode == KeyEvent.KEYCODE_BACK && repetir == 1) {
	//
	// finish();
	// Intent intent = new Intent(Intent.ACTION_MAIN);
	// intent.addCategory(Intent.CATEGORY_HOME);
	//
	// startActivity(intent);
	// repetir = 0;
	// return true;
	// }
	// return super.onKeyDown(keyCode, event);
	// }

}
