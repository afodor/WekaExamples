package examples;

import java.io.File;
import java.util.Random;

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
				"C:\\Users\\corei7\\git\\afodor.github.io\\classes\\prog2016\\pivoted_genusLogNormalWithMetadata.arff");
				
		ThresholdVisualizePanel tvp = TestClassify.getVisPanel(inArff.getName());
		
		int numPermutations = 20;
		
		//TestClassify.plotROCForAnArff(inArff, numPermutations,random,false,tvp);	
		//TestClassify.plotROCForAnArff(inArff, numPermutations,random,true,tvp);	
		
		TestClassify.plotRocUsingMultithread(inArff, numPermutations, random, false, tvp);
		TestClassify.plotRocUsingMultithread(inArff, numPermutations, random, true, tvp);
		
		System.out.println("Finished in " + (System.currentTimeMillis() - startTime)/1000f + " seconds ");
	}
}
