package services;

import java.io.IOException;

import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Layout;
import tech.tablesaw.plotly.traces.HistogramTrace;

public class DataAnalysis {
	
	public static void main(String[] args)
	{
//		System.out.println("data Analysis");
		
		try {

			Table Test = Table.read().csv("src/main/java/services/Test.arff");

//			System.out.println(bank_data.shape());
//			
//			System.out.println(bank_data.first(7));
//			System.out.println(bank_data.last(7));
			
			System.out.println(Test.structure());
			
			System.out.println(Test.summary());
			
////		Histogram
			Layout layout1 = Layout.builder().title("Distribution of GENDER").build();
			HistogramTrace trace1 = HistogramTrace.builder(Test.nCol("Gender").asDoubleArray())
					.build();
			Plot.show(new Figure(layout1, trace1));




		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
