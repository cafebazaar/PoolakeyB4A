package ir.cafebazaar.poolakey.b4a;

import static ir.cafebazaar.poolakey.b4a.B4AEvents.CheckTrialSubscriptionFailed;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.CheckTrialSubscriptionFailed_B4A;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.CheckTrialSubscriptionSucceed;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.CheckTrialSubscriptionSucceed_B4A;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.ConnectionFailed;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.ConnectionFailed_B4A;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.ConnectionSucceed;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.ConsumeFailed;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.ConsumeFailed_B4A;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.ConsumeSucceed;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.Disconnected;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.FailedToBeginFlow;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.FailedToBeginFlow_B4A;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.GetSkuDetailsFailed;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.GetSkuDetailsFailed_B4A;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.GetSkuDetailsSucceed;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.GetSkuDetailsSucceed_B4A;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.PurchaseCanceled;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.PurchaseFailed;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.PurchaseFailed_B4A;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.PurchaseFlowBegan;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.PurchaseSucceed;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.PurchaseSucceed_B4A;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.QueryFailed;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.QueryFailed_B4A;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.QuerySucceed;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.QuerySucceed_B4A;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.subExists;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.wrapFailure;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.wrapFunction0;
import static ir.cafebazaar.poolakey.b4a.B4AEvents.wrapFunction1;

import androidx.activity.result.ActivityResultRegistry;

import java.util.Arrays;

import anywheresoftware.b4a.AbsObjectWrapper;
import anywheresoftware.b4a.BA;
import ir.cafebazaar.poolakey.Payment;
import ir.cafebazaar.poolakey.callback.CheckTrialSubscriptionCallback;
import ir.cafebazaar.poolakey.callback.ConnectionCallback;
import ir.cafebazaar.poolakey.callback.ConsumeCallback;
import ir.cafebazaar.poolakey.callback.GetSkuDetailsCallback;
import ir.cafebazaar.poolakey.callback.PurchaseCallback;
import ir.cafebazaar.poolakey.callback.PurchaseQueryCallback;
import ir.cafebazaar.poolakey.config.PaymentConfiguration;
import ir.cafebazaar.poolakey.config.SecurityCheck;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * Android In-App Billing SDK for CafeBazaar App Store.
 */
@BA.ShortName("Poolakey")
@BA.Author("CafeBazaar")
@BA.Version(2.10f)
@BA.ActivityObject
@BA.Permissions(values = {
        "com.farsitel.bazaar.permission.PAY_THROUGH_BAZAAR"
})
@BA.DependsOn(values = {
        "poolakey-2.1.0.aar"
})
@BA.Events(values = {
        ConnectionSucceed,
        Disconnected,
        ConnectionFailed_B4A,
        CheckTrialSubscriptionSucceed_B4A,
        CheckTrialSubscriptionFailed_B4A,
        QuerySucceed_B4A,
        QueryFailed_B4A,
        GetSkuDetailsSucceed_B4A,
        GetSkuDetailsFailed_B4A,
        ConsumeSucceed,
        ConsumeFailed_B4A,
        PurchaseSucceed_B4A,
        PurchaseFlowBegan,
        PurchaseCanceled,
        PurchaseFailed_B4A,
        FailedToBeginFlow_B4A,
})
public class B4APayment extends AbsObjectWrapper<Payment> {

    private B4AConnection connection;

    public B4APaymentConfiguration Initialize() {
        return new B4APaymentConfiguration();
    }

    public class B4APaymentConfiguration {

        private boolean shouldSupportSubscription;
        private SecurityCheck securityCheck;

        private B4APaymentConfiguration() {
            // Default values
            DisableSecurityCheck();
            ShouldSupportSubscription(true);
        }

        /**
         * You have to use this object in order to disable local security checks.
         */
        public B4APaymentConfiguration DisableSecurityCheck() {
            securityCheck = SecurityCheck.Disable.INSTANCE;
            return this;
        }

