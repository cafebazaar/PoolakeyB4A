package ir.cafebazaar.poolakey.b4a;

import anywheresoftware.b4a.BA;
import ir.cafebazaar.poolakey.request.PurchaseRequest;

@BA.ShortName("PoolakeyPurchaseRequest")
public class B4APurchaseRequest {

    private String productId = null;
    private String payload = null;
    private String dynamicPriceToken = null;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getDynamicPriceToken() {
        return dynamicPriceToken;
    }

    public void setDynamicPriceToken(String dynamicPriceToken) {
        this.dynamicPriceToken = dynamicPriceToken;
    }

    @BA.Hide
    PurchaseRequest build() {
        if (productId == null) {
            throw new RuntimeException("ProductId can not be null!");
        }

        return new PurchaseRequest(productId, payload, dynamicPriceToken);
    }
}
