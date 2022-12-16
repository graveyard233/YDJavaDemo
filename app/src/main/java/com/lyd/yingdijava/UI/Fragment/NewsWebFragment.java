package com.lyd.yingdijava.UI.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.bytedance.scene.Scene;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebViewClient;
import com.lyd.yingdijava.R;

public class NewsWebFragment extends Scene {
    private AgentWeb webView;
    private LinearLayout linear;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_news_web,viewGroup,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linear = findViewById(R.id.fragment_news_web_linear);
        ToastUtils.make().show(getArguments().getString("URL"));
        setWebView("https://iyingdi.com/tz" + getArguments().getString("URL"));
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
