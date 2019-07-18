package io.cmichel.boilerplate;

import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.HashMap;
import java.util.Map;
import com.facebook.react.bridge.Promise;


import com.github.druk.rxdnssd.BonjourService;
import com.github.druk.rxdnssd.RxDnssd;
import com.github.druk.rxdnssd.RxDnssdBindable;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import org.json.JSONException;

public class Module extends ReactContextBaseJavaModule {

  private static final String DURATION_SHORT_KEY = "SHORT";
  private static final String DURATION_LONG_KEY = "LONG";

  Subscription subscription;

  String bonjourServiceResult;




  List<BonjorDevice> allIpList = new ArrayList<>();

  public Module(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "Boilerplate";
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
    constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
    return constants;
  }

  @ReactMethod
  public void show(String message, int duration) {
    Toast.makeText(getReactApplicationContext(), message, duration).show();
  }

  @ReactMethod
  public void scanNetworks(){
    RxDnssd rxdnssd = new RxDnssdBindable(getReactApplicationContext());

      subscription = rxdnssd.browse("_workstation._tcp", "local.")
        .compose(rxdnssd.resolve())
        .compose(rxdnssd.queryRecords())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<BonjourService>() {
          @Override
            public void call(BonjourService bonjourService) {
              Log.d("TAG", bonjourService.toString());
              bonjourServiceResult = bonjourService.toString();

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
              Log.e("TAG", "error", throwable);
              bonjourServiceResult = throwable.toString();
            }
      });
  }



  @ReactMethod
  public void getData() {

          allIpList = new ArrayList<>();

          RxDnssd rxdnssd = new RxDnssdBindable(getReactApplicationContext());

            subscription = rxdnssd.browse("_workstation._tcp", "local.")
              .compose(rxdnssd.resolve())
              .compose(rxdnssd.queryRecords())
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new Action1<BonjourService>() {
                @Override
                  public void call(BonjourService bonjourService) {
                    Log.d("TAG", bonjourService.toString());
                    //bonjourServiceResult = bonjourService.toString();
              //      promise.resolve(bonjourServiceResult);
                      if (bonjourService != null){

                          BonjorDevice bDevice = new BonjorDevice();
                          bDevice.deviceName = bonjourService.getServiceName();
                          bDevice.ipAddress = bonjourService.getInet4Address().getHostAddress();

                          allIpList.add(bDevice);

                      }

                  }
              }, new Action1<Throwable>() {
                  @Override
                  public void call(Throwable throwable) {
                    Log.e("TAG", "error", throwable);
                  //  bonjourServiceResult = throwable.toString();
                  //  promise.resolve(bonjourServiceResult);
                  }
            });


      }

      @ReactMethod
      public void getBonjourDevicesList(Promise promise){

        JSONArray ja = new JSONArray();
        for(int i = 0; i < allIpList.size();i++){
          try {
                JSONObject jo = new JSONObject();
                jo.put("deviceName", allIpList.get(i).deviceName);
                jo.put("ipAddress", allIpList.get(i).ipAddress);
                ja.put(jo);

              } catch (JSONException e) {
                  //some exception handler code.
              }
        }

        promise.resolve(ja.toString());



      }



      public class BonjorDevice {


          public String deviceName;
          public String ipAddress;

      }



}
