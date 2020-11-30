package at.jku.restservice;

public class SupplierAnswer {
    int price;
    String deliverydate;

    public SupplierAnswer(int price, String deliverydate) {
        this.price = price;
        this.deliverydate = deliverydate;
    }
    public SupplierAnswer() {

    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDeliverydate() {
        return deliverydate;
    }

    public void setDeliverydate(String deliverydate) {
        this.deliverydate = deliverydate;
    }
}
