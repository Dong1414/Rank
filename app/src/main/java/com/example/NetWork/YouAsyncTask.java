package com.example.NetWork;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.example.rank.YouSearch;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class YouAsyncTask extends AsyncTask<Void, Void, Void> {
    public static final String YOUTUBE_SITE = "Y";
    private static final int FROM_APPLICATION1 = 1;

    private Context taskContext;
    String taskSite;
    private int fromWhere;
    private ArrayList<YouSearch> youArr;
    private AsyncTaskCallBack callBack;

    public YouAsyncTask(Context context, ArrayList<YouSearch> arr, String site, AsyncTaskCallBack callBack) {
        super();
        this.taskContext = context;
        this.taskSite = site;
        this.fromWhere = FROM_APPLICATION1;
        this.youArr = taskSite.equalsIgnoreCase(YOUTUBE_SITE) ? arr : null;
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
            if (fromWhere == FROM_APPLICATION1) {
                getRealRank(taskSite);
            } else {
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
        YouSearch item;
        if (YOUTUBE_SITE.equalsIgnoreCase(whatSite)) {
            youArr.clear();
            try {
                Document document = Jsoup.connect("https://datalab.naver.com/keyword/realtimeList.naver?where=main").get();
                if (document != null) {
                    Elements elements = document.select("div.item_box");
                    for (Element element : elements) {
                        item = new YouSearch();
                        item.setTotal(element.select("span.item_num").text() + ". ");
                        item.setTitle(element.select("span.item_num").text());
                        item.setUrl("\"https://search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query="
                                + item.getTitle());
                        String id = item.getUrl().substring(item.getUrl().lastIndexOf("=") + 1);
                    //    Log.d(id, id);
                      //  item.setIcon(element.select("https://www.youtube.com/watch?v=" + id + "/\" + \"default.jpg").text());

                        youArr.add(item);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
