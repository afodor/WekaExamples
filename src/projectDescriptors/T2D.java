package projectDescriptors;

import java.io.File;
import java.util.HashSet;

import utils.ConfigReader;

public class T2D extends AbstractProjectDescription
{
	@Override
	public String getProjectName()
	{
		return "t2d";
	}
	
	@Override
	public String getArffIndiviudalFileFromRDP(String taxa) throws Exception
	{
		return null;
	}
	
	@Override
	public String getLogNormalizedKrakenCounts(String taxa) throws Exception
	{
		return ConfigReader.getMergedArffDir() + File.separator + 
				File.separator + "td2" + File.separator + 
					"t2d_minikraken_merged_lognorm_" + taxa + ".txt";
	}
	
	@Override
	public HashSet<String> getPositiveClassifications()
	{
		HashSet<String> set = new HashSet<String>();
		set.add("t2d"); set.add("T2D");
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
