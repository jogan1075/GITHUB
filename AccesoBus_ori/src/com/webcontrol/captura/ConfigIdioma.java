package com.webcontrol.captura;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class ConfigIdioma extends Activity {
	
	Spinner SpIdioma;
	DataBaseManager Manager;
	String Idioma;
	TextView Txt1;
	Button BtnRegresar;
	private Vibrator Vibrador;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.configidioma_layout);
		
		Vibrador  = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
		Txt1 = (TextView) findViewById(R.id.camaraConfig);
		BtnRegresar = (Button) findViewById(R.id.buttonRegresar);
		
		HomeKeyLocker mHomeKeyLocker = new HomeKeyLocker();
		mHomeKeyLocker.lock(this);
		Manager = new DataBaseManager(this);
    	
    	Cursor CursorIdioma = Manager.CursorIdioma();
    	
    	if (CursorIdioma.moveToFirst())
    	{
    		Idioma = CursorIdioma.getString(0);
    	}

		SpIdioma = (Spinner) findViewById(R.id.spinnerIdioma);
		ArrayAdapter Adapter = ArrayAdapter.createFromResource(this,R.array.idioma, R.layout.spinner_view);
		Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		SpIdioma.setAdapter(Adapter);
		
		if(Idioma.equals("ESPANIOL"))
    	{
    		SpIdioma.setSelection(0);
    		Textos("Seleccione Idioma","Regresar");
    	}
    	else if(Idioma.equals("INGLES"))
    	{
    		SpIdioma.setSelection(1);
    		Textos("Select Language","Back");
    	}
    	else if(Idioma.equals("PORTUGUES"))
    	{
    		SpIdioma.setSelection(2);
    		Textos("Selecione o Idioma","Voltar");
    	}
		
		SpIdioma.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				if(arg0.getItemAtPosition(arg2).toString().equals("Español"))
				{
					Idioma= "ESPANIOL";
					Textos("Seleccione Idioma","Regresar");
				}
				else if(arg0.getItemAtPosition(arg2).toString().equals("English"))
				{
					Idioma= "INGLES";
					Textos("Select Language","Back");
				}
				else if(arg0.getItemAtPosition(arg2).toString().equals("Português"))
				{
					Idioma= "PORTUGUES";
					Textos("Selecione o Idioma","Voltar");
				}	
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub	
			}
		});	
	}
	public void Textos(String TextoTitulo, String TextoBoton)
	{
		Txt1.setText(TextoTitulo);
		BtnRegresar.setText(TextoBoton);
	}
	
	public void Regresar(View v)
	{ 
		Vibrador.vibrate(80);
		Manager.ActualizarIdioma(Idioma);
		Intent i = new Intent (this, ConfiguracionAvanzada.class);
		finish();
		startActivity(i);
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
