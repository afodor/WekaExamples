package projectDescriptors;

import java.io.File;
import java.util.HashSet;

import utils.ConfigReader;

public class China2015_Timepoint2 extends AbstractProjectDescription
{
	@Override
	public String getProjectName()
	{
		return "China2015_Time2";
	}
	
	@Override
	public String getArffIndiviudalFileFromRDP(String taxa) throws Exception
	{
		return ConfigReader.getMergedArffDir() + File.separator + 
				"China2015" + File.separator + 
				"pivoted_" + taxa + "asColumnsLogNormalPlusMetadata_second_B.arff";
	}
	
	@Override
	public String getLogNormalizedKrakenCounts(String taxa) throws Exception
	{
		return ConfigReader.getMergedArffDir() + File.separator +
				"China2015" + File.separator +  "kraken" + File.separator + 
				"China_2015_kraken_" + taxa +  "loggedWithMetadata_second_B.txt";
				
	}
	
	@Override
	public HashSet<String> getPositiveClassifications()
	{
		HashSet<String> set = new HashSet<String>();
		set.add("urban");
		return set;
	}
	
	@Override
	public HashSet<String> getNegativeClassifications()
	{
		HashSet<String> set = new HashSet<String>();
		set.add("rural");
		return set;
	}
}
