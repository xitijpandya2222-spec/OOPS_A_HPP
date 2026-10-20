interface Notifier {
    void send(String message);
}

interface Urgent {
}

public class NotificationSender {

    public static void main(String[] args) {

        
        Notifier email = (message) ->
                System.out.println("Email: " + message);

        
        Notifier sms = (message) ->
                System.out.println("SMS: " + message);

        
        Notifier[] senders = {email, sms};

        String message = "Exam starts tomorrow!";

        
        for (Notifier sender : senders) {
            sender.send(message);
        }
    }
}
