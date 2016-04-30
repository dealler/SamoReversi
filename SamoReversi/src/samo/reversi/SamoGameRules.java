package samo.reversi;

import bl.SamoReversiGame;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;

public class SamoGameRules extends Activity {
	private Button butBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gamerules);
		
		butBack=(Button)findViewById(R.id.butBack);
		butBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

					finish();
			}
	   	 });
	}
}
