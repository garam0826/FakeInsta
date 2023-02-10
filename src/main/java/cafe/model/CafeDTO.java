package cafe.model;

public class CafeDTO {

    private String customer;
    private String drink;
    private int waitingNumber = -999999999;

    public void setCustomer(String custumer)
    {
        this.customer = custumer;
    }
    public String getCustomer()
    {
        return this.customer;
    }

    public void setDrink(String drik)
    {
        this.drink = drik;
    }
    public String getDrink()
    {
        return this.drink;
    }

    public void setWaitingNumber(int watingNumber)
    {
        this.waitingNumber = watingNumber;
    }
    public int getWaitingNumber()
    {
        return this.waitingNumber;
    }



}

