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
        String project = "Default";

		while (running){
			String decision = ui.userOptions(userInput, project);

            switch(decision){
                case "ls":
                    ui.taskOutput(project);
                    break;
                case "list":
                    ui.displayTodoList(userInput);
                    break;
                case "mklist":
                    ui.insertTodoList(userInput);
                    break;
                case "chlist":
                    project = ui.selectProject(userInput);
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
		for(String test : results){
			System.out.println(test);
		}
        System.out.println();
	}

    public void help(){
        System.out.println("ls     : retrieves todo list");
        System.out.println("list   : lists projects");
        System.out.println("mklist : creates new project");
        System.out.println("chlist : change project");
        System.out.println("clear  : clears the content from the screen");
        System.out.println("exit   : exits the application");
        System.out.println();
    }

    public String selectProject(Scanner userInput){
        System.out.print("Enter todo list to switch to: ");
		String project = userInput.nextLine();
        System.out.println();
        return(project);
    }

	public void clearScreen(){
		System.out.println("\033[H\033[2J");
		System.out.flush();
	}

	public String userOptions(Scanner userInput, String project){
        System.out.print(project + ": ");
		String option = userInput.nextLine();
		return(option);
	}

	public void displayTodoList(Scanner userInput){
		ui.projectOutput();
	}

	public void insertTodoList(Scanner userInput){
		System.out.print("Enter new todo list: ");
		String newProject = userInput.nextLine();
		db.putProjects(conn, newProject);
        System.out.println();
	}
	
	public boolean quitTodo(){
		ui.clearScreen();
		return(false);
	}

	public void taskOutput(String project){
        ArrayList<ArrayList<String>> todoList = new ArrayList<ArrayList<String>>(db.getTask(conn, project));
        for(ArrayList<String> column : todoList){
            for(String content : column){
                System.out.print(content + " | ");
            }
            System.out.println();
        }
        System.out.println();
	}
}
