package ir.cafebazaar.poolakey.b4a;

import anywheresoftware.b4a.AbsObjectWrapper;
import anywheresoftware.b4a.BA;
import ir.cafebazaar.poolakey.entity.SkuDetails;

@BA.ShortName("PoolakeySkuDetails")
public class B4ASkuDetails extends AbsObjectWrapper<SkuDetails> {

    public String getDescription() {
        return getObject().getDescription();
    }

    public String getSku() {
        return getObject().getSku();
    }

    public String getPrice() {
        return getObject().getPrice();
    }

    public String getTitle() {
        return getObject().getTitle();
    }

    public String getType() {
        return getObject().getType();
    }

    @BA.Hide
    @Override
    public String toString() {
        return getObject() == null ? super.toString() : getObject().toString();
    }
}
