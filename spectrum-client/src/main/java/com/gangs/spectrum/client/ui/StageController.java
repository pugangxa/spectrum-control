package com.gangs.spectrum.client.ui;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.gangs.spectrum.client.ui.event.DragWindowHandler;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StageController {
	private Map<String, Stage> stages = new HashMap<>();

	private Map<String, ControlledStage> controllers = new HashMap<>();
	
	public void addStage(String name, Stage stage) {
		this.stages.put(name, stage);
	}
	
	public Stage getStageBy(String name) {
		return this.stages.get(name);
	}
	
	public void setPrimaryStage(String name, Stage stage) {
		this.addStage(name, stage);
	}
	
	public Stage loadStage(String name, String resource, StageStyle... styles) {
		Stage result = null;
		try {
			URL url = Thread.currentThread().getContextClassLoader().getResource(resource);
			FXMLLoader loader = new FXMLLoader(url);
			loader.setResources(ResourceBundle.getBundle("i18n/message"));
			Pane tmpPane = (Pane)loader.load();
			ControlledStage controlledStage = (ControlledStage)loader.getController();
			this.controllers.put(name, controlledStage);
			Scene tmpScene = new Scene(tmpPane);
			result = new Stage();
			result.setScene(tmpScene);
			
			for(StageStyle style:styles) {
				result.initStyle(style);
			}
			this.addStage(name, result);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T load(String resource, Class<T> clazz) {
		try{
			URL url = Thread.currentThread().getContextClassLoader().getResource(resource);
			FXMLLoader loader = new FXMLLoader(url);
			return (T)loader.load();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T load(String resource, Class<T> clazz, ResourceBundle resources) {
		try{
			URL url = Thread.currentThread().getContextClassLoader().getResource(resource);
			return (T)FXMLLoader.load(url, resources);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public Stage setStage(String name) {
		Stage stage = this.getStageBy(name);
		if (stage == null) {
			return null;
		}
		stage.show();
		/**拖动*/
		EventHandler handler = new DragWindowHandler(stage);
		stage.getScene().setOnMousePressed(handler);//如果去掉这一行代码将会使鼠标进入面板时面板左上角会定位到鼠标的位置
		stage.getScene().setOnMouseDragged(handler);
		return stage;
	}
	

	public boolean switchStage(String toShow, String toClose) {
		getStageBy(toClose).close();
		setStage(toShow);

		return true;
	}

	public void closeStage(String name) {
		Stage target = getStageBy(name);
		target.close();
	}
	
	public boolean unloadStage(String name) {
		return this.stages.remove(name) != null;
	}

	public ControlledStage getController(String name) {
		return this.controllers.get(name);
	}

	
}
