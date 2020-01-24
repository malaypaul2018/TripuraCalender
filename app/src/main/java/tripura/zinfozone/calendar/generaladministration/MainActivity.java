package tripura.zinfozone.calendar.generaladministration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {
    public GregorianCalendar cal_month, cal_month_copy;
    private HwAdapter hwAdapter;
    private TextView tv_month;
    TextView taptext;
    Animation animBlink;
    ProgressDialog progressDialog;
    GridView gridview;

    private OkHttpClient client;
    private String dataServerResponse;
    private  JSONObject jsonObject = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);


        client=new OkHttpClient();
        dataServerResponse=new String(  );


        TextView taptext = findViewById(R.id.taptext);
        animBlink = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);
        taptext.setVisibility(View.VISIBLE);
        taptext.startAnimation(animBlink);


        HomeCollection.date_collection_arr=new ArrayList<HomeCollection>();

        //oCTOBER 2018
        //oCTOBER 2018

        HomeCollection.date_collection_arr.add( new HomeCollection("2020-01-17" ,"Akheri-Chahar-Sumba","Restricted Holiday","this is holiday"));




        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_month_copy = (GregorianCalendar) cal_month.clone();
 // hwAdapter = new HwAdapter(this, cal_month,HomeCollection.date_collection_arr);
//
        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));


        ImageButton previous = (ImageButton) findViewById(R.id.ib_prev);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cal_month.get(GregorianCalendar.MONTH) == 10&&cal_month.get(GregorianCalendar.YEAR)==2019) {
                    //cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
                    Toast.makeText(MainActivity.this, "No Previous Data Found", Toast.LENGTH_SHORT).show();
                }
                else {
                    setPreviousMonth();
                    refreshCalendar();
                }


            }
        });
        ImageButton next = (ImageButton) findViewById(R.id.Ib_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cal_month.get(GregorianCalendar.MONTH) == 11&&cal_month.get(GregorianCalendar.YEAR)==2020) {
                    //cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1), cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
                    Toast.makeText(MainActivity.this, "Update the App (Updates will be available in the year end.)", Toast.LENGTH_LONG).show();
                }
                else {
                    setNextMonth();
                    refreshCalendar();
                }
            }
        });
       gridview = (GridView) findViewById(R.id.gv_calendar);