        /**
         * You have to use this class in order to enable local security checks. You can access to your
         * app's public rsa key from Bazaar's developer panel, under "In-App Billing" tab:
         * https://pishkhan.cafebazaar.ir/apps/YOUR_APPS_PACKAGE_NAME/in-app-billing
         */
        public B4APaymentConfiguration EnableSecurityCheck(String RSAPublicKey) {
            securityCheck = new SecurityCheck.Enable(RSAPublicKey);
            return this;
        }

        public B4APaymentConfiguration ShouldSupportSubscription(boolean support) {
            shouldSupportSubscription = support;
            return this;
        }

        public void Build(final BA ba) {
            setObject(new Payment(
                    ba.context,
                    new PaymentConfiguration(
                            securityCheck,
                            shouldSupportSubscription
                    )
            ));
        }
    }

    /**
     * Returns last Connection interface which you can use to disconnect from the
     * service or get the current connection state.
     */
    public B4AConnection getConnection() {
        if (connection == null) {
            connection = new B4AConnection();
        }
        return connection;
    }

    /**
     * You have to use this function to connect to the In-App Billing service. Note that you have to
     * connect to Bazaar's Billing service before using any other available functions, So make sure
     * you call this function before doing anything else, also make sure that you are connected to
     * the billing service through Connection.
     * <p>
     * Returns a Connection interface which you can use to disconnect from the
     * service or get the current connection state.
     *
     * <b>Events:</b>
     * <code>EventName_ConnectionSucceed</code>
     * <code>EventName_Disconnected</code>
     * <code>EventName_ConnectionFailed (Error As PoolakeyException)</code>
     */
    public B4AConnection Connect(final BA ba, final String EventName) {
        connection = new B4AConnection();
        connection.setObject(getObject().connect(new Function1<ConnectionCallback, Unit>() {
            @Override
            public Unit invoke(final ConnectionCallback callback) {
                if (subExists(ba, EventName, ConnectionSucceed)) {
                    callback.connectionSucceed(wrapFunction0(ba, EventName, ConnectionSucceed, callback));
                }
                if (subExists(ba, EventName, Disconnected)) {
                    callback.disconnected(wrapFunction0(ba, EventName, Disconnected, callback));
                }
                if (subExists(ba, EventName, ConnectionFailed)) {
                    callback.connectionFailed(wrapFailure(ba, EventName, ConnectionFailed));
                }
                return null;
            }
        }));
        return connection;
    }

    /**
     * You can use this function to check trial subscription,
     *
     * <b>Events:</b>
     * <code>EventName_CheckTrialSubscriptionSucceed (Info As BazaarTrialSubscriptionInfo)</code>
     * <code>EventName_CheckTrialSubscriptionFailed (Error As PoolakeyException)</code>
     */
    public void CheckTrialSubscription(final BA ba, final String EventName) {
        getObject().checkTrialSubscription(new Function1<CheckTrialSubscriptionCallback, Unit>() {
            @Override
            public Unit invoke(CheckTrialSubscriptionCallback callback) {
                if (subExists(ba, EventName, CheckTrialSubscriptionFailed)) {
                    callback.checkTrialSubscriptionFailed(wrapFailure(ba, EventName, CheckTrialSubscriptionFailed));
                }
                if (subExists(ba, EventName, CheckTrialSubscriptionSucceed)) {
                    callback.checkTrialSubscriptionSucceed(
                            wrapFunction1(
                                    ba, EventName, CheckTrialSubscriptionSucceed,
                                    B4ATrialSubscriptionInfo.class
                            )
                    );
                }
                return null;
            }
        });
    }

    /**
     * You can use this function to query user's purchased products, Note that if you want to query
     * user's subscribed products, you have to use 'getSubscribedProducts' function, since this function
     * will only query purchased products and not the subscribed products.
     *
     * <b>Events:</b>
     * <code>EventName_QuerySucceed (Query As BazaarPurchaseQuery)</code>
     * <code>EventName_QueryFailed (Error As PoolakeyException)</code>
     */
    public void GetPurchasedProducts(final BA ba, final String EventName) {
        getObject().getPurchasedProducts(getProductsQueryCallback(ba, EventName));
    }

