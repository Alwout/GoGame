package com.wouterbaudoin.goGame;

import java.util.ArrayList;

/**
 * The GameBoard class handles the creation of the board and the rules of the game.
 * 
 * @author Wouter
 *
 */
public class GameBoard {

	private int boardWidth;
	private int boardHeight;
	private IPiece[][] gameBoard;
	private GameRules gameRules;
	public ArrayList<Piece> piecesPlayed = new ArrayList<Piece>();
	
	public enum Player {
		Black,
		White
	}
	
	public Player playerTurn = Player.Black;
	
	/**
	 * The Gameboard is created based on a width and height.
	 * 
	 * @param gridWidth		The amount of horizontal points in the grid.
	 * @param gridHeight	The amount of vertical points in the grid.
	 */
	public GameBoard(short gridWidth, short gridHeight) {
		this.boardWidth = gridWidth;
		this.boardHeight = gridHeight;
		
		gameBoard = new IPiece[gridWidth][gridHeight];
		
		for (int row = 0; row < gridHeight; row++)
	    {
	        for (int column = 0; column < gridWidth; column++)
	        {
	            gameBoard[row][column] = new Piece(row, column);
	        }
	    }
	}
	
	/**
	 * Imposes a set of game rules on the gameBoard.
	 * @param gameRules		The rules of the go game.
	 */
	public void addRules(GameRules gameRules) {
		this.gameRules = gameRules;
	}
	
	/** 
	 * Gets all the pieces.
	 * @return the pieces on the board, also empty pieces.
	 */
	public IPiece[][] getPieces() {
		return gameBoard;
	}
	
	/**
	 * Checks if there is a piece of the given state at the given position.
	 * @param pieceState
	 * @param row
	 * @param column
	 * @return			The piece at the position.
	 */
	public Piece getAdjacentPiece(State pieceState, int row, int column) {
		if (isValid(row, column) && gameBoard[row][column].isState(pieceState)) {
			return (Piece) gameBoard[row][column];
		}
		return null;
	}
	
	/**
	 * Checks if there is a piece of the given state at the given position.
	 * @param pieceState
	 * @param row
	 * @param column
	 * @return			If it is connected or not.
	 */
	public boolean isConnectedToAdjacentPiece(State pieceState, int row, int column) {
		if (isValid(row, column) && gameBoard[row][column].isState(pieceState)) {
			return true;
		}
		return false;
	}

	/** 
	 * Checks to see if you can place a piece on that point.
	 * 
	 * @param row		The row to check.
	 * @param column	The column to check
	 * @return			Whether you are allowed to place a piece there.
	 */
	public boolean canPlacePiece(int row, int column) {
		if (!isValid(row, column)) return false;
		if (gameBoard[row][column].isState(State.Empty) && gameRules.isNotSuicide(playerTurn, row, column)) {
				return true;
		}
		return false;
	}
	
	/**
	 * Checks whether the row and column are within the grid.
	 * 
	 * @param row		The row of the grid.
	 * @param column	The column of the grid.
	 * @return			Whether the row & column are valid options.
	 */
	public boolean isValid(int row, int column) {
		if (row < 0 || row >= boardHeight || column < 0 || column >= boardWidth) {
			return false;
		}
		return true;
	}
	
	/**
	 * Places a piece on the board.
	 * @param row
	 * @param column
	 */
	public void placePiece(int row, int column) {
		gameBoard[row][column].setState(playerTurn == Player.Black ? State.Black : State.White);
		Piece piece = (Piece)gameBoard[row][column];
		piecesPlayed.add(piece);
		
		gameRules.checkConnections(piece);
		gameRules.checkOpponentsLiberties(piece);
	}
}
