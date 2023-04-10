package ir.cafebazaar.poolakey.b4a;

import anywheresoftware.b4a.AbsObjectWrapper;
import anywheresoftware.b4a.BA;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

@BA.Hide
class B4AEvents {

    static final String ConnectionSucceed = "ConnectionSucceed";
    static final String Disconnected = "Disconnected";
    static final String ConnectionFailed = "ConnectionFailed";
    static final String ConnectionFailed_B4A = "ConnectionFailed (Error As PoolakeyException)";

    static final String CheckTrialSubscriptionFailed = "CheckTrialSubscriptionFailed";
    static final String CheckTrialSubscriptionFailed_B4A = "CheckTrialSubscriptionFailed (Error As PoolakeyException)";
    static final String CheckTrialSubscriptionSucceed = "CheckTrialSubscriptionSucceed";
    static final String CheckTrialSubscriptionSucceed_B4A = "CheckTrialSubscriptionSucceed (Info As PoolakeyTrialSubscriptionInfo)";

    static final String QueryFailed = "QueryFailed";
    static final String QueryFailed_B4A = "QueryFailed (Error As PoolakeyException)";
    static final String QuerySucceed = "QuerySucceed";
    static final String QuerySucceed_B4A = "QuerySucceed (Query As PoolakeyPurchaseQuery)";

    static final String GetSkuDetailsFailed = "GetSkuDetailsFailed";
    static final String GetSkuDetailsFailed_B4A = "GetSkuDetailsFailed (Error As PoolakeyException)";
    static final String GetSkuDetailsSucceed = "GetSkuDetailsSucceed";
    static final String GetSkuDetailsSucceed_B4A = "GetSkuDetailsSucceed (Query As PoolakeySkuDetailsQuery)";

    static final String ConsumeFailed = "ConsumeFailed";
    static final String ConsumeFailed_B4A = "ConsumeFailed (Error As PoolakeyException)";
    static final String ConsumeSucceed = "ConsumeSucceed";

    static final String FailedToBeginFlow = "PurchaseFailedToBeginFlow";
    static final String FailedToBeginFlow_B4A = "PurchaseFailedToBeginFlow (Error As PoolakeyException)";
    static final String PurchaseFailed = "PurchaseFailed";
    static final String PurchaseFailed_B4A = "PurchaseFailed (Error As PoolakeyException)";
    static final String PurchaseCanceled = "PurchaseCanceled";
    static final String PurchaseFlowBegan = "PurchaseFlowBegan";
    static final String PurchaseSucceed = "PurchaseSucceed";
    static final String PurchaseSucceed_B4A = "PurchaseSucceed (PurchaseInfo As PoolakeyPurchaseInfo)";

    private static String toSub(String eventName, String sub) {
        return (eventName + "_" + sub).toLowerCase();
    }

    static boolean subExists(BA ba, String eventName, String sub) {
        return ba.subExists(toSub(eventName, sub));
    }

    static void call(BA ba, String eventName, String sub, Object sender, Object... params) {
        ba.raiseEvent2(sender, true, toSub(eventName, sub), false, params);
    }

    static Function0<Unit> wrapFunction0(final BA ba, final String eventName, final String sub) {
        return wrapFunction0(ba, eventName, sub, null);
    }

    static Function0<Unit> wrapFunction0(
            final BA ba,
            final String eventName,
            final String sub,
            final Object sender
    ) {
        return new Function0<Unit>() {
            @Override
            public Unit invoke() {
                call(ba, eventName, sub, sender);
                return null;
            }
        };
    }

    static Function1<Throwable, Unit> wrapFailure(
            final BA ba,
            final String eventName,
            final String sub
    ) {
        return new Function1<Throwable, Unit>() {
            @Override
            public Unit invoke(Throwable throwable) {
                B4AException exception = new B4AException();
                exception.Initialize(throwable);
                call(ba, eventName, sub, throwable, exception);
                return null;
            }
        };
    }

    static <T, K extends AbsObjectWrapper<T>> Function1<T, Unit> wrapFunction1(
            final BA ba,
            final String eventName,
            final String sub,
            final Class<K> cls
    ) {
        return new Function1<T, Unit>() {
            @Override
            public Unit invoke(T out) {
                try {
                    K k = cls.newInstance();
                    k.setObject(out);
                    call(ba, eventName, sub, out, k);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }
}
