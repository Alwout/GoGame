package com.wouterbaudoin.goGame;

enum State {
	Empty,
	Black,
	White
}

public class Piece implements IPiece {
	public State state = State.Empty;
	public short liberties = 4;
	public int row;
	public int column;
	public PieceChain chain;
	
	/**
	 * Constructor of the piece
	 * 
	 * @param row
	 * @param column
	 */
	public Piece(int row, int column) {
		this.row = row;
		this.column = column;
	}

	/** Sets the state of the piece to black/white or empty.
	 * @param newState whether the piece is empty, black or white.
	 */
	public void setState(State newState) {
		state = newState;
	}

	/** Checks the state of the piece
	 * @param state whether the piece is empty, black or white.
	 */
	public boolean isState(State state) {
		return this.state == state;
	}

	public boolean inChain() {
		if (chain != null) {
			return true;
		}
		return false;
	}

	public PieceChain getChain() {
		if (chain != null) {
			return chain;
		}
		return null;
	}
	
}
