package com.mygdx.game;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Stack;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;

// Square class
// object for holding a set of coordinates (which correspond to a square) conveniently, along with other attributes of a move
class Square {
	private int x; // x coord of square
	private int y; // y coord of square
	private boolean enPassant = false; // holds whether move is enPassant
	private boolean promotion = false; // holds whether move is promotion
	private boolean queenCastle = false; // holds whether move is castling queenside
	private boolean kingCastle = false; // holds whether move is castling kingside

	// Square()
	// instantiates a squares
	public Square(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// prints x and y of square
	public String toString() {
		String square = "";
		square = square + " X:" + this.x + " Y:" + this.y + " Promotion:" + this.promotion + " En Passant:" + this.enPassant + " kingCastle:" + this.kingCastle + " queenCastle:" + this.queenCastle;
		return square;
	}

	// obtain x of a square
	public int getX() {
		return this.x;
	}

	// obtain y of a square
	public int getY() {
		return this.y;
	}

	// set x of a square
	public void setX(int x) {
		this.x = x;
	}

	// set y of a square
	public void setY(int y) {
		this.y = y;
	}

	public void setEnPassant() {
		this.enPassant = !this.enPassant;
	}

	public void setPromotion() {
		this.promotion = !this.promotion;
	}

	public void setKingCastle() {
		this.kingCastle = !this.kingCastle;
	}

	public void setQueenCastle() {
		this.queenCastle = !this.queenCastle;
	}

	public boolean getKingCastle() {
		return this.kingCastle;
	}

	public boolean getQueenCastle() {
		return this.queenCastle;
	}

	public boolean getEnPassant() {
		return this.enPassant;
	}

	public boolean getPromotion() {
		return this.promotion;
	}

}

class Piece {
	private int x; // x pos of piece
	private int y; // y pos of piece
	private int type; // 0 = pawn, 1 = king, 2 = queen, 3 = knight, 4 = bishop, 5 = rook
	private int colour; // 0 = white, 1 = black
	private boolean hasMoved; // holds whether piece was moved (used to check whether piece can castle or not)

	// creates a new piece
	public Piece(int x, int y, int type, int colour) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.colour = colour;
	}

	// prints attributes of piece
	public String toString() {
		String piece = "";
		piece = piece + " X:" + this.x + " Y:" + this.y + " Type:" + this.type + " Colour:" + this.colour;
		return piece;
	}

	// obtains x of piece
	public int getPieceX() {
		return this.x;
	}

	// obtains y of piece
	public int getPieceY() {
		return this.y;
	}

	// obtains piece type
	public int getPieceType() {
		return this.type;
	}

	// obtains piece colour
	public int getPieceColour() {
		return this.colour;
	}

	// sets piece x
	public void setPieceX(int x) {
		this.x = x;
	}

	// sets piece y
	public void setPieceY(int y) {
		this.y = y;
	}

	public void pieceHasMoved() {
		this.hasMoved = true;
	}

	public boolean hasPieceMoved() {
		return this.hasMoved;
	}
}

// class which contains game state
class ChessGame {
	private int turn; // 0 = white, 1 = black

	Stack<Piece> previousPieces = new Stack<Piece>();
	Stack<Square> previousMoves = new Stack<Square>();

	private Piece[] whitePieces = new Piece[16];
	private Piece[] blackPieces = new Piece[16];

	private ArrayList<Piece> whitePiecesList = new ArrayList<Piece>();
	private ArrayList<Piece> blackPiecesList = new ArrayList<Piece>();

	public ChessGame() {
		this.setupGame();
		this.turn = 0;
	}

