package com.ronen.catanboard;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ronen.catanboard.classes.Board;
import com.ronen.catanboard.classes.SaveData;
import com.ronen.catanboard.util.Data;
import com.ronen.catanboard.util.Preferences;
import com.ronen.catanboard.util.Util;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final List<String> items;
    private final OnItemClickListener onItemClickListener;
    private final OnDeleteClickListener onDeleteClickListener;

    private final boolean isButton;

    public ListAdapter(List<String> items, boolean isButton, OnItemClickListener onItemClickListener, OnDeleteClickListener onDeleteClickListener) {
        this.items = items;
        this.onItemClickListener = onItemClickListener;
        this.isButton = isButton;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String item);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(String item);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        Button button;
        TextView textView;
        View dividerView;
        ImageButton deleteButton;
        RelativeLayout boardLayout;
        public View view;

        public ViewHolder(View view, boolean isButton, OnItemClickListener onItemClickListener, OnDeleteClickListener deleteClickListener) {
            super(view);
            this.view = view;
            button = view.findViewById(R.id.button);
            textView = view.findViewById(R.id.textView);
            dividerView = view.findViewById(R.id.dividerView);
            deleteButton = view.findViewById(R.id.deleteButton);
            boardLayout = view.findViewById(R.id.main);

            if (isButton) {
                button.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                button.setOnClickListener(v -> onItemClickListener.onItemClick(button.getText().toString()));
                deleteButton.setOnClickListener(v -> deleteClickListener.onDeleteClick(button.getText().toString()));

            } else {
                textView.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
                textView.setOnClickListener(v -> onItemClickListener.onItemClick(textView.getText().toString()));            deleteButton.setOnClickListener(v -> deleteClickListener.onDeleteClick(textView.getText().toString()));
                deleteButton.setOnClickListener(v -> deleteClickListener.onDeleteClick(textView.getText().toString()));

            }
            dividerView.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewTypeInt) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view, isButton, onItemClickListener, onDeleteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = items.get(position);
        String show = text;


        if (text.length() > 8 && text.endsWith("NODELETE"))
            show = text.substring(0, text.length() - 8);
        if (isButton) {
            holder.button.setText(show);
            holder.button.setTextColor(Util.resolveAttrColor(holder.view.getContext()));
        } else {
            holder.textView.setText(show);
            holder.textView.setTextColor(Util.resolveAttrColor(holder.view.getContext()));
        }

        if (text.contains("NODELETE")) {
            holder.deleteButton.setVisibility(View.GONE);
            holder.deleteButton.setOnClickListener(null);
            holder.boardLayout.setVisibility(View.GONE);
            return;
        }
        holder.boardLayout.setVisibility(View.VISIBLE);
        holder.deleteButton.setVisibility(View.VISIBLE);

        SaveData sd = Data.getBoard(holder.view.getContext(), text);
        if (sd == null)
            return;
        Board b = sd.getBoard();
        holder.boardLayout.post(() ->
                b.drawBoard(holder.boardLayout, holder.view.getContext(), false)
        );
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
