package metaMergers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class GatherResultsEachClassifier
{
	
	public static void main(String[] args) throws Exception
	{
		for(int x=1; x< NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String taxa = NewRDPParserFileLine.TAXA_ARRAY[x];
			
			HashMap<String, Holder> map =getResultsForALevel(taxa);
			writeResults(map, taxa);
			writeSkinnyColumns(map, taxa);
		}
	}
	
	private static class Holder
	{
		List<Double> notScrambled = new ArrayList<Double>();
		List<Double> scrambled = new ArrayList<Double>();
	}
	
	private static void writeSkinnyColumns( HashMap<String, Holder> map, String level) throws Exception
	{
		
		List<String> list = new ArrayList<String>(map.keySet());
		Collections.sort(list);
		
		if( list.size() > 0 ) for(String s : list)
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter( ConfigReader.getMergedArffDir()
					+ File.separator + 
					"spreasheetsLocal" + File.separator + "allClassifiers_" + level + "_skinny.txt"));
			
			writer.write("dataset\tclassifier\tscrambed\troc\n");
			
			
			Holder h = map.get(s);
			
			while(s.endsWith("_"))
				s= s.substring(0, s.length()-1);
			
			int index = s.lastIndexOf("_");
			String dataset = s.substring(0, index);
			String classifier = s.substring(index, s.length());
			
			while(dataset.endsWith("_"))
				dataset= dataset.substring(0, dataset.length()-1);
			
			for( int x=0;x < RunAllClassifiersVsAllDataLocal.NUM_PERMUTATIONS; x++)
			{
				writer.write(dataset + "\t" + classifier + "\t" + "false\t" +  h.notScrambled.get(x) + "\n");
				writer.write(dataset + "\t" + classifier + "\t" + "true\t" + h.scrambled.get(x) + "\n");
			}
			
			writer.flush();  writer.close();
		}
	}
	
	private static void writeResults( HashMap<String, Holder> map, String level) throws Exception
	{
		
		List<String> list = new ArrayList<String>(map.keySet());
		Collections.sort(list);
		
		if(list.size() > 0)
		{

			BufferedWriter writer = new BufferedWriter(new FileWriter( ConfigReader.getMergedArffDir()
					+ File.separator + 
					"spreasheetsLocal" + File.separator + "allClassifiers_" + level + ".txt"));
			
			writer.write("index");
			
			for( String s : list)
				writer.write("\t" + s + "\t" + s + "_scrambled");
			
			writer.write("\n");
			
			int length = map.get(list.get(0)).scrambled.size();
			
			for( int x=0; x < length; x++)
			{
				writer.write("" + (x+1));
				
				for(String s : list)
					writer.write("\t" + map.get(s).notScrambled.get(x) + "\t" + map.get(s).scrambled.get(x));
				
				writer.write("\n");
			}
			
			writer.flush();  writer.close();
		}
	}
	
	private static int getNumLines(File file ) throws Exception
	{
		int i =0;
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		for(String s= reader.readLine();s != null; s= reader.readLine())
			i++;
		
		reader.close();
		
		return i;
	}
	
	private static HashMap<String, Holder> getResultsForALevel(String level) throws Exception
	{
		HashMap<String, Holder>  map = new HashMap<String,Holder>();
		
		File outputDir = new File(ConfigReader.getMergedArffDir() + File.separator + 
						"allVsallMerged");
		String[] files = outputDir.list();
		
		for( String s : files)
		{
			if( s.indexOf(level + ".txt") != -1)
			{
				
				File file = new File(outputDir.getAbsolutePath() + File.separator + s);
				
				if( getNumLines(file)!= RunAllClassifiersVsAllDataLocal.NUM_PERMUTATIONS+ 1)
				{
					System.out.println("Expecting " +  (RunAllClassifiersVsAllDataLocal.NUM_PERMUTATIONS+ 1) + 
							" but got "  + getNumLines(file) + " skipping " + file.getAbsolutePath() );
				}
				else
				{
					BufferedReader reader = new BufferedReader(new FileReader(file));
					
					s = s.replace(level + ".txt", "").replace("projectDescriptors.", "")
							.replace("ProjectDescriptor", "").replace("weka.classifiers.meta.", "").
							replace("weka.classifiers.rules.", "").replace("weka.classifiers.trees.", "")
							.replace("weka.classifiers.bayes.", "").replace("weka.classifiers.functions.", "");
					
					if(map.containsKey(s))
						throw new Exception("Duplicate key " + s);
					
					Holder h =new Holder();
					map.put(s,h);
					
					reader.readLine();
					
					for( String s2 = reader.readLine(); s2 != null; s2= reader.readLine())
					{
						String[] splits =s2.split("\t");
						
						if( splits.length != 2)
							throw new Exception("Parsing error");
						
						h.notScrambled.add(Double.parseDouble(splits[0]));
						h.scrambled.add(Double.parseDouble(splits[1]));
					}
					
					reader.close();			
				}
			}
		}
		
		return map;
		
	}
}