	public void setupGame() { // is hardcoded by necessity, because in a traditional game of chess there is
								// only one initial
								// configuration
		for (int i = 0; i <= 7; i++) { // row of white pawns
			this.whitePieces[i] = new Piece(i, 1, 0, 0);
		}
		this.whitePieces[8] = new Piece(0, 0, 5, 0);
		this.whitePieces[9] = new Piece(7, 0, 5, 0); // white rooks
		this.whitePieces[10] = new Piece(1, 0, 3, 0);
		this.whitePieces[11] = new Piece(6, 0, 3, 0); // white knights
		this.whitePieces[12] = new Piece(2, 0, 4, 0);
		this.whitePieces[13] = new Piece(5, 0, 4, 0); // white bishops
		this.whitePieces[14] = new Piece(3, 0, 2, 0); // white queen
		this.whitePieces[15] = new Piece(4, 0, 1, 0); // white king
		// black pieces
		for (int i = 0; i <= 7; i++) { // row of black pawns
			this.blackPieces[i] = new Piece(i, 6, 0, 1);
		}
		this.blackPieces[8] = new Piece(0, 7, 5, 1);
		this.blackPieces[9] = new Piece(7, 7, 5, 1); // black rooks
		this.blackPieces[10] = new Piece(1, 7, 3, 1);
		this.blackPieces[11] = new Piece(6, 7, 3, 1); // black knights
		this.blackPieces[12] = new Piece(2, 7, 4, 1);
		this.blackPieces[13] = new Piece(5, 7, 4, 1); // black bishops
		this.blackPieces[14] = new Piece(3, 7, 2, 1); // black queen
		this.blackPieces[15] = new Piece(4, 7, 1, 1); // black king

		for (int i = 0; i < 16; i++) { // converts array to arraylist (it admittedly would be better to just replace
										// the original array with an arraylist, but it is easier to do it this way)
			whitePiecesList.add(this.whitePieces[i]);
			blackPiecesList.add(this.blackPieces[i]);
		}

	}

	// returns arraylist of white pieces
	public ArrayList<Piece> getWhitePieces() {
		return this.whitePiecesList;
	}

	// returns arraylist of black pieces
	public ArrayList<Piece> getBlackPieces() {
		return this.blackPiecesList;
	}

	// returns current turn
	public int getCurrentTurn() {
		return this.turn;
	}

	// advances to next turn
	public void nextTurn() {
		if (this.turn == 0)
			this.turn = 1;
		else
			this.turn = 0;
	}

	// find the piece at a given location
	public Piece pieceLookUp(int x, int y) {
		Piece piece = null; // assume piece is null, have it overwritten if it isn't the case
		for (int i = 0; i < this.whitePiecesList.size(); i++) {
			if (this.whitePiecesList.get(i).getPieceX() == x && this.whitePiecesList.get(i).getPieceY() == y) {
				return this.whitePiecesList.get(i);
			}
		}
		for (int j = 0; j < this.blackPiecesList.size(); j++) {
			if (this.blackPiecesList.get(j).getPieceX() == x && this.blackPiecesList.get(j).getPieceY() == y) {
				return this.blackPiecesList.get(j);
			}
		}
		return piece;
	}

	// remove a piece at a given location
	public void removePiece(int x, int y) {
		for (int i = 0; i < this.whitePiecesList.size(); i++) {
			if (this.whitePiecesList.get(i).getPieceX() == x && this.whitePiecesList.get(i).getPieceY() == y) {
				this.whitePiecesList.remove(i);
			}
		}
		for (int j = 0; j < this.blackPiecesList.size(); j++) {
			if (this.blackPiecesList.get(j).getPieceX() == x && this.blackPiecesList.get(j).getPieceY() == y) {
				this.blackPiecesList.remove(j);
			}
		}
	}

	// modify a game piece
	public void modifyPiece(Square originalPos, Square newPos) {
		for (int i = 0; i < this.whitePiecesList.size(); i++) {
			if (this.whitePiecesList.get(i).getPieceX() == originalPos.getX()
					&& this.whitePiecesList.get(i).getPieceY() == originalPos.getY()) {
				this.whitePiecesList.get(i).setPieceX(newPos.getX());
				this.whitePiecesList.get(i).setPieceY(newPos.getY());
				this.whitePiecesList.get(i).pieceHasMoved();
			}
		}
		for (int j = 0; j < blackPiecesList.size(); j++) {
			if (this.blackPiecesList.get(j).getPieceX() == originalPos.getX()
					&& this.blackPiecesList.get(j).getPieceY() == originalPos.getY()) {
				this.blackPiecesList.get(j).setPieceX(newPos.getX());
				this.blackPiecesList.get(j).setPieceY(newPos.getY());
				this.blackPiecesList.get(j).pieceHasMoved();
			}
		}
	}

