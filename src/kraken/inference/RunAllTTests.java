package kraken.inference;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


import kraken.RunAllClassifiers;
import projectDescriptors.AbstractProjectDescription;
import utils.StatisticReturnObject;
import utils.TTest;

public class RunAllTTests
{
	public static void main(String[] args) throws Exception
	{
		for( int x=0; x < RunAllClassifiers.TAXA_ARRAY.length; x++)
			for( AbstractProjectDescription apd : RunAllClassifiers.getAllProjects() )
				runATTest(apd, RunAllClassifiers.TAXA_ARRAY[x]);
	}
	
	public static void runATTest(AbstractProjectDescription apd, String taxa) throws Exception
	{
		System.out.println(apd.getProjectName() + " " + taxa);
		HashMap<String, CaseControlHolder> map = getCaseControlMap(apd, taxa);
		
		//System.out.println(map.size());
		//for( String s : map.keySet())
			//System.out.println(s + " " + map.get(s).caseVals.size() + " " +map.get(s).controlVals.size());
		
		List<TTestResultsHolder> ttest = runTTests(map);
		System.out.println(ttest.size());
		
	}
	
	private static List<TTestResultsHolder> runTTests(HashMap<String, CaseControlHolder> map)  
	{
		List<TTestResultsHolder> list = new ArrayList<TTestResultsHolder>();
		
		for(String s : map.keySet())
		{
			TTestResultsHolder t = new TTestResultsHolder(s);
			list.add(t);
			CaseControlHolder cch = map.get(s);
			t.cch = cch;
			
			try
			{
				StatisticReturnObject sro = 
						TTest.ttestFromNumberUnequalVariance(cch.controlVals,cch.caseVals );
				
				t.pValue = sro.getPValue();
				t.tValue = sro.getScore();
				
				t.threwException = false;
			}
			catch(Exception ex)
			{
				t.threwException = true;
			}
		}
		
		Collections.sort(list);
		return list;
	}
	
	private static class TTestResultsHolder implements Comparable<TTestResultsHolder>
	{
		final String taxaName;
		double pValue = 1;
		boolean threwException = false;
		double tValue;
		CaseControlHolder cch;
		
		public TTestResultsHolder(String taxaName)
		{
			this.taxaName = taxaName;
		}
		
		@Override
		public int compareTo(TTestResultsHolder o)
		{
			return Double.compare(this.pValue, o.pValue);
		}
	}
	
	public static class CaseControlHolder 
	{
		List<Double> caseVals = new ArrayList<Double>();
		List<Double> controlVals = new ArrayList<Double>();
	}
	
	public static HashMap<String, CaseControlHolder> getCaseControlMap( AbstractProjectDescription apd ,
			String taxa)
		throws Exception
	{
		HashMap<String, CaseControlHolder> map = new HashMap<String, CaseControlHolder>();
		
		BufferedReader reader = new BufferedReader(new FileReader(
				apd.getLogNormalizedKrakenCounts(taxa)));
		
		String[] topLine = reader.readLine().split("\t");
		
		for( int i =2; i < topLine.length; i++)
		{
			String key = topLine[i];
			
			if( map.containsKey(key))
				throw new Exception("Duplicate key " + key);
			
			map.put(key, new CaseControlHolder());
			
		}
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != topLine.length)
				throw new Exception("Parsing error");
			
			for( int i=2; i < topLine.length; i++)
			{
				CaseControlHolder cch = map.get(topLine[i]);
				
				if( apd.getPositiveClassifications().contains(splits[1]) )
					cch.caseVals.add(Double.parseDouble(splits[i]));
				else if( apd.getNegativeClassifications().contains(splits[1]) )
					cch.controlVals.add(Double.parseDouble(splits[i]));
				else 
					System.out.println("Skipping " + splits[1]);
				
			}
		}
		
		return map;
	}
}
