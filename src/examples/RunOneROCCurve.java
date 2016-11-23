package examples;

import java.io.File;
import java.util.Random;

import weka.gui.visualize.ThresholdVisualizePanel;

public class RunOneROCCurve
{
	public static void main(String[] args) throws Exception
	{
		Random random = new Random();
		// this file is at 
		//https://github.com/afodor/afodor.github.io/blob/master/classes/prog2016/pivoted_genusLogNormalWithMetadata.arff
		File inArff= new File(
				"C:\\Users\\corei7\\git\\afodor.github.io\\classes\\prog2016\\pivoted_genusLogNormalWithMetadata.arff");
				
		ThresholdVisualizePanel tvp = TestClassify.getVisPanel(inArff.getName());
				
		TestClassify.plotROCForAnArff(inArff, 1,random,false,tvp);	
		TestClassify.plotROCForAnArff(inArff, 1,random,true,tvp);	
	}
}
