import java.util.*;
import java.lang.NoClassDefFoundError;

interface Switchable {
    void on();
    void off();
    boolean isOn();

    default void toggle() {
        if (isOn())
            off();
        else
            on();
    }
}

class Fan implements Switchable {
    boolean on = false;

    public void on() {
        on = true;
        System.out.println("Fan is ON");
    }

    public void off() {
        on = false;
        System.out.println("Fan is OFF");
    }

    public boolean isOn() {
        return on;
    }
}

class Light implements Switchable {
    boolean on = false;

    public void on() {
        on = true;
        System.out.println("Light is ON");
    }

    public void off() {
        on = false;
        System.out.println("Light is OFF");
    }

    public boolean isOn() {
        return on;
    }
}

@FunctionalInterface
interface SwitchPermission {
    boolean canSwitch(Switchable device, int hour);
}

public class main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Switchable[] devices = {new Fan(), new Light()};

        System.out.println("Toggling devices:");
        for (Switchable d : devices)
            d.toggle();

        System.out.print("\nEnter hour (0-23): ");
        int hour = sc.nextInt();

        SwitchPermission check1 = new SwitchPermission() {
            public boolean canSwitch(Switchable device, int h) {
                return h >= 6 && h <= 22;
            }
        };

        SwitchPermission check2 = (device, h) -> h >= 8 && h <= 20;

        System.out.println("\nAnonymous Class Result:");
        System.out.println(check1.canSwitch(devices[0], hour)
                ? "Fan can switch ON" : "Fan cannot switch ON");

        System.out.println("\nLambda Result:");
        System.out.println(check2.canSwitch(devices[1], hour)
                ? "Light can switch ON" : "Light cannot switch ON");

        sc.close();
    }
}