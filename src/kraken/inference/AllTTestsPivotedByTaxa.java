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
			
			HashMap<AbstractProjectDescription, HashMap<String,TTestResultsHolder>> 
				map = getAllTTests(projects, taxa);
			
			writePivot(map, projects, taxa);
		}
	}
	
	// ok for AAbstractProjectDescription to be a key in the map
	// because we keep default .equals and .hashMap methods
	// so each key in the map is unique
	// inner map is with taxa as key
	private static HashMap<AbstractProjectDescription, HashMap<String,TTestResultsHolder>> 
		getAllTTests(List<AbstractProjectDescription> projects, String taxa) throws Exception
	{
		HashMap<AbstractProjectDescription, HashMap<String,TTestResultsHolder>>   map = 
				new HashMap<AbstractProjectDescription, HashMap<String,TTestResultsHolder>>();
		
		for(AbstractProjectDescription apd : projects)
		{
			HashMap<String, CaseControlHolder> ccMap = RunAllTTests.getCaseControlMap(apd, taxa,true);
			List<TTestResultsHolder> ttests = RunAllTTests.runTTests(ccMap);	
			HashMap<String, TTestResultsHolder> innerMap = new HashMap<String, TTestResultsHolder>();
			map.put(apd, innerMap);
			
			for(TTestResultsHolder ttest : ttests)
				innerMap.put(ttest.taxaName, ttest);
		}
		
		return map;
	}
	
	private static List<String> getAllTaxaNames(
			 HashMap<AbstractProjectDescription, HashMap<String,TTestResultsHolder>> map) throws Exception
	{
		HashSet<String> set = new HashSet<String>();
		
		for( HashMap<String,TTestResultsHolder> innerMap : map.values())
			set.addAll(innerMap.keySet());
		
		List<String> list = new ArrayList<String>(set);
		Collections.sort(list);
		
		return list;
	}
	
	private static void writePivot(HashMap<AbstractProjectDescription, 
					HashMap<String,TTestResultsHolder>> map,
			List<AbstractProjectDescription> projects, String taxa) 
				throws Exception
	{
		List<String> names = getAllTaxaNames(map);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(
					ConfigReader.getMergedArffDir() + File.separator 
					+ "allTTestsPivoted_" + taxa + ".txt"));
		
		writer.write("taxa");
		
		for( int x=0; x < projects.size(); x++)
					writer.write("\t" + projects.get(x).getProjectName());
		
		writer.write("\n");
		
		for(String s : names)
		{
			writer.write(s);
			
			for( int x=0; x < projects.size(); x++)
			{
				HashMap<String,TTestResultsHolder> innerMap = map.get(projects.get(x));
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
