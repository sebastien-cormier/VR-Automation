package fr.cormier.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtils {

    static final String FS = File.separator;

    /** This method does the actual GET
     * 
     * @param theUrl The URL to retrieve
     * @param filename the local file to save to
     * @exception IOException 
     */
    public static String wget(String theUrl) throws IOException
    {
   
    	StringBuffer sb = new StringBuffer();
        try {
            URL gotoUrl = new URL(theUrl);
            InputStreamReader isr = new InputStreamReader(gotoUrl.openStream());
            BufferedReader in = new BufferedReader(isr);
           
            String inputLine;
            
            //grab the contents at the URL
            while ((inputLine = in.readLine()) != null){
                sb.append(inputLine+"\r\n");
            }
           
        }
        catch (MalformedURLException mue) {
            mue.printStackTrace();
        }
        catch (IOException ioe) {
            throw ioe;
        }
        return sb.toString();
    }

}
