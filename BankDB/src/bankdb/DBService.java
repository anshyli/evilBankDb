package bankdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.sql.Date;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import evilbank.Accounts;
import evilbank.Transactions;

public class DBService {	
	private String userName;
	private String password;
	private static Connection conn;
	
	private static final String ACCOUNT_TABLE = "Accounts";
	private static final String ID = "ID";
	private static final String ACCOUNT_NUMBER = "ACCTNO";
	private static final String OWNER_NAME = "OWNERNAME";
	private static final String ACCOUNT_BALANCE = "ACCTBALANCE";
	private static final String OWNER_BIRTHDAY = "OWNERBDAY";

	private static final String TRANSACTION_TABLE = "Transactions";
	private static final String TRANSACTION_TYPE_ID = "T_TYPE_ID";
	private static final String TRANSACTION_TYPE = "T_TYPE";
	private static final String TRANSACTION_DATE = "T_DATE";
	private static final String AMOUNT = "AMOUNT";
	
	private static final String TRANSTYPE_TABLE = "transtype";
	private static final String TRANSTYPE_ID = "id";
	
	public DBService() {
	}

	public DBService(String userName, String password) {
		this.userName = userName;
		this.password = password;
		try {
			this.conn = getConnected(userName, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		return conn;
	}

	public void setConnection(Connection conn) {
		this.conn = conn;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public  Connection getConnected(String name, String password) throws SQLException {
		// URL of Oracle database server
		Connection conn;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = "jdbc:oracle:thin:testuser/password@localhost";
		// properties for creating connection to Oracle database
		Properties props = new Properties();
		props.setProperty("user", name);
		props.setProperty("password", password);

		// creating connection to Oracle database using JDBC
		conn = DriverManager.getConnection(url, props);
		return conn;
	}
	public ResultSet runSelectSql(String sql) {
		ResultSet result = null;
		try {
			PreparedStatement preStatement = getConnection().prepareStatement(sql);
			result = preStatement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public void runCUDSql(String sql) {
		boolean result = false;;
		try {
			Statement statement = getConnection().createStatement();
			result = statement.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!result) System.out.println("The SQL: " + sql + "Failed!");
	}

	public ArrayList<Accounts> getAcctList() {
		ArrayList<Accounts> accounts = new ArrayList<Accounts>();
		String sql = "select * from " + ACCOUNT_TABLE;
		 SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		// creating PreparedStatement object to execute query
		ResultSet result = runSelectSql(sql);
		try {
			while (result.next()) {
				Accounts account = new Accounts();
				account.setAcctNo(result.getString(ACCOUNT_NUMBER));
				account.setOwnerName(result.getString(OWNER_NAME));
				account.setAcctBalance(result.getDouble(ACCOUNT_BALANCE));
//				java.util.Date utilDate = sdf.parse(result.getString("owner_birthday"));
//				java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
//				account.setBirthDay(utilDate);
				account.setBirthDay(sdf.format(result.getDate(OWNER_BIRTHDAY)));
				accounts.add(account);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accounts;
	}
	public boolean checkAcctExist(String accountNumber) {
		String sql = "select count(" + ACCOUNT_NUMBER + ") from "+ ACCOUNT_TABLE + " WHERE " + ACCOUNT_NUMBER + " =  '" + accountNumber + "'";
		System.out.println(sql);
		ResultSet result = runSelectSql(sql);
		boolean exist = false;
		int acctCount = 0;
		try {
			while (result.next()) {
				acctCount = result.getInt("COUNT(ACCTNO)");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		if (acctCount > 0) exist = true;

		return exist;
	}
	public Accounts getAcctByNO(String accountNumber) {
		Accounts account = new Accounts();
		String sql = "SELECT * FROM " + ACCOUNT_TABLE + " WHERE " + ACCOUNT_NUMBER + " =  '" + accountNumber + "'";
		// String sql = "SELECT * FROM " + ACCOUNT_TABLE;
		System.out.println(sql);
		ResultSet result = runSelectSql(sql);
		 SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		try {
			while (result.next()) {
				account.setAcctNo(result.getString(ACCOUNT_NUMBER));
				account.setOwnerName(result.getString(OWNER_NAME));
				account.setAcctBalance(result.getDouble(ACCOUNT_BALANCE));
				account.setBirthDay(sdf.format(result.getDate(OWNER_BIRTHDAY)));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return account;
	}

		public void addAccount(Accounts account) {
			String insertsql = "INSERT INTO " + ACCOUNT_TABLE + "( " + " " + ACCOUNT_NUMBER + ", " + " " + OWNER_NAME
					+ ", " + " " + ACCOUNT_BALANCE + ", " + " " + OWNER_BIRTHDAY + " ) VALUES  " + "(?,?,?,?)";
			System.out.println(insertsql);
			 SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
			try {
				PreparedStatement prepareStatement = getConnection().prepareStatement(insertsql);
				prepareStatement.setString(1, account.getAcctNo());
				prepareStatement.setString(2, account.getOwnerName());
				prepareStatement.setDouble(3, account.getAcctBalance());
				java.util.Date utilDate = sdf.parse(account.getBirthDay());
				java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
				prepareStatement.setDate(4, sqlDate);

				prepareStatement.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void deleteAccount(String account_number) {		
			String sql = "DELETE FROM " + ACCOUNT_TABLE + " WHERE " + ACCOUNT_NUMBER + " =  '" + account_number + "'";
			runCUDSql(sql);
		}

		public double getBalance(String acctNo) {
			String sql = "select " + ACCOUNT_BALANCE + " from " + ACCOUNT_TABLE 
					+ " WHERE " + ACCOUNT_NUMBER + "= '" + acctNo + "'";
			System.out.println(sql);
			ResultSet result = runSelectSql(sql);
			double amount = 0;
			try {
				while (result.next()) {
					amount = result.getDouble(ACCOUNT_BALANCE);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return amount;
		}
		public ArrayList<Transactions> getGrandTransList() {
			ArrayList<Transactions> transList = new ArrayList<Transactions>();
			String sql = "select t.acctno, t.amount, p.t_type, t.t_date  from " + TRANSACTION_TABLE + " t inner join " + TRANSTYPE_TABLE
					+ " p on t.t_type_id = p.id";
			 SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
			ResultSet result = runSelectSql(sql);
			try {
				while (result.next()) {
					Transactions trans = new Transactions();
					trans.setAcctNo(result.getString(ACCOUNT_NUMBER));
					trans.setChangeAmt(result.getDouble(AMOUNT));
					trans.setType(result.getString(TRANSACTION_TYPE));
					trans.setDate(sdf.format(result.getDate(TRANSACTION_DATE)));

					transList.add(trans);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return transList;
		}
	public ArrayList<Transactions> getTransList(String acctno) {
		ArrayList<Transactions> transList = new ArrayList<Transactions>();
		String sql = "select t.acctno, t.amount, p.t_type, t.t_date  from " + TRANSACTION_TABLE + " t inner join " + TRANSTYPE_TABLE
					+ " p on t.t_type_id = p.id" + " WHERE " + ACCOUNT_NUMBER + " = '" + acctno + "'";
		 SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		ResultSet result = runSelectSql(sql);
		try {
			while (result.next()) {
				Transactions trans = new Transactions();
				trans.setAcctNo(result.getString(ACCOUNT_NUMBER));
				trans.setChangeAmt(result.getDouble(AMOUNT));
				trans.setType(result.getString(TRANSACTION_TYPE));
				trans.setDate(sdf.format(result.getDate(TRANSACTION_DATE)));

				transList.add(trans);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transList;
	}
	public  int getTranstypeId(String transType) {
		String thesql = "select  " + TRANSTYPE_ID + " from " + TRANSTYPE_TABLE + " where t_type = '" + transType + "'";
		ResultSet  result = runSelectSql(thesql);
		int transId = 0; 
		try {
			while(result.next()) {
				transId = result.getInt(TRANSTYPE_ID);
			}
		} catch (Exception e) {
			
		}
		return transId;
	}
	public void addTransaction(Transactions transaction) {
		// insert transaction
		String insertTransaction = "INSERT INTO " + TRANSACTION_TABLE + "( " + " " + ACCOUNT_NUMBER + ", " + " "
				+ AMOUNT + ", " + " " + TRANSACTION_TYPE_ID + ", " + " " + TRANSACTION_DATE + " ) values "
				+ "(?,?,?,?)";
		System.out.println(insertTransaction);
		int transId = getTranstypeId(transaction.getType());
//		insert into Transactions (ACCTNO, amount, t_type_id, t_date)
//		select '4221', 800.0, (select id from transtype where t_type = 'Check'),
//		  to_date('10-20-2015', 'mm-dd-yyyy') from dual;
		try {
			 SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
//			    Date convertedCurrentDate = sdf.parse("2013-09-18");
//			    String date=sdf.format(convertedCurrentDate );
			java.util.Date utilDate = null;
			try {
				utilDate = sdf.parse(transaction.getDate());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

			PreparedStatement prepareStatement = conn.prepareStatement(insertTransaction);

			prepareStatement.setString(1, transaction.getAcctNo());
			prepareStatement.setDouble(2, transaction.getChangeAmt());
			prepareStatement.setInt(3, transId);
			prepareStatement.setDate(4, sqlDate);

			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}