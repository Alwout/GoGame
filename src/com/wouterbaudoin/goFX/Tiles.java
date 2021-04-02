package com.wouterbaudoin.goFX;

import java.util.ArrayList;

import com.wouterbaudoin.goGame.GameBoard;
import com.wouterbaudoin.goGame.IPiece;
import com.wouterbaudoin.goGame.Piece;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

//Based on a 2d array given makes each corresponding tile: corner, edge, centre;
public class Tiles {
	
	private GameBoard gameBoard;
	private IPiece[][] boardPieces;
	private int boardWidth;
	private int boardHeight;
	private ArrayList<ImageView> stonePiecesPlayed = new ArrayList<ImageView>();
	
	private Group root;
	
	private int tileSize = 25;
	
	private int START_X = 100;
	private int START_Y = 100;
	
	/**
	 *  Constructor of the tile manager
	 * @param root 			The JavaFX group that the tiles will belong to.
	 * @param windowSize 	The width of the screen, used to fit the board.
	 * @param gameBoard		The 
	 */
	public Tiles(Group root, int windowSize,  GameBoard gameBoard) {
		this.root = root;
		this.gameBoard = gameBoard;
		boardPieces = gameBoard.getPieces();
		boardWidth = boardPieces.length;
		boardHeight = boardPieces[0].length;
		
		this.tileSize = (windowSize/(1 + boardWidth));
		this.START_X = tileSize;
		this.START_Y = tileSize;
		
		for (int row = 0; row < boardHeight; row ++) {
			for (int column = 0; column < boardWidth; column ++ ) {
				if (isCornerPiece(row, column)) {
					
					new CornerPiece(root, tileSize, getX(column), getY(row), getCornerType(row, column));
					
					continue;
				}
				
				if (isEdgePiece(row, column)) {
					
					new EdgePiece(root, tileSize, getX(column), getY(row), getEdgeType(row, column));
					
					continue;
				}
				
				new CenterPiece(root, tileSize, getX(column), getY(row));
				
			}
		}
	}
	
	/**
	 * 
	 * @param row
	 * @param column
	 * @param stonePieceImage
	 */
	public void placePiece(int row , int column, Image stonePieceImage) {
		ImageView stonePiece = new ImageView(stonePieceImage);
		
		int imageOffset = 50+(100-tileSize);
		
		double imageScale = (0.35*tileSize)/100;
		
		stonePiece.setScaleX(imageScale);
		stonePiece.setScaleY(imageScale);
		stonePiece.setLayoutX(getX(column) - tileSize*(3/2) - imageOffset);
		stonePiece.setLayoutY(getY(row) - tileSize*(3/2) - imageOffset);
		
		root.getChildren().add(stonePiece);
		stonePiecesPlayed.add(stonePiece);
		
		checkLiberties();
	}
	
	/**
	 * Checks the liberties of a piece.
	 */
	private void checkLiberties() {
		for (Piece piece: gameBoard.piecesPlayed) {
			if (piece.liberties == 0) {
				int pieceIndex = gameBoard.piecesPlayed.indexOf(piece);
				ImageView stonePiece = stonePiecesPlayed.get(pieceIndex);
				root.getChildren().remove(stonePiece);
			}
		}
	}
	
	/**
	 * 
	 * @param y
	 * @return the row of the game board.
	 */
	public int getGridRow(int y) {
		return Math.floorDiv(y - START_Y + tileSize/2, tileSize);
	}
	
	/**
	 * 
	 * @param x
	 * @return the column of the game board.
	 */
	public int getGridColumn(int x) {
		return Math.floorDiv(x - START_X + tileSize/2, tileSize);
	}
	
	/**
	 * 
	 * @param column
	 * @return
	 */
	public int getX(int column) {
		return START_X + column * tileSize;
	}
	
	/**
	 * 
	 * @param row
	 * @return
	 */
	public int getY(int row) {
		return START_Y + row * tileSize;
	}
	
