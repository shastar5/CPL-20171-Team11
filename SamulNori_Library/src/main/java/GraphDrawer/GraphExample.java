package GraphDrawer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
 
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.category.AbstractCategoryItemRenderer;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.StandardGradientPaintTransformer;
import org.jfree.ui.TextAnchor;

class Graph {
	private JFreeChart graph;
	private ChartFrame frame;
	private int height = 0, width = 0;
	
	private CategoryPlot plot;
	private DefaultCategoryDataset[] ds;
	private DefaultCategoryDataset ds2 = new DefaultCategoryDataset();
	int DataSetCount = 0;
	private String DataName;
	private LineAndShapeRenderer renderer;
	
    // ���� �ɼ� ����
    final CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator();
    final ItemLabelPosition p_center = new ItemLabelPosition(
            ItemLabelAnchor.CENTER, TextAnchor.CENTER
        );
    final ItemLabelPosition p_below = new ItemLabelPosition(
                 ItemLabelAnchor.OUTSIDE6, TextAnchor.TOP_LEFT
                 );
    Font f = new Font("Gulim", Font.BOLD, 14);
    Font axisF = new Font("Gulim", Font.PLAIN, 14);
	
    public void AddDataSet(double[] input, String DataName) {
    	this.DataName = DataName;
    	for(int i=0;i<input.length;i++) {
    		Number temp = input[i];
    		ds2.addValue(temp, DataName, String.valueOf(i));
    		ds[DataSetCount].addValue(temp, DataName, String.valueOf(i));
    	}
    	
    	DataSetCount++;
    }
	
	Graph(int h, int w) {
		this.height = h;
		this.width = w;
		renderer = new LineAndShapeRenderer();
		ds = new DefaultCategoryDataset[20];
		for(int i=0;i<20;++i)
			ds[i] = new DefaultCategoryDataset();
	}
	
	public void DrawChart() {
        graph = getChart(ds);
        ChartFrame frame=new ChartFrame("Data Graph", graph);
		frame.setSize(height, width);
        frame.setVisible(true);
	}
	
	JFreeChart getChart(DefaultCategoryDataset[] ds) {
		plot = new CategoryPlot();
		// Plot�� Data ����
		for(int i=0;i<DataSetCount;++i) {
			plot.setDataset(ds[i]);
			plot.setRenderer(renderer);
		}
		
	     // plot �⺻ ����
	     plot.setOrientation(PlotOrientation.VERTICAL);             // �׷��� ǥ�� ����
	     plot.setRangeGridlinesVisible(true);                       // X�� ���̵� ���� ǥ�ÿ���
	     plot.setDomainGridlinesVisible(true);                      // Y�� ���̵� ���� ǥ�ÿ���
	     
	     // ������ ���� ���� : dataset ��� ������� ������ ( ��, ���� ����Ѱ� �Ʒ��� �� )
	     plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
	    
	     // X�� ����
	     plot.setDomainAxis(new CategoryAxis());              // X�� ���� ����
	     plot.getDomainAxis().setTickLabelFont(axisF); // X�� ���ݶ� ��Ʈ ����
	     plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.STANDARD);       // ī�װ� �� ��ġ ����

	     // Y�� ����
	     plot.setRangeAxis(new NumberAxis());                 // Y�� ���� ����
	     plot.getRangeAxis().setTickLabelFont(axisF);  // Y�� ���ݶ� ��Ʈ ����
	    
	     // ���õ� plot�� �������� chart ����
	     final JFreeChart chart = new JFreeChart(plot);
	     
	     return chart;
	}
}

public class GraphExample {
	private JFreeChart graph;
	private ChartFrame frame;
	
	GraphExample(int height, int width) {
		frame.setSize(height, width);
		frame.setVisible(true);
	}

 public static void main(final String[] args) {
	 double []ds1 = new double[]{0,1,2,3,4,5,6,7,8,9,7,8,4};
	 Graph graph = new Graph(800,400);
	 graph.AddDataSet(ds1, "S1");
	 graph.DrawChart();
 }


}
