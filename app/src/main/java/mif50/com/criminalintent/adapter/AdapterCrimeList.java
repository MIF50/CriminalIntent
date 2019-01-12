package mif50.com.criminalintent.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import mif50.com.criminalintent.R;
import mif50.com.criminalintent.listener.ItemClickListener;
import mif50.com.criminalintent.model.Crime;


/*
 * the Adapter class
 * */

public class AdapterCrimeList extends RecyclerView.Adapter<CrimeViewHolder> {
    private List<Crime> crimes;
    private ItemClickListener itemClickListener;

    public AdapterCrimeList(List<Crime> crimes) {
        this.setCrimes(crimes);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public List<Crime> getCrimes() {

        return crimes;
    }

    public void setCrimes(List<Crime> crimes) {
        this.crimes = crimes;

    }

    @NonNull
    @Override
    public CrimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.crime_list_item, parent, false);

        return new CrimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CrimeViewHolder holder, final int position) {

        Crime crime = crimes.get(position);
        holder.bindCrime(crime);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.onItemClicked(v, position, false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getCrimes().size();
    }
}

/*
     the view holder class
   */
class CrimeViewHolder extends RecyclerView.ViewHolder {

    TextView title, date;
    CheckBox solved;
    Crime crime;


    public CrimeViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.m_list_title);
        date = itemView.findViewById(R.id.m_list_date);
        solved = itemView.findViewById(R.id.m_list_check_solved);

    }

    public void bindCrime(Crime crime) {

        this.crime = crime;
        title.setText(crime.getmTitle());
        date.setText(crime.getmData().toString());
        solved.setChecked(crime.ismSolved());

    }


}
