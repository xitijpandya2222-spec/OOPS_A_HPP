abstract class Employee 
{ 
    String name; 
    int id; 
 
    Employee(String name, int id) 
    {         
        this.name = name; 
        this.id = id; 
    } 
 
    abstract double monthlySalary(); 
 
    public static void main(String[] args) 
    { 
 
        Employee[] employees = { 
    new FullTime("Yashvi", 101, 50000), 
    new PartTime("Rahul", 102, 80, 300), 
    new Intern("Amit", 103, 15000), 
    new FullTime("Neha", 104, 60000) 
        }; 
 
        double total = 0; 
 
        for (Employee e : employees) { 
 
            double salary = e.monthlySalary(); 
 
            System.out.println(e.name + " (" + e.id + ") = ₹" + salary ); 
 
            total += salary; 
 
            if (e instanceof Intern) 
                { 
                System.out.println("  Note: This employee is an intern."); 
                } 
        } 
 
        System.out.println("--------------------"); 
        System.out.println("Total Payroll = ₹" + total); 
    } 
}
 
class FullTime extends Employee { 
 
    double salary; 
 
    FullTime(String name, int id, double salary) 
    {    
        super(name, id);        
        this.salary = salary; 
    } 
 
    double monthlySalary() 
    {         
        return salary; 
    } 
} 
 
class PartTime extends Employee 
{ 
 
    double hours;     
    double rate; 
 
    PartTime(String name, int id, double hours, double rate) 
    {         
        super(name, id);      
        this.hours = hours;      
        this.rate = rate; 
    } 
 
    double monthlySalary() 
    { 
        return hours * rate; 
    }
}
 
class Intern extends Employee 
{ 
 
    double stipend; 
 
    Intern(String name, int id, double stipend) 
    {        
        super(name, id);        
        this.stipend = stipend; 
    } 
    double monthlySalary() 
    {         
        return stipend; 
    } 
} 
