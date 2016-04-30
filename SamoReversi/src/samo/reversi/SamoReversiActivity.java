package samo.reversi;

import android.app.Activity;
import samo.reversi.R;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SamoReversiActivity extends Activity {
	private MediaPlayer MPsound;
    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	MPsound = MediaPlayer.create(this, R.raw.gong2);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);
		
		Thread welcomeTimer = new Thread(){
			public void run(){
				try{
					int welcomeTimer = 0;
					MPsound.setVolume(0.3f, 0.3f);
					MPsound.start();
					while(welcomeTimer < 3700){
						sleep(100);
						welcomeTimer = welcomeTimer +100;
					}
					startActivity(new Intent("samo.reversi.MENU"));
				}
				catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				finally{
					MPsound.stop();
					finish();
				}
			}
		};
		welcomeTimer.start();
		
	}
}