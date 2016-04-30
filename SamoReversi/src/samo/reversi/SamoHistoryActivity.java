package samo.reversi;

import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SamoHistoryActivity extends Activity {
	/**
	 * If a game selected from the history list.
	 */
	public static boolean isHistoryOn  = false;// Mode History the game not supposed to happend.
	public static boolean is2Save2History=true; // if allowed to save more games;
	public String fileName;
	private String listview_array[];
	private ArrayAdapter<String> adapterhistory;
	private Context context;
	private SamoGameBoard sgb;
	private InputStream in = null;
	private ObjectInputStream ois = null;
	private Button LoadButton;
	private Button DeleteButton;
	private ListView LVhistory;
	
	/**
	 * Scans list of saved files and return them as String array.
	 */
	private String[] getSavedFilesList() {
		
		File f = this.getFilesDir();//new File("/"+"/data//" + "/"); //data/samo.reversi/files/");
		if (f.exists() && f.canRead()) {
			return f.list();
		}
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gamehistory);

		try {
			listview_array = getSavedFilesList();
			if (listview_array == null) {
				Toast.makeText(this, "Never saved a game - the list is empy",
						Toast.LENGTH_SHORT).show();
				return;
			}

			//adapter = new MyExpandableListAdapter(this, listview_array);
			adapterhistory = new ArrayAdapter<String>(this,
		            android.R.layout.simple_spinner_item, listview_array);

			//ExpandableListView listView = (ExpandableListView) findViewById(R.id.historyList);
			LVhistory=(ListView)findViewById(R.id.LVhistory);
			LVhistory.setAdapter(adapterhistory);
			
			LVhistory.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					fileName=(String) arg0.getItemAtPosition(arg2);
					Toast.makeText(SamoHistoryActivity.this,
							"Selected play: " + fileName, Toast.LENGTH_LONG)
							.show();
					
				}
				
			});
			
			LoadButton=(Button)findViewById(R.id.butLoad);
			LoadButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					sgb = new SamoGameBoard();
					
					//fileName = (String) LVhistory.getSelectedItem();
					if(fileName== null){
						Toast.makeText(SamoHistoryActivity.this,
								"Please choose a game from the history list", Toast.LENGTH_LONG).show();
					}
					else{
					try{
						in = openFileInput(fileName);
						ois = new ObjectInputStream(in);
						sgb = (SamoGameBoard) ois.readObject();
						isHistoryOn = true;// After reading successfully

						// Start SamoGameBoard Activity:
						startActivity(new Intent(SamoHistoryActivity.this, sgb.getClass()));
						in.close();
						ois.close();
					}catch(Exception e){
						System.out.println(e.getMessage());
					} finally{
	
					}
				  }
					
				}
			});

			DeleteButton=(Button)findViewById(R.id.butDelete);
			DeleteButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//fileName = (String) LVhistory.getSelectedItem();
					if(fileName== null){
						Toast.makeText(SamoHistoryActivity.this,
								"Please choose a game from the history list", Toast.LENGTH_LONG).show();
					}
					else{
					if(SamoHistoryActivity.this.deleteFile(fileName))
					{
						Toast.makeText(SamoHistoryActivity.this,
								"Deleted game: " + fileName, Toast.LENGTH_LONG)
								.show();
					}else{
						Toast.makeText(SamoHistoryActivity.this,
								"can not Delete this game: " + fileName, Toast.LENGTH_LONG)
								.show();
					}
					
					
						listview_array = getSavedFilesList();
						if (listview_array == null) {
							Toast.makeText(SamoHistoryActivity.this, "Never saved a game - the list is empy",
									Toast.LENGTH_SHORT).show();
							return;
						}
						adapterhistory = new ArrayAdapter<String>(SamoHistoryActivity.this,
					            android.R.layout.simple_spinner_item, listview_array);
						LVhistory.setAdapter(adapterhistory);
				}
			}
			});
			
			
			

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
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
   					SamoHistoryActivity.this);
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

}