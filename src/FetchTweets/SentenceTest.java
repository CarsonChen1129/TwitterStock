package FetchTweets;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;

/**
 * SentenceTest.class
 * 
 * </br>
 * 
 * 	<h3>Note:</h3>
 * 	<ul>
 * 	<li>This program analyzes a sentence and detects the emotion of the sentence</li>
 * 	<li>Use Stanford Parser to analyze the structure of the sentence and the pos tag of every words inside the sentence</li>
 * 	<li>Pass the words and their pos tags to the EmotionWeights class</li>
 * 	<li>Accumulate the emotion score from the EmotionWeights class to get the total emotion score of the sentence</li>
 * 	<li>Print out the score and the relevant result</li>
 * 	</ul>
 * 
 * </br>
 * 
 * 
 * 
 * @author carsonchen
 *
 */

public class SentenceTest {
	
		private static final CsvPreference DELIMITED 		= new CsvPreference.Builder(' ', '|', "\n").build();
	  	public static EmotionWeight weight;								/* Initialize a EmotionWeight Object */	
	  	public static double emotion 						= 0.0;		/* Initialize a variable to store the emotion */
	  	public static double totalPos 						= 0.0;
	  	public static double totalNeg 						= 0.0;
	  	public static double totalEmo 						= 0.0;
	  	public static double totalEmoAbs 					= 0.0;
	  	public static int totalNoPos 						= 0;
	  	public static int totalNoNeu 						= 0;
	  	public static int totalNoNeg 						= 0;
	  	public static int totalNo 							= 0;
	  	
	  	public static double posValTotal 					= 0.0;
	  	public static double negValTotal 					= 0.0;
	  	
