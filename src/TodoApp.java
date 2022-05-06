import java.sql.*;
import java.util.*;

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

class Database{
	
	public Connection databaseConnection(){
		String url = "jdbc:postgresql://127.0.0.1:5432/todoapp";
		Connection dbConnection = null;

		try{
			dbConnection = DriverManager.getConnection(url);
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return(dbConnection);
	}

	public ArrayList<String> getProjects(Connection conn){
		String query = "SELECT project FROM project";
		ArrayList<String> projectList = new ArrayList<String>();

		try{
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();
			while (rs.next()){
				projectList.add(rs.getString(1));
			}
		} 
		catch(Exception e){
			e.printStackTrace();
		}

		return(projectList);
	}

	public void putProjects(Connection conn, String newProject){
		String query = "INSERT INTO project(VALUES(?, ?));";	
		int id = getProjectId(conn);

		try{
			PreparedStatement st = conn.prepareStatement(query);
			st.setInt(1, id);
			st.setString(2, newProject);	

			st.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	private int getProjectId(Connection conn) {
		String query = "SELECT MAX(project_id) FROM project";
		int maxid = -1;

		try{
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();
			while(rs.next()){
				maxid = rs.getInt(1);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}

		return(maxid + 1);
	}

	public ArrayList<ArrayList<String>> getTask(Connection conn){
		String query = "SELECT project.project, task.task, progress.progress, priority.priority "
					 + "FROM task "
					 + "INNER JOIN project ON task.project_id = project.project_id "
					 + "INNER JOIN priority ON task.priority_id = priority.priority_id "
					 + "INNER JOIN progress ON task.progress_id = progress.progress_id;";
					 // update needs to have WHERE task.project_id = project
		ArrayList<ArrayList<String>> taskList = new ArrayList<ArrayList<String>>();

		try{
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();
			int columnCount = rs.getMetaData().getColumnCount();

			while(rs.next()){
				ArrayList<String> taskColumn = new ArrayList<String>();

				for(int i = 1; i < columnCount; i++){
					taskColumn.add(rs.getString(i));
				}
				taskList.add(taskColumn);
			}
		}
		catch(SQLException e){
			e.printStackTrace();	
		}
		return(taskList);
	}
}
