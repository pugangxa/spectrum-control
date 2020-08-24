package com.gangs.spectrum.client;

import com.gangs.spectrum.client.base.UiBaseService;
import com.gangs.spectrum.client.netty.SocketClient;
import com.gangs.spectrum.client.netty.message.ClientPushCaculatedPoint;
import com.gangs.spectrum.client.ui.R;
import com.gangs.spectrum.client.ui.StageController;
import com.gangs.spectrum.core.netty.message.PacketType;

import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClientStartup extends Application {
	
    public void init() throws Exception {
        PacketType.registerPackets(ClientPushCaculatedPoint.packetType, ClientPushCaculatedPoint.class);
    }

	@Override
	public void start(Stage stage) throws Exception {
        //与服务端建立连接
        connectToServer();
        //打开界面
        openUi(stage);        
	}
	
	private void openUi(Stage stage){
        StageController stageController = UiBaseService.INSTANCE.getStageController();
        stageController.setPrimaryStage("root", stage);

        /*主界面*/
        Stage mainStage = stageController.loadStage(R.id.MainView, R.layout.MainView, StageStyle.DECORATED);
        //把主界面放在右上方
        Screen screen = Screen.getPrimary();
        double rightTopX = screen.getVisualBounds().getWidth()*0.75;
        double rightTopY = screen.getVisualBounds().getHeight()*0.05;
        mainStage.setX(rightTopX);
        mainStage.setY(rightTopY);

        stageController.setStage(R.id.MainView);      
	}
	
	private void connectToServer() {
		new Thread() {
			public void run() {
				new SocketClient().start();
			}
		}.start();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
