package com.lyd.yingdijava.UI.Fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import com.bumptech.glide.Glide;
import com.bytedance.scene.group.GroupScene;
import com.lyd.yingdijava.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CardSearchFragment extends GroupScene {
    private static final String TAG = "CardSearchFragment";

    private SearchView searchView;

    private ImagesSelectController professionController;

    private ImagesSelectController manaController;

    @NonNull
    @Override
    public ViewGroup onCreateView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup, @Nullable Bundle bundle) {
        return (ViewGroup) layoutInflater.inflate(R.layout.fragment_card_search,viewGroup,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = view.findViewById(R.id.fragment_cardSearch_searchView);
        searchView.setIconifiedByDefault(false);
        initProfessionBar(view);
        initMana(view);
    }

    private void initProfessionBar(View view) {
        professionController = new ImagesSelectController();
        String path = "img/hearthstone/searchFaction/";
        loadImgFromAssetsToImg(path,"Demonhunter.png",view,R.id.bar_hearthstone_profession_demonhunter,professionController);
        loadImgFromAssetsToImg(path,"Druid.png",view,R.id.bar_hearthstone_profession_druid,professionController);
        loadImgFromAssetsToImg(path,"Hunter.png",view,R.id.bar_hearthstone_profession_hunter,professionController);
        loadImgFromAssetsToImg(path,"Mage.png",view,R.id.bar_hearthstone_profession_mage,professionController);
        loadImgFromAssetsToImg(path,"Paladin.png",view,R.id.bar_hearthstone_profession_paladin,professionController);
        loadImgFromAssetsToImg(path,"Priest.png",view,R.id.bar_hearthstone_profession_priest,professionController);
        loadImgFromAssetsToImg(path,"Rogue.png",view,R.id.bar_hearthstone_profession_rogue,professionController);
        loadImgFromAssetsToImg(path,"Shaman.png",view,R.id.bar_hearthstone_profession_shaman,professionController);
        loadImgFromAssetsToImg(path,"Warlock.png",view,R.id.bar_hearthstone_profession_warlock,professionController);
        loadImgFromAssetsToImg(path,"Warrior.png",view,R.id.bar_hearthstone_profession_warrior,professionController);
        loadImgFromAssetsToImg(path,"Deathknight.png",view,R.id.bar_hearthstone_profession_deathknight,professionController);
        loadImgFromAssetsToImg(path,"Neutral.png",view,R.id.bar_hearthstone_profession_neutral,professionController);
    }

    private void initMana(View view) {
        manaController = new ImagesSelectController();
        String path = "img/hearthstone/mana/";
        loadImgFromAssetsToImg(path,"HS0.png",view,R.id.bar_hearthstone_mana_0,manaController);
        loadImgFromAssetsToImg(path,"HS1.png",view,R.id.bar_hearthstone_mana_1,manaController);
        loadImgFromAssetsToImg(path,"HS2.png",view,R.id.bar_hearthstone_mana_2,manaController);
        loadImgFromAssetsToImg(path,"HS3.png",view,R.id.bar_hearthstone_mana_3,manaController);
        loadImgFromAssetsToImg(path,"HS4.png",view,R.id.bar_hearthstone_mana_4,manaController);
        loadImgFromAssetsToImg(path,"HS5.png",view,R.id.bar_hearthstone_mana_5,manaController);
        loadImgFromAssetsToImg(path,"HS6.png",view,R.id.bar_hearthstone_mana_6,manaController);
        loadImgFromAssetsToImg(path,"HS7.png",view,R.id.bar_hearthstone_mana_7,manaController);
        loadImgFromAssetsToImg(path,"HS8.png",view,R.id.bar_hearthstone_mana_8,manaController);
        loadImgFromAssetsToImg(path,"HS9more.png",view,R.id.bar_hearthstone_mana_9more,manaController);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG, "onQueryTextSubmit -> " + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void loadImgFromAssetsToImg(String basePath,String fileName,View baseView,@IdRes int id,ImagesSelectController controller){
        try {
            InputStream inputStream = getResources().getAssets().open(basePath + fileName);
            Glide.with(requireSceneContext())
                    .load(BitmapFactory.decodeStream(inputStream))
                    .into((ImageView) baseView.findViewById(id));
            controller.addImage(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class ImagesSelectController{
        private List<ImgForControl> listImgMsg = new ArrayList<>();

        private float mAlphaNotSelecte = 0.55f;

        public void addImage(@IdRes int id){
            if (listImgMsg.stream().anyMatch(img -> img.id.equals(id))){
                return;
            } else {
                listImgMsg.add(new ImgForControl(id,mAlphaNotSelecte));
            }

            findViewById(id).setAlpha(mAlphaNotSelecte);//统一设置透明度
            findViewById(id).setOnClickListener(view -> {
                for (ImgForControl controller :
                        listImgMsg) {
                    if (controller.id.equals(view.getId())){
                        if (controller.alpha == 1f){
                            //点到同一个item上，设置完属性后退出
                            view.setAlpha(mAlphaNotSelecte);
                            controller.alpha = mAlphaNotSelecte;
                            return;
                        }
                        view.setAlpha(1f);
                        controller.alpha = 1f;
                    } else if (controller.alpha != mAlphaNotSelecte){
                        controller.alpha = mAlphaNotSelecte;
                        findViewById(controller.id).setAlpha(mAlphaNotSelecte);
                    }
                }
            });
        }

        private class ImgForControl {
            public final Integer id;
            public float alpha;

            public ImgForControl(int id, float alpha) {
                this.id = id;
                this.alpha = alpha;
            }

            @Override
            public String toString() {
                return "ImgForControl{" +
                        "id=" + id +
                        ", alpha=" + alpha +
                        '}';
            }
        }
    }
}
