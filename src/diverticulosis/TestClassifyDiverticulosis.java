package diverticulosis;

import java.awt.Color;
import java.io.File;
import java.util.List;

import examples.TestClassify;
import metaMergers.BringIntoOneNameSpace;
import parsers.NewRDPParserFileLine;
import projectDescriptors.AbstractProjectDescription;
import weka.classifiers.trees.RandomForest;
import weka.gui.visualize.ThresholdVisualizePanel;

public class TestClassifyDiverticulosis
{
	public static void main(String[] args) throws Exception
	{
		int numPermutations = 50;
		List<AbstractProjectDescription> projects = BringIntoOneNameSpace.getAllProjects();
		
		for( int x=1 ; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String taxa = NewRDPParserFileLine.TAXA_ARRAY[x];
			for( AbstractProjectDescription apd : projects)
			{
				File inArff= new File(apd.getArffMergedFileFromRDP(taxa));

				ThresholdVisualizePanel tvp = TestClassify.getVisPanel(
					apd.getProjectName() + " " + taxa	);
				TestClassify.plotRocUsingMultithread(
						inArff, numPermutations, false, tvp, new RandomForest().getClass().getName(), 
						Color.BLACK);
				TestClassify.plotRocUsingMultithread(
						inArff, numPermutations, true, tvp, new RandomForest().getClass().getName(), 
						Color.RED);

			}				
		}
		
	}	
}
