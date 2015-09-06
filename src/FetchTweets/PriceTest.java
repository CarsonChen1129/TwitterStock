package FetchTweets;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Scanner;

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
 * 
 * @author carsonchen
 *
 */
public class PriceTest {
	
	private static final CsvPreference DELIMITED = new CsvPreference.Builder(' ', '|', "\n").build();
	public static EmotionWeight weight;		/* Initialize a EmotionWeight Object */	
  	public static double emotion = 0.0;		/* Initialize a variable to store the emotion */
	
	public static void main(String[] args) throws IOException,FileNotFoundException{
		
		
		List<String> row = null;
		
		long start = System.currentTimeMillis();
		
		String path = "/Users/carsonchen/Desktop/Twitter_project/tweets_edit4.csv";
		
		CsvListReader csvReader = new CsvListReader(new FileReader(path),DELIMITED);
		
		while((row = csvReader.read())!= null){

			String sentence = row.get(0);
			System.out.println();
			
			
		}

		String parserModel = "edu/stanford/nlp/models/lexparser/englishPCFG.caseless.ser.gz";
		String pathToSWN = "/Users/carsonchen/Desktop/Twiiter_project/Dictionary/SentiWordNet_3.0.0_20130122.txt";
	    
		/* Initialize a LexicalizedParser */
	    LexicalizedParser lp = LexicalizedParser.loadModel(parserModel);
	    
	    /* Initialize a EmotionWeight Object */
	    weight = new EmotionWeight(pathToSWN);
	    
	    /* Initalize a Scanner and get the input */
	    Scanner keyboard = new Scanner(System.in);
	    System.out.println("Please input the sentence: ");
	    String input = keyboard.nextLine();
	    
	    /* Call the function to analyze and get the result */
	    SentenceAnalyze(lp,input);
	    
	    System.out.println("The emotion value of this sentence is: "+emotion);
	    
	    /* Check the emotion of the sentence */
	    if(emotion > 0) {
	    	System.out.println("This sentence has positive emotion :)");
	    } else 
	    if(emotion < 0) {
	    	System.out.println("This is sentence has negative emotion :(");
	    } else {
	    	System.out.println("The emotion of this sentence is neutral :|");
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
	    	
	    	TregexPattern NPpattern = TregexPattern.compile(posTags[i]);
		    TregexMatcher matcher = NPpattern.matcher(parse);
		    String tag = posTags[i+1];
		   
		    while (matcher.findNextMatchingNode()) {
		    	
		      Tree match = matcher.getMatch();
		      String word = Sentence.listToString(match.yield());
		      word = word.toLowerCase();
		      double value = weight.extract(word, tag);
		      System.out.println(word+" --- pos: "+tag+"; value: "+value);
		      emotion +=value;
		      
		    }
	    	
	    }
	    


	    /* Print trees and dependencies */
	    TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
	    tp.printTree(parse);
	  }
}
