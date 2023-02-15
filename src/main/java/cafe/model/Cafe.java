package cafe.model;

public class Cafe {

    private String customer;
    private String drink;
    private Integer waitingNumber;

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
    public Integer getWaitingNumber()
    {
        return this.waitingNumber;
    }



}

