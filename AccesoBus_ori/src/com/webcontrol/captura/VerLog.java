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

public class VerLog extends Activity {
	
	private ListView lista;
	private Cursor CursorLog;
	private DataBaseManager Manager;
	ArrayList<Lista_entrada_Log> datos;
	TextView TxtEvento, TxtFecha, TxtHora, TxtRut;
	Button BtnRegresar;
	Vibrator Vibrador;
	private String Idioma;
	private HomeKeyLocker mHomeKeyLocker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.verlog_layout);
        
        mHomeKeyLocker = new HomeKeyLocker();
    	mHomeKeyLocker.lock(this);
        Vibrador  = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        Manager = new DataBaseManager(this);
        lista = (ListView) findViewById(R.id.ListView_listado2);	
        datos = new ArrayList<Lista_entrada_Log>();
        
        TxtEvento = (TextView) findViewById(R.id.textViewDetalle);
        TxtFecha = (TextView) findViewById(R.id.textViewFecha);
        TxtHora = (TextView) findViewById(R.id.textViewHora);
        BtnRegresar = (Button) findViewById(R.id.buttonRegresar);
        TxtRut = (TextView)findViewById(R.id.textViewRut);
        
//        Cursor CursorIdioma = Manager.CursorIdioma();
//		
//		if (CursorIdioma.moveToFirst())
//		{
//			Idioma = CursorIdioma.getString(0);
//		}
//		
//		if(Idioma.equals("ESPANIOL"))
//		{
			Textos("EVENTO", "FECHA", "HORA", "Regresar", "RUT");
//		}
//		else if(Idioma.equals("INGLES"))
//		{
//			Textos("EVENT", "DATE", "TIME", "Back");
//		}
//		else if(Idioma.equals("PORTUGUES"))
//		{
//			Textos("EVENTO", "DATA", "TEMPO", "Voltar");
//		}
        
        CursorLog = Manager.CursorLogOrderDESC();
        if (CursorLog.moveToFirst())
        {
        	do{
        		datos.add(new Lista_entrada_Log(CursorLog.getString(1), CursorLog.getString(2),CursorLog.getString(3),CursorLog.getString(4)));
        		
        	} while(CursorLog.moveToNext());
        }       
        LlenarLista();             
	}
	
	private void Textos(String TextoEvento, String TextoFecha, String TextoHora, String TextoRegresar,String TextoRut)
	{
		TxtEvento.setText(TextoEvento);
        TxtFecha.setText(TextoFecha);
        TxtHora.setText(TextoHora);
        BtnRegresar.setText(TextoRegresar);
        TxtRut.setText(TextoRut);
	}
	
	public void LlenarLista()
	{
		 lista.setAdapter(new Lista_adaptador_Log(VerLog.this, R.layout.entradalog_layout, datos){

				@Override
				public void onEntrada(Object entrada, View view) {
					// TODO Auto-generated method stub
					if (entrada != null) 
					{
					
						TextView TxtEvento1 = (TextView) view.findViewById(R.id.textViewDetalle);
						TxtEvento1.setText(((Lista_entrada_Log) entrada).get_Rut()); 

						TextView TxtRut1 = (TextView) view.findViewById(R.id.textViewRut);
						TxtRut1.setText(((Lista_entrada_Log) entrada).get_Descripcion()); 
						
						TextView TxtFecha1 = (TextView) view.findViewById(R.id.textView_Fecha1); 
						TxtFecha1.setText(((Lista_entrada_Log) entrada).get_Fecha());
						
						TextView TxtHora1 = (TextView) view.findViewById(R.id.textView_Hora1); 
						TxtHora1.setText(((Lista_entrada_Log) entrada).get_Hora());
						
						
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
