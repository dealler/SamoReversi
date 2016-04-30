package bl;

import bl.TKindEnum.TKind;

//import Move;
//import Reversi;
//import java.awt.Cursor;
//import java.awt.event.MouseEvent;
//import javax.swing.JOptionPane;


public class SamoReversiGame {
	
	private Board board = null;	
	private Player black = null;
	private Player white = null;
	private boolean vsComputer=true;
	private int ratingComputer=3;
	private int winner=0;//0-tie ; 1-player 1 won ; 2-player 2 won 

	private static SamoReversiGame theGame = null;
	
	/**
	 * Singleton
	 */
	private SamoReversiGame() {
		setSettings(new Board(8), "player1", "player2",true);
	}

	public static SamoReversiGame getInstance(){
		if (theGame == null){
			theGame = new SamoReversiGame();
		}
		return theGame;
	}
	
	public void setSettings(Board board, String player1Name, String player2Name,boolean computer){
		setBoard(board);
		setBlackPlayer(new Player(player1Name));
		setWhitePlayer(new Player(player2Name));
		setVsComputer(computer);
	}
	
	public void setBlackPlayerScore(){
		getBlackPlayer().setScore(board.getCounter(TKind.black));
	}

	public void setWhitePlayerScore(){
		getBlackPlayer().setScore(board.getCounter(TKind.white));
	}
	
	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public Player getBlackPlayer() {
		return black;
	}

	public void setBlackPlayer(Player player1) {
		this.black = player1;
	}

	public Player getWhitePlayer() {
		return white;
	}

	public void setWhitePlayer(Player player2) {
		this.white = player2;
	}
	public boolean isVsComputer() {
		return vsComputer;
	}

	public void setVsComputer(boolean vsComputer) {
		this.vsComputer = vsComputer;
	}
	public int getRatingComputer() {
		return ratingComputer;
	}

	public void setRatingComputer(int ratingComputer) {
		this.ratingComputer = ratingComputer;
	}
	public int getWinner() {
		return winner;
	}

	public void setWinner(int winner) {
		this.winner = winner;
	}
}
