package com.winning.mobileclinical.dialog;

import java.util.ArrayList;
import java.util.List;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.model.cis.DeptWardMapInfo;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

public class SelectDialog extends AlertDialog{

public SelectDialog(Context context, int theme) {
    super(context, theme);
}

public SelectDialog(Context context) {
    super(context);
}

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.test);
}
}
