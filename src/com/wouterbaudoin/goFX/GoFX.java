package com.wouterbaudoin.goFX;

import java.io.FileInputStream;
import java.io.InputStream;

import com.wouterbaudoin.goGame.GameBoard;
import com.wouterbaudoin.goGame.GoGame;
import com.wouterbaudoin.goGame.GameBoard.Player;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class GoFX extends Application {
	
	private final int WINDOW_WIDTH = 800;
	private final int WINDOW_HEIGHT = 800;
	private final Paint backgroundColor = Color.rgb(220, 179, 92);
	private Tiles tileManager;
	private GoGame game = new GoGame();
	
	public GoFX() {
		System.out.println("Go Game started!");
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Group root = new Group();
		
		short boardSize = 19;
		short boardWidth = boardSize;
		short boardHeight = boardSize;
		
		InputStream blackStonePieceFile = new FileInputStream("images/b.png");
		InputStream whiteStonePieceFile = new FileInputStream("images/w.png");
		
		Image blackStonePieceImage = new Image(blackStonePieceFile);
		Image whiteStonePieceImage = new Image(whiteStonePieceFile);
		
		GameBoard gameBoard = game.createGameBoard(boardWidth, boardHeight);
		
		tileManager = new Tiles(root, WINDOW_WIDTH, gameBoard);
		  
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, backgroundColor);
        
        scene.setOnMouseReleased(new EventHandler<MouseEvent>() {  
            public void handle(MouseEvent event) {  
            	
            	int mouseX = (int) event.getX();
            	int mouseY = (int) event.getY();
            	
            	int row = tileManager.getGridRow(mouseY);
            	int column = tileManager.getGridColumn(mouseX);
                
                if (gameBoard.canPlacePiece(row, column)) {              
                	gameBoard.placePiece(row, column);
                	if (gameBoard.playerTurn == Player.Black) {                		
                		tileManager.placePiece(row, column, blackStonePieceImage);
                	} else {
                		tileManager.placePiece(row, column, whiteStonePieceImage);
                	}
                	game.nextTurn();
                } else {
                	System.out.println("Can't place piece");
                }
                
            }  
          });
		
		primaryStage.setTitle("EduGO");  
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
	}
	
	public static void main (String[] args)  
    {  
        launch(args);  
    }
}


