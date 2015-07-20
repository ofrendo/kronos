package kronos.db;

public class LogDB {

	public static void main(String[] args) {
		try {
			DB.getDB().logDB();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
