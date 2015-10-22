package bankdb;

import java.util.ArrayList;
import evilbank.Transactions;
import evilbank.Accounts;

public class DBTest {

	public static void main(String[] args) {
		DBService bankDB = new DBService("testuserdb", "password");

		// list all accounts
		ArrayList<Accounts> acctList = new ArrayList<Accounts>();
		acctList = bankDB.getAcctList();
		// display the acct list
		for (Accounts anAcct : acctList) {
			System.out.println(anAcct.toString());
		}
		// get one account
		Accounts anAcct = new Accounts();
		anAcct = bankDB.getAcctByNO("4221");
		System.out.println(anAcct.toString());
		anAcct.setAcctBalance(8888.0);
		//update acct balance
		bankDB.getBalance(anAcct.getAcctNo());
		//verify the update
		anAcct = bankDB.getAcctByNO("4221");
		System.out.println(anAcct.toString());
		
		// insert one account
		anAcct.setAcctBalance(1000.0);
		anAcct.setAcctNo("1111");
		anAcct.setBirthDay("01-11-2011");
		anAcct.setOwnerName("Nelson Li");
		//verify the acct got added
		bankDB.addAccount(anAcct);
		acctList = bankDB.getAcctList();
//		for (Accounts theAcct : acctList) {
//			System.out.println(theAcct.toString());
//		}		
		//remove the account just added
		bankDB.deleteAccount("1111");
		//verify result
		acctList = bankDB.getAcctList();
//		for (Accounts theAcct : acctList) {
//			System.out.println(theAcct.toString());
//		}		
		//get total transaction list
		ArrayList<Transactions> transList = new ArrayList<Transactions>();
		transList = bankDB.getGrandTransList();
		// display the acct list
		for (Transactions atrans : transList) {
			System.out.println(atrans.toString());
		}
		//get transaction list by account
		ArrayList<Transactions> acctTransList = new ArrayList<Transactions>();
		acctTransList = bankDB.getTransList("6660");
		// display the acct list
		for (Transactions atrans : acctTransList) {
			System.out.println(atrans.toString());
		}
	    //create a transaction and add to Tranactions table
		Transactions oneTrans = new Transactions();
		oneTrans.setAcctNo("4221");
		oneTrans.setChangeAmt(800.0);
		oneTrans.setType("Check");
		oneTrans.setDate("10-20-2015");
		bankDB.addTransaction(oneTrans);
	}
}