	// promote a pawn to a queen
	public void promotePiece(Square originalPos) {
		for (int i = 0; i < this.whitePiecesList.size(); i++) {
			if (this.whitePiecesList.get(i).getPieceX() == originalPos.getX()
					&& this.whitePiecesList.get(i).getPieceY() == originalPos.getY()) {
				Piece piece = new Piece(originalPos.getX(), originalPos.getY(), 2, 0); // 2 means queen, 0 means white
				this.whitePiecesList.set(i, piece);
			}
		}
		for (int j = 0; j < blackPiecesList.size(); j++) {
			if (this.blackPiecesList.get(j).getPieceX() == originalPos.getX()
					&& this.blackPiecesList.get(j).getPieceY() == originalPos.getY()) {
				Piece piece = new Piece(originalPos.getX(), originalPos.getY(), 2, 1); // 2 means queen, 1 means black
				this.blackPiecesList.set(j, piece);
			}
		}
	}

	// handles the execution of a piece move
	public void movePiece(Piece piece, Square move) {
		if (piece == null || move == null) // return if piece or move is null
			return;
		Piece attackedPiece = pieceLookUp(move.getX(), move.getY()); // find if there is a piece at the desired move
		// location
		Square originalPos = new Square(piece.getPieceX(), piece.getPieceY());
		Piece newPiece = new Piece(piece.getPieceX(), piece.getPieceY(), piece.getPieceType(), piece.getPieceColour());
		previousPieces.push(newPiece);
		previousMoves.push(move);
 
		if (move.getKingCastle() || move.getQueenCastle()) { // for castling purposes only
			switch (piece.getPieceColour()) {
			case 0:
				if (move.getKingCastle()) {
					originalPos = new Square(4,0); // move king (pos should always be same for castling)
					move = new Square(6, 0);
					modifyPiece(originalPos, move);
					originalPos = new Square(7,0); // move rook (pos should always be same for castling)
					move = new Square(5, 0);
					modifyPiece(originalPos, move);
				} else if (move.getQueenCastle()) {
					originalPos = new Square(4,0); // move king (pos should always be same for castling)
					move = new Square(2, 0);
					modifyPiece(originalPos, move);
					originalPos = new Square(0,0); // move rook (pos should always be same for castling)
					move = new Square(3, 0);
				}
				break;
			case 1:
				if (move.getKingCastle()) {
					originalPos = new Square(4,7); // move king (pos should always be same for castling)
					move = new Square(6, 7);
					modifyPiece(originalPos, move);
					originalPos = new Square(7,7); // move rook (pos should always be same for castling)
					move = new Square(5, 7);
					modifyPiece(originalPos, move);
				} else if (move.getQueenCastle()) {
					originalPos = new Square(4,7); // move king (pos should always be same for castling)
					move = new Square(2, 7);
					modifyPiece(originalPos, move);
					originalPos = new Square(0,7); // move rook (pos should always be same for castling)
					move = new Square(3, 7);
				}
				break;
			}
			return;
		}
		
		if (attackedPiece != null && attackedPiece.getPieceColour() != piece.getPieceColour()) {
			removePiece(attackedPiece.getPieceX(), attackedPiece.getPieceY()); // remove piece if there is one at the
																				// requested position
			System.out.println("removed piece" + attackedPiece + " with attacking piece " + piece);
		}
		
		modifyPiece(originalPos, move); // move piece to requested position
		if (move.getPromotion()) { // if white pawn or black pawn on last row,
																			// promote said pawn
			promotePiece(move);
		}
		if (move.getEnPassant()) {
			switch (piece.getPieceColour()) {
			case 0:
				removePiece(move.getX(), move.getY() - 1);
				break;
			case 1:
				removePiece(move.getX(), move.getY() + 1);
				break;
			}
		}
	}

