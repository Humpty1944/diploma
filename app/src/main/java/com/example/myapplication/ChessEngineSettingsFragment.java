package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;

import chessModel.ChessHistory;
import chessModel.ChessModel;
import openings.Opening;

public class  ChessEngineSettingsFragment extends Fragment
{


    View view;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings, container, false);
       getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new ChessEngineSettingsPrefernces(this))
                .commit();
        return view;
    }
//    public void changePreferences(){
//        getActivity().getSupportFragmentManager()
//                .beginTransaction().addToBackStack("DetailSetting")
//                .replace(R.id.settings_container, new ChessEngineDetailsPreferences())
//                .commit();
//
//    }

//    public ChessOpeningFragment() {
//        super(R.layout.chess_opening);
//    }
//    public ChessOpeningFragment(Opening opening) {
//        super(R.layout.chess_opening);
////        this.opening=opening;
//
//    }


//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.chess_opening, container, false);
////        chessView =  view.findViewById(R.id.chessBoardOpening);
////        name=view.findViewById(R.id.text_view_opening_name);
////        eco=view.findViewById(R.id.text_view_opening_name_code);
////        steps=view.findViewById(R.id.text_view_opening_steps);
//        return view;
//    }




}
