package service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Pudding;

	public class MenuService {
		
		private static Database db = Database.getInstance();
		
		public static void save(Pudding pudding) {
			String query = "INSERT INTO pudding VALUES (?, ?, ?, ?)";
			try {
				PreparedStatement ps = db.prepareStatement(query);
				ps.setString(1, pudding.getId());
				ps.setString(2, pudding.getName());
				ps.setInt(3, pudding.getPrice());
				ps.setInt(4,  pudding.getStock());
				ps.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public static void update(Pudding pudding) {
			String query = "UPDATE pudding SET price = ?, stock = ? WHERE id = ?";
			
			try {
				PreparedStatement ps = db.prepareStatement(query);
				ps.setInt(1, pudding.getPrice());
				ps.setInt(2, pudding.getStock());
				ps.setString(3, pudding.getId());
				ps.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public static void delete(Pudding pudding) {
			String query = "DELETE FROM pudding WHERE id = ?";
			
			try {
				PreparedStatement ps = db.prepareStatement(query);
				ps.setString(1, pudding.getId());
				ps.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public static ObservableList<Pudding> getAllItems(){
			String query = "SELECT * FROM pudding";
			ObservableList<Pudding> puddingList = FXCollections.observableArrayList();
			
			try {
				PreparedStatement ps = db.prepareStatement(query);
				ResultSet rs = ps.executeQuery();
				
				while(rs.next()) {
					puddingList.add(new Pudding(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return puddingList;
		}
	}
