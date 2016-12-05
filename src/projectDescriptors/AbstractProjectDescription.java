package projectDescriptors;

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
}
