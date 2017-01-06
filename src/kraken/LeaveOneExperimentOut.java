package kraken;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import projectDescriptors.AbstractProjectDescription;
import projectDescriptors.Adenomas2015ProjectDescriptor;
import projectDescriptors.CRCZeller;
import projectDescriptors.China2015_wgs;
import projectDescriptors.CirrhosisQin;
import projectDescriptors.Divitriculosis2015ProjectDescriptor;
import projectDescriptors.IbdMetaHit;
import projectDescriptors.Obesity;
import projectDescriptors.T2D;
import projectDescriptors.WT2D2;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class LeaveOneExperimentOut
{
	public static List<AbstractProjectDescription> getAllExperiments() throws Exception
	{
		List<AbstractProjectDescription> list = new ArrayList<AbstractProjectDescription>();
		list.add(new T2D());
		list.add(new WT2D2());
		
		list.add(new Divitriculosis2015ProjectDescriptor());
		list.add(new China2015_wgs());
		list.add(new Adenomas2015ProjectDescriptor());
		list.add( new CRCZeller());
		list.add( new CirrhosisQin());
		list.add( new IbdMetaHit());
		list.add( new Obesity());
		
		return list;
	}
	
	private static Instances getAllButOne(List<AbstractProjectDescription> list,
									AbstractProjectDescription one, String taxa) throws Exception
	{
		HashSet<String> skipSet=  new HashSet<String>();
		skipSet.add(one.getProjectName());
		
		boolean firstIsTheOne = false;
		
		if( list.get(0).getProjectName().equals(one.getProjectName()))
			firstIsTheOne = true;
		
		AbstractProjectDescription apb = firstIsTheOne ? list.get(1) : list.get(0);
		
		Instances data = DataSource.read( apb.getLogNormalizedArffFromKraken(taxa));
		skipSet.add(apb.getProjectName());
		
		for( AbstractProjectDescription apd : list )
		{
			if( ! skipSet.contains(one.getProjectName()))
			{
				File inFile = new File( apd.getLogNormalizedArffFromKraken(taxa));
				data.addAll(DataSource.read(inFile.getAbsolutePath()));	
			}
		}
		
		return data;
	}
	
	public static void main(String[] args) throws Exception
	{
		for( int x=0; x < RunAllClassifiers.TAXA_ARRAY.length; x++)
		{
			String taxa = RunAllClassifiers.TAXA_ARRAY[x];
			System.out.println(taxa);
			for(AbstractProjectDescription abd : getAllExperiments())
			{
				Instances data = getAllButOne(getAllExperiments(), abd, taxa);
			}
		}
	}
}
