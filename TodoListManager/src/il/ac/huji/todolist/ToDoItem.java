package il.ac.huji.todolist;

import java.util.Date;

public class ToDoItem implements ITodoItem {
	String todoStr;
	Date eventDate;
	public ToDoItem(String todoStr, Date eventDate) {
		this.todoStr = todoStr;
		this.eventDate = eventDate;
	}
	@Override
	public String getTitle() {
		return todoStr;
	}
	@Override
	public Date getDueDate() {
		// TODO Auto-generated method stub
		return eventDate;
	}

}
