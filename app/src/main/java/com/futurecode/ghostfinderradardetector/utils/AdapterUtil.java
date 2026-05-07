package com.futurecode.ghostfinderradardetector.utils;

import androidx.annotation.Nullable;

import java.util.List;

public interface AdapterUtil<type> {
    void set(List<type> list);
    void add(List<type> list);
    void add(type item, int position);
    void add(type item);
    @Nullable
    type get(int position);

}