	  	public static double posNoTotal	 					= 0.0;
	  	public static double neuNoTotal  					= 0.0;
	  	public static double negNoTotal  					= 0.0;
	  	
	  	
	  	public static void main(String[] args) throws IOException {
	  	
		  
	//	    String parserModel = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
			String parserModel = "edu/stanford/nlp/models/lexparser/englishPCFG.caseless.ser.gz";
			String pathToSWN = "Dictionary/SentiWordNet_3.0.0_20130122.txt";
		    
			/* Initialize a LexicalizedParser */
		    LexicalizedParser lp = LexicalizedParser.loadModel(parserModel);
		    
		    /* Initialize a EmotionWeight Object */
		    weight = new EmotionWeight(pathToSWN);
		    
		    /* Initalize a Scanner and get the input */
		    System.out.println("1. Sentence 2. File ");
		    Scanner keyboard = new Scanner(System.in);
		    int option = keyboard.nextInt();
		    
		    if (option == 1) {
		    	
		    	System.out.println("Please input the sentence: ");
		    	Scanner keyboard1 	= new Scanner(System.in);
    			String input 		= keyboard1.nextLine();
    
			    /* Call the function to analyze and get the result */
			    SentenceAnalyze(lp,input);
			    
			    System.out.println("The emotion value of this sentence is: "+emotion);
			    
			    /* Check the emotion of the sentence */
			    if (emotion > 0) {
			    	System.out.println("This sentence has positive emotion :)");
			    } else 
			    if (emotion < 0) {
			    	System.out.println("This is sentence has negative emotion :(");
			    } else {
			    	System.out.println("The emotion of this sentence is neutral :|");
			    }
		    } else 
		    if (option == 2) {
		    
		    			String path 			= "/Users/carsonchen/Desktop/tweetsData/01-12-2014/$COKE.csv";
			    		File file 				= new File(path);
			    		List<String> row 		= null;
			    	    CsvListReader csvReader = new CsvListReader(new FileReader(file),DELIMITED);
			    	    
			    	    while ((row=csvReader.read())!=null) {
				    		
				    		String str = row.get(0);
				    		/* Call the function to analyze and get the result */
				    	    SentenceAnalyze(lp,str.replaceAll("https?://\\S+\\s?", "").replaceAll("@\\S+\\s?", ""));
				    	    
				    	    System.out.println("The emotion value of this sentence is: "+emotion);
				    	    
				    	    totalEmo += emotion;
				    	    totalEmoAbs += Math.abs(emotion);
				    	    totalNo ++;
				    	    
				    	    /* Check the emotion of the sentence */
				    	    if (emotion > 0) {
				    	    	totalPos 	+= emotion;
				    	    	totalNoPos	++;
				    	    	System.out.println("This sentence has positive emotion :)");
				    	    } else 
				    	    if (emotion < 0) {
				    	    	totalNeg 	+= emotion;
				    	    	totalNoNeg	++;
				    	    	System.out.println("This is sentence has negative emotion :(");
				    	    } else {
				    	    	totalNoNeu	++;
				    	    	System.out.println("The emotion of this sentence is neutral :|");
				    	    }
				    	    
				    	    System.out.println();
				    		
				    }
				    
				    
				    System.out.println("The total emotion value is: "+totalEmo);
				    System.out.println("The total positive emotion value is: "+totalPos);
				    System.out.println("The total negative emotion value is: "+totalNeg);
				    
				    posValTotal = totalPos*1.0/totalEmoAbs;
				    negValTotal = Math.abs(totalNeg*1.0)/totalEmoAbs;
				    System.out.println("pos/totalAbs is: "+ posValTotal);
				    System.out.println("negative/totalAbs is: "+negValTotal);
				    
				    System.out.println("Total No of Pos is: "+totalNoPos);
				    System.out.println("Total No of Neu is: "+totalNoNeu);
				    System.out.println("Total No of Neg is: "+totalNoNeg);
				    System.out.println("Total No is: "+totalNo);
				    
				    posNoTotal = totalNoPos*1.0/totalNo;
				    neuNoTotal = totalNoNeu*1.0/totalNo;
				    negNoTotal = totalNoNeg*1.0/totalNo;
				    System.out.println("NoOfPos/NoTotal is: "+posNoTotal);
				    System.out.println("NoOfNeu/NoTotal is: "+neuNoTotal);
				    System.out.println("NoOfNeg/NoTotal is: "+negNoTotal);
		    }
		    
		  }
	
	
		 /**
		  * SentenceAnalyze function
		  * 
		  * 
		  * 
		  * @param lp
		  * @param input
		  */
		  public static void SentenceAnalyze(LexicalizedParser lp,String input) {
	
		    
			/* load and use an explicit tokenizer from the Stanford Parser */
		    TokenizerFactory<CoreLabel> tokenizerFactory 		= PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
		    Tokenizer<CoreLabel> tok 							= tokenizerFactory.getTokenizer(new StringReader(input));
		    List<CoreLabel> rawWords2 							= tok.tokenize();
		    Tree parse 											= lp.apply(rawWords2);
		    
		    /* Initialize a string array to help the program go over all the pos tags in a sentence */
		    String[] posTags = {"@NN !<< @NN","n","@JJ !<< @JJ","a","@RB !<< @RB","r","@VB !<< @VB","v"};
		    
		    for (int i=0;i<posTags.length;i+=2) {
		    	
		    	TregexPattern NPpattern 	= TregexPattern.compile(posTags[i]);
			    TregexMatcher matcher 		= NPpattern.matcher(parse);
			    String tag 					= posTags[i+1];
			   
			    while (matcher.findNextMatchingNode()) {
			    	
			      Tree match 				= matcher.getMatch();
			      String word 				= Sentence.listToString(match.yield());
			      word 						= word.toLowerCase();
			      double value 				= weight.extract(word, tag);
			      System.out.println(word+" --- pos: "+tag+"; value: "+value);
			      emotion 					+=value;
			      
			    }
		    	
		    }
		    
	
	
		    /* Print trees and dependencies */
		    TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
		    tp.printTree(parse);
		    
		  }
	
		  private SentenceTest() {} // static methods only	

}
