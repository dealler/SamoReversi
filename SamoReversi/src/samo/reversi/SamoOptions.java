package samo.reversi;

import samo.reversi.R.id;
import bl.Board;
import bl.SamoReversiGame;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SamoOptions extends Activity {
	private Button butSavingOptions;
	private TextView player1Name;
	private EditText editplayer1Name;
	private EditText editplayer2Name;
	private RatingBar ratingComputer;
	private TextView player2Name;
	private Spinner spinSize;
	private String[] items;
	private ArrayAdapter<String> adapter;
	private boolean is2Save2History;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.options);
		
		editplayer1Name=(EditText)findViewById(R.id.EditTxtPlayer1Name);
		editplayer2Name=(EditText)findViewById(R.id.editTxtPlayer2Name);
		editplayer1Name.setText(SamoReversiGame.getInstance().getBlackPlayer().getName());
		editplayer2Name.setText(SamoReversiGame.getInstance().getWhitePlayer().getName());
		
		//board4 = (RadioButton) findViewById(R.id.radioButSize4by4);
		//board6 = (RadioButton) findViewById(R.id.radioButSize6by6);
		//board8 = (RadioButton) findViewById(R.id.radioButSize8by8);
		
		items = new String[] {"8 X 8","6 X 6","4 X 4"};
		spinSize = (Spinner) findViewById(R.id.spinSize);
		adapter = new ArrayAdapter<String>(this,
	            android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinSize.setAdapter(adapter);
		
		if(SamoReversiGame.getInstance().getBoard().getSize()==8){
			spinSize.setSelection(0);
		}
		else if(SamoReversiGame.getInstance().getBoard().getSize()==6){
			spinSize.setSelection(1);
		}
		else{
			spinSize.setSelection(2);
		}
		
		ratingComputer=(RatingBar)findViewById(R.id.ratingcomputer);
		ratingComputer.setRating(SamoReversiGame.getInstance().getRatingComputer());
		

		butSavingOptions = (Button) findViewById(R.id.butSavingOptions);
		butSavingOptions.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				//CheckBox chkHistoryAvailable = (CheckBox) findViewById(id.chkBoxHistory);
				//SamoHistoryActivity.is2Save2History = chkHistoryAvailable.isChecked();
				//is2Save2History =chkHistoryAvailable.isChecked();
						
				player1Name = (TextView) findViewById(R.id.EditTxtPlayer1Name);
				player2Name = (TextView) findViewById(R.id.editTxtPlayer2Name);

				// Validity checks:
				if ((player1Name.getText().toString().equals(""))
						|| (player2Name.getText().toString().equals(""))) {
					Toast.makeText(getApplicationContext(),
							"Invalid player name.", Toast.LENGTH_LONG).show();
					return;
				}
				
				SamoReversiGame.getInstance().setRatingComputer((int)ratingComputer.getRating());
				if (spinSize.getSelectedItemPosition()==2) {
					SamoReversiGame.getInstance().setSettings(new Board(4),
							editplayer1Name.getText().toString(),
							editplayer2Name.getText().toString(),
							SamoReversiGame.getInstance().isVsComputer());
				} else if (spinSize.getSelectedItemPosition()==1) {
					SamoReversiGame.getInstance().setSettings(new Board(6),
							editplayer1Name.getText().toString(),
							editplayer2Name.getText().toString(),
							SamoReversiGame.getInstance().isVsComputer());
				} else if (spinSize.getSelectedItemPosition()==0) {
					SamoReversiGame.getInstance().setSettings(new Board(8),
							editplayer1Name.getText().toString(),
							editplayer2Name.getText().toString(),
							SamoReversiGame.getInstance().isVsComputer());
				}

				/*vsAndroid = (CheckBox) findViewById(R.id.VsComputer);
				if (vsAndroid.isChecked()) {
					SamoGameBoard.vsComputer = true;
				} else {
					SamoGameBoard.vsComputer = false;
				}*/
				finish();
			}

		});
	}

}
