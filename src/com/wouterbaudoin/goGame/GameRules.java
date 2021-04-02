package com.wouterbaudoin.goGame;

import java.util.ArrayList;

import com.wouterbaudoin.goGame.GameBoard.Player;

public class GameRules {
	
	private GameBoard gameBoard;
	public ArrayList<PieceChain> pieceChains = new ArrayList<PieceChain>();
	
	public GameRules(GameBoard gameBoard) {
		this.gameBoard = gameBoard;
	}
	
	/**
	 * Checks the opponents pieces next to the piece placed. E.g. A white piece placed checks the adjacent black pieces and vice versa.
	 * @param piece
	 */
	public void checkOpponentsLiberties(Piece piece) {
		//Get the opponents color
		State opponentColor = piece.state == State.Black ? State.White : State.Black;
		int row = piece.row;
		int column = piece.column;
		int[][] sides = {{row + 1, column}, {row - 1, column}, {row, column + 1}, {row, column - 1}};
		
		//Only check the opponent's pieces next to the piece placed.
		for (int[] side: sides) {
			int sideRow = side[0];
			int sideColumn = side[1];
			
			Piece adjacentPiece = gameBoard.getAdjacentPiece(opponentColor, sideRow, sideColumn);
			if (adjacentPiece != null) {
				checkLiberties(adjacentPiece);
			}
		}
	}
	
	/**
	 * Checks the liberties of a piece.
	 * 
	 * @param piece		A stone piece.
	 * @param row		The row of the piece.
	 * @param column	The column of the piece.
	 * @return			The amount of liberties this piece has.
	 */
	private void checkLiberties(Piece piece) {
		int liberties = 0;
		if (piece.inChain()) {
			liberties = checkPieceChainLiberty(piece.chain);
		} else {
			liberties = checkPieceLiberty(piece);
		}
		setLiberties(piece, liberties);
	}
	
	/**
	 * Set the liberties for each piece played.
	 */
	public void setLiberties(Piece piece, int libertyAmount) {
		if (piece.inChain()) {
			for (Piece pieceInChain : piece.chain.list) {	
				pieceInChain.liberties = (short)libertyAmount;
				if (libertyAmount == 0) {
					pieceInChain.setState(State.Empty);
				}
			}
			if (libertyAmount == 0) {
				removePieceChain(piece.chain);
			}
		} else {
			piece.liberties = (short)libertyAmount;
			if (libertyAmount == 0) {
				piece.setState(State.Empty);
			}
		}
	}
	
	/**
	 * Checks the liberties of each piece in a chain and returns the total liberties.
	 * @param pieceChain	When pieces are connected horizontally or vertically they form a chain.
	 * @return				The total liberties of the chain.
	 */
	private int checkPieceChainLiberty(PieceChain pieceChain) {
		int liberties = 0;
		for (Piece piece : pieceChain.list) {
			liberties += checkPieceLiberty(piece);
		}
		return liberties;
	}
	
	/**
	 * Checks the liberties of a single piece.
	 * @param piece
	 * @return
	 */
	private int checkPieceLiberty(Piece piece) {
		int liberties = 0;
		State pieceColor = piece.state;
		int row = piece.row;
		int column = piece.column;
		int[][] sides = {{row + 1, column}, {row - 1, column}, {row, column + 1}, {row, column - 1}};
		
		//Only check the opponent's pieces next to the piece placed.
		for (int[] side: sides) {
			int sideRow = side[0];
			int sideColumn = side[1];
			
			if (checkPieceLibertySide(pieceColor, sideRow, sideColumn)) {
				liberties += 1;
			}
		}
		
		return liberties;
	}
	
	/**
	 * Checks the side of the piece if it has a liberty or not.
	 * 
	 * @param pieceState
	 * @param row
	 * @param column
	 * @return
	 */
	public boolean checkPieceLibertySide(State pieceState, int row, int column) {
		if (gameBoard.isValid(row, column) && (gameBoard.getPieces()[row][column].isState(State.Empty))) {
			return true;
		}
		return false;
	}

