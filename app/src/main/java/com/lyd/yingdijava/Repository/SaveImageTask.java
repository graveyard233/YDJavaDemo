package com.lyd.yingdijava.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.bytedance.scene.group.GroupScene;
import com.lyd.yingdijava.Utils.BaseSubscriber;

import org.reactivestreams.Subscription;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SaveImageTask {
    private final Context mContext;

    private int mDownloadCount;

    private static final String PATH_IMAGES = Environment.DIRECTORY_PICTURES + "/YDJava";//DCIM是系统的相册，不应该使用，而改使用picture，这个是专门给应用用的图片文件夹

    private Subscription mSubscription;

    public SaveImageTask(GroupScene scene) {
        this.mContext = scene.requireSceneContext();
    }

    public static class DownloadResult {//可以在未来拓展更多用法
        String suffix;

        String url;

        BufferedInputStream bufferedInputStream;

        public DownloadResult(String suffix, String url, BufferedInputStream inputStream) {
            this.suffix = suffix;
            this.url = url;
            this.bufferedInputStream = inputStream;
        }
    }

    public void toDownload(String... urls){
        if (isRunning()){
            ToastUtils.getDefaultMaker().show("图片正在下载，防止风怒");
        }
        Observable.fromArray(urls)
                .observeOn(Schedulers.io())
                .map(url ->{
                    URL urL = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) urL.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(connection.getInputStream());
//                    File file = Glide.with(mContext)
//                            .load(url)
//                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                            .get();
                    if (url.contains(".gif")){
                        Log.i("TAG", "toDownload: 这张图是gif图，要切换下载后缀");
//                        return new DownloadResult("image/gif",url,(InputStream) new ByteArrayInputStream(File2byte(file)));
                        return new DownloadResult("image/gif",url,bufferedInputStream);
                    } else {
//                        return new DownloadResult("image/jpeg",url,(InputStream) new ByteArrayInputStream(File2byte(file)));
                        return new DownloadResult("image/jpeg",url,bufferedInputStream);
                    }
                })
                .map(downloadResult ->{
                    Uri tableUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DESCRIPTION,"用户从营地下载的图片");
                    values.put(MediaStore.Images.Media.DISPLAY_NAME,System.currentTimeMillis());//使用时间作为图片名称，防止重复
                    values.put(MediaStore.Images.Media.MIME_TYPE,downloadResult.suffix);
                    values.put(MediaStore.Images.Media.TITLE,"营地图片");
                    values.put(MediaStore.Images.Media.RELATIVE_PATH,PATH_IMAGES);//下载路径
                    values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());//加入时间？
                    Uri imgUri = mContext.getContentResolver().insert(tableUri,values);
                    OutputStream os = mContext.getContentResolver().openOutputStream(imgUri);
                    BufferedOutputStream bos = new BufferedOutputStream(os,8192 * 3);


                    byte[] bytes = new byte[1024 * 2];
                    int len = 0;
                    while (((len = downloadResult.bufferedInputStream.read(bytes)) != -1)){
                        bos.write(bytes,0,len);
                    }
                    bos.flush();
                    bos.close();
                    downloadResult.bufferedInputStream.close();
//                    try {
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        if (imgUri != null){
//                            mContext.getContentResolver().delete(imgUri,null,null);
//                        }
//                    } finally {
//                        if (os!=null)
//                            os.close();
//                    }
                    return true;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Boolean>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        super.onSubscribe(s);
                        mSubscription = s;
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean){
                            mDownloadCount++;
                            Log.i("TAG", "onNext: " + mDownloadCount);
                            if (mDownloadCount == urls.length){
                                ToastUtils.getDefaultMaker().show("所有图片下载完成");
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.getDefaultMaker().show("有张图下载失败");
                    }

                    @Override
                    public void onComplete() {
                        Log.i("TAG", "onComplete: 流程走完了");
                        mSubscription = null;
                    }
                });



    }

    private boolean isRunning() {
        return mSubscription != null;
    }

    private static byte[] File2byte(File tradeFile){
        byte[] buffer = null;
        try
        {
            FileInputStream fis = new FileInputStream(tradeFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024 * 2];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e){
            e.printStackTrace();
        }
        return buffer;
    }

}
