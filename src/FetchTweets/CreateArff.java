package FetchTweets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

/**
 * Arrange the data and create an arff file
 * @author carsonchen
 *
 */
public class CreateArff {
	
	private static final CsvPreference DELIMITED 		= new CsvPreference.Builder('"', ',', "\n").build();
	private static String tag 							= null;
	private static String date 							= null;
	public static ArrayList<ArrayList<Double>> EmoVal 	= null;
	public static ArrayList<ArrayList<Double>> EmoNo  	= null;
	public static ArrayList<Double> 	PercentChange 	= null;
	/* Initialize a csv writer */
    private static FileWriter fwVal;
	private static CsvListWriter writerVal;
	private static FileWriter fwNo;
	private static CsvListWriter writerNo;
	private static double GposTotal 					= 0.0;
	private static double GnegTotal 					= 0.0;
	private static double GposNo 						= 0.0;
	private static double GneuNo 						= 0.0;
	private static double GnegNo 						= 0.0;
	private static double change 						= 0.0;
	private static SimpleDateFormat formatter 			= new SimpleDateFormat("dd-MM-yyyy");
	private static DateFormat format 					= new SimpleDateFormat("MM/dd/yyyy");
	private static Date	stockDate						= null;
	private static Pattern patt							= Pattern.compile("\\d{1,2}+[-]\\d{1,2}+[-]\\d{4}+");
	
	/**
	 * CreateArff class constructor
	 * @throws Exception
	 */
	public CreateArff() throws Exception {
		
		
		String parserModel 		= "edu/stanford/nlp/models/lexparser/englishPCFG.caseless.ser.gz";
		String pathToSWN 		= "Dictionary/SentiWordNet_3.0.0_20130122.txt";
		
		/* Initialize a LexicalizedParser */
	    LexicalizedParser lp 	= LexicalizedParser.loadModel(parserModel);
	    
	    
		JFileSelector choose 	= new JFileSelector();
	    File source 			= choose.openFile("Please choose the tweets file");
	    
	    	
	    	EmoVal 			= new ArrayList<ArrayList<Double>>();
	    	EmoNo  			= new ArrayList<ArrayList<Double>>();
	    	PercentChange 	= new ArrayList<Double>();	    	
			date = source.getName();
			System.out.println(date);

			if (source.isDirectory() || !source.isFile()) {
				File[] files 	= source.listFiles();
				for (int i=0;i<files.length;i++) {
					ReadFiles(files[i],lp,pathToSWN);
				}
			} else {
				File f = source;
				ReadFiles(f,lp,pathToSWN);
			}
			
			
			System.out.println("The size of EmoVal is: "+EmoVal.size());
			System.out.println("The size of EmoNo is: "+EmoNo.size());
			System.out.println("The size of PercentChange is: "+PercentChange.size());
								
	    CreateArffFiles();
	    
	    JOptionPane.showMessageDialog(null, "Successfully calculate the emotion valus and create arff files");
		new Menu();
		Menu.main(null);
		
	}
	
