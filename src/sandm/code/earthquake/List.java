package sandm.code.earthquake;

import java.util.ArrayList;

import sandm.code.earthquake.db.Content;
import sandm.code.earthquake.db.DbAdapter;
import sandm.code.earthquake.menu.CustomMenu;
import sandm.code.earthquake.menu.CustomMenuItem;
import sandm.code.earthquake.shapeengin.PersianReshap;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class List extends Activity implements CustomMenu.OnMenuItemSelectedListener {


	java.util.List<Content> content;
	DbAdapter database;
	ListView listView;
	private CustomMenu mMenu;
	private String title="",CategoryID="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		database=new DbAdapter(this);
		getExtra();
		initial();

	}

	private void getExtra() {
		Intent intent=getIntent();
		if(intent!=null)
		{
			title=intent.getStringExtra("TITLE");
			CategoryID=intent.getStringExtra("ID");
			content=database.SelectContent(CategoryID);
		}

	}

	private void initial() {
		database=new DbAdapter(this);
		initialtitle();
		initiallistview();
		initialbutton();
		initialmenu();
	}

	private void initialbutton() {
		ImageButton back=(ImageButton)findViewById(R.id.backbtn);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});
		
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
			
			mMenu.show(findViewById(R.id.listrl));
		}
	}
	@Override
	public void MenuItemSelectedEvent(CustomMenuItem selection) {
		if(selection.getId()==1)
		{
			Intent i=new Intent(List.this,Alert.class);
			i.putExtra("Page", false);
			startActivity(i);
		}
		//
		else if(selection.getId()==2)
		{
			Intent i=new Intent(List.this,Alert.class);
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
	
	

	private void initiallistview() {
		String[] items=LoadRows(CategoryID);
		listView=(ListView)findViewById(R.id.listitems);
		listView.setDivider(null);
		//
		listView.setAdapter(new CustomArrayAdapter(this, items));
		// set image based on selected text
		listView.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View v,int position, long id) 
			{
				/*ScaleAnimation scale = new ScaleAnimation(1.1f, 0.9f, 1.1f, 0.9f, 
						ScaleAnimation.RELATIVE_TO_SELF, 1f, 
						ScaleAnimation.RELATIVE_TO_SELF, 1f);
				scale.setDuration(300);
				scale.setRepeatCount(0);
				//
				v.startAnimation(scale);
				*/
				final int positiontemp=position;
				final TextView textView = (TextView) v
						.findViewById(R.id.tiletxt);
				textView.setTextColor(Color.parseColor("#33b5e5"));
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						textView.setTextColor(Color.parseColor("#333333"));
						//
						java.util.List<Content> temp=database.SelectContent(String.valueOf(content.get(positiontemp).id));

						if(temp.size()==1)
						{
							Intent intent=new Intent(List.this,sandm.code.earthquake.Content.class);
							intent.putExtra("TITLE", content.get(positiontemp).content);

							intent.putExtra("CONTENT", temp.get(0).content);
							startActivity(intent);
						}
						else
						{
							Intent intent=new Intent(List.this, List.class);
							intent.putExtra("TITLE", content.get(positiontemp).content);

							intent.putExtra("ID", String.valueOf(content.get(positiontemp).id));
							startActivity(intent);
						}
					}
				}, 300);
				//
				
				//
				

			}
		});

	}

	private String[] LoadRows(String categoryID2) {
		String[] out=new String[content.size()];
		for(int i=0;i<out.length;i++)
			out[i]=content.get(i).content;
		return out;
	}

	private void initialtitle() {
		TextView listtitle=(TextView)findViewById(R.id.listtitlelbl);
		listtitle.setText(PersianReshap.reshape(title));
		listtitle.setTypeface(Typeface.createFromAsset(this.getAssets(), "font/irsans.ttf"));

	}

	///**************************List Adapter************************
	public class CustomArrayAdapter extends BaseAdapter 
	{
		private Context context;
		private final String[] Values;


		public CustomArrayAdapter(Context context, String[] Values) {
			this.context = context;
			this.Values = Values;
		}
		
		

		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View mView;

			if (convertView == null) {

				mView = new View(context);
				mView = inflater.inflate(R.layout.activity_list_row, null);

				//


			} else {
				mView = convertView;
			}

			// set value into textview
			TextView textView = (TextView) mView
					.findViewById(R.id.tiletxt);
			textView.setText(PersianReshap.reshape(Values[position]));
			textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "font/irsans.ttf"));
			//Typeface face = Typeface.createFromAsset(context.getAssets(),"font/"+farin.code.rahnamaee.attrib.attribute.font_title2);
			//textView.setTypeface(face);
			//textView.setWidth(imageView.getWidth());
			//textView.setTextSize(farin.code.rahnamaee.attrib.attribute.title2_font_size);
			//textView.setTextColor(farin.code.rahnamaee.attrib.attribute.title2_font_color);

			return mView;
		}


		public int getCount() {
			return Values.length;
		}


		public Object getItem(int position) {
			return null;
		}


		public long getItemId(int position) {
			return 0;
		}
	}

}
