package samo.reversi;

import java.io.Externalizable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import bl.Board;
import bl.Move;
import bl.Player;
import bl.SamoReversiGame;
import bl.TKindEnum.TKind;
//import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

//@SuppressLint("SimpleDateFormat")
public class SamoGameBoard extends Activity implements Externalizable {
	private TextView score_black;
	private TextView score_white;
	private TextView player_1_name;
	private TextView player_2_name;
	private int playerturn = 1;// 1-player 1 turn ; 2-player 2 turn
	private ArrayList<Integer> playerpreviousturn = new ArrayList<Integer>();
	private ArrayList<Boolean> playerVsComputerTurn = new ArrayList<Boolean>();
	private int level;
	private TimerTask scanTask;
	private Handler handler = new Handler();
	private Timer t = new Timer();
	private boolean playermove = false;
	private boolean isComputerFinish = true;
	private boolean gamefinished = false;
	private int countshow = 0;
	public static List<Board> previousBoard = new ArrayList<Board>(); // for
																		// Undo
																		// and
																		// History
																		// features!
	public/* static */String timeStamp; // Date
	private static int historyImgCounter = 1;
	private static final long serialVersionUID = -1728959499L;

	/**
	 * The game flow is according to the following 2 steps: 1. select your item
	 * 2.select where to move it. This variable indictates step 2 if true
	 * otherwise step 1.
	 */
	// protected static boolean vsComputer = true;
	private Button undo;
	private ImageButton matrixButton00;
	private ImageButton matrixButton01;
	private ImageButton matrixButton02;
	private ImageButton matrixButton03;
	private ImageButton matrixButton04;
	private ImageButton matrixButton05;
	private ImageButton matrixButton06;
	private ImageButton matrixButton07;
	private ImageButton matrixButton10;
	private ImageButton matrixButton11;
	private ImageButton matrixButton12;
	private ImageButton matrixButton13;
	private ImageButton matrixButton14;
	private ImageButton matrixButton15;
	private ImageButton matrixButton16;
	private ImageButton matrixButton17;
	private ImageButton matrixButton20;
	private ImageButton matrixButton21;
	private ImageButton matrixButton22;
	private ImageButton matrixButton23;
	private ImageButton matrixButton24;
	private ImageButton matrixButton25;
	private ImageButton matrixButton26;
	private ImageButton matrixButton27;
	private ImageButton matrixButton30;
	private ImageButton matrixButton31;
	private ImageButton matrixButton32;
	private ImageButton matrixButton33;
	private ImageButton matrixButton34;
	private ImageButton matrixButton35;
	private ImageButton matrixButton36;
	private ImageButton matrixButton37;
	private ImageButton matrixButton40;
	private ImageButton matrixButton41;
	private ImageButton matrixButton42;
	private ImageButton matrixButton43;
	private ImageButton matrixButton44;
	private ImageButton matrixButton45;
	private ImageButton matrixButton46;
	private ImageButton matrixButton47;
	private ImageButton matrixButton50;
	private ImageButton matrixButton51;
	private ImageButton matrixButton52;
	private ImageButton matrixButton53;
	private ImageButton matrixButton54;
	private ImageButton matrixButton55;
	private ImageButton matrixButton56;
	private ImageButton matrixButton57;
	private ImageButton matrixButton60;
	private ImageButton matrixButton61;
	private ImageButton matrixButton62;
	private ImageButton matrixButton63;
	private ImageButton matrixButton64;
	private ImageButton matrixButton65;
	private ImageButton matrixButton66;
	private ImageButton matrixButton67;
	private ImageButton matrixButton70;
	private ImageButton matrixButton71;
	private ImageButton matrixButton72;
	private ImageButton matrixButton73;
	private ImageButton matrixButton74;
	private ImageButton matrixButton75;
	private ImageButton matrixButton76;
	private ImageButton matrixButton77;

	// In Computer VS User: Black = User, White = Computer.

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		if (SamoReversiGame.getInstance().getBoard().getSize() == 8) {
			setContentView(R.layout.gameplay);
		} else if (SamoReversiGame.getInstance().getBoard().getSize() == 6) {
			setContentView(R.layout.gameplay6x6);
		} else if (SamoReversiGame.getInstance().getBoard().getSize() == 4) {
			setContentView(R.layout.gameplay4x4);
		}
		
		Button nextHistoryStep = (Button)findViewById(R.id.historynextstep);
		nextHistoryStep.setVisibility(View.GONE);
		Button prevHistoryStep = (Button)findViewById(R.id.historyprevstep);
		prevHistoryStep.setVisibility(View.GONE);
		
		Button undoBut = (Button)findViewById(R.id.butundo);
		undoBut.setVisibility(View.VISIBLE);
		
		undoBut.invalidate();
		nextHistoryStep.invalidate();
		prevHistoryStep.invalidate();
		
		player_1_name = (TextView) findViewById(R.id.txtBlack);
		player_2_name = (TextView) findViewById(R.id.txtWhite);

		// Initializing board towards new game.
		if (SamoReversiGame.getInstance().isVsComputer() && !(SamoHistoryActivity.isHistoryOn)) {
			player_1_name.setText(SamoReversiGame.getInstance().getBlackPlayer().getName());
			SamoReversiGame.getInstance().getWhitePlayer().setName("Computer");
			player_2_name.setText(SamoReversiGame.getInstance().getWhitePlayer().getName());
		} else {
			player_1_name.setText(SamoReversiGame.getInstance().getBlackPlayer().getName());
			player_2_name.setText(SamoReversiGame.getInstance().getWhitePlayer().getName());
		}
		SamoReversiGame.getInstance().getBoard().clear();
		drawBLCurrentMatrix();

		scanTask = new TimerTask() {
			public void run() {

			try {

					handler.post(new Runnable() {
						public void run() {
							if (gamefinished) {
								if (countshow == 1) {
									scanTask.cancel();
									showWinner();
								} else {
									countshow++;
								}
							} else if (playermove) {
								computerMove();
								playermove = false;
								drawBLCurrentMatrix();
							}
						}
					});
				} catch(Exception e){
				System.out.println(e.getMessage());
			}
			}
		};
		
