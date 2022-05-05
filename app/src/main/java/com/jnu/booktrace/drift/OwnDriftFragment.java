package com.jnu.booktrace.drift;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jnu.booktrace.MainActivity;
import com.jnu.booktrace.R;
import com.jnu.booktrace.database.DBManager;


public class OwnDriftFragment extends BaseDriftFragment {

    @Override
    public void loadDatas() {
        setmDatas(DBManager.GetOwnDriftById(MainActivity.person.getName()));
        Log.i("用户id", MainActivity.person.getId()+"");
    }
}