package com.fete.common.tools.helper;

import android.net.Uri;
import android.os.Environment;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.model.CropOptions;

import java.io.File;

/**
 * Created by A on 2018/9/17.
 * <p>
 * 照相拍照工具类
 final TakePhoto takePhoto = getTakePhoto();
 BottomMenuDialog dialog = new BottomMenuDialog.BottomMenuBuilder()
 .addItem("拍照", new View.OnClickListener() {
@Override
public void onClick(View v) {
PhotoHelper.getInstance().doPhoto(0, takePhoto);
}
})
 .addItem("相册中选择", new View.OnClickListener() {
@Override
public void onClick(View v) {
PhotoHelper.getInstance().doPhoto(1, takePhoto);
}
})
 .addItem("取消", null).build();
 dialog.show(getFragmentManager());
 *
 */

public class PhotoHelper {

    CropOptions cropOptions;
    Uri imageUri;
    int height = 800;
    int width = 800;
    int maxSize = 102400;
    int limit = 3;

    private static PhotoHelper photoHelper = null;

    private PhotoHelper() {
    }

    public static PhotoHelper getInstance() {
        if (photoHelper == null) {
            synchronized (PhotoHelper.class) {
                if (photoHelper == null) {
                    photoHelper = new PhotoHelper();
                }
            }
        }

        photoHelper.initTakePhoto();
        return photoHelper;
    }


    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public void initTakePhoto() {

        //初始化图片保存路径
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        cropOptions = getCropOptions();
        imageUri = Uri.fromFile(file);
        //take配置
//        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
//        builder.setWithOwnGallery(false);//使用TakePhoto自带相册
//        builder.setCorrectImage(false);//纠正拍照的照片旋转角度
//        takePhoto.setTakePhotoOptions(builder.create());
        //压缩配置
//        boolean enableRawFile = true;//压缩后是否保存原图
//        CompressConfig config;
//        LubanOptions option = new LubanOptions.Builder().setMaxHeight(height).setMaxWidth(width).setMaxSize(maxSize).create();
//        config = CompressConfig.ofLuban(option);
//        config.enableReserveRaw(enableRawFile);
//        takePhoto.onEnableCompress(config, true);
    }

    private CropOptions getCropOptions() {

        boolean withWonCrop = false;//false 3方剪切 true takephoto自己的
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setAspectX(width).setAspectY(height);
        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
    }


    /**
     * 相册
     * type 1 //从相册中获取图片并裁剪
     * type 2 //从相册中获取图片
     * type 3 //从文件中获取图片并裁剪
     * type 4 //从文件中获取图片
     * type 5 //图片多选，并裁切
     * type 6 //图片多选
     * 照相
     * type 0 //拍照，并裁剪
     *
     * @param type
     */
    public void doPhoto(int type, TakePhoto takePhoto) {
        switch (type) {
            case 0:
                takePhoto.onPickFromCaptureWithCrop(imageUri, cropOptions);
                break;
            case 1:
                takePhoto.onPickFromGalleryWithCrop(imageUri, cropOptions);
                break;
            case 2:
                takePhoto.onPickFromGallery();
                break;
            case 3:
                takePhoto.onPickFromDocumentsWithCrop(imageUri, cropOptions);
                break;
            case 4:
                takePhoto.onPickFromDocuments();
                break;
            case 5:
                takePhoto.onPickMultipleWithCrop(limit, cropOptions);
                break;
            case 6:
                takePhoto.onPickMultiple(limit);
                break;

        }


    }

}
