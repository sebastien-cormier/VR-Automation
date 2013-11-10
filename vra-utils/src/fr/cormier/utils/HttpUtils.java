package fr.cormier.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

public class HttpUtils {

	static final String FS = File.separator;

	/**
	 * This method does the actual GET
	 * 
	 * @param theUrl
	 *            The URL to retrieve
	 * @param filename
	 *            the local file to save to
	 * @exception IOException
	 */
	public static String wget(String theUrl) throws IOException {

		StringBuffer sb = new StringBuffer();
		try {
			URL gotoUrl = new URL(theUrl);
			InputStreamReader isr = new InputStreamReader(gotoUrl.openStream());
			BufferedReader in = new BufferedReader(isr);

			String inputLine;

			// grab the contents at the URL
			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine + "\r\n");
			}

		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			throw ioe;
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param adress
	 * @param keys
	 * @param values
	 * @return
	 * @throws IOException
	 */
	public static String post(String adress, List<String> keys,
			List<String> values) throws IOException {
		
		String result = "";
		OutputStreamWriter writer = null;
		BufferedReader reader = null;
		try {
			// encodage des paramètres de la requête
			String data = "";
			for (int i = 0; i < keys.size(); i++) {
				if (i != 0)
					data += "&amp;";
				data += URLEncoder.encode(keys.get(i), "UTF-8") + "="
						+ URLEncoder.encode(values.get(i), "UTF-8");
			}
			// création de la connection
			URL url = new URL(adress);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);

			// envoi de la requête
			writer = new OutputStreamWriter(conn.getOutputStream());
			writer.write(data);
			writer.flush();

			// lecture de la réponse
			reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String ligne;
			while ((ligne = reader.readLine()) != null) {
				result += ligne;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
			try {
				reader.close();
			} catch (Exception e) {
			}
		}
		return result;
	}
	
}
