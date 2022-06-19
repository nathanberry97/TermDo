import java.sql.*;
import java.util.*;

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

	public ArrayList<ArrayList<String>> getTask(Connection conn, String project){
        		String query = "SELECT task.task_id, project.project, task.task, progress.progress "
					 + "FROM task "
					 + "INNER JOIN project ON task.project_id = project.project_id "
					 + "INNER JOIN progress ON task.progress_id = progress.progress_id "
                     + "WHERE project = '" + project + "' and task.progress_id = 0;";
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

    public void putTasks(Connection conn, String task, String project){
		String query = "INSERT INTO task(VALUES(?, ?, ?, 0));";	
        int taskId = getTaskId(conn);
        int projectId = getProjectNameId(conn, project);

        try{
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, taskId);
            st.setString(2, task);
            st.setInt(3, projectId);
			st.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        };
    }

	private int getTaskId(Connection conn) {
		String query = "SELECT MAX(task_id) FROM task";
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

	private int getProjectNameId(Connection conn, String name) {
		String query = "SELECT project_id FROM project WHERE project = '" + name + "';";
		int id = -1;

		try{
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();
			while(rs.next()){
				id = rs.getInt(1);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}

		return(id);
	}

    public void completedTask(Connection conn, String taskId){
        String query = "UPDATE task SET progress_id = 1 WHERE task_id = " + taskId + ";";

        try{
            PreparedStatement st = conn.prepareStatement(query);
			st.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        };
    }

    public void resetTasks(Connection conn){
        String query = "DELETE FROM task;";
        try{
            PreparedStatement st = conn.prepareStatement(query);
			st.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        };
    }
}
