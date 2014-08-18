package sandm.code.earthquake;

import sandm.code.earthquake.shapeengin.PersianReshap;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Typeface;
import android.widget.TextView;

public class Alert extends Activity {

	TextView textview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_layout);
		//
		textview=(TextView)findViewById(R.id.textView1);
		textview.setTypeface(Typeface.createFromAsset(this.getAssets(), "font/irsans.ttf"));
		boolean contact=getIntent().getBooleanExtra("Page", true);
		if(contact)
		{
			String data="\n\nبرای انتقاد,پیشنهاد و یا سفارش نرم افزار با ایمیل زیر تماس حاصل فرمایید\n\n\n" +
					"sa.akbari66@gmail.com \n\n\n" +
					"شماره تماس \n\n" +
					"09122710858\n\n\n\n با تشکر";
			textview.setText(PersianReshap.reshape(data));
		}else
		{
			String data="\nگزیده ای از مقالات مهندس علیرضا سعیدی دبیر جمعیت کاهش خطرات زلزله ایران\n\n" +
					"www.Ehrsi.com\n\n" +
					"www.tebyan.net";
			textview.setText(PersianReshap.reshape(data));
		}
	}

	

}
