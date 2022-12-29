package com.lyd.yingdijava.UI.Fragment;

import android.graphics.drawable.Drawable;
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
import com.lyd.yingdijava.UI.Adapter.SceneAdapter;

import java.util.List;

public class ImgGalleryFragment extends GroupScene {

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
            // TODO: 2022/12/28 这里需要增加下载功能 
            Log.i("TAG", "download -> " + getArguments().getStringArrayList("LIST").get(mViewPage.getCurrentItem()));
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
