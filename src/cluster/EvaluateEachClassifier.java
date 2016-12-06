package cluster;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import metaMergers.BringIntoOneNameSpace;
import parsers.NewRDPParserFileLine;
import projectDescriptors.AbstractProjectDescription;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.meta.Bagging;
import weka.classifiers.rules.OneR;
import weka.classifiers.trees.RandomForest;

public class EvaluateEachClassifier
{
	public static final String SCRIPT_DIR = "/nobackup/afodor_research/arffMerged/scripts/evaluteEachClassifier";
	private final static int numNodes = 50;
	private final static int numPermutations = 100;
	
	private static final String CLASSPATH_STRING = 
			"/users/afodor/gitInstall/WekaExamples/bin:/users/afodor/gitInstall/WekaExamples/weka.jar";
	
	public static List<Classifier> getClassifiers() throws Exception
	{
		List<Classifier> list = new ArrayList<Classifier>();
		
		list.add(new RandomForest());
		list.add(new OneR());
		list.add(new NaiveBayes());
		list.add(new SMO());
		list.add(new AdaBoostM1());
		//list.add(new AdditiveRegression());
		list.add(new AttributeSelectedClassifier());
		list.add(new Bagging());
		list.add(new BayesNet());
		return list;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<Integer, BufferedWriter> writerMap = new HashMap<Integer,BufferedWriter>();
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			SCRIPT_DIR + File.separator + "runAll.txt"	)));
		
		for( int x=0; x < numNodes; x++)
		{
			File f = new File( SCRIPT_DIR + File.separator +  "run_" + x + ".txt" );
			writer.write("qsub " + f.getAbsolutePath() + "\n");
			writerMap.put(x,new BufferedWriter(new FileWriter(f)));
		}
		
		writer.flush();  writer.close();
		
		int index =0;
		
		
		for( Classifier c : getClassifiers())
		for( AbstractProjectDescription apd : BringIntoOneNameSpace.getAllProjects())
		{
			for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
			{
				String taxa = NewRDPParserFileLine.TAXA_ARRAY[x];
				
				BufferedWriter aWriter = writerMap.get(index);
				
				aWriter.write("java -cp " + CLASSPATH_STRING + " " + 
						"cluster.EvaluateAClassifier " + apd.getClass().getName() + " " + 
						c.getClass().getName() + " " + taxa + " "+ numPermutations + "\n"
						);
				
				index++;
				
				if( index == numNodes)
					index =0;
			}
		}
		
		for( BufferedWriter aWriter : writerMap.values())
		{
			aWriter.flush();  aWriter.close();
		}
	}
}
