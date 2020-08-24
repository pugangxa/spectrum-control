package com.gangs.spectrum.client.base;

import com.gangs.spectrum.client.ui.StageController;

import javafx.application.Platform;

public enum UiBaseService {
	INSTANCE;

	private StageController stageController = new StageController();

	public StageController getStageController() {
		return stageController;
	}

	/**
	 * 将任务转移给fxapplication线程延迟执行
	 * @param task
	 */
	public void runTaskInFxThread(Runnable task){
		Platform.runLater(task);
	}
}
