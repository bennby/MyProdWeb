package com.example.myprodWeb;



import com.example.myprodWeb.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;

public class BirthdayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_birthday);
		
		Button btn_OK=(Button)findViewById(R.id.btn_OK);
		
		btn_OK.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DatePicker datePickerBir=(DatePicker)findViewById(R.id.datePickerBir);
				Intent intent=new Intent();
				int year=datePickerBir.getYear();
				int month=datePickerBir.getMonth();
				int day=datePickerBir.getDayOfMonth();
				String str=Integer.toString(year)+"/"+Integer.toString(month+1)+"/"+Integer.toString(day);
				intent.putExtra("birthday_data", str);
				setResult(1,intent);
				finish();
			}
			
		});
		
	}

}
