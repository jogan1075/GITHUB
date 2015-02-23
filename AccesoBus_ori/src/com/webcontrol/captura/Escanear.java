package com.webcontrol.captura;

import mobi.pdf417.Pdf417MobiScanData;
import mobi.pdf417.Pdf417MobiSettings;
import mobi.pdf417.activity.Pdf417ScanActivity;
import net.photopay.base.BaseBarcodeActivity;
import net.photopay.base.BaseRecognitionActivity;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;



public class Escanear extends Activity {

	DataBaseManager Manager;
	Button volver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Manager = new DataBaseManager(this);

		cargar();

	}

	public void cargar() {

		Intent intent = new Intent(this, Pdf417ScanActivity.class);

		intent.putExtra(BaseRecognitionActivity.EXTRAS_LICENSE_KEY,
				"MTS6-4K52-KVSL-J6QW-55X7-FZ2U-YUP2-ZBIU");
		// intent.putExtra(Pdf417ScanActivity.EXTRAS_BEEP_RESOURCE, R.raw.beep);

		Pdf417MobiSettings sett = new Pdf417MobiSettings();
		sett.setRemoveOverlayEnabled(true);

		Cursor CursorPantalla = Manager.BuscarPantalla();
		String Pantalla = "";

		if (CursorPantalla.moveToFirst()) {
			do {
				Pantalla = CursorPantalla.getString(0);

			} while (CursorPantalla.moveToNext());

		}

		if ((Pantalla.equals("VehiculoIngreso"))
				|| (Pantalla.equals("VehiculoSalida"))) {
			sett.setCode128Enabled(true);
			sett.setCode39Enabled(true);
			
			sett.setPdf417Enabled(false);
			sett.setQrCodeEnabled(false);
		} else {
			sett.setCode128Enabled(false);
			sett.setCode39Enabled(false);
			sett.setPdf417Enabled(true);
			sett.setQrCodeEnabled(true);
		}

		sett.setDontShowDialog(true);
		sett.setNullQuietZoneAllowed(true);

		intent.putExtra(BaseRecognitionActivity.EXTRAS_SETTINGS, sett);
		startActivityForResult(intent, 1);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (data != null) {
			Pdf417MobiScanData scanData = data
					.getParcelableExtra(BaseBarcodeActivity.EXTRAS_RESULT);

			String barcodeData = scanData.getBarcodeData().trim();
			String barcodeType = scanData.getBarcodeType();
			Intent intent = new Intent(getApplicationContext(), inicio.class);
			intent.putExtra("rut", barcodeData);
			intent.putExtra("tipo", barcodeType);
			finish();
			startActivity(intent);
			

		} else {
			finish();
			startActivity(new Intent(getApplicationContext(), inicio.class));
		}

	}

}
