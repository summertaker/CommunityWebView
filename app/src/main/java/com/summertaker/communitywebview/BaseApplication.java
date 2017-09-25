package com.summertaker.communitywebview;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class BaseApplication extends Application {

    private static BaseApplication mInstance;

    public static final String TAG = BaseApplication.class.getSimpleName();

    private List<SiteData> mSiteList;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        mSiteList = new ArrayList<>();

        //mSiteData.add(new SiteData("더쿠 재팬스퀘어", "http://theqoo.net/index.php?mid=japan&filter_mode=normal&category=26063"));
        //mSiteData.add(new SiteData("더쿠 48스퀘어", "http://theqoo.net/index.php?mid=talk48&filter_mode=normal&category=161632742"));
        //mSiteData.add(new SiteData("더쿠 48돌", "http://theqoo.net/dol48?filter_mode=normal"));
        //mSiteData.add(new SiteData("더쿠 사카미치", "http://theqoo.net/index.php?mid=jdol&filter_mode=normal&category=29770"));
        mSiteList.add(new SiteData("오유 베오베", "http://m.todayhumor.co.kr/list.php?table=bestofbest&page=1"));
        mSiteList.add(new SiteData("루리웹 힛갤", "http://m.ruliweb.com/best/selection?page=1"));
        mSiteList.add(new SiteData("보배 베스트", "http://m.bobaedream.co.kr/board/new_writing/best/1"));
        mSiteList.add(new SiteData("웃대 오베", "http://m.humoruniv.com/board/list.html?table=pds&st=day&pg=0"));
        mSiteList.add(new SiteData("뽐뿌 핫", "http://m.ppomppu.co.kr/new/hot_bbs.php?page=1"));
        mSiteList.add(new SiteData("뽐뿌 인기", "http://m.ppomppu.co.kr/new/pop_bbs.php?page=1"));
        mSiteList.add(new SiteData("SLR클럽 추천", "http://www.slrclub.com/bbs/zboard.php?id=best_article&category=1&setsearch=category"));
        mSiteList.add(new SiteData("클리앙 공감", "https://m.clien.net/service/group/board_all?od=T33"));
        mSiteList.add(new SiteData("엠팍 최다추천", "http://mlbpark.donga.com/mp/best.php?b=bullpen&m=like"));
    }

    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }

    public List<SiteData> getSiteList() {
        return mSiteList;
    }

    public SiteData getSiteData(int position) {
        return mSiteList.get(position);
    }
}
