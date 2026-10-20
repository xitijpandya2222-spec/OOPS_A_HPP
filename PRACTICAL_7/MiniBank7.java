package practical.practical7;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface Id {
}

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface Positive {
    String message() default "must be > 0";
}

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface MaxLength {
    int value();
}

@FunctionalInterface
interface WithdrawRule {
    boolean allow(Account account, long amount);
}

class Account {

    @Id
    @MaxLength(10)
    private String accountNumber;

    @Positive
    private long balance;

    public Account(String accountNumber, long balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public void deposit(long amount) {
        balance += amount;
    }

    public boolean withdraw(long amount) {
        if (amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Account: " + accountNumber +
               ", Balance: " + balance;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (!(obj instanceof Account))
            return false;

        Account other = (Account) obj;

        return accountNumber.equals(other.accountNumber);
    }

    @Override
    public int hashCode() {
        return accountNumber.hashCode();
    }
}

class AnnotationValidator {

    public static String[] validate(Object obj) {

        List<String> errors = new ArrayList<>();

        Field[] fields =
                obj.getClass().getDeclaredFields();

        for (Field field : fields) {

            field.setAccessible(true);

            try {

              
                if (field.isAnnotationPresent(Positive.class)) {

                    long value = field.getLong(obj);

                    Positive annotation =
                            field.getAnnotation(Positive.class);

                    if (value <= 0) {
                        errors.add(
                            field.getName() + " "
                            + annotation.message()
                        );
                    }
                }

                if (field.isAnnotationPresent(MaxLength.class)) {

                    Object value = field.get(obj);

                    MaxLength annotation =
                            field.getAnnotation(MaxLength.class);

                    if (value != null &&
                        value.toString().length() > annotation.value()) {

                        errors.add(
                            field.getName()
                            + " length must be <= "
                            + annotation.value()
                        );
                    }
                }

            } catch (Exception e) {

                errors.add(
                    "Error checking " + field.getName()
                );
            }
        }

        return errors.toArray(new String[0]);
    }
}

public class MiniBank7 {

    public static void main(String[] args) {

        
        Account account =
                new Account("A101", -500);

        System.out.println(account);

        String[] errors =
                AnnotationValidator.validate(account);

        System.out.println("Validation Errors:");

        for (String error : errors) {
            System.out.println(error);
        }
    }
}
