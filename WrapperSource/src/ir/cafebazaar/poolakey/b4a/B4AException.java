package ir.cafebazaar.poolakey.b4a;

import anywheresoftware.b4a.AbsObjectWrapper;
import anywheresoftware.b4a.BA;
import ir.cafebazaar.poolakey.Connection;
import ir.cafebazaar.poolakey.ConnectionState;
import ir.cafebazaar.poolakey.exception.AbortedException;
import ir.cafebazaar.poolakey.exception.BazaarNotFoundException;
import ir.cafebazaar.poolakey.exception.BazaarNotSupportedException;
import ir.cafebazaar.poolakey.exception.ConsumeFailedException;
import ir.cafebazaar.poolakey.exception.DisconnectException;
import ir.cafebazaar.poolakey.exception.DynamicPriceNotSupportedException;
import ir.cafebazaar.poolakey.exception.IAPNotSupportedException;
import ir.cafebazaar.poolakey.exception.PurchaseHijackedException;
import ir.cafebazaar.poolakey.exception.ResultNotOkayException;
import ir.cafebazaar.poolakey.exception.SubsNotSupportedException;

@BA.ShortName("PoolakeyException")
public class B4AException extends AbsObjectWrapper<Throwable> {

    public void Initialize(Throwable throwable) {
        setObject(throwable);
    }

    public String getErrorMessage() {
        return getObject().getMessage();
    }

    public boolean getIsAbortedException() {
        return getObject() instanceof AbortedException;
    }

    public boolean getIsBazaarNotFoundException() {
        return getObject() instanceof BazaarNotFoundException;
    }

    public boolean getIsBazaarNotSupportedException() {
        return getObject() instanceof BazaarNotSupportedException;
    }

    public boolean getIsConsumeFailedException() {
        return getObject() instanceof ConsumeFailedException;
    }

    public boolean getIsDisconnectException() {
        return getObject() instanceof DisconnectException;
    }

    public boolean getIsDynamicPriceNotSupportedException() {
        return getObject() instanceof DynamicPriceNotSupportedException;
    }

    public boolean getIsIAPNotSupportedException() {
        return getObject() instanceof IAPNotSupportedException;
    }

    public boolean getIsPurchaseHijackedException() {
        return getObject() instanceof PurchaseHijackedException;
    }

    public boolean getIsResultNotOkayException() {
        return getObject() instanceof ResultNotOkayException;
    }

    public boolean getIsSubsNotSupportedException() {
        return getObject() instanceof SubsNotSupportedException;
    }

    @BA.Hide
    @Override
    public String toString() {
        return getObject() == null ? super.toString() : getObject().toString();
    }
}
