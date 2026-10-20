class SeatBooking {
    int seatsLeft = 5;

    void book() {
        if (seatsLeft > 0) {
            System.out.println(Thread.currentThread().getName() + " booked a seat");
            seatsLeft--;
        } else {
            System.out.println(Thread.currentThread().getName() + " failed");
        }
    }
}

class BookingThread extends Thread {
    SeatBooking booking;

    BookingThread(SeatBooking booking) {
        this.booking = booking;
    }

    public void run() {
        booking.book();
    }
}

public class SeatBookingRace {
    public static void main(String[] args) throws Exception {
        SeatBooking booking = new SeatBooking();

        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new BookingThread(booking);
            threads[i].start();
        }

        for (int i = 0; i < 10; i++) {
            threads[i].join();
        }

        System.out.println("Seats left = " + booking.seatsLeft);
    }
}