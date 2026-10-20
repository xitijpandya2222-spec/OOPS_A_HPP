class FileResource implements AutoCloseable {

    boolean closed = false;

    FileResource() {
        System.out.println("Resource opened");
    }

    void read() throws Exception {
        System.out.println("Reading resource...");
        throw new Exception("Error while reading file");
    }

    public void close() {
        closed = true;
        System.out.println("Resource closed");
    }
}

public class Main {
    public static void main(String[] args) {

        try (FileResource r = new FileResource()) {
            r.read();
        }
        catch (Exception e) {
            System.out.println("Original error: " + e.getMessage());
        }

        System.out.println("Program continues...");
    }
}