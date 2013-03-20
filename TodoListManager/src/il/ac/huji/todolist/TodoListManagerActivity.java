package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;



public class TodoListManagerActivity extends Activity {

	private ArrayAdapter<ToDoItem> adapter;
	private List<ToDoItem> todoItemsList;
	ListView todoListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list);
		todoItemsList = new ArrayList<ToDoItem>();
		adapter = new TodoDisplayAdapter(this, todoItemsList);
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



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) return;

		Bundle mBundle = data.getExtras();
		Date dueDate = (Date) mBundle.get("dueDate");	
		String title = (String) mBundle.get("title");
		ToDoItem newItem = new ToDoItem(title, dueDate);
		adapter.add(newItem);
	}



	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		getMenuInflater().inflate(R.menu.context_menu, menu); 
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		String todo = todoItemsList.get(info.position).todoStr;
		menu.setHeaderTitle(todo);
		if (todo.startsWith("Call ")){
			menu.getItem(1).setTitle(todo);
		}
		else{
			menu.removeItem(R.id.menuItemCall);
		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		ToDoItem tdi = todoItemsList.get(info.position);
		switch (item.getItemId()){
		case R.id.menuItemDelete:

			adapter.remove(tdi);
			break;
		case R.id.menuItemCall: 
			Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tdi.todoStr.substring(5))); 
			startActivity(dial);
			break;

		}


		return true;
	}

}
