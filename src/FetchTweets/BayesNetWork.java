package FetchTweets;

import java.io.File;
import java.io.FileWriter;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.lazy.KStar;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 * Apply BayesNetwork from waka on the data set
 * @author carsonchen
 *
 */
public class BayesNetWork {

	public BayesNetWork(String path) {
		
			Instances trainIns = null;
			BayesNet bayes;
			
			try{
		          
		           
				   File output 		= new File("output.txt");
		    	   FileWriter write = new FileWriter(output);
		    	   
		    	   write.write("Applying Bayes Network Algorithm......\n");
		    	   
		           File file			= new File(path);
		           ArffLoader loader 	= new ArffLoader();
		           loader.setFile(file);
		           trainIns = loader.getDataSet();
		         
		           trainIns.setClassIndex(trainIns.numAttributes()-1);
		           
		           bayes = (BayesNet)Class.forName("weka.classifiers.bayes.BayesNet").newInstance();
		           
		           bayes.buildClassifier(trainIns);
		           
		           Evaluation eval = new Evaluation(trainIns);
		           
		           eval.crossValidateModel(bayes, trainIns, 10, new Random(1));
		           
		           
		              try{
	//	            	  	System.out.println(eval.toClassDetailsString());
	//	   	           		System.out.println(eval.toCumulativeMarginDistributionString());
	//	   	           		System.out.println(eval.toSummaryString());
	//	   	           		System.out.println(eval.toMatrixString());
		            	  
		            	  write.write(eval.toClassDetailsString());
		            	  write.write("\n");
		            	  write.write(eval.toCumulativeMarginDistributionString());
		            	  write.write("\n");
		            	  write.write(eval.toSummaryString());
		            	  write.write("\n");
		            	  write.write(eval.toMatrixString());
		            	  write.write("\n");
		            	  
		              } catch(Exception e) {
		            	  e.getMessage();
		              }
		              
		           write.close();
		           
		} catch(Exception e) {
	        e.printStackTrace();
	    }
	
	}

}
