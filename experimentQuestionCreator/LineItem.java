package experimentQuestionCreator;

public class LineItem {
    Long id;
    String name;
    String description;
    double quantity;
    double price;
 
    public LineItem(){
        
    }
    
    /**
     * @param name
     * @param quantity
     * @param price
     */
    public LineItem(String name, double quantity, double price) {
        super();
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
 
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public String getDescription() {
        return description;
    }
 
    public void setDescription(String description) {
        this.description = description;
    }
 
    public double getQuantity() {
        return quantity;
    }
 
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
 
    public double getPrice() {
        return price;
    }
 
    public void setPrice(double price) {
        this.price = price;
    }
 
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result
                + ((name == null) ? 0 : name.hashCode());
        long temp;
        temp = Double.doubleToLongBits(price);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(quantity);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        final LineItem other = (LineItem) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (Double.doubleToLongBits(price) != Double
                .doubleToLongBits(other.price))
            return false;
        if (Double.doubleToLongBits(quantity) != Double
                .doubleToLongBits(other.quantity))
            return false;
        return true;
    }
 
}
