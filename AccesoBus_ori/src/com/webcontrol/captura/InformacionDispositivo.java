package com.webcontrol.captura;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class InformacionDispositivo extends Activity {
	
	Vibrator Vibrador;
	TextView TxtIMEI, TxtMAC, TxtNombre, TxtNombre1, TxtDivision, TxtDivision1, TxtLocal, TxtLocal1;
	Button BtnRegresar;
	DataBaseManager Manager;
	Cursor  CursorWs;
	String Idioma;
	private HomeKeyLocker mHomeKeyLocker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.informaciondispositivo_layout);
		
		mHomeKeyLocker = new HomeKeyLocker();
    	mHomeKeyLocker.lock(this);
//    	
		Vibrador  = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
		Manager = new DataBaseManager(this);
		
		TxtIMEI = (TextView) findViewById(R.id.textViewIMEI2);
		TxtMAC = (TextView) findViewById(R.id.textViewMAC2);
		TxtNombre = (TextView) findViewById(R.id.textViewNombreDispositivo2);
		TxtNombre1 = (TextView) findViewById(R.id.textViewNombreDispositivo1);
		TxtDivision = (TextView) findViewById(R.id.textViewDivision2);
		TxtDivision1 = (TextView) findViewById(R.id.textViewDivision1);
		TxtLocal = (TextView) findViewById(R.id.textViewLocal2);
		TxtLocal1 = (TextView) findViewById(R.id.textViewLocal1);
		BtnRegresar = (Button) findViewById(R.id.buttonRegresar);
		
		
//		HomeKeyLocker mHomeKeyLocker = new HomeKeyLocker();
//		mHomeKeyLocker.lock(this);
//		
		Cursor CursorIdioma = Manager.CursorIdioma();
    	
    	if (CursorIdioma.moveToFirst())
    	{
    		Idioma = CursorIdioma.getString(0);
    	}
		
		if(Idioma.equals("ESPANIOL"))
    	{
    		Textos("Nombre Dispositivo", "Identificador División", "Local", "Regresar");
    	}
    	else if(Idioma.equals("INGLES"))
    	{
    		Textos("Device Name", "ID Division", "Local", "Back");
    	}
    	else if(Idioma.equals("PORTUGUES"))
    	{
    		Textos("Nome do Dispositivo", "Divisão Identificador", "Local", "Voltar");
    	}
		
		CursorWs = Manager.CursorConfig();
		
		if (CursorWs.moveToFirst())
		{
			do{
				TxtNombre.setText(CursorWs.getString(6));
				TxtDivision.setText(CursorWs.getString(7));
				TxtLocal.setText(CursorWs.getString(8));
				
			}while(CursorWs.moveToNext());
		}
		
		TxtMAC.setText(getMAC());
		TxtIMEI.setText(getIMEI());
		
	}
	
	private void Textos(String TextoNombre, String TextoDvision, String TextoLocal, String TextoRegresar)
	{
		TxtNombre1.setText(TextoNombre);
		TxtDivision1.setText(TextoDvision);
		TxtLocal1.setText(TextoLocal);
		BtnRegresar.setText(TextoRegresar);
	}
	
	public String getIMEI()
	{
		TelephonyManager phonyManager = (TelephonyManager)getSystemService(android.content.Context.TELEPHONY_SERVICE);
		String id = phonyManager.getDeviceId();
		if (id == null)
		{
			id = "not available";
		}
	
		int phoneType = phonyManager.getPhoneType();
		switch(phoneType)
		{
			case TelephonyManager.PHONE_TYPE_NONE:
			return "NONE: " + id;
		}
		
		return id;
	}
	
	public String getMAC()
	{
		WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = manager.getConnectionInfo();
		String address = info.getMacAddress();
		return address;
	}
	
	public void Regresar(View v)
	{
		Vibrador.vibrate(80);
		Intent i = new Intent (this, Configuracion.class);
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
