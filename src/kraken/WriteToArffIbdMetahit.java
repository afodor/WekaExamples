package kraken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import projectDescriptors.IbdMetaHit;

public class WriteToArffIbdMetahit
{
	private static int getNumSamples(File file) throws Exception
	{
		int count = 0;
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		reader.readLine();
		
		for( String s = reader.readLine(); s != null; s= reader.readLine())
		{
			count++;
		}
		
		reader.close();
		
		return count;
	}
	
	
	private static void write(File inFile, File outFile, String taxa)
		throws Exception
	{
		System.out.println(inFile.getAbsolutePath());
		int numSamples = getNumSamples(inFile);
		System.out.println("Got" + numSamples);
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write("% " + taxa + "\n");
		
		writer.write("@relation " +  "metahit_IBD"+  "\n");
		
		String[] topSplits = reader.readLine().replaceAll("\"","").split("\t");
		
		for( int y=0; y < topSplits.length; y++)
		{
			if( y >= 2)
			writer.write("@attribute " + 
					topSplits[y].replaceAll(" ", "_").replaceAll("shannonEntropy", "shannonDiversity") 
							+ " numeric\n");
		}
		
		writer.write("@attribute isCase { true, false }\n");
		
		writer.write("\n\n@data\n");
		
		writer.write("%\n% " + numSamples + " instances\n%\n");
		
		for( String s= reader.readLine(); s != null; s = reader.readLine())
		{
			s = s.replaceAll("\"", "");
			String[] splits = s.split("\t");
			
			if( splits.length != topSplits.length)
				throw new Exception("Parsing error!");
			
			
			for( int y=0; y < splits.length; y++)
			{
				if( y>=2 )
					writer.write( splits[y] + ",");
			}
		
			if( splits[1].equals("n") )
				writer.write("false\n");
			else if( splits[1].equals("ibd_ulcerative_colitis") || splits[1].equals("ibd_crohn_disease"))
				writer.write("true\n");
			else throw new Exception("Parsing error " + splits[1]);
		}
		
		writer.flush();  writer.close();
		
		reader.close();
		
	}
	
	public static void main(String[] args) throws Exception
	{
		
		IbdMetaHit mh = new IbdMetaHit();
		
		for( int x=0; x < RunAllClassifiers.TAXA_ARRAY.length; x++)
		{
			String taxa = RunAllClassifiers.TAXA_ARRAY[x];
			
			System.out.println(taxa);
			
			File inFile = new File(mh.getLogNormalizedKrakenCounts(taxa));
			File outFile = new File(mh.getLogNormalizedArffFromKraken(taxa));
			
			write(inFile, outFile, taxa);
		}
	}
}
