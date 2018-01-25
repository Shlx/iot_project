package marvin.babyphone.util;

import android.content.Context;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {

    private Context mContext;

    public DateFormatter(Context context) {
        this.mContext = context;
    }

    public String getDateString(long timestamp) {
        Date date = new Date(timestamp);
        Locale locale = getCurrentLocale(mContext);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", locale);
        return formatter.format(date);
    }

    public String getDateOnly(long timestamp) {
        Date date = new Date(timestamp);
        Locale locale = getCurrentLocale(mContext);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.", locale);
        return formatter.format(date);
    }

    public String getTimeOnly(long timestamp) {
        Date date = new Date(timestamp);
        Locale locale = getCurrentLocale(mContext);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", locale);
        return formatter.format(date);
    }

    private Locale getCurrentLocale(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return context.getResources().getConfiguration().getLocales().get(0);
        } else{
            // noinspection deprecation
            return context.getResources().getConfiguration().locale;
        }
    }
}