	public ArrayList<Square> getPieceOffset(Piece piece) {
		ArrayList<Square> offsetList = new ArrayList<Square>();
		Square baseOffset[] = new Square[8]; // there is probably a more elegant way to do this but it really shouldn't
												// matter
		baseOffset[0] = new Square(-1, 1);
		baseOffset[1] = new Square(1, 1);
		baseOffset[2] = new Square(-1, -1);
		baseOffset[3] = new Square(1, -1); // all four diagonals
		baseOffset[4] = new Square(0, 1);
		baseOffset[5] = new Square(1, 0);
		baseOffset[6] = new Square(0, -1);
		baseOffset[7] = new Square(-1, 0); // four adjacent sides
		Square knightOffset[] = new Square[8]; // there is probably a more elegant way to do this but it really
												// shouldn't matter
		knightOffset[0] = new Square(-1, 2);
		knightOffset[1] = new Square(-2, 1);
		knightOffset[2] = new Square(-2, -1);
		knightOffset[3] = new Square(-1, -2);
		knightOffset[4] = new Square(1, -2);
		knightOffset[5] = new Square(2, -1);
		knightOffset[6] = new Square(1, 2);
		knightOffset[7] = new Square(2, 1);
		switch (piece.getPieceType()) {
		case 1: // king
			for (int i = 0; i < 8; i++) {
				offsetList.add(baseOffset[i]);
			}
			break;
		case 2: // queen
			for (int i = 0; i < 8; i++) {
				offsetList.add(baseOffset[i]);
			}
			break;
		case 3: // knight
			for (int i = 0; i < 8; i++) {
				offsetList.add(knightOffset[i]);
			}
			break;
		case 4: // bishop
			for (int i = 0; i < 4; i++) {
				offsetList.add(baseOffset[i]);
			}
			break;
		case 5: // rook
			for (int i = 4; i < 8; i++) {
				offsetList.add(baseOffset[i]);
			}
			break;
		}
		return offsetList;
	}