//        gridview.setAdapter(hwAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String selectedGridDate = HwAdapter.day_string.get(position);
                ((HwAdapter) parent.getAdapter()).getPositionList(selectedGridDate, MainActivity.this);
            }

        });

        try {
            postRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    protected void setNextMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMaximum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1), cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) + 1);
        }
    }

    protected void setPreviousMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMinimum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH, cal_month.get(GregorianCalendar.MONTH) - 1);
        }

    }

    public void refreshCalendar() {
        hwAdapter.refreshDays();
        hwAdapter.notifyDataSetChanged();
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
    }

    public void share(View view)
    {
        final String appPackageName = BuildConfig.APPLICATION_ID;
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Hello!!! \nTripura Calendar 2020 GA(SA), Government of Tripura Freely available at \n http://play.google.com/store/apps/details?id=" + appPackageName + "\n\nShare this App with your friends, colleagues, official staff completely free of cost";
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Tripura Calendar 2020");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void about(View view)
    {
        Intent intent = new Intent(MainActivity.this, Main4Activity.class);
        startActivity(intent);

//        if (mInterstitialAd.isLoaded()) {
//////            mInterstitialAd.show();
//////        }
    }

    public void rateapp(View view)
    {
        final String appPackageName = BuildConfig.APPLICATION_ID;
        Intent browIntent = new Intent(Intent.ACTION_VIEW, Uri.parse( "http://play.google.com/store/apps/details?id=" + appPackageName));
        startActivity(browIntent);
    }

    public void listholiday(View view)
    {
        Intent intent = new Intent(MainActivity.this, listholiday.class);
        startActivity(intent);

//        Intent browIntent = new Intent(Intent.ACTION_VIEW, Uri.parse( "http://play.google.com/store/apps/details?id=com.tripura.zinfozone.highereducation"));
//        startActivity(browIntent);

//        if (mInterstitialAd.isLoaded()) {
//////            mInterstitialAd.show();
//////        }
    }


    public void postRequest() throws IOException {

        progressDialog=new ProgressDialog( this );
        progressDialog.setMessage( "Loading...." );
        progressDialog.show();

       /* rv=findViewById(R.id.recyclerview_1);
       rv.setLayoutManager(new LinearLayoutManager(this));*/

        // Log.e("mm", ".......1.111111xxxxxxxxxxxxxx........" );

        MediaType MEDIA_TYPE = MediaType.parse( "application/json" );
        //String url = "https://api.myjson.com/bins/nqnpc";

        String url = "http://sebaloy.in/apis/android/Tripura_Express/tripura_calender_data_downloader.php";


        JSONObject postdata = new JSONObject();


        try {


           // postdata.put( "category", Category.trim() );
            postdata.put( "last_sl", 1 );
            // Log.e("mm", "....laptop..................................");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //Log.e("mm", ".......2.111111........" );

        RequestBody body = RequestBody.create( MEDIA_TYPE, postdata.toString() );

        okhttp3.Request request = new okhttp3.Request.Builder()
                //.url(Constants.LOGIN_VALIDATOR)
                .url( url )
                .post( body )
                .header( "Accept", "application/json" )
                .header( "Content-Type", "application/json" )
                .build();

       // Log.e("mm", ".......3.111111........" );

        client.newCall( request ).enqueue( new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
               // Log.e("mm", "..4......xxx........"+mMessage );
                call.cancel();
                // dialog.dismiss();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {

                progressDialog.dismiss();
               // Log.e("mm", ".......5.111111.............................." );

                //dialog.dismiss();

                dataServerResponse = response.body().string();
                Log.e("mm", "....server data........==============++++++++++++++"+  dataServerResponse);


                try {

                    // Log.e("mm", ".......5.111111........" );
                    jsonObject = new JSONObject(dataServerResponse);
                    // Log.e("mm", ".......5.111111........" );
                    JSONArray jsonArray=jsonObject.getJSONArray( "testdata" );
                    //Log.e("mm", ".......5.111111........" );
                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject recive=jsonArray.getJSONObject( i );
//
                        HomeCollection.date_collection_arr.add( new HomeCollection(recive.getString( "event_date" ).trim() ,recive.getString( "event_title" ).trim(),recive.getString( "event_subject" ).trim(),recive.getString( "event_description" ).trim()));

//

                    }

//                 for(int i=0;i<listItem.size();i++) {
//
//                        Log.e("mm", ".......6.111111........"+listItem.get( i ).getDesc()+"======"+listItem.get(i).getHead()+"==========="+listItem.get( i ).getImageurl()+"======" );
//                    }

                    /*Log.e("mm", ".......6.111111........" );

                    adapter=new MyRecyclerViewAdapter(listItem,getApplicationContext());
                    Log.e("mm", ".......7.111111........" );
                    rv.setAdapter(adapter);
                    Log.e("mm", ".......8.111111........" );


*/


                } catch (JSONException e) {
                    Log.e("mm", "....PROBLEM response..data........=============="+e);


                    // MyNewsItem item=new MyNewsItem("news_head" , "news_details" , "news_head" );
                    //Log.e("mm", "....Heading........==============++++++++++++++");
                    //listItem.add(item);


                    e.printStackTrace();
                }


                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                       // Log.e("mm", "....Heading2........==============++++++++++++++");
                    hwAdapter = new HwAdapter( MainActivity.this, cal_month,HomeCollection.date_collection_arr);
                    gridview.setAdapter(hwAdapter);
                   // Log.e("mm", "....Heading3........==============++++++++++++++");

                    }
                });













            }
        } );

    }

}
