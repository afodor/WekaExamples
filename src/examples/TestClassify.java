package examples;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class TestClassify
{
	public static void main(String[] args) throws Exception
	{
		int numPermutations = 20;
		File adenomas = new File("C:\\adenomasRelease\\spreadsheets\\pivoted_genusLogNormalWithMetadataBigSpace.arff");
		List<Double> firstARoc = getROCAreasForOneFile(adenomas, numPermutations);
		
		for( Double d : firstARoc)
			System.out.println(d);
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
