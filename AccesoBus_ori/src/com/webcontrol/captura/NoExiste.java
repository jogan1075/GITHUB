package com.webcontrol.captura;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NoExiste extends Activity {
	TextView TxtNombre, TxtNombreFuncionario,  TxtId, TxtIdUsuario, TxtIdEmpresa, TxtNombreEmpresa, TxtAutorizacion, TxtAutorizacionConductor, TxtFecha, TxtMensaje, TxtEmpresa, TxtEmpresa1;
	ImageView verimagen1;
	Button BtnPause;
	ProgressBar progress;
	  int Pause = 12;
	  MediaPlayer mpCorrecto, mpIncorrecto;
	  Vibrator Vibrador;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_no_existe);
		
		mpCorrecto = MediaPlayer.create(getApplicationContext(), R.raw.ok);
		mpIncorrecto= MediaPlayer.create(getApplicationContext(), R.raw.error);
		mpIncorrecto.start();
		Vibrador  = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
		
		verimagen1 = (ImageView) findViewById(R.id.imageView12);
		TxtNombre = (TextView) findViewById(R.id.textViewNombre2);
		TxtNombreFuncionario = (TextView) findViewById(R.id.textViewNombreFuncionario2);
		TxtId = (TextView) findViewById(R.id.textViewId2);
		TxtIdUsuario = (TextView) findViewById(R.id.textViewIdUsuario2);
		TxtIdEmpresa = (TextView) findViewById(R.id.textViewIdEmpresa2);
		TxtEmpresa = (TextView) findViewById(R.id.textView52);
		TxtEmpresa1 = (TextView) findViewById(R.id.textView72);
		TxtNombreEmpresa = (TextView) findViewById(R.id.textViewNombreEmpresa2);
		TxtAutorizacion = (TextView) findViewById(R.id.textViewAutorizacion2);
		TxtAutorizacionConductor = (TextView) findViewById(R.id.textViewAutorizacionConductor2);
		TxtFecha = (TextView) findViewById(R.id.textViewFecha2);
		TxtMensaje = (TextView) findViewById(R.id.textViewMensaje2);
		progress = (ProgressBar) findViewById(R.id.progressBarPause2);
		BtnPause = (Button) findViewById(R.id.buttonPause2);
		progress.setMax(12);
		progress.setProgress(12);
		
		HomeKeyLocker mHomeKeyLocker = new HomeKeyLocker();
		mHomeKeyLocker.lock(this);
		
		 new Thread(new Task()).start();
	}
	
	public void Pause(View v){
		Vibrador.vibrate(80);
		finish();
		startActivity(new Intent(getApplicationContext(),inicio.class));
	}
	 class Task implements Runnable {
         @Override
         public void run() {
        	
        		 for (int i = 11; i >= 0; i--) {
                     try {
                         Thread.sleep(250);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                      progress.incrementProgressBy(-1);
                    Pause--;
                      if (Pause == 0)
                      {
                     	 finish();	
                     	startActivity(new Intent(getApplicationContext(),inicio.class));
                      } 
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
