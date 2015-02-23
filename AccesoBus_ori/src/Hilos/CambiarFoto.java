package Hilos;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.webcontrol.captura.DataBaseManager;
import com.webcontrol.captura.R;

public class CambiarFoto extends AsyncTask<String, Void, String> {
	ImageView foto;
	DataBaseManager Manager;
	String color;
	Context context;

	public CambiarFoto(ImageView img, Context contexto) {
		this.foto = img;
		this.context = contexto;
		Manager = new DataBaseManager(context);
	}

	private void tareaLarga() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		//tareaLarga();
		Cursor VerColorFoto = Manager.CursorCambiarFoto();
		if (VerColorFoto.moveToFirst()) {
			color = VerColorFoto.getString(0);
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (color.equalsIgnoreCase("verde")) {
			foto.setImageResource(R.drawable.verde);
		} else if (color.equalsIgnoreCase("azul")) {
			foto.setImageResource(R.drawable.azul);
		} else if (color.equalsIgnoreCase("rojo")) {
			foto.setImageResource(R.drawable.rojo);
		} else if (color.equalsIgnoreCase("blanco")) {
			foto.setImageResource(R.drawable.blanco);
		}

		new CambiarFoto(foto, context).cancel(true);
	}

}
