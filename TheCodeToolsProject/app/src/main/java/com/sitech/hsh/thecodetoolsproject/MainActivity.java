package com.sitech.hsh.thecodetoolsproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sitech.hsh.thecodetoolsproject.tools.floatpermission.RomUtilsEx;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RomUtilsEx.applyPermission(this);
    }
}