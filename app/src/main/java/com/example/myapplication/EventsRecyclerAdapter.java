package com.example.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.Calendar.ParseDate.DateParser;
import com.example.myapplication.models.entity.Event;

import java.util.List;

public class EventsRecyclerAdapter extends RecyclerView.Adapter<EventsRecyclerAdapter.ViewHolder> {

    private List<Event> dataModels;
    private IItemClickListener listener;

    public EventsRecyclerAdapter(List<Event> dataModels) {
        this.dataModels = dataModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viewholder_event, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {
        Event event = dataModels.get(index);
        viewHolder.bind(event);
        viewHolder.setItemClickListener(listener);
    }

    @Override
    public int getItemCount() {
        int count = 0;

        if (dataModels != null) {
            count = dataModels.size();
        }
        return count;
    }

    public void setOnClickListener(IItemClickListener listener) {
        this.listener = listener;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private Event model;
        private Event formatingModel;
        private TextView title;
        private TextView description;
        private TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.viewholder_title);
            description = itemView.findViewById(R.id.viewholder_description);
            date = itemView.findViewById(R.id.viewholder_date);
        }


        void bind(Event eventModel) {
            model = eventModel;
            formatingModel = eventModel;

            title.setText(model.getTitle());
            description.setText(model.getDescription());
            date.setText(DateParser.formatDate(formatingModel.getDate(),"yyyy-MM-dd", "dd   MMMM"));
        }

        void setItemClickListener(final IItemClickListener listener) {
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(model);
                }
            });
        }
    }

    public void updateEvent(Event event) {
        dataModels.add(event);
        notifyDataSetChanged();
    }

    interface IItemClickListener {
        void onItemClick(Event model);
    }
}
