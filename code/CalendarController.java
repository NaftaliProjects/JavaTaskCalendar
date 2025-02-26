/*Author : Naftali Davidov
 * Ver :    24/12/2024
 * 
 * 
 * this is the GUI controller for the Calendar class
 */

import javafx.fxml.FXML;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import javafx.scene.layout.GridPane;

import java.util.Calendar;

public class CalendarController {
	final static int MAX_YEARS_COUNT = 10;
	final static int NUM_OF_MONTHS = 11;
	
	//buttons 
    @FXML
    private Button nextMonthButton; // move one month forwards

    @FXML
    private Button prevMonthButton; // move one month backwards

    @FXML
    private Button textAreaButton;  // saves from text area to tasks object
        
    @FXML
    private TextArea textArea;

    
    //Labels 
    @FXML
    private Label yearLabel; //shows current year

    @FXML
    private Label monthLabel; //shows current month
    
    
    //checkBoxes 
    @FXML
    private ChoiceBox<String> monthSelect; // months selection
    
    @FXML
    private ChoiceBox<Integer> yearSelect;// years selection
    
    
    //containers 
    @FXML
    private GridPane grid;
    private int gridRows;
    private int gridColumns;
    
    //controllers 
    private Button[] buttons;
    private final double DAY_BUTTON_SIZE = 100;
    
    //attributes
    private Calendar currentDate; 
    private Calendar prevMonth;
    private Calendar firstDayOfMonthDate;
    
    private String[] monthsName = {"JAN","FEB","MER","APR","MAY","JUN","JUL","OG","SEP","OKT","NOV","DEC"};
    
    private Task tasks; //hold a date as a key, and a taskContent as a value.
    private Calendar newDateTask;
    
    
    @FXML
    void initialize() {
    	this.textArea.setVisible(false);
    	this.textAreaButton.setVisible(false);

    	this.tasks = new Task();
        
        //draw buttons
    	this.gridRows = grid.getColumnConstraints().size() - 1;
    	this.gridColumns = grid.getColumnConstraints().size();
        initializeButtonsGrid();
        
        //get instance of current date
        this.currentDate =  Calendar.getInstance();
        
        //get an instance of one month prior to this month
        this.prevMonth = Calendar.getInstance();
        this.prevMonth.set(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH)-1, 1);
        
        //get instance of first day of current month
        this.firstDayOfMonthDate = Calendar.getInstance();
        this. firstDayOfMonthDate.set(Calendar.DAY_OF_MONTH, 1);
        
        
        //create a list of years for year_choiceBOx
        int currentYear = currentDate.get(Calendar.YEAR);
        List<Integer> years = IntStream.rangeClosed(currentYear - MAX_YEARS_COUNT, currentYear + MAX_YEARS_COUNT)
                                      .boxed()
                                      .collect(Collectors.toList());
        
        //set choice boxes 
        this.yearSelect.getItems().addAll( (years) );     	
        this.yearSelect.setOnAction(this::yearSelected);
    	
        this.monthSelect.getItems().addAll(monthsName); 
        this.monthSelect.setOnAction(this::monthSelected);
    	
