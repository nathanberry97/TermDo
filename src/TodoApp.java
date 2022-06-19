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
                    System.out.println();
                    ui.taskOutput(project);
                    System.out.println();
                    break;
                case "mktask":
                    System.out.println();
                    ui.insertTask(userInput, project);
                    System.out.println();
                    break;
                case "ls -p":
                    System.out.println();
                    ui.displayTodoList(userInput);
                    System.out.println();
                    break;
                case "mklist":
                    System.out.println();
                    ui.insertTodoList(userInput);
                    System.out.println();
                    break;
                case "rmtask":
                    System.out.println();
                    db.resetTasks(conn);
                    break;
                case "mvtask":
                    System.out.println();
                    ui.updateTask(userInput);
                    System.out.println();
                    break;
                case "chlist":
                    System.out.println();
                    project = ui.selectProject(userInput);
                    System.out.println();
                    break;
                case "clear":
                    System.out.println();
                    ui.clearScreen();
                    System.out.println();
                    break;
                case "help":
                    System.out.println();
                    ui.help();
                    System.out.println();
                    break;
                case "exit":
                    running = ui.quitTodo();
                    break;
            }
		}
	}

	public void projectOutput(){
		ArrayList<String> results = new ArrayList<String>(db.getProjects(conn));		
		for(String projects : results){
			System.out.println(projects);
		}
	}

    public void help(){
        System.out.println("ls     : retrieves todo list tasks");
        System.out.println("mktask : creates new task");
        System.out.println("rmtask : deletes all tasks");
        System.out.println("mvtask : move task to done");
        System.out.println("ls -p  : lists projects");
        System.out.println("chlist : change project");
        System.out.println("mklist : creates new project");
        System.out.println("clear  : clears the content from the screen");
        System.out.println("exit   : exits the application");
    }

    public String selectProject(Scanner userInput){
        System.out.println("Enter todo list to switch to: ");
		String project = userInput.nextLine();
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
		System.out.println("Enter new todo list:");
		String newProject = userInput.nextLine();
		db.putProjects(conn, newProject);
	}

	public void insertTask(Scanner userInput, String project){
		System.out.println("Enter new task:");
		String newTask = userInput.nextLine();
		db.putTasks(conn, newTask, project);
	}
	
	public boolean quitTodo(){
		ui.clearScreen();
		return(false);
	}

	public void taskOutput(String project){
        ArrayList<ArrayList<String>> todoList = new ArrayList<ArrayList<String>>(db.getTask(conn, project));
        for(ArrayList<String> column : todoList){
            int index = 0;
            for(String content : column){
                System.out.print(content);
                if (index != 2){ 
                    System.out.print(" | ");
                    index += 1;
                }
            }
            System.out.println();
        }
	}

	public void updateTask(Scanner userInput){
		System.out.println("Enter task id:");
		String taskId = userInput.nextLine();
		db.completedTask(conn, taskId);
	}
}
