package sandm.code.earthquake;



import java.util.ArrayList;

import sandm.code.earthquake.db.DbAdapter;
import sandm.code.earthquake.db.Probs;
import sandm.code.earthquake.menu.CustomMenu;
import sandm.code.earthquake.menu.CustomMenuItem;
import sandm.code.earthquake.shapeengin.PersianReshap;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;

public class MainActivity extends Activity implements CustomMenu.OnMenuItemSelectedListener,
	ExpandableListView.OnChildClickListener,ExpandableListView.OnGroupExpandListener{

	// Sample data set.  children[i] contains the children (String[]) for groups[i].
	private String[] groups = {"آشنایی با زلزله", "آمادگی در برابر زلزله", "هنگام زلزله", "پس از زلزله"};
	private String[][] children = {
			{"زلزله چیست؟","سؤالات رایج در باب زلزله"},
			{ "قبل از زلزله", "کیف امداد و نجات"},
			{ "هنگام زلزله"},
			{ "پس از زلزله" }
	};

	private CustomMenu mMenu;
	RelativeLayout background;
	public static int Reshape;
	DbAdapter database;
	ExpandableListAdapter mAdapter;
	ExpandableListView list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initial();
	}

	private void initial() {
		database=new DbAdapter(this);
		loadReshape();
		checkfirsttime();
		loadReshape();
		mAdapter = new MyExpandableListAdapter(this);
		list=(ExpandableListView)findViewById(R.id.mainlistview);
		background=(RelativeLayout)findViewById(R.id.background);
		list.setAdapter(mAdapter);
		registerForContextMenu(list);
		//list.setBackgroundColor(Color.parseColor("#444444"));
		list.setOnChildClickListener(this);
		list.setOnGroupExpandListener(this);
		list.setDivider(null);
		initialmenu();
		
	}



	private void loadReshape() {
		Reshape=database.SelectProbs(Probs.RESHAPE);
		
	}

	private void checkfirsttime() {
		if(database.SelectProbs(Probs.FIRST)==1)
		{
			final Dialog dialog = new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
			dialog.setContentView(R.layout.dialog_reshape);
			//Typeface face = Typeface.createFromAsset(contex.getAssets(),"font/"+farin.code.rahnamaee.attrib.attribute.font_title);

			Button enablereshape = (Button) dialog.findViewById(R.id.withreshape);
			enablereshape.setText(PersianReshap.reshape(getResources().getString(R.string.trytxt)));
			enablereshape.setTypeface(Typeface.createFromAsset(this.getAssets(), "font/irsans.ttf"));
			//enablereshape.setTextSize(farin.code.rahnamaee.attrib.attribute.title1_font_size);
			// if button is clicked, close the custom dialog
			enablereshape.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					database.UpdateProbs(Probs.RESHAPE,1);
					Reshape=1;
					database.UpdateProbs(Probs.FIRST,0);
				}
			});
			Button disablereshape = (Button) dialog.findViewById(R.id.withoutreshape);
			disablereshape.setText(getResources().getString(R.string.trytxt));
			disablereshape.setTypeface(Typeface.createFromAsset(this.getAssets(), "font/irsans.ttf"));
			//disablereshape.setTextSize(farin.code.rahnamaee.attrib.attribute.title1_font_size);
			// if button is clicked, close the custom dialog
			disablereshape.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					database.UpdateProbs(Probs.RESHAPE,0);
					Reshape=0;
					database.UpdateProbs(Probs.FIRST,0);
				}
			});
			//
			TextView trylbl =(TextView)dialog.findViewById(R.id.trylbl);
			trylbl.setTypeface(Typeface.createFromAsset(this.getAssets(), "font/irsans.ttf"));
			trylbl.setText(PersianReshap.reshape(getResources().getString(R.string.trylbl)));
			//
			try{
				dialog.getWindow().getAttributes().windowAnimations = R.style.reshapDialogAnimation;
				dialog.show();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void onGroupExpand(int groupPosition) {
		
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
			int childPosition, long id) {
		String lable=children[groupPosition][childPosition];
		Intent intent=new Intent(MainActivity.this, List.class);
		intent.putExtra("TITLE", lable);
		if(groupPosition==0&&childPosition==0)
		{
			intent.putExtra("ID", "00000");
		}
		else if(groupPosition==0&&childPosition==1)
		{
			intent.putExtra("ID", "000000");
		}
		else if(groupPosition==1&&childPosition==0)
		{
			intent.putExtra("ID", "0");
		}
		else if(groupPosition==1&&childPosition==1)
		{
			intent.putExtra("ID", "00");
		}
		else if(groupPosition==2&&childPosition==0)
		{
			intent.putExtra("ID", "000");
		}
		else if(groupPosition==3&&childPosition==0)
		{
			intent.putExtra("ID", "0000");
		}
		startActivity(intent);
		return true;
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




	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();
		String title = ((TextView) info.targetView).getText().toString();
		int type = ExpandableListView.getPackedPositionType(info.packedPosition);
		if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
			int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition); 
			int childPos = ExpandableListView.getPackedPositionChild(info.packedPosition); 
			Toast.makeText(this, title + ": Child " + childPos + " clicked in group " + groupPos,
					Toast.LENGTH_SHORT).show();
			return true;
		} else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
			int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition); 
			Toast.makeText(this, title + ": Group " + groupPos + " clicked", Toast.LENGTH_SHORT).show();
			return true;
		}

		return false;
	}

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
			
			mMenu.show(background);
		}
	}




	//********************adapter class************************
	/**
	 * A simple adapter which maintains an ArrayList of photo resource Ids. 
	 * Each photo is displayed as an image. This adapter supports clearing the
	 * list of photos and adding a new photo.
	 *
	 */
	public class MyExpandableListAdapter extends BaseExpandableListAdapter {

		private Context mContext;

		public MyExpandableListAdapter(Context mContext) {
			super();
			this.mContext = mContext;
		}

		public Object getChild(int groupPosition, int childPosition) {
			return children[groupPosition][childPosition];
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			return children[groupPosition].length;
		}

		public TextView getGenericView() {
			// Layout parameters for the ExpandableListView
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, 64);

			TextView textView = new TextView(MainActivity.this);
			textView.setLayoutParams(lp);
			// Center the text vertically
			textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			// Set the text starting position
			textView.setPadding(36, 0, 0, 0);
			return textView;
		}

		public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
				View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View mView;

			if (convertView == null) {

				mView = new View(mContext);
				mView = inflater.inflate(R.layout.list_subitem_row, null);

			} else {
				mView = convertView;
			}
			TextView textView = (TextView)mView.findViewById(R.id.titlelbl);
			textView.setText(PersianReshap.reshape( getChild(groupPosition, childPosition).toString()));
			textView.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "font/irsans.ttf"));
			return mView;
		}

		public Object getGroup(int groupPosition) {
			return groups[groupPosition];
		}

		public int getGroupCount() {
			return groups.length;
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View mView;

			if (convertView == null) {

				mView = new View(mContext);
				mView = inflater.inflate(R.layout.list_header_row, null);

			} else {
				mView = convertView;
			}

			TextView textView = (TextView)mView.findViewById(R.id.titlelbl);
			textView.setText(PersianReshap.reshape( getGroup(groupPosition).toString()));  
			textView.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "font/irsans.ttf"));
			return mView;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		public boolean hasStableIds() {
			return true;
		}

	}




	@Override
	public void MenuItemSelectedEvent(CustomMenuItem selection) {
		if(selection.getId()==1)
		{
			Intent i=new Intent(MainActivity.this,Alert.class);
			i.putExtra("Page", false);
			startActivity(i);
		}
		//
		else if(selection.getId()==2)
		{
			Intent i=new Intent(MainActivity.this,Alert.class);
			i.putExtra("Page", true);
			startActivity(i);
		}


	}

}
