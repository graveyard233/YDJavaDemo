package com.lyd.yingdijava.UI.Fragment;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bytedance.scene.group.GroupScene;
import com.bytedance.scene.group.UserVisibleHintGroupScene;
import com.github.chrisbanes.photoview.PhotoView;
import com.lyd.yingdijava.R;
import com.lyd.yingdijava.Repository.SaveImageTask;
import com.lyd.yingdijava.UI.Adapter.SceneAdapter;

import java.util.List;

public class ImgGalleryFragment extends GroupScene {

    private final static String TAG = "ImgGalleryFragment";

    private ViewPager2 mViewPage;
    private TextView positionText;
    private ImageView downloadImg;

    @NonNull
    @Override
    public ViewGroup onCreateView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup, @Nullable Bundle bundle) {
        return (ViewGroup) layoutInflater.inflate(R.layout.fragment_img_gallery,viewGroup,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPage = view.findViewById(R.id.fragment_imgGallery_viewPager);
        positionText = view.findViewById(R.id.fragment_imgGallery_position);
        downloadImg = view.findViewById(R.id.fragment_imgGallery_download);

        positionText.setText("1" + " / " + getArguments().getStringArrayList("LIST").size() );

        mViewPage.setAdapter(new MyImgGalleryAdapter(this,getArguments().getStringArrayList("LIST")));
        mViewPage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                positionText.setText((position + 1) + " / " + getArguments().getStringArrayList("LIST").size());
            }
        });
        mViewPage.setCurrentItem(getArguments().getInt("POSITION"));

        downloadImg.setOnClickListener(view1 -> {
            Log.i("TAG", "download -> " + getArguments().getStringArrayList("LIST").get(mViewPage.getCurrentItem()));
            //这里考虑到国内用户手机版本基本过Android10，所以不考虑Android10以前的存储适配，Android10以上统一用MediaStore插入图片
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R){
                Log.i(TAG, "setOnClickListener: sdk小于Android11,常规方法请求权限");

            } else {
                Log.i(TAG, "setOnClickListener: sdk->" + Build.VERSION.SDK_INT + "，需要特殊方法请求");
//                Log.i(TAG, "onViewCreated: " + requireSceneContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
//                这句话是：onViewCreated: /storage/emulated/0/Android/data/com.lyd.yingdijava/files/Pictures  tm是在自己的私有文件夹中搞文件，所以要靠MediaStore来访问媒体库
                //咱没有申请存储文件读写权限

                // 下面是权限请求，到Android13的时候就需要特殊的<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />运行时权限了，到了Android13做好适配再删
//                PermissionX.init((FragmentActivity) requireActivity())
//                        .permissions(Manifest.permission.READ_MEDIA_IMAGES)//这句话只有在Android13生效，在之前默认拒绝
//                        .onExplainRequestReason(new ExplainReasonCallback() {
//                            @Override
//                            public void onExplainReason(@NonNull ExplainScope scope, @NonNull List<String> deniedList) {
//                                scope.showRequestReasonDialog(deniedList,"假如需要下载图片，需要存储权限","允许","拒绝");
//                            }
//                        })
//                        .onForwardToSettings(new ForwardToSettingsCallback() {
//                            @Override
//                            public void onForwardToSettings(@NonNull ForwardScope scope, @NonNull List<String> deniedList) {
//                                scope.showForwardToSettingsDialog(deniedList,"您需要去应用设置中打开存储权限","OK");
//                            }
//                        })
//                        .request(new RequestCallback() {
//                            @Override
//                            public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
//                                if (allGranted){
//                                    Log.i(TAG, "onResult: 用户允许存储访问");
//                                    Log.i(TAG, "onResult: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
//                                } else {
//                                    ToastUtils.getDefaultMaker().show("您拒绝了如下权限" + deniedList);
//                                }
//                            }
//                        });
            }
            SaveImageTask task = new SaveImageTask(ImgGalleryFragment.this);
            task.toDownload(getArguments().getStringArrayList("LIST").get(mViewPage.getCurrentItem()));
        });
    }

    private class MyImgGalleryAdapter extends SceneAdapter{
        final List<String> imgUrlList;

        public MyImgGalleryAdapter(@NonNull GroupScene groupScene, final List<String> imgUrlList) {
            super(groupScene);
            this.imgUrlList = imgUrlList;
        }

        @NonNull
        @Override
        public UserVisibleHintGroupScene createScene(int position) {
            PhotoViewScene photoViewScene = new PhotoViewScene();
            Bundle bundle = new Bundle();
            bundle.putString("URL", imgUrlList.get(position));
            photoViewScene.setArguments(bundle);
            return photoViewScene;
        }

        @Override
        public int getItemCount() {
            return imgUrlList.size();
        }
    }

    private class PhotoViewScene extends UserVisibleHintGroupScene{

        PhotoView photoView;

        @NonNull
        @Override
        public ViewGroup onCreateView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup, @Nullable Bundle bundle) {
            FrameLayout frameLayout = new FrameLayout(requireSceneContext());
            photoView = new PhotoView(PhotoViewScene.this.requireSceneContext());
            photoView.setMaximumScale(10.0f);

            frameLayout.addView(photoView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            return frameLayout;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            Glide.with(PhotoViewScene.this.requireSceneContext())
                    .load(getArguments().getString("URL"))
                    .placeholder(R.drawable.img_loading)
                    .error(R.drawable.img_load_error)
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            if (resource instanceof GifDrawable){
                                if (!((GifDrawable)resource).isRunning()){
                                    try{
                                        ((GifDrawable)resource).startFromFirstFrame();
                                        ((GifDrawable)resource).setLoopCount(GifDrawable.LOOP_FOREVER);
                                    }catch (IllegalArgumentException e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                            photoView.setImageDrawable(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        }
    }
}
