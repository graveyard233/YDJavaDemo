package com.lyd.yingdijava.UI.Fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bytedance.scene.Scene;
import com.bytedance.scene.ktx.NavigationSceneExtensionsKt;
import com.bytedance.scene.navigation.OnBackPressedListener;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebViewClient;
import com.lyd.yingdijava.Info.UrlInfo;
import com.lyd.yingdijava.R;


public class NewsWebFragment extends Scene {
    private AgentWeb webView;
    private LinearLayout linear;
    private DrawerLayout drawerLayout;
    private View drawer;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_news_web,viewGroup,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linear = findViewById(R.id.fragment_webView_webScene_container);
        ToastUtils.make().show(getArguments().getString("URL"));
        drawer = findViewById(R.id.fragment_webView_comment);
        ViewGroup.LayoutParams lp = drawer.getLayoutParams();
        lp.width = ScreenUtils.getAppScreenWidth();

        drawerLayout = findViewById(R.id.fragment_webView_drawerLayout);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setWebView(UrlInfo.getInstance().getUrlByKey("webView") + getArguments().getString("URL"));
        NavigationSceneExtensionsKt.requireNavigationScene(this)
                .addOnBackPressedListener(this, new OnBackPressedListener() {
                    @Override
                    public boolean onBackPressed() {
                        if (!drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                            return false;
                        } else {
                            Toast.makeText(NewsWebFragment.this.getActivity(), "再按一次就退出~", Toast.LENGTH_SHORT).show();
                            drawerLayout.closeDrawer(Gravity.RIGHT);
                            return true;
                        }
                    }
                });
    }

    private void setWebView(String url){
        webView = AgentWeb.with(requireActivity())
                .setAgentWebParent(linear,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()
                .setWebViewClient(new WebViewClient())
                .createAgentWeb()
                .ready()
                .go(url);

    }
}
