package projectDescriptors;

import java.util.HashSet;

public abstract class AbstractProjectDescription
{
	abstract public String getProjectName();
	
	// last attribute should be @attribute isCase { true, false }
	// should end in .arff
	abstract public String getArffIndiviudalFileFromRDP(String taxa) throws Exception;
	
	
	public String getArffMergedFileFromRDP(String taxa) throws Exception
	{
		
		String baseFile = getArffIndiviudalFileFromRDP(taxa);
		baseFile = baseFile.substring(0, baseFile.lastIndexOf(".arff") );
		return baseFile + "allMerged.arff";
	}
	
	public String getLogNormalizedKrakenCounts(String taxa) throws Exception
	{
		return null;
	}
	
	public String getLogNormalizedArffFromKraken(String taxa) throws Exception
	{
		String baseFile = getLogNormalizedKrakenCounts(taxa);
		baseFile = baseFile.substring(0, baseFile.lastIndexOf(".txt") );
		return baseFile + "krakenLogNorm.arff";
	}
	
	public HashSet<String> getPositiveClassifications()
	{
		return null;
	}
	public HashSet<String> getNegativeClassifications()
	{
		return null;
	}
	
}
