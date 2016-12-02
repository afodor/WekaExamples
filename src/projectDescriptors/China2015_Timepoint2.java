package projectDescriptors;

import java.io.File;

import utils.ConfigReader;

public class China2015_Timepoint2 extends AbstractProjectDescription
{
	@Override
	public String getProjectName()
	{
		return "China2015_Time2";
	}
	
	@Override
	public String getArffIndiviudalFileFromRDP(String taxa) throws Exception
	{
		return ConfigReader.getMergedArffDir() + File.separator + 
				"China2015" + File.separator + 
				"pivoted_" + taxa + "asColumnsLogNormalPlusMetadata_second_B.arff";
	}
}
