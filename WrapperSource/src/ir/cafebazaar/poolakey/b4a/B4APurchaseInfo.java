package ir.cafebazaar.poolakey.b4a;

import anywheresoftware.b4a.AbsObjectWrapper;
import anywheresoftware.b4a.BA;
import ir.cafebazaar.poolakey.entity.PurchaseInfo;
import ir.cafebazaar.poolakey.entity.PurchaseState;

@BA.ShortName("PoolakeyPurchaseInfo")
public class B4APurchaseInfo extends AbsObjectWrapper<PurchaseInfo> {

    public String getDataSignature() {
        return getObject().getDataSignature();
    }

    public String getOrderId() {
        return getObject().getOrderId();
    }

    public String getProductId() {
        return getObject().getProductId();
    }

    public String getPurchaseToken() {
        return getObject().getPurchaseToken();
    }

    public String getOriginalJson() {
        return getObject().getOriginalJson();
    }

    public String getPackageName() {
        return getObject().getPackageName();
    }

    public String getPayload() {
        return getObject().getPayload();
    }

    public long getPurchaseTime() {
        return getObject().getPurchaseTime();
    }

    public boolean getHasPurchased() {
        return getObject().getPurchaseState() == PurchaseState.PURCHASED;
    }

    public boolean getHasRefunded() {
        return getObject().getPurchaseState() == PurchaseState.REFUNDED;
    }

    @BA.Hide
    @Override
    public String toString() {
        return getObject() == null ? super.toString() : getObject().toString();
    }
}
