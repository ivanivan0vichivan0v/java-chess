package com.mygdx.game;

import java.util.ArrayList;
import java.util.ListIterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;

class Square {
	private int x;
	private int y;

	public Square(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public String toString() {
		String square = "";
		square = square + " X:" + this.x + " Y:" + this.y;
		return square;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}

class Piece {
	private int x; // x pos of piece
	private int y; // y pos of piece
	private int type; // 0 = pawn, 1 = king, 2 = queen, 3 = knight, 4 = bishop, 5 = rook
	private int colour; // 0 = white, 1 = black

	public Piece(int x, int y, int type, int colour) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.colour = colour;
	}

	public String toString() {
		String piece = "";
		piece = piece + " X:" + this.x + " Y:" + this.y + " Type:" + this.type + " Colour:" + this.colour;
		return piece;
	}

	public int getPieceX() {
		return this.x;
	}

	public int getPieceY() {
		return this.y;
	}

	public int getPieceType() {
		return this.type;
	}

	public int getPieceColour() {
		return this.colour;
	}

	public void setPieceX(int x) {
		this.x = x;
	}

	public void setPieceY(int y) {
		this.y = y;
	}
}

class ChessGame {
	private int turn; // 0 = white, 1 = black
	private Piece[] whitePieces = new Piece[16];
	private Piece[] blackPieces = new Piece[16];

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
	}

	public Piece[] getWhitePieces() {
		return this.whitePieces;
	}

	public Piece[] getBlackPieces() {
		return this.blackPieces;
	}

	public int getCurrentTurn() {
		return this.turn;
	}

	public void nextTurn() {
		if (this.turn == 0)
			this.turn = 1;
		else
			this.turn = 0;
	}

	// find the piece at a given location
	public Piece pieceLookUp(int x, int y) {
		Piece piece = null;
		for (int i = 0; i < 16; i++) {
			if (whitePieces[i].getPieceX() == x && whitePieces[i].getPieceY() == y) {
				return whitePieces[i];
			}
			if (blackPieces[i].getPieceX() == x && blackPieces[i].getPieceY() == y) {
				return blackPieces[i];
			}
		}
		return piece;
	}

	// remove a piece at a given location
	public void removePiece(int x, int y) {
		for (int i = 0; i < 16; i++) {
			if (whitePieces[i].getPieceX() == x && whitePieces[i].getPieceY() == y) {
				whitePieces[i] = null;
			}
			if (blackPieces[i].getPieceX() == x && blackPieces[i].getPieceY() == y) {
				blackPieces[i] = null;
			}
		}
	}

	// modify a game piece
	public void modifyPiece(Square originalPos, Square newPos) {
		for (int i = 0; i < 16; i++) {
			if (whitePieces[i].getPieceX() == originalPos.getX() && whitePieces[i].getPieceY() == originalPos.getY()) {
				whitePieces[i].setPieceX(newPos.getX());
				whitePieces[i].setPieceY(newPos.getY());
			}
			if (blackPieces[i].getPieceX() == originalPos.getX() && blackPieces[i].getPieceY() == originalPos.getY()) {
				blackPieces[i].setPieceX(newPos.getX());
				blackPieces[i].setPieceY(newPos.getY());
			}
		}
	}

	public void movePiece(Piece piece, Square move) {
		Piece attackedPiece = pieceLookUp(move.getX(), move.getY());
		if (attackedPiece != null) {
			removePiece(attackedPiece.getPieceX(), attackedPiece.getPieceY());
		}
		Square originalPos = new Square(piece.getPieceX(), piece.getPieceY());
		modifyPiece(originalPos, move);
	}

	public ArrayList<Square> getPieceOffset(Piece piece) {
		ArrayList<Square> offsetList = new ArrayList<Square>();
		int pieceX = piece.getPieceX();
		int pieceY = piece.getPieceY();

		switch (piece.getPieceType()) {
		case 1:

			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		}
		return offsetList;
	}

