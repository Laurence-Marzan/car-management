public class Car
{
    private int id;
    private String name;
    private String make;
    private int year;

    public Car(int id, String name, String make, int year)
    {
        this.id = id;
        this.name = name;
        this.make = make;
        this.year = year;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getMake()
    {
        return make;
    }

    public void setMake(String make)
    {
        this.make = make;
    }

    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
    }
}
