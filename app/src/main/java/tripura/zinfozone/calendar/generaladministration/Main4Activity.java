package tripura.zinfozone.calendar.generaladministration;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.InterstitialAd;
//import com.google.android.gms.ads.MobileAds;

public class Main4Activity extends AppCompatActivity {
 //   private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        //////////////////////////////////////////////////////////////////////////////////////////////




    //    MobileAds.initialize(Main4Activity.this, "ca-app-pub-8238907620086193~2028560174");
//
  //      AdView adView = (AdView) findViewById(R.id.banner1);
 //       AdRequest adRequest = new AdRequest.Builder().build();
  //      adView.loadAd(adRequest);
//
  //      mInterstitialAd =new InterstitialAd(Main4Activity.this);
 //       mInterstitialAd.setAdUnitId("ca-app-pub-8238907620086193/1589576473");
  //      mInterstitialAd.loadAd(new AdRequest.Builder().build());



//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Code to be executed when an ad request fails.
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when the ad is displayed.
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Code to be executed when when the interstitial ad is closed.
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//            }
//        });
//


//        if (mInterstitialAd.isLoaded()) {
  //          mInterstitialAd.show();
 //       }


        /////////////////////////////////////////////////////////////////////////////////////////
    }

    public void share(View view)
    {
        final String appPackageName = BuildConfig.APPLICATION_ID;
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Hello!!! \nTripura Calendar 2020 GA(SA), Government of Tripura Freely available at \n http://play.google.com/store/apps/details?id=" + appPackageName + "\n\nShare this App with your friends, colleagues, official staff completely free of cost";
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Tripura Calendar");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


}
