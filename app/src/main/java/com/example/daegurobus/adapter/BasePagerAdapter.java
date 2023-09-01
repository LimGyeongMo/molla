package com.example.daegurobus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class BasePagerAdapter<T> extends PagerAdapter {
    protected Context context;
    protected LayoutInflater layoutInflater;
    protected ArrayList<T> items;
    protected OnItemClickListener onItemClickListener;
    protected int currentPage;

    public BasePagerAdapter(Context context, LayoutInflater layoutInflater) {
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        if (items != null) {
            return items.size();
        }

        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void initItems(ArrayList<T> list) {
        if (items == null) {
            items = new ArrayList<>();
        } else {
            items.clear();
        }

        if (list != null) {
            items.addAll(list);
        }

        notifyDataSetChanged();
    }

    public void setItem(T item) {
        if (items == null) {
            items = new ArrayList<>();
        } else {
            items.clear();
        }

        if (item != null) {
            items.add(item);
        }

        notifyDataSetChanged();
    }

    public void addItems(T item) {
        if (items == null) {
            items = new ArrayList<>();
        }

        if (item != null) {
            items.add(item);
        }

        notifyDataSetChanged();
    }

    public void addItems(ArrayList<T> list) {
        if (items == null) {
            items = list;
        } else {
            items.addAll(list);
        }

        notifyDataSetChanged();
    }

    public ArrayList<T> getItems() {
        if (items != null) {
            return items;
        }

        return null;
    }

    public T getItem(int position) {
        if (items != null) {
            return items.get(position);
        }

        return null;
    }

    public void clear() {
        if (items != null) {
            items.clear();
        } else {
            items = new ArrayList<>();
        }

        notifyDataSetChanged();
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
