package cafe.model;

public class CafeDTO {
    private String customer;
    private String drink;
    private int waitingNumber;

    public void setCustomer(String customer){
        this.customer=customer;
    }

    public String getCustomer(){
        return this.customer;
    }

    public void setDrink(String drink){
        this.drink=drink;
    }

    public String getDrink(){
        return this.drink;
    }

    public void setWaitingNumber(int waitingNumber){
        this.waitingNumber=waitingNumber;
    }

    public int getWaitingNumber(){
        return this.waitingNumber;
    }
}


