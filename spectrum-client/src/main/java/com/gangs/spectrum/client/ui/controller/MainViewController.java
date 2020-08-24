package com.gangs.spectrum.client.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.gangs.spectrum.client.base.UiBaseService;
import com.gangs.spectrum.client.service.ClientScheduledService;
import com.gangs.spectrum.client.ui.ControlledStage;
import com.gangs.spectrum.client.ui.R;
import com.gangs.spectrum.client.ui.StageController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainViewController implements ControlledStage, Initializable{
	
	ClientScheduledService clientScheduledService;
	
    @FXML
    private NumberAxis yAxis;

    @FXML
    private NumberAxis xAxis;
	
    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    @FXML
    private LineChart<Number, Number> pointYChart;

    @FXML
    private Button resetButton;
    
    ObservableList<XYChart.Series<Number, Number>> pointYChartData = FXCollections.observableArrayList();
	
    @FXML
    void onStart(ActionEvent event) {
    	System.out.println("Main view, start entered");
    	clientScheduledService.setDelay(Duration.seconds(0));
    	clientScheduledService.setPeriod(Duration.seconds(0.1));
        //启动失败重新启动
    	clientScheduledService.setRestartOnFailure(true);
        //程序启动失败后重新启动次数
    	clientScheduledService.setMaximumFailureCount(4);
    	
		LineChart.Series<Number, Number> pointYSeries = new LineChart.Series<Number, Number>();
		pointYSeries.setName("Received Data");
		pointYChartData.add(pointYSeries);
        xAxis = new NumberAxis();
        xAxis.setLabel("Time-Series");
        yAxis = new NumberAxis();
        yAxis.setLabel("Received-Data");
        pointYChart.setData(pointYChartData);
		pointYChart.setLegendSide(Side.TOP);
		pointYChart.setCreateSymbols(false);

		clientScheduledService.setChartData(pointYChartData);
    	
    	clientScheduledService.start();
    }

    @FXML
    void onStop(ActionEvent event) {
    	System.out.println("Main view, stop entered");
    	clientScheduledService.cancel();
    }
    
    @FXML
    void onRestart(ActionEvent event) {
    	System.out.println("Main view, restart entered");
    	clientScheduledService.restart();
		LineChart.Series<Number, Number> pointYSeries = new LineChart.Series<Number, Number>();
		pointYSeries.setName("Received Data");
		pointYChartData.add(pointYSeries);
    }

    @FXML
    void onReset(ActionEvent event) {
    	System.out.println("Main view, reset entered");
    	clientScheduledService.reset();
    	pointYChart.getData().clear();
    }

	@Override
	public Stage getMyStage() {
        StageController stageController = UiBaseService.INSTANCE.getStageController();
        return stageController.getStageBy(R.id.MainView);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		clientScheduledService = new ClientScheduledService();
	}
	
	

}
