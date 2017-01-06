package projectDescriptors;

import java.io.File;
import java.util.HashSet;

import utils.ConfigReader;

public class Hmp_wgs extends AbstractProjectDescription
{
	@Override
	public String getProjectName()
	{
		return "Hmp_wgs";
	}
	
	@Override
	public String getArffIndiviudalFileFromRDP(String taxa) throws Exception
	{
		return null;
	}
	
	@Override
	public String getLogNormalizedKrakenCounts(String taxa) throws Exception
	{
		return ConfigReader.getMergedArffDir() +
				File.separator + "hmp" + File.separator + 
				"hmp_minikraken_merged_lognorm_" + taxa +  ".txt";
	}
	
	@Override
	public HashSet<String> getPositiveClassifications()
	{
		HashSet<String> set = new HashSet<String>();
		return set;
	}
	
	@Override
	public HashSet<String> getNegativeClassifications()
	{
		HashSet<String> set = new HashSet<String>();
		set.add("n"); set.add("N");
		return set;
	}
}
