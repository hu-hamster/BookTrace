package com.jnu.booktrace.drift;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jnu.booktrace.MainActivity;
import com.jnu.booktrace.R;
import com.jnu.booktrace.database.DBManager;
import com.jnu.booktrace.database.DatabaseManager;


public class OtherDriftFragment extends BaseDriftFragment {

    @Override
    public void loadDatas() {
        setmDatas(DBManager.GetOtherDrift(MainActivity.person.getName()));
    }

}