	/**
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public boolean isEdgePiece(int row, int column) {
		if (row == 0 || column == 0 || row == boardHeight - 1 || column == boardWidth - 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public boolean isCornerPiece(int row, int column) {
		if (row == 0 && column == 0) {
			return true;
		} else if (row == 0 && column == boardWidth - 1) {
			return true;
		} else if (row == boardHeight - 1 && column == 0) {
			return true;
		} else if (row == boardHeight - 1 && column == boardWidth - 1) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public EdgeType getEdgeType(int row, int column) {
		if (row == 0) {
			return EdgeType.TOP;
		} else if (column == 0) {
			return EdgeType.LEFT;
		} else if (row == boardHeight - 1) {
			return EdgeType.BOT;
		} else if (column == boardWidth - 1) {
			return EdgeType.RIGHT;
		}
		return null;
	}
	
	/**
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public CornerType getCornerType(int row, int column) {
		if (row == 0 && column == 0) {
			return CornerType.TOPLEFT;
		} else if (row == boardHeight - 1 && column == 0) {
			return CornerType.BOTLEFT;
		} else if (row == 0 && column == boardWidth - 1) {
			return CornerType.TOPRIGHT;
		} else if (row == boardHeight - 1 && column == boardWidth - 1) {
			return CornerType.BOTRIGHT;
		}

		return null;
	}
}

enum CornerType {
	TOPLEFT,
	TOPRIGHT,
	BOTLEFT,
	BOTRIGHT
}

enum EdgeType {
	TOP,
	LEFT,
	RIGHT,
	BOT
}

class LinePiece {
	protected final Paint lineColor = Color.web("#212B35");
}

class CornerPiece extends LinePiece {
	/**
	 * 
	 * @param root
	 * @param tileSize
	 * @param x
	 * @param y
	 * @param cornerType
	 */
	public CornerPiece(Group root, int tileSize, int x, int y, CornerType cornerType) {
		Line line = new Line(); //instantiating Line class   
        line.setStartX(x); //setting starting X point of Line  
        line.setStartY(y); //setting starting Y point of Line
        line.setEndX(x+tileSize/2); //setting ending X point of Line
        line.setEndY(y); //setting ending Y point of Line
        line.setStroke(lineColor);
        line.setStrokeWidth(2);
        
        Line line2 = new Line(); //instantiating Line class   
        line2.setStartX(x); //setting starting X point of Line  
        line2.setStartY(y); //setting starting Y point of Line
        line2.setEndX(x); //setting ending X point of Line
        line2.setEndY(y+tileSize/2); //setting ending Y point of Line
        line2.setStroke(lineColor);
        line2.setStrokeWidth(2);
        
        adjustCornerByType(tileSize, line, line2, cornerType);
        
        root.getChildren().addAll(line, line2);
	}
	
	/**
	 * 
	 * @param tileSize
	 * @param line1
	 * @param line2
	 * @param cornerType
	 */
	private void adjustCornerByType(int tileSize, Line line1, Line line2, CornerType cornerType) {
		double line1X = line1.getStartX();
		double line2Y = line2.getStartY();
        if (cornerType == CornerType.TOPRIGHT) {
        	line1.setEndX(line1X-tileSize/2);
        } else if (cornerType == CornerType.BOTRIGHT) {
        	line1.setEndX(line1X-tileSize/2);
        	line2.setEndY(line2Y-tileSize/2);
        } else if (cornerType == CornerType.BOTLEFT) {
        	line2.setEndY(line2Y-tileSize/2);
        }
	}
}

class EdgePiece extends LinePiece {
	/**
	 * 
	 * @param root
	 * @param tileSize
	 * @param x
	 * @param y
	 * @param edgeType
	 */
	public EdgePiece(Group root, int tileSize, int x, int y, EdgeType edgeType) {
		Line line = new Line(); //instantiating Line class   
        line.setStartX(x-tileSize/2); //setting starting X point of Line  
        line.setStartY(y); //setting starting Y point of Line
        line.setEndX(x+tileSize/2); //setting ending X point of Line
        line.setEndY(y); //setting ending Y point of Line
        line.setStroke(lineColor);
        line.setStrokeWidth(2);
        
        Line line2 = new Line(); //instantiating Line class   
        line2.setStartX(x); //setting starting X point of Line  
        line2.setStartY(y); //setting starting Y point of Line
        line2.setEndX(x); //setting ending X point of Line
        line2.setEndY(y+tileSize/2); //setting ending Y point of Line
        line2.setStroke(lineColor);
        line2.setStrokeWidth(2);
        
        adjustEdgeByType(tileSize, x, y, line, line2, edgeType);
        
        root.getChildren().addAll(line, line2);
	}
	
	/**
	 * 
	 * @param tileSize
	 * @param x
	 * @param y
	 * @param line1
	 * @param line2
	 * @param edgeType
	 */
	public void adjustEdgeByType(int tileSize, int x, int y, Line line1, Line line2, EdgeType edgeType) {

		int halfTile = tileSize/2;
		
		if (edgeType == EdgeType.LEFT) {
			line1.setStartX(x);
			line1.setEndX(x+halfTile);
			line2.setStartY(y-halfTile);
			line2.setEndY(y+halfTile);
		} else if (edgeType == EdgeType.RIGHT) {
			line1.setStartX(x);
			line1.setEndX(x-halfTile);
			line2.setStartY(y-halfTile);
			line2.setEndY(y+halfTile);
		} else if (edgeType == EdgeType.BOT) {
			line2.setStartY(y-halfTile);
			line2.setEndY(y);
		}
	}
}

class CenterPiece extends LinePiece {
	/**
	 * 
	 * @param root
	 * @param tileSize
	 * @param x
	 * @param y
	 */
	public CenterPiece(Group root, int tileSize, int x, int y) {
		
		int halfTile = tileSize / 2;
		
		Line line = new Line(); //instantiating Line class   
        line.setStartX(x-halfTile); //setting starting X point of Line  
        line.setStartY(y); //setting starting Y point of Line
        line.setEndX(x+halfTile); //setting ending X point of Line
        line.setEndY(y); //setting ending Y point of Line
        line.setStroke(lineColor);
        line.setStrokeWidth(2);
        
        Line line2 = new Line(); //instantiating Line class   
        line2.setStartX(x); //setting starting X point of Line  
        line2.setStartY(y-halfTile); //setting starting Y point of Line
        line2.setEndX(x); //setting ending X point of Line
        line2.setEndY(y+halfTile); //setting ending Y point of Line
        line2.setStroke(lineColor);
        line2.setStrokeWidth(2);
        
        root.getChildren().addAll(line, line2);
	}
}
