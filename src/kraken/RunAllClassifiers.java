package kraken;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import examples.TestClassify;
import projectDescriptors.AbstractProjectDescription;
import projectDescriptors.IbdMetaHit;
import utils.ConfigReader;
import weka.classifiers.rules.OneR;
import weka.classifiers.trees.RandomForest;
import weka.gui.visualize.ThresholdVisualizePanel;

public class RunAllClassifiers
{
	public static String[] TAXA_ARRAY
		= { "domain", "phylum", "class", "order", "family", "genus", "species"};
	
	
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
	
	public static List<AbstractProjectDescription> getAllProjects() throws Exception
	{
		List<AbstractProjectDescription> list = new ArrayList<AbstractProjectDescription>();
		
		list.add( new IbdMetaHit());
		
		return list;
	}
	
	public static void main(String[] args) throws Exception
	{
		int numPermutations = 50;
		List<AbstractProjectDescription> projects = getAllProjects();
		
		for( int x=1 ; x < TAXA_ARRAY.length; x++)
		{

			HashMap<String, List<Double>> resultsMap = new LinkedHashMap<String,List<Double>>();
			
			String taxa = TAXA_ARRAY[x];
			for( AbstractProjectDescription apd : projects)
			{
				File inArff= new File(apd.getLogNormalizedArffFromKraken(taxa));

				ThresholdVisualizePanel tvp = TestClassify.getVisPanel(
					apd.getProjectName() + " " + taxa	);
				
				String unScrambled = apd.getProjectName();
				String scrambled= apd.getProjectName() + "_" +"_scrambled";
				String oneR = apd.getProjectName() + "_" +"_oneR";
				
				if( resultsMap.containsKey(unScrambled) || resultsMap.containsKey(scrambled)
							|| resultsMap.containsKey(oneR))
					throw new Exception("duplicate");
				
				resultsMap.put(unScrambled, 
				TestClassify.plotRocUsingMultithread(
					inArff, numPermutations, false, tvp, new RandomForest().getClass().getName(), 
						Color.BLACK));
				
				resultsMap.put(oneR, 
				TestClassify.plotRocUsingMultithread(
					inArff, numPermutations, true, tvp, new OneR().getClass().getName(), 
						Color.GREEN));
				
				resultsMap.put(scrambled, 
						TestClassify.plotRocUsingMultithread(
							inArff, numPermutations, true, tvp, new RandomForest().getClass().getName(), 
								Color.RED));
			}	
			
			writeResults(taxa, resultsMap);
		}
		
	}	
}
