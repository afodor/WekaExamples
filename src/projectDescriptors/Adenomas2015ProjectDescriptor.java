package projectDescriptors;

import java.io.File;
import java.util.HashSet;

import utils.ConfigReader;

public class Adenomas2015ProjectDescriptor extends AbstractProjectDescription
{
	
	
	@Override
	public String getProjectName()
	{
		return "Adenomas2015";
	}
	
	@Override
	public String getArffIndiviudalFileFromRDP(String taxa) throws Exception
	{
		return ConfigReader.getMergedArffDir() + File.separator + "Adenomas2015" 
						+ File.separator + taxa +
						"asColumnsLogNormalPlusMetadataFilteredCaseControl.arff";
	}
	
	@Override
	public String getLogNormalizedKrakenCounts(String taxa) throws Exception
	{
		return ConfigReader.getMergedArffDir() + File.separator + "Adenomas2015" + 
					File.separator + "kraken" + File.separator + 
					"kraken_"  + taxa + "logNormalPlusMetadata.txt";
	}
	
	@Override
	public HashSet<String> getPositiveClassifications()
	{
		HashSet<String> set = new HashSet<String>();
		set.add("1");
		return set;
	}
	
	@Override
	public HashSet<String> getNegativeClassifications()
	{
		HashSet<String> set = new HashSet<String>();
		set.add("0");
		return set;
	}
}