	// movePiece()
	// returns all legal moves for a certain piece
	public ArrayList<Square> generateMoveList(Piece piece, boolean castleCheck) { // TODO: find a more efficient way of
																					// doing this
		if (piece == null) {
			return null;
		}
		ArrayList<Square> moveList = new ArrayList<Square>(); // arraylist which contains all moves
		ArrayList<Square> offsetList = new ArrayList<Square>(); // contains offset generated from getPieceOffset
																// function

		ArrayList<Square> temporaryMoveList = new ArrayList<Square>(); // arraylist which holds a temporary movelist
		ArrayList<ArrayList<Square>> totalMoveList = new ArrayList<ArrayList<Square>>(); // arraylist of an arraylist of
																							// squares, which contains
																							// all moves from all
																							// opposing pieces, used
																							// when checking whether
																							// castling is possible

		Piece kingsideRook;
		Piece queensideRook;
		
		Square[] whiteKingsideSquares = new Square[2]; // white kingside squares to check
		Square[] whiteQueensideSquares = new Square[3]; // white queenside squares to check
		Square[] blackKingsideSquares = new Square[2]; // black queenside squares to check
		Square[] blackQueensideSquares = new Square[3]; // black queenside squares to check
		
		whiteKingsideSquares[0] = new Square(5, 0);
		whiteKingsideSquares[1] = new Square(6, 0);
		whiteQueensideSquares[0] = new Square(3, 0);
		whiteQueensideSquares[1] = new Square(2, 0);
		whiteQueensideSquares[2] = new Square(1, 0);
		blackKingsideSquares[0] = new Square(5, 7);
		blackKingsideSquares[1] = new Square(6, 7);
		blackQueensideSquares[0] = new Square(3, 7);
		blackQueensideSquares[1] = new Square(2, 7);
		blackQueensideSquares[2] = new Square(1, 7);

		int pieceX = piece.getPieceX();
		int pieceY = piece.getPieceY();

		Square temp = null;

		if (!previousPieces.isEmpty()) {
			System.out.println(previousPieces.peek() + " " + previousMoves.peek());
		}

		switch (piece.getPieceType()) {
		case 0: // if piece == pawn
			switch (piece.getPieceColour()) { // absolutely dreadful code
			case 0: // white pawn
				if (pieceLookUp(pieceX - 1, pieceY + 1) != null
						&& pieceLookUp(pieceX - 1, pieceY + 1).getPieceColour() != piece.getPieceColour()) {
					temp = new Square(pieceX - 1, pieceY + 1);
					if ((pieceY + 1) == 7) temp.setPromotion(); // set the move to promotion type if it's reached the end of the board
					moveList.add(temp);
				}
				if (pieceLookUp(pieceX + 1, pieceY + 1) != null
						&& pieceLookUp(pieceX + 1, pieceY + 1).getPieceColour() != piece.getPieceColour()) {
					temp = new Square(pieceX + 1, pieceY + 1);
					if ((pieceY + 1) == 7) temp.setPromotion(); // set the move to promotion type if it's reached the end of the board
					moveList.add(temp);
				}
				if (pieceLookUp(pieceX, pieceY + 1) == null) {
					temp = new Square(pieceX, pieceY + 1);
					if ((pieceY + 1) == 7) temp.setPromotion(); // set the move to promotion type if it's reached the end of the board
					moveList.add(temp);
				}
				if (pieceY == 1 && pieceLookUp(pieceX, pieceY + 1) == null && pieceLookUp(pieceX, pieceY + 2) == null) {
					temp = new Square(pieceX, pieceY + 2);
					moveList.add(temp);
				}
				if (!previousPieces.isEmpty() && !previousMoves.isEmpty()) {
					Piece previousPiece = previousPieces.peek();
					Square previousMove = previousMoves.peek();
					if (pieceY == 4 && previousPiece.getPieceType() == 0
							&& (previousPiece.getPieceY() - previousMove.getY()) == 2
							&& ((previousMove.getX() == (pieceX - 1)) || previousMove.getX() == (pieceX + 1))) {
						temp = new Square(previousMove.getX(), pieceY + 1);
						temp.setEnPassant(); // set the move to enpassant type if conditions for enpassant are met
						moveList.add(temp);
					}
				}
				break;
			case 1: // black pawn
				if (pieceLookUp(pieceX - 1, pieceY - 1) != null
						&& pieceLookUp(pieceX - 1, pieceY - 1).getPieceColour() != piece.getPieceColour()) {
					temp = new Square(pieceX - 1, pieceY - 1);
					if ((pieceY - 1) == 0) temp.setPromotion(); // set the move to promotion type if it's reached the end of the board
					moveList.add(temp);
				}
				if (pieceLookUp(pieceX + 1, pieceY - 1) != null
						&& pieceLookUp(pieceX + 1, pieceY - 1).getPieceColour() != piece.getPieceColour()) {
					temp = new Square(pieceX + 1, pieceY - 1);
					if ((pieceY - 1) == 0) temp.setPromotion(); // set the move to promotion type if it's reached the end of the board
					moveList.add(temp);
				}
				if (pieceLookUp(pieceX, pieceY - 1) == null) {
					temp = new Square(pieceX, pieceY - 1);
					if ((pieceY - 1) == 0) temp.setPromotion(); // set the move to promotion type if it's reached the end of the board
					moveList.add(temp);
				}
				if (pieceY == 6 && pieceLookUp(pieceX, pieceY - 1) == null && pieceLookUp(pieceX, pieceY - 2) == null) {
					temp = new Square(pieceX, pieceY - 2);
					moveList.add(temp);
				}
				if (!previousPieces.isEmpty() && !previousMoves.isEmpty()) {
					Piece previousPiece = previousPieces.peek();
					Square previousMove = previousMoves.peek();
					if (pieceY == 3 && previousPiece.getPieceType() == 0
							&& (previousMove.getY() - previousPiece.getPieceY()) == 2
							&& (previousMove.getX() == (pieceX - 1) || previousMove.getX() == (pieceX + 1))) {
						temp = new Square(previousMove.getX(), pieceY - 1);
						temp.setEnPassant();
						moveList.add(temp);
					}
				}
				break;
			}
			break;
		case 1: // king
			offsetList = getPieceOffset(piece);
			for (int i = 0; i < offsetList.size(); i++) {
				Square newMove = new Square(piece.getPieceX() + offsetList.get(i).getX(),
						piece.getPieceY() + offsetList.get(i).getY());
				Piece attackedPiece = pieceLookUp(newMove.getX(), newMove.getY());
				if (attackedPiece == null || attackedPiece.getPieceColour() != piece.getPieceColour()) {
					moveList.add(newMove);
				}
			}
			if (!piece.hasPieceMoved() && castleCheck == false) {
				switch (piece.getPieceColour()) {
				case 0:
					kingsideRook = pieceLookUp(7, 0);
					queensideRook = pieceLookUp(0, 0);

					kingsideCastle: 
						// label used to break loop to prevent unnecessary processing
						if (kingsideRook != null && !kingsideRook.hasPieceMoved()) {
							for (int i = 0; i < 2; i++) {
								temp = whiteKingsideSquares[i];
								System.out.println("castleCheck status: " + castleCheck);
								Piece squarePiece;
								squarePiece = pieceLookUp(temp.getX(), temp.getY());
								if (squarePiece != null) {
									break kingsideCastle;
								}
							}
							for (int j = 0; j < blackPiecesList.size(); j++) {
								temporaryMoveList = generateMoveList(blackPiecesList.get(j), true);
								if (temporaryMoveList != null) {
									totalMoveList.add(temporaryMoveList);
								}
							}
							for (int k = 0; k < blackPiecesList.size(); k++) {
								for (int l = 0; l < totalMoveList.get(k).size(); l++) {
									temp = totalMoveList.get(k).get(l);
									if (temp == whiteKingsideSquares[0] || temp == whiteKingsideSquares[1]) {
										System.out.println("reaches here");
										break kingsideCastle;
									}
								}
							}
							temp = new Square(6, 0);
							temp.setKingCastle();
							moveList.add(temp);
						}
					break;
				case 1:
					kingsideRook = pieceLookUp(7, 7);
					queensideRook = pieceLookUp(0, 7);
					break;
				}
			}
			break;
		case 2: // queen
			offsetList = getPieceOffset(piece);
			for (int i = 0; i < offsetList.size(); i++) {
				for (int j = 1; j < 8; j++) {
					Square newMove = new Square(piece.getPieceX() + offsetList.get(i).getX() * j,
							piece.getPieceY() + offsetList.get(i).getY() * j);
					Piece attackedPiece = pieceLookUp(newMove.getX(), newMove.getY());
					if (attackedPiece == null) {
						moveList.add(newMove);
					} else if (attackedPiece.getPieceColour() != piece.getPieceColour()) {
						moveList.add(newMove);
						break;
					} else {
						break;
					}
				}
			}
			break;
		case 3: // knight
			offsetList = getPieceOffset(piece);
			for (int i = 0; i < offsetList.size(); i++) {
				Square newMove = new Square(piece.getPieceX() + offsetList.get(i).getX(),
						piece.getPieceY() + offsetList.get(i).getY());
				Piece attackedPiece = pieceLookUp(newMove.getX(), newMove.getY());
				if (attackedPiece == null || attackedPiece.getPieceColour() != piece.getPieceColour()) {
					moveList.add(newMove);
				}
			}
			break;
		case 4: // bishop
			offsetList = getPieceOffset(piece);
			for (int i = 0; i < offsetList.size(); i++) {
				for (int j = 1; j < 8; j++) {
					Square newMove = new Square(piece.getPieceX() + offsetList.get(i).getX() * j,
							piece.getPieceY() + offsetList.get(i).getY() * j);
					Piece attackedPiece = pieceLookUp(newMove.getX(), newMove.getY());
					if (attackedPiece == null) {
						moveList.add(newMove);
					} else if (attackedPiece.getPieceColour() != piece.getPieceColour()) {
						moveList.add(newMove);
						break;
					} else {
						break;
					}
				}
			}
			break;
		case 5: // rook
			offsetList = getPieceOffset(piece);
			for (int i = 0; i < offsetList.size(); i++) {
				for (int j = 1; j < 8; j++) {
					Square newMove = new Square(piece.getPieceX() + offsetList.get(i).getX() * j,
							piece.getPieceY() + offsetList.get(i).getY() * j);
					Piece attackedPiece = pieceLookUp(newMove.getX(), newMove.getY());
					if (attackedPiece == null) {
						moveList.add(newMove);
					} else if (attackedPiece.getPieceColour() != piece.getPieceColour()) {
						moveList.add(newMove);
						break;
					} else {
						break;
					}
				}
			}
			break;
		}
		return moveList;
	}
}

