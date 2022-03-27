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
			ui.clearScreen();
			System.out.println("Display your different todo lists? (y/n)");
			String decision = userInput.nextLine();

			if(decision.equals("y")){
				ui.clearScreen();
				ui.projectOutput();
				userInput.nextLine();
			}
			else{
				if(decision.equals("n")){
					running = false;
					ui.clearScreen();
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

}
