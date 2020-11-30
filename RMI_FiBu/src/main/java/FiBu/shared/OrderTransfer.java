package FiBu.shared;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.MessageFormat;

public class OrderTransfer implements Serializable {
    private final BigInteger orderId;
    private final String handlebarType;
    private final String handlebarMaterial;
    private final String handlebarGearshift;
    private final String handleMaterial;
    private int price;
    private String deliveryDate;
    public OrderTransfer(final BigInteger orderId, final String handlebarType,
                         final String handlebarMaterial, final String handlebarGearshift,
                         final String handleMaterial, final Integer price, final String deliveryDate) {
        this.orderId = orderId;
        this.handlebarType = handlebarType;
        this.handlebarMaterial = handlebarMaterial;
        this.handlebarGearshift = handlebarGearshift;
        this.handleMaterial = handleMaterial;
        this.price = price;
        this.deliveryDate = deliveryDate;
    }

    @Override public String toString() {
        return MessageFormat.format(
            "OrderTransfer'{'orderId=''{0}'', handlebarType=''{1}'', handlebarMaterial=''{2}'', handlebarGearshift=''{3}'', handleMaterial=''{4}'', price=''{5}'',deliveryDate=''{6}''}'",
            orderId, handlebarType, handlebarMaterial, handlebarGearshift, handleMaterial,price,deliveryDate);
    }
}
