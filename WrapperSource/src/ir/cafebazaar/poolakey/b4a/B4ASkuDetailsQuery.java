package ir.cafebazaar.poolakey.b4a;

import java.util.List;

import anywheresoftware.b4a.AbsObjectWrapper;
import anywheresoftware.b4a.BA;
import ir.cafebazaar.poolakey.entity.SkuDetails;

@BA.ShortName("PoolakeySkuDetailsQuery")
public class B4ASkuDetailsQuery extends AbsObjectWrapper<List<SkuDetails>> {

    public int getSize() {
        return getObject().size();
    }

    public B4ASkuDetails GetBySkuId(String SkuId) {
        int size = getSize();
        for (int i = 0; i < size; i++) {
            if (getObject().get(i).getSku().equals(SkuId)) {
                return Get(i);
            }
        }
        return null;
    }

    public B4ASkuDetails Get(int index) {
        return (B4ASkuDetails) ConvertToWrapper(new B4ASkuDetails(), getObject().get(index));
    }

    @BA.Hide
    @Override
    public String toString() {
        return getObject() == null ? super.toString() : getObject().toString();
    }
}