    /**
     * You can use this function to query user's subscribed products, Note that if you want to query
     * user's purchased products, you have to use 'getPurchasedProducts' function, since this function
     * will only query subscribed products and not the purchased products.
     *
     * <b>Events:</b>
     * <code>EventName_QuerySucceed (Query As BazaarPurchaseQuery)</code>
     * <code>EventName_QueryFailed (Error As PoolakeyException)</code>
     */
    public void GetSubscribedProducts(final BA ba, final String EventName) {
        getObject().getSubscribedProducts(getProductsQueryCallback(ba, EventName));
    }

    @BA.Hide
    private static Function1<PurchaseQueryCallback, Unit> getProductsQueryCallback(final BA ba, final String EventName) {
        return new Function1<PurchaseQueryCallback, Unit>() {
            @Override
            public Unit invoke(PurchaseQueryCallback callback) {
                if (subExists(ba, EventName, QueryFailed)) {
                    callback.queryFailed(wrapFailure(ba, EventName, QueryFailed));
                }

                if (subExists(ba, EventName, QuerySucceed)) {
                    callback.querySucceed(wrapFunction1(ba, EventName, QuerySucceed, B4APurchaseQuery.class));
                }
                return null;
            }
        };
    }

    /**
     * You can use this function to get detail of inApp product sku's,
     * <b>SkuIds:</b> This contain all sku id's that you want to get info about it.
     *
     * <b>Events:</b>
     * <code>EventName_GetSkuDetailsSucceed (Query As PoolakeySkuDetailsQuery)</code>
     * <code>EventName_GetSkuDetailsFailed (Error As PoolakeyException)</code>
     */
    public void GetInAppSkuDetails(final BA ba, final String EventName, final String... SkuIds) {
        getObject().getInAppSkuDetails(Arrays.asList(SkuIds), getSkuDetailsQueryCallback(ba, EventName));
    }

    /**
     * You can use this function to get detail of subscription product sku's,
     * <b>SkuIds:</b> This contain all sku id's that you want to get info about it.
     *
     * <b>Events:</b>
     * <code>EventName_GetSkuDetailsSucceed (Query As PoolakeySkuDetailsQuery)</code>
     * <code>EventName_GetSkuDetailsFailed (Error As PoolakeyException)</code>
     */
    public void GetSubscriptionSkuDetails(final BA ba, final String EventName, final String... SkuIds) {
        getObject().getSubscriptionSkuDetails(Arrays.asList(SkuIds), getSkuDetailsQueryCallback(ba, EventName));
    }

    @BA.Hide
    private static Function1<GetSkuDetailsCallback, Unit> getSkuDetailsQueryCallback(final BA ba, final String EventName) {
        return new Function1<GetSkuDetailsCallback, Unit>() {
            @Override
            public Unit invoke(GetSkuDetailsCallback callback) {
                if (subExists(ba, EventName, GetSkuDetailsFailed)) {
                    callback.getSkuDetailsFailed(wrapFailure(ba, EventName, GetSkuDetailsFailed));
                }

                if (subExists(ba, EventName, GetSkuDetailsSucceed)) {
                    callback.getSkuDetailsSucceed(
                            wrapFunction1(ba, EventName, GetSkuDetailsSucceed, B4ASkuDetailsQuery.class)
                    );
                }
                return null;
            }
        };
    }

