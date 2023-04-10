package ir.cafebazaar.poolakey.b4a;

import java.util.List;

import anywheresoftware.b4a.AbsObjectWrapper;
import anywheresoftware.b4a.BA;
import ir.cafebazaar.poolakey.entity.PurchaseInfo;

@BA.ShortName("PoolakeyPurchaseQuery")
public class B4APurchaseQuery extends AbsObjectWrapper<List<PurchaseInfo>> {

    public int getSize() {
        return getObject().size();
    }

    public B4APurchaseInfo GetByProductId(String ProductId) {
        int size = getSize();
        for (int i = 0; i < size; i++) {
            if (getObject().get(i).getProductId().equals(ProductId)) {
                return Get(i);
            }
        }
        return null;
    }

    public B4APurchaseInfo GetByOrderId(String OrderId) {
        int size = getSize();
        for (int i = 0; i < size; i++) {
            if (getObject().get(i).getOrderId().equals(OrderId)) {
                return Get(i);
            }
        }
        return null;
    }

    public B4APurchaseInfo GetByPurchaseToken(String PurchaseToken) {
        int size = getSize();
        for (int i = 0; i < size; i++) {
            if (getObject().get(i).getPurchaseToken().equals(PurchaseToken)) {
                return Get(i);
            }
        }
        return null;
    }

    public B4APurchaseInfo Get(int index) {
        return (B4APurchaseInfo) ConvertToWrapper(new B4APurchaseInfo(), getObject().get(index));
    }

    @BA.Hide
    @Override
    public String toString() {
        return getObject() == null ? super.toString() : getObject().toString();
    }
}
