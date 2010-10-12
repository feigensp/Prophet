package experimentQuestionCreator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
public class Order {
    Long id;
    Date date;
    String customer;
    List<LineItem> items;
 
    public Order() {
        this("NO_CUSTOMER");
    }
 
    public Order(String customer) {
        this(customer, new Date());
    }
 
    /**
     * @param customer
     * @param date
     */
    public Order(String customer, Date date) {
        super();
        this.customer = customer;
        this.date = date;
        this.items = new ArrayList<LineItem>();
    }
 
    public Order add(LineItem item) {
        getItems().add(item);
        return this;
    }
 
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public Date getDate() {
        return date;
    }
 
    public void setDate(Date date) {
        this.date = date;
    }
 
    public String getCustomer() {
        return customer;
    }
 
    public void setCustomer(String customer) {
        this.customer = customer;
    }
 
    public List<LineItem> getItems() {
        return items;
    }
    
    
 
    public void setItems(List<LineItem> items) {
        this.items = items;
    }
 
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((customer == null) ? 0 : customer.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((items == null) ? 0 : items.hashCode());
        return result;
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        final Order other = (Order) obj;
        if (customer == null) {
            if (other.customer != null)
                return false;
        } else if (!customer.equals(other.customer))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (items == null) {
            if (other.items != null)
                return false;
        } else if (!items.equals(other.items))
            return false;
        return true;
    }
 
}