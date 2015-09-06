package FetchTweets;

import java.io.File;
import java.io.FileWriter;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 * Apply NeuralNetwork from weka to the data set
 * @author carsonchen
 *
 */
public class NeuralNetwork {
	
	/**
	 * class constructor
	 * @param path
	 */
	public NeuralNetwork(String path) {
		
			Instances trainIns = null;
			MultilayerPerceptron mp;
			try{
		          
		          
				   File output 			= new File("output.txt");
		    	   FileWriter write 	= new FileWriter(output);
		    	   write.write("Applying Neural Network Algorithm......\n");
		           File file			= new File(path);
		           ArffLoader loader 	= new ArffLoader();
		           loader.setFile(file);
		           trainIns = loader.getDataSet();
		          
		           
		           trainIns.setClassIndex(trainIns.numAttributes()-1);
		           
		           mp = (MultilayerPerceptron)Class.forName("weka.classifiers.functions.MultilayerPerceptron").newInstance();
		           
		           mp.buildClassifier(trainIns);
		           
		           Evaluation eval = new Evaluation(trainIns);
		           
		           eval.crossValidateModel(mp, trainIns, 10, new Random(1));
		           
		           
		              try {
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
