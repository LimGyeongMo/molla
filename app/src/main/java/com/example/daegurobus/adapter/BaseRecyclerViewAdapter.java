package com.example.daegurobus.adapter;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public abstract class BaseRecyclerViewAdapter<T, H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected Context context;
    protected ArrayList<T> items;
    protected ArrayList<T> savedItems;
    protected OnItemClickListener onItemClickListener;
    protected OnItemLongClickListener onItemLongClickListener;

    public BaseRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(holder.itemView, position);
            }
        });
        holder.itemView.setOnLongClickListener(view -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(holder.itemView, position);
            }

            return false;
        });

        onBindView((H) holder, position);
    }

    abstract public void onBindView(H holder, int position);

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }

        return items.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public T getItem(int position) {
        if (items == null) {
            return null;
        }

        return items.get(position);
    }

    public ArrayList<T> getItems() {
        if (items == null) {
            return new ArrayList<>();
        }

        return items;
    }

    public void initItem(T item) {
        if (items == null) {
            items = new ArrayList<>();
        } else {
            items.clear();
        }

        items.add(item);

        initSavedItems(items);

        notifyDataSetChanged();
    }

    public void initItems(ArrayList<T> list) {

        if (items == null) {
            items = new ArrayList<>();
            items.addAll(list);
        } else {
            items.clear();
            items = list;
        }

        initSavedItems(items);

        notifyDataSetChanged();
    }

    public void addItem(int position, T item) {
        if (items == null) {
            items = new ArrayList<>();
        }

        items.add(position, item);

        addSavedItem(position, item);

        notifyDataSetChanged();
    }

    public void addItem(T item) {
        if (items == null) {
            items = new ArrayList<>();
        }

        items.add(item);

        addSavedItem(item);

        notifyDataSetChanged();
    }

    public void addItems(ArrayList<T> list) {
        if (items == null) {
            items = new ArrayList<>();
        }

        items.addAll(list);

        addSavedItems(list);

        notifyDataSetChanged();
    }

    public void initSavedItems(ArrayList<T> list) {
        if (savedItems == null) {
            savedItems = new ArrayList<>();
        } else {
            savedItems.clear();
        }

        savedItems.addAll(list);
    }

    public void addSavedItem(int position, T item) {
        if (savedItems == null) {
            savedItems = new ArrayList<>();
        }

        savedItems.add(position, item);
    }

    public void addSavedItem(T item) {
        if (savedItems == null) {
            savedItems = new ArrayList<>();
        }

        savedItems.add(item);
    }

    public void addSavedItems(ArrayList<T> list) {
        if (savedItems == null) {
            savedItems = new ArrayList<>();
        }

        savedItems.addAll(list);
    }

    public void clear() {
        if (items != null) {
            items.clear();
        }

        if (savedItems != null) {
            savedItems.clear();
        }
    }
}