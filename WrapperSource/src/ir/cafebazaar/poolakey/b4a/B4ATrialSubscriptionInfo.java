package ir.cafebazaar.poolakey.b4a;

import anywheresoftware.b4a.AbsObjectWrapper;
import anywheresoftware.b4a.BA;
import ir.cafebazaar.poolakey.entity.TrialSubscriptionInfo;

@BA.ShortName("PoolakeyTrialSubscriptionInfo")
public class B4ATrialSubscriptionInfo extends AbsObjectWrapper<TrialSubscriptionInfo> {

    public int getTrialPeriodDays() {
        return getObject().getTrialPeriodDays();
    }

    public boolean getIsAvailable() {
        return getObject().isAvailable();
    }

    @BA.Hide
    @Override
    public String toString() {
        return getObject() == null ? super.toString() : getObject().toString();
    }
}
