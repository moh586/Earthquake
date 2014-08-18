package sandm.code.earthquake;


import java.util.ArrayList;

import sandm.code.earthquake.menu.CustomMenu;
import sandm.code.earthquake.menu.CustomMenuItem;
import sandm.code.earthquake.shapeengin.PersianReshap;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Content extends Activity implements CustomMenu.OnMenuItemSelectedListener {

	//DbAdapter database;
	private String title;
	private String Content;
	private TableLayout table;
	CustomMenu mMenu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);
		initial();
	}

	private void initial() {
		getExtra();
		loadTitle();
		loadContent();
		initialbutton();
		initialmenu();
	}
	//
	private void initialmenu()
	{
		mMenu = new CustomMenu(this, this, getLayoutInflater());
		mMenu.setHideOnSelect(true);
		mMenu.setItemsPerLineInPortraitOrientation(3);
		mMenu.setItemsPerLineInLandscapeOrientation(8);
		//load the menu items
		loadMenuItems();
	}


	/**
	 * Load up our menu.
	 */
	private void loadMenuItems() {
		//This is kind of a tedious way to load up the menu items.
		//Am sure there is room for improvement.
		ArrayList<CustomMenuItem> menuItems = new ArrayList<CustomMenuItem>();
		CustomMenuItem cmi = new CustomMenuItem();
		cmi.setCaption(getResources().getString(R.string.document));
		cmi.setImageResourceId(R.drawable.document);
		cmi.setId(1);
		menuItems.add(cmi);
		cmi = new CustomMenuItem();
		cmi.setCaption(getResources().getString(R.string.aboutus));
		cmi.setImageResourceId(R.drawable.contactus);
		cmi.setId(2);
		menuItems.add(cmi);

		if (!mMenu.isShowing())
			try {
				mMenu.setMenuItems(menuItems);
			} catch (Exception e) {
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("Egads!");
				alert.setMessage(e.getMessage());
				alert.show();
			}
	}

	/**
	 * Toggle our menu on user pressing the menu key.
	 */
	private void doMenu() {
		if (mMenu.isShowing()) {
			mMenu.hide();
		} else {
			
			mMenu.show(findViewById(R.id.contentrl));
		}
	}
	@Override
	public void MenuItemSelectedEvent(CustomMenuItem selection) {
		if(selection.getId()==1)
		{
			Intent i=new Intent(Content.this,Alert.class);
			i.putExtra("Page", false);
			startActivity(i);
		}
		//
		else if(selection.getId()==2)
		{
			Intent i=new Intent(Content.this,Alert.class);
			i.putExtra("Page", true);
			startActivity(i);
		}


	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//Handle the back button
		if( keyCode == KeyEvent.KEYCODE_BACK ) {
			//Ask the user if they want to quit
			if (mMenu.isShowing()) {
				doMenu();
				return true; //always eat it!
			}
			finish();

		}
		else if (keyCode == KeyEvent.KEYCODE_MENU) {
			doMenu();
			return true; //always eat it!
		}

		return super.onKeyDown(keyCode, event);
	}
	
	//
	
	private void initialbutton() {
		ImageButton back=(ImageButton)findViewById(R.id.backbtn);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});
		
	}
	
	private void loadContent() {
		String[] rows=Content.split("\n");
		LayoutInflater inflate=getLayoutInflater();
		table=(TableLayout)this.findViewById(R.id.tableitems);
		for(int i=0;i<rows.length;i++)
		{
			if(rows[i].length()<=1)
				continue;
			if(rows[i].contains("IMG"))
			{
				TableRow t=(TableRow)inflate.inflate(R.layout.image, table, false);
				ImageView image=(ImageView)t.findViewById(R.id.imageView1);
				int offset=Integer.parseInt(rows[i].substring(3));
				image.setImageResource(R.drawable.img1+offset-1);
				//
				//Typeface face = Typeface.createFromAsset(contex.getAssets(),"font/"+farin.code.rahnamaee.attrib.attribute.font_title3);
				//text.setTypeface(face);
				//text.setText(rows[i]);
				//text.setTypeface(Typeface.createFromAsset(this.getAssets(), "font/irsans.ttf"));
				//text.setText(PersianReshape.reshape(rows[i]));
				//
				table.addView(t);
			}
			else
			{
				TableRow t=(TableRow)inflate.inflate(R.layout.tile, table, false);
				TextView text=(TextView)t.findViewById(R.id.tiletxt);
				//
				//Typeface face = Typeface.createFromAsset(contex.getAssets(),"font/"+farin.code.rahnamaee.attrib.attribute.font_title3);
				//text.setTypeface(face);
				text.setText(PersianReshap.reshape(rows[i]));
				text.setTypeface(Typeface.createFromAsset(this.getAssets(), "font/irsans.ttf"));
				//text.setText(PersianReshape.reshape(rows[i]));
				//
				table.addView(t);
			}
		}
		
	}

	private void loadTitle() {
		TextView listtitle=(TextView)findViewById(R.id.contenttitlelbl);
		listtitle.setTypeface(Typeface.createFromAsset(this.getAssets(), "font/irsans.ttf"));
		listtitle.setText(PersianReshap.reshape(title));
	}

	private void getExtra() {
		Intent intent=getIntent();
		if(intent!=null)
		{
			title=intent.getStringExtra("TITLE");
			Content=intent.getStringExtra("CONTENT");
			//content=database.SelectContent(CategoryID);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.content, menu);
		return true;
	}

}