    /**
     * You can use this function to consume an already purchased product. Note that you can't use
     * this function to consume subscribed products.
     * <b>PurchaseToken:</b> You have received this token when user purchased that particular product.
     * You can also use 'getPurchasedProducts' function to get all the purchased products by this
     * particular user.
     *
     * <b>Events:</b>
     * <code>EventName_ConsumeSucceed</code>
     * <code>EventName_ConsumeFailed (Error As PoolakeyException)</code>
     */
    public void ConsumeProduct(final BA ba, final String EventName, final String PurchaseToken) {
        getObject().consumeProduct(PurchaseToken, new Function1<ConsumeCallback, Unit>() {
            @Override
            public Unit invoke(ConsumeCallback callback) {
                if (subExists(ba, EventName, ConsumeFailed)) {
                    callback.consumeFailed(wrapFailure(ba, EventName, ConsumeFailed));
                }

                if (subExists(ba, EventName, ConsumeSucceed)) {
                    callback.consumeSucceed(wrapFunction0(ba, EventName, ConsumeSucceed));
                }
                return null;
            }
        });
    }

    /**
     * You can use this function to navigate user to Bazaar's payment activity to purchase a product.
     * Note that for subscribing a product you have to use the 'subscribeProduct' function.
     * <b>Request:</b> This contains some information about the product that we are going to purchase.
     *
     * <b>Events:</b>
     * <code>EventName_PurchaseSucceed (PurchaseInfo As PoolakeyPurchaseInfo)</code>
     * <code>EventName_PurchaseFlowBegan</code>
     * <code>EventName_PurchaseCanceled</code>
     * <code>EventName_PurchaseFailed (Error As PoolakeyException)</code>
     * <code>EventName_PurchaseFailedToBeginFlow (Error As PoolakeyException)</code>
     */
    public void PurchaseProduct(final BA ba, final String EventName, final B4APurchaseRequest Request) {
        getObject().purchaseProduct(validateActivity(ba), Request.build(), getPurchaseCallback(ba, EventName));
    }

    /**
     * You can use this function to navigate user to Bazaar's payment activity to subscribe a product.
     * Note that for purchasing a product you have to use the 'purchaseProduct' function.
     * <b>Request:</b> This contains some information about the product that we are going to subscribe.
     *
     * <b>Events:</b>
     * <code>EventName_PurchaseSucceed (PurchaseInfo As PoolakeyPurchaseInfo)</code>
     * <code>EventName_PurchaseFlowBegan</code>
     * <code>EventName_PurchaseCanceled</code>
     * <code>EventName_PurchaseFailed (Error As PoolakeyException)</code>
     * <code>EventName_PurchaseFailedToBeginFlow (Error As PoolakeyException)</code>
     */
    public void SubscribeProduct(final BA ba, final String EventName, final B4APurchaseRequest Request) {
        getObject().subscribeProduct(validateActivity(ba), Request.build(), getPurchaseCallback(ba, EventName));
    }

    @BA.Hide
    private static Function1<PurchaseCallback, Unit> getPurchaseCallback(final BA ba, final String EventName) {
        return new Function1<PurchaseCallback, Unit>() {
            @Override
            public Unit invoke(PurchaseCallback callback) {
                if (subExists(ba, EventName, FailedToBeginFlow)) {
                    callback.failedToBeginFlow(wrapFailure(ba, EventName, FailedToBeginFlow));
                }
                if (subExists(ba, EventName, PurchaseFailed)) {
                    callback.purchaseFailed(wrapFailure(ba, EventName, PurchaseFailed));
                }
                if (subExists(ba, EventName, PurchaseCanceled)) {
                    callback.purchaseCanceled(wrapFunction0(ba, EventName, PurchaseCanceled));
                }
                if (subExists(ba, EventName, PurchaseFlowBegan)) {
                    callback.purchaseFlowBegan(wrapFunction0(ba, EventName, PurchaseFlowBegan));
                }
                if (subExists(ba, EventName, PurchaseSucceed)) {
                    callback.purchaseSucceed(wrapFunction1(ba, EventName, PurchaseSucceed, B4APurchaseInfo.class));
                }
                return null;
            }
        };
    }

    @BA.Hide
    private static ActivityResultRegistry validateActivity(BA ba) {
        return new ActivityResultRegistryWrapper(ba);
    }
}
