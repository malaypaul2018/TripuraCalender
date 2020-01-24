package tripura.zinfozone.calendar.generaladministration;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static java.util.Calendar.DAY_OF_MONTH;


class HwAdapter extends BaseAdapter {
    private Activity context;
  //  public Context mContext;

 //   public HwAdapter(Context context){
 //       this.mContext = context;
 //   }
    private Calendar month;
    public GregorianCalendar pmonth;
    /**
     * calendar instance for previous month for getting complete view
     */
    public GregorianCalendar pmonthmaxset;
    private GregorianCalendar selectedDate;
    int firstDay;
    int maxWeeknumber;
    int maxP;
    int calMaxP;
    int mnthlength;
    String itemvalue, curentDateString;
    DateFormat df;

    private ArrayList<String> items;
    public static List<String> day_string;
    public ArrayList<HomeCollection>  date_collection_arr;
    private String gridvalue;
    private ListView listTeachers;
    private ArrayList<Dialogpojo> alCustom=new ArrayList<Dialogpojo>();

    public HwAdapter(Activity context, GregorianCalendar monthCalendar,ArrayList<HomeCollection> date_collection_arr) {
        this.date_collection_arr=date_collection_arr;
        HwAdapter.day_string = new ArrayList<String>();
        Locale.setDefault(Locale.US);
        month = monthCalendar;
        selectedDate = (GregorianCalendar) monthCalendar.clone();
        this.context = context;
        month.set(DAY_OF_MONTH, 1);

        this.items = new ArrayList<String>();
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        curentDateString = df.format(selectedDate.getTime());
//////////////////////////////////////////////////////

        Calendar cal = Calendar.getInstance();
        cal = df.getCalendar();

        int month = cal.get(Calendar.MONTH);
        int day = cal.get(DAY_OF_MONTH);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        String weekday = new DateFormatSymbols().getShortWeekdays()[dayOfWeek];

        String nameofday="Thu";
        Log.d("myTag", weekday+nameofday);
        if(weekday.equals(nameofday))
        {
            Log.d("myTag", weekday+weekday);
            Log.d("myTag", "Sunday will be displayed");
        }



        ////////////////////////////////////////////////
        refreshDays();

    }

    public int getCount() {
        return day_string.size();
    }

    public Object getItem(int position) {
        return day_string.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new view for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView dayView;
        if (convertView == null) { // if it's not recycled, initialize some
            // attributes
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.cal_item, null);

        }


        dayView = (TextView) v.findViewById(R.id.date);
        String[] separatedTime = day_string.get(position).split("-");


        gridvalue = separatedTime[2].replaceFirst("^0*", "");
/////////////////////////////////////////////////////////


