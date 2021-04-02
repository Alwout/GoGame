package com.wouterbaudoin.goGame;

import java.util.ArrayList;

/**
 * Forms a chain of pieces of the same color, with a minimum of 2 pieces.
 * @author Wouter
 *
 */
public class PieceChain {
	public ArrayList<Piece> list = new ArrayList<Piece>();
	
	/**
	 * Add one piece to the piece chain list.
	 * @param piece
	 */
	public void add(Piece piece) {
		piece.chain = this;
		list.add(piece);
	}
	
	/**
	 * Add an array of pieces to the piece chain list.
	 * @param pieces
	 */
	public void add(Piece[] pieces) {
		for (Piece piece : pieces) {			
			piece.chain = this;
			list.add(piece);
		}
	}
	
	/**
	 * Get all the pieces in the piece chain.
	 * @return		A list of pieces.
	 */
	public Piece[] getPieces() {
		if (list.isEmpty()) return null;
		Piece pieceArray[] = new Piece[list.size()];
		int i = 0;
		for (Piece piece : list) {
			pieceArray[i] = piece;
			i++;
		}
		return pieceArray;
	}
	
	/**
	 * Removes a piece from the list.
	 * @param piece
	 */
	public void remove(Piece piece) {
		piece.chain = null;
		list.remove(piece);
	}

	
}
