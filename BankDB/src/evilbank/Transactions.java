package evilbank;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.*;

public class Transactions {
	private String date; // in format: MM-DD-YYYY
	private String acctNo = "";
	private String type = ""; // "Check", "Debit", Deposit", "Withdraw"
	private boolean processedFlag = false; //did not implement
	private double amount = 0.0;

	public Transactions() {

	}

	public Transactions(String acctNo, Double changeAmt, String type, String date) {
		this.acctNo = acctNo;
		this.amount = changeAmt;
		this.type = type;
		this.date = date;
	}

	public Transactions(String acctNo) {
		this.acctNo = acctNo;
	}

	public int compareTo(Transactions aTrans) {
		int res = 0;
		Calendar thisCal = string2Calendar(this.date);
		Calendar transCal = string2Calendar(aTrans.date);

		if (thisCal.getTimeInMillis() < transCal.getTimeInMillis()) {
			res = -1;
		}
		if (thisCal.getTimeInMillis() > transCal.getTimeInMillis()) {
			res = 1;
		}
		return res;
	}

	private Calendar string2Calendar(String aDate) {
		String[] tokens = aDate.split("-");
		// System.out.println(tokens[0] + " " + tokens[1] + " " + tokens[2]);
		Calendar cal = new GregorianCalendar((int) Integer.parseInt(tokens[2]),
				(int) Integer.parseInt(tokens[0]) - 1,
				(int) Integer.parseInt(tokens[1]));
		return cal;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getChangeAmt() {
		return amount;
	}

	public void setChangeAmt(double chargeAmt) {
		this.amount = chargeAmt;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	@Override
	public String toString() {
		return "Transaction type: " + type + "  Account:" + acctNo
				+ "  Change Amount: " + amount + "  Date: " + date;
	}

}
