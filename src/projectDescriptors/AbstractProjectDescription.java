package projectDescriptors;

import java.io.File;
import java.util.HashSet;

import utils.ConfigReader;

public abstract class AbstractProjectDescription
{
	public static final String KRAKEN = "KRAKEN";
	public static final String RDP = "RDP";
	
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
	
	public String getTTestResultsFilePath(String taxa, String classificationScheme) throws Exception
	{
		return ConfigReader.getMergedArffDir() + File.separator + 
					"ttests" + File.separator + this.getProjectName() + "_" + taxa 
					+ "_ttests_" + classificationScheme +  ".txt";
	}
	
	public String getLogNormalizedKrakenCounts(String taxa) throws Exception
	{
		return null;
	}
	
	public String getLogNormalizedRDPCounts(String taxa) throws Exception
	{
		return null;
	}
	
	public String getLogNormalizedClosedRefQiimeCounts(String taxa) throws Exception
	{
		return null;
	}
	
	public String getLogNormalizedArffFromKraken(String taxa) throws Exception
	{
		String baseFile = getLogNormalizedKrakenCounts(taxa); 
		baseFile = baseFile.substring(0, baseFile.lastIndexOf(".txt") );
		return baseFile + "krakenLogNorm.arff";
	}
	
	public String getLogNormalizedArffFromKrakenMergedNamedspace(String taxa) throws Exception
	{
		String baseFile = getLogNormalizedArffFromKraken(taxa);
		baseFile = baseFile.substring(0, baseFile.lastIndexOf("krakenLogNorm.arff") );
		return baseFile + "krakenLogNormMergedNamespace.arff";
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
