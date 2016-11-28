package examples;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import weka.classifiers.trees.RandomForest;
import weka.gui.visualize.ThresholdVisualizePanel;

public class RunOneROCCurve
{
	public static void main(String[] args) throws Exception
	{
		long startTime = System.currentTimeMillis();
		// this file is at 
		//https://github.com/afodor/afodor.github.io/blob/master/classes/prog2016/pivoted_genusLogNormalWithMetadata.arff
		File inArff= new File(
				"C:\\Users\\afodor\\git\\afodor.github.io\\classes\\prog2016\\pivoted_genusLogNormalWithMetadata.arff");
				
		ThresholdVisualizePanel tvp = TestClassify.getVisPanel(inArff.getName());
		
		int numPermutations = 50;
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				"c:\\temp\\comparisonRandomForest.txt")));
		writer.write("notScrambled\tscrambled\n");
		
		//TestClassify.plotROCForAnArff(inArff, numPermutations,random,false,tvp);	
		//TestClassify.plotROCForAnArff(inArff, numPermutations,random,true,tvp);	
		
		String className = RandomForest.class.getName();
		List<Double> notScrambled = TestClassify.plotRocUsingMultithread(inArff, numPermutations, false, tvp,
						className, Color.black);
		List<Double> scrambled =  TestClassify.plotRocUsingMultithread(inArff, numPermutations, true, tvp,
						className, Color.red);
		
		for( int x=0; x < numPermutations; x++)
			writer.write(notScrambled.get(x) + "\t" + scrambled.get(x) + "\n");
		
		System.out.println("Finished in " + (System.currentTimeMillis() - startTime)/1000f + " seconds ");
		
		writer.flush(); writer.close();
	}
}