        /////////////////////////////////////////////////////////////////////////////
        if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
            dayView.setTextColor(Color.parseColor("#A9A9A9"));
            dayView.setTextSize(18);
            dayView.setClickable(false);
            dayView.setFocusable(false);


        } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
            dayView.setTextColor(Color.parseColor("#A9A9A9"));
            dayView.setTextSize(18);
            dayView.setClickable(false);
            dayView.setFocusable(false);

        } else {
            // setting curent month's days in black color.
            dayView.setTextSize(18);
            dayView.setTextColor(Color.parseColor("#000000"));
         //   dayView.setTextColor(Color.RED);
        }


        if (day_string.get(position).equals(curentDateString)) {
        //    Animation animBlink;
///////////////////////////////////////////////////////// date color change
     //       v.setBackgroundColor(Color.parseColor("#505050"));
            dayView.setTextColor(Color.BLUE);
            dayView.setTextSize(20);
       //     animBlink = AnimationUtils.loadAnimation(context,R.anim.blink);
       //     dayView.startAnimation(animBlink);
      //      dayView.setTypeface(null, Typeface.BOLD);
        //    dayView.setTextSize(15);
        } else {
            v.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }


        dayView.setText(gridvalue);
        dayView.setTextSize(18);

        // create date string for comparison
        String date = day_string.get(position);

        if (date.length() == 1) {
            date = "0" + date;
        }
        String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
        if (monthStr.length() == 1) {
            monthStr = "0" + monthStr;
        }

        setEventView(v, position,dayView);

        return v;
    }

    public void refreshDays() {
        // clear items
        items.clear();
        day_string.clear();
        Locale.setDefault(Locale.US);
        pmonth = (GregorianCalendar) month.clone();
        // month start day. ie; sun, mon, etc
        firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
        // finding number of weeks in current month.
        maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        // allocating maximum row number for the gridview.
        mnthlength = maxWeeknumber * 7;
        maxP = getMaxP(); // previous month maximum day 31,30....
        calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25 ...
        pmonthmaxset = (GregorianCalendar) pmonth.clone();

        pmonthmaxset.set(DAY_OF_MONTH, calMaxP + 1);


        for (int n = 0; n < mnthlength; n++) {

            itemvalue = df.format(pmonthmaxset.getTime());
            pmonthmaxset.add(GregorianCalendar.DATE, 1);

            day_string.add(itemvalue);


        }
    }

    private int getMaxP() {
        int maxP;
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            pmonth.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
        maxP = pmonth.getActualMaximum(DAY_OF_MONTH);

        return maxP;
    }




    public void setEventView(View v,int pos,TextView txt){

        int len=HomeCollection.date_collection_arr.size();
        for (int i = 0; i < len; i++) {
            HomeCollection cal_obj=HomeCollection.date_collection_arr.get(i);
            String date=cal_obj.date;
            int len1=day_string.size();
            if (len1>pos) {

                if (day_string.get(pos).equals(date)) {
                    if ((Integer.parseInt(gridvalue) > 1) && (pos < firstDay)) {

                    } else if ((Integer.parseInt(gridvalue) < 7) && (pos > 28)) {

                    } else {
      //                 v.setBackgroundColor(Color.rgb(255,192,203));
                        txt.setTextSize(18);
                        v.setBackgroundResource(R.drawable.rounded_calender);
                        txt.setTextColor(Color.RED);
           //             txt.setTextSize(25);
                    }

                }
                ////////////////////////////////////////////////////////////////////////////////
                if (day_string.get(pos).equals(curentDateString)) {
///////////////////////////////////////////////////////// date color change
                    //       v.setBackgroundColor(Color.parseColor("#505050"));
                    txt.setTextColor(Color.BLUE);
       //             v.setBackgroundResource(R.drawable.square_calender);
                          txt.setTypeface(null, Typeface.BOLD);
                    txt.setTextSize(18);
                    //    dayView.setTextSize(15);
                }


                /////////////////////////////////////////////////////////////////
            }}
    }

    public void getPositionList(String date,final Activity act){

        int len= HomeCollection.date_collection_arr.size();
        JSONArray jbarrays=new JSONArray();
        for (int j=0; j<len; j++){
            if (HomeCollection.date_collection_arr.get(j).date.equals(date)){
                HashMap<String, String> maplist = new HashMap<String, String>();
                maplist.put("hnames",HomeCollection.date_collection_arr.get(j).name);
                maplist.put("hsubject",HomeCollection.date_collection_arr.get(j).subject);
                maplist.put("descript",HomeCollection.date_collection_arr.get(j).description);
                JSONObject json1 = new JSONObject(maplist);
                jbarrays.put(json1);
            }
        }
        if (jbarrays.length()!=0) {
            final Dialog dialogs = new Dialog(context);
            dialogs.setContentView(R.layout.dialog_inform);
            listTeachers = (ListView) dialogs.findViewById(R.id.list_teachers);
            ImageView imgCross = (ImageView) dialogs.findViewById(R.id.img_cross);
            listTeachers.setAdapter(new DialogAdaptorStudent(context, getMatchList(jbarrays + "")));
            imgCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogs.dismiss();
                }
            });

            dialogs.show();


        }

    }

    private ArrayList<Dialogpojo> getMatchList(String detail) {
        try {
            JSONArray jsonArray = new JSONArray(detail);
            alCustom = new ArrayList<Dialogpojo>();
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.optJSONObject(i);

                Dialogpojo pojo = new Dialogpojo();

                pojo.setTitles(jsonObject.optString("hnames"));
                pojo.setSubjects(jsonObject.optString("hsubject"));
                pojo.setDescripts(jsonObject.optString("descript"));


                alCustom.add(pojo);


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return alCustom;
    }
}

