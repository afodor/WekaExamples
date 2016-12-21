package kraken.inference;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javafx.scene.control.Tab;
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
			
		}
	}
	
	// ok for AAbstractProjectDescription to be a key in the map
	// because we keep default .equals and .hashMap methods
	// so each key in the map is unique
	private static HashMap<AbstractProjectDescription, List<TTestResultsHolder>> 
		getAllTTests(List<AbstractProjectDescription> projects, String taxa) throws Exception
	{
		HashMap<AbstractProjectDescription, List<TTestResultsHolder>>  map = 
				new HashMap<AbstractProjectDescription,List<TTestResultsHolder>>();
		
		for(AbstractProjectDescription apd : projects)
		{
			HashMap<String, CaseControlHolder> ccMap = RunAllTTests.getCaseControlMap(apd, taxa);
			List<TTestResultsHolder> ttests = RunAllTTests.runTTests(ccMap);	
			map.put(apd, ttests);
		}
		
		return map;
	}
	
	private static List<String> getAllTaxaNames(
			HashMap<AbstractProjectDescription, List<TTestResultsHolder>> map) throws Exception
	{
		HashSet<String> set = new HashSet<String>();
		
		for( List<TTestResultsHolder> list : map.values())
			for( TTestResultsHolder t : list)
				set.add(t.taxaName);
		
		List<String> list = new ArrayList<String>(set);
		Collections.sort(list);
		
		return list;
	}
	
	private static void writePivot(HashMap<AbstractProjectDescription, List<TTestResultsHolder>> map,
			List<AbstractProjectDescription> projects, String taxa) 
				throws Exception
	{
		List<String> names = getAllTaxaNames(map);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(
					ConfigReader.getMergedArffDir() + File.separator 
					+ "allTTestsPivoted_" + taxa + ".txt"));
		
		writer.write(taxa);
		
		for( int x=0; x < projects.size(); x++)
			for( int y=0 ; y < projects.size(); y++)
				if( x != y)
					writer.write("\t" + projects.get(x).getProjectName() 
							+ "@" + projects.get(y).getProjectName());
		
		writer.write("\n");
		
		for(String s : names)
		{
			for( int x=0; x < projects.size(); x++)
				for( int y=0 ; y < projects.size(); y++)
					if( x != y)
					{
							
					}
		}
		
		writer.flush();  writer.close();
		
	}
}
