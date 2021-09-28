package examples;

import java.awt.Color;
import java.io.File;
import java.util.Random;

import weka.classifiers.trees.RandomForest;
import weka.gui.visualize.ThresholdVisualizePanel;

public class RunOneROCCurve
{
	public static void main(String[] args) throws Exception
	{
		long startTime = System.currentTimeMillis();
		Random random = new Random();
		// this file is at 
		//https://github.com/afodor/afodor.github.io/blob/master/classes/prog2016/pivoted_genusLogNormalWithMetadata.arff
		File inArff= new File(
				"C:\\temp\\pivoted_genusLogNormalWithMetadata.arff");
				
		ThresholdVisualizePanel tvp = TestClassify.getVisPanel(inArff.getName());
		
		int numPermutations = 20;
		
		// uncomment for multi-threaded
		TestClassify.plotRocUsingMultithread(inArff, numPermutations, false, tvp, RandomForest.class.getName(), Color.BLACK);
		TestClassify.plotRocUsingMultithread(inArff, numPermutations, true, tvp, RandomForest.class.getName(), Color.RED);
		
		// uncomment for single threaded
		//TestClassify.plotROCForAnArff(inArff, numPermutations,random,false,tvp);	
		//TestClassify.plotROCForAnArff(inArff, numPermutations,random,true,tvp);	
		
		System.out.println("Finished in " + (System.currentTimeMillis() - startTime)/1000f + " seconds ");
	}
}
