package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class TodoDAL {
	
	DBHelper dbhelper;
	 public TodoDAL(Context context) {
		 dbhelper = new DBHelper(context);
		 Parse.initialize(context, "0Qhqvwh4wUSGAsNGx7y99KcfLqQUfxSgVyJewvie", "FlF8P73rS8ccIDSJXdjC3ThxPBH4KTJiRZBBf53H"); 
		 ParseUser.enableAutomaticUser();
	 }
	 public boolean insert(ITodoItem todoItem) {
		 //if (update(todoItem)) return false;//TODO: remove this!!!!!!!!
		 ///Not to forget to remove this!!!!!!!!!!!!!!!
		 
		 ParseObject parse = new ParseObject("todo");
		 parse.add(TodoListManagerActivity.TITLE_COLUMN, todoItem.getTitle());
		 parse.add(TodoListManagerActivity.DATE_COLUMN, todoItem.getDueDate().getTime());
		 parse.saveInBackground();
		 
		 
		 SQLiteDatabase out = dbhelper.getWritableDatabase();
		 ContentValues todo = new ContentValues();
		 todo.put(TodoListManagerActivity.TITLE_COLUMN, todoItem.getTitle());
		 todo.put(TodoListManagerActivity.DATE_COLUMN, todoItem.getDueDate().getTime());
		 long success = out.insert(TodoListManagerActivity.TABLE_NAME, null, todo);
		 List<ITodoItem> all = all();
		 
		 for (ITodoItem td : all){
			 System.out.println(td.getTitle());
		 }
		 return success != -1;
		 
	 }
	 public boolean update(ITodoItem todoItem) {
		 ParseQuery query = new ParseQuery(TodoListManagerActivity.TABLE_NAME);
		 final long date = todoItem.getDueDate().getTime();
		 query.whereEqualTo(TodoListManagerActivity.TITLE_COLUMN, todoItem.getTitle());
		 query.findInBackground(new FindCallback() {
			 @Override
			 public void done(List<ParseObject> itemList, ParseException e) {
		         if (e != null) {
		             return;
		         }
		         itemList.get(0).put(TodoListManagerActivity.DATE_COLUMN, date);
		         itemList.get(0).saveInBackground();
		     }
		 });
		 
		 SQLiteDatabase out = dbhelper.getWritableDatabase();
		 ContentValues content = new ContentValues();
		 content.put(TodoListManagerActivity.DATE_COLUMN, date);
		 int updatedRows = out.update(TodoListManagerActivity.TABLE_NAME,content, "title = \""+todoItem.getTitle()+"\"", null);
		 return updatedRows!=0;
		 
	 }
	 public boolean delete(ITodoItem todoItem) {
		 ParseQuery query = new ParseQuery(TodoListManagerActivity.TABLE_NAME);
		 query.whereEqualTo(TodoListManagerActivity.TITLE_COLUMN, todoItem.getTitle());
		 query.findInBackground(new FindCallback() {
			 @Override
			 public void done(List<ParseObject> itemList, ParseException e) {
		         if (e != null || itemList.size() == 0) {
		             return;
		         }
		         itemList.get(0).deleteInBackground();
		     }
		 });	 
		
		 SQLiteDatabase out = dbhelper.getWritableDatabase();
		 int delRows = out.delete(TodoListManagerActivity.TABLE_NAME, "title = \""+todoItem.getTitle()+"\"", null);
		 return delRows != 0;
	 }
	 public List<ITodoItem> all() {
		 List <ITodoItem> todoList = new ArrayList<ITodoItem>();
		 SQLiteDatabase in = dbhelper.getReadableDatabase();
		 String [] querystr = {TodoListManagerActivity.ID_COLUMN,TodoListManagerActivity.TITLE_COLUMN,TodoListManagerActivity.DATE_COLUMN};		
		 Cursor cursor = in.query(TodoListManagerActivity.TABLE_NAME, querystr , null, null, null, null, null);
		 
		 if (!cursor.moveToFirst()){
			 return todoList;
		 }
		 do {
			 long   date  = cursor.getLong(cursor.getColumnIndex(TodoListManagerActivity.DATE_COLUMN));
			 String title = cursor.getString(cursor.getColumnIndex(TodoListManagerActivity.TITLE_COLUMN));
			 todoList.add(new ToDoItem(title, new Date(date)));
		 }while(cursor.moveToNext());
		 return todoList;
	 }
	 
	 public DBHelper getDBHelper(){
		 return dbhelper;
	 }
	 
}