		// Checking modes: history or regular:
		if (SamoHistoryActivity.isHistoryOn) {
				final int lastImg;
				Iterator<Board> iter = previousBoard.iterator();
				int k=0;
				while (iter.hasNext()){
					if (iter.next() == null) {
						break;
					}
					k++;
				}
				lastImg  = k-1; // Since from 0
				
				historyImgCounter = lastImg;
				undoBut = (Button)findViewById(R.id.butundo);
				undoBut.setVisibility(View.GONE);

				nextHistoryStep = (Button)findViewById(R.id.historynextstep);
				nextHistoryStep.setVisibility(View.VISIBLE);
				nextHistoryStep.invalidate();
				
				prevHistoryStep = (Button)findViewById(R.id.historyprevstep);
				prevHistoryStep.setVisibility(View.VISIBLE);
				prevHistoryStep.invalidate();
				SamoReversiGame.getInstance().setBoard(previousBoard.get(historyImgCounter));
				drawBLCurrentMatrix();
				
				prevHistoryStep.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v){	
					try{
						if (historyImgCounter!=lastImg){
							historyImgCounter++;
							SamoReversiGame.getInstance().setBoard(previousBoard.get(historyImgCounter));
							drawBLCurrentMatrix();
							
							score_black = (TextView) findViewById(R.id.score_black);
							score_white = (TextView) findViewById(R.id.score_white);
							score_black.setText(Integer.toString(SamoReversiGame.getInstance().getBoard().getCounter(TKind.black)));
							score_white.setText(Integer.toString(SamoReversiGame.getInstance().getBoard().getCounter(TKind.white)));
							
							//drawHistoryMatrix();
						}else{
							//historyImgCounter = 1;
							SamoReversiGame.getInstance().setBoard(previousBoard.get(historyImgCounter));
							drawBLCurrentMatrix();
							score_black = (TextView) findViewById(R.id.score_black);
							score_white = (TextView) findViewById(R.id.score_white);
							score_black.setText(Integer.toString(SamoReversiGame.getInstance().getBoard().getCounter(TKind.black)));
							score_white.setText(Integer.toString(SamoReversiGame.getInstance().getBoard().getCounter(TKind.white)));
							Toast.makeText(SamoGameBoard.this, "Begining of History Game", Toast.LENGTH_SHORT).show();
						}
					}catch(Exception e){
						System.out.println(e.getMessage());	
					}					
				}					
				});


				nextHistoryStep.setOnClickListener(new View.OnClickListener() {			
					@Override
					public void onClick(View v) {
						try{
							
							if (historyImgCounter!=1){
								historyImgCounter--;
								SamoReversiGame.getInstance().setBoard(previousBoard.get(historyImgCounter));
								drawBLCurrentMatrix();
								
								score_black = (TextView) findViewById(R.id.score_black);
								score_white = (TextView) findViewById(R.id.score_white);
								score_black.setText(Integer.toString(SamoReversiGame.getInstance().getBoard().getCounter(TKind.black)));
								score_white.setText(Integer.toString(SamoReversiGame.getInstance().getBoard().getCounter(TKind.white)));
								
								//drawHistoryMatrix();
							}else{
								//historyImgCounter = lastImg;
								SamoReversiGame.getInstance().setBoard(previousBoard.get(historyImgCounter));
								drawBLCurrentMatrix();
								
								score_black = (TextView) findViewById(R.id.score_black);
								score_white = (TextView) findViewById(R.id.score_white);
								score_black.setText(Integer.toString(SamoReversiGame.getInstance().getBoard().getCounter(TKind.black)));
								score_white.setText(Integer.toString(SamoReversiGame.getInstance().getBoard().getCounter(TKind.white)));
								Toast.makeText(SamoGameBoard.this, "End of History Game", Toast.LENGTH_SHORT).show();
							}
						}catch(Exception e){
							System.out.println(e.getMessage());	
						}					
					}
				});
				
			
		} else { // Regular:

			// Getting current game date:
			previousBoard.clear();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
			timeStamp = sdf.format(new Date());

			// Vibrate for 300 milliseconds
			// if (SamoReversiGame.getInstance().getBoard().getSize() == 8) {
			// setContentView(R.layout.gameplay);
			// } else if (SamoReversiGame.getInstance().getBoard().getSize() ==
			// 6) {
			// setContentView(R.layout.gameplay6x6);
			// } else if (SamoReversiGame.getInstance().getBoard().getSize() ==
			// 4) {
			// setContentView(R.layout.gameplay4x4);
			// }
			// player_1_name = (TextView) findViewById(R.id.txtBlack);
			// player_2_name = (TextView) findViewById(R.id.txtWhite);
			//
			// // Initializing board towards new game.
			// if (SamoReversiGame.getInstance().isVsComputer()) {
			// player_1_name.setText(SamoReversiGame.getInstance()
			// .getBlackPlayer().getName());
			// SamoReversiGame.getInstance().getWhitePlayer()
			// .setName("Computer");
			// player_2_name.setText(SamoReversiGame.getInstance()
			// .getWhitePlayer().getName());
			// } else {
			// player_1_name.setText(SamoReversiGame.getInstance()
			// .getBlackPlayer().getName());
			// player_2_name.setText(SamoReversiGame.getInstance()
			// .getWhitePlayer().getName());
			// }
			// SamoReversiGame.getInstance().getBoard().clear();
			level = SamoReversiGame.getInstance().getRatingComputer();

			drawBLCurrentMatrix();

			Toast.makeText(SamoGameBoard.this, "Fight", Toast.LENGTH_SHORT).show();

			undo = (Button) findViewById(R.id.butundo);
			undo.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!(previousBoard.isEmpty())) {
						if(SamoReversiGame.getInstance().isVsComputer())
						{//cheacing if this game is vs computer
							//playerVsComputerTurn.remove(playerVsComputerTurn.size()-1);
							while(playerVsComputerTurn.remove(playerVsComputerTurn.size()-1)){	
								previousBoard.remove(previousBoard.size() - 1);
							}
						}
						SamoReversiGame.getInstance().setBoard(
								previousBoard.remove(previousBoard.size() - 1));
						score_black = (TextView) findViewById(R.id.score_black);
						score_white = (TextView) findViewById(R.id.score_white);
						score_black.setText(Integer.toString(SamoReversiGame
								.getInstance().getBoard()
								.getCounter(TKind.black)));
						score_white.setText(Integer.toString(SamoReversiGame
								.getInstance().getBoard()
								.getCounter(TKind.white)));
						if (!SamoReversiGame.getInstance().isVsComputer()) {
							playerturn = playerpreviousturn
									.remove(playerpreviousturn.size() - 1);
						}
						drawBLCurrentMatrix();

					}
				}
			});

			// The buttons and their Listeners:
			// Which board:

			switch (SamoReversiGame.getInstance().getBoard().getSize()) {
			case 8: {
				// Row 0:=======
				matrixButton00 = (ImageButton) findViewById(R.id.butboard_8_mat_00);
				matrixButton00.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(0, 0); // If vs Computer the computer plays
										// after
										// user
										// within userMove.
					}
				});

				matrixButton01 = (ImageButton) findViewById(R.id.butboard_8_mat_01);
				matrixButton01.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(0, 1);
					}
				});

				matrixButton02 = (ImageButton) findViewById(R.id.butboard_8_mat_02);
				matrixButton02.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(0, 2);
					}
				});

				matrixButton03 = (ImageButton) findViewById(R.id.butboard_8_mat_03);
				matrixButton03.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(0, 3);
					}
				});

				matrixButton04 = (ImageButton) findViewById(R.id.butboard_8_mat_04);
				matrixButton04.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(0, 4);
					}
				});

				matrixButton05 = (ImageButton) findViewById(R.id.butboard_8_mat_05);
				matrixButton05.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(0, 5);
					}
				});

				matrixButton06 = (ImageButton) findViewById(R.id.butboard_8_mat_06);
				matrixButton06.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(0, 6);
					}
				});

				matrixButton07 = (ImageButton) findViewById(R.id.butboard_8_mat_07);
				matrixButton07.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(0, 7);
					}
				});

				// Row 1:======
				matrixButton10 = (ImageButton) findViewById(R.id.butboard_8_mat_10);
				matrixButton10.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(1, 0); // If vs Computer the computer plays
										// after
										// user
										// within userMove.
					}
				});

				matrixButton11 = (ImageButton) findViewById(R.id.butboard_8_mat_11);
				matrixButton11.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(1, 1);
					}
				});

				matrixButton12 = (ImageButton) findViewById(R.id.butboard_8_mat_12);
				matrixButton12.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(1, 2);
					}
				});

				matrixButton13 = (ImageButton) findViewById(R.id.butboard_8_mat_13);
				matrixButton13.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(1, 3);
					}
				});

				matrixButton14 = (ImageButton) findViewById(R.id.butboard_8_mat_14);
				matrixButton14.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(1, 4);
					}
				});

				matrixButton15 = (ImageButton) findViewById(R.id.butboard_8_mat_15);
				matrixButton15.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(1, 5);
					}
				});

				matrixButton16 = (ImageButton) findViewById(R.id.butboard_8_mat_16);
				matrixButton16.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(1, 6);
					}
				});

				matrixButton17 = (ImageButton) findViewById(R.id.butboard_8_mat_17);
				matrixButton17.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(1, 7);
					}
				});

				// Row 2:======
				matrixButton20 = (ImageButton) findViewById(R.id.butboard_8_mat_20);
				matrixButton20.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(2, 0); // If vs Computer the computer plays
										// after
										// user
										// within userMove.
					}
				});

				matrixButton21 = (ImageButton) findViewById(R.id.butboard_8_mat_21);
				matrixButton21.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(2, 1);
					}
				});

				matrixButton22 = (ImageButton) findViewById(R.id.butboard_8_mat_22);
				matrixButton22.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(2, 2);
					}
				});

				matrixButton23 = (ImageButton) findViewById(R.id.butboard_8_mat_23);
				matrixButton23.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(2, 3);
					}
				});

				matrixButton24 = (ImageButton) findViewById(R.id.butboard_8_mat_24);
				matrixButton24.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(2, 4);
					}
				});

				matrixButton25 = (ImageButton) findViewById(R.id.butboard_8_mat_25);
				matrixButton25.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(2, 5);
					}
				});

				matrixButton26 = (ImageButton) findViewById(R.id.butboard_8_mat_26);
				matrixButton26.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(2, 6);
					}
				});

				matrixButton27 = (ImageButton) findViewById(R.id.butboard_8_mat_27);
				matrixButton27.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(2, 7);
					}
				});

				// Row 3:======
				matrixButton30 = (ImageButton) findViewById(R.id.butboard_8_mat_30);
				matrixButton30.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(3, 0); // If vs Computer the computer plays
										// after
										// user
										// within userMove.
					}
				});

				matrixButton31 = (ImageButton) findViewById(R.id.butboard_8_mat_31);
				matrixButton31.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(3, 1);
					}
				});

				matrixButton32 = (ImageButton) findViewById(R.id.butboard_8_mat_32);
				matrixButton32.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(3, 2);
					}
				});

				matrixButton33 = (ImageButton) findViewById(R.id.butboard_8_mat_33);
				matrixButton33.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(3, 3);
					}
				});

				matrixButton34 = (ImageButton) findViewById(R.id.butboard_8_mat_34);
				matrixButton34.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(3, 4);
					}
				});

				matrixButton35 = (ImageButton) findViewById(R.id.butboard_8_mat_35);
				matrixButton35.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(3, 5);
					}
				});

				matrixButton36 = (ImageButton) findViewById(R.id.butboard_8_mat_36);
				matrixButton36.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(3, 6);
					}
				});

				matrixButton37 = (ImageButton) findViewById(R.id.butboard_8_mat_37);
				matrixButton37.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(3, 7);
					}
				});

				// Row 4:======
				matrixButton40 = (ImageButton) findViewById(R.id.butboard_8_mat_40);
				matrixButton40.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(4, 0); // If vs Computer the computer plays
										// after
										// user
										// within userMove.
					}
				});

				matrixButton41 = (ImageButton) findViewById(R.id.butboard_8_mat_41);
				matrixButton41.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(4, 1);
					}
				});

				matrixButton42 = (ImageButton) findViewById(R.id.butboard_8_mat_42);
				matrixButton42.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(4, 2);
					}
				});

				matrixButton43 = (ImageButton) findViewById(R.id.butboard_8_mat_43);
				matrixButton43.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(4, 3);
					}
				});

				matrixButton44 = (ImageButton) findViewById(R.id.butboard_8_mat_44);
				matrixButton44.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(4, 4);
					}
				});

				matrixButton45 = (ImageButton) findViewById(R.id.butboard_8_mat_45);
				matrixButton45.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(4, 5);
					}
				});

				matrixButton46 = (ImageButton) findViewById(R.id.butboard_8_mat_46);
				matrixButton46.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(4, 6);
					}
				});

				matrixButton47 = (ImageButton) findViewById(R.id.butboard_8_mat_47);
				matrixButton47.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(4, 7);
					}
				});

				// Row 5:======
				matrixButton50 = (ImageButton) findViewById(R.id.butboard_8_mat_50);
				matrixButton50.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(5, 0); // If vs Computer the computer plays
										// after
										// user
										// within userMove.
					}
				});

				matrixButton51 = (ImageButton) findViewById(R.id.butboard_8_mat_51);
				matrixButton51.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(5, 1);
					}
				});

				matrixButton52 = (ImageButton) findViewById(R.id.butboard_8_mat_52);
				matrixButton52.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(5, 2);
					}
				});

				matrixButton53 = (ImageButton) findViewById(R.id.butboard_8_mat_53);
				matrixButton53.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(5, 3);
					}
				});

				matrixButton54 = (ImageButton) findViewById(R.id.butboard_8_mat_54);
				matrixButton54.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(5, 4);
					}
				});

				matrixButton55 = (ImageButton) findViewById(R.id.butboard_8_mat_55);
				matrixButton55.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(5, 5);
					}
				});

				matrixButton56 = (ImageButton) findViewById(R.id.butboard_8_mat_56);
				matrixButton56.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(5, 6);
					}
				});

				matrixButton57 = (ImageButton) findViewById(R.id.butboard_8_mat_57);
				matrixButton57.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(5, 7);
					}
				});

				// Row 6:======
				matrixButton60 = (ImageButton) findViewById(R.id.butboard_8_mat_60);
				matrixButton60.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(6, 0); // If vs Computer the computer plays
										// after
										// user
										// within userMove.
					}
				});

				matrixButton61 = (ImageButton) findViewById(R.id.butboard_8_mat_61);
				matrixButton61.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(6, 1);
					}
				});

				matrixButton62 = (ImageButton) findViewById(R.id.butboard_8_mat_62);
				matrixButton62.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(6, 2);
					}
				});

				matrixButton63 = (ImageButton) findViewById(R.id.butboard_8_mat_63);
				matrixButton63.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(6, 3);
					}
				});

				matrixButton64 = (ImageButton) findViewById(R.id.butboard_8_mat_64);
				matrixButton64.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(6, 4);
					}
				});

				matrixButton65 = (ImageButton) findViewById(R.id.butboard_8_mat_65);
				matrixButton65.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(6, 5);
					}
				});

				matrixButton66 = (ImageButton) findViewById(R.id.butboard_8_mat_66);
				matrixButton66.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(6, 6);
					}
				});

				matrixButton67 = (ImageButton) findViewById(R.id.butboard_8_mat_67);
				matrixButton67.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(6, 7);
					}
				});

				// Row 7:======
				matrixButton70 = (ImageButton) findViewById(R.id.butboard_8_mat_70);
				matrixButton70.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(7, 0); // If vs Computer the computer plays
										// after
										// user
										// within userMove.
					}
				});

				matrixButton71 = (ImageButton) findViewById(R.id.butboard_8_mat_71);
				matrixButton71.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(7, 1);
					}
				});

				matrixButton72 = (ImageButton) findViewById(R.id.butboard_8_mat_72);
				matrixButton72.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(7, 2);
					}
				});

				matrixButton73 = (ImageButton) findViewById(R.id.butboard_8_mat_73);
				matrixButton73.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(7, 3);
					}
				});

				matrixButton74 = (ImageButton) findViewById(R.id.butboard_8_mat_74);
				matrixButton74.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(7, 4);
					}
				});

				matrixButton75 = (ImageButton) findViewById(R.id.butboard_8_mat_75);
				matrixButton75.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(7, 5);
					}
				});

				matrixButton76 = (ImageButton) findViewById(R.id.butboard_8_mat_76);
				matrixButton76.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(7, 6);
					}
				});

				matrixButton77 = (ImageButton) findViewById(R.id.butboard_8_mat_77);
				matrixButton77.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(7, 7);
					}
				});
				break;
			}
			case 6: {
				// Row 0:=======
				matrixButton00 = (ImageButton) findViewById(R.id.butboard_6_mat_00);
				matrixButton00.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(0, 0); // If vs Computer the computer plays
										// after
										// user
										// within userMove.
					}
				});

				matrixButton01 = (ImageButton) findViewById(R.id.butboard_6_mat_01);
				matrixButton01.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(0, 1);
					}
				});

				matrixButton02 = (ImageButton) findViewById(R.id.butboard_6_mat_02);
				matrixButton02.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(0, 2);
					}
				});

				matrixButton03 = (ImageButton) findViewById(R.id.butboard_6_mat_03);
				matrixButton03.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(0, 3);
					}
				});

				matrixButton04 = (ImageButton) findViewById(R.id.butboard_6_mat_04);
				matrixButton04.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(0, 4);
					}
				});

				matrixButton05 = (ImageButton) findViewById(R.id.butboard_6_mat_05);
				matrixButton05.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(0, 5);
					}
				});

				// Row 1:======
				matrixButton10 = (ImageButton) findViewById(R.id.butboard_6_mat_10);
				matrixButton10.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(1, 0); // If vs Computer the computer plays
										// after
										// user
										// within userMove.
					}
				});

				matrixButton11 = (ImageButton) findViewById(R.id.butboard_6_mat_11);
				matrixButton11.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(1, 1);
					}
				});

				matrixButton12 = (ImageButton) findViewById(R.id.butboard_6_mat_12);
				matrixButton12.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(1, 2);
					}
				});

				matrixButton13 = (ImageButton) findViewById(R.id.butboard_6_mat_13);
				matrixButton13.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(1, 3);
					}
				});

				matrixButton14 = (ImageButton) findViewById(R.id.butboard_6_mat_14);
				matrixButton14.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(1, 4);
					}
				});

				matrixButton15 = (ImageButton) findViewById(R.id.butboard_6_mat_15);
				matrixButton15.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(1, 5);
					}
				});

				// Row 2:======
				matrixButton20 = (ImageButton) findViewById(R.id.butboard_6_mat_20);
				matrixButton20.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(2, 0); // If vs Computer the computer plays
										// after
										// user
										// within userMove.
					}
				});

				matrixButton21 = (ImageButton) findViewById(R.id.butboard_6_mat_21);
				matrixButton21.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(2, 1);
					}
				});

				matrixButton22 = (ImageButton) findViewById(R.id.butboard_6_mat_22);
				matrixButton22.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(2, 2);
					}
				});

				matrixButton23 = (ImageButton) findViewById(R.id.butboard_6_mat_23);
				matrixButton23.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(2, 3);
					}
				});

				matrixButton24 = (ImageButton) findViewById(R.id.butboard_6_mat_24);
				matrixButton24.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(2, 4);
					}
				});

				matrixButton25 = (ImageButton) findViewById(R.id.butboard_6_mat_25);
				matrixButton25.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(2, 5);
					}
				});

				// Row 3:======
				matrixButton30 = (ImageButton) findViewById(R.id.butboard_6_mat_30);
				matrixButton30.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(3, 0); // If vs Computer the computer plays
										// after
										// user
										// within userMove.
					}
				});

				matrixButton31 = (ImageButton) findViewById(R.id.butboard_6_mat_31);
				matrixButton31.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(3, 1);
					}
				});

				matrixButton32 = (ImageButton) findViewById(R.id.butboard_6_mat_32);
				matrixButton32.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(3, 2);
					}
				});

				matrixButton33 = (ImageButton) findViewById(R.id.butboard_6_mat_33);
				matrixButton33.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(3, 3);
					}
				});

				matrixButton34 = (ImageButton) findViewById(R.id.butboard_6_mat_34);
				matrixButton34.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(3, 4);
					}
				});

				matrixButton35 = (ImageButton) findViewById(R.id.butboard_6_mat_35);
				matrixButton35.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(3, 5);
					}
				});

				// Row 4:======
				matrixButton40 = (ImageButton) findViewById(R.id.butboard_6_mat_40);
				matrixButton40.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(4, 0); // If vs Computer the computer plays
										// after
										// user
										// within userMove.
					}
				});

				matrixButton41 = (ImageButton) findViewById(R.id.butboard_6_mat_41);
				matrixButton41.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(4, 1);
					}
				});

				matrixButton42 = (ImageButton) findViewById(R.id.butboard_6_mat_42);
				matrixButton42.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(4, 2);
					}
				});

				matrixButton43 = (ImageButton) findViewById(R.id.butboard_6_mat_43);
				matrixButton43.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(4, 3);
					}
				});

				matrixButton44 = (ImageButton) findViewById(R.id.butboard_6_mat_44);
				matrixButton44.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(4, 4);
					}
				});

				matrixButton45 = (ImageButton) findViewById(R.id.butboard_6_mat_45);
				matrixButton45.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(4, 5);
					}
				});

				// Row 5:======
				matrixButton50 = (ImageButton) findViewById(R.id.butboard_6_mat_50);
				matrixButton50.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(5, 0); // If vs Computer the computer plays
										// after
										// user
										// within userMove.
					}
				});

				matrixButton51 = (ImageButton) findViewById(R.id.butboard_6_mat_51);
				matrixButton51.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(5, 1);
					}
				});

				matrixButton52 = (ImageButton) findViewById(R.id.butboard_6_mat_52);
				matrixButton52.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(5, 2);
					}
				});

				matrixButton53 = (ImageButton) findViewById(R.id.butboard_6_mat_53);
				matrixButton53.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(5, 3);
					}
				});

				matrixButton54 = (ImageButton) findViewById(R.id.butboard_6_mat_54);
				matrixButton54.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(5, 4);
					}
				});

				matrixButton55 = (ImageButton) findViewById(R.id.butboard_6_mat_55);
				matrixButton55.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(5, 5);
					}
				});
				break;
			}
			case 4: {

				// Row 0:=======
				matrixButton00 = (ImageButton) findViewById(R.id.butboard_4_mat_00);
				matrixButton00.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(0, 0); // If vs Computer the computer plays
										// after
										// user
										// within userMove.
					}
				});

				matrixButton01 = (ImageButton) findViewById(R.id.butboard_4_mat_01);
				matrixButton01.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(0, 1);
					}
				});

				matrixButton02 = (ImageButton) findViewById(R.id.butboard_4_mat_02);
				matrixButton02.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(0, 2);
					}
				});

				matrixButton03 = (ImageButton) findViewById(R.id.butboard_4_mat_03);
				matrixButton03.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(0, 3);
					}
				});

				// Row 1:======
				matrixButton10 = (ImageButton) findViewById(R.id.butboard_4_mat_10);
				matrixButton10.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(1, 0); // If vs Computer the computer plays
										// after
										// user
										// within userMove.
					}
				});

				matrixButton11 = (ImageButton) findViewById(R.id.butboard_4_mat_11);
				matrixButton11.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(1, 1);
					}
				});

				matrixButton12 = (ImageButton) findViewById(R.id.butboard_4_mat_12);
				matrixButton12.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(1, 2);
					}
				});

				matrixButton13 = (ImageButton) findViewById(R.id.butboard_4_mat_13);
				matrixButton13.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(1, 3);
					}
				});

				// Row 2:======
				matrixButton20 = (ImageButton) findViewById(R.id.butboard_4_mat_20);
				matrixButton20.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(2, 0); // If vs Computer the computer plays
										// after
										// user
										// within userMove.
					}
				});

				matrixButton21 = (ImageButton) findViewById(R.id.butboard_4_mat_21);
				matrixButton21.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(2, 1);
					}
				});

				matrixButton22 = (ImageButton) findViewById(R.id.butboard_4_mat_22);
				matrixButton22.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(2, 2);
					}
				});

				matrixButton23 = (ImageButton) findViewById(R.id.butboard_4_mat_23);
				matrixButton23.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(2, 3);
					}
				});

				// Row 3:======
				matrixButton30 = (ImageButton) findViewById(R.id.butboard_4_mat_30);
				matrixButton30.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(3, 0); // If vs Computer the computer plays
										// after
										// user
										// within userMove.
					}
				});

				matrixButton31 = (ImageButton) findViewById(R.id.butboard_4_mat_31);
				matrixButton31.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(3, 1);
					}
				});

				matrixButton32 = (ImageButton) findViewById(R.id.butboard_4_mat_32);
				matrixButton32.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(3, 2);
					}
				});

				matrixButton33 = (ImageButton) findViewById(R.id.butboard_4_mat_33);
				matrixButton33.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						userMove(3, 3);
					}
				});
				break;
			}
			}
			t.schedule(scanTask, 2000, 1200);
			// scanTask = new TimerTask() {
			// public void run() {
			//
			// //History feature:-----
			// if (SamoHistoryActivity.isHistoryOn){
			// // Runnign on the list and draw the board:
			// SamoReversiGame.getInstance().setBoard(previousBoard.get(historyImgCounter++));
			// drawBLCurrentMatrix();
			// }
			// //----------------------
			//
			// handler.post(new Runnable() {
			// public void run() {
			// if (gamefinished) {
			// if (countshow == 1) {
			// showWinner();
			// } else {
			// countshow++;
			// }
			// } else if (playermove) {
			// computerMove();
			// playermove = false;
			// drawBLCurrentMatrix();
			// }
			//
			// }
			// });
			// }
			// };
			//
			// t.schedule(scanTask, 2000, 1200);
		}
	}

	@Override
	public void onBackPressed() {
		if (SamoReversiGame.getInstance().isVsComputer()) {
			SamoReversiGame.getInstance().getWhitePlayer().setName("Player 2");

		}
		finish();
	}

	/**
	 * Menu
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater mioption = getMenuInflater();
		if (SamoHistoryActivity.isHistoryOn) {
			mioption.inflate(R.menu.gamemenuhistory, menu);
		} else if(SamoReversiGame.getInstance().isVsComputer()){
			mioption.inflate(R.menu.gamemenu2players, menu);
		}else{
			mioption.inflate(R.menu.gamemenu1player, menu);
		}
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.p1restartgame:
		case R.id.p2restartgame:
			startActivity(new Intent("samo.reversi.PLAY"));
			finish();
			return true;
		case R.id.p2vsplayer:
			SamoReversiGame.getInstance().getWhitePlayer().setName("Player 2");
			SamoReversiGame.getInstance().setVsComputer(false);
			startActivity(new Intent("samo.reversi.PLAY"));
			finish();
			return true;
		case R.id.p1vscomputer:
			SamoReversiGame.getInstance().setVsComputer(true);
			startActivity(new Intent("samo.reversi.PLAY"));
			finish();
			return true;
		case R.id.p1gamerules:
		case R.id.p2gamerules:
		case R.id.historygamerules:	
			startActivity(new Intent(SamoGameBoard.this, SamoGameRules.class));
			return true;
		case R.id.p1gameinfo:
		case R.id.p2gameinfo:
		case R.id.historygameinfo:
			LayoutInflater inflater = this.getLayoutInflater();
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					SamoGameBoard.this);

			alertDialogBuilder.setView(
					inflater.inflate(R.layout.about_dialog, null))
					.setOnCancelListener(new OnCancelListener() {

						@Override
						public void onCancel(DialogInterface dialog) {
							dialog.dismiss();

						}
					});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
			return true;
		}
		return false;
	}

	/**
	 * User clicked and wants to move.
	 * 
	 * @param e
	 */
	public void userMove(int i, int j) {
		score_black = (TextView) findViewById(R.id.score_black);
		score_white = (TextView) findViewById(R.id.score_white);
		playerpreviousturn.add(playerturn);
		previousBoard.add(new Board(SamoReversiGame.getInstance().getBoard()));
		if (SamoReversiGame.getInstance().isVsComputer()) {
			if ((i < SamoReversiGame.getInstance().getBoard().getSize())
					&& (j < SamoReversiGame.getInstance().getBoard().getSize())
					&& (SamoReversiGame.getInstance().getBoard().get(i, j) == TKind.nil)
					&& (SamoReversiGame.getInstance().getBoard()
							.move(new Move(i, j), TKind.black) != 0)) {
				playerVsComputerTurn.add(playermove);
				((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(100);
				// Finding objects:

				// Set update values:
				score_black.setText(Integer.toString(SamoReversiGame
						.getInstance().getBoard().getCounter(TKind.black)));
				score_white.setText(Integer.toString(SamoReversiGame
						.getInstance().getBoard().getCounter(TKind.white)));

				// computer operation:
				playerturn = 1;
				drawBLCurrentMatrix();

				playermove = true;
			} else {
			}

		} else {
			if (playerturn == 1) {
				if ((i < SamoReversiGame.getInstance().getBoard().getSize())
						&& (j < SamoReversiGame.getInstance().getBoard()
								.getSize())
						&& (SamoReversiGame.getInstance().getBoard().get(i, j) == TKind.nil)
						&& (SamoReversiGame.getInstance().getBoard()
								.move(new Move(i, j), TKind.black) != 0)) {

					((Vibrator) getSystemService(VIBRATOR_SERVICE))
							.vibrate(100);

					score_black.setText(Integer.toString(SamoReversiGame
							.getInstance().getBoard().getCounter(TKind.black)));
					score_white.setText(Integer.toString(SamoReversiGame
							.getInstance().getBoard().getCounter(TKind.white)));
					playerturn = 2;
					if (!SamoReversiGame.getInstance().getBoard()
							.userCanMove(TKind.white)
							&& !SamoReversiGame.getInstance().getBoard()
									.userCanMove(TKind.black)) {
						showWinner();
					} else if (!SamoReversiGame.getInstance().getBoard()
							.userCanMove(TKind.white)) {
						playerturn = 1;
					}
				} else if (SamoReversiGame.getInstance().getBoard().gameEnd()) {
					showWinner();
				}

			} else if (playerturn == 2) {
				if ((i < SamoReversiGame.getInstance().getBoard().getSize())
						&& (j < SamoReversiGame.getInstance().getBoard()
								.getSize())
						&& (SamoReversiGame.getInstance().getBoard().get(i, j) == TKind.nil)
						&& (SamoReversiGame.getInstance().getBoard()
								.move(new Move(i, j), TKind.white) != 0)) {

					((Vibrator) getSystemService(VIBRATOR_SERVICE))
							.vibrate(100);

					score_black.setText(Integer.toString(SamoReversiGame
							.getInstance().getBoard().getCounter(TKind.black)));
					score_white.setText(Integer.toString(SamoReversiGame
							.getInstance().getBoard().getCounter(TKind.white)));
					playerturn = 1;
					if (!SamoReversiGame.getInstance().getBoard()
							.userCanMove(TKind.white)
							&& !SamoReversiGame.getInstance().getBoard()
									.userCanMove(TKind.black)) {
						showWinner();
					} else if (!SamoReversiGame.getInstance().getBoard()
							.userCanMove(TKind.black)) {
						playerturn = 2;

					}
				} else if (SamoReversiGame.getInstance().getBoard().gameEnd()) {
					showWinner();
				}
			}
		}

		drawBLCurrentMatrix();
		// making random sounds
		/*
		 * Random randomGenerator = new Random(); int randomInt =
		 * randomGenerator.nextInt(2); switch(randomInt){ case (0):{ MPsound =
		 * MediaPlayer.create(this, R.raw.punch); MPsound.start(); try {
		 * Thread.sleep(500); } catch (InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } MPsound.stop();
		 * break; } case (1):{ MPsound = MediaPlayer.create(this, R.raw.punch2);
		 * MPsound.start(); try { Thread.sleep(500); } catch
		 * (InterruptedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } MPsound.stop(); break; } }
		 */

	}

	public void computerMove() {

		if (SamoReversiGame.getInstance().getBoard().gameEnd()) {
			showWinner();
			return;
		}
		playerVsComputerTurn.add(playermove);
		previousBoard.add(new Board(SamoReversiGame.getInstance().getBoard()));
		// drawBLCurrentMatrix();
		Move move = new Move();
		if (SamoReversiGame.getInstance().getBoard()
				.findMove(TKind.white, (level + 1), move)) {
			SamoReversiGame.getInstance().getBoard().move(move, TKind.white);

			score_black = (TextView) findViewById(R.id.score_black);
			score_white = (TextView) findViewById(R.id.score_white);

			score_black.setText(Integer.toString(SamoReversiGame.getInstance()
					.getBoard().getCounter(TKind.black)));
			score_white.setText(Integer.toString(SamoReversiGame.getInstance()
					.getBoard().getCounter(TKind.white)));

			if (SamoReversiGame.getInstance().getBoard().gameEnd())
				showWinner();
			else if (!SamoReversiGame.getInstance().getBoard()
					.userCanMove(TKind.black)) {// Wait to user next click.
				new Runnable() {
					public void run() {
						computerMove();
					}
				}.run();
			}
		} else if (SamoReversiGame.getInstance().getBoard()
				.userCanMove(TKind.black)) { // User can move, another turn to
												// computer.
			return;// wait for next user click.
		} else {
			if (isComputerFinish) {
				showWinner();
				isComputerFinish = false;
			}
		}

	}

	public void showWinner() {
		// inputEnabled = false;
		// active = false;
		if (!gamefinished) {
			gamefinished = true;
			// Save this game 2 History?
			if (SamoHistoryActivity.is2Save2History) {
				try {
					previousBoard.add(new Board(SamoReversiGame.getInstance().getBoard()));
					saveHistory();
				} catch (IOException e) {
					// TODO Toast failed saving
					Toast.makeText(SamoGameBoard.this,
							"Failed saving to history.", Toast.LENGTH_SHORT);
					e.printStackTrace();
				}
			}
		} else {
			if (SamoReversiGame.getInstance().getBoard()
					.getCounter(TKind.black) > SamoReversiGame.getInstance()
					.getBoard().getCounter(TKind.white)) {
				SamoReversiGame.getInstance().setWinner(1);
				startActivity(new Intent("samo.reversi.WINNER"));
				t.cancel();
				finish();
			} else if (SamoReversiGame.getInstance().getBoard()
					.getCounter(TKind.black) < SamoReversiGame.getInstance()
					.getBoard().getCounter(TKind.white)) {
				SamoReversiGame.getInstance().setWinner(2);
				startActivity(new Intent("samo.reversi.WINNER"));
				t.cancel();
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				finish();
			} else {
				SamoReversiGame.getInstance().setWinner(0);
				startActivity(new Intent("samo.reversi.WINNER"));
				t.cancel();
				finish();
			}

		}
	}

//	public void drawHistoryMatrix(){
//		ImageButton tmpButton = null;
//		for (int i = 0; i < SamoReversiGame.getInstance().getBoard().getSize(); i++)
//			for (int j = 0; j < SamoReversiGame.getInstance().getBoard().getSize(); j++) {
//				tmpButton = getCurrentButton(i, j);
//				if (SamoReversiGame.getInstance().getBoard().get(i, j).equals(TKind.black)){
//					tmpButton.setImageResource(R.drawable.blackbiggerbutten71);
//				}else if (SamoReversiGame.getInstance().getBoard().get(i, j).equals(TKind.white)){
//					tmpButton.setImageResource(R.drawable.redbiggerbutten71); 
//				} else{
//					tmpButton.setImageResource(R.drawable.butten71); 				
//			}
//				tmpButton.invalidate();
//			}
//	}
	
	public void drawBLCurrentMatrix() {
		ImageButton tmpButton = null;

		for (int i = 0; i < SamoReversiGame.getInstance().getBoard().getSize(); i++)
			for (int j = 0; j < SamoReversiGame.getInstance().getBoard().getSize(); j++) {
				if (SamoReversiGame.getInstance().getBoard().get(i, j).equals(TKind.black)) {
					tmpButton = getCurrentButton(i, j);
					if (j == 0) {
						if (i == 0) {
							tmpButton.setImageResource(R.drawable.blackbiggerbutten00);
						} else if (i == SamoReversiGame.getInstance()
								.getBoard().getSize() - 1) {
							tmpButton.setImageResource(R.drawable.blackbiggerbutten70);
						} else {
							tmpButton
									.setImageResource(R.drawable.blackbiggerbutten10);
						}
					} else if (j == SamoReversiGame.getInstance().getBoard()
							.getSize() - 1) {
						if (i == 0) {
							tmpButton
									.setImageResource(R.drawable.blackbiggerbutten07);
						} else if (i == SamoReversiGame.getInstance()
								.getBoard().getSize() - 1) {
							tmpButton
									.setImageResource(R.drawable.blackbiggerbutten77);
						} else {
							tmpButton
									.setImageResource(R.drawable.blackbiggerbutten17);
						}

					} else {
						if (i == 0) {
							tmpButton
									.setImageResource(R.drawable.blackbiggerbutten01);
						} else if (i == SamoReversiGame.getInstance()
								.getBoard().getSize() - 1) {
							tmpButton
									.setImageResource(R.drawable.blackbiggerbutten71);
						} else {
							tmpButton
									.setImageResource(R.drawable.blackbiggerbutten11);
							tmpButton.postInvalidate();
						}
					}
					// tmpButton.setImageResource(R.drawable.black_button);//black
				} else if (SamoReversiGame.getInstance().getBoard().get(i, j)
						.equals(TKind.white)) {
					tmpButton = getCurrentButton(i, j);
					if (j == 0) {
						if (i == 0) {
							tmpButton
									.setImageResource(R.drawable.redbiggerbutten00);
						} else if (i == SamoReversiGame.getInstance()
								.getBoard().getSize() - 1) {
							tmpButton
									.setImageResource(R.drawable.redbiggerbutten70);
						} else {
							tmpButton
									.setImageResource(R.drawable.redbiggerbutten10);
						}
					} else if (j == SamoReversiGame.getInstance().getBoard()
							.getSize() - 1) {
						if (i == 0) {
							tmpButton
									.setImageResource(R.drawable.redbiggerbutten07);
						} else if (i == SamoReversiGame.getInstance()
								.getBoard().getSize() - 1) {
							tmpButton
									.setImageResource(R.drawable.redbiggerbutten77);
						} else {
							tmpButton
									.setImageResource(R.drawable.redbiggerbutten17);
						}

					} else {
						if (i == 0) {
							tmpButton
									.setImageResource(R.drawable.redbiggerbutten01);
						} else if (i == SamoReversiGame.getInstance()
								.getBoard().getSize() - 1) {
							tmpButton
									.setImageResource(R.drawable.redbiggerbutten71);
						} else {
							tmpButton
									.setImageResource(R.drawable.redbiggerbutten11);
						}
					}
					// tmpButton.setImageResource(R.drawable.butten11);//white
				}/*
				 * else if (SamoReversiGame.getInstance().getBoard().get(i,
				 * j).equals(TKind.nil)) { tmpButton = getCurrentButton(i,j);
				 * tmpButton.setImageResource(R.drawable.butten00);//nill }
				 */else if (playerturn == 1) {
					tmpButton = getCurrentButton(i, j);
					if (SamoReversiGame.getInstance().getBoard().isValid(new Move(i, j), TKind.black)
							&& (!playermove)&&(!SamoHistoryActivity.isHistoryOn)) {
						if (j == 0) {
							if (i == 0) {
								tmpButton.setImageResource(R.drawable.blackoption00);
							} else if (i == SamoReversiGame.getInstance()
									.getBoard().getSize() - 1) {
								tmpButton
										.setImageResource(R.drawable.blackoption70);
							} else {
								tmpButton
										.setImageResource(R.drawable.blackoption10);
							}
						} else if (j == SamoReversiGame.getInstance()
								.getBoard().getSize() - 1) {
							if (i == 0) {
								tmpButton
										.setImageResource(R.drawable.blackoption07);
							} else if (i == SamoReversiGame.getInstance()
									.getBoard().getSize() - 1) {
								tmpButton
										.setImageResource(R.drawable.blackoption77);
							} else {
								tmpButton
										.setImageResource(R.drawable.blackoption17);
							}

						} else {
							if (i == 0) {
								tmpButton
										.setImageResource(R.drawable.blackoption01);
							} else if (i == SamoReversiGame.getInstance()
									.getBoard().getSize() - 1) {
								tmpButton
										.setImageResource(R.drawable.blackoption71);
							} else {
								tmpButton
										.setImageResource(R.drawable.blackoption11);
							}
						}
					} else if (SamoReversiGame.getInstance().getBoard()
							.get(i, j).equals(TKind.nil)) {
						tmpButton = getCurrentButton(i, j);
						if (j == 0) {
							if (i == 0) {
								tmpButton.setImageResource(R.drawable.biggerbutten00);
							} else if (i == SamoReversiGame.getInstance().getBoard().getSize() - 1) {
								tmpButton.setImageResource(R.drawable.biggerbutten70);
							} else {
								tmpButton.setImageResource(R.drawable.biggerbutten10);
							}
						} else if (j == SamoReversiGame.getInstance()
								.getBoard().getSize() - 1) {
							if (i == 0) {
								tmpButton.setImageResource(R.drawable.biggerbutten07);
							} else if (i == SamoReversiGame.getInstance().getBoard().getSize() - 1) {
								tmpButton.setImageResource(R.drawable.biggerbutten77);
							} else {
								tmpButton
										.setImageResource(R.drawable.biggerbutten17);
							}

						} else {
							if (i == 0) {
								tmpButton
										.setImageResource(R.drawable.biggerbutten01);
							} else if (i == SamoReversiGame.getInstance()
									.getBoard().getSize() - 1) {
								tmpButton
										.setImageResource(R.drawable.biggerbutten71);
							} else {
								tmpButton
										.setImageResource(R.drawable.biggerbutten11);
							}
						}// nill
					}

				} else if (playerturn == 2) {
					tmpButton = getCurrentButton(i, j);
					if (SamoReversiGame.getInstance().getBoard()
							.isValid(new Move(i, j), TKind.white)&&(!SamoHistoryActivity.isHistoryOn)) {
						if (j == 0) {
							if (i == 0) {
								tmpButton.setImageResource(R.drawable.redoption00);
							} else if (i == SamoReversiGame.getInstance()
									.getBoard().getSize() - 1) {
								tmpButton
										.setImageResource(R.drawable.redoption70);
							} else {
								tmpButton
										.setImageResource(R.drawable.redoption10);
							}
						} else if (j == SamoReversiGame.getInstance()
								.getBoard().getSize() - 1) {
							if (i == 0) {
								tmpButton
										.setImageResource(R.drawable.redoption07);
							} else if (i == SamoReversiGame.getInstance()
									.getBoard().getSize() - 1) {
								tmpButton
										.setImageResource(R.drawable.redoption77);
							} else {
								tmpButton
										.setImageResource(R.drawable.redoption17);
							}

						} else {
							if (i == 0) {
								tmpButton
										.setImageResource(R.drawable.redoption01);
							} else if (i == SamoReversiGame.getInstance()
									.getBoard().getSize() - 1) {
								tmpButton
										.setImageResource(R.drawable.redoption71);
							} else {
								tmpButton
										.setImageResource(R.drawable.redoption11);
							}
						}
					} else if (SamoReversiGame.getInstance().getBoard()
							.get(i, j).equals(TKind.nil)) {
						tmpButton = getCurrentButton(i, j);
						if (j == 0) {
							if (i == 0) {
								tmpButton.setImageResource(R.drawable.biggerbutten00);
							} else if (i == SamoReversiGame.getInstance()
									.getBoard().getSize() - 1) {
								tmpButton
										.setImageResource(R.drawable.biggerbutten70);
							} else {
								tmpButton
										.setImageResource(R.drawable.biggerbutten10);
							}
						} else if (j == SamoReversiGame.getInstance()
								.getBoard().getSize() - 1) {
							if (i == 0) {
								tmpButton
										.setImageResource(R.drawable.biggerbutten07);
							} else if (i == SamoReversiGame.getInstance()
									.getBoard().getSize() - 1) {
								tmpButton
										.setImageResource(R.drawable.biggerbutten77);
							} else {
								tmpButton
										.setImageResource(R.drawable.biggerbutten17);
							}

						} else {
							if (i == 0) {
								tmpButton
										.setImageResource(R.drawable.biggerbutten01);
							} else if (i == SamoReversiGame.getInstance()
									.getBoard().getSize() - 1) {
								tmpButton
										.setImageResource(R.drawable.biggerbutten71);
							} else {
								tmpButton
										.setImageResource(R.drawable.biggerbutten11);
							}
						}// nill
					}

				}
				if (SamoReversiGame.getInstance().isVsComputer()) {
					tmpButton.invalidate();
				}
			}
	}

	/**
	 * Scanns the buttons to find the correct one.
	 */
	public ImageButton getCurrentButton(int i, int j) {
		// Validity checks:
		if ((i > SamoReversiGame.getInstance().getBoard().getSize() - 1)
				|| (j > SamoReversiGame.getInstance().getBoard().getSize() - 1)
				|| (i < 0) || (j < 0)) {
			return null;
		}

		switch (SamoReversiGame.getInstance().getBoard().getSize()) {
		case 8: {
			switch (i) {
			case 0: {// Row 0:
				switch (j) {
				case (0): { // Column 0
					return (ImageButton) findViewById(R.id.butboard_8_mat_00);
				}
				case (1): {// Column 1...
					return (ImageButton) findViewById(R.id.butboard_8_mat_01);
				}
				case (2): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_02);
				}
				case (3): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_03);
				}
				case (4): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_04);
				}
				case (5): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_05);
				}
				case (6): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_06);
				}
				case (7): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_07);
				}
				}
				break;
			}
			case 1: {
				switch (j) {
				case (0): { // Column 0
					return (ImageButton) findViewById(R.id.butboard_8_mat_10);
				}
				case (1): {// Column 1...
					return (ImageButton) findViewById(R.id.butboard_8_mat_11);
				}
				case (2): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_12);
				}
				case (3): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_13);
				}
				case (4): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_14);
				}
				case (5): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_15);
				}
				case (6): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_16);
				}
				case (7): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_17);
				}
				}
				break;
			}
			case 2: {
				switch (j) {
				case (0): { // Column 0
					return (ImageButton) findViewById(R.id.butboard_8_mat_20);
				}
				case (1): {// Column 1...
					return (ImageButton) findViewById(R.id.butboard_8_mat_21);
				}
				case (2): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_22);
				}
				case (3): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_23);
				}
				case (4): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_24);
				}
				case (5): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_25);
				}
				case (6): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_26);
				}
				case (7): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_27);
				}
				}
				break;
			}

			case 3: {
				switch (j) {
				case (0): { // Column 0
					return (ImageButton) findViewById(R.id.butboard_8_mat_30);
				}
				case (1): {// Column 1...
					return (ImageButton) findViewById(R.id.butboard_8_mat_31);
				}
				case (2): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_32);
				}
				case (3): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_33);
				}
				case (4): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_34);
				}
				case (5): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_35);
				}
				case (6): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_36);
				}
				case (7): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_37);
				}
				}
				break;
			}

			case 4: {
				switch (j) {
				case (0): { // Column 0
					return (ImageButton) findViewById(R.id.butboard_8_mat_40);
				}
				case (1): {// Column 1...
					return (ImageButton) findViewById(R.id.butboard_8_mat_41);
				}
				case (2): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_42);
				}
				case (3): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_43);
				}
				case (4): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_44);
				}
				case (5): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_45);
				}
				case (6): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_46);
				}
				case (7): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_47);
				}
				}
				break;
			}

			case 5: {
				switch (j) {
				case (0): { // Column 0
					return (ImageButton) findViewById(R.id.butboard_8_mat_50);
				}
				case (1): {// Column 1...
					return (ImageButton) findViewById(R.id.butboard_8_mat_51);
				}
				case (2): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_52);
				}
				case (3): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_53);
				}
				case (4): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_54);
				}
				case (5): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_55);
				}
				case (6): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_56);
				}
				case (7): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_57);
				}
				}
				break;
			}

			case 6: {
				switch (j) {
				case (0): { // Column 0
					return (ImageButton) findViewById(R.id.butboard_8_mat_60);
				}
				case (1): {// Column 1...
					return (ImageButton) findViewById(R.id.butboard_8_mat_61);
				}
				case (2): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_62);
				}
				case (3): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_63);
				}
				case (4): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_64);
				}
				case (5): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_65);
				}
				case (6): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_66);
				}
				case (7): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_67);
				}
				}
				break;
			}
			case 7: {
				switch (j) {
				case (0): { // Column 0
					return (ImageButton) findViewById(R.id.butboard_8_mat_70);
				}
				case (1): {// Column 1...
					return (ImageButton) findViewById(R.id.butboard_8_mat_71);
				}
				case (2): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_72);
				}
				case (3): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_73);
				}
				case (4): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_74);
				}
				case (5): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_75);
				}
				case (6): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_76);
				}
				case (7): {
					return (ImageButton) findViewById(R.id.butboard_8_mat_77);
				}
				}
				break;
			}
			default:
				return null;
			}
			break;
		}

		case 6: {
			switch (i) {
			case 0: {// Row 0:
				switch (j) {
				case (0): { // Column 0
					return (ImageButton) findViewById(R.id.butboard_6_mat_00);
				}
				case (1): {// Column 1...
					return (ImageButton) findViewById(R.id.butboard_6_mat_01);
				}
				case (2): {
					return (ImageButton) findViewById(R.id.butboard_6_mat_02);
				}
				case (3): {
					return (ImageButton) findViewById(R.id.butboard_6_mat_03);
				}
				case (4): {
					return (ImageButton) findViewById(R.id.butboard_6_mat_04);
				}
				case (5): {
					return (ImageButton) findViewById(R.id.butboard_6_mat_05);
				}
				}
				break;
			}
			case 1: {
				switch (j) {
				case (0): { // Column 0
					return (ImageButton) findViewById(R.id.butboard_6_mat_10);
				}
				case (1): {// Column 1...
					return (ImageButton) findViewById(R.id.butboard_6_mat_11);
				}
				case (2): {
					return (ImageButton) findViewById(R.id.butboard_6_mat_12);
				}
				case (3): {
					return (ImageButton) findViewById(R.id.butboard_6_mat_13);
				}
				case (4): {
					return (ImageButton) findViewById(R.id.butboard_6_mat_14);
				}
				case (5): {
					return (ImageButton) findViewById(R.id.butboard_6_mat_15);
				}
				}
				break;
			}
			case 2: {
				switch (j) {
				case (0): { // Column 0
					return (ImageButton) findViewById(R.id.butboard_6_mat_20);
				}
				case (1): {// Column 1...
					return (ImageButton) findViewById(R.id.butboard_6_mat_21);
				}
				case (2): {
					return (ImageButton) findViewById(R.id.butboard_6_mat_22);
				}
				case (3): {
					return (ImageButton) findViewById(R.id.butboard_6_mat_23);
				}
				case (4): {
					return (ImageButton) findViewById(R.id.butboard_6_mat_24);
				}
				case (5): {
					return (ImageButton) findViewById(R.id.butboard_6_mat_25);
				}
				}
				break;
			}

			case 3: {
				switch (j) {
				case (0): { // Column 0
					return (ImageButton) findViewById(R.id.butboard_6_mat_30);
				}
				case (1): {// Column 1...
					return (ImageButton) findViewById(R.id.butboard_6_mat_31);
				}
				case (2): {
					return (ImageButton) findViewById(R.id.butboard_6_mat_32);
				}
				case (3): {
					return (ImageButton) findViewById(R.id.butboard_6_mat_33);
				}
				case (4): {
					return (ImageButton) findViewById(R.id.butboard_6_mat_34);
				}
				case (5): {
					return (ImageButton) findViewById(R.id.butboard_6_mat_35);
				}
				}
				break;
			}

			case 4: {
				switch (j) {
				case (0): { // Column 0
					return (ImageButton) findViewById(R.id.butboard_6_mat_40);
				}
				case (1): {// Column 1...
					return (ImageButton) findViewById(R.id.butboard_6_mat_41);
				}
				case (2): {
					return (ImageButton) findViewById(R.id.butboard_6_mat_42);
				}
				case (3): {
					return (ImageButton) findViewById(R.id.butboard_6_mat_43);
				}
				case (4): {
					return (ImageButton) findViewById(R.id.butboard_6_mat_44);
				}
				case (5): {
					return (ImageButton) findViewById(R.id.butboard_6_mat_45);
				}
				}
				break;
			}

			case 5: {
				switch (j) {
				case (0): { // Column 0
					return (ImageButton) findViewById(R.id.butboard_6_mat_50);
				}
				case (1): {// Column 1...
					return (ImageButton) findViewById(R.id.butboard_6_mat_51);
				}
				case (2): {
					return (ImageButton) findViewById(R.id.butboard_6_mat_52);
				}
				case (3): {
					return (ImageButton) findViewById(R.id.butboard_6_mat_53);
				}
				case (4): {
					return (ImageButton) findViewById(R.id.butboard_6_mat_54);
				}
				case (5): {
					return (ImageButton) findViewById(R.id.butboard_6_mat_55);
				}
				}
				break;
			}

			default:
				return null;
			}
			break;
		}

		case 4: {
			switch (i) {
			case 0: {// Row 0:
				switch (j) {
				case (0): { // Column 0
					return (ImageButton) findViewById(R.id.butboard_4_mat_00);
				}
				case (1): {// Column 1...
					return (ImageButton) findViewById(R.id.butboard_4_mat_01);
				}
				case (2): {
					return (ImageButton) findViewById(R.id.butboard_4_mat_02);
				}
				case (3): {
					return (ImageButton) findViewById(R.id.butboard_4_mat_03);
				}
				}
				break;
			}
			case 1: {
				switch (j) {
				case (0): { // Column 0
					return (ImageButton) findViewById(R.id.butboard_4_mat_10);
				}
				case (1): {// Column 1...
					return (ImageButton) findViewById(R.id.butboard_4_mat_11);
				}
				case (2): {
					return (ImageButton) findViewById(R.id.butboard_4_mat_12);
				}
				case (3): {
					return (ImageButton) findViewById(R.id.butboard_4_mat_13);
				}
				}
				break;
			}
			case 2: {
				switch (j) {
				case (0): { // Column 0
					return (ImageButton) findViewById(R.id.butboard_4_mat_20);
				}
				case (1): {// Column 1...
					return (ImageButton) findViewById(R.id.butboard_4_mat_21);
				}
				case (2): {
					return (ImageButton) findViewById(R.id.butboard_4_mat_22);
				}
				case (3): {
					return (ImageButton) findViewById(R.id.butboard_4_mat_23);
				}
				}
				break;
			}

			case 3: {
				switch (j) {
				case (0): { // Column 0
					return (ImageButton) findViewById(R.id.butboard_4_mat_30);
				}
				case (1): {// Column 1...
					return (ImageButton) findViewById(R.id.butboard_4_mat_31);
				}
				case (2): {
					return (ImageButton) findViewById(R.id.butboard_4_mat_32);
				}
				case (3): {
					return (ImageButton) findViewById(R.id.butboard_4_mat_33);
				}
				}
				break;
			}
			}
		}
		default:
			return null;
		}
		return null;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	//@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		historyImgCounter = 1;
		// setTimeStamp(in.readUTF());
		setTimeStamp((String) in.readObject());
		previousBoard = (List<Board>) in.readObject();
		SamoReversiGame.getInstance().setBlackPlayer(
				new Player((String) in.readObject()));
		SamoReversiGame.getInstance().setWhitePlayer(
				new Player((String) in.readObject()));

		// Board size:
		SamoReversiGame.getInstance().getBoard()
				.setSize((Integer) in.readObject());
		
