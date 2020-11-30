package at.jku.restservice;

import java.math.BigInteger;
import java.text.MessageFormat;

public class HandlebarSupplier {
    private Integer price;
    private BigInteger orderId;
    private String handlebarType;
    private String handlebarMaterial;
    private String handlebarGearshift;
    private String handleMaterial;
    private String deliveryDate;

    public HandlebarSupplier() {
        //empty constructor
    }

    public HandlebarSupplier(final BigInteger orderId, final Integer price, final String deliveryDate) {
        this.orderId = orderId;
        this.price = price;
        this.deliveryDate = deliveryDate;
    }

    public BigInteger getOrderId() {
        return orderId;
    }

    public void setOrderId(final BigInteger orderId) {
        this.orderId = orderId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(final Integer price) {
        this.price = price;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(final String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @Override
    public String toString() {
        return MessageFormat
                .format("Order Confirmation: {0} - Price {1} - DeliveryDate {2}", orderId, price, deliveryDate);
    }
}
