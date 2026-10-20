abstract class Account { 
 
    protected String accountNumber;     
    protected String ownerName;     
    protected long balance; 
 
    
    public Account(String accountNumber, String ownerName, long balance) 
    {         
        this.accountNumber = accountNumber; 
        this.ownerName = ownerName; 
 	    this.balance = balance; 
    } 
 
    public abstract double interestRate(); 
 
    public abstract boolean canWithdraw(long amount); 
 
    public String getAccountNumber() {         
        return accountNumber; 
    } 
 
    public String getOwnerName() {         
        return ownerName; 
    } 
 
    public long getBalance() {         
        return balance; 
    } 
 
    @Override     
    public String toString() 
    {         
        return "Account Number: " + accountNumber + ", Owner: " + ownerName + ", Balance: " + balance; 
    } 
} 
 
class SavingsAccount extends Account 
{ 
 
    private long minBalance; 
 
    public SavingsAccount(String accountNumber, String ownerName, long balance, long minBalance)
    { 
     super(accountNumber, ownerName, balance);        
      this.minBalance = minBalance; 
    } 
 
    @Override     
    public double interestRate() 
    {         
        return 4.0; 
    } 
 
    @Override     
    public boolean canWithdraw(long amount) 
    {         
        return balance - amount >= minBalance; 
    } 
 
    public long getMinBalance() 
    {        
         return minBalance; 
    } 
}
 
class CurrentAccount extends Account { 
 
    private long overdraftLimit; 
    
    public CurrentAccount(String accountNumber, String ownerName, long balance,long overdraftLimit) 
    { 
 
        super(accountNumber, ownerName, balance);         
        this.overdraftLimit = overdraftLimit; 
    } 
 
    @Override     
    public double interestRate() 
    {        
         return 0.0; 
    } 
 
    @Override    
     public boolean canWithdraw(long amount) 
     {         
        return balance - amount >= -overdraftLimit; 
    } 
 
    public long getOverdraftLimit() 
    {         
        return overdraftLimit; 
    }   
}
 
class FixedDepositAccount extends Account { 
 
    
    public FixedDepositAccount(String accountNumber, String ownerName, long balance) 
    { 
 
        super(accountNumber, ownerName, balance); 
    } 
 
    @Override     
    public double interestRate()
     {         
        return 7.0; 
    } 
 
    @Override    
     public boolean canWithdraw(long amount) 
     { 
        return false; 
    } 
} 
 
public class MiniBank3 { 
   public static void main(String[] args)
    {SavingsAccount savings = new SavingsAccount( "SA1001", "Yashvi", 50000,10000 ); 
 
        CurrentAccount current = new CurrentAccount("CA1001","Rahul", 20000, 15000 ); 
 
        FixedDepositAccount fd = new FixedDepositAccount("FD1001","Priya",100000 ); 
        Account[] accounts = {savings,current,fd}; 
       System.out.println("----- ACCOUNT DETAILS -----"); 
 
        for (Account account : accounts)
             { 
 
            System.out.println(account); 
            System.out.println( "Interest Rate: " + account.interestRate() + "%");  
            System.out.println(); 
        }  
        System.out.println("----- WITHDRAWAL TEST -----");  
        System.out.println( "Savings withdraw 45000: " + savings.canWithdraw(45000)); 
        System.out.println( "Current withdraw 30000: "  + current.canWithdraw(30000)); 
        System.out.println( "Fixed Deposit withdraw 50000: " + fd.canWithdraw(50000)); 
 
        System.out.println("\n----- INSTANCEOF TEST -----"); 
 
        for (Account account : accounts) 
            { 
 
            if (account instanceof SavingsAccount sa) 
                { 
 
                System.out.println( "Savings Account found."); 
 
                System.out.println( "Minimum Balance: " + sa.getMinBalance() ); 
            } 
        } 
    } 
}
