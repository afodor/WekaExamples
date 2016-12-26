package kraken.inference;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


import kraken.RunAllClassifiers;
import projectDescriptors.AbstractProjectDescription;
import utils.Avevar;
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
		HashMap<String, CaseControlHolder> map = getCaseControlMap(apd, taxa,true);
		
		//System.out.println(map.size());
		//for( String s : map.keySet())
			//System.out.println(s + " " + map.get(s).caseVals.size() + " " +map.get(s).controlVals.size());
		
		List<TTestResultsHolder> ttests = runTTests(map);
		
		writeResults(apd, taxa, ttests);
		
	}
	
	private static void writeResults( AbstractProjectDescription apd, String taxa, 
			List<TTestResultsHolder> list) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				apd.getTTestResultsFilePath(taxa)
				)));
		
		writer.write("taxa\tpValue\tfdrPValue\ttValue\ttTestFailed\taverageCase\taverageControl\tcaseVals\tcontrolVals\n");
		
		for( int x=0; x < list.size(); x++)
		{
			TTestResultsHolder tTest = list.get(x);
			
			writer.write(tTest.taxaName + "\t");
			writer.write(tTest.pValue + "\t");
			writer.write( ( tTest.pValue * list.size() / (x+1) ) + "\t");
			writer.write( tTest.tValue + "\t");
			writer.write(tTest.threwException + "\t");
			writer.write(tTest.caseAverage + "\t");
			writer.write( tTest.controlAverage+ "\t");
			writer.write( tTest.cch.caseVals+ "\t");
			writer.write( tTest.cch.controlVals+ "\n");
		}
		
		writer.flush();  writer.close();
	}
	
	public static List<TTestResultsHolder> runTTests(HashMap<String, CaseControlHolder> map)  
	{
		List<TTestResultsHolder> list = new ArrayList<TTestResultsHolder>();
		
		for(String s : map.keySet())
		{
			TTestResultsHolder t = new TTestResultsHolder(s);
			list.add(t);
			CaseControlHolder cch = map.get(s);
			t.cch = cch;
			t.caseAverage = new Avevar(cch.caseVals).getAve();
			t.controlAverage = new Avevar(cch.controlVals).getAve();
			
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
	
	static class TTestResultsHolder implements Comparable<TTestResultsHolder>
	{
		final String taxaName;
		double pValue = 1;
		boolean threwException = false;
		double tValue;
		double caseAverage;
		double controlAverage;
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
		public List<Double> caseVals = new ArrayList<Double>();
		public List<Double> controlVals = new ArrayList<Double>();
	}
	
	public static HashMap<String, CaseControlHolder> getCaseControlMap( AbstractProjectDescription apd ,
			String taxa, boolean useKraken)
		throws Exception
	{
		HashMap<String, CaseControlHolder> map = new HashMap<String, CaseControlHolder>();
		
		BufferedReader reader = new BufferedReader(new FileReader(
			useKraken ?	apd.getLogNormalizedKrakenCounts(taxa) : apd.getLogNormalizedRDPCounts(taxa)));
		
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
