//package Hilos;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.ksoap2.HeaderProperty;
//import org.ksoap2.SoapEnvelope;
//import org.ksoap2.serialization.SoapObject;
//import org.ksoap2.serialization.SoapPrimitive;
//import org.ksoap2.serialization.SoapSerializationEnvelope;
//import org.ksoap2.transport.HttpTransportSE;
//import org.xmlpull.v1.XmlPullParserException;
//
//import com.webcontrol.captura.DataBaseManager;
//
//import UTIL.MetodosGenerales;
//import android.app.Activity;
//import android.database.Cursor;
//import android.graphics.Color;
//import android.os.AsyncTask;
//import android.os.Vibrator;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class HiloSincronizarVehiculos extends AsyncTask<String, Void, String> {
//
//	Vibrator Vibrador;
//	private DataBaseManager Manager;
//	Cursor CursorSincronizacion;
//	private MetodosGenerales util;
//	
//	String Rut, IdSync, METHOD_NAME, SOAP_ACTION, NAMESPACE, URL, UsuarioWs,
//	ContraseniaWs, Autentificacion, EstadoSync, TipoSync,
//	TipoSincronizacion, Idioma, Toast1, Toast2, Toast3, Toast4, Toast5,
//	Toast6, Toast7, Toast8, Toast9, Toast10;
//	
//	String UID = "";
//	String EstadoUID = "";
//	String IdVehiculo = "";
//	String Marca = "";
//	String Modelo = "";
//	String AnioVehiculo = "";
//	String TipoVehiculo = "";
//	String AutorizacionVehiculo = "";
//	String FechaConsulta = "";
//	String ROLEmpresa = "";
//	String NombreEmpresa = "";
//	String Fecha = "";
//	String Estado = "";
//	String Mensaje = "";
//	int ContadorMax = 0;
//	String Restantes;
//
//	int ContadorErrores;
//	String _Respuesta;
//	JSONObject _RespuestaJson;
//	Exception _Exception = null;
//	TextView TxtSincronizacion;
//	Button BtnSyncInicioDia;
//	Button BtnRegresar;
//	
//	@Override
//	protected void onPreExecute() {
//	CursorSincronizacion = Manager.CursorSincronizacionVehiculo();
//
//		if (CursorSincronizacion.moveToFirst()) {
//			do {
//				IdSync = CursorSincronizacion.getString(1);
//				ContadorErrores = CursorSincronizacion.getInt(2);
//
//			} while (CursorSincronizacion.moveToNext());
//		}
//	}
//
//	@Override
//	protected Void doInBackground(Void... params) {
//
//		METHOD_NAME = "WSSincronizarVehiculos";
//		SOAP_ACTION = NAMESPACE + "WSSincronizarVehiculos";
//		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
//
//		request.addProperty("IdSincronizacion", IdSync);
//		request.addProperty("imei", util.getIMEI());
//
//		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
//				SoapEnvelope.VER11); // la version de WS 1.1
//		envelope.dotNet = true; // estamos utilizando .net
//		envelope.setOutputSoapObject(request);
//		HttpTransportSE transporte = new HttpTransportSE(URL);
//
//		try {
//			if (Autentificacion.equals("SI")) {
//				// *****
//				List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
//				String Conexion = UsuarioWs + ":" + ContraseniaWs;
//				headerList.add(new HeaderProperty("Authorization", "Basic "
//						+ org.kobjects.base64.Base64.encode(Conexion
//								.getBytes())));
//				transporte.call(SOAP_ACTION, envelope, headerList);
//				// /*****
//			} else {
//				transporte.call(SOAP_ACTION, envelope);
//			}
//
//			SoapPrimitive resultado = (SoapPrimitive) envelope
//					.getResponse();
//
//			_Respuesta = resultado.toString();
//			_RespuestaJson = new JSONObject(_Respuesta);
//
//			UID = _RespuestaJson.getString("UID");
//			EstadoUID = _RespuestaJson.getString("EstadoUID");
//			IdVehiculo = _RespuestaJson.getString("IdVehiculo");
//			Marca = _RespuestaJson.getString("Marca");
//			Modelo = _RespuestaJson.getString("Modelo");
//			AnioVehiculo = _RespuestaJson.getString("AnioVehiculo");
//			TipoVehiculo = _RespuestaJson.getString("TipoVehiculo");
//			AutorizacionVehiculo = _RespuestaJson.getString("Autorizacion");
//			ROLEmpresa = _RespuestaJson.getString("ROLEmpresa");
//			NombreEmpresa = _RespuestaJson.getString("NombreEmpresa");
//			FechaConsulta = _RespuestaJson.getString("Fecha");
//			Estado = _RespuestaJson.getString("Estado");
//			Mensaje = _RespuestaJson.getString("Mensaje");
//
//			Restantes = _RespuestaJson.getString("restantes");
//			IdSync = _RespuestaJson.getString("idsincronizacion");
//
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			_Exception = e;
//			// e.printStackTrace();
//		} catch (XmlPullParserException e) {
//			// TODO Auto-generated catch block
//			_Exception = e;
//			// e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			_Exception = e;
//		}
//		return null;
//	}
//
//	@Override
//	protected void onPostExecute(Void aVoid) {
//		int datosrestantes = (Integer.parseInt(Restantes));
//		if (_Exception == null) {
//
//			// if (Progreso == -1) {
//			// progress.setMax(datosrestantes);
//			// progress.setProgress(0);
//			// progress.setVisibility(View.VISIBLE);
//			// Progreso = datosrestantes;
//			// } else {
//			// progress.incrementProgressBy(1);
//			// }
//
//			TxtSincronizacion.setText(Toast8 + datosrestantes + Toast6);
//			// progress.setMessage("Sincronizando Vehículos... Restan " +
//			// datosrestantes + " Registros");
//
//			if (Integer.parseInt(IdSync) != 0) {
//				if (TipoSincronizacion.equals("INICIAL")) {
//					Manager.ActualizarSincronizacionVehiculo(IdVehiculo,
//							IdSync, 0, "ON", "INICIAL");
//				}
//				// else if (TipoSincronizacion.equals("INICIODIA")) {
//				// Manager.ActualizarSincronizacionVehiculo(IdVehiculo,
//				// IdSync, 0, "ON", "INICIODIA");
//				// }
//
//				Cursor CursorVehiculo;
//				CursorVehiculo = Manager.BuscarVehiculoId(IdVehiculo);
//				if (CursorVehiculo.moveToFirst()) {
//					int identificadorvehiculo = CursorVehiculo.getInt(0);
//					// actualizamos vehiculo
//					Manager.ActualizarDataVehiculo(identificadorvehiculo,
//							UID, EstadoUID, IdVehiculo, Marca, Modelo,
//							AnioVehiculo, TipoVehiculo, NombreEmpresa,
//							ROLEmpresa, "0", AutorizacionVehiculo,
//							FechaConsulta, Mensaje);
//				} else {
//					Manager.InsertarDatosVehiculo(UID, EstadoUID,
//							IdVehiculo, Marca, Modelo, AnioVehiculo,
//							TipoVehiculo, NombreEmpresa, ROLEmpresa, "0",
//							AutorizacionVehiculo, FechaConsulta, Mensaje);
//				}
//			}
//
//			if (!Restantes.equals("null")) {
//				new HiloSincronizarVehiculos().execute();
//			} else {
//				TxtSincronizacion.setText(Toast9);
//				Manager.InsertarDatosLog(
//						"Sincronización Vehículos Finalizada ", util.getFecha(),
//						util.getHora());
//
//				if (!IdSync.equals("0")) {
//					if (TipoSincronizacion.equals("INICIAL")) {
//						Manager.InsertarDatosLog(
//								"Sincronización Inicial Finalizada ",
//								util.getFecha(), util.getHora());
//						Manager.ActualizarSincronizacionVehiculo(
//								IdVehiculo, IdSync, 0, "OFF", "INICIODIA");
//						BtnSyncInicioDia.setTextColor(Color.WHITE);
//						BtnSyncInicioDia.setEnabled(true);
//					}
//					// else if (TipoSincronizacion.equals("INICIODIA")) {
//					// Manager.InsertarDatosLog(
//					// "Sincronización Inicio Día Finalizada ",
//					// getFecha(), getHora());
//					// Manager.ActualizarSincronizacionVehiculo(
//					// IdVehiculo, IdSync, 0, "OFF", "INCREMENTAL");
//					// BtnSyncFinDia.setTextColor(Color.WHITE);
//					// BtnSyncFinDia.setEnabled(true);
//					// }
//				} else {
//					if (TipoSincronizacion.equals("INICIAL")) {
//						Manager.InsertarDatosLog(
//								"Sincronización Inicial Finalizada ",
//								util.getFecha(), util.getHora());
//						Manager.ActualizarSincronizacionVehiculoFin("OFF",
//								"INICIODIA");
//						BtnSyncInicioDia.setTextColor(Color.WHITE);
//						BtnSyncInicioDia.setEnabled(true);
//					}
//					// else if (TipoSincronizacion.equals("INICIODIA")) {
//					// Manager.InsertarDatosLog(
//					// "Sincronización Inicio Día Finalizada ",
//					// getFecha(), getHora());
//					// Manager.ActualizarSincronizacionVehiculoFin("OFF",
//					// "INCREMENTAL");
//					// BtnSyncFinDia.setTextColor(Color.WHITE);
//					// BtnSyncFinDia.setEnabled(true);
//					// }
//				}
//				BtnRegresar.setTextColor(Color.WHITE);
//				BtnRegresar.setEnabled(true);
//				// progress.setVisibility(View.INVISIBLE);
//			}
//
//		} else {
//			if (ContadorErrores >= 5) {
//				if (TipoSincronizacion.equals("INICIAL")) {
//					Manager.InsertarDatosLog(
//							"Error: Sincronización Inicial Vehículos ",
//							util.getFecha(), util.getHora());
//					Manager.ActualizarContadorSincronizacion(0, "OFF",
//							"INICIAL");
//					BtnSyncInicial.setTextColor(Color.WHITE);
//					BtnSyncInicial.setEnabled(true);
//				} else if (TipoSincronizacion.equals("INICIODIA")) {
//					Manager.InsertarDatosLog(
//							"Error: Sincronización Inicio Día Vehículos",
//							util.getFecha(), util.getHora());
//					Manager.ActualizarContadorSincronizacion(0, "OFF",
//							"INICIODIA");
//					BtnSyncInicioDia.setTextColor(Color.WHITE);
//					BtnSyncInicioDia.setEnabled(true);
//				}
//
//				BtnRegresar.setTextColor(Color.WHITE);
//				BtnRegresar.setEnabled(true);
//				// progress.setVisibility(View.INVISIBLE);
//				TxtSincronizacion.setText(Toast2 + datosrestantes + Toast3);
//				Toast.makeText(this,
//						Toast2 + datosrestantes + Toast3, Toast.LENGTH_LONG)
//						.show();
//			} else {
//				if (TipoSincronizacion.equals("INICIAL")) {
//					Manager.ActualizarContadorSincronizacion(
//							(ContadorErrores + 1), "ON", "INICIAL");
//				} else if (TipoSincronizacion.equals("INICIODIA")) {
//					Manager.ActualizarContadorSincronizacion(
//							(ContadorErrores + 1), "ON", "INICIODIA");
//				}
//				new HiloSincronizarVehiculos().execute();
//			}
//		}
//	}
//	
//	
//}
//
