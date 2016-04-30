package samo.reversi;

import bl.SamoReversiGame;
import android.app.Activity;
import android.app.AlertDialog;
import samo.reversi.R;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class SamoMenu extends Activity {
	private Button butPlayGame;
	private Button but2PlayersGame;
	private Button butOptions;
	private Button butGameRules;
	private Button butHistory;
	
	private ImageView GamePlayExample;
	//private SamoGamePlayExampleView gameView;
	private AnimationDrawable myAnimationDrawable;

	
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.menu);
	        
	    GamePlayExample = (ImageView)findViewById(R.id.imgGamePlay);	   
	        
	    myAnimationDrawable
	    = (AnimationDrawable)GamePlayExample.getDrawable();

	    GamePlayExample.post(
	    new Runnable(){

	      @Override
	      public void run() {
	       myAnimationDrawable.start();
	      }
	    });
	    
	    butHistory = (Button) findViewById(R.id.butHistory);
	    butHistory.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent("samo.reversi.HISTORY"));
			}
		});
	    
	     //gameView= new SamoGamePlayExampleView(GamePlayExample.getContext());
	     //GamePlayExample.setc(gameView);
	   	 butPlayGame = (Button) findViewById(R.id.butPlay);
	   	 butPlayGame.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {							
				SamoReversiGame.getInstance().setVsComputer(true);
				SamoHistoryActivity.isHistoryOn = false;
				startActivity(new Intent("samo.reversi.PLAY"));
			}
	   	 });
	   	but2PlayersGame = (Button) findViewById(R.id.but2Players);
	   	but2PlayersGame.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				SamoReversiGame.getInstance().setVsComputer(false);
				SamoHistoryActivity.isHistoryOn = false;
				startActivity(new Intent("samo.reversi.PLAY"));
								//finish();
			}
	   	 });
	   	butOptions = (Button) findViewById(R.id.butOptions);
	   	butOptions.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent("samo.reversi.OPTIONS"));				
				//				finish();
			}
	   	 });
	   	butGameRules = (Button) findViewById(R.id.butHowToPlay);   	
	   	butGameRules.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					startActivity(new Intent("samo.reversi.RULES"));				
					//				finish();
				}
		   	 });	
	  
	 }
	 public boolean onCreateOptionsMenu(Menu menu){
	        super.onCreateOptionsMenu(menu);
	        MenuInflater mioption=getMenuInflater();
	        mioption.inflate(R.menu.mainmenuinfo, menu);
	        return true;
	    }
	    
	    public boolean onOptionsItemSelected(MenuItem item){
	        switch(item.getItemId()){
	        case R.id.menugameinfo:
	        	LayoutInflater inflater = this.getLayoutInflater();
	   		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
	   					SamoMenu.this);
	   				alertDialogBuilder
	   					.setView(inflater.inflate(R.layout.about_dialog, null))
	   					.setOnCancelListener(new OnCancelListener() {
	   						
	   						@Override
	   						public void onCancel(DialogInterface dialog) {
	   							dialog.dismiss();
	   							
	   						}
	   					})
	   					/*.setPositiveButton("Back",new DialogInterface.OnClickListener() {
	   						public void onClick(DialogInterface dialog,int id) {
	   							// if this button is clicked, close
	   							// current activity
	   							dialog.dismiss();
	   						}
	   					  })*/;
	   				
	   	 
	   					// create alert dialog
	   					AlertDialog alertDialog = alertDialogBuilder.create();
	   	 
	   					// show it
	   					alertDialog.show();
	            //startActivity(new Intent(AndroidClient.this, connectionPage.class));
	            return true;
	        }
	        return false;
	    }

	 @Override
	   	public void onBackPressed() {
		 LayoutInflater inflater = this.getLayoutInflater();
		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					SamoMenu.this);
	 
				// set title
				//alertDialogBuilder.setTitle("SamoReversi");
		 	
				// set dialog message
				alertDialogBuilder
					.setMessage("are you sure you want to quit?")
					.setCustomTitle(inflater.inflate(R.layout.exit_dialog, null))
					.setOnCancelListener(new OnCancelListener() {
						
						@Override
						public void onCancel(DialogInterface dialog) {
							myAnimationDrawable.stop();
							SamoMenu.this.finish();
							
						}
					})
					.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, close
							// current activity
							myAnimationDrawable.stop();
							SamoMenu.this.finish();
						}
					  })
					.setNegativeButton("No",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, just close
							// the dialog box and do nothing
							dialog.dismiss();
						}
					});
				
	 
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
	 
					// show it
					alertDialog.show();
		 
	   	return;
	   	}

	 	 

}
