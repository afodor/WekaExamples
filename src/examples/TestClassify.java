package examples;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class TestClassify
{
	public static void main(String[] args) throws Exception
	{
		int numPermutations = 4;

		for( int x=1 ; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			System.out.println(NewRDPParserFileLine.TAXA_ARRAY[x]);
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getAdenomasWekaDir() + File.separator + 
						 NewRDPParserFileLine.TAXA_ARRAY[x] +"_Adenomas.txt" )));
			
			writer.write("ad1\tad2\n");
			
			File adenomas = new File("C:\\adenomasRelease\\spreadsheets\\pivoted_" + 
					NewRDPParserFileLine.TAXA_ARRAY[x] + 	"LogNormalWithMetadataBigSpace.arff");
			List<Double> firstARoc = getROCAreasForOneFile(adenomas, numPermutations);
			
			File ad2 = new File("C:\\tope_Sep_2015\\spreadsheets\\" + 
					NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumnsLogNormalPlusMetadataBigSpace.arff");
			
			List<Double> secondROC = getROCAreasForOneFile(ad2, numPermutations);;
			
			for(int y=0;y < firstARoc.size(); y++)
				writer.write(firstARoc.get(y) + "\t" + secondROC.get(y) + "\n");
			
			writer.flush();  writer.close();
		}
		
	}
	
	public static List<Double> getROCAreasForOneFile( File inFile, int numPermutations ) throws Exception
	{
		Random random = new Random(0);
		List<Double> rocAreas = new ArrayList<Double>();
		
		for( int x=0; x< numPermutations; x++)
		{
			Instances data = DataSource.read(inFile.getAbsolutePath());
			data.setClassIndex(data.numAttributes() -1);
			Evaluation ev = new Evaluation(data);
			AbstractClassifier rf = new RandomForest();
			
			//rf.buildClassifier(data);
			ev.crossValidateModel(rf, data, 10, random);
			//System.out.println(ev.toSummaryString("\nResults\n\n", false));
			System.out.println(x + " " + ev.areaUnderROC(0));
			rocAreas.add(ev.areaUnderPRC(0));
		}
		
		return rocAreas;
		
	}
}
