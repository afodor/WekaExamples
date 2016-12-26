package projectDescriptors;

import java.io.File;
import java.util.HashSet;

import utils.ConfigReader;

public class China2015_Timepoint1 extends AbstractProjectDescription
{
	@Override
	public String getProjectName()
	{
		return "China2015_Time1";
	}
	
	@Override
	public String getLogNormalizedRDPCounts(String taxa) throws Exception
	{
		return ConfigReader.getMergedArffDir() + File.separator +
				"China2015" + File.separator +  "rdp" + File.separator + 
				"China_2015_kraken_" + taxa +  "loggedWithMetadataRDP_first_A.txt";
	}
	
	@Override
	public String getArffIndiviudalFileFromRDP(String taxa) throws Exception
	{
		return ConfigReader.getMergedArffDir() + File.separator + 
				"China2015" + File.separator + 
				"pivoted_" + taxa + "asColumnsLogNormalPlusMetadata_first_A.arff";
	}
	
	@Override
	public String getLogNormalizedKrakenCounts(String taxa) throws Exception
	{
		return ConfigReader.getMergedArffDir() + File.separator +
				"China2015" + File.separator +  "kraken" + File.separator + 
				"China_2015_kraken_" + taxa +  "loggedWithMetadata_first_A.txt";
				
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
