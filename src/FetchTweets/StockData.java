package FetchTweets;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JOptionPane;

import FetchTweets.Menu;

/**
 * 
 * @author Joe Chakko
 * @editedby: carsonchen
 *
 */
public class StockData {


		public StockData() {
			
	        try{
	        	/*Connect to the Yahoo! Finance API */
	            URL url = new URL("http://finance.yahoo.com/d/quotes.csv?s=VZ+CMCSA+F+GT+"
	            		+ "MPAA+SMP+GPRO+COKE+DPS+MNST+PEP+CPB+GIS+K+KRFT+TSN+SJM+CL+PG+REV+MORN+"
	            		+ "LOGI+HPQ+AMD+AAPL+BBRY+GRMN+MSI+COF+CIT+BAC+WFC+ANF+ARO+AEO+BURL+DSW+EXPR"
	            		+ "+FOSL+URBN+JCP+KSS+M+DG+DLTR+TGT+WMT+CVS+WAG+BBY+HGG+RSH+SHOS+SWY+WFM+"
	            		+ "LOW+HD+AMZN+OSTK+ADBE+CSCO+YHOO+FB+AAL+DAL+JBLU+LUV+SAVE+"
	            		+ "GOOG+MSFT&f=snabpocw1d1hg");
	            
	            
	            /* Create a directory to store the tweets data if the directory does not exists */
	    		File stockData = new File("stockData");
	    		if (!(stockData.exists()) && !(stockData.isDirectory())) {
	    			stockData.mkdir();
	    		}
	    		
	            URLConnection connection = url.openConnection();
	            System.out.println("Connection Opened");
	            
	            /*Create a file to write to */
	            BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
	            File out 						= new File("stockData/stockData.csv");
	            String path 					= out.getAbsolutePath();
	            System.out.println(path);
	            System.out.println("Created new file");
	            
	            /*Write to the file */
	            System.out.println("Preparing to write to file");
	            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(out));
	            byte[] b = new byte[8 *1024];
	            int read = 0;
	            while ((read = inputStream.read(b)) > -1) {
	                outputStream.write(b,0, read);
	            }
	            System.out.println("Write successful");
	            
	            
	            /*Clear the streams */
	            outputStream.flush();
	            outputStream.close();
	            inputStream.close();
	            
	            JOptionPane.showMessageDialog(null,"Data from Yahoo Finance has been saved to the local file:\n"+path);
	            new Menu();
				Menu.main(null);
	            
	        }
	        catch(IOException mfu) {
	        	
	            mfu.printStackTrace();
	            JOptionPane.showMessageDialog(null, "ERROR exists or no Internet access");
	            new Menu();
				Menu.main(null);
				
	        }					
		
		}

}