        drawDays(); //draw the table of days
        drawYearAndMonth(); //draw the labels of year and month
    }
    
    
    //get the month from the choice box and set it as the current month
    private void monthSelected(ActionEvent e) {
    	setTaskAreaVisible(true);
    	
    	//get the index of the month selected from the monthSelect choice box
    	int monthIndex = monthSelect.getItems().indexOf(monthSelect.getValue());
    	
    	this.currentDate.set(Calendar.MONTH, monthIndex);
    	this.prevMonth.set(Calendar.MONTH, (monthIndex-1) % NUM_OF_MONTHS );
    	this.firstDayOfMonthDate.set(Calendar.MONTH, monthIndex);
    	
    	drawDays();
    	drawYearAndMonth();
    }
    
    
    //get the year from the choice box and set it as the current year
    private void yearSelected(ActionEvent e) {  	
    	setTaskAreaVisible(false);
    	
    	this.currentDate.set(Calendar.YEAR, yearSelect.getValue());
    	this.prevMonth.set(Calendar.YEAR, yearSelect.getValue());
    	this.firstDayOfMonthDate.set(Calendar.YEAR, yearSelect.getValue());
    	
    	drawDays();
    	drawYearAndMonth();
    }
    
    
	//draws days buttons 
	private void initializeButtonsGrid() {
		this.buttons = new Button[gridRows * gridColumns];
	    
		for (int i = gridColumns; i < (gridRows * gridColumns); i++) {
			this.buttons[i] = new Button(String.valueOf(' '));
			this.buttons[i].setPrefSize(DAY_BUTTON_SIZE, DAY_BUTTON_SIZE);
			
			this.buttons[i].setOnAction(new EventHandler<ActionEvent>() {
    		    @Override public void handle(ActionEvent e) {
    		    	handleDateButton(e);
    		    }
    		});
			
			this.grid.add(buttons[i], i % gridColumns, i / gridColumns);
		}
	}
	
	
	//draw the days table
	private void drawDays() {
		//get days count for both current and prev month
		int currentDateDaysCount = currentDate.getActualMaximum(Calendar.DAY_OF_MONTH);
		int prevDateDaysCount = prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		//get the number of day of week for the first day of the month
		int dayOfWeek = firstDayOfMonthDate.get(Calendar.DAY_OF_WEEK) - 1;
             
		//draw the first row of the table:
		for (int i = 0 ; i < dayOfWeek ; i++) {
			this.buttons[i+gridColumns].setText(String.valueOf((prevDateDaysCount-dayOfWeek+1)+i));
		}
		
		//draw the rest of the table 
		for (int i = dayOfWeek ; i < (gridColumns*(gridRows-1)); i++) {
			this.buttons[i+gridColumns].setText(String.valueOf( (i  - dayOfWeek) % currentDateDaysCount + 1));
		}
		
	}
	
	
	//draw the year and month labels 
	private void drawYearAndMonth() {
		int month = this.currentDate.get(Calendar.MONTH);
		int year = this.currentDate.get(Calendar.YEAR);
		
		this.monthLabel.setText(this.monthsName[month]);
		this.yearLabel.setText(String.valueOf(year));
	}
	
	
	
	@FXML
	private void textAreaButtonPressed(ActionEvent event) {
		setTaskAreaVisible(false);    	
		this.tasks.addTask(this.newDateTask,this.textArea.getText());
    	this.textArea.clear();
    }
    
	
	
	
    @FXML
    private void nextMonthButtonPressed(ActionEvent event) {	
    	this.currentDate.add(Calendar.MONTH, 1);
    	this.prevMonth.add(Calendar.MONTH, 1);
    	this.firstDayOfMonthDate.add(Calendar.MONTH, 1);
    	
    	drawDays();
    	drawYearAndMonth();
    }

    
    @FXML
    private void prevMonthButtonPressed(ActionEvent event) {
    	this.currentDate.add(Calendar.MONTH, -1);
    	this.prevMonth.add(Calendar.MONTH, -1);
    	this.firstDayOfMonthDate.add(Calendar.MONTH, -1);
    	
    	drawDays();
    	drawYearAndMonth();
    }
    
    
    //method to handle date button press
    private void handleDateButton(ActionEvent e) {
    	setTaskAreaVisible(true);
    	
    	//get info from button (day , grid_index)
    	Button btn = (Button)e.getSource();
    	int dayNum = Integer.valueOf(btn.getText());
    	int btnRowNum = GridPane.getRowIndex(btn);
    	
    	int year;
		int month;
		
		//create a new date and set it to the selected date
		this.newDateTask = Calendar.getInstance();
    	if(btnRowNum == 1 && (dayNum > 7)) { 
    		//reference prevMonth
    		year = this.prevMonth.get(Calendar.YEAR);
    		month = this.prevMonth.get(Calendar.MONTH);
    		
    		this.newDateTask.set(year, month, dayNum);
    	}
    	else {
    		//reference current Month
    		year = this.currentDate.get(Calendar.YEAR);
    		month = this.currentDate.get(Calendar.MONTH);
    		
    		this.newDateTask.set(year, month, dayNum);
    	}
    	
    	//if date contains a task, print it to the text are
    	if(tasks.isTaskContain(this.newDateTask)) {    	
    		this.textArea.setText(tasks.getTask(this.newDateTask));
    	}
    	else {
    		this.textArea.clear();
    	}
      
    }
    
    private void setTaskAreaVisible(Boolean bool) {
    	this.textArea.setVisible(bool);
    	this.textAreaButton.setVisible(bool);
    }

}
