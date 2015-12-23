package modele;

public class Employee extends Person {
	private String aNumber;

    public String getaNumber() {
        return aNumber;
    }

    public void setaNumber(String aNumber) {
        this.aNumber = aNumber;
    }

    public Employee(String aNumber) {
        this.aNumber = aNumber;
    }
    
}