package samo.reversi;

import bl.SamoReversiGame;
import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class SamoWinner extends Activity{
	private Button butPlayAgain;
	private Button butReturnToMenu;
	private TextView BlackWin;
	private TextView WhiteWin;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        if(SamoReversiGame.getInstance().getWinner()==1){
	        	setContentView(R.layout.blackwin);
	        	butPlayAgain=(Button)findViewById(R.id.butPlayAgainBlack);
	        	butReturnToMenu=(Button)findViewById(R.id.butReturnToMenuBlack);
	        	BlackWin=(TextView)findViewById(R.id.txtBlackWin);
	        	BlackWin.setText(SamoReversiGame.getInstance().getBlackPlayer().getName()+"   Wins!!!  ");
	        	butPlayAgain.setOnClickListener(new View.OnClickListener() {

	    			@Override
	    			public void onClick(View v) {
	    				
	    				startActivity(new Intent("samo.reversi.PLAY"));
	    				finish();
	    			}
	    	   	 });
	        	butReturnToMenu.setOnClickListener(new View.OnClickListener() {

	    			@Override
	    			public void onClick(View v) {
	    				finish();
	    			}
	    	   	 });
	        }else if(SamoReversiGame.getInstance().getWinner()==2){
	        	setContentView(R.layout.whitewin);
	        	butPlayAgain=(Button)findViewById(R.id.butPlayAgainWhite);
	        	butReturnToMenu=(Button)findViewById(R.id.butReturnToMenuWhite);
	        	WhiteWin=(TextView)findViewById(R.id.txtWhiteWin);
	        	WhiteWin.setText(SamoReversiGame.getInstance().getWhitePlayer().getName()+"   Wins!!!  ");
	        	if(SamoReversiGame.getInstance().isVsComputer()){
	        		SamoReversiGame.getInstance().getWhitePlayer().setName("Player 2");
	        	}
	        	butPlayAgain.setOnClickListener(new View.OnClickListener() {

	    			@Override
	    			public void onClick(View v) {
	    				
	    				startActivity(new Intent("samo.reversi.PLAY"));
	    				finish();
	    			}
	    	   	 });
	        	butReturnToMenu.setOnClickListener(new View.OnClickListener() {

	    			@Override
	    			public void onClick(View v) {	    				
	    				finish();
	    			}
	    	   	 });
	        }else{
	        	setContentView(R.layout.tiegame);
	        	butPlayAgain=(Button)findViewById(R.id.butPlayAgainTie);
	        	butReturnToMenu=(Button)findViewById(R.id.butReturnToMenuTie);
	        	WhiteWin=(TextView)findViewById(R.id.txtTiewhite);
	        	BlackWin=(TextView)findViewById(R.id.txtTieblack);
	        	WhiteWin.setText("NO ONE WINS");
	        	BlackWin.setText("IT'S A TIE");
	        	butPlayAgain.setOnClickListener(new View.OnClickListener() {
	    			@Override
	    			public void onClick(View v) {	    				
	    				startActivity(new Intent("samo.reversi.PLAY"));
	    				finish();
	    			}
	    	   	 });
	        	butReturnToMenu.setOnClickListener(new View.OnClickListener() {
	    			@Override
	    			public void onClick(View v) {
	    				finish();
	    			}
	    	   	 });	        	
	        }
	 }

}
