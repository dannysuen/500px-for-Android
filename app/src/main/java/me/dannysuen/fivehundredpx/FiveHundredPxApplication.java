package me.dannysuen.fivehundredpx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import android.app.Application;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by danny on 16-7-26.
 */
public class FiveHundredPxApplication extends Application {

    // 10M volume capacity for Disk Cache
    private static final long HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;

    private Retrofit mRetrofitSingleton;
    private Gson mGson;

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);

        mGson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        OkHttpClient client = new OkHttpClient.Builder().cache(allocateCache()).addNetworkInterceptor(new
                StethoInterceptor())
                .build();

        initPicasso(client);

        mRetrofitSingleton = new Retrofit.Builder()
                .baseUrl(ApiRoot.BASE_URL)
                .callFactory(client)
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .build();
    }

    private void initPicasso(OkHttpClient client) {
        Picasso picasso = new Picasso.Builder(this).downloader(new OkHttp3Downloader(client)).build();
        picasso.setIndicatorsEnabled(BuildConfig.DEBUG);
        picasso.setLoggingEnabled(BuildConfig.DEBUG);
        Picasso.setSingletonInstance(picasso);
    }

    // Allocates disk cache for OkHttpClient, both for
    private Cache allocateCache() {
        // Base directory recommended by http://stackoverflow.com/a/32752861/400717.
        // Guard against null, which is possible according to
        // https://groups.google.com/d/msg/android-developers/-694j87eXVU/YYs4b6kextwJ and
        // http://stackoverflow.com/q/4441849/400717.
        File baseDir = getCacheDir();
        if (baseDir != null) {
            final File cacheDir = new File(baseDir, "HttpResponseCache");
            return new Cache(cacheDir, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE);
        } else {
            return null;
        }
    }

    public Retrofit getRetrofit() {
        return mRetrofitSingleton;
    }

    public Gson getGson() {
        return mGson;
    }
}
