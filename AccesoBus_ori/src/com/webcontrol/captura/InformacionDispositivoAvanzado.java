package com.webcontrol.captura;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class InformacionDispositivoAvanzado extends Activity {
	
	private Vibrator Vibrador;
	private Cursor CursorWS;
	private DataBaseManager Manager;
	String NAMESPACE, URL, UsuarioWs, ContraseniaWs, Autentificacion;
	String IdentificadorDivision, Local, NombreDispositivo, Idioma, Toast1, Toast2, Toast3;
	LinkedList<ObjetosSpinnerDivision> DataDivision;
	ArrayList<String> DataLocalDivision;
	Spinner SpDivision, SpLocal;
	int Contador, Contador2;
	EditText EdTxtNombreDispositivo;
	TextView TxtNombre, TxtDivision, TxtLocal;
	Button BtnGuardar, BtnCancelar;
ProgressDialog pDialog;
String mensaje;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setContentView(R.layout.informaciondispositivoavanzada_layout);
	    
	    Vibrador  = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
	    Manager = new DataBaseManager(this);    
	    SpDivision = (Spinner) findViewById(R.id.spinnerDivision);
	    SpLocal = (Spinner) findViewById(R.id.spinnerLocal);
	    EdTxtNombreDispositivo= (EditText) findViewById(R.id.editTextNameSpace);
	    TxtNombre= (TextView) findViewById(R.id.textViewName);
	    TxtDivision= (TextView) findViewById(R.id.textViewDivision);
	    TxtLocal= (TextView) findViewById(R.id.textViewLocal);
	    BtnGuardar= (Button) findViewById(R.id.buttonGuardar);
	    BtnCancelar= (Button) findViewById(R.id.buttonCancelar);
	      
	    DataLocalDivision = new ArrayList<String>();
	    Contador = 0;
	    Contador2 = 0;
	
//	    HomeKeyLocker mHomeKeyLocker = new HomeKeyLocker();
//		mHomeKeyLocker.lock(this);
	    DataDivision = new LinkedList<ObjetosSpinnerDivision>();
	    
	    Cursor CursorIdioma = Manager.CursorIdioma();
		
		if (CursorIdioma.moveToFirst())
		{
			Idioma = CursorIdioma.getString(0);
		}
		
		if(Idioma.equals("ESPANIOL"))
		{
			Textos("Nombre Dispositivo", "División", "Local", "Guardar", "Cancelar", 
					"Error al guardar, Intente Nuevamente", 
					"Error al conectar con el Servidor, Intente Nuevamente",
					"Configure el Servicio Web","Cargando Divisiones...");
		}
		else if(Idioma.equals("INGLES"))
		{
			Textos("Device Name", "Division", "Local", "Save", "Cancel", 
					"Failed to save, Try Again", "Failed to connect to the Server, " +
							"Try Again", "Configure the Web Service","Loading Divisions...");
		}
		else if(Idioma.equals("PORTUGUES"))
		{
			Textos("Nome do Dispositivo", "Divisão", "Local", "Salvar", 
					"Cancelar","Falha ao salvar, Tente Novamente",
					"Falha ao Conectar ao Servidor, Tente Novamente",
					"Configurar o Serviço Web","Carregando Divisões...");
		}
	    
	    CursorWS = Manager.CursorConfig();
		
		if (CursorWS.moveToFirst())
		{
			do{
				NAMESPACE= CursorWS.getString(1);
				URL = CursorWS.getString(2);
				UsuarioWs= CursorWS.getString(3); // desarrollo
				ContraseniaWs = CursorWS.getString(4); // Desa1.
				Autentificacion = CursorWS.getString(5);
				NombreDispositivo = CursorWS.getString(6);
				
			}while (CursorWS.moveToNext());
			
			EdTxtNombreDispositivo.setText(NombreDispositivo.toUpperCase());
		    pDialog = new ProgressDialog(InformacionDispositivoAvanzado.this);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setMessage(mensaje);
			pDialog.setCancelable(false);
			pDialog.setMax(100);
			new HiloDivision().execute();
		}
		else
		{
			Toast.makeText(getApplicationContext(), Toast3, Toast.LENGTH_LONG).show();
			Intent i = new Intent (this, ConfiguracionAvanzada.class);
			finish();
			startActivity(i);
			
		}
		
		SpDivision.setOnItemSelectedListener(new OnItemSelectedListener() {
	
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				if (Contador !=0)
				{
					Vibrador.vibrate(80);
				}
				Contador = Contador +1;
				IdentificadorDivision = ((ObjetosSpinnerDivision) arg0.getItemAtPosition(arg2)).getId();
				
				if(!(IdentificadorDivision.equals("0")))
				{
					new HiloLocalDivision().execute();
				}
				
			}
	
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
				
			}
		});
		
		SpLocal.setOnItemSelectedListener(new OnItemSelectedListener() {
	
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				if (Contador2 !=0)
				{
					Vibrador.vibrate(80);
				}
				Contador2 = Contador2 +1;
				Local = arg0.getItemAtPosition(arg2).toString();
				
			}
	
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
				
			}
		});
    
	}
	private void tareaLarga() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}
	private void Textos(String TextoNombre, String TextoDivision, String TextoLocal, String TextoGuardar, String TextoCancelar, String TextoToast1, String TextoToast2, String TextoToast3,String mensajeDialogo)
	{
		TxtNombre.setText(TextoNombre);
	    TxtDivision.setText(TextoDivision);
	    TxtLocal.setText(TextoLocal);
	    BtnGuardar.setText(TextoGuardar);
	    BtnCancelar.setText(TextoCancelar);
	    Toast1 = TextoToast1;
	    Toast2 = TextoToast2;
	    Toast3 = TextoToast3;
	    mensaje = mensajeDialogo;
	}
	
	public void BtnGuardar(View v)
	{
		Vibrador.vibrate(80);
		new HiloGuardarInformacion().execute();
		
		
	}
	public void Guardar()
	{
		Intent i = new Intent (this, ConfiguracionAvanzada.class);
		Cursor CursorConfig;
		CursorConfig = Manager.CursorConfig();	
		
		if (CursorConfig.moveToFirst())
		{
			//actualizamos
			Manager.ActualizarConfigInfoDispositivo(NombreDispositivo.toUpperCase(), IdentificadorDivision, Local);
		}
		else
		{
			//insertamos
			Manager.InsertarDatosConfig2(NombreDispositivo.toUpperCase(), IdentificadorDivision, Local);
		}
		
		finish();
		startActivity(i);
	}
	
	public void BtnCancelar(View v)
	{
		Vibrador.vibrate(80);
		Intent i = new Intent (this, ConfiguracionAvanzada.class);
		finish();
		startActivity(i);
	}
	
	@Override
	public void onBackPressed() 
	{
		
	}
	
	public void CargarSpinnerDivision()
	{
		ArrayAdapter<ObjetosSpinnerDivision> spinner_adapter = new ArrayAdapter<ObjetosSpinnerDivision>(this, android.R.layout.simple_spinner_item, DataDivision);
		spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		SpDivision.setAdapter(spinner_adapter);
	}
	
	public void CargarSpinnerLocal()
	{
		ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, DataLocalDivision);
		spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		SpLocal.setAdapter(null);
		SpLocal.setAdapter(spinner_adapter);
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
	
	
	private class HiloDivision extends AsyncTask<Void, Void, Void>
	 {		 
		// String IdDivision;
		 //String NombreDivision;
		 String _Respuesta;
		 JSONObject _RespuestaJson;
		 Exception _Exception = null;
	
		@Override
		protected void onPreExecute()
		{		
			pDialog.show();
		}
		
		@Override
		protected Void doInBackground(Void... params) 
		{
		//	tareaLarga();

			String METHOD_NAME = "WSDivision";
			String SOAP_ACTION = NAMESPACE + "WSDivision";
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			
			//request.addProperty("IdSincronizacion",IdSync);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // la version de WS 1.1
			envelope.dotNet = true; // estamos utilizando .net
			envelope.setOutputSoapObject(request); 
			HttpTransportSE transporte = new HttpTransportSE(URL);
			
			try 
			{
				if (Autentificacion.equals("SI"))
				{
					//*****
					List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
					String Conexion = UsuarioWs + ":" + ContraseniaWs;
					headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode(Conexion.getBytes())));
					transporte.call(SOAP_ACTION, envelope, headerList);
					///*****
				}
				else
				{
					transporte.call(SOAP_ACTION, envelope);
				}

				SoapPrimitive resultado = (SoapPrimitive) envelope.getResponse();
				 
				_Respuesta = resultado.toString(); 

				if (!(_Respuesta.equals("Sin Divisiones")))
				{
				 
				 	JSONObject jsonObject=new JSONObject(_Respuesta);
				 	JSONArray jsonArray= jsonObject.getJSONArray("Table");
				 	for(int i=0;i<jsonArray.length();i++)
			          { 
			              JSONObject jsonObject1=jsonArray.getJSONObject(i);
			     		 
			              DataDivision.add(new ObjetosSpinnerDivision(jsonObject1.getString("Id").toString(), jsonObject1.getString("Nombre").toString()));
			              //IdDivision = jsonObject1.getString("Id").toString();
			              //NombreDivision =jsonObject1.getString("Nombre").toString();
			          }
				}
				else
				{
					DataDivision.add(new ObjetosSpinnerDivision("0","Sin Divisiones"));
				}
		          
				
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				_Exception = e;
				//e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				_Exception = e;
				//e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				_Exception = e;
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void aVoid)
		{
			if (_Exception == null)
			{
				CargarSpinnerDivision();

			}
			else
			{
				
			}
		}		
	 }
	
	
	private class HiloLocalDivision extends AsyncTask<Void, Void, Void>
	 {		 
		 String _Respuesta;
		 String DAto;
		 JSONObject _RespuestaJson;
		 Exception _Exception = null;
	
		@Override
		protected void onPreExecute()
		{		
			//Toast.makeText(getApplicationContext(), IdentificadorDivision, Toast.LENGTH_LONG).show();//
		}
		
		@Override
		protected Void doInBackground(Void... params) 
		{

			String METHOD_NAME = "WSLocalPorDivision";
			String SOAP_ACTION = NAMESPACE + "WSLocalPorDivision";
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			
			request.addProperty("iddivision",IdentificadorDivision);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // la version de WS 1.1
			envelope.dotNet = true; // estamos utilizando .net
			envelope.setOutputSoapObject(request); 
			HttpTransportSE transporte = new HttpTransportSE(URL);
			
			try 
			{
				if (Autentificacion.equals("SI"))
				{
					//*****
					List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
					String Conexion = UsuarioWs + ":" + ContraseniaWs;
					headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode(Conexion.getBytes())));
					transporte.call(SOAP_ACTION, envelope, headerList);
					///*****
				}
				else
				{
					transporte.call(SOAP_ACTION, envelope);
				}

				SoapPrimitive resultado = (SoapPrimitive) envelope.getResponse();
				 
				_Respuesta = resultado.toString(); 
				 
				DataLocalDivision.clear();
				
				if (!(_Respuesta.equals("Sin Locales")))
				{
					 JSONObject jsonObject=new JSONObject(_Respuesta);
			          JSONArray jsonArray= jsonObject.getJSONArray("Table");
			          
			          for(int i=0;i<jsonArray.length();i++)
			          { 
			              JSONObject jsonObject1=jsonArray.getJSONObject(i);
			     		 
			              DataLocalDivision.add(jsonObject1.getString("LOCAL").toString());
			              //IdDivision = jsonObject1.getString("Id").toString();
			              //NombreDivision =jsonObject1.getString("Nombre").toString();
			          }
				}
				else
				{
					DataLocalDivision.add("Sin Locales");
				}
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				_Exception = e;
				//e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				_Exception = e;
				//e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				_Exception = e;
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void aVoid)
		{
			if (_Exception == null)
			{
				CargarSpinnerLocal();
				pDialog.dismiss();

			}
			else
			{
				
			}
		}		
	 }
	
	private class HiloGuardarInformacion extends AsyncTask<Void, Void, Void>
	 {		 
		String Marca, Modelo, ImeiBase64, Imeimd5;
		String _Respuesta;
		JSONObject _RespuestaJson;
		byte[] ImeiByte;
		Exception _Exception = null;
		int Retorno;
	
		@Override
		protected void onPreExecute()
		{		
			Marca = android.os.Build.MANUFACTURER;
			Modelo = android.os.Build.MODEL;
			NombreDispositivo = EdTxtNombreDispositivo.getText().toString();
		}
		
		@Override
		protected Void doInBackground(Void... params) 
		{

			String METHOD_NAME = "WSInsertarInformacionDispositivo";
			String SOAP_ACTION = NAMESPACE + "WSInsertarInformacionDispositivo";
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			
			request.addProperty("marca",Marca);
			request.addProperty("modelo",Modelo);
			request.addProperty("imei", getIMEI());
			request.addProperty("mac",getMAC());
			request.addProperty("nombredispositivo",NombreDispositivo);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // la version de WS 1.1
			envelope.dotNet = true; // estamos utilizando .net
			envelope.setOutputSoapObject(request); 
			HttpTransportSE transporte = new HttpTransportSE(URL);
			
			try 
			{
				if (Autentificacion.equals("SI"))
				{
					//*****
					List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
					String Conexion = UsuarioWs + ":" + ContraseniaWs;
					headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode(Conexion.getBytes())));
					transporte.call(SOAP_ACTION, envelope, headerList);
					///*****
				}
				else
				{
					transporte.call(SOAP_ACTION, envelope);
				}

				SoapPrimitive resultado = (SoapPrimitive) envelope.getResponse();
				 
				_Respuesta = resultado.toString(); 
				
				_RespuestaJson = new JSONObject(_Respuesta);		
				Retorno = _RespuestaJson.getInt("retorno");	
				
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				_Exception = e;
				//e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				_Exception = e;
				//e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				_Exception = e;
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void aVoid)
		{
			if (_Exception == null)
			{
				if (Retorno == 1)
				{
					//Guardar();
					
					Intent i = new Intent(InformacionDispositivoAvanzado.this, ConfiguracionAvanzada.class);
					Cursor CursorConfig;
					CursorConfig = Manager.CursorConfig();	
					
					if (CursorConfig.moveToFirst())
					{
						//actualizamos
						Manager.ActualizarConfigInfoDispositivo(NombreDispositivo.toUpperCase(), IdentificadorDivision, Local);
					}
					else
					{
						//insertamos
						Manager.InsertarDatosConfig2(NombreDispositivo.toUpperCase(), IdentificadorDivision, Local);
					}
					
					finish();
					startActivity(i);
				}
				else
				{
					Toast.makeText(getApplicationContext(), Toast1, Toast.LENGTH_LONG).show();
				}		
			}
			else
			{
				Toast.makeText(getApplicationContext(), Toast2, Toast.LENGTH_LONG).show();
			}
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
