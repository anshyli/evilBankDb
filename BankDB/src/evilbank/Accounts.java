package evilbank;
import java.util.Date;
import java.text.NumberFormat;


public class Accounts {
	private String acctNo;
	private String ownerName;
	private double acctBalance;
	private String birthDay; // "mm-dd-yyyy"
	private String balanceDate; // "mm-dd-yyyy", very coarse, did not implement
	final static double fee = 35.0;

	public Accounts(String acctNo, String acctName, double acctBalance, String bDate) {
		this.acctNo = acctNo;
		this.ownerName = acctName;
		this.acctBalance = acctBalance;
		this.birthDay = bDate;
		System.out.println(
				"A new account " + acctNo + " created with an initial balance of: " + formatDollar(acctBalance));
	}

	public Accounts() {
	}

	public static String formatDollar(double theDollar) {
		String formattedDollar;
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		formattedDollar = nf.format(theDollar);
		return formattedDollar;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}
	public double getAcctBalance() {
		return acctBalance;
	}

	public void setAcctBalance(double acctBalance) {
			this.acctBalance = acctBalance;
	}
	@Override
	public String toString(){
		return "  Account:"+acctNo+" Account Name: "+ownerName + "Account Balance: " + acctBalance + "Owner Birthday: " + birthDay;
	}
	public void processTrans(Transactions trans){
		switch(trans.getType()){
		case "Check": {
			System.out.println("Your balance before Check: " + formatDollar(acctBalance));
			acctBalance-=trans.getChangeAmt();
			if (acctBalance<0.0){
				acctBalance-=fee;
			}
			System.out.println("Balance after Check: " + formatDollar(acctBalance));
		}break;
		case "Debit":{
			System.out.println("Your balance before Debit: " + formatDollar(acctBalance));
			acctBalance-=trans.getChangeAmt();
			if (acctBalance<0.0){
				acctBalance-=fee;
			}
			System.out.println("Balance after Debit: " + formatDollar(acctBalance));
			
		}break;
		case "Deposit":{
			System.out.println("Your balance before Deposit: " + formatDollar(acctBalance));
			acctBalance+=trans.getChangeAmt();
			System.out.println("Balance after Deposit: " + formatDollar(acctBalance));

		}break;
		case "Withdrawl":{
			System.out.println("Your balance before Withdrawl: " + formatDollar(acctBalance));
			if (trans.getChangeAmt()>acctBalance) System.out.println("Insufficient funds! Withdrawl declined.");
			else acctBalance-=trans.getChangeAmt();
			System.out.println("Balance after Withdrawl: " + formatDollar(acctBalance));
		}break;
		default: {
			System.out.println("Invalid Transaction Type. Transaction ignored.");
		}
		}
	}

}
