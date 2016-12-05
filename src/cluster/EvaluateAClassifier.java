package cluster;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import examples.TestClassify;
import projectDescriptors.AbstractProjectDescription;
import weka.classifiers.Classifier;

public class EvaluateAClassifier
{
	private static final File OUTPUT_DIR = 
			new File("/nobackup/afodor_research/arffMerged/eachClassifierOut");
	
	public static void main(String[] args) throws Exception
	{
		if( args.length != 4)
		{
			System.out.println("Usage AbstractProject AbstractClassifier taxaLevel numPermutations");
			System.exit(1);
		}
		
		AbstractProjectDescription apd = (AbstractProjectDescription)
				Class.forName(args[0]).newInstance();
		
		Classifier classifier = (Classifier) Class.forName(args[1]).newInstance();
		
		String taxaLevel = args[2];
		int numPermutations = Integer.parseInt(args[3]);
		
		File inputFile = new File(apd.getArffMergedFileFromRDP(taxaLevel));
		
		if(! inputFile.exists())
			throw new Exception("Could not find " + inputFile.getAbsolutePath());
		
		List<Double> unscrambled= 
				TestClassify.plotRocUsingMultithread(inputFile, numPermutations, false, null,
				classifier.getClass().getName(), Color.green);
		
		List<Double> scrambled= TestClassify.plotRocUsingMultithread(inputFile, 
				numPermutations, true, null,
						classifier.getClass().getName(), Color.black);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				OUTPUT_DIR + File.separator + 
			args[0] + "_" + args[1] + "_" + args[2] + ".txt" 	)));
		writer.write("unscrambled\tscrambled\n");
		
		for(int x=0; x < numPermutations; x++)
			writer.write(unscrambled.get(x) + "\t" + scrambled.get(x) + "\n");
		
		writer.flush();  writer.close();
	}
}
