package projectDescriptors;

import java.io.File;

import utils.ConfigReader;

public class China2015_Timepoint1 extends AbstractProjectDescription
{
	@Override
	public String getProjectName()
	{
		return "China2015_Time1";
	}
	
	@Override
	public String getArffIndiviudalFileFromRDP(String taxa) throws Exception
	{
		return ConfigReader.getMergedArffDir() + File.separator + 
				"China2015" + File.separator + 
				"pivoted_" + taxa + "asColumnsLogNormalPlusMetadata_first_A.arff";
	}
}
