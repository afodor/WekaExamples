package projectDescriptors;

import java.io.File;

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

}
