package ir.cafebazaar.poolakey.b4a;

import anywheresoftware.b4a.AbsObjectWrapper;
import anywheresoftware.b4a.BA;
import ir.cafebazaar.poolakey.Connection;
import ir.cafebazaar.poolakey.ConnectionState;

@BA.ShortName("PoolakeyConnection")
public class B4AConnection extends AbsObjectWrapper<Connection> {

    /**
     * You can use this function to actually disconnect from the billing service.
     */
    public void Disconnect() {
        if (getObject() == null) return;
        getObject().disconnect();
    }

    /**
     * You can use this function to get notified about the billing service's connection state.
     */
    public boolean getHasConnected() {
        if (getObject() == null) return false;
        return getObject().getState() == ConnectionState.Connected.INSTANCE;
    }

    /**
     * You can use this function to get notified about the billing service's connection state.
     */
    public boolean getHasDisconnected() {
        if (getObject() == null) return false;
        return getObject().getState() == ConnectionState.Disconnected.INSTANCE;
    }

    /**
     * You can use this function to get notified about the billing service's connection state.
     */
    public boolean getHasFailedToConnect() {
        if (getObject() == null) return false;
        return getObject().getState() == ConnectionState.FailedToConnect.INSTANCE;
    }
}
