package projectDescriptors;

import java.io.File;
import java.util.HashSet;

import utils.ConfigReader;

public class Divitriculosis2015ProjectDescriptor extends AbstractProjectDescription
{
	@Override
	public String getProjectName()
	{
		return "Divitriculosis2015";
	}
	
	@Override
	public String getArffIndiviudalFileFromRDP(String taxa) throws Exception
	{
		return ConfigReader.getMergedArffDir() + File.separator + "Diverticulosis2015" 
						+ File.separator + 
						"pivoted_" + taxa + "asColumnsLogNormalPlusMetadata.arff";
		 
	}
	
	@Override
	public String getLogNormalizedRDPCounts(String taxa) throws Exception
	{
		return ConfigReader.getMergedArffDir()+ File.separator + 
				"Diverticulosis2015" + File.separator + "rdp" +  
				taxa+ "_asColumnsLogNormalPlusMetadataCaseContol.txt";
	}
	
	public String getLogNormalizedKrakenCounts(String taxa) throws Exception
	{
		return ConfigReader.getMergedArffDir() + File.separator + "Diverticulosis2015" + 
					File.separator + "kraken" + File.separator + 
					"pivoted_diverticulosis_merged_kraken_" + taxa + "LogNormalPlusMetadata.txt";
	}
	
	@Override
	public String getLogNormalizedClosedRefQiimeCounts(String taxa) throws Exception
	{
		return ConfigReader.getMergedArffDir() + File.separator + "Diverticulosis2015" + 
				File.separator + "qiimeClosed" + File.separator + 
				"diverticulosis_closed_" + taxa + "_AsColumnsLogNormalWithMetadata.txt";
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
