package marvin.babyphone;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import marvin.babyphone.model.BabyEntry;
import marvin.babyphone.util.DateFormatter;
import timber.log.Timber;

/**
 * Adapter for creating list items from BabyEntry objects.
 *
 * @author Marvin Suhr
 */
public class BabyAdapter extends RecyclerView.Adapter<BabyAdapter.ViewHolder> {

    private List<BabyEntry> mData;
    private Context mContext;

    // The ViewHolder provides a reference to each view in a data item.
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mDate, mTime, mBabyState, mSitterState;

        ViewHolder(View v) {
            super(v);

            mDate = v.findViewById(R.id.text_date);
            mTime = v.findViewById(R.id.text_time);
            mBabyState = v.findViewById(R.id.text_baby_state);
            mSitterState = v.findViewById(R.id.text_sitter_state);
        }
    }

    /**
     * Initialize without data.
     */
    public BabyAdapter(Context context) {
        mData = new ArrayList<>();
        mContext = context;
    }

    @Override
    public BabyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_babystate, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BabyAdapter.ViewHolder holder, int position) {
        BabyEntry entry = mData.get(position);

        // Get necessary strings
        String time = new DateFormatter(mContext).getTimeOnly(entry.getTimeStamp());
        String date = new DateFormatter(mContext).getDateOnly(entry.getTimeStamp());
        String babyState = getStringForBabyState(entry.getBabyState());
        String sitterState = getStringForSitterState(entry.getSitterState());

        // Set the strings
        holder.mDate.setText(time);
        holder.mTime.setText(date);
        holder.mBabyState.setText(babyState);
        holder.mSitterState.setText(sitterState);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * Add multiple entries and notify to reflect the changes in the UI.
     *
     * @param entries List of new data entries.
     */
    public void addData(List<BabyEntry> entries) {
        Timber.i("Adding " + entries.size() + " entries to the RecyclerView");
        mData.addAll(0, entries);
        notifyDataSetChanged();
    }

    /**
     * Clear the dataset.
     */
    public void deleteData() {
        mData = new ArrayList<>();
        notifyDataSetChanged();
    }

    /**
     * Get a string representation for a BabyState.
     *
     * @param state The BabyState to be displayed.
     * @return A string describing the BabyState.
     */
    private String getStringForBabyState(BabyEntry.BabyState state) {
        switch(state) {
            case CALM:
                return mContext.getString(R.string.baby_calm);

            case NOISY:
                return mContext.getString(R.string.baby_noisy);

            case CRYING:
                return mContext.getString(R.string.baby_crying);

            default:
                return "";
        }
    }

    /**
     * Get a string representation for a SitterState.
     *
     * @param state The SitterState to be displayed.
     * @return A string describing the SitterState.
     */
    private String getStringForSitterState(BabyEntry.SitterState state) {
        switch(state) {
            case PRESENT:
                return mContext.getString(R.string.sitter_present);

            case NOT_PRESENT:
                return mContext.getString(R.string.sitter_not_present);

            default:
                return "";
        }
    }

}
