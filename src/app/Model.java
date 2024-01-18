package app;

import static com.mongodb.client.model.Filters.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

public class Model {

	/**
	 * Funció que canvia un string que li passem per parametre a un hash SHA256
	 * @param input String que volem passar a hash
	 * @return String amb el hash. Null en cas d'error.
	 */
	public static String calcularSHA256(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

			StringBuilder hexString = new StringBuilder();
			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}

			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Funció que canvia una imatge en base64 a un .jpg y la guarda a la carpeta /img
	 * @param document Registre de la BDD que conté les dades de la imatge
	 */
	private static void processImage(Document document) {
		String id = document.getString("id");
		String base64Data = document.getString("base64");

		// Decodificar la cadena base64 a un array de bytes
		byte[] imageData = Base64.getDecoder().decode(base64Data);

		// Guardar la imagen como archivo .jpg en la carpeta /img
		String imagePath = "img/" + id;
		try (FileOutputStream fos = new FileOutputStream(imagePath)) {
			fos.write(imageData);
			System.out.println("Imagen guardada en: " + imagePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Funció que comprova si un directori existeix o no y el crea en cas negatiu.
	 * @param directoryName Nom del directori a crear
	 */
	private static void createDirectoryIfNotExists(String directoryName) {
		Path directoryPath = Paths.get(directoryName);
		if (Files.notExists(directoryPath)) {
			try {
				Files.createDirectory(directoryPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Funció que elegeix imatges de la carpeta /img y després les duplica i randomitza
	 * @param cantidadDeseada Quantitat de imatges que volem per al joc
	 * @return List<String> amb les rutes de les imatges
	 */
	public static List<String> obtenerImagenesAleatorias(int cantidadDeseada) {
		List<String> imagenes = new ArrayList<>();
		File directorio = new File("img");

		// Verificar si el directorio existe y es un directorio
		if (directorio.exists() && directorio.isDirectory()) {
			File[] archivos = directorio.listFiles();

			// Verificar si hay imágenes en el directorio
			if (archivos != null && archivos.length > 0) {
				// Obtener nombres de archivos de imágenes
				for (File archivo : archivos) {
					if (archivo.isFile()) {
						imagenes.add(archivo.getName());
					}
				}

				// Barajar las imágenes
				Collections.shuffle(imagenes);

				// Obtener la cantidad deseada de imágenes
				if (cantidadDeseada > imagenes.size()) {
					System.err.println(
							"La cantidad deseada de imágenes es mayor o igual al número de imágenes disponibles.");
				} else {
					imagenes = imagenes.subList(0, cantidadDeseada);
				}
			} else {
				System.err.println("No hay imágenes en el directorio /img.");
			}
		} else {
			System.err.println("El directorio /img no existe o no es un directorio.");
		}

		return imagenes;
	}

	/**
	 * Funció que registra a un usuari.
	 * Realitza les següents comprobacions: si els parametres están buits, si l'usuari ja está registrat.
	 * @param usuario String del usuari
	 * @param pswd String de la contrasenya
	 * @return true en cas de que el registre siga correcte. false en cas de que el registre no siga correcte
	 */
	public boolean RegistrarUsuario(String usuario, String pswd) {

		if (!usuario.isBlank() && !pswd.isBlank()) {
			try (BufferedReader reader = new BufferedReader(new FileReader("config.json"))) {
				StringBuilder jsonString = new StringBuilder();
				String line;

				while ((line = reader.readLine()) != null) {
					jsonString.append(line);
				}

				JSONObject jsonObject = new JSONObject(jsonString.toString());

				// Obtener valores del JSON
				String host = jsonObject.getString("host");
				int port = jsonObject.getInt("port");
				String databaseName = jsonObject.getString("databaseName");
				JSONArray collectionName = jsonObject.getJSONArray("collectionName");
				String colUsuarios = collectionName.getString(0);

				// Crear el cliente de MongoDB y acceder a la colección
				MongoClient mongoClient = new MongoClient(host, port);
				MongoDatabase database = mongoClient.getDatabase(databaseName);
				MongoCollection<Document> collection = database.getCollection(colUsuarios);

				// CRUD
				Bson query = eq("user", usuario);
				MongoCursor<Document> cursor = collection.find(query).iterator();
				if (!cursor.hasNext()) {
					System.out.println("No se ha encontrado el usuario y se va a registrar");
					Document doc = new Document();
					doc.append("user", usuario);
					doc.append("pass", calcularSHA256(pswd));
					collection.insertOne(doc);
					mongoClient.close();
					System.out.println("Todo ha funcionado correctamente");
					return true;
				} else {
					mongoClient.close();
					JOptionPane.showMessageDialog(null, "El usuario ya está registrado", "Error", JOptionPane.INFORMATION_MESSAGE);
					System.err.println("Se ha encontrado el usuario y no se va a registrar");
				}

			} catch (FileNotFoundException e) {
				System.err.println("Archivo de configuración no encontrado: " + e.getMessage());
			} catch (IOException e) {
				System.err.println("Error de entrada/salida: " + e.getMessage());
			} catch (Exception e) {
				System.err.println("Error al leer el archivo JSON: " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.err.println("Alguna de las cadenas está vacía, no se va a ejecutar la función");
		}
		return false;
	}

	/**
	 * Funció que inicia sesió a un usuari.
	 * Realitza les següents comprobacions: si els parametres están buits, si l'usuari no existeix, si la contrasenya no es correcta.
	 * @param usuario String del usuari
	 * @param pswd String de la contrasenya
	 * @return true en cas de que el inici de sessió siga correcte. false en cas de que el inici de sessió no siga correcte
	 */
	public boolean IniSesionUsuario(String usuario, String pswd) {

		if (!usuario.isBlank() && !pswd.isBlank()) {
			try (BufferedReader reader = new BufferedReader(new FileReader("config.json"))) {
				StringBuilder jsonString = new StringBuilder();
				String line;

				while ((line = reader.readLine()) != null) {
					jsonString.append(line);
				}

				JSONObject jsonObject = new JSONObject(jsonString.toString());

				// Obtener valores del JSON
				String host = jsonObject.getString("host");
				int port = jsonObject.getInt("port");
				String databaseName = jsonObject.getString("databaseName");
				JSONArray collectionName = jsonObject.getJSONArray("collectionName");
				String colUsuarios = collectionName.getString(0);

				// Crear el cliente de MongoDB y acceder a la colección
				MongoClient mongoClient = new MongoClient(host, port);
				MongoDatabase database = mongoClient.getDatabase(databaseName);
				MongoCollection<Document> collection = database.getCollection(colUsuarios);

				// CRUD
				pswd = calcularSHA256(pswd);
				Bson query = eq("user", usuario);
				MongoCursor<Document> cursor = collection.find(query).iterator();
				if (cursor.hasNext()) {
					System.out.println("Se ha encontrado el usuario y se va a iniciar sesión");

					Document userDocument = cursor.next();
					String storedPasswordHash = userDocument.getString("pass");

					if (pswd.equals(storedPasswordHash)) {
						System.out.println("Contraseña válida. Iniciando sesión...");
						mongoClient.close();
						return true;
					} else {
						JOptionPane.showMessageDialog(null, "Contraseña incorrecta", "Error", JOptionPane.INFORMATION_MESSAGE);
						System.err.println("Contraseña incorrecta. No se iniciará sesión.");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Error", JOptionPane.INFORMATION_MESSAGE);
					System.err.println("No se ha encontrado el usuario, debes registrarte");
				}

				mongoClient.close();
			} catch (FileNotFoundException e) {
				System.err.println("Archivo de configuración no encontrado: " + e.getMessage());
			} catch (IOException e) {
				System.err.println("Error de entrada/salida: " + e.getMessage());
			} catch (Exception e) {
				System.err.println("Error al leer el archivo JSON: " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.err.println("Alguna de las cadenas está vacía, no se va a ejecutar la función");
		}

		return false;
	}

	/**
	 * Funció que conecta amb la BDD i itera la colecció de les imatges per importarles
	 */
	public void importarImagenes() {
		try (BufferedReader reader = new BufferedReader(new FileReader("config.json"))) {
			StringBuilder jsonString = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null) {
				jsonString.append(line);
			}

			JSONObject jsonObject = new JSONObject(jsonString.toString());

			// Obtener valores del JSON
			String host = jsonObject.getString("host");
			int port = jsonObject.getInt("port");
			String databaseName = jsonObject.getString("databaseName");
			JSONArray collectionName = jsonObject.getJSONArray("collectionName");
			String colImatges = collectionName.getString(1);

			// Crear el cliente de MongoDB y acceder a la colección
			MongoClient mongoClient = new MongoClient(host, port);
			MongoDatabase database = mongoClient.getDatabase(databaseName);
			MongoCollection<Document> collection = database.getCollection(colImatges);

			// CRUD
			// Crear el directorio /img si no existe
			createDirectoryIfNotExists("img");

			// Iterar sobre las entradas en la colección
			FindIterable<Document> documents = collection.find();
			MongoCursor<Document> cursor = documents.iterator();

			while (cursor.hasNext()) {
				Document document = cursor.next();
				processImage(document);
			}

			mongoClient.close();
			System.out.println("Ha funcionado la importación de imagenes");
		} catch (FileNotFoundException e) {
			System.err.println("Archivo de configuración no encontrado: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Error de entrada/salida: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error al leer el archivo JSON: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Funció que guarda el temps que ha durat la partida
	 * @param usuario String amb l'usuari
	 * @param dificultad int amb la dificultat del joc
	 * @param duracion int amb el temps de joc
	 * @return true en cas de que el registre siga correcte. false en cas de que el registre no siga correcte
	 */
	public boolean guardarTiempo(String usuario, int dificultad, int duracion) {
		try (BufferedReader reader = new BufferedReader(new FileReader("config.json"))) {
			StringBuilder jsonString = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null) {
				jsonString.append(line);
			}

			JSONObject jsonObject = new JSONObject(jsonString.toString());

			// Obtener valores del JSON
			String host = jsonObject.getString("host");
			int port = jsonObject.getInt("port");
			String databaseName = jsonObject.getString("databaseName");
			JSONArray collectionName = jsonObject.getJSONArray("collectionName");
			String colRecords = collectionName.getString(2);

			// Crear el cliente de MongoDB y acceder a la colección
			MongoClient mongoClient = new MongoClient(host, port);
			MongoDatabase database = mongoClient.getDatabase(databaseName);
			MongoCollection<Document> collection = database.getCollection(colRecords);

			// CRUD
			Document nuevoRegistro = new Document().append("usuario", usuario) // Cambiar a tu usuario actual
					.append("dificultad", dificultad) // Cambiar a tu dificultad actual
					.append("timestamp", generarTimestamp()) // Generar timestamp actual
					.append("duracion", duracion); // Cambiar a tu duración actual

			// Insertar el nuevo documento en la colección
			collection.insertOne(nuevoRegistro);

			mongoClient.close();
			System.out.println("Se ha registrado el tiempo de manera correcta");
			return true;
		} catch (FileNotFoundException e) {
			System.err.println("Archivo de configuración no encontrado: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Error de entrada/salida: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error al leer el archivo JSON: " + e.getMessage());
			e.printStackTrace();
		}

		return false;
	}

	private String generarTimestamp() {
		// Obtener la fecha y hora actual
		Date fechaActual = new Date();

		// Crear un formato para el timestamp
		SimpleDateFormat formatoTimestamp = new SimpleDateFormat("yyyyMMdd_HHmmss");

		// Formatear la fecha actual según el formato
		return formatoTimestamp.format(fechaActual);
	}

	/**
	 * Funció que compara el temps proporcionat amb la resta de la coleccio, filtrant per la dificultat
	 * @param dificultad int amb la dificultat del joc
	 * @param duracion int amb el temps de joc
	 * @return true o false depenent si es el menor temps de la colecció. false en cas d'error.
	 */
	public boolean compararTiempo(int dificultad, int duracion) {
		try (BufferedReader reader = new BufferedReader(new FileReader("config.json"))) {
			StringBuilder jsonString = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null) {
				jsonString.append(line);
			}

			JSONObject jsonObject = new JSONObject(jsonString.toString());

			// Obtener valores del JSON
			String host = jsonObject.getString("host");
			int port = jsonObject.getInt("port");
			String databaseName = jsonObject.getString("databaseName");
			JSONArray collectionName = jsonObject.getJSONArray("collectionName");
			String colRecords = collectionName.getString(2);

			// Crear el cliente de MongoDB y acceder a la colección
			MongoClient mongoClient = new MongoClient(host, port);
			MongoDatabase database = mongoClient.getDatabase(databaseName);
			MongoCollection<Document> collection = database.getCollection(colRecords);

			// CRUD
			Bson filtro = Filters.eq("dificultad", dificultad);

			Document menorTiempoRegistro = collection.find(filtro).sort(Sorts.ascending("duracion")).limit(1).first();

			if (menorTiempoRegistro != null) {
				int menorTiempo = menorTiempoRegistro.getInteger("duracion");
				mongoClient.close();
				return duracion < menorTiempo;
			}

			mongoClient.close();
			System.out.println("No se ha encontrado ningún registro en la colección");
		} catch (FileNotFoundException e) {
			System.err.println("Archivo de configuración no encontrado: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Error de entrada/salida: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error al leer el archivo JSON: " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Funció que filtra els records per la dificultat i torna una lista de Document amb els registres de la colecció
	 * @param dificultad int amb la dificultat del joc
	 * @return List<Document> Lista amb els registres de la colecció
	 */
	public List<Document> seleccionRecords(int dificultad) {
		List<Document> resultados = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader("config.json"))) {
			StringBuilder jsonString = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null) {
				jsonString.append(line);
			}

			JSONObject jsonObject = new JSONObject(jsonString.toString());

			// Obtener valores del JSON
			String host = jsonObject.getString("host");
			int port = jsonObject.getInt("port");
			String databaseName = jsonObject.getString("databaseName");
			JSONArray collectionName = jsonObject.getJSONArray("collectionName");
			String colRecords = collectionName.getString(2);

			// Crear el cliente de MongoDB y acceder a la colección
			MongoClient mongoClient = new MongoClient(host, port);
			MongoDatabase database = mongoClient.getDatabase(databaseName);
			MongoCollection<Document> collection = database.getCollection(colRecords);
			
			// Filtrar registros según la dificultad
	        Document filtro = new Document("dificultad", dificultad);
	        FindIterable<Document> registros = collection.find(filtro).sort(new Document("duracion", 1));

	        // Convertir FindIterable a List<Document>
	        registros.into(resultados);

			mongoClient.close();

		} catch (FileNotFoundException e) {
			System.err.println("Archivo de configuración no encontrado: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Error de entrada/salida: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error al leer el archivo JSON: " + e.getMessage());
			e.printStackTrace();
		}
		return resultados;
	}

}