//		SamoReversiGame.getInstance().getBoard().setCounter(player)(in.readObject());
//		SamoReversiGame.getInstance().setBlackPlayerScore(in.readObject());

	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {

		// Saving the date:
		// out.writeUTF(timeStamp);
		out.writeObject(timeStamp);

		// Saving the game play:
		previousBoard.add(new Board(SamoReversiGame.getInstance().getBoard()
				.getSize()));// To start with new state "reset" board.
		Collections.reverse(previousBoard); // now the list is in order and not
											// as stack...
		out.writeObject(previousBoard);

		// Saving players name:
		out.writeObject(SamoReversiGame.getInstance().getBlackPlayer()
				.getName());
		out.writeObject(SamoReversiGame.getInstance().getWhitePlayer()
				.getName());

		// Board Size:
		out.writeObject(SamoReversiGame.getInstance().getBoard().getSize());
		
		//Saving Players scores:also an array!
//		out.writeObject(SamoReversiGame.getInstance().getBlackPlayer().getScore());
//		out.writeObject(SamoReversiGame.getInstance().getWhitePlayer().getScore());
		
		//Saving who`s turn: --> Need to be an array

	}

	/**
	 * Saves the game to file within android
	 * 
	 * @throws IOException
	 */
	public void saveHistory() throws IOException {
		FileOutputStream fos = openFileOutput("Samorai_" + timeStamp,
				Context.MODE_PRIVATE);// Private mode ==> Only SamoReversi APP can summon this file.
		ObjectOutputStream out = new ObjectOutputStream(fos);
		out.writeObject(this); // Will call the writeExternal method above.
		fos.close();
		out.close();
	}

	public int getPlayerturn() {
		return playerturn;
	}

	public void setPlayerturn(int playerturn) {
		this.playerturn = playerturn;
	}
}
