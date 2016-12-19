package projectDescriptors;

import java.io.File;
import java.util.HashSet;

import utils.ConfigReader;

public class CRCZeller extends AbstractProjectDescription
{

	@Override
	public String getProjectName()
	{
		return "CRCZeller";
	}

	@Override
	public String getArffIndiviudalFileFromRDP(String taxa) throws Exception
	{
		return null;
	}
	
	@Override
	public String getLogNormalizedKrakenCounts(String taxa) throws Exception
	{
		return ConfigReader.getMergedArffDir() + File.separator + "kraken" + File.separator + 
						"crc_Zeller" + File.separator + "colorectal_minikraken_merged_lognorm_" + taxa + ".txt";
		
	}
	
	@Override
	public  HashSet<String> getPositiveClassifications()
	{
		HashSet<String> set = new HashSet<String>();
		set.add("cancer");
		set.add("CRC");
		return set;
	}
	
	@Override
	public HashSet<String> getNegativeClassifications()
	{
		HashSet<String> set = new HashSet<String>();
		set.add("n");
		return set;
	}
	
}