	/**
	 * Checks whether there is a piece of the same color next to this piece.
	 * @param piece
	 */
	public void checkConnections(Piece piece) {
		//Check only pieces with the same color as the piece played.
		State pieceColor = piece.state;
		int row = piece.row;
		int column = piece.column;
		
		IPiece[][] gamePieces = gameBoard.getPieces();
		
		//The four sides next to a piece.
		int[][] sides = {{row + 1, column}, {row - 1, column}, {row, column + 1}, {row, column - 1}};
		
		//Only check the pieces next to the piece placed.
		for (int[] side: sides) {
			int sideRow = side[0];
			int sideColumn = side[1];
			
			//Check a side
			if (gameBoard.isConnectedToAdjacentPiece(pieceColor, sideRow, sideColumn)) {
				Piece adjacentPiece = (Piece) gamePieces[sideRow][sideColumn];
				//Connect the piece to this adjacent piece.
				connectPieces(piece, adjacentPiece);
			}
		}
		
	}
	
	/**
	 * Connects piece1 to piece2 to form a piece chain.
	 * @param piece1
	 * @param piece2
	 */
	private void connectPieces(Piece piece1, Piece piece2) {
		if (piece1.inChain() && piece2.inChain()) {
			PieceChain pieceChain1 = piece1.getChain();
			PieceChain pieceChain2 = piece2.getChain();
			mergePieceChains(pieceChain1, pieceChain2);
		} else if (piece1.inChain()) {
			PieceChain pieceChain1 = piece1.getChain();
			pieceChain1.add(piece2);
		} else if (piece2.inChain()) {
			PieceChain pieceChain2 = piece2.getChain();
			pieceChain2.add(piece1);
		} else {
			PieceChain pieceChain = createPieceChain();
			pieceChain.add(piece1);
			pieceChain.add(piece2);
		}
	}
	
	/**
	 * 
	 * @param pieceChain1
	 * @param pieceChain2
	 */
	private void mergePieceChains(PieceChain pieceChain1, PieceChain pieceChain2) {
		if (pieceChain1 != pieceChain2) {			
			Piece[] piecesInChain2 = pieceChain2.getPieces();
			pieceChain1.add(piecesInChain2);
			removePieceChain(pieceChain2);
		}
	}
	
	/**
	 * Creates a piece chain and adds it to the gameBoard pieceChains.
	 * @return
	 */
	private PieceChain createPieceChain() {
		PieceChain pieceChain = new PieceChain();
		pieceChains.add(pieceChain);
		return pieceChain;
	}
	
	/**
	 * Removes a pieceChain from the list of piece chains.
	 * @param pieceChain
	 */
	private void removePieceChain(PieceChain pieceChain) {
		pieceChain.list.forEach(piece->piece.chain = null);
		pieceChain.list.clear();
		pieceChains.remove(pieceChain);
	}
	
	/**
	 * Checks whether the piece to be placed satisfies the no suicide rule.
	 * @param playerTurn
	 * @param row
	 * @param column
	 * @return
	 */
	public boolean isNotSuicide(Player playerTurn, int row, int column) {
		State opponentColor = playerTurn == Player.Black ? State.White : State.Black;
		
		int[][] sides = {{row + 1, column}, {row - 1, column}, {row, column + 1}, {row, column - 1}};
		int validSides = 0;
		int opponentPieces = 0;
		
		for (int[] side : sides) {
			int sideRow = side[0];
			int sideColumn = side[1];
			if (gameBoard.isValid(sideRow, sideColumn)) {
				validSides ++;
				Piece opponentPiece = gameBoard.getAdjacentPiece(opponentColor, sideRow, sideColumn);
				if (opponentPiece != null) {
					opponentPieces ++;
				}
			}
		}
		
		if (validSides == opponentPieces) {
			return false;
		}
		
		return true;
	}
	
	
}
