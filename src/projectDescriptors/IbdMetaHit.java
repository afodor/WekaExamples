package projectDescriptors;

import java.io.File;

import utils.ConfigReader;

public class IbdMetaHit extends AbstractProjectDescription
{

	@Override
	public String getProjectName()
	{
		return "IbdMetaHit";
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
						"ibd_Metahit" + File.separator + "ibd_minikraken_merged_lognorm_" + taxa + ".txt";
		
	}
	
}