	// movePiece()
	// returns all legal moves for a certain piece
	public ArrayList<Square> generateMoveList(Piece piece) { // TODO: find a more efficient way of doing this
		if (piece == null) {
			return null;
		}
		ArrayList<Square> moveList = new ArrayList<Square>();
		int pieceX = piece.getPieceX();
		int pieceY = piece.getPieceY();
		Square temp = null;
		switch (piece.getPieceType()) {
		case 0: // if piece == pawn
			switch (piece.getPieceColour()) { // absolutely dreadful code
			case 0:
				if (pieceLookUp(pieceX - 1, pieceY + 1) != null) {
					temp = new Square(pieceX - 1, pieceY + 1);
					moveList.add(temp);
				}
				if (pieceLookUp(pieceX + 1, pieceY + 1) != null) {
					temp = new Square(pieceX + 1, pieceY + 1);
					moveList.add(temp);
				}
				if (pieceLookUp(pieceX, pieceY + 1) == null) {
					temp = new Square(pieceX, pieceY + 1);
					moveList.add(temp);
				}
				if (pieceY == 1 && pieceLookUp(pieceX, pieceY + 1) == null && pieceLookUp(pieceX, pieceY + 2) == null) {
					temp = new Square(pieceX, pieceY + 2);
					moveList.add(temp);
				}
				break;
			case 1:
				if (pieceLookUp(pieceX - 1, pieceY - 1) != null) {
					temp = new Square(pieceX - 1, pieceY - 1);
					moveList.add(temp);
				}
				if (pieceLookUp(pieceX + 1, pieceY - 1) != null) {
					temp = new Square(pieceX + 1, pieceY - 1);
					moveList.add(temp);
				}
				if (pieceLookUp(pieceX, pieceY - 1) == null) {
					temp = new Square(pieceX, pieceY - 1);
					moveList.add(temp);
				}
				if (pieceY == 6 && pieceLookUp(pieceX, pieceY - 1) == null && pieceLookUp(pieceX, pieceY - 2) == null) {
					temp = new Square(pieceX, pieceY - 2);
					moveList.add(temp);
				}
				break;
			}
			break;
		case 1:

			break;
		case 2:

			break;
		case 3:

			break;
		case 4:

			break;
		case 5:

			break;
		}
		return moveList;
	}
}

public class Chess extends ApplicationAdapter {
	private Texture chessboard;
	private Texture selectedSquareTexture;
	// 0 = pawn, 1 = king, 2 = queen, 3 = knight, 4 = bishop, 5 = rook
	private Texture whitePieceTextures[] = new Texture[6];
	private Texture blackPieceTextures[] = new Texture[6];

	private ChessGame chessGame = new ChessGame();

	private Piece whitePieces[] = new Piece[16];
	private Piece blackPieces[] = new Piece[16];
	private Piece selectedPiece;
	private Piece temporaryPiece;
	
	private OrthographicCamera camera;
	private SpriteBatch batch;

	private Square projectedSquare = new Square(-100, -100);
	private Square selectedSquare;
	private Square selectedMove = null;
	private Square tempSquare;

	private ArrayList<Square> moveList;

	@Override
	public void create() {
		chessboard = new Texture(Gdx.files.internal("chessboard4.png"));
		selectedSquareTexture = new Texture(Gdx.files.internal("selectedSquare.png"));
		for (int i = 1; i <= 6; i++) {
			whitePieceTextures[i - 1] = new Texture(Gdx.files.internal("w" + i + ".png"));
			blackPieceTextures[i - 1] = new Texture(Gdx.files.internal("b" + i + ".png"));
		}
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 480, 480);
		batch = new SpriteBatch();
	}

	@Override
	public void render() {
		whitePieces = chessGame.getWhitePieces();
		blackPieces = chessGame.getBlackPieces();
		ScreenUtils.clear(1, 1, 1, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(chessboard, 0, 0);
		batch.draw(selectedSquareTexture, projectedSquare.getX(), projectedSquare.getY());
		if (moveList != null) {
			for (int i = 0; i < moveList.size(); i++) {
				batch.draw(selectedSquareTexture, moveList.get(i).getX() * 60, moveList.get(i).getY() * 60);
			}
		}
		for (int i = 0; i < 16; i++) {
			batch.draw(whitePieceTextures[whitePieces[i].getPieceType()], whitePieces[i].getPieceX() * 60,
					whitePieces[i].getPieceY() * 60);
			batch.draw(blackPieceTextures[blackPieces[i].getPieceType()], blackPieces[i].getPieceX() * 60,
					blackPieces[i].getPieceY() * 60);
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
			if (temporaryPiece != null
					&& temporaryPiece.getPieceColour() == chessGame.getCurrentTurn()) {
				selectedPiece = temporaryPiece;
				moveList = chessGame.generateMoveList(selectedPiece);
				projectedSquare = new Square(a, b);
				selectedMove = null;
			}
			else {
				selectedMove = tempSquare;
			}
			if (selectedPiece != null && selectedMove != null) {
				for (int i = 0; i < moveList.size(); i++) {
					try {
						if (moveList.get(i).getX() == selectedMove.getX() && moveList.get(i).getY() == selectedMove.getY()) {
							System.out.println(selectedPiece);
							System.out.println(selectedMove);
							chessGame.movePiece(selectedPiece, selectedMove);
							selectedMove = null;
							chessGame.nextTurn();
						}
					}
					catch (Exception e) { 
						e.printStackTrace();
					}
					
				}
			}
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