package com.fete.basemodel.location;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.fete.basemodel.R;
import com.fete.basemodel.utils.LogTest;
import com.fete.basemodel.utils.ToastUtil;

import java.util.List;

/**
 * Created by A on 2018/10/10.
 */

public class LocationHelper {


    private static LocationHelper locationHelper = null;


    private LocationHelper() {
        // TODO Auto-generated constructor stub
    }


    public static LocationHelper getInstance() {
        if (locationHelper == null) {
            synchronized (LocationHelper.class) {
                if (locationHelper == null) {
                    locationHelper = new LocationHelper();
                }
            }
        }
        return locationHelper;
    }


    // 定位相关
    private double lat;
    private double lon;
    private boolean isShowOnlyOne = true;//只显示一次

    public LocationClient mLocationClient = null;
    private MyLocationListenerAddress myListener = new MyLocationListenerAddress();

    public void locationStart(Context context) {
        mLocationClient = new LocationClient(context);//声明LocationClient类
        mLocationClient.registerLocationListener(myListener);//注册监听函数

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        //高精度定位模式  LocationMode.Hight_Accuracy：这种定位模式下，会同时使用网络定位和GPS定位，优先返回最高精度的定位结果；
        //低功耗定位模式 LocationMode.Battery_Saving：这种定位模式下，不会使用GPS，只会使用网络定位（Wi-Fi和基站定位）；
        //仅用设备定位模式 LocationMode.Device_Sensors：这种定位模式下，不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位。

        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 10000;
        if (isShowOnlyOne) {
            span = 0;
        }
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类	似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不	杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);

        mLocationClient.start();
    }


    public class MyLocationListener implements BDLocationListener {


        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);

            StringBuffer lotitude = new StringBuffer(256);//获得经纬度的变量，直接用StringBuffer接受了
            StringBuffer longitude = new StringBuffer(256);

            sb.append("time : ");
            sb.append(location.getTime());  //获取定位时间
            sb.append("\nerror code : ");
            sb.append(location.getLocType()); //获取类型类型
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            lotitude.append(location.getLatitude());//获取纬度信息
            lat = location.getLatitude();
            Log.e("jwd", String.valueOf(lotitude));


            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            longitude.append(location.getLongitude()); //获取经度信息
            lon = location.getLongitude();

            Log.e("jwd", String.valueOf(longitude));

            sb.append("\nradius : ");
            sb.append(location.getRadius());  //获取定位精准度

            // map view 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }


            if (location.getLocType() == BDLocation.TypeGpsLocation) {

                // GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());//获取卫星数
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr()); //获取地址信息
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

                // 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr()); //获取地址信息

                sb.append("\noperationers : ");
                sb.append(location.getOperators()); //获取运营商信息

                sb.append("\ndescribe : ");
                sb.append("网络定位成功");

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                // 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");

            } else if (location.getLocType() == BDLocation.TypeServerError) {

                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

            }

            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe()); //位置语义化信息

            final List<Poi> list = location.getPoiList();// POI数据

            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.i("BaiduLocationApiDem", sb.toString());
//            Toast.makeText(MainActivity.this, "经度:" + lotitude.toString() + "纬度:" + longitude.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public class MyLocationListenerAddress extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息
            String str = "location___" + addr + "_" + country + "_" + province + "_" + city + "_" + district + "_" + street;
            LogTest.e("common___" + addr + "_" + country + "_" + province + "_" + city + "_" + district + "_" + street);
            showToast(str);

        }
    }

    /**
     * 显示普通的toast
     *
     * @return
     */
    public void showToast(String str) {
        ToastUtil.sToastUtil.shortDuration(str).setToastBackground(Color.WHITE, R.drawable.toast_radius).show();
    }


}
