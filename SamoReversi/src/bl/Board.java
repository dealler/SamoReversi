package bl;

import java.io.Serializable;
import java.util.Random;
import bl.TKindEnum.TKind;

public class Board implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2885666332364819427L;

	private int size = 8;

	private TKind[][] board;
	public int[] counter = new int[2]; // 0 = black, 1 = white
	private boolean PassCounter;

	public Board() {
		board = new TKind[size][size];
		clear();
	}

	public Board(int size) {
		setSize(size);
		board = new TKind[getSize()][getSize()];
		clear();
	}

	public Board(Board b) {
		this(b.getSize());
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				this.board[i][j] = b.board[i][j];
			}

		// Scores re-update:
		this.counter[0] = b.counter[0];
		this.counter[1] = b.counter[1];
	}

	public TKind get(int i, int j) {
		return board[i][j];
	}

	public void set(Move move, TKind player) {
		switch (board[move.i][move.j]) {
		case white:
			counter[1]--;
			break;
		case black:
			counter[0]--;
			break;
		}
		board[move.i][move.j] = player;
		switch (player) {
		case white:
			counter[1]++;
			break;
		case black:
			counter[0]++;
			break;
		}
	}

	public int getCounter(TKind player) {
		return counter[player.ordinal() - 1];
	}

	public void clear() {
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				System.out.println("nil: " + i + "," + j);
				board[i][j] = TKind.nil;
			}

		board[size / 2 - 1][size / 2] = TKind.black;
		board[size / 2][size / 2 - 1] = TKind.black;
		board[size / 2 - 1][size / 2 - 1] = TKind.white;
		board[size / 2][size / 2] = TKind.white;

		counter[0] = 2;
		counter[1] = 2;
		PassCounter = false;
	}

	public void println() {
		System.out.print("[");
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++)
				System.out.print(board[i][j] + ",");
			System.out.println((i == size - 1 ? "]" : ""));
		}
	}

	public int move(Move move, TKind kind) {
		return checkBoard(move, kind);
	}

	public boolean gameEnd() {
		return counter[0] + counter[1] == size * size;
	}

	private int Check(Move move, int incx, int incy, TKind kind, boolean set) {
		TKind opponent;
		int x = move.i;
		int y = move.j;
		if (kind == TKind.black)
			opponent = TKind.white;
		else
			opponent = TKind.black;
		int n_inc = 0;
		x += incx;
		y += incy;
		while ((x < size) && (x >= 0) && (y < size) && (y >= 0)
				&& (board[x][y] == opponent)) {
			x += incx;
			y += incy;
			n_inc++;
		}
		if ((n_inc != 0) && (x < size) && (x >= 0) && (y < size) && (y >= 0)
				&& (board[x][y] == kind)) {
			if (set)
				for (int j = 1; j <= n_inc; j++) {
					x -= incx;
					y -= incy;
					set(new Move(x, y), kind);
				}
			return n_inc;
		} else
			return 0;
	}

	private int checkBoard(Move move, TKind kind) {
		// check increasing x
		int j = Check(move, 1, 0, kind, true);
		// check decreasing x
		j += Check(move, -1, 0, kind, true);
		// check increasing y
		j += Check(move, 0, 1, kind, true);
		// check decreasing y
		j += Check(move, 0, -1, kind, true);
		// check diagonals
		j += Check(move, 1, 1, kind, true);
		j += Check(move, -1, 1, kind, true);
		j += Check(move, 1, -1, kind, true);
		j += Check(move, -1, -1, kind, true);
		if (j != 0)
			set(move, kind);
		return j;
	}

	public boolean isValid(Move move, TKind kind) {
		// check increasing x
		if (Check(move, 1, 0, kind, false) != 0)
			return true;
		// check decreasing x
		if (Check(move, -1, 0, kind, false) != 0)
			return true;
		// check increasing y
		if (Check(move, 0, 1, kind, false) != 0)
			return true;
		// check decreasing y
		if (Check(move, 0, -1, kind, false) != 0)
			return true;
		// check diagonals
		if (Check(move, 1, 1, kind, false) != 0)
			return true;
		if (Check(move, -1, 1, kind, false) != 0)
			return true;
		if (Check(move, 1, -1, kind, false) != 0)
			return true;
		if (Check(move, -1, -1, kind, false) != 0)
			return true;
		return false;
	}

	private int strategy(TKind me, TKind opponent) {
		int tstrat = 0;
		for (int i = 0; i < size; i++)
			if (board[i][0] == opponent)
				tstrat++;
			else if (board[i][0] == me)
				tstrat--;
		for (int i = 0; i < size; i++)
			if (board[i][size - 1] == opponent)
				tstrat++;
			else if (board[i][size - 1] == me)
				tstrat--;
		for (int i = 0; i < size; i++)
			if (board[0][i] == opponent)
				tstrat++;
			else if (board[0][i] == me)
				tstrat--;
		for (int i = 0; i < size; i++)
			if (board[size - 1][i] == opponent)
				tstrat++;
			else if (board[size - 1][i] == me)
				tstrat--;
		return tstrat;
	}

	private class resultFindMax {
		int max, nb, nw;
	};

	private resultFindMax FindMax(int level, TKind me, TKind opponent) {
		int min, score, tnb, tnw;
		TKind[][] TempBoard = new TKind[size][size];
		int[] TempCounter = new int[2];
		resultFindMax res = new resultFindMax();
		level--;
		res.nb = counter[0];
		res.nw = counter[1];
		for (int i = 0; i < size; i++)
			System.arraycopy(board[i], 0, TempBoard[i], 0, size);
		System.arraycopy(counter, 0, TempCounter, 0, 2);
		min = 10000; // high value

		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				if ((board[i][j] == TKind.nil)
						&& (checkBoard(new Move(i, j), me) != 0)) {
					if (level != 0) {
						resultFindMax tres = FindMax(level, opponent, me);
						tnb = tres.nb;
						tnw = tres.nw;
						score = tres.max;
					} else {
						tnb = counter[0];
						tnw = counter[1];
						score = counter[opponent.ordinal() - 1]
								- counter[me.ordinal() - 1]
								+ strategy(me, opponent);
					}
					if (min > score) {
						min = score;
						res.nb = tnb;
						res.nw = tnw;
					}
					for (int k = 0; k < size; k++)
						System.arraycopy(TempBoard[k], 0, board[k], 0, size);
					System.arraycopy(TempCounter, 0, counter, 0, 2);
				}
		res.max = -min;
		return res;
	}

	/**
	 * The output move return as parmater.
	 * 
	 * @param player
	 * @param llevel
	 * @param move
	 * @return
	 */
	public boolean findMove(TKind player, int llevel, Move move) {
		TKind[][] TempBoard = new TKind[size][size];
		int[] TempCounter = new int[2];
		int nb, nw, min, n_min;
		boolean found;
		resultFindMax res = new resultFindMax();
		Random random = new Random();

		if (counter[0] + counter[1] >= 52 + llevel) {
			llevel = counter[0] + counter[1] - 52;
			if (llevel > 5)
				llevel = 5;
		}

		for (int i = 0; i < size; i++)
			System.arraycopy(board[i], 0, TempBoard[i], 0, size);
		System.arraycopy(counter, 0, TempCounter, 0, 2);
		found = false;
		min = 10000; // high value
		n_min = 1;
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				if ((board[i][j] == TKind.nil)
						&& (checkBoard(new Move(i, j), player) != 0)) {
					if (player == TKind.black)
						res = FindMax(llevel - 1, TKind.white, player);
					else
						res = FindMax(llevel - 1, TKind.black, player);
					if ((!found) || (min > res.max)) {
						min = res.max;
						nw = res.nw;
						nb = res.nb;
						move.i = i;
						move.j = j;
						found = true;
					} else if (min == res.max) { // RANDOM MOVE GENERATOR
						n_min++;
						if (random.nextInt(n_min) == 0) {
							nw = res.nw;
							nb = res.nb;
							move.i = i;
							move.j = j;
						}
					}
					// if found
					// then PreView(nw,nb);
					for (int k = 0; k < size; k++)
						System.arraycopy(TempBoard[k], 0, board[k], 0, size);
					System.arraycopy(TempCounter, 0, counter, 0, 2);
				}
		return found;
	}

	public boolean userCanMove(TKind player) {
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				if ((board[i][j] == TKind.nil)
						&& isValid(new Move(i, j), player))
					return true;
		return false;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean equals(Board b) {
		if(this.counter[0]==b.getCounter(TKind.black)&&
		   this.counter[1]==b.getCounter(TKind.white)&&
		   this.size==b.getSize()){
			for (int i = 0; i < size; i++){
				for (int j = 0; j < size; j++) {
					if(this.board[i][j] != b.board[i][j]){
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}
	

}