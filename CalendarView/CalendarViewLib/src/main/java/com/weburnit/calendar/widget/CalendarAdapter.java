package com.weburnit.calendar.widget;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weburnit.calendar.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by paulnguyen on 11/28/16.
 */

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    private List<CalendarItem> data;

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView price;
        private TextView subject;
        private TextView information;
        private TextView name;
        private CircleImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            price = (TextView) itemView.findViewById(R.id.price);
            subject = (TextView) itemView.findViewById(R.id.subject);
            information = (TextView) itemView.findViewById(R.id.information);
            name = (TextView) itemView.findViewById(R.id.name);
            imageView = (CircleImageView) itemView.findViewById(R.id.photo);
        }

        public void setData(CalendarItem item) {
            price.setText(item.getPrice());
            subject.setText(item.getSubject());
            name.setText(item.getName());
            information.setText(item.getInformation());
            imageView.setImageURI(Uri.parse(item.getPhoto()));
        }
    }

    public CalendarAdapter() {
        this.data = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.class_info_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(this.data.get(position));
    }

    public void addData(List<CalendarItem> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public void clearData() {
        this.data.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }
}
