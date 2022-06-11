import java.util.*;
import java.sql.*;

class TodoApp{
	private static Database db = new Database();
	private static Connection conn = db.databaseConnection(); 
	private static TodoApp ui = new TodoApp();

    public static void main(String args[]){
		boolean running = true;
        ui.clearScreen();
		Scanner userInput = new Scanner(System.in);
		
		while (running){
			String decision = ui.userOptions(userInput);

            switch(decision){
                case "todo":
                    ui.taskOutput();
                    break;
                case "projects":
                    ui.displayTodoList(userInput);
                    break;
                case "new project":
                    ui.insertTodoList(userInput);
                    break;
                case "clear":
                    ui.clearScreen();
                    break;
                case "help":
                    ui.help();
                    break;
                case "exit":
                    running = ui.quitTodo();
                    break;
            }
		}
	}

	public void projectOutput(){
		ArrayList<String> results = new ArrayList<String>(db.getProjects(conn));		
		System.out.println("TODO LISTS");
		System.out.println();
		for(String test : results){
			System.out.println(test);
		}
	}

    public void help(){
        System.out.println("todo         : retrieves todo list");
        System.out.println("projects     : lists projects");
        System.out.println("new projects : creates new project");
        System.out.println("clear        : clears the content from the screen");
        System.out.println("exit         : exits the application");
        System.out.println();
    }

	public void clearScreen(){
		System.out.println("\033[H\033[2J");
		System.out.flush();
	}

	public String userOptions(Scanner userInput){
        System.out.print("todo$ ");
		String option = userInput.nextLine();
		return(option);
	}

	public void displayTodoList(Scanner userInput){
        System.out.println();
		ui.projectOutput();
        System.out.println();
	}

	public void insertTodoList(Scanner userInput){
		System.out.println("Please enter todo list name:");
		String newProject = userInput.nextLine();
		db.putProjects(conn, newProject);
	}
	
	public boolean quitTodo(){
		ui.clearScreen();
		return(false);
	}

	public void taskOutput(){
        // TODO need to format the todo list to have a better output on the display
        ArrayList<ArrayList<String>> todoList = new ArrayList<ArrayList<String>>(db.getTask(conn));
        System.out.println();
        for(ArrayList<String> column : todoList){
            for(String content : column){
                System.out.print(content + " | ");
            }
            System.out.println();
            System.out.println();
        }
	}
}
