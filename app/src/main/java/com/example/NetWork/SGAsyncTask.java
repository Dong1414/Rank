package com.example.NetWork;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.example.rank.SGSearch;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class SGAsyncTask extends AsyncTask<Void, Void, Void> {
    public static final String NAVER_SITE = "N";
    public static final String GOOGLE_SITE = "G";
    public static final String YOUTUBE_SITE = "Y";
    private static final int FROM_WIDGET = 0;
    private static final int FROM_APPLICATION = 1;

    private Context taskContext;
    String taskSite;
    private int fromWhere;
    private ArrayList<SGSearch> naverArr, googleArr, youtubeArr;
    private AsyncTaskCallBack callBack;

    public SGAsyncTask(Context context, ArrayList<SGSearch> arr, String site, AsyncTaskCallBack callBack) {
        super();
        this.taskContext = context;
        this.taskSite = site;
        this.fromWhere = FROM_APPLICATION;
        this.naverArr = taskSite.equalsIgnoreCase(NAVER_SITE) ? arr : null;
        this.googleArr = taskSite.equalsIgnoreCase(GOOGLE_SITE) ? arr : null;
        this.youtubeArr = taskSite.equalsIgnoreCase(YOUTUBE_SITE) ? arr : null;
        this.callBack = callBack;
    }

    private boolean isConnected() {
        ConnectivityManager conn = (ConnectivityManager) taskContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (isConnected()) {
            if (fromWhere == FROM_APPLICATION) {
                getRealRank(taskSite);
            } else {
                getRealRank(NAVER_SITE);
                getRealRank(GOOGLE_SITE);
                getRealRank(YOUTUBE_SITE);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (callBack != null) {
            callBack.onSuccess();
        }
    }

    private void getRealRank(String whatSite) {
        SGSearch item;
        if (NAVER_SITE.equalsIgnoreCase(whatSite)) {
            naverArr.clear();
            try {
                Document document = Jsoup.connect("https://datalab.naver.com/keyword/realtimeList.naver?where=main").get();
                if (document != null) {
                    Elements elements = document.select("div.item_box");
                    for (Element element : elements) {
                        item = new SGSearch();
                        item.setRank(element.select("span.item_num").text() + ". ");
                        item.setTitle(element.select("span.item_title").text());
                        item.setUrl("https://search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query="
                                + item.getTitle());
                        naverArr.add(item);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (GOOGLE_SITE.equalsIgnoreCase(whatSite)) {
            googleArr.clear();
            try {
                Document document = Jsoup.connect("https://trends.google.co.kr/trends/trendingsearches/daily/rss?geo=KR").get();
                if (document != null) {
                    Elements elements = document.select("item");
                    for (Element element : elements) {
                        item = new SGSearch();
                        item.setRank(element.select("ht|approx_traffic").text() + ". ");
                        item.setTitle(element.select("title").text());
                        item.setUrl("https://www.google.com/search?q=" + item.getTitle());
                        googleArr.add(item);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
