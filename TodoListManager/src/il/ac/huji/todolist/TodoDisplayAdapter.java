package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.graphics.Color;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TodoDisplayAdapter extends ArrayAdapter <ToDoItem>{

	public TodoDisplayAdapter(TodoListManagerActivity activity , List<ToDoItem> textViewResourceId) {
		super(activity,0,textViewResourceId);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView,ViewGroup parent ){
		ToDoItem item = getItem(position);
		LayoutInflater inf = LayoutInflater.from(getContext());
		View v = inf.inflate(R.layout.todo_item, null);
		TextView todoItem = (TextView)v.findViewById(R.id.txtTodoTitle);
		TextView todoDate = (TextView)v.findViewById(R.id.txtTodoDate);
		todoItem.setText(item.todoStr);
		
		if (item.eventDate == null){
			todoDate.setText("No due date");
			return v;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String dueStr = formatter.format(item.eventDate);
		
		todoDate.setText(dueStr);
		Time now = new Time();
		now.setToNow();
		@SuppressWarnings("deprecation")
		Date nowD = new Date (now.year-1900,now.month,now.monthDay);
		
		if (item.eventDate.before(nowD)){
			todoDate.setTextColor(Color.RED);
			todoItem.setTextColor(Color.RED);
		}
		return v;
		
	}

}
