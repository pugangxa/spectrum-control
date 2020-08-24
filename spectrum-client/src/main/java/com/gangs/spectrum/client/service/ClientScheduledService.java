package com.gangs.spectrum.client.service;

import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.chart.XYChart;

public class ClientScheduledService extends ScheduledService<Number> {
	
	int times = 0;
	
	private ObservableList<XYChart.Series<Number, Number>> chartData;
	
	public ObservableList<XYChart.Series<Number, Number>> getChartData() {
		return chartData;
	}

	public void setChartData(ObservableList<XYChart.Series<Number, Number>> chartData) {
		this.chartData = chartData;
	}

	private static double currentPointY;

	public static double getCurrentPointY() {
		return currentPointY;
	}

	public static void setCurrentPointY(double currentPointY) {
		ClientScheduledService.currentPointY = currentPointY;
	}

	@Override
	protected Task<Number> createTask() {
		Task<Number> task = new Task<Number>() {
			@Override
			protected Number call() throws Exception {
				times++;
				//System.out.println("Current PointY is: " + currentPointY);
				for(XYChart.Series<Number, Number> series : chartData) {
					if(series.getName().equals("Received Data")){
						series.getData().add(new XYChart.Data<Number, Number>(times, currentPointY));
					}
				}
				return currentPointY;
			}
		};
		return task;
	}

}
