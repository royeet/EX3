package il.ac.huji.todolist;

import java.util.Date;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;




public class TodoListManagerActivity extends Activity {
	public static String TABLE_NAME   = "todo";
	public static String TITLE_COLUMN = "title";
	public static String DATE_COLUMN  = "due";
	public static String ID_COLUMN    = "_id";
	
	
	private SimpleCursorAdapter adapter;
	ListView todoListView;
	
	TodoDAL dataManager; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataManager = new TodoDAL(this);
		setContentView(R.layout.activity_todo_list);
		setAdapter();
		
	}



	private void setAdapter() {
		SQLiteDatabase in = dataManager.getDBHelper().getReadableDatabase();
		String [] querystr = {ID_COLUMN,TITLE_COLUMN,DATE_COLUMN};		
		Cursor cursor = in.query(TABLE_NAME, querystr , null, null, null, null, null);
		String [] from = {TITLE_COLUMN,DATE_COLUMN};
		int    [] to   = {R.id.txtTodoTitle, R.id.txtTodoDate};
		adapter = new TodoDisplayCursorAdapter(this, R.layout.todo_item,cursor,from,to);
		
		todoListView = (ListView) findViewById(R.id.lstTodoItems);
		todoListView.setAdapter(adapter);
		registerForContextMenu(todoListView);
		
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_todo_list, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuItemAdd:
			addNewItem();
			break;
		}
		return true;
	}


	private void addNewItem() {
		Intent intent = new Intent(this, AddNewTodoItemActivity.class);
		startActivityForResult(intent,0); 
	}



	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) return;

		Bundle mBundle = data.getExtras();
		Date dueDate = (Date) mBundle.get("dueDate");	
		String title = (String) mBundle.get("title");
		ITodoItem newItem = new ToDoItem(title, dueDate);
		
		dataManager.insert(newItem);
		
		adapter.getCursor().requery();
	}

	private ITodoItem getTodoItemByID (long id){
		SQLiteDatabase db = dataManager.getDBHelper().getReadableDatabase();
		String [] querystr = {ID_COLUMN,TITLE_COLUMN,DATE_COLUMN};		
		Cursor cursor = db.query(TABLE_NAME, querystr , "_id = " +id,null, null, null, null);
		if (cursor.moveToFirst()){
			String title = cursor.getString(1);
			Date date 	 = new Date (cursor.getLong(2));
			return new ToDoItem(title, date);
		}
		return null;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		
		super.onCreateContextMenu(menu, v, menuInfo);

		getMenuInflater().inflate(R.menu.context_menu, menu); 
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		//String todo = todoItemsList.get(info.position).getTitle();
		
		String todo = getTodoItemByID(info.id).getTitle();
		menu.setHeaderTitle(todo);
		if (todo.startsWith("Call ")){
			menu.getItem(1).setTitle(todo);
		}
		else{
			menu.removeItem(R.id.menuItemCall);
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		ITodoItem tdi = getTodoItemByID(info.id);
		switch (item.getItemId()){
		case R.id.menuItemDelete:
			dataManager.delete(tdi);
			adapter.getCursor().requery();
			break;
		case R.id.menuItemCall: 
			Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tdi.getTitle().substring(5))); 
			startActivity(dial);
			break;

		}


		return true;
	}

}
