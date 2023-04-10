package ir.cafebazaar.poolakey.b4a;

import static android.app.Activity.RESULT_CANCELED;
import static androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions.ACTION_REQUEST_PERMISSIONS;
import static androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions.EXTRA_PERMISSIONS;
import static androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult.EXTRA_ACTIVITY_OPTIONS_BUNDLE;
import static androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult.ACTION_INTENT_SENDER_REQUEST;
import static androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult.EXTRA_INTENT_SENDER_REQUEST;
import static androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult.EXTRA_SEND_INTENT_EXCEPTION;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.HashMap;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.IOnActivityResult;

/**
 * B4A overrides onActivityResult without calling super method
 * So ActivityResultRegistry won't notify the result of poolakey purchase,
 * This is a custom implementation of ActivityResultRegistry which
 * adds a B4A IOnActivityResult to B4A's onActivityResultMap
 * and notifies the registry whenever activity in b4a got a result.
 */
@BA.Hide
class ActivityResultRegistryWrapper extends ActivityResultRegistry {

    private final BA ba;

    ActivityResultRegistryWrapper(BA ba) {
        this.ba = ba;
    }

    @Override
    public <I, O> void onLaunch(
            final int requestCode,
            ActivityResultContract<I, O> contract,
            I input,
            ActivityOptionsCompat options
    ) {
        simulateB4AStartActivityForResult(ba, requestCode);

        Activity activity = ba.activity;

        // Immediate result path
        final ActivityResultContract.SynchronousResult<O> synchronousResult =
                contract.getSynchronousResult(activity, input);
        if (synchronousResult != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    dispatchResult(requestCode, synchronousResult.getValue());
                }
            });
            return;
        }

        // Start activity path
        Intent intent = contract.createIntent(activity, input);
        Bundle optionsBundle = null;
        // If there are any extras, we should defensively set the classLoader
        if (intent.getExtras() != null && intent.getExtras().getClassLoader() == null) {
            intent.setExtrasClassLoader(activity.getClassLoader());
        }
        if (intent.hasExtra(EXTRA_ACTIVITY_OPTIONS_BUNDLE)) {
            optionsBundle = intent.getBundleExtra(EXTRA_ACTIVITY_OPTIONS_BUNDLE);
            intent.removeExtra(EXTRA_ACTIVITY_OPTIONS_BUNDLE);
        } else if (options != null) {
            optionsBundle = options.toBundle();
        }
        if (ACTION_REQUEST_PERMISSIONS.equals(intent.getAction())) {

            // requestPermissions path
            String[] permissions = intent.getStringArrayExtra(EXTRA_PERMISSIONS);

            if (permissions == null) {
                permissions = new String[0];
            }

            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        } else if (ACTION_INTENT_SENDER_REQUEST.equals(intent.getAction())) {
            IntentSenderRequest request =
                    intent.getParcelableExtra(EXTRA_INTENT_SENDER_REQUEST);
            try {
                // startIntentSenderForResult path
                ActivityCompat.startIntentSenderForResult(activity, request.getIntentSender(),
                        requestCode, request.getFillInIntent(), request.getFlagsMask(),
                        request.getFlagsValues(), 0, optionsBundle);
            } catch (final IntentSender.SendIntentException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        dispatchResult(requestCode, RESULT_CANCELED,
                                new Intent().setAction(ACTION_INTENT_SENDER_REQUEST)
                                        .putExtra(EXTRA_SEND_INTENT_EXCEPTION, e));
                    }
                });
            }
        } else {
            // startActivityForResult path
            ActivityCompat.startActivityForResult(activity, intent, requestCode, optionsBundle);
        }
    }

    @SuppressWarnings("unchecked")
    private void simulateB4AStartActivityForResult(
            final BA ba,
            final int requestCode
    ) {
        if (ba.processBA != null) {
            simulateB4AStartActivityForResult(ba.processBA, requestCode);
        } else if (ba.sharedProcessBA != null) {
            try {
                Field onActivityResultMap = ba.sharedProcessBA.getClass()
                        .getDeclaredField("onActivityResultMap");
                onActivityResultMap.setAccessible(true);
                HashMap<Integer, WeakReference<IOnActivityResult>> map =
                        (HashMap<Integer, WeakReference<IOnActivityResult>>)
                                onActivityResultMap.get(ba.sharedProcessBA);
                if (map == null) {
                    map = new HashMap<>();
                    onActivityResultMap.set(ba.sharedProcessBA, map);
                }

                map.put(requestCode, new BypassWeakReference(new IOnActivityResult() {
                    @Override
                    public void ResultArrived(int i, Intent intent) {
                        dispatchResult(requestCode, i, intent);
                    }
                }));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Avoid "onActivityResult: IOnActivityResult was released"
     */
    private static final class BypassWeakReference extends WeakReference<IOnActivityResult> {

        private final IOnActivityResult value;

        public BypassWeakReference(IOnActivityResult referent) {
            super(null);
            this.value = referent;
        }

        @Override
        public IOnActivityResult get() {
            return value;
        }
    }
}
