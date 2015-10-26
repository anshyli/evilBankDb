package evilbank;

import java.text.NumberFormat;
import java.util.Scanner;
import java.util.ArrayList;
import bankdb.DBService;

public class EvilBankApp {
	// protected static ArrayList<Transactions> grandTransHistory = new
	// ArrayList<Transactions>();
	// protected static ArrayList<Accounts> acctList = new
	// ArrayList<Accounts>();
	private DBService dbService;
	private String username; //for the purpose
	private String password; // of db connection

	public EvilBankApp(String userName, String password) {
		super();
		this.username = userName;
		this.password = password;
//		initializeBank();
	}
	public EvilBankApp() {
		// TODO Auto-generated constructor stub
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void initializeBank(Scanner keyboard) {
		String testuserdb = Validator.getString(keyboard,
				"Enter user name # or -1 to stop: ");
		String password = Validator.getString(keyboard,
				"Enter user password # or -1 to stop: ");
		dbService = new DBService(testuserdb, password);
		// acctList = bankDB.getAcctList();
		// grandTransHistory = bankDB.getGrandTransList();
	}
	public DBService getDbService() {
		return dbService;
	}
	public void setDbService(DBService dbService) {
		this.dbService = dbService;
	}
	public void displayTransOfAcct(Scanner keyboard) {
		boolean acctLoop = true;
		String acctNum = "";
		while (acctLoop) {
			acctNum = Validator.getString(keyboard,
				"Enter account # for deletion or -1 to stop: ");
			if (acctNum.equals("-1")) {
				acctLoop = false;
			} else {	
				if (!getDbService().checkAcctExist(acctNum)) {
					System.out.println("The account does not exist. Try again.");
					continue;
				} else {
					System.out.println("Transaction History for Account " + acctNum);
					ArrayList<Transactions> transList = new ArrayList<Transactions>();
					transList = getDbService().getTransList(acctNum);					
				    for (Transactions trans : transList) {
				    	System.out.println(trans.toString());
					}
				}
			}
		}
	}
	public void deleteAnAcct(Scanner keyboard) {
		boolean acctLoop = true;
		String acctNum = "";
		while (acctLoop) {
			acctNum = Validator.getString(keyboard,
				"Enter account # for deletion or -1 to stop: ");
			if (acctNum.equals("-1")) {
				acctLoop = false;
			} else {	
				if (!getDbService().checkAcctExist(acctNum)) {
					System.out.println("The account does not exist. Try again.");
					continue;
				} else {
					getDbService().deleteAccount(acctNum);
					System.out.println("Account " + acctNum
						+ " has been deleted.");
				}
			}
		}
	}
	public void addAnAcct(Scanner keyboard) {
		boolean acctLoop = true;
		String acctNum = "";
		while (acctLoop) {
			Accounts acct = new Accounts();
			// System.out.println("Enter account # or -1 to stop entering accounts: ");
			acctNum = Validator
					.getString(
							keyboard,
							"Want to create user account(s)?  Enter account # or -1 to stop entering accounts: ");
			// acctNum=keyboard.next();
			if (acctNum.equals("-1")) {
				acctLoop = false;
			} else {
				if (getDbService().checkAcctExist(acctNum)) {
					System.out
							.println("The account already exists. Try again.");
					continue;
				} else {
					acct.setAcctNo(acctNum);
				}
				// System.out.println("Enter the name for acct # "+acctNum+": ");
				String acctName = Validator.getString(keyboard,
						"Enter the name for acct # " + acctNum
								+ ":  or -1 to stop entering accounts: ");
				acct.setOwnerName(acctName);
				double balance = 0.0;
				balance = Validator.getDouble(keyboard,"Enter the balance for acct # "	+ acctNum + ": ");
				acct.setAcctBalance(balance);
				acct.setBirthDay("11-11-1111");
				getDbService().addAccount(acct);
			}
		}
	}
	public void checkAcctBalance(Scanner keyboard) {
		boolean acctLoop = true;
		double amount = 0.0;
		while (acctLoop) {
			String acctNum = Validator.getString(keyboard,
				"Enter account # for balance or -1 to stop: ");
		if (acctNum.equals("-1")) {
			acctLoop = false;
		} else {
			if (!getDbService().checkAcctExist(acctNum)) {
				System.out
						.println("The account does not exist. Try again.");
				continue;
			} else {
				amount = getDbService().getBalance(acctNum);
				System.out.println("Account " + acctNum + " has "
						+ formatCurrency(amount) + " currently.");
			}
		}
		}
	}
	// This kind of methods should be inside too package
	public String formatCurrency(double Dollar) {
		NumberFormat currency = NumberFormat.getCurrencyInstance();
		return currency.format(Dollar);
	}
	public void doTransaction(Scanner keyboard) {
		// looping for transactions
		String ltype = "", laccount = "", ldate = "";
		double lamount = 0.0;
		boolean acctExist = false, transLoop = true;
		Accounts theAcct = new Accounts();
		while (transLoop) {
			acctExist = false;
			ltype = Validator.getString(	keyboard,
							"Enter a transaction type(Check, Debit, Deposit, or Withdrawal) or -1 to finish: ");
			if (ltype.equals("-1"))
				break;
			laccount = Validator.getString(keyboard,
					"Enter account # or -1 to stop: ");
			// check if account exist
			if (!getDbService().checkAcctExist(laccount)) {
				System.out
						.println("The account does not exist. Try again.");
				continue;
			} else {
				lamount = Validator.getDouble(keyboard,
						"Enter the transaction amount: ");
				ldate = Validator.getString(keyboard,
						"Enter the transaction date (MM-DD-YYYY): ");
				Transactions trans = new Transactions(laccount, lamount, ltype,
						ldate);
				//save transaction to DB
				getDbService().addTransaction(trans);
				//process the transaction
				getDbService().getAcctByNO(laccount).processTrans(trans);
			}
		}	
	}
	public static void main(String[] args) {
		// start the bank
		EvilBankApp aBank = new EvilBankApp();
		Scanner keyboard = new Scanner(System.in);
		aBank.initializeBank(keyboard);
		String acctNum = "", acctName = "";
		System.out.println("Welcome to Evil Bank!\n");
		String options = "0";
		options = Validator.getString(keyboard,
						"What would you like to do? 1: Create Account, 2: Delete Account, 3: Check Account Balance, 4: Do Transaction(s), 5: Check Account Transaction History.");
		switch (options) {
		case "1": { // create account
			aBank.addAnAcct(keyboard);
		}
			break;
		case "2": {// delete account
			aBank.deleteAnAcct(keyboard)	;
		}
			break;
		case "3": {// check balance
			aBank.checkAcctBalance(keyboard);
		}
			break;
		case "4": {// perform transaction(s)
			aBank.doTransaction(keyboard);
		}
			break;
		case "5": {// check history
			aBank.displayTransOfAcct(keyboard);
		}
			break;
		default: {
			System.out.println("Come back when you know what you want to do.");
		}
			break;
		}
		aBank.dbService.disconn();
		System.out.println("\nClosing Program.....");
		keyboard.close();		}
	/*
		displayTrans(); // display the total transaction records
		// looping through each account and process its transactions in time
		// order
		for (Account anAcct : aBank.bankAssets.getAcctList()) {
			anAcct.sortTransactions(anAcct.getTransHistory());// sort into time
																// order
			for (Transaction aTrans : anAcct.getTransHistory()) {
				System.out.println("going to process: " + aTrans.toString());
				anAcct.processTrans(aTrans);
				aTrans.writeToFile(anAcct.getAcctNo());
			}
			System.out.println("The balance for account " + anAcct.getAcctNo()
					+ " is " + anAcct.getAcctBalance());
		}

		System.out.println("\nClosing Program.....");
		keyboard.close();
	*/
	}
