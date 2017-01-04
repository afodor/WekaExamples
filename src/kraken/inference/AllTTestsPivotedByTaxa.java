package kraken.inference;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import kraken.RunAllClassifiers;
import kraken.inference.RunAllTTests.CaseControlHolder;
import kraken.inference.RunAllTTests.TTestResultsHolder;
import projectDescriptors.AbstractProjectDescription;
import utils.ConfigReader;

public class AllTTestsPivotedByTaxa
{
	public static void main(String[] args) throws Exception
	{
		List<AbstractProjectDescription> projects = RunAllClassifiers.getAllProjects();
		
		for( int x=0; x < RunAllClassifiers.TAXA_ARRAY.length; x++)
		{
			String taxa = RunAllClassifiers.TAXA_ARRAY[x];
			
			HashMap<String, HashMap<String,TTestResultsHolder>> 
				map = getAllTTests(projects, taxa);
			
			writePivot(map, projects, taxa);
		}
	}
	
	// outer string is projectID@method
	// inner string is taxa
	private static HashMap<String, HashMap<String,TTestResultsHolder>> 
		getAllTTests(List<AbstractProjectDescription> projects, String taxa) throws Exception
	{
		HashMap<String, HashMap<String,TTestResultsHolder>>   map = 
				new HashMap<String, HashMap<String,TTestResultsHolder>>();
		
		for(AbstractProjectDescription apd : projects)
		{
			addOne(apd, apd.getLogNormalizedKrakenCounts(taxa), taxa, map, AbstractProjectDescription.KRAKEN);
			addOne(apd, apd.getLogNormalizedRDPCounts(taxa), taxa, map, AbstractProjectDescription.RDP);
			addOne(apd, apd.getLogNormalizedClosedRefQiimeCounts(taxa), taxa, map, AbstractProjectDescription.QIIME_CLOSED);
		}
		
		return map;
	}
	
	private static void addOne(AbstractProjectDescription apd, String filepath, String taxa,
			HashMap<String, HashMap<String,TTestResultsHolder>>   map, 
			String classificationScheme) throws Exception
	{
		String key = apd.getProjectName() + "@" + classificationScheme;
		
		if(map.containsKey(key))
			throw new Exception("Duplicate " + key);
		
		if( filepath == null)
			return;
		
		File inFile = new File(filepath);
		
		if( !inFile.exists())
		{
			System.out.println("Could not find " + inFile.getAbsolutePath() + " skipping ");
			return;
		}
		
		HashMap<String, CaseControlHolder> ccMap = RunAllTTests.getCaseControlMap(apd, taxa, filepath);
		List<TTestResultsHolder> ttests = RunAllTTests.runTTests(ccMap);	
		HashMap<String, TTestResultsHolder> innerMap = new HashMap<String, TTestResultsHolder>();
		map.put(key, innerMap);
		
		for(TTestResultsHolder ttest : ttests)
			innerMap.put(ttest.taxaName, ttest);
	}
	
	private static List<String> getAllTaxaNames(
			 HashMap<String, HashMap<String,TTestResultsHolder>> map) throws Exception
	{
		HashSet<String> set = new HashSet<String>();
		
		for( HashMap<String,TTestResultsHolder> innerMap : map.values())
			set.addAll(innerMap.keySet());
		
		List<String> list = new ArrayList<String>(set);
		Collections.sort(list);
		
		return list;
	}
	
	private static void writePivot(HashMap<String, HashMap<String,TTestResultsHolder>> map,
			List<AbstractProjectDescription> projects, String taxa) 
				throws Exception
	{
		List<String> names = getAllTaxaNames(map);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(
					ConfigReader.getMergedArffDir() + File.separator 
					+ "allTTestsPivoted_" + taxa + ".txt"));
		
		writer.write("taxa");
		
		List<String> keys = new ArrayList<String>(map.keySet());
		Collections.sort(keys);
		
		for( String s : keys)
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for(String s : names)
		{
			writer.write(s);
			
			for( String key : keys)
			{
				HashMap<String,TTestResultsHolder> innerMap = map.get(key);
				TTestResultsHolder t = innerMap.get(s);
				
				if( t== null)
				{
					writer.write("\t");
				}
				else
				{
					double pValue = Math.log10(t.pValue);
					
					if( t.caseAverage > t.controlAverage)
						pValue = - pValue;
					
					writer.write("\t" + pValue);
				}
			}

			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
}
