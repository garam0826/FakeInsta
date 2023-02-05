package cafe.model;

public class CafeDTO {

    private String customer;
    private String drink;
    private int watingNumber;

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

    public void setWatingNumber(int watingNumber)
    {
        this.watingNumber = watingNumber;
    }
    public int getWatingNumber()
    {
        return this.watingNumber;
    }



}

