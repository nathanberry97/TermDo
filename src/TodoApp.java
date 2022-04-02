import java.sql.*;
import java.util.*;

class TodoApp{
	private static Database db = new Database();
	private static Connection conn = db.databaseConnection(); 
	private static TodoApp ui = new TodoApp();

    public static void main(String args[]){
		boolean running = true;
		Scanner userInput = new Scanner(System.in);
		
		while (running){
			ui.taskOutput();
			String decision = ui.userOptions(userInput);

			if(decision.equals("y")){
				ui.displayTodoList(userInput);
			}
			else{ 
				if (decision.equals("i")){
					ui.insertTodoList(userInput);
				}
				else{
					if(decision.equals("n")){
						running = ui.quitTodo();
					}
				}
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
		ui.clearScreen();
		System.out.println("Display todo lists? (y/n)");
		System.out.println("Insert new todo list (i)");
		String option = userInput.nextLine();
		return(option);
	}

	public void displayTodoList(Scanner userInput){
		ui.clearScreen();
		ui.projectOutput();
		userInput.nextLine();
	}

	public void insertTodoList(Scanner userInput){
		ui.clearScreen();
		System.out.println("Please enter todo list name:");
		String newProject = userInput.nextLine();
		db.putProjects(conn, newProject);
	}
	
	public boolean quitTodo(){
		ui.clearScreen();
		return(false);
	}

	public void taskOutput(){
		System.out.println(db.getTask(conn));
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