	/**
	 * Read csv files
	 * @param file
	 * @param lp
	 * @param pathToSWN
	 * @throws Exception
	 */
	private static void ReadFiles (File file,LexicalizedParser lp,String pathToSWN) throws Exception {
		
		if (file.getAbsolutePath().endsWith(".csv")) {
			
			fwVal = new FileWriter("tempVal.csv",true);
		    writerVal = new CsvListWriter(fwVal,DELIMITED);
			writerVal.write("pos/total","neg/total","change_percentage");
		    fwNo = new FileWriter("tempNo.csv",true);
		    writerNo = new CsvListWriter(fwNo,DELIMITED);
			writerNo.write("pos/total","neu/total","neg/total","change_percentage");
			
			System.out.println("Path: "+file.getAbsolutePath());
			
			try {
				
			emotionData(file.getAbsolutePath(),lp,pathToSWN);
			stockData(); 
			
			writerVal.write(GposTotal,GnegTotal,change);
			writerNo.write(GposNo,GneuNo,GnegNo,change);
			writerVal.flush();
			writerNo.flush();
			writerVal.close();
			writerNo.close();
			
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	public static void CreateArffFiles() throws Exception {
		
	 
	     CSVLoader loader 	= new CSVLoader();
	     loader.setSource(new File("tempVal.csv"));
	     Instances csvData 	= loader.getDataSet();
	     
	     ArffSaver saver 	= new ArffSaver();
	     saver.setInstances(csvData);
	     saver.setFile(new File("TweetStockEmotionValues.arff"));
	     saver.writeBatch();
	     
	     List<String> row 			= null;
		 CsvListReader csvReader 	= new CsvListReader(new FileReader("tempVal.csv"),DELIMITED);
		 CsvListWriter csvWriter 	= new CsvListWriter(new FileWriter("tempValNo.csv"),DELIMITED);
		 csvWriter.write("pos/total","neg/total","change");
		 row = csvReader.read();
		 
		 while ((row=csvReader.read()) !=null) {
			 
			 String change = null;
			 
			 try {
				 
				 if (Double.parseDouble(row.get(2)) > 0) {
					 change = "increase";
				 } else if (Double.parseDouble(row.get(2)) < 0) {
					 change = "decrease";
				 } else {
					 change = "stay";
				 }
			 } catch(Exception e) {
				 
			 } 
			 csvWriter.write(row.get(0),row.get(1),change);
		 }
		 csvWriter.close();
	     
		 CSVLoader loader2 	= new CSVLoader();
	     loader2.setSource(new File("tempValNo.csv"));
	     Instances csvData2 = loader2.getDataSet();
	     
	     ArffSaver saver2 	= new ArffSaver();
	     saver2.setInstances(csvData2);
	     saver2.setFile(new File("TweetStockEmotionValuesNo.arff"));
	     saver2.writeBatch();
	     


	     /*  ======================================== */	     
	     
	     
	     
	     CSVLoader loaderNo = new CSVLoader();
	     loaderNo.setSource(new File("tempNo.csv"));
	     Instances csvDataNo = loaderNo.getDataSet();
	     
	     ArffSaver saverNo = new ArffSaver();
	     saverNo.setInstances(csvDataNo);
	     saverNo.setFile(new File("TweetStockEmotionNo.arff"));
	     saverNo.writeBatch();
	     
	     List<String> rowNo = null;
		 CsvListReader csvReaderNo = new CsvListReader(new FileReader("tempNo.csv"),DELIMITED);
		 CsvListWriter csvWriterNo = new CsvListWriter(new FileWriter("tempNoNo.csv"),DELIMITED);
		 csvWriterNo.write("pos/total","neu/total","neg/total","change");
		 rowNo = csvReaderNo.read();
		 
		 while ((rowNo=csvReaderNo.read()) !=null) {
			 
			 String changeNo = null;
			 
			 
			 try {
				 
				 if (Double.parseDouble(rowNo.get(3)) > 0) {
					 changeNo = "increase";
				 } else if (Double.parseDouble(rowNo.get(3)) < 0) {
					 changeNo = "decrease";
				 } else {
					 changeNo = "stay";
				 }
				 
			 } catch(Exception e) {
				 
			 }
			 
			 
			 csvWriterNo.write(rowNo.get(0),rowNo.get(1),rowNo.get(2),changeNo);
		 }
		 csvWriterNo.close();
	     
		 CSVLoader loaderNo2 	= new CSVLoader();
	     loaderNo2.setSource(new File("tempNoNo.csv"));
	     Instances csvDataNo2 	= loaderNo2.getDataSet();
	     
	     ArffSaver saverNo2 	= new ArffSaver();
	     saverNo2.setInstances(csvDataNo2);
	     saverNo2.setFile(new File("TweetStockEmotionNoNo.arff"));
	     saverNo2.writeBatch();
	   

	}
	
	private static void emotionData(String path,LexicalizedParser lp,String pathToSWN) throws Exception {
		
		ArrayList<Double> listVal 	= new ArrayList<Double>();
		ArrayList<Double> listNo 	= new ArrayList<Double>();
		AnalyzeSentence sent 		= new AnalyzeSentence(path,lp,pathToSWN);
		
		GposTotal 					= AnalyzeSentence.posValTotal;
		GnegTotal 					= AnalyzeSentence.negValTotal;
		GposNo 						= AnalyzeSentence.posNoTotal;
		GneuNo 						= AnalyzeSentence.neuNoTotal;
		GnegNo 						= AnalyzeSentence.negNoTotal;
		
		listVal.add(AnalyzeSentence.posValTotal);
		listVal.add(AnalyzeSentence.negValTotal);
		listNo.add(AnalyzeSentence.posNoTotal);
		listNo.add(AnalyzeSentence.neuNoTotal);
		listNo.add(AnalyzeSentence.negNoTotal);
		EmoVal.add(listVal);
		EmoNo.add(listNo);
		
		tag = AnalyzeSentence.fileName;
	}
	
	private static void stockData() throws Exception {
		
		String stockFile 		= "stockData/stockData.csv";
		List<String> row 		= null;
	    CsvListReader csvReader = new CsvListReader(new FileReader(stockFile),DELIMITED);
	    boolean found 			= false;
	    
	    System.out.println("Global tag is: "+tag);
	    System.out.println("Global date is: "+date);
	    while ((row=csvReader.read())!=null) {

	    	found = false;
	    	
	    	if (row.get(0).equals(tag)) {
	    		
	    		String stockMarketDate 	= row.get(8);
	    		Matcher match 			= patt.matcher(stockMarketDate);
	    		
	    		if (match.matches() == false ) {
	    			stockDate 		= format.parse(stockMarketDate);
	    			stockMarketDate = formatter.format(stockDate);
	    		}
	    		
	    		if (stockMarketDate.equals(date)){
	    			
	    			double changePercent = 0.0;
	    			try {
	    				changePercent = Double.parseDouble(row.get(6));
	    			} catch(Exception e) {
	    				changePercent = 0.0;
	    			}
	    			PercentChange.add(changePercent);
		    		System.out.println("Change percent is: "+row.get(6));
		    		change = changePercent;
		    		found = true;
	    		}	
	    		
	    	}
	    	
	    }
	    if (found == false) {
	    	PercentChange.add(0.0);
	    	change = 0.0;
	    }
	}
	

}
