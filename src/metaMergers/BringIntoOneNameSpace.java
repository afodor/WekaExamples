package metaMergers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import parsers.NewRDPParserFileLine;
import projectDescriptors.AbstractProjectDescription;
import projectDescriptors.Adenomas2012ProjectDescriptor;
import projectDescriptors.Adenomas2015ProjectDescriptor;
import projectDescriptors.Divitriculosis2015ProjectDescriptor;

public class BringIntoOneNameSpace
{
	public static List<AbstractProjectDescription> getAllProjects() throws Exception
	{
		List<AbstractProjectDescription> list = new ArrayList<AbstractProjectDescription>();
		
		list.add(new Adenomas2012ProjectDescriptor());
		list.add(new Adenomas2015ProjectDescriptor());

		list.add(new Divitriculosis2015ProjectDescriptor());
		
		return list;
	}
	
	public static void main(String[] args) throws Exception
	{
		List<AbstractProjectDescription> projectList = getAllProjects();
		
		
		for(int x=1;x  < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
			writeMergedForOneLevel(projectList, NewRDPParserFileLine.TAXA_ARRAY[x]);
	}
	
	private static void writeMergedForOneLevel( List<AbstractProjectDescription> projects, String taxa)
		throws Exception
	{
		
		List<String> allNumeric = getAllNumericAttributes(projects, taxa);
		
		for(String s : allNumeric)
			System.out.println(s);
	}
	
	private static List<String> getAllNumericAttributes( List<AbstractProjectDescription> projects,
				String level) throws Exception
	{
		HashSet<String> set = new HashSet<String>();
		
		for(AbstractProjectDescription abd : projects)
		{
			set.addAll(getNumericAttributes( new File( abd.getArffIndiviudalFileFromRDP(level))));
		}
		
		List<String> list = new ArrayList<String>(set);
		Collections.sort(list);
		return list;
	}
	
	private static List<String> getAttributes(File inFile) throws Exception
	{
		List<String> list = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		for( String s= reader.readLine(); s != null; s= reader.readLine())
		{
			if( s.startsWith("@attribute"))
				list.add(s);
		}
		
		reader.close();
		
		return list;
	}
	
	private static List<String> getNumericAttributes(File inFile) throws Exception
	{
		List<String> allAttributes = getAttributes(inFile);
		List<String> numericAttributes = new ArrayList<String>();
		
		for( String s : allAttributes)
		{
			s  = s.trim();
			if( s.endsWith("numeric"))
			{
				StringTokenizer sToken = new StringTokenizer(s);
				
				if( sToken.countTokens() != 3)
					throw new Exception("Unexpected line " + s + " " + inFile.getAbsolutePath());
				
				sToken.nextToken();
				numericAttributes.add(new String(sToken.nextToken()));
			}
		}
		
		return numericAttributes;
	}
}
