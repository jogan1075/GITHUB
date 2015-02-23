package com.webcontrol.captura;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class VerControlAcceso extends Activity {
	
	private ListView lista;
	private Cursor CursorDatos;
	private DataBaseManager Manager;
	ArrayList<Lista_entrada_ControlAcceso> datos;
	TextView TxtDni, TxtPlaca, TxtCod, TxtFecha, TxtHora;
	Button BtnRegresar;
	private String Idioma;
	
	Vibrator Vibrador;
	private HomeKeyLocker mHomeKeyLocker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.vercontrolacceso_layout);
        
        TxtDni = (TextView) findViewById(R.id.textViewDNI);
        TxtPlaca = (TextView) findViewById(R.id.textViewPlaca);
        TxtCod = (TextView) findViewById(R.id.textViewCOD);
        TxtFecha = (TextView) findViewById(R.id.textViewFecha);
        TxtHora = (TextView) findViewById(R.id.textViewHora);
        BtnRegresar = (Button) findViewById(R.id.buttonRegresar);
        
        mHomeKeyLocker = new HomeKeyLocker();
    	mHomeKeyLocker.lock(this);
        Vibrador  = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        Manager = new DataBaseManager(this);
        lista = (ListView) findViewById(R.id.ListView_listado);	
        
        Cursor CursorIdioma = Manager.CursorIdioma();
		
		if (CursorIdioma.moveToFirst())
		{
			Idioma = CursorIdioma.getString(0);
		}
		
		if(Idioma.equals("ESPANIOL"))
		{
			Textos("DNI", "PLACA", "COD", "FECHA", "HORA", "Regresar");
		}
		else if(Idioma.equals("INGLES"))
		{
			Textos("DNI", "PLATE", "COD", "DATE", "TIME", "Back");
		}
		else if(Idioma.equals("PORTUGUES"))
		{
			Textos("DNI", "PLACA", "COD", "DATA", "TEMPO", "Voltar");
		}
        
        datos = new ArrayList<Lista_entrada_ControlAcceso>();
        CursorDatos = Manager.CargarCursorControlAccesoLocal();
        if (CursorDatos.moveToFirst())
        {
        	do{
        		datos.add(new Lista_entrada_ControlAcceso(CursorDatos.getString(0), CursorDatos.getString(1), CursorDatos.getString(2),CursorDatos.getString(3), CursorDatos.getString(4)));
        		
        	} while(CursorDatos.moveToNext());
        }
        
        
        LlenarLista();
        
        
	}
	
	private void Textos(String TextoDni, String TextoPlaca, String TextoCod, String TextoFecha, String TextoHora, String TextoRegresar)
	{
		TxtDni.setText(TextoDni);
        TxtPlaca.setText(TextoPlaca);
        TxtCod.setText(TextoCod);
        TxtFecha.setText(TextoFecha);
        TxtHora.setText(TextoHora);
        BtnRegresar.setText(TextoRegresar);
	}
	
	public void LlenarLista()
	{
		 lista.setAdapter(new Lista_adaptador_ControlAcceso(VerControlAcceso.this, R.layout.entradacontrolacceso_layout, datos){

				@Override
				public void onEntrada(Object entrada, View view) {
					// TODO Auto-generated method stub
					if (entrada != null) 
					{
						
												
						TextView TxtDNI1 = (TextView) view.findViewById(R.id.textViewDNI);
						TxtDNI1.setText(((Lista_entrada_ControlAcceso) entrada).get_IdFuncionario()); 

						TextView TxtPlaca1 = (TextView) view.findViewById(R.id.textView_Placa);
						TxtPlaca1.setText(((Lista_entrada_ControlAcceso) entrada).get_Patente()); 
						
						TextView TxtCod1 = (TextView) view.findViewById(R.id.textView_Cod); 
						TxtCod1.setText(((Lista_entrada_ControlAcceso) entrada).get_CodError());
						
						TextView TxtFecha1= (TextView) view.findViewById(R.id.textView_Fecha); 
						TxtFecha1.setText(((Lista_entrada_ControlAcceso) entrada).get_Fecha());
						
						TextView TxtHora1 = (TextView) view.findViewById(R.id.textView_Nivel); 
						TxtHora1.setText(((Lista_entrada_ControlAcceso) entrada).get_Hora());
						
						//Toast.makeText(mContext,cadenaNueva, Toast.LENGTH_LONG).show();

					}
				}
		
			});
	}
	
	public void Regresar(View v)
	{
		Vibrador.vibrate(80);
		Intent i = new Intent(this, Configuracion.class);
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
