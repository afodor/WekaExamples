package projectDescriptors;

import java.io.File;

import utils.ConfigReader;

public class Adenomas2015ProjectDescriptor extends AbstractProjectDescription
{
	@Override
	public String getProjectName()
	{
		return "Adenomas2012";
	}
	
	@Override
	public String getArffIndiviudalFileFromRDP(String taxa) throws Exception
	{
		return ConfigReader.getMergedArffDir() + File.separator + "Adenomas2012" 
						+ File.separator + "pivoted_" + taxa +  "LogNormalWithMetadata.arff";
	}
}
