package com.webcontrol.captura;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseManager {

	public static final String TABLE_FUNCIONARIO = "FUNCIONARIO";
	public static final String CN_ID = "_id";
	public static final String CN_UID = "UID";
	public static final String CN_ESTADOUID = "EstadoUID";
	public static final String CN_IDFUNCIONARIO = "IdFuncionario";
	public static final String CN_NOMBRE = "Nombre";
	public static final String CN_APELLIDO = "Apellido";
	public static final String CN_NOMBREEMPRESA = "NombreEmpresa";
	public static final String CN_IDEMPRESA = "IdEmpresa";
	public static final String CN_OSTFUN = "Ost";
	public static final String CN_CCOSTO = "CCosto";
	public static final String CN_TIPOPASEFUN = "TipoPase";
	public static final String CN_IMAGEN = "Imagen";
	public static final String CN_AUTORIZACION = "Autorizacion";
	public static final String CN_AUTORIZACIONCONDUCTOR = "AutorizacionConductor";
	public static final String CN_FECHACONSULTA = "FechaConsulta";
	public static final String CN_IDSYNC = "idSYNC";

	public static final String TABLE_VEHICULO = "VEHICULO";
	public static final String CN_ID2 = "_id";
	public static final String CN_UIDVEHICULO = "UID";
	public static final String CN_ESTADOUIDVEHICULO = "EstadoUID";
	public static final String CN_IDVEHICULO = "IdVehiculo";
	public static final String CN_MARCA = "Marca";
	public static final String CN_MODELO = "Modelo";
	public static final String CN_ANIO = "Anio";
	public static final String CN_TIPOVEHICULO = "TipoVehiculo";
	public static final String CN_NOMBREEMPRESAVEHICULO = "NombreEmpresa";
	public static final String CN_IDEMPRESAVEHICULO = "IdEmpresa";
	public static final String CN_IMAGENVEHICULO = "Imagen";
	public static final String CN_AUTORIZACIONVEHICULO = "Autorizacion";
	public static final String CN_FECHACONSULTAVEHICULO = "FechaConsulta";
	public static final String CN_MENSAJEVEHICULO = "Mensaje";

	public static final String TABLE_GUARIA = "GUARDIA";
	public static final String CN_IDGUARDIA = "_id";
	public static final String CN_UIDGUARDIA = "UID";
	public static final String CN_ESTADOUIDGUARDIA = "EstadoUID";
	public static final String CN_IDFUNCIONARIOGUARDIA = "IdFuncionario";
	public static final String CN_NOMBREGUARDIA = "Nombre";
	public static final String CN_APELLIDOGUARDIA = "Apellido";
	public static final String CN_NOMBREEMPRESAGUARDIA = "NombreEmpresa";
	public static final String CN_IDEMPRESAGUARDIA = "IdEmpresa";
	public static final String CN_AUTORIZACIONGUARDIA = "Autorizacion";
	public static final String CN_IMAGENGUARDIA = "Imagen";

	public static final String TABLE_DATOSGUARDIA = "DATOSGUARDIA";
	public static final String CN_IDDATOSGUARDIA = "_id";
	public static final String CN_NOMBREDATOSGUARDIA = "Nombre";
	public static final String CN_APELLIDODATOSGUARDIA = "Apellido";

	public static final String TABLE_CONTROLACCESO = "CONTROLACCESO";
	public static final String CN_IDCONTROL = "Id";
	public static final String CN_UIDCONTROL = "UID"; // si no existe es 0
	public static final String CN_IDFUNCIONARIOCONTROL = "IdFuncionario";
	public static final String CN_IDEMPRESACONTROL = "IdEmpresa";
	public static final String CN_CONDUCTOR = "Conductor"; // 1 si es que va
	public static final String CN_TRANSPORTE = "Transporte";// INGRESO ZZ9999 -
															// // SALIDA ZZ9998
	public static final String CN_CODIGOERROR = "CodigoError";
	public static final String CN_IDLOCAL = "IdLocal";
	public static final String CN_SENTIDO = "Sentido"; // IN OUT
														// Sentido.CodigoError,FechaControl,HoraControl,IdLocal
	public static final String CN_CENTROCOSTO = "CentroCosto"; // Division
	public static final String CN_OST = "OST";
	public static final String CN_TIPOPASE = "TipoPase"; // VISITA, TRABAJO
	public static final String CN_ESTADOCONEXION = "EstadoConexion"; // LOCAL,
	public static final String CN_FECHACONTROL = "FechaControl";
	public static final String CN_HORACONTROL = "HoraControl";
	public static final String CN_SINCRONIZACION = "Sincronizacion"; // 1
																		// sincronizado,
																		// 0 NO

	public static final String TABLE_CHOFERPASAJERO = "CHOFERPASAJERO";
	public static final String CN_IDCHOFERPASAJERO = "Id";
	public static final String CN_VEHICULO = "Vehiculo";// 1 si , 0 no
	public static final String CN_PATENTE = "Patente";
	public static final String CN_SENTIDOCP = "Sentido";

	public static final String TABLE_PANTALLA = "PANTALLA";
	public static final String CN_IDPANTALLA = "Id";
	public static final String CN_NOMBREPANTALLA = "NombrePantalla";

	public static final String TABLE_CONEXION = "CONEXION";
	public static final String CN_IDCONEXION = "Id";
	public static final String CN_TIPOCONEXION = "TipoConexion"; // Local Movil
	public static final String CN_CONTADOR = "Contador"; // si llega a 3 cambia
															// a local
	public static final String CN_FECHAHORACONEXION = "FechaHoraConexion";

	public static final String TABLE_CONFIG = "CONFIG";
	public static final String CN_IDCONFIG = "Id";
	public static final String CN_CONTRASENIACONFIG = "ContraseniaConfig";
	public static final String CN_NAMESPACEWS = "NameSpaceWS";
	public static final String CN_URLWS = "UrlWS";
	public static final String CN_USUARIOWS = "UsuarioWS";
	public static final String CN_CONTRASENIAWS = "ContraseniaWS";
	public static final String CN_AUTENTIFICACIONWS = "AutentificacionWS";
	public static final String CN_NOMBREDISPOSITIVO = "NombreDispositivo";
	public static final String CN_DIVISION = "Division";
	public static final String CN_LOCAL = "Local";

	public static final String TABLE_CONFIGINTRANET = "CONFIGINTRANET";
	public static final String CN_IDCONFIGINTRANET = "Id";
	public static final String CN_CONTRASENIACONFIGINTRANET = "ContraseniaConfig";
	public static final String CN_NAMESPACEWSINTRANET = "NameSpaceWS";
	public static final String CN_URLWSINTRANET = "UrlWS";
	public static final String CN_USUARIOWSINTRANET = "UsuarioWS";
	public static final String CN_CONTRASENIAWSINTRANET = "ContraseniaWS";
	public static final String CN_AUTENTIFICACIONWSINTRANET = "AutentificacionWS";

	public static final String TABLE_LOG = "LOG";
	public static final String CN_IDLOG = "Id";
	public static final String CN_DESCRIPCION = "Descripcion";
	public static final String CN_RUTFUNCIONARIOLOG = "Rut";
	public static final String CN_FECHALOG = "Fecha";
	public static final String CN_HORALOG = "Hora";

	public static final String TABLE_SINCRONIZACION = "SINCRONIZACION";
	public static final String CN_IDSINCRONIZACION = "Id";
	public static final String CN_RUT = "Rut";
	public static final String CN_PLACA = "Placa";
	public static final String CN_IDSYNCGUARDIA = "IdSyncGuardia";
	public static final String CN_IDSYNCPERSONA = "IdSyncPersona";
	public static final String CN_IDSYNCVEHICULO = "IdSyncVehiculo";
	public static final String CN_CONTADORSYNC = "Contador";
	public static final String CN_ESTADOSYNC = "EstadoSync"; // ON OFF
	public static final String CN_TIPOSYNC = "TipoSync";// INICIAL, INICIODIA,
														// INCREMENTAL

	public static final String TABLE_SINCRONIZACIONINTRANET = "SINCRONIZACIONINTRANET";
	public static final String CN_IDSINCRONIZACIONINTRANET = "Id";
	public static final String CN_RUTINTRANET = "Rut";
	public static final String CN_PLACAINTRANET = "Placa";
	public static final String CN_IDSYNCGUARDIAINTRANET = "IdSyncGuardia";
	public static final String CN_IDSYNCPERSONAINTRANET = "IdSyncPersona";
	public static final String CN_IDSYNCVEHICULOINTRANET = "IdSyncVehiculo";
	public static final String CN_CONTADORSYNCINTRANET = "Contador";
	public static final String CN_ESTADOSYNCINTRANET = "EstadoSync"; // ON OFF
	public static final String CN_TIPOSYNCINTRANET = "TipoSync";

	public static final String TABLE_IDIOMA = "IDIOMA";
	public static final String CN_IDIDIOMA = "Id";
	public static final String CN_IDIOMA = "Idioma";

	public static final String TABLE_CONEXIONWSCORRECTA = "CONEXIONCORRECTA";
	public static final String CN_IDCONEXIONCORRECTA = "Id";
	public static final String CN_ESTADOCONEXIONCORRECTA = "Idioma";

	public static final String TABLE_CONFIGAPP = "CONFIGAPP";
	public static final String CN_IDCONFIGAPP = "Id";
	public static final String CN_CAMARA = "Camara";
	public static final String CN_NFC = "NFC";
	public static final String CN_LOG = "Log";
	public static final String CN_SINC = "Sinc";

	public static final String TABLE_CONF_NFC = "NFC";
	public static final String CN_IDNFCCAM = "Id";
	public static final String CN_ESTADONFCCAM = "Estado";

	public static final String TABLE_TIPOCONEXION3GWIFI = "TIPOCONEXION3GWIFI";
	public static final String CN_TIPOCONEXION3GWIFI = "Id";
	public static final String CN_3GWIFI = "Tipo";

	public static final String TABLE_SINCOK = "ServiceUpdate";
	public static final String CN_IDCONFSINC = "Id";
	public static final String CN_ESTADOCONF = "Estado";

	public static final String TABLE_CONEXIONVALIDAD = "CONEXIONINTERNETINTRANETOK";
	public static final String CN_IDCONEXIONVAL = "Id";
	public static final String CN_ESTADOOK_INTRANET = "EstadoINTRANET";
	public static final String CN_ESTADOOK_INTERNET = "EstadoINTERNET";

	public static final String TABLE_CAMBIARFOTO = "FotoSinc";
	public static final String CN_IDCAMBIARFOTO = "Id";
	public static final String CN_COLOR_FOTO = "Color";

	public static final String CREATE_TABLE = "create table "
			+ TABLE_FUNCIONARIO + " (" + CN_ID
			+ " integer primary key autoincrement, " + CN_UID + " text, "
			+ CN_ESTADOUID + " text, " + CN_IDFUNCIONARIO + " text, "
			+ CN_NOMBRE + " text, " + CN_APELLIDO + " text, "
			+ CN_NOMBREEMPRESA + " text, " + CN_IDEMPRESA + " text, "
			+ CN_OSTFUN + " text," + CN_CCOSTO + " text, " + CN_TIPOPASEFUN
			+ " text, " + CN_IMAGEN + " text, " + CN_AUTORIZACION + " text, "
			+ CN_AUTORIZACIONCONDUCTOR + " text, " + CN_FECHACONSULTA
			+ " text, " + CN_IDSYNC + " text);";

	public static final String CREATE_TABLE2 = "create table " + TABLE_VEHICULO
			+ " (" + CN_ID2 + " integer primary key autoincrement, "
			+ CN_UIDVEHICULO + " text, " + CN_ESTADOUIDVEHICULO + " text, "
			+ CN_IDVEHICULO + " text, " + CN_MARCA + " text, " + CN_MODELO
			+ " text, " + CN_ANIO + " text, " + CN_TIPOVEHICULO + " text, "
			+ CN_NOMBREEMPRESAVEHICULO + " text, " + CN_IDEMPRESAVEHICULO
			+ " text, " + CN_IMAGENVEHICULO + " text, "
			+ CN_AUTORIZACIONVEHICULO + " text, " + CN_FECHACONSULTAVEHICULO
			+ " text, " + CN_MENSAJEVEHICULO + " text);";

	public static final String CREATE_TABLE3 = "create table "
			+ TABLE_CONTROLACCESO + " (" + CN_IDCONTROL
			+ " integer primary key autoincrement, " + CN_UIDCONTROL
			+ " text, " + CN_IDFUNCIONARIOCONTROL + " text, "
			+ CN_IDEMPRESACONTROL + " text, " + CN_CONDUCTOR + " text, "
			+ CN_TRANSPORTE + " text, " + CN_CODIGOERROR + " text, "
			+ CN_IDLOCAL + " text, " + CN_SENTIDO + " text, " + CN_CENTROCOSTO
			+ " text, " + CN_OST + " text, " + CN_TIPOPASE + " text, "
			+ CN_ESTADOCONEXION + " text, " + CN_FECHACONTROL + " text, "
			+ CN_HORACONTROL + " text, " + CN_SINCRONIZACION + " text);";

	public static final String CREATE_TABLE4 = "create table "
			+ TABLE_CHOFERPASAJERO + " (" + CN_IDCHOFERPASAJERO
			+ " integer primary key autoincrement, " + CN_VEHICULO + " text, "
			+ CN_PATENTE + " text, " + CN_SENTIDOCP + " text);";

	public static final String CREATE_TABLE5 = "create table " + TABLE_PANTALLA
			+ " (" + CN_IDPANTALLA + " integer primary key, "
			+ CN_NOMBREPANTALLA + " text);";

	public static final String CREATE_TABLE6 = "create table " + TABLE_CONEXION
			+ " (" + CN_IDCONEXION + " integer primary key, " + CN_TIPOCONEXION
			+ " text, " + CN_CONTADOR + " integer, " + CN_FECHAHORACONEXION
			+ " integer);";

	public static final String CREATE_TABLE7 = "create table " + TABLE_CONFIG
			+ " (" + CN_IDCONFIG + " integer primary key, "
			+ CN_CONTRASENIACONFIG + " text, " + CN_NAMESPACEWS + " text, "
			+ CN_URLWS + " text, " + CN_USUARIOWS + " text, "
			+ CN_CONTRASENIAWS + " text, " + CN_AUTENTIFICACIONWS + " text, "
			+ CN_NOMBREDISPOSITIVO + " text, " + CN_DIVISION + " text, "
			+ CN_LOCAL + " text);";

	public static final String CREATE_TABLE13 = "create table "
			+ TABLE_CONFIGINTRANET + " (" + CN_IDCONFIGINTRANET
			+ " integer primary key, " + CN_CONTRASENIACONFIGINTRANET
			+ " text, " + CN_NAMESPACEWSINTRANET + " text, " + CN_URLWSINTRANET
			+ " text, " + CN_USUARIOWSINTRANET + " text, "
			+ CN_CONTRASENIAWSINTRANET + " text, "
			+ CN_AUTENTIFICACIONWSINTRANET + " text);";

	public static final String CREATE_TABLE8 = "create table " + TABLE_LOG
			+ " (" + CN_IDLOG + " integer primary key autoincrement, "
			+ CN_RUTFUNCIONARIOLOG + " text,"
			+ CN_DESCRIPCION + " text, " + CN_FECHALOG + " text, " + CN_HORALOG
			+ " text);";

	public static final String CREATE_TABLE9 = "create table "
			+ TABLE_SINCRONIZACION + " (" + CN_IDSINCRONIZACION
			+ " integer primary key, " + CN_RUT + " text, " + CN_PLACA
			+ " text, " + CN_IDSYNCGUARDIA + " text, " + CN_IDSYNCPERSONA
			+ " text, " + CN_IDSYNCVEHICULO + " text, " + CN_CONTADORSYNC
			+ " int, " + CN_ESTADOSYNC + " text, " + CN_TIPOSYNC + " text);";

	public static final String CREATE_TABLE21 = "create table "
			+ TABLE_SINCRONIZACIONINTRANET + " (" + CN_IDSINCRONIZACIONINTRANET
			+ " integer primary key, " + CN_RUTINTRANET + " text, "
			+ CN_PLACAINTRANET + " text, " + CN_IDSYNCGUARDIAINTRANET
			+ " text, " + CN_IDSYNCPERSONAINTRANET + " text, "
			+ CN_IDSYNCVEHICULOINTRANET + " text, " + CN_CONTADORSYNCINTRANET
			+ " int, " + CN_ESTADOSYNCINTRANET + " text, "
			+ CN_TIPOSYNCINTRANET + " text);";

	public static final String CREATE_TABLE10 = "create table " + TABLE_GUARIA
			+ " (" + CN_IDGUARDIA + " integer primary key autoincrement, "
			+ CN_UIDGUARDIA + " text, " + CN_ESTADOUIDGUARDIA + " text, "
			+ CN_IDFUNCIONARIOGUARDIA + " text, " + CN_NOMBREGUARDIA
			+ " text, " + CN_APELLIDOGUARDIA + " text, "
			+ CN_NOMBREEMPRESAGUARDIA + " int, " + CN_IDEMPRESAGUARDIA
			+ " text, " + CN_AUTORIZACIONGUARDIA + " text, " + CN_IMAGENGUARDIA
			+ " text);";

	public static final String CREATE_TABLE11 = "create table " + TABLE_IDIOMA
			+ " (" + CN_IDIDIOMA + " integer primary key, " + CN_IDIOMA
			+ " text);";

	public static final String CREATE_TABLE12 = "create table "
			+ TABLE_CONEXIONWSCORRECTA + " (" + CN_IDCONEXIONCORRECTA
			+ " integer primary key, " + CN_ESTADOCONEXIONCORRECTA + " text);";

	public static final String CREATE_TABLE17 = "create table "
			+ TABLE_TIPOCONEXION3GWIFI + " (" + CN_TIPOCONEXION3GWIFI
			+ " integer primary key, " + CN_3GWIFI + " text);";
	public static final String CREATE_TABLE14 = "create table "
			+ TABLE_DATOSGUARDIA + " (" + CN_IDDATOSGUARDIA
			+ " integer primary key autoincrement, " + CN_NOMBREDATOSGUARDIA
			+ " text, " + CN_APELLIDODATOSGUARDIA + " text);";

	public static final String CREATE_TABLE15 = "create table "
			+ TABLE_CONFIGAPP + " (" + CN_IDCONFIGAPP
			+ " integer primary key autoincrement, " + CN_CAMARA + " text, "
			+ CN_NFC + " text, " + CN_LOG + " text, " + CN_SINC + " text);";

	public static final String CREATE_TABLE16 = "create table "
			+ TABLE_CONF_NFC + " (" + CN_IDNFCCAM
			+ " integer primary key autoincrement, " + CN_IDNFCCAM + " text);";

	public static final String CREATE_TABLE18 = "create table " + TABLE_SINCOK
			+ " (" + CN_IDCONFSINC + " integer primary key autoincrement, "
			+ CN_ESTADOCONF + " text);";

	public static final String CREATE_TABLE20 = "create table "
			+ TABLE_CAMBIARFOTO + " (" + CN_IDCAMBIARFOTO
			+ " integer primary key autoincrement, " + CN_COLOR_FOTO
			+ " text);";

	public static final String CREATE_TABLE19 = "create table "
			+ TABLE_CONEXIONVALIDAD + " (" + CN_IDCONEXIONVAL
			+ " integer primary key autoincrement, " + CN_ESTADOOK_INTRANET
			+ " text, " + CN_ESTADOOK_INTERNET + " text);";

	// -----------------------------------
	private DbHelper helper;
	private SQLiteDatabase db;

	// constructor
	public DataBaseManager(Context context) {
		helper = new DbHelper(context);
		db = helper.getWritableDatabase();
	}

	// FUNCION QUE CONTIENE LOS VALORES A INSERTAR A LA TABLA FUNCIONARIO
	private ContentValues ValoresDatosFuncionario(String UID, String EstadoUID,
			String IdFuncionario, String Nombre, String Apellido,
			String NombreEmpresa, String IdEmpresa, String Ost, String CCosto,
			String TipoPase, String Imagen, String Autorizacion,
			String AutorizacionConductor, String FechaConsulta) {
		ContentValues valores = new ContentValues();
		valores.put(CN_UID, UID);
		valores.put(CN_ESTADOUID, EstadoUID);
		valores.put(CN_IDFUNCIONARIO, IdFuncionario);
		valores.put(CN_NOMBRE, Nombre);
		valores.put(CN_APELLIDO, Apellido);
		valores.put(CN_NOMBREEMPRESA, NombreEmpresa);
		valores.put(CN_IDEMPRESA, IdEmpresa);
		valores.put(CN_OSTFUN, Ost);
		valores.put(CN_CCOSTO, CCosto);
		valores.put(CN_TIPOPASEFUN, TipoPase);
		valores.put(CN_IMAGEN, Imagen);
		valores.put(CN_AUTORIZACION, Autorizacion);
		valores.put(CN_AUTORIZACIONCONDUCTOR, AutorizacionConductor);
		valores.put(CN_FECHACONSULTA, FechaConsulta);

		return valores;
	}

	// INSERCION EN TABLA FUNCIONARIO
	public void InsertarDatosFuncionario(String UID, String EstadoUID,
			String IdFuncionario, String Nombre, String Apellido,
			String NombreEmpresa, String IdEmpresa, String Ost, String CCosto,
			String TipoPase, String Imagen, String Autorizacion,
			String AutorizacionConductor, String FechaConsulta) {
		// db.insert(table, nullColumnHack, values); //Explicacion
		db.insert(
				TABLE_FUNCIONARIO,
				null,
				ValoresDatosFuncionario(UID, EstadoUID, IdFuncionario, Nombre,
						Apellido, NombreEmpresa, IdEmpresa, Ost, CCosto,
						TipoPase, Imagen, Autorizacion, AutorizacionConductor,
						FechaConsulta));
	}

	// ELIMINAR DE TABLA FUNCIONARIO, LA COLUMNA CUYO IDFUNCIONARIO CORRESPONDA
	public void EliminarDatosFuncionarioId(String IdFuncionario) {
		db.delete(TABLE_FUNCIONARIO, CN_IDFUNCIONARIO + "=?",
				new String[] { IdFuncionario });
	}

	public void EliminarDatosFuncionarioUID(String UIDFuncionario) {
		db.delete(TABLE_FUNCIONARIO, CN_UID + "=?",
				new String[] { UIDFuncionario });
	}

	// ELIMINAR DE TABLA DATOS PALABRA, LA COLUMNA CUYO NIVEL SEA MENOR O IGUAL
	// AL NIVEL INGRESADO
	// public void EliminarNivelesPalabra (String Nivel){
	// db.delete(TABLE_FUNCIONARIO, CN_NIVEL + "<=?", new String[]{Nivel} );
	// }gbgvggguokkkgyhhypololyohyl

	// RETORNA LOS DATOS DE TABLA (SELECT *) CURSOR DE LA BASE DE DATOS PARA
	// PODER RECORRERLA
	public Cursor CargarCursorFuncionario() {
		String[] Columnas = new String[] { CN_UID, CN_ESTADOUID,
				CN_IDFUNCIONARIO, CN_NOMBRE, CN_APELLIDO, CN_NOMBREEMPRESA,
				CN_IDEMPRESA, CN_OSTFUN, CN_CCOSTO, CN_TIPOPASEFUN, CN_IMAGEN,
				CN_AUTORIZACION, CN_AUTORIZACIONCONDUCTOR, CN_FECHACONSULTA };
		// db.query(TABLE_NAME, columns, selection, selectionArgs, groupBy,
		// having, orderBy) //EJEMPLO DE DATOS
		return db.query(TABLE_FUNCIONARIO, Columnas, null, null, null, null,
				null);
	}

	// RETORNA TODOS LOS DATOS DEL FUNCIONARIO SEGUN EL IDFUNCIONARIO
	public Cursor BuscarFuncionarioId(String IdFuncionario) {
		String[] Columnas = new String[] { CN_UID, CN_ESTADOUID,
				CN_IDFUNCIONARIO, CN_NOMBRE, CN_APELLIDO, CN_NOMBREEMPRESA,
				CN_IDEMPRESA, CN_OSTFUN, CN_CCOSTO, CN_TIPOPASEFUN, CN_IMAGEN,
				CN_AUTORIZACION, CN_AUTORIZACIONCONDUCTOR, CN_FECHACONSULTA };

		return db.query(TABLE_FUNCIONARIO, Columnas, CN_IDFUNCIONARIO + "=?",
				new String[] { IdFuncionario }, null, null, null);
	}

	// RETORNA TODOS LOS DATOS DEL FUNCIONARIO SEGUN EL UID DEL FUNCIONARIO
	public Cursor BuscarFuncionarioUID(String UID) {
		String[] Columnas = new String[] { CN_UID, CN_ESTADOUID,
				CN_IDFUNCIONARIO, CN_NOMBRE, CN_APELLIDO, CN_NOMBREEMPRESA,
				CN_IDEMPRESA, CN_OSTFUN, CN_CCOSTO, CN_TIPOPASEFUN, CN_IMAGEN,
				CN_AUTORIZACION, CN_AUTORIZACIONCONDUCTOR, CN_FECHACONSULTA };

		return db.query(TABLE_FUNCIONARIO, Columnas, CN_UID + "=?",
				new String[] { UID }, null, null, null);
	}

	public void ActualizarAutorizacionFuncionario(String IdFuncionario,
			String EstadoUID, String Autorizacion, String FechaConsulta,
			String AutorizacionConductor) {
		ContentValues valores = new ContentValues();
		valores.put(CN_ESTADOUID, EstadoUID);
		valores.put(CN_AUTORIZACION, Autorizacion);
		valores.put(CN_FECHACONSULTA, FechaConsulta);
		valores.put(CN_AUTORIZACIONCONDUCTOR, AutorizacionConductor);

		// Actualizamos el registro en la base de datos
		db.update(TABLE_FUNCIONARIO, valores, CN_IDFUNCIONARIO + "="
				+ IdFuncionario, null);
	}

	public int devolveridFuncionario(String idFuncionario) {
		Cursor c = db.rawQuery(
				"SELECT * From FUNCIONARIO WHERE IdFuncionario ='"
						+ idFuncionario + "'", null);
		int id = 0;
		if (c.moveToFirst()) {
			do {
				id = c.getInt(0);
			} while (c.moveToNext());
		}
		return id;
	}

	public void ActualizarDataFuncionario(String UID, String EstadoUID,
			String IdFuncionario, String Nombre, String Apellido,
			String NombreEmpresa, String IdEmpresa, String Ost, String CCosto,
			String TipoPase, String Imagen, String Autorizacion,
			String AutorizacionConductor, String FechaConsulta, int id) {
		ContentValues valores = new ContentValues();

		valores.put(CN_UID, UID);
		valores.put(CN_ESTADOUID, EstadoUID);
		valores.put(CN_NOMBRE, Nombre);
		valores.put(CN_APELLIDO, Apellido);
		valores.put(CN_NOMBREEMPRESA, NombreEmpresa);
		valores.put(CN_IDEMPRESA, IdEmpresa);
		valores.put(CN_OSTFUN, Ost);
		valores.put(CN_CCOSTO, CCosto);
		valores.put(CN_TIPOPASEFUN, TipoPase);
		valores.put(CN_IMAGEN, Imagen);
		valores.put(CN_AUTORIZACION, Autorizacion);
		valores.put(CN_AUTORIZACIONCONDUCTOR, AutorizacionConductor);
		valores.put(CN_FECHACONSULTA, FechaConsulta);

		// Actualizamos el registro en la base de datos
		db.update(TABLE_FUNCIONARIO, valores, CN_ID + "=" + id, null);
	}

	public void ActualizarDataFuncionarioPORID(String UID, String EstadoUID,
			int id, String Nombre, String Apellido, String NombreEmpresa,
			String IdEmpresa, String Ost, String CCosto, String TipoPase,
			String Imagen, String Autorizacion, String AutorizacionConductor,
			String FechaConsulta) {
		ContentValues valores = new ContentValues();

		valores.put(CN_UID, UID);
		valores.put(CN_ESTADOUID, EstadoUID);
		valores.put(CN_NOMBRE, Nombre);
		valores.put(CN_APELLIDO, Apellido);
		valores.put(CN_NOMBREEMPRESA, NombreEmpresa);
		valores.put(CN_IDEMPRESA, IdEmpresa);
		valores.put(CN_OSTFUN, Ost);
		valores.put(CN_CCOSTO, CCosto);
		valores.put(CN_TIPOPASEFUN, TipoPase);
		valores.put(CN_IMAGEN, Imagen);
		valores.put(CN_AUTORIZACION, Autorizacion);
		valores.put(CN_AUTORIZACIONCONDUCTOR, AutorizacionConductor);
		valores.put(CN_FECHACONSULTA, FechaConsulta);

		// Actualizamos el registro en la base de datos
		db.update(TABLE_FUNCIONARIO, valores, CN_ID + "=" + id, null);
	}

	public Cursor TraerImagenporRut(String rut) {

		Cursor c = db.rawQuery(
				"select Imagen from FUNCIONARIO where  IdFuncionario= '" + rut
						+ "'", null);
		return c;
	}

	public int traerIDFuncionario(String rut) {
		Cursor c = db.rawQuery(
				"SELECT _id FROM FUNCIONARIO WHERE IdFuncionario = '" + rut
						+ "'", null);
		int funcionario = 0;
		if (c.moveToFirst()) {
			do {
				funcionario = c.getInt(0);
			} while (c.moveToNext());
		}
		return funcionario;
	}

	public void ActualizarFechaConsultaFuncionario(String IdFuncionario,
			String FechaConsulta) {
		ContentValues valores = new ContentValues();
		valores.put(CN_FECHACONSULTA, FechaConsulta);

		// Actualizamos el registro en la base de datos
		db.update(TABLE_FUNCIONARIO, valores, CN_IDFUNCIONARIO + "="
				+ IdFuncionario, null);
	}

	// FUNCION QUE CONTIENE LOS VALORES A INSERTAR A LA TABLA VEHICULO
	private ContentValues ValoresDatosVehiculo(String UID, String EstadoUID,
			String IdVehiculo, String Marca, String Modelo, String Anio,
			String TipoVehiculo, String NombreEmpresa, String IdEmpresa,
			String ImagenVehiculo, String AutorizacionVehiculo,
			String FechaConsulta, String Mensaje) {
		ContentValues valores = new ContentValues();
		valores.put(CN_UIDVEHICULO, UID);
		valores.put(CN_ESTADOUIDVEHICULO, EstadoUID);
		valores.put(CN_IDVEHICULO, IdVehiculo);
		valores.put(CN_MARCA, Marca);
		valores.put(CN_MODELO, Modelo);
		valores.put(CN_ANIO, Anio);
		valores.put(CN_TIPOVEHICULO, TipoVehiculo);
		valores.put(CN_NOMBREEMPRESAVEHICULO, NombreEmpresa);
		valores.put(CN_IDEMPRESAVEHICULO, IdEmpresa);
		valores.put(CN_IMAGENVEHICULO, ImagenVehiculo);
		valores.put(CN_AUTORIZACIONVEHICULO, AutorizacionVehiculo);
		valores.put(CN_FECHACONSULTAVEHICULO, FechaConsulta);
		valores.put(CN_MENSAJEVEHICULO, Mensaje);

		return valores;
	}

	// INSERCION EN TABLA VEHICULO
	public void InsertarDatosVehiculo(String UID, String EstadoUID,
			String IdVehiculo, String Marca, String Modelo, String Anio,
			String TipoVehiculo, String NombreEmpresa, String IdEmpresa,
			String ImagenVehiculo, String AutorizacionVehiculo,
			String FechaConsulta, String Mensaje) {
		// db.insert(table, nullColumnHack, values); //Explicacion
		db.insert(
				TABLE_VEHICULO,
				null,
				ValoresDatosVehiculo(UID, EstadoUID, IdVehiculo, Marca, Modelo,
						Anio, TipoVehiculo, NombreEmpresa, IdEmpresa,
						ImagenVehiculo, AutorizacionVehiculo, FechaConsulta,
						Mensaje));
	}

	// RETORNA TODOS LOS DATOS DEL VEHICULO SEGUN SEA SU ID
	public Cursor BuscarVehiculoId(String IdVehiculo) {
		String[] Columnas = new String[] { CN_ID2, CN_UIDVEHICULO,
				CN_ESTADOUIDVEHICULO, CN_IDVEHICULO, CN_MARCA, CN_MODELO,
				CN_ANIO, CN_TIPOVEHICULO, CN_NOMBREEMPRESAVEHICULO,
				CN_IDEMPRESAVEHICULO, CN_IMAGENVEHICULO,
				CN_AUTORIZACIONVEHICULO, CN_FECHACONSULTAVEHICULO,
				CN_MENSAJEVEHICULO };

		return db.query(TABLE_VEHICULO, Columnas, CN_IDVEHICULO + "=?",
				new String[] { IdVehiculo }, null, null, null);
	}

	// RETORNA TODOS LOS DATOS DEL VEHICULO SEGUN SEA SU ID
	public Cursor BuscarVehiculoUID(String UIDVehiculo) {
		String[] Columnas = new String[] { CN_ID2, CN_UIDVEHICULO,
				CN_ESTADOUIDVEHICULO, CN_IDVEHICULO, CN_MARCA, CN_MODELO,
				CN_ANIO, CN_TIPOVEHICULO, CN_NOMBREEMPRESAVEHICULO,
				CN_IDEMPRESAVEHICULO, CN_IMAGENVEHICULO,
				CN_AUTORIZACIONVEHICULO, CN_FECHACONSULTAVEHICULO,
				CN_MENSAJEVEHICULO };

		return db.query(TABLE_VEHICULO, Columnas, CN_UIDVEHICULO + "=?",
				new String[] { UIDVehiculo }, null, null, null);
	}

	public void ActualizarAutorizacionVehiculo(String IdVehiculo,
			String EstadoUID, String Autorizacion, String FechaConsulta,
			String Mensaje) {
		ContentValues valores = new ContentValues();
		valores.put(CN_ESTADOUIDVEHICULO, EstadoUID);
		valores.put(CN_AUTORIZACIONVEHICULO, Autorizacion);
		valores.put(CN_FECHACONSULTAVEHICULO, FechaConsulta);
		valores.put(CN_MENSAJEVEHICULO, Mensaje);

		// Actualizamos el registro en la base de datos
		db.update(TABLE_VEHICULO, valores, CN_ID2 + "=" + IdVehiculo, null);

		// db.execSQL("UPDATE "+TABLE_VEHICULO
		// +" set "+CN_AUTORIZACIONVEHICULO+" = \""+FechaConsulta.toString()
		// +"\" where _id = "+ IdVehiculo +";");
	}

	public void ActualizarDataVehiculo(int IdentificadorVehiculo, String UID,
			String EstadoUID, String IdVehiculo, String Marca, String Modelo,
			String Anio, String TipoVehiculo, String NombreEmpresa,
			String IdEmpresa, String ImagenVehiculo,
			String AutorizacionVehiculo, String FechaConsulta, String Mensaje) {
		ContentValues valores = new ContentValues();

		valores.put(CN_UIDVEHICULO, UID);
		valores.put(CN_ESTADOUIDVEHICULO, EstadoUID);
		valores.put(CN_MARCA, Marca);
		valores.put(CN_MODELO, Modelo);
		valores.put(CN_ANIO, Anio);
		valores.put(CN_TIPOVEHICULO, TipoVehiculo);
		valores.put(CN_NOMBREEMPRESAVEHICULO, NombreEmpresa);
		valores.put(CN_IDEMPRESAVEHICULO, IdEmpresa);
		valores.put(CN_IMAGENVEHICULO, ImagenVehiculo);
		valores.put(CN_AUTORIZACIONVEHICULO, AutorizacionVehiculo);
		valores.put(CN_FECHACONSULTAVEHICULO, FechaConsulta);
		valores.put(CN_MENSAJEVEHICULO, Mensaje);

		db.update(TABLE_VEHICULO, valores,
				CN_ID2 + "=" + IdentificadorVehiculo, null);
	}

	public void ActualizarFechaConsultaVehiculo(String IdVehiculo,
			String FechaConsulta) {
		ContentValues valores = new ContentValues();
		valores.put(CN_FECHACONSULTAVEHICULO, FechaConsulta);

		// Actualizamos el registro en la base de datos
		db.update(TABLE_VEHICULO, valores, CN_ID2 + "=" + IdVehiculo, null);
		// db.execSQL("UPDATE "+TABLE_VEHICULO
		// +" set "+CN_FECHACONSULTAVEHICULO+" = \""+FechaConsulta.toString()
		// +"\" where _id = "+ IdVehiculo +";");
	}

	// ELIMINAR DE TABLA VEHICULO, LA COLUMNA CUYO IDVEHICULO CORRESPONDA
	public void EliminarDatosVehiculoId(String IdVehiculo) {
		db.delete(TABLE_VEHICULO, CN_IDVEHICULO + "=?",
				new String[] { IdVehiculo });
	}

	// ELIMINAR DE TABLA VEHICULO, LA COLUMNA CUYO IDVEHICULO CORRESPONDA
	public void EliminarDatosVehiculoUID(String UIDVehiculo) {
		db.delete(TABLE_VEHICULO, CN_UIDVEHICULO + "=?",
				new String[] { UIDVehiculo });
	}

	// FUNCION QUE CONTIENE LOS VALORES A INSERTAR A LA TABLA CONTROL DE ACCESO
	private ContentValues ValoresDatosControlAcceso(String UID,
			String IdFuncionario, String IdEmpresa, String Conductor,
			String Transporte, String CodigoError, String IdLocal,
			String Sentido, String CentroCosto, String OST, String TipoPase,
			String EstadoConexion, String FechaControl, String HoraControl,
			String Sincronizacion) {
		ContentValues valores = new ContentValues();
		valores.put(CN_UIDCONTROL, UID);
		valores.put(CN_IDFUNCIONARIOCONTROL, IdFuncionario);
		valores.put(CN_IDEMPRESACONTROL, IdEmpresa);
		valores.put(CN_CONDUCTOR, Conductor);
		valores.put(CN_TRANSPORTE, Transporte);
		valores.put(CN_CODIGOERROR, CodigoError);
		valores.put(CN_IDLOCAL, IdLocal);
		valores.put(CN_SENTIDO, Sentido);
		valores.put(CN_CENTROCOSTO, CentroCosto);
		valores.put(CN_OST, OST);
		valores.put(CN_TIPOPASE, TipoPase);
		valores.put(CN_ESTADOCONEXION, EstadoConexion);
		valores.put(CN_FECHACONTROL, FechaControl);
		valores.put(CN_HORACONTROL, HoraControl);
		valores.put(CN_SINCRONIZACION, Sincronizacion);

		return valores;
	}

	// INSERCION EN TABLA CONTROL DE ACCESO
	public void InsertarDatosControlAcceso(String UID, String IdFuncionario,
			String IdEmpresa, String Conductor, String Transporte,
			String CodigoError, String IdLocal, String Sentido,
			String CentroCosto, String OST, String TipoPase,
			String EstadoConexion, String FechaControl, String HoraControl,
			String Sincronizacion) {
		// db.insert(table, nullColumnHack, values); //Explicacion
		db.insert(
				TABLE_CONTROLACCESO,
				null,
				ValoresDatosControlAcceso(UID, IdFuncionario, IdEmpresa,
						Conductor, Transporte, CodigoError, IdLocal, Sentido,
						CentroCosto, OST, TipoPase, EstadoConexion,
						FechaControl, HoraControl, Sincronizacion));
	}

	public Cursor CargarCursorControlAccesoLocal() {
		String[] Columnas = new String[] { CN_IDFUNCIONARIOCONTROL,
				CN_TRANSPORTE, CN_CODIGOERROR, CN_FECHACONTROL, CN_HORACONTROL };
		// db.query(TABLE_NAME, columns, selection, selectionArgs, groupBy,
		// having, orderBy) //EJEMPLO DE DATOS
		return db.query(TABLE_CONTROLACCESO, Columnas, null, null, null, null,
				null);
	}

	public Cursor CursorSincronizacionControlAccesoLocal() {
		String[] Columnas = new String[] { CN_IDCONTROL, CN_UIDCONTROL,
				CN_IDFUNCIONARIOCONTROL, CN_IDEMPRESACONTROL, CN_CONDUCTOR,
				CN_TRANSPORTE, CN_CODIGOERROR, CN_IDLOCAL, CN_SENTIDO,
				CN_CENTROCOSTO, CN_OST, CN_TIPOPASE, CN_ESTADOCONEXION,
				CN_FECHACONTROL, CN_HORACONTROL, CN_SINCRONIZACION };
		// db.query(TABLE_NAME, columns, selection, selectionArgs, groupBy,
		// having, orderBy) //EJEMPLO DE DATOS
		return db.query(TABLE_CONTROLACCESO, Columnas, null, null, null, null,
				null);
	}

	public Cursor CursorSincronizacionControlAccesoLocal2() {
		String[] Columnas = new String[] { CN_IDCONTROL,
				CN_IDFUNCIONARIOCONTROL, CN_IDEMPRESACONTROL, CN_CONDUCTOR,
				CN_TRANSPORTE, CN_CODIGOERROR, CN_IDLOCAL, CN_SENTIDO,
				CN_CENTROCOSTO, CN_OST, CN_TIPOPASE, CN_FECHACONTROL,
				CN_HORACONTROL };
		// db.query(TABLE_NAME, columns, selection, selectionArgs, groupBy,
		// having, orderBy) //EJEMPLO DE DATOS
		return db.query(TABLE_CONTROLACCESO, Columnas, null, null, null, null,
				null);
	}

	public Cursor CursorConsultarControlAcceso(String IdFuncionario,
			String Sentido, String CodigoError, String FechaControl,
			String HoraControl, String IdLocal) {

		Cursor c = db.rawQuery(
				"SELECT * FROM CONTROLACCESO WHERE IdFuncionario= '"
						+ IdFuncionario + "' AND Sentido = '" + Sentido
						+ "' AND CodigoError='" + CodigoError
						+ "' AND FechaControl= '" + FechaControl
						+ "' AND HoraControl='" + HoraControl
						+ "' AND IdLocal='" + IdLocal + "'", null);

		return c;

	}

	public void EliminarDatosControlAccesoLocal(String IdControl) {
		db.delete(TABLE_CONTROLACCESO, CN_IDCONTROL + "=?",
				new String[] { IdControl });
	}

	// FUNCION QUE CONTIENE LOS VALORES A INSERTAR A LA TABLA CHOFERPASAJERO
	private ContentValues ValoresDatosChoferPasajero(String Vehiculo,
			String Patente, String Sentido) {
		ContentValues valores = new ContentValues();
		valores.put(CN_VEHICULO, Vehiculo);
		valores.put(CN_PATENTE, Patente);
		valores.put(CN_SENTIDOCP, Sentido);
		return valores;
	}

	// INSERCION EN TABLA CHOFERPASAJERO
	public void InsertarDatosChoferPasajero(String Vehiculo, String Patente,
			String Sentido) {
		// db.insert(table, nullColumnHack, values); //Explicacion
		db.insert(TABLE_CHOFERPASAJERO, null,
				ValoresDatosChoferPasajero(Vehiculo, Patente, Sentido));
	}

	// ELIMINAR DE TABLA CHOFERPASAJERO, LA COLUMNA CUYO VEHICULO CORRESPONDA 1
	public void EliminarDatosChoferPasajero() {
		db.delete(TABLE_CHOFERPASAJERO, CN_VEHICULO + "=?",
				new String[] { "1" });
	}

	public Cursor BuscarEstadoChoferPasajero() {
		String[] Columnas = new String[] { CN_VEHICULO, CN_PATENTE,
				CN_SENTIDOCP };

		return db.query(TABLE_CHOFERPASAJERO, Columnas, CN_VEHICULO + "=?",
				new String[] { "1" }, null, null, null);
	}

	// FUNCION QUE CONTIENE LOS VALORES A INSERTAR A LA TABLA PANTALLA
	private ContentValues ValoresPantalla(int Id, String NombrePantalla) {
		ContentValues valores = new ContentValues();
		valores.put(CN_NOMBREPANTALLA, NombrePantalla);
		valores.put(CN_IDPANTALLA, Id);

		return valores;
	}

	// INSERCION EN TABLA PANTALLA
	public void InsertarDatosPantalla(String NombrePantalla) {
		db.insert(TABLE_PANTALLA, null, ValoresPantalla(1, NombrePantalla));
	}

	// ELIMINAR DE TABLA PANTALLA
	public void EliminarDatosPantalla() {
		db.delete(TABLE_CHOFERPASAJERO, CN_IDPANTALLA + "=?",
				new String[] { "1" });
	}

	public Cursor BuscarPantalla() {
		String[] Columnas = new String[] { CN_NOMBREPANTALLA };

		return db.query(TABLE_PANTALLA, Columnas, CN_IDPANTALLA + "=?",
				new String[] { "1" }, null, null, null);
	}

	public void ActualizarPantalla(String NombrePantalla) {
		ContentValues valores = new ContentValues();
		valores.put(CN_NOMBREPANTALLA, NombrePantalla);
		db.update(TABLE_PANTALLA, valores, CN_IDPANTALLA + "=" + "1", null);
	}

	// FUNCION QUE CONTIENE LOS VALORES A INSERTAR A LA TABLA CONEXION
	private ContentValues ValoresConexion(int Id, String TipoConexion,
			int Contador, String FechaHoraConexion) {
		ContentValues valores = new ContentValues();
		valores.put(CN_IDCONEXION, Id);
		valores.put(CN_TIPOCONEXION, TipoConexion);
		valores.put(CN_CONTADOR, Contador);
		valores.put(CN_FECHAHORACONEXION, FechaHoraConexion);
		return valores;
	}

	// INSERCION EN TABLA CONEXION
	public void InsertarDatosConexion(String FechaHoraConexion) {
		db.insert(TABLE_CONEXION, null,
				ValoresConexion(1, "Movil", 0, FechaHoraConexion));
	}

	public Cursor CursorConexion() {
		String[] Columnas = new String[] { CN_TIPOCONEXION, CN_CONTADOR,
				CN_FECHAHORACONEXION };
		return db.query(TABLE_CONEXION, Columnas, CN_IDCONEXION + "=?",
				new String[] { "1" }, null, null, null);
	}

	public void ActualizarConexion(String TipoConexion, int Contador,
			String FechaHoraConexion) {
		ContentValues valores = new ContentValues();
		valores.put(CN_TIPOCONEXION, TipoConexion);
		valores.put(CN_CONTADOR, Contador);
		valores.put(CN_FECHAHORACONEXION, FechaHoraConexion);

		db.update(TABLE_CONEXION, valores, CN_IDCONEXION + "=" + "1", null);
	}

	// FUNCION QUE CONTIENE LOS VALORES A INSERTAR A LA TABLA CONFIG
	private ContentValues ValoresConfig(int Id, String ContraseniaConfig,
			String NameSpaceWs, String UrlWs, String UsuarioWs,
			String ContraseniaWs, String AutentificacionWs) {
		ContentValues valores = new ContentValues();
		valores.put(CN_IDCONFIG, Id);
		valores.put(CN_CONTRASENIACONFIG, ContraseniaConfig);
		valores.put(CN_NAMESPACEWS, NameSpaceWs);
		valores.put(CN_URLWS, UrlWs);
		valores.put(CN_USUARIOWS, UsuarioWs);
		valores.put(CN_CONTRASENIAWS, ContraseniaWs);
		valores.put(CN_AUTENTIFICACIONWS, AutentificacionWs);
		valores.put(CN_NOMBREDISPOSITIVO, "Sin Nombre");
		return valores;
	}

	private ContentValues ValoresConfig2(int Id, String NombreDispositivo,
			String Division, String Local) {
		ContentValues valores = new ContentValues();
		valores.put(CN_IDCONFIG, Id);
		valores.put(CN_NOMBREDISPOSITIVO, NombreDispositivo);
		valores.put(CN_DIVISION, Division);
		valores.put(CN_LOCAL, Local);

		return valores;
	}

	// INSERCION EN TABLA CONFIG
	public void InsertarDatosConfig(String ContraseniaConfig,
			String NameSpaceWs, String UrlWs, String UsuarioWs,
			String ContraseniaWs, String AutentificacionWs) {
		db.insert(
				TABLE_CONFIG,
				null,
				ValoresConfig(1, ContraseniaConfig, NameSpaceWs, UrlWs,
						UsuarioWs, ContraseniaWs, AutentificacionWs));
	}

	public void InsertarDatosConfig2(String NombreDispositivo, String Division,
			String Local) {
		db.insert(TABLE_CONFIG, null,
				ValoresConfig2(1, NombreDispositivo, Division, Local));
	}

	public Cursor CursorConfig() {
		String[] Columnas = new String[] { CN_CONTRASENIACONFIG,
				CN_NAMESPACEWS, CN_URLWS, CN_USUARIOWS, CN_CONTRASENIAWS,
				CN_AUTENTIFICACIONWS, CN_NOMBREDISPOSITIVO, CN_DIVISION,
				CN_LOCAL };
		return db.query(TABLE_CONFIG, Columnas, CN_IDCONFIG + "=?",
				new String[] { "1" }, null, null, null);
	}

	private ContentValues ValoresConfigIntranet(int Id,
			String ContraseniaConfig, String NameSpaceWs, String UrlWs,
			String UsuarioWs, String ContraseniaWs, String AutentificacionWs) {
		ContentValues valores = new ContentValues();
		valores.put(CN_IDCONFIGINTRANET, Id);
		valores.put(CN_CONTRASENIACONFIGINTRANET, ContraseniaConfig);
		valores.put(CN_NAMESPACEWSINTRANET, NameSpaceWs);
		valores.put(CN_URLWSINTRANET, UrlWs);
		valores.put(CN_USUARIOWSINTRANET, UsuarioWs);
		valores.put(CN_CONTRASENIAWSINTRANET, ContraseniaWs);
		valores.put(CN_AUTENTIFICACIONWSINTRANET, AutentificacionWs);

		return valores;
	}

	public void InsertarDatosConfigIntranet(String ContraseniaConfig,
			String NameSpaceWs, String UrlWs, String UsuarioWs,
			String ContraseniaWs, String AutentificacionWs) {
		db.insert(
				TABLE_CONFIGINTRANET,
				null,
				ValoresConfigIntranet(1, ContraseniaConfig, NameSpaceWs, UrlWs,
						UsuarioWs, ContraseniaWs, AutentificacionWs));
	}

	public Cursor CursorConfigIntranet() {
		String[] Columnas = new String[] { CN_CONTRASENIACONFIGINTRANET,
				CN_NAMESPACEWSINTRANET, CN_URLWSINTRANET, CN_USUARIOWSINTRANET,
				CN_CONTRASENIAWSINTRANET, CN_AUTENTIFICACIONWS };
		return db.query(TABLE_CONFIGINTRANET, Columnas, CN_IDCONFIGINTRANET
				+ "=?", new String[] { "1" }, null, null, null);
	}

	public void ActualizarConfigWsIntranet(String NameSpaceWs, String UrlWs,
			String UsuarioWs, String ContraseniaWs, String AutentificacionWs) {
		ContentValues valores = new ContentValues();
		valores.put(CN_NAMESPACEWSINTRANET, NameSpaceWs);
		valores.put(CN_URLWSINTRANET, UrlWs);
		valores.put(CN_USUARIOWSINTRANET, UsuarioWs);
		valores.put(CN_CONTRASENIAWSINTRANET, ContraseniaWs);
		valores.put(CN_AUTENTIFICACIONWSINTRANET, AutentificacionWs);

		db.update(TABLE_CONFIGINTRANET, valores, CN_IDCONFIGINTRANET + "="
				+ "1", null);
	}

	public void ActualizarConfigWs(String NameSpaceWs, String UrlWs,
			String UsuarioWs, String ContraseniaWs, String AutentificacionWs) {
		ContentValues valores = new ContentValues();
		valores.put(CN_NAMESPACEWS, NameSpaceWs);
		valores.put(CN_URLWS, UrlWs);
		valores.put(CN_USUARIOWS, UsuarioWs);
		valores.put(CN_CONTRASENIAWS, ContraseniaWs);
		valores.put(CN_AUTENTIFICACIONWS, AutentificacionWs);

		db.update(TABLE_CONFIG, valores, CN_IDCONFIG + "=" + "1", null);
	}

	public void ActualizarConfigInfoDispositivo(String NombreDispositivo,
			String Division, String Local) {
		ContentValues valores = new ContentValues();
		valores.put(CN_NOMBREDISPOSITIVO, NombreDispositivo);
		valores.put(CN_DIVISION, Division);
		valores.put(CN_LOCAL, Local);

		db.update(TABLE_CONFIG, valores, CN_IDCONFIG + "=" + "1", null);
	}

	// FUNCION QUE CONTIENE LOS VALORES A INSERTAR A LA TABLA LOG
	private ContentValues ValoresLog(String rut, String Descripcion,
			String Fecha, String Hora) {
		ContentValues valores = new ContentValues();
		valores.put(CN_RUTFUNCIONARIOLOG, rut);
		valores.put(CN_DESCRIPCION, Descripcion);
		valores.put(CN_FECHALOG, Fecha);
		valores.put(CN_HORALOG, Hora);
		return valores;
	}

	// INSERCION EN TABLA LOG
	public void InsertarDatosLog(String rut, String Descripcion, String Fecha,
			String Hora) {
		db.insert(TABLE_LOG, null, ValoresLog(rut, Descripcion, Fecha, Hora));
	}

	public Cursor CursorLog() {
		String[] Columnas = new String[] { CN_IDLOG, CN_RUTFUNCIONARIOLOG,
				CN_DESCRIPCION, CN_FECHALOG, CN_HORALOG };
		return db.query(TABLE_LOG, Columnas, null, null, null, null, null);
	}

	public Cursor CursorLogOrderDESC() {
		String[] Columnas = new String[] { CN_IDLOG, CN_RUTFUNCIONARIOLOG,
				CN_DESCRIPCION, CN_FECHALOG, CN_HORALOG };
		return db.query(TABLE_LOG, Columnas, null, null, null, null, CN_HORALOG
				+ " DESC");
	}

	public void EliminarDatosLog() {

		db.rawQuery("delete from LOG where Fecha < date('now')", null);

		// db.delete(TABLE_LOG, CN_IDLOG + "=?", new String[] { IdLog });
	}

	public void EliminarDatosLog2(String fecha) {

		db.delete(TABLE_LOG, CN_FECHALOG + "<?", new String[] { fecha });
	}

	// FUNCION QUE CONTIENE LOS VALORES A INSERTAR A LA TABLA SINCRONIZACION
	private ContentValues ValoresSincronizacion(String Id, String Rut,
			String Placa, String IdSyncGuardia, String IdSyncPersona,
			String IdSyncVehiculo, int Contador, String EstadoSync,
			String TipoSync) {
		ContentValues valores = new ContentValues();
		valores.put(CN_IDSINCRONIZACION, Id);
		valores.put(CN_RUT, Rut);
		valores.put(CN_PLACA, Placa);
		valores.put(CN_IDSYNCGUARDIA, IdSyncGuardia);
		valores.put(CN_IDSYNCPERSONA, IdSyncPersona);
		valores.put(CN_IDSYNCVEHICULO, IdSyncVehiculo);
		valores.put(CN_CONTADORSYNC, Contador);
		valores.put(CN_ESTADOSYNC, EstadoSync);
		valores.put(CN_TIPOSYNC, TipoSync);
		return valores;
	}

	// INSERCION EN TABLA SINCRONIZACION
	public void InsertarDatosSincronizacion(String Rut, String Placa,
			String IdSyncGuardia, String IdSyncPersona, String IdSyncVehiculo,
			int Contador, String EstadoSync, String TipoSync) {
		db.insert(
				TABLE_SINCRONIZACION,
				null,
				ValoresSincronizacion("1", Rut, Placa, IdSyncGuardia,
						IdSyncPersona, IdSyncVehiculo, Contador, EstadoSync,
						TipoSync));
	}

	public Cursor CursorSincronizacionGuardia() {
		String[] Columnas = new String[] { CN_IDSYNCGUARDIA, CN_CONTADORSYNC,
				CN_ESTADOSYNC, CN_TIPOSYNC };
		return db.query(TABLE_SINCRONIZACION, Columnas, CN_IDSINCRONIZACION
				+ "=?", new String[] { "1" }, null, null, null);
	}

	public Cursor CursorSincronizacionPersona() {
		String[] Columnas = new String[] { CN_RUT, CN_IDSYNCPERSONA,
				CN_CONTADORSYNC, CN_ESTADOSYNC, CN_TIPOSYNC };
		return db.query(TABLE_SINCRONIZACION, Columnas, CN_IDSINCRONIZACION
				+ "=?", new String[] { "1" }, null, null, null);
	}

	public Cursor CursorSincronizacionVehiculo() {
		String[] Columnas = new String[] { CN_PLACA, CN_IDSYNCVEHICULO,
				CN_CONTADORSYNC, CN_ESTADOSYNC, CN_TIPOSYNC };
		return db.query(TABLE_SINCRONIZACION, Columnas, CN_IDSINCRONIZACION
				+ "=?", new String[] { "1" }, null, null, null);
	}

	public void ActualizarSincronizacionPersona(String Rut, String IdSync,
			int Contador, String EstadoSync, String TipoSync) {
		ContentValues valores = new ContentValues();
		valores.put(CN_RUT, Rut);
		valores.put(CN_IDSYNCPERSONA, IdSync);
		valores.put(CN_CONTADORSYNC, Contador);
		valores.put(CN_ESTADOSYNC, EstadoSync);
		valores.put(CN_TIPOSYNC, TipoSync);

		db.update(TABLE_SINCRONIZACION, valores, CN_IDSINCRONIZACION + "="
				+ "1", null);
	}

	public void ActualizarSincronizacionVehiculo(String Placa, String IdSync,
			int Contador, String EstadoSync, String TipoSync) {
		ContentValues valores = new ContentValues();
		valores.put(CN_PLACA, Placa);
		valores.put(CN_IDSYNCVEHICULO, IdSync);
		valores.put(CN_CONTADORSYNC, Contador);
		valores.put(CN_ESTADOSYNC, EstadoSync);
		valores.put(CN_TIPOSYNC, TipoSync);

		db.update(TABLE_SINCRONIZACION, valores, CN_IDSINCRONIZACION + "="
				+ "1", null);
	}

	public void ActualizarSincronizacionGuardia(String IdSync, int Contador,
			String EstadoSync, String TipoSync) {
		ContentValues valores = new ContentValues();
		valores.put(CN_IDSYNCGUARDIA, IdSync);
		valores.put(CN_CONTADORSYNC, Contador);
		valores.put(CN_ESTADOSYNC, EstadoSync);
		valores.put(CN_TIPOSYNC, TipoSync);

		db.update(TABLE_SINCRONIZACION, valores, CN_IDSINCRONIZACION + "="
				+ "1", null);
	}

	public void ActualizarSincronizacionVehiculoFin(String EstadoSync,
			String TipoSync) {
		ContentValues valores = new ContentValues();
		valores.put(CN_ESTADOSYNC, EstadoSync);
		valores.put(CN_TIPOSYNC, TipoSync);

		db.update(TABLE_SINCRONIZACION, valores, CN_IDSINCRONIZACION + "="
				+ "1", null);
	}

	public void ActualizarContadorSincronizacion(int Contador,
			String EstadoSync, String TipoSync) {
		ContentValues valores = new ContentValues();
		valores.put(CN_CONTADORSYNC, Contador);
		valores.put(CN_ESTADOSYNC, EstadoSync);
		valores.put(CN_TIPOSYNC, TipoSync);

		db.update(TABLE_SINCRONIZACION, valores, CN_IDSINCRONIZACION + "="
				+ "1", null);
	}

	private ContentValues ValoresGuardia(String UID, String EstadoUID,
			String IdFuncionario, String Nombre, String Apellido,
			String NombreEmpresa, String IdEmpresa, String Autorizacion,
			String Imagen) {
		ContentValues valores = new ContentValues();
		valores.put(CN_UIDGUARDIA, UID);
		valores.put(CN_ESTADOUIDGUARDIA, EstadoUID);
		valores.put(CN_IDFUNCIONARIOGUARDIA, IdFuncionario);
		valores.put(CN_NOMBREGUARDIA, Nombre);
		valores.put(CN_APELLIDOGUARDIA, Apellido);
		valores.put(CN_NOMBREEMPRESAGUARDIA, NombreEmpresa);
		valores.put(CN_IDEMPRESAGUARDIA, IdEmpresa);
		valores.put(CN_AUTORIZACIONGUARDIA, Autorizacion);
		valores.put(CN_IMAGENGUARDIA, Imagen);
		return valores;
	}

	public Cursor BuscarGuardiaId(String IdFuncionario) {
		String[] Columnas = new String[] { CN_UIDGUARDIA, CN_ESTADOUIDGUARDIA,
				CN_IDFUNCIONARIOGUARDIA, CN_NOMBREGUARDIA, CN_APELLIDOGUARDIA,
				CN_NOMBREEMPRESAGUARDIA, CN_IDEMPRESAGUARDIA,
				CN_AUTORIZACIONGUARDIA, CN_IMAGENGUARDIA };

		return db.query(TABLE_GUARIA, Columnas, CN_IDFUNCIONARIOGUARDIA + "=?",
				new String[] { IdFuncionario }, null, null, null);
	}

	public Cursor BuscarGuardiaUID(String UIDFuncionario) {
		String[] Columnas = new String[] { CN_UIDGUARDIA, CN_ESTADOUIDGUARDIA,
				CN_IDFUNCIONARIOGUARDIA, CN_NOMBREGUARDIA, CN_APELLIDOGUARDIA,
				CN_NOMBREEMPRESAGUARDIA, CN_IDEMPRESAGUARDIA,
				CN_AUTORIZACIONGUARDIA, CN_IMAGENGUARDIA };

		return db.query(TABLE_GUARIA, Columnas, CN_UIDGUARDIA + "=?",
				new String[] { UIDFuncionario }, null, null, null);
	}

	public Cursor VerificarSync() {
		String[] Columnas = new String[] { CN_UIDGUARDIA, CN_ESTADOUIDGUARDIA,
				CN_IDFUNCIONARIOGUARDIA, CN_NOMBREGUARDIA, CN_APELLIDOGUARDIA,
				CN_NOMBREEMPRESAGUARDIA, CN_IDEMPRESAGUARDIA,
				CN_AUTORIZACIONGUARDIA, CN_IMAGENGUARDIA };

		return db.query(TABLE_GUARIA, Columnas, null, null, null, null, null);
	}

	public void InsertarDatosGuardia(String UID, String EstadoUID,
			String IdFuncionario, String Nombre, String Apellido,
			String NombreEmpresa, String IdEmpresa, String Autorizacion,
			String Imagen) {
		db.insert(
				TABLE_GUARIA,
				null,
				ValoresGuardia(UID, EstadoUID, IdFuncionario, Nombre, Apellido,
						NombreEmpresa, IdEmpresa, Autorizacion, Imagen));
	}

	public int devolveridGuardia(String idFuncionario) {
		Cursor c = db.rawQuery("SELECT * From GUARDIA WHERE IdFuncionario ='"
				+ idFuncionario + "'", null);
		int id = 0;
		if (c.moveToFirst()) {
			do {
				id = c.getInt(0);
			} while (c.moveToNext());
		}
		return id;
	}

	public void ActualizarDataGuardia(String UID, String EstadoUID,
			String IdFuncionario, String Nombre, String Apellido,
			String NombreEmpresa, String IdEmpresa, String Autorizacion,
			String Imagen, int id) {
		// Actualizamos el registro en la base de datos
		db.update(
				TABLE_GUARIA,
				ValoresGuardia(UID, EstadoUID, IdFuncionario, Nombre, Apellido,
						NombreEmpresa, IdEmpresa, Autorizacion, Imagen),
				CN_IDGUARDIA + "=" + id, null);
	}

	public void ActualizarAutorizacionGuardiaId(String IdFuncionario,
			String EstadoUID, String Autorizacion) {
		ContentValues valores = new ContentValues();
		valores.put(CN_ESTADOUIDGUARDIA, EstadoUID);
		valores.put(CN_AUTORIZACIONGUARDIA, Autorizacion);
		// Actualizamos el registro en la base de datos
		db.update(TABLE_GUARIA, valores, CN_IDFUNCIONARIOGUARDIA + "="
				+ IdFuncionario, null);
	}

	public Cursor CursorIdGuradiaUID(String UIDFuncionario) {
		String[] Columnas = new String[] { CN_IDGUARDIA };

		return db.query(TABLE_GUARIA, Columnas, CN_UIDGUARDIA + "=?",
				new String[] { UIDFuncionario }, null, null, null);
	}

	public void ActualizarAutorizacionGuardiaUID(String Id, String EstadoUID,
			String Autorizacion) {
		ContentValues valores = new ContentValues();
		valores.put(CN_ESTADOUIDGUARDIA, EstadoUID);
		valores.put(CN_AUTORIZACIONGUARDIA, Autorizacion);
		// Actualizamos el registro en la base de datos
		db.update(TABLE_GUARIA, valores, CN_IDGUARDIA + "=" + Id, null);
	}

	public void EliminarDatosGuardiaUID(String UIDVehiculo) {
		db.delete(TABLE_GUARIA, CN_UIDGUARDIA + "=?",
				new String[] { UIDVehiculo });
	}

	public void EliminarDatosGuardiaId(String IdVehiculo) {
		db.delete(TABLE_GUARIA, CN_IDFUNCIONARIOGUARDIA + "=?",
				new String[] { IdVehiculo });
	}

	public Cursor CursorIdioma() {
		String[] Columnas = new String[] { CN_IDIOMA };

		return db.query(TABLE_IDIOMA, Columnas, CN_IDIDIOMA + "=?",
				new String[] { "1" }, null, null, null);
	}

	public void InsertarDatosIdioma(String Idioma) {
		ContentValues Valores = new ContentValues();
		Valores.put(CN_IDIDIOMA, 1);
		Valores.put(CN_IDIOMA, Idioma);
		db.insert(TABLE_IDIOMA, null, Valores);
	}

	public void ActualizarIdioma(String Idioma) {
		ContentValues Valores = new ContentValues();
		Valores.put(CN_IDIOMA, Idioma);
		db.update(TABLE_IDIOMA, Valores, CN_IDIDIOMA + "=" + "1", null);
	}

	/**************************************/

	public void InsertarTipoConexion(String conexion) {
		ContentValues Valores = new ContentValues();
		Valores.put(CN_3GWIFI, conexion);
		db.insert(TABLE_TIPOCONEXION3GWIFI, null, Valores);
	}

	public void ActualizarTipoConexion(String estado) {
		ContentValues Valores = new ContentValues();
		Valores.put(CN_3GWIFI, estado);
		db.update(TABLE_TIPOCONEXION3GWIFI, Valores, CN_TIPOCONEXION3GWIFI
				+ "=" + "1", null);
	}

	public Cursor CursorTipoConexion() {
		String[] Columnas = new String[] { CN_3GWIFI };

		return db.query(TABLE_TIPOCONEXION3GWIFI, Columnas,
				CN_TIPOCONEXION3GWIFI + "=?", new String[] { "1" }, null, null,
				null);
	}

	/***************************************/
	public void InsertarEstadoConexionWs(String estado) {
		ContentValues Valores = new ContentValues();
		Valores.put(CN_IDCONEXIONCORRECTA, 1);
		Valores.put(CN_ESTADOCONEXIONCORRECTA, estado);
		db.insert(TABLE_CONEXIONWSCORRECTA, null, Valores);
	}

	public void ActualizarEstadoConexionWs(String estado) {
		ContentValues Valores = new ContentValues();
		Valores.put(CN_IDIOMA, estado);
		db.update(TABLE_CONEXIONWSCORRECTA, Valores, CN_IDCONEXIONCORRECTA
				+ "=" + "1", null);
	}

	public Cursor CursorEstadoConexionWs() {
		String[] Columnas = new String[] { CN_ESTADOCONEXIONCORRECTA };

		return db.query(TABLE_CONEXIONWSCORRECTA, Columnas,
				CN_IDCONEXIONCORRECTA + "=?", new String[] { "1" }, null, null,
				null);
	}

	public int traerID(String tabla, String rut) {
		Cursor c = db.rawQuery("SELECT _id FROM " + tabla
				+ " WHERE IdFuncionario = '" + rut + "'", null);
		int funcionario = 0;
		if (c.moveToFirst()) {
			do {
				funcionario = c.getInt(0);
			} while (c.moveToNext());
		}
		return funcionario;
	}

	public void ActualizarDataGuardiaPorId(String UID, String EstadoUID,
			String IdFuncionario, String Nombre, String Apellido,
			String NombreEmpresa, String IdEmpresa, String Autorizacion,
			String Imagen, int id) {
		// Actualizamos el registro en la base de datos
		db.update(
				TABLE_GUARIA,
				ValoresGuardia(UID, EstadoUID, IdFuncionario, Nombre, Apellido,
						NombreEmpresa, IdEmpresa, Autorizacion, Imagen),
				CN_IDGUARDIA + "=" + id, null);
	}

	// public void ActualizarRutConK(String habilitar, int id) {
	// ContentValues valores = new ContentValues();
	// valores.put(CN_SINOLT, habilitar);
	// db.update(TABLE_LOTE, valores, CN_IDLOTE + "=" + id, null);
	//
	// // }public static final String TABLE_DATOSGUARDIA ="DATOSGUARDIA";
	// public static final String CN_IDDATOSGUARDIA = "_id";
	// public static final String CN_NOMBREDATOSGUARDIA = "Nombre";
	// public static final String CN_APELLIDODATOSGUARDIA = "Apellido";

	public void ActualizarDatosGuardia(String nombre, String apellido) {
		ContentValues Valores = new ContentValues();
		Valores.put(CN_NOMBREDATOSGUARDIA, nombre);
		Valores.put(CN_APELLIDODATOSGUARDIA, apellido);
		db.update(TABLE_DATOSGUARDIA, Valores, CN_IDDATOSGUARDIA + "=" + 1,
				null);
	}

	public void ActualizarDatosGuardiaconid(String nombre, String apellido,
			int id) {
		ContentValues Valores = new ContentValues();
		Valores.put(CN_NOMBREDATOSGUARDIA, nombre);
		Valores.put(CN_APELLIDODATOSGUARDIA, apellido);
		db.update(TABLE_DATOSGUARDIA, Valores, CN_IDDATOSGUARDIA + "=" + id,
				null);
	}

	/**************************************/
	public void InsertarDatosGuardia(String nombre, String apellido) {
		ContentValues Valores = new ContentValues();
		Valores.put(CN_NOMBREDATOSGUARDIA, nombre);
		Valores.put(CN_APELLIDODATOSGUARDIA, apellido);
		db.insert(TABLE_DATOSGUARDIA, null, Valores);
	}

	public Cursor CursorDevolverDatosGuardia() {
		String[] Columnas = new String[] { CN_NOMBREDATOSGUARDIA,
				CN_APELLIDODATOSGUARDIA };

		return db.query(TABLE_DATOSGUARDIA, Columnas, null, null, null, null,
				null);
	}

	public int devolverIdGuardia() {
		Cursor c = db.rawQuery("SELECT _id FROM DATOSGUARDIA", null);
		int dato2 = 0;
		if (c.moveToFirst()) {
			do {
				dato2 = c.getInt(0);
			} while (c.moveToNext());
		}
		return dato2;
	}

	public void EliminarGuardia() {
		db.execSQL("DELETE FROM DATOSGUARDIA");
	}

	public void InsertarConfigApp(String camara, String nfc, String log,
			String sinc) {
		ContentValues Valores = new ContentValues();
		Valores.put(CN_CAMARA, camara);
		Valores.put(CN_NFC, nfc);
		Valores.put(CN_LOG, log);
		Valores.put(CN_SINC, sinc);
		db.insert(TABLE_CONFIGAPP, null, Valores);
	}

	public void ActualizarConfigApp(String camara, String nfc, String log,
			String sinc) {
		ContentValues Valores = new ContentValues();
		Valores.put(CN_CAMARA, camara);
		Valores.put(CN_NFC, nfc);
		Valores.put(CN_LOG, log);
		Valores.put(CN_SINC, sinc);
		db.update(TABLE_CONFIGAPP, Valores, CN_IDCONFIGAPP + "=" + 1, null);
	}

	public Cursor CursorConfigApp() {
		String[] Columnas = new String[] { CN_CAMARA, CN_NFC, CN_LOG, CN_SINC };

		return db
				.query(TABLE_CONFIGAPP, Columnas, null, null, null, null, null);
	}

	public void InsertarContador(String cont) {
		ContentValues Valores = new ContentValues();
		Valores.put(CN_ESTADONFCCAM, cont);

		db.insert(TABLE_CONF_NFC, null, Valores);
	}

	public void ActualizarContador(String cont) {
		ContentValues Valores = new ContentValues();
		Valores.put(CN_ESTADONFCCAM, cont);
		db.update(TABLE_CONF_NFC, Valores, CN_IDNFCCAM + "=" + 1, null);
	}

	public Cursor cursorContador() {
		String[] Columnas = new String[] { CN_ESTADONFCCAM };

		return db.query(TABLE_CONF_NFC, Columnas, null, null, null, null, null);
	}

	public Cursor CursorSincronizacionLista() {
		String[] Columnas = new String[] { CN_ESTADOCONF };

		return db.query(TABLE_SINCOK, Columnas, CN_IDCONFSINC + "=" + 1, null,
				null, null, null);
	}

	public void InsertarSincronizacionLista(String sinc) {
		ContentValues Valores = new ContentValues();
		Valores.put(CN_ESTADOCONF, sinc);

		db.insert(TABLE_SINCOK, null, Valores);
	}

	public void ActualizarSincronizacionLista(String cont) {
		ContentValues Valores = new ContentValues();
		Valores.put(CN_ESTADOCONF, cont);
		db.update(TABLE_SINCOK, Valores, CN_IDCONFSINC + "=" + 1, null);
	}

	/************************************/
	// public static final String TABLE_CAMBIARFOTO = "FotoSinc";
	// public static final String CN_IDCAMBIARFOTO = "Id";
	// public static final String CN_COLOR_FOTO= "Color";
	public Cursor CursorCambiarFoto() {
		String[] Columnas = new String[] { CN_COLOR_FOTO };

		return db.query(TABLE_CAMBIARFOTO, Columnas, null, null, null, null,
				null);
	}

	public String TraerColorFoto() {
		Cursor c = db.rawQuery("SELECT * FROM FotoSinc", null);
		String dato2 = "";
		if (c.moveToFirst()) {
			do {
				dato2 = c.getString(1);
			} while (c.moveToNext());
		}
		return dato2;
	}

	public void InsertarColorFoto(String sinc) {
		ContentValues Valores = new ContentValues();
		Valores.put(CN_COLOR_FOTO, sinc);

		db.insert(TABLE_CAMBIARFOTO, null, Valores);
	}

	public void ActualizarColorFoto(String cont) {
		ContentValues Valores = new ContentValues();
		Valores.put(CN_COLOR_FOTO, cont);
		db.update(TABLE_CAMBIARFOTO, Valores, CN_IDCAMBIARFOTO + "=" + 1, null);
	}

	// public static final String TABLE_CONEXIONVALIDAD =
	// "CONEXIONINTERNETINTRANETOK";
	// public static final String CN_IDCONEXIONVAL = "Id";
	// public static final String CN_ESTADOOK_INTRANET = "EstadoINTRANET";
	// public static final String CN_ESTADOOK_INTERNET = "EstadoINTERNET";
	public Cursor CursorConexionesValidas() {
		String[] Columnas = new String[] { CN_ESTADOOK_INTRANET,
				CN_ESTADOOK_INTERNET };

		return db.query(TABLE_CONEXIONVALIDAD, Columnas, CN_IDCONEXIONVAL + "="
				+ 1, null, null, null, null);
	}

	public void InsertarConexionesValidas(String intranet, String internet) {
		ContentValues Valores = new ContentValues();
		Valores.put(CN_ESTADOOK_INTRANET, intranet);
		Valores.put(CN_ESTADOOK_INTERNET, internet);
		db.insert(TABLE_CONEXIONVALIDAD, null, Valores);
	}

	public void ActualizarConexionesValidas(String intranet, String internet) {
		ContentValues Valores = new ContentValues();
		Valores.put(CN_ESTADOOK_INTRANET, intranet);
		Valores.put(CN_ESTADOOK_INTERNET, internet);
		db.update(TABLE_CONEXIONVALIDAD, Valores, CN_IDCONEXIONVAL + "=" + 1,
				null);
	}

	public String TraerRutEmpresa(String rut) {
		Cursor c = db
				.rawQuery("SELECT * FROM FUNCIONARIO WHERE IdFuncionario = '"
						+ rut + "'", null);
		String dato2 = "";
		if (c.moveToFirst()) {
			do {
				dato2 = c.getString(7);
			} while (c.moveToNext());
		}
		return dato2;
	}

	public String TraerDivision() {
		Cursor c = db.rawQuery("SELECT * FROM CONFIG", null);
		String dato2 = "";
		if (c.moveToFirst()) {
			do {
				dato2 = c.getString(8);
			} while (c.moveToNext());
		}
		return dato2;
	}

	public String TraerLocal() {
		Cursor c = db.rawQuery("SELECT * FROM CONFIG", null);
		String dato2 = "";
		if (c.moveToFirst()) {
			do {
				dato2 = c.getString(9);
			} while (c.moveToNext());
		}
		return dato2;
	}

	// public static final String TABLE_CONFIGAPP ="CONFIGAPP";
	// public static final String CN_IDCONFIGAPP = "Id";
	// public static final String CN_CAMARA = "Camara";
	// public static final String CN_NFC = "NFC";
	// public static final String CN_LOG = "Log";
	//
	/* ...................................................................... */

	public void InsertarDatosSincronizacionIntranet(String Rut, String Placa,
			String IdSyncGuardia, String IdSyncPersona, String IdSyncVehiculo,
			int Contador, String EstadoSync, String TipoSync) {
		db.insert(
				TABLE_SINCRONIZACIONINTRANET,
				null,
				ValoresSincronizacion("1", Rut, Placa, IdSyncGuardia,
						IdSyncPersona, IdSyncVehiculo, Contador, EstadoSync,
						TipoSync));
	}

	public Cursor CursorSincronizacionGuardiaIntranet() {
		String[] Columnas = new String[] { CN_IDSYNCGUARDIAINTRANET,
				CN_CONTADORSYNCINTRANET, CN_ESTADOSYNCINTRANET,
				CN_TIPOSYNCINTRANET };
		return db.query(TABLE_SINCRONIZACIONINTRANET, Columnas,
				CN_IDSINCRONIZACIONINTRANET + "=?", new String[] { "1" }, null,
				null, null);
	}

	public Cursor CursorSincronizacionPersonaIntranet() {
		String[] Columnas = new String[] { CN_RUTINTRANET,
				CN_IDSYNCPERSONAINTRANET, CN_CONTADORSYNCINTRANET,
				CN_ESTADOSYNCINTRANET, CN_TIPOSYNCINTRANET };
		return db.query(TABLE_SINCRONIZACIONINTRANET, Columnas,
				CN_IDSINCRONIZACIONINTRANET + "=?", new String[] { "1" }, null,
				null, null);
	}

	public Cursor CursorSincronizacionVehiculoIntranet() {
		String[] Columnas = new String[] { CN_PLACAINTRANET,
				CN_IDSYNCVEHICULOINTRANET, CN_CONTADORSYNCINTRANET,
				CN_ESTADOSYNCINTRANET, CN_TIPOSYNCINTRANET };
		return db.query(TABLE_SINCRONIZACIONINTRANET, Columnas,
				CN_IDSINCRONIZACIONINTRANET + "=?", new String[] { "1" }, null,
				null, null);
	}

	public void ActualizarSincronizacionPersonaIntranet(String Rut,
			String IdSync, int Contador, String EstadoSync, String TipoSync) {
		ContentValues valores = new ContentValues();
		valores.put(CN_RUTINTRANET, Rut);
		valores.put(CN_IDSYNCPERSONAINTRANET, IdSync);
		valores.put(CN_CONTADORSYNCINTRANET, Contador);
		valores.put(CN_ESTADOSYNCINTRANET, EstadoSync);
		valores.put(CN_TIPOSYNCINTRANET, TipoSync);

		db.update(TABLE_SINCRONIZACIONINTRANET, valores,
				CN_IDSINCRONIZACIONINTRANET + "=" + "1", null);
	}

	public void ActualizarSincronizacionVehiculoIntranet(String Placa,
			String IdSync, int Contador, String EstadoSync, String TipoSync) {
		ContentValues valores = new ContentValues();
		valores.put(CN_PLACAINTRANET, Placa);
		valores.put(CN_IDSYNCVEHICULOINTRANET, IdSync);
		valores.put(CN_CONTADORSYNCINTRANET, Contador);
		valores.put(CN_ESTADOSYNCINTRANET, EstadoSync);
		valores.put(CN_TIPOSYNCINTRANET, TipoSync);

		db.update(TABLE_SINCRONIZACIONINTRANET, valores,
				CN_IDSINCRONIZACIONINTRANET + "=" + "1", null);
	}

	public void ActualizarSincronizacionGuardiaIntranet(String IdSync,
			int Contador, String EstadoSync, String TipoSync) {
		ContentValues valores = new ContentValues();
		valores.put(CN_IDSYNCGUARDIAINTRANET, IdSync);
		valores.put(CN_CONTADORSYNCINTRANET, Contador);
		valores.put(CN_ESTADOSYNCINTRANET, EstadoSync);
		valores.put(CN_TIPOSYNCINTRANET, TipoSync);

		db.update(TABLE_SINCRONIZACIONINTRANET, valores,
				CN_IDSINCRONIZACIONINTRANET + "=" + "1", null);
	}

	/**********************************************************/
	public static final String TABLE_HABILITARFOTOS = "HABILITAR_FOTOS";
	public static final String CN_IDHABILITARFOTOS = "Id";
	public static final String CN_HABILITAR = "habilitar";

	public static final String CREATE_TABLE24 = "create table "
			+ TABLE_HABILITARFOTOS + " (" + CN_IDHABILITARFOTOS
			+ " integer primary key, " + CN_HABILITAR + " text);";

	private ContentValues ValoresHABILITAR_FOTOS(String Id, String habilitar) {

		ContentValues valores = new ContentValues();
		valores.put(CN_IDHABILITARFOTOS, Id);
		valores.put(CN_HABILITAR, habilitar);

		return valores;
	}

	public void InsertarHABILITAR_FOTOS(String habilitar) {

		db.insert(TABLE_HABILITARFOTOS, null,
				ValoresHABILITAR_FOTOS("1", habilitar));
	}

	public Cursor CursorHABILITAR_FOTOS() {
		String[] Columnas = new String[] { CN_IDHABILITARFOTOS, CN_HABILITAR };

		return db.query(TABLE_HABILITARFOTOS, Columnas, CN_IDHABILITARFOTOS
				+ "=?", new String[] { "1" }, null, null, null);
	}

	public void ActualizarHABILITAR_FOTOS(String habilitar) {
		ContentValues valores = new ContentValues();
		valores.put(CN_HABILITAR, habilitar);

		db.update(TABLE_HABILITARFOTOS, valores, CN_IDHABILITARFOTOS + "="
				+ "1", null);
	}

	/**********************************************************/
	public static final String TABLE_UTIL = "UTIL";
	public static final String CN_IDUTIL = "Id";
	public static final String CN_VALORUTIL = "Valor";

	public static final String CREATE_TABLE25 = "create table " + TABLE_UTIL
			+ " (" + CN_IDUTIL + " integer primary key, " + CN_VALORUTIL
			+ " text);";

	private ContentValues ValoresUTIL(String Id, String valor) {

		ContentValues valores = new ContentValues();
		valores.put(CN_IDUTIL, Id);
		valores.put(CN_VALORUTIL, valor);

		return valores;
	}

	public void InsertarUTIL(String valor) {

		db.insert(TABLE_UTIL, null, ValoresUTIL("1", valor));
	}

	public Cursor CursorUTIL() {
		String[] Columnas = new String[] { CN_IDUTIL, CN_VALORUTIL };

		return db.query(TABLE_UTIL, Columnas, CN_IDUTIL + "=?",
				new String[] { "1" }, null, null, null);
	}

	public void ActualizarUTIL(String VALOR) {
		ContentValues valores = new ContentValues();
		valores.put(CN_VALORUTIL, VALOR);

		db.update(TABLE_UTIL, valores, CN_IDUTIL + "=" + "1", null);
	}

	public Cursor CursorConsultaUtil(String valor) {
		String[] Columnas = new String[] { CN_VALORUTIL };

		return db.query(TABLE_UTIL, Columnas, CN_VALORUTIL + "=?",
				new String[] { valor }, null, null, null);
	}
}
