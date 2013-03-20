package il.ac.huji.todolist;

import java.util.Date;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddNewTodoItemActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_item);
		setOkButton();
		setCancelButton();
	}

	private void setCancelButton() {
		Button cancel = (Button)findViewById(R.id.btnCancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = getIntent();
				setResult(RESULT_CANCELED, intent);
				finish();				
			}
		});
		
	}

	private void setOkButton() {
		Button ok = (Button)findViewById(R.id.btnOK);
		final DatePicker dp = (DatePicker)findViewById(R.id.datePicker);
		final EditText   et = (EditText)  findViewById(R.id.edtNewItem);
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = getIntent();
				
				@SuppressWarnings("deprecation")
				Date due = new Date (dp.getYear()-1900, dp.getMonth(), dp.getDayOfMonth());
				
				intent.putExtra("dueDate", due);
				intent.putExtra("title", et.getText().toString());
				setResult(RESULT_OK, intent);
				finish();				
			}
		});
		
	}
}
