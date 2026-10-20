public class Driver 
{ 
    public static void main(String[] args) 
    { 
        String[] passwords = 
        { 
            "abc", 
            "Password", 
            "Abcd1234", 
            "Abcd1234!" 
        };  
       for (String pw : passwords) 
       { 
           System.out.println("Password: " + pw); 
            if (PasswordChecker.hasLength(pw))                 
                System.out.println(" Length is at least 8");             
            else 
                System.out.println(" Length is less than 8");  
            if (PasswordChecker.hasUppercase(pw)) 
                System.out.println(" Contains uppercase letter");             
            else 
                System.out.println(" No uppercase letter");  
            if (PasswordChecker.hasDigit(pw))                 
                System.out.println("Contains digit");             
            else 
                System.out.println(" No digit");  
            if (PasswordChecker.hasSpecial(pw)) 
                System.out.println(" Contains special character");             
            else 
                System.out.println(" No special character");  
                System.out.println("Password Strength: " + PasswordChecker.strength(pw));             
                System.out.println("-----------------------------------"); 
        } 
    } 
}
