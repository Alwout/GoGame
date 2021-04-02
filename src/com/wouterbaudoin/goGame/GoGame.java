package com.wouterbaudoin.goGame;

import com.wouterbaudoin.goGame.GameBoard.Player;

public class GoGame {
	
	public GameBoard gameBoard;
	public GameRules gameRules;
	
	/**
	 * 
	 * @param gridWidth
	 * @param gridHeight
	 * @return
	 */
	public GameBoard createGameBoard(short gridWidth, short gridHeight) {
		gameBoard = new GameBoard(gridWidth, gridHeight);
		gameRules = new GameRules(gameBoard);
		gameBoard.addRules(gameRules);
		return gameBoard;
	}
	
	public void nextTurn() {
		gameBoard.playerTurn = gameBoard.playerTurn == Player.Black ? Player.White : Player.Black;
	}
}
