package com.jnu.booktrace.drift;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jnu.booktrace.R;
import com.jnu.booktrace.adapter.DriftAdapter;
import com.jnu.booktrace.bean.Drift;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseDriftFragment extends Fragment {
    private List<Drift> mDatas;
    private RecyclerView recyclerView;
    private DriftAdapter driftAdapter;
    private int[] colors;
    public BaseDriftFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_drift, container, false);
        mDatas = new ArrayList<>();
        loadDatas();
        recyclerView = view.findViewById(R.id.Base_drift_recycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        driftAdapter = new DriftAdapter(this.getContext(),mDatas);
        recyclerView.setAdapter(driftAdapter);
        return view;
    }

    abstract public void loadDatas();

    public List<Drift> getmDatas() {
        return mDatas;
    }

    public void setmDatas(List<Drift> mDatas) {
        this.mDatas = mDatas;
    }
}