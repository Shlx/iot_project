package marvin.babyphone.util;

import android.content.Context;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Small class to put dates in the correct format.
 *
 * @author Marvin Suhr
 */
public class DateFormatter {

    private Context mContext;

    public DateFormatter(Context context) {
        this.mContext = context;
    }

    /**
     * Get a full date string, including day and time.
     *
     * @param timestamp Timestamp for the requested date.
     * @return A string representation of that timestamp.
     */
    public String getDateString(long timestamp) {
        Date date = new Date(timestamp * 1000);
        Locale locale = getCurrentLocale(mContext);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", locale);
        return formatter.format(date);
    }

    /**
     * Get a date string for the date only.
     *
     * @param timestamp Timestamp for the requested date.
     * @return A string representation of that timestamp (date only).
     */
    public String getDateOnly(long timestamp) {
        Date date = new Date(timestamp * 1000);
        Locale locale = getCurrentLocale(mContext);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM", locale);
        return formatter.format(date);
    }

    /**
     * Get a date string for the time only.
     *
     * @param timestamp Timestamp for the requested date.
     * @return A string representation of that timestamp (time only).
     */
    public String getTimeOnly(long timestamp) {
        Date date = new Date(timestamp * 1000);
        Locale locale = getCurrentLocale(mContext);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", locale);
        return formatter.format(date);
    }

    /**
     * Get the current system locale.
     *
     * @param context Current application context.
     * @return The current system locale.
     */
    @SuppressWarnings("deprecation")
    private Locale getCurrentLocale(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            return context.getResources().getConfiguration().locale;
        }
    }
}
