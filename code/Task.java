/*Author : Naftali Davidov
 * Ver :    24/12/2024
 * 
 * Task class hold tasks for each date 
 */

import java.util.Calendar;
import java.util.HashMap;

public class Task {
	//attributes
	private HashMap<String,String> tasks;
	
	//constructor
	public Task(){
		this.tasks = new HashMap<>();
	}
	
	
	//adds a task to a given date
	public void addTask(Calendar date, String taskStr) {	
    	this.tasks.put(serilzeDate(date), taskStr);
    }
	
	
	//check if a given date contains a task
    public boolean isTaskContain(Calendar date) {
    	if(this.tasks.containsKey(serilzeDate(date))) {
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
     
    //convert a date into a string so it could be comparable easliy 
    private String serilzeDate(Calendar date) {
    	String dateStr = String.valueOf(date.get(Calendar.YEAR))          +
			         	 String.valueOf(date.get(Calendar.MONTH))         + 
				         String.valueOf(date.get(Calendar.DAY_OF_MONTH));
    	
    	return dateStr;
    }
    
    
    
    String getTask(Calendar date) {
    	return this.tasks.get(serilzeDate(date));
    }
}
