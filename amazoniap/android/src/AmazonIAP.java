package org.godotengine.godot;

import android.util.Log;
import com.godot.game.R;
import android.app.Activity;

import com.amazon.device.iap.model.FulfillmentResult;
import com.amazon.device.iap.PurchasingListener;
import com.amazon.device.iap.PurchasingService;
import com.amazon.device.iap.model.ProductDataResponse;
import com.amazon.device.iap.model.PurchaseResponse;
import com.amazon.device.iap.model.PurchaseUpdatesResponse;
import com.amazon.device.iap.model.UserDataResponse;

import java.util.EnumSet;

public class AmazonIAP extends Godot.SingletonBase{

    int deviceID;
    private Activity activity;

    final String TAG = "Godot: ";

    

    static public Godot.SingletonBase initialize(Activity p_activity) { return new AmazonIAP(p_activity);}

    public AmazonIAP(Activity p_activity) {

        this.activity = p_activity;

        registerClass("AmazonIAP", new String[]{
        "init",
        "consume_iap"});
	}


    public void init(int deviceId) {
        Log.i(TAG, "AmazonIAP: init");
        deviceID = deviceId;
    }

    

    public void consume_iap(final String sku)
    {
        PurchasingListener purchasingListener = new PurchasingListener() {
            @Override
            public void onUserDataResponse(UserDataResponse userDataResponse) {
                Log.i(TAG, "AmazonIAP: onUserDataResponse");
            }

            @Override
            public void onProductDataResponse(ProductDataResponse productDataResponse) {
                Log.i(TAG, "AmazonIAP: onProductDataResponse");
            }

            @Override
            public void onPurchaseResponse(PurchaseResponse purchaseResponse) {
                Log.i(TAG, "AmazonIAP: onPurchaseResponse");
                Log.i(TAG, "AmazonIAP: purchaseResponse=" + String.valueOf(purchaseResponse) + "; " + String.valueOf(purchaseResponse.getRequestStatus()));
                switch (purchaseResponse.getRequestStatus()) {
                    // ...
                    case SUCCESSFUL:
                        String sku = purchaseResponse.getReceipt().getSku();
                        PurchasingService.notifyFulfillment(purchaseResponse.getReceipt().getReceiptId(), FulfillmentResult.FULFILLED);
                        GodotLib.calldeferred(deviceID, "purchase_success", new Object[]{"","",sku});
                        break;
                }
            }

            @Override
            public void onPurchaseUpdatesResponse(PurchaseUpdatesResponse purchaseUpdatesResponse) {
                Log.i(TAG, "AmazonIAP: onPurchaseUpdatesResponse");
            }
        };

        PurchasingService.registerListener(activity.getApplicationContext(), purchasingListener);

        PurchasingService.purchase(sku);
    }

}
