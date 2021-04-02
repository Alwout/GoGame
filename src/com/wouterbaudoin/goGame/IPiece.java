package com.wouterbaudoin.goGame;

public interface IPiece {
	public void setState(State newState);
	public boolean isState(State state);
	public boolean inChain();
	public PieceChain getChain();
}