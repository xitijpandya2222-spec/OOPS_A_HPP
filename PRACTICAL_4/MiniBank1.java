import java.util.regex.Pattern;

class Validator {
 
    private static final Pattern MOBILE = Pattern.compile("^[6-9][0-9]{9}$"); 
 
    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"); 
 
    private static final Pattern PAN = Pattern.compile("^[A-Z]{5}[0-9]{4}[A-Z]$"); 
 
    private static final Pattern IFSC = Pattern.compile("^[A-Z]{4}0[A-Z0-9]{6}$"); 
 
    public static boolean isValidMobile(String mobile) { 
       return MOBILE.matcher(mobile).matches(); 
   } 
    public static boolean isValidEmail(String email) {         
        return EMAIL.matcher(email).matches(); 
    } 
 
    public static boolean isValidPan(String pan) {         
        return PAN.matcher(pan).matches(); 
    } 
 
    public static boolean isValidIfsc(String ifsc) {         
        return IFSC.matcher(ifsc).matches(); 
    } 
} 
 
enum TransactionType { 
    DEPOSIT, 
    WITHDRAW, 
    TRANSFER 
} 
 
record Command( 
        TransactionType type,         
        String accountNumber,         
        long amount 
) { 
} 
class CommandParser { 
 
    public static Command parse(String line) { 
 
        String[] parts = line.split(" "); 
 
        TransactionType type = TransactionType.valueOf(parts[0]); 
 
        String accountNumber = parts[1]; 
 
        long amount = Long.parseLong(parts[2]); 
 
        return new Command(type, accountNumber, amount); 
    } 
} 
 
class Account { 
 
    private String accountNumber;     
    private long balance; 
 
    public Account(String accountNumber, long balance) 
    {         
        this.accountNumber = accountNumber;         
        this.balance = balance; 
    } 
 
   public String getAccountNumber() {        
        return accountNumber; 
   } 
 
    public long getBalance() {         
        return balance; 
    } 
} 
 
 
class StatementFormatter { 
 
    public static String buildStatement(Account account) { 
 
        StringBuilder sb = new StringBuilder(); 
 
        sb.append("Account Statement\n");         
        sb.append("-----------------\n");         
        sb.append("Account Number: ");         
        sb.append(account.getAccountNumber());         
        sb.append("\n"); 
        sb.append("Balance: ");         
        sb.append(account.getBalance());         
        sb.append("\n"); 
 
        return sb.toString(); 
    } 
}
public class MiniBank1 { 
 
    public static void main(String[] args) { 
 
        // Validator testing 
        System.out.println("===== VALIDATOR TESTING ====="); 
 
        System.out.println("\nMobile:"); 

        System.out.println("9876543210 -> " + Validator.isValidMobile("9876543210")); 
 
        System.out.println("12345 -> " + Validator.isValidMobile("12345")); 
 
        System.out.println("\nEmail:");

        System.out.println("abc@gmail.com -> " + Validator.isValidEmail("abc@gmail.com")); 
 
        System.out.println("abc@ -> " + Validator.isValidEmail("abc@")); 
 
        System.out.println("\nPAN:"); 

        System.out.println("ABCDE1234F -> " + Validator.isValidPan("ABCDE1234F")); 
 
        System.out.println("ABC123 -> " + Validator.isValidPan("ABC123"));      

        System.out.println("\nIFSC:");

        System.out.println("SBIN0001234 -> " + Validator.isValidIfsc("SBIN0001234")); 
 
        System.out.println("SBI123 -> " + Validator.isValidIfsc("SBI123")); 
 
        System.out.println("\n===== COMMAND PARSING ====="); 
 
        Command command = CommandParser.parse("DEPOSIT AC0001 500"); 
 
        System.out.println("Type: " + command.type()); 
 
        System.out.println("Account Number: " + command.accountNumber()); 
 
        System.out.println("Amount: " + command.amount()); 
 
        System.out.println("\n===== ACCOUNT STATEMENT ====="); 
 
        Account account = new Account("AC0001", 5000);
                System.out.println( 
                StatementFormatter.buildStatement(account) 
        ); 
    } 
} 