public class Chess extends ApplicationAdapter {
	private Texture chessboard; // holds board image
	private Texture selectedSquareTexture; // holds selected square image
	// 0 = pawn, 1 = king, 2 = queen, 3 = knight, 4 = bishop, 5 = rook
	private Texture whitePieceTextures[] = new Texture[6]; // holds white piece textures
	private Texture blackPieceTextures[] = new Texture[6]; // holds black piece textures

	private ChessGame chessGame = new ChessGame(); // create a new instance of chessgame

	private ArrayList<Piece> whitePieces = new ArrayList<Piece>(); // holds white piece positions
	private ArrayList<Piece> blackPieces = new ArrayList<Piece>(); // holds black piece positions
	private Piece selectedPiece; // selected piece in GUI
	private Piece temporaryPiece; // placeholder piece

	private OrthographicCamera camera; // camera object
	private SpriteBatch batch; // spritebatch to draw textures onto the screen

	private Square projectedSquare = new Square(-100, -100); //
	private Square selectedMove = null;
	private Square tempSquare;

	private ArrayList<Square> moveList;

	private boolean moveMade = false;

	@Override
	public void create() {
		chessboard = new Texture(Gdx.files.internal("chessboard4.png"));
		selectedSquareTexture = new Texture(Gdx.files.internal("selectedSquare.png"));
		for (int i = 1; i <= 6; i++) {
			whitePieceTextures[i - 1] = new Texture(Gdx.files.internal("w" + i + ".png"));
			blackPieceTextures[i - 1] = new Texture(Gdx.files.internal("b" + i + ".png"));
		}
		camera = new OrthographicCamera(); // create camera object
		camera.setToOrtho(false, 480, 480); // set camera to 2d
		batch = new SpriteBatch(); // make a new sprite batch
	}

