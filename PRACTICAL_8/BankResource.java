package exception;

public class BankResource implements AutoCloseable {

    public void use() {
        System.out.println("Using bank resource...");
    }

    @Override
    public void close() {
        System.out.println("Bank resource closed.");
    }
}