package diverticulosis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;

import kraken.inference.RunAllTTests;
import parsers.NewRDPParserFileLine;
import projectDescriptors.AbstractProjectDescription;
import projectDescriptors.Divitriculosis2015ProjectDescriptor;
import utils.ConfigReader;

public class KrakenToRDPPValues
{
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length;x++)
		{
			String taxa = NewRDPParserFileLine.TAXA_ARRAY[x];
			System.out.println(taxa);

			AbstractProjectDescription apd = new Divitriculosis2015ProjectDescriptor();
			HashMap<String, RunAllTTests.CaseControlHolder> rdpMap =
					RunAllTTests.getCaseControlMap(apd, taxa, false);
			
			HashMap<String, RunAllTTests.CaseControlHolder> krakenMap =
					RunAllTTests.getCaseControlMap(apd, taxa, true);
			
		
			
		}
	}
	
}