	@Override
	public void render() {
		whitePieces = chessGame.getWhitePieces();
		blackPieces = chessGame.getBlackPieces();
		ScreenUtils.clear(1, 1, 1, 1);
		camera.update(); // update camera
		batch.setProjectionMatrix(camera.combined); // boilerplate code necessary to draw image
		batch.begin();
		batch.draw(chessboard, 0, 0);
		batch.draw(selectedSquareTexture, projectedSquare.getX(), projectedSquare.getY());
		if (moveList != null) {
			for (int i = 0; i < moveList.size(); i++) {
				batch.draw(selectedSquareTexture, moveList.get(i).getX() * 60, moveList.get(i).getY() * 60);
			}
		}
		for (int i = 0; i < whitePieces.size(); i++) {
			batch.draw(whitePieceTextures[whitePieces.get(i).getPieceType()], whitePieces.get(i).getPieceX() * 60,
					whitePieces.get(i).getPieceY() * 60);
		}
		for (int j = 0; j < blackPieces.size(); j++) {
			batch.draw(blackPieceTextures[blackPieces.get(j).getPieceType()], blackPieces.get(j).getPieceX() * 60,
					blackPieces.get(j).getPieceY() * 60);
		}
		batch.end();
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3(); // boilerplate code for input processing taken from documentation
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			int a = (int) (touchPos.x - (touchPos.x % 60));
			int b = (int) (touchPos.y - (touchPos.y % 60));
			tempSquare = new Square(a / 60, b / 60);
			temporaryPiece = chessGame.pieceLookUp(tempSquare.getX(), tempSquare.getY());
			if (temporaryPiece != null && temporaryPiece.getPieceColour() == chessGame.getCurrentTurn()) {
				selectedPiece = temporaryPiece;
				moveList = chessGame.generateMoveList(selectedPiece, false);
				projectedSquare = new Square(a, b);
				selectedMove = null;
			} else {
				selectedMove = tempSquare;
			}
			if (selectedPiece != null && selectedMove != null) {
				for (int i = 0; i < moveList.size(); i++) {
					try {
						if (moveList.get(i).getX() == selectedMove.getX()
								&& moveList.get(i).getY() == selectedMove.getY()) {
							System.out.println(selectedPiece);
							selectedMove = moveList.get(i);
							System.out.println(selectedMove);
							moveMade = true;
						}
					} catch (Exception e) {
						System.out.println(moveList.size());
					}

				}
			}
		}
		if (moveMade) {
			chessGame.movePiece(selectedPiece, selectedMove);
			moveList = null;
			selectedPiece = null;
			selectedMove = null;
			moveMade = false;
			chessGame.nextTurn();
			System.out.println("Move made, next turn is " + chessGame.getCurrentTurn());
		}
	}

	@Override
	public void dispose() {
		chessboard.dispose();
		batch.dispose();
		selectedSquareTexture.dispose();
		for (int i = 1; i <= 6; i++) {
			whitePieceTextures[i - 1].dispose();
			blackPieceTextures[i - 1].dispose();
		}

	}
}