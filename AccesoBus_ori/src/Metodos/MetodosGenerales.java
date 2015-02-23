package Metodos;

import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.webcontrol.captura.DataBaseManager;

public class MetodosGenerales {

	
	private DataBaseManager Manager;
	
	public String getFecha() {
		String FechaHora = getDateTime();
		String Fecha = FechaHora.substring(0, 10);
		String Hora = FechaHora.substring(11, FechaHora.length());
		return Fecha;
	}

	public String getHora() {
		String FechaHora = getDateTime();
		String Fecha = FechaHora.substring(0, 10);
		String Hora = FechaHora.substring(11, FechaHora.length());
		return Hora;
	}

	public String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	public String getIMEI(Context c) {
		
		TelephonyManager phonyManager = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
		String id = phonyManager.getDeviceId();
		if (id == null) {
			id = "not available";
		}

		int phoneType = phonyManager.getPhoneType();
		switch (phoneType) {
		case TelephonyManager.PHONE_TYPE_NONE:
			return "NONE: " + id;
		}

		return id;
	}
	public boolean isOnline(Context context) {
		boolean enabled = true;

		ConnectivityManager connectivityManager = (ConnectivityManager) context
		.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();

		if ((info == null || !info.isConnected() || !info.isAvailable())) {
		enabled = false;
		}
		return enabled;
		}
	
	public String devolverVersionApp(Context context) {
		String versionName;
		try {
			versionName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			versionName = "";
		}
		return versionName;
	}

	 public Bitmap ImagenBitmap (byte[] Imagen){
			
			byte[] ImagenEntranteEnByts = Imagen;
			ByteArrayInputStream imageStream = new ByteArrayInputStream(ImagenEntranteEnByts);
			Bitmap ImagenNueva = BitmapFactory.decodeStream(imageStream);
			return ImagenNueva;
		}
	 
	 
	
}
