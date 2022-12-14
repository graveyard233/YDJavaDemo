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
            //????????????????????????????????????????????????Android10??????????????????Android10????????????????????????Android10???????????????MediaStore????????????
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R){
                Log.i(TAG, "setOnClickListener: sdk??????Android11,????????????????????????");

            } else {
                Log.i(TAG, "setOnClickListener: sdk->" + Build.VERSION.SDK_INT + "???????????????????????????");
//                Log.i(TAG, "onViewCreated: " + requireSceneContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
//                ???????????????onViewCreated: /storage/emulated/0/Android/data/com.lyd.yingdijava/files/Pictures  tm?????????????????????????????????????????????????????????MediaStore??????????????????
                //???????????????????????????????????????

                // ???????????????????????????Android13???????????????????????????<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />???????????????????????????Android13??????????????????
//                PermissionX.init((FragmentActivity) requireActivity())
//                        .permissions(Manifest.permission.READ_MEDIA_IMAGES)//??????????????????Android13??????????????????????????????
//                        .onExplainRequestReason(new ExplainReasonCallback() {
//                            @Override
//                            public void onExplainReason(@NonNull ExplainScope scope, @NonNull List<String> deniedList) {
//                                scope.showRequestReasonDialog(deniedList,"?????????????????????????????????????????????","??????","??????");
//                            }
//                        })
//                        .onForwardToSettings(new ForwardToSettingsCallback() {
//                            @Override
//                            public void onForwardToSettings(@NonNull ForwardScope scope, @NonNull List<String> deniedList) {
//                                scope.showForwardToSettingsDialog(deniedList,"?????????????????????????????????????????????","OK");
//                            }
//                        })
//                        .request(new RequestCallback() {
//                            @Override
//                            public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
//                                if (allGranted){
//                                    Log.i(TAG, "onResult: ????????????????????????");
//                                    Log.i(TAG, "onResult: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
//                                } else {
//                                    ToastUtils.getDefaultMaker().show("????????????????????????" + deniedList);
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
