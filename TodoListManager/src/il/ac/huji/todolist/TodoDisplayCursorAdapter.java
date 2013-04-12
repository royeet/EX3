package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TodoDisplayCursorAdapter extends SimpleCursorAdapter{

	public TodoDisplayCursorAdapter(Context c, int layout, Cursor cursor, String[] from, int [] to) {
		super(c, R.layout.todo_item,cursor,from,to);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	  public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inf = LayoutInflater.from(context);

	    return inf.inflate(R.layout.todo_item, null);
	  }
	@Override
	public void bindView(View v, Context context, Cursor cursor){
		String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
		long time    = cursor.getLong(cursor.getColumnIndexOrThrow("due"));
		Date parsedDate = new Date (time);
		ITodoItem item = new ToDoItem(title, parsedDate);
		TextView todoItem = (TextView)v.findViewById(R.id.txtTodoTitle);
		TextView todoDate = (TextView)v.findViewById(R.id.txtTodoDate);
		todoItem.setText(item.getTitle());
		
		if (item.getDueDate() == null){
			todoDate.setText("No due date");
			return;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String dueStr = formatter.format(item.getDueDate());
		
		todoDate.setText(dueStr);
		Time now = new Time();
		now.setToNow();
		Date nowD = new Date (now.year-1900,now.month,now.monthDay);
		
		if (item.getDueDate().before(nowD)){
			todoDate.setTextColor(Color.RED);
			todoItem.setTextColor(Color.RED);
		}
		else{
			todoDate.setTextColor(Color.BLACK);
			todoItem.setTextColor(Color.BLACK);
		}
		return;
		
	}

}
