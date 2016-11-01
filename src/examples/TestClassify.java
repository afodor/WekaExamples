package examples;

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
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class TestClassify
{
	public static void main(String[] args) throws Exception
	{
		Random random = new Random(0);
		int numPermutations = 50;

		for( int x=1 ; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			
			System.out.println(NewRDPParserFileLine.TAXA_ARRAY[x]);
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getAdenomasWekaDir() + File.separator + 
						 NewRDPParserFileLine.TAXA_ARRAY[x] +"_Adenomas.txt" )));
			
			writer.write("ad1\tad2\tcross\n");
			
			File adenomas = new File("C:\\adenomasRelease\\spreadsheets\\pivoted_" + 
					NewRDPParserFileLine.TAXA_ARRAY[x] + 	"LogNormalWithMetadataBigSpace.arff");
			List<Double> firstARoc = getPercentCorrectForOneFile(adenomas, numPermutations,random);
			
			File ad2 = new File("C:\\tope_Sep_2015\\spreadsheets\\" + 
					NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumnsLogNormalPlusMetadataBigSpace.arff");
			
			List<Double> secondROC = getPercentCorrectForOneFile(ad2, numPermutations,random);;
			
			Instances trainData = DataSource.read(ad2.getAbsolutePath());
			Instances testData = DataSource.read(adenomas.getAbsolutePath());
			trainData.setClassIndex(trainData.numAttributes() -1);
			testData.setClassIndex(testData.numAttributes() -1);
			
			List<Double> crossROC = 
						getRocForTrainingToTest(trainData, testData, random, numPermutations);
			
			for(int y=0;y < firstARoc.size(); y++)
				writer.write(firstARoc.get(y) + "\t" + secondROC.get(y) + "\t" + crossROC.get(y) + "\n");
			
			writer.flush();  writer.close();
		}
		
	}
	
	public static List<Double> getRocForTrainingToTest(Instances trainingData, Instances testData,
				Random random, int numIterations) throws Exception
	{

		List<Double> rocAreas = new ArrayList<Double>();
		
		for( int x=0; x < numIterations; x++)
		{

			Instances halfTrain = new Instances(trainingData, trainingData.size() / 2 );
			
			for(Instance i : trainingData)
				if( random.nextFloat() <= 0.5)
					halfTrain.add(i);
			
			System.out.println(halfTrain.size() + " " + trainingData.size());
			
			AbstractClassifier rf = new RandomForest();
			rf.buildClassifier(halfTrain);
			Evaluation ev = new Evaluation(halfTrain);
			ev.evaluateModel(rf, testData);
			System.out.println("cross " + x + " " + ev.areaUnderROC(0) + " " + ev.pctCorrect());
			rocAreas.add(ev.pctCorrect());
		}
		
		return rocAreas;
	}
	
	public static List<Double> getPercentCorrectForOneFile( File inFile, int numPermutations, Random random ) 
				throws Exception
	{
		List<Double> percentCorrect = new ArrayList<Double>();
		
		for( int x=0; x< numPermutations; x++)
		{
			Instances data = DataSource.read(inFile.getAbsolutePath());
			data.setClassIndex(data.numAttributes() -1);
			Evaluation ev = new Evaluation(data);
			AbstractClassifier rf = new RandomForest();
			
			//rf.buildClassifier(data);
			ev.crossValidateModel(rf, data, 10, random);
			//System.out.println(ev.toSummaryString("\nResults\n\n", false));
			//System.out.println(x + " " + ev.areaUnderROC(0) + " " + ev.pctCorrect());
			percentCorrect.add(ev.pctCorrect());
		}
		
		return percentCorrect;
		
	}
}
