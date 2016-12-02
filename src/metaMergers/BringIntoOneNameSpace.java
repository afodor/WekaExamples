package metaMergers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import parsers.NewRDPParserFileLine;
import projectDescriptors.AbstractProjectDescription;
import projectDescriptors.Adenomas2012ProjectDescriptor;
import projectDescriptors.Adenomas2015ProjectDescriptor;
import projectDescriptors.China2015_Timepoint1;
import projectDescriptors.China2015_Timepoint2;
import projectDescriptors.Divitriculosis2015ProjectDescriptor;

public class BringIntoOneNameSpace
{
	public static List<AbstractProjectDescription> getAllProjects() throws Exception
	{
		List<AbstractProjectDescription> list = new ArrayList<AbstractProjectDescription>();
		
		list.add(new China2015_Timepoint1());
		list.add(new China2015_Timepoint2());
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
	{;
		List<String> allNumeric = getAllNumericAttributes(projects, taxa);
		
		HashMap<String, Integer> positionMap = new HashMap<String,Integer>();
		for(int x=0;x  < allNumeric.size(); x++)
		{
			if(positionMap.containsKey(allNumeric.get(x)))
				throw new Exception("Logic error");
			
			positionMap.put(allNumeric.get(x), x);
		}
		
		for(AbstractProjectDescription apd : projects)
		{
			System.out.println(apd.getProjectName() + " " + taxa);
			List<String> numericAttributes = getNumericAttributes(
				new File(apd.getArffIndiviudalFileFromRDP(taxa)));
			
			HashMap<String, Integer> thisPositionMap = new HashMap<String,Integer>();
			
			for(int x=0;x  < numericAttributes.size(); x++)
			{
				if(thisPositionMap.containsKey(numericAttributes.get(x)))
					throw new Exception("Parsing error duplicate attibute " +numericAttributes.get(x) );
				
				thisPositionMap.put(numericAttributes.get(x), x);
			}
			
			HashMap<Integer, String> flipMap = new HashMap<Integer,String>();
			
			for(String s : thisPositionMap.keySet())
				flipMap.put(thisPositionMap.get(s), s);
			
			BufferedReader reader = new BufferedReader(new FileReader(new File(
				apd.getArffIndiviudalFileFromRDP(taxa)	)));
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					apd.getArffMergedFileFromRDP(taxa))));
			
			writer.write(reader.readLine() + "\n");  // header comment line
			writer.write(reader.readLine() + "\n");  // @relation
			
			for( int x=0; x < allNumeric.size(); x++)
				writer.write("@attribute " + allNumeric.get(x) + " numeric\n");
			
			writer.write("@attribute isCase { true, false }\n");
			
			writer.write("\n\n");
			
			String nextLine = reader.readLine();
			
			while(! nextLine.startsWith("@data"))
				nextLine = reader.readLine();
			
			writer.write(nextLine + "\n");
			writer.write(reader.readLine() + "\n");  // %
			writer.write(reader.readLine() + "\n");  // number of instances
			writer.write(reader.readLine() + "\n");  //
			
			for(String s = reader.readLine(); s != null; s= reader.readLine())
			{
				writer.write(getNewLine(s, flipMap, positionMap));
			}
			
			writer.flush(); writer.close();
			reader.close();
		}
	}
	

	private static String getNewLine(String oldLine, HashMap<Integer, String> flipMap,
						HashMap<String, Integer> newPositionMap) throws Exception
	{
		double[] vals = new double[newPositionMap.size()];
		
		String[] splits = oldLine.split(",");
		
		if( splits.length -1 != flipMap.size())
			throw new Exception("Parsing error");
		
		for( int x=0; x < splits.length - 1; x++)
		{
			String key = flipMap.get(x);
			Integer newPosition = newPositionMap.get(key);
			
			if( newPosition == null)
				throw new Exception("Could not find " + key);
			
			vals[newPosition] = Double.parseDouble(splits[x]);
			
		}
		
		StringBuffer buff = new StringBuffer();
		
		for( int x=0; x < vals.length; x++)
			buff.append(vals[x] + ",");
		
		buff.append(splits[splits.length-1]  + "\n");
		
		return buff.toString();
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
