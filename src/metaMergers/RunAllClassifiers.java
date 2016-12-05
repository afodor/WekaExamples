package metaMergers;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import examples.TestClassify;
import parsers.NewRDPParserFileLine;
import projectDescriptors.AbstractProjectDescription;
import utils.ConfigReader;
import weka.classifiers.rules.OneR;
import weka.classifiers.trees.RandomForest;
import weka.gui.visualize.ThresholdVisualizePanel;

public class RunAllClassifiers
{
	private static void writeResults(String taxa, HashMap<String, List<Double>> results) 
				throws Exception
	{
		System.out.println( "allProjects_" + taxa + ".txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			ConfigReader.getMergedArffDir() + File.separator + "allProjects_" + taxa + ".txt"	)));
		
		List<String> list = new ArrayList<String>( results.keySet());
		
		writer.write("iteration");
		
		for(String s : list )
			writer.write("\t" + s);
		
		writer.write("\n");
		
		int length = results.get(list.get(0)).size();
		
		for( int x=0; x < length; x++)
		{
			writer.write("" + (x+1));
			
			for(String s : list) 
				writer.write("\t" + results.get(s).get(x));
				
			writer.write("\n");
			
		}
		
		
		writer.flush();  writer.close();
		
	}
	
	public static void main(String[] args) throws Exception
	{
		int numPermutations = 50;
		List<AbstractProjectDescription> projects = BringIntoOneNameSpace.getAllProjects();
		
		for( int x=1 ; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{

			HashMap<String, List<Double>> resultsMap = new LinkedHashMap<String,List<Double>>();
			
			String taxa = NewRDPParserFileLine.TAXA_ARRAY[x];
			for( AbstractProjectDescription apd : projects)
			{
				File inArff= new File(apd.getArffMergedFileFromRDP(taxa));

				//ThresholdVisualizePanel tvp = TestClassify.getVisPanel(
					//apd.getProjectName() + " " + taxa	);
				
				String unScrambled = apd.getProjectName();
				String scrambled= apd.getProjectName() + "_" +"_scrambled";
				String oneR = apd.getProjectName() + "_" +"_oneR";
				
				if( resultsMap.containsKey(unScrambled) || resultsMap.containsKey(scrambled)
							|| resultsMap.containsKey(oneR))
					throw new Exception("duplicate");
				
				resultsMap.put(unScrambled, 
				TestClassify.plotRocUsingMultithread(
					inArff, numPermutations, false, null, new RandomForest().getClass().getName(), 
						Color.BLACK));
				
				resultsMap.put(oneR, 
				TestClassify.plotRocUsingMultithread(
					inArff, numPermutations, false, null, new OneR().getClass().getName(), 
						Color.BLACK));
				
				resultsMap.put(scrambled, 
						TestClassify.plotRocUsingMultithread(
							inArff, numPermutations, true, null, new RandomForest().getClass().getName(), 
								Color.RED));
			}	
			
			writeResults(taxa, resultsMap);
		}
		
	}	
}
