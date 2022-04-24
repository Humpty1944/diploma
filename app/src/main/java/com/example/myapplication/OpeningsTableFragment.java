package com.example.myapplication;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import chessModel.ChessHistoryStep;
import chessModel.ChessModel;
import interfaces.ChessHistoryInterfaceCallbacks;
import openings.Opening;
import openings.OpeningsManager;
import views.OpeningAdapter;

public class OpeningsTableFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    View view;

    List<Opening> fileNames = new ArrayList<>();
    private OpeningAdapter mAdapter;
    ConstraintLayout layout;
    RecyclerView recyclerView;
    int wrapContent = ConstraintLayout.LayoutParams.WRAP_CONTENT;
    ConstraintLayout.LayoutParams lParams = new ConstraintLayout.LayoutParams(wrapContent, wrapContent);
    SearchView searchView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.openings_table, container, false);
        layout = (ConstraintLayout) view.findViewById(R.id.layoutIdOpen);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewOpen);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        populateWithPlays();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        // populateWithPlays();
    }

    private void populateWithPlays() {
        OpeningsManager openingsManager = new OpeningsManager(getActivity().getApplicationContext());
        fileNames = openingsManager.readFiles();
        mAdapter = new OpeningAdapter(fileNames, new ChessHistoryInterfaceCallbacks() {
            @Override
            public void onClick(Object object) {
                searchView.clearFocus();
                MainActivity mainActivity = (MainActivity) getActivity();
                byte type = 1;
                Opening opening = (Opening) object;
                mainActivity.changeFragment(type, null, opening);
            }
        });


        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.opening_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
         searchView = new SearchView(((MainActivity) getActivity()).getSupportActionBar().getThemedContext());
        // MenuItemCompat.setShowAsAction(item, //MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | //MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        //  MenuItemCompat.setActionView(item, searchView);
        // These lines are deprecated in API 26 use instead
        //item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setShowAsAction( MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setActionView(searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null || newText.trim().isEmpty()) {
                    resetSearch();
                    return false;
                }

                List<Opening> filteredValues = new ArrayList<Opening>(fileNames);
                for (Opening value : fileNames) {
                    if(!value.getName().toLowerCase().contains(newText.toLowerCase())&&!value.getNumber().toLowerCase().contains(newText.toLowerCase())){
                        filteredValues.remove(value);
                    }

                }
                mAdapter.setOpeningList(filteredValues);
                mAdapter.notifyDataSetChanged();
                return false;
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {

                                          }
                                      }
        );
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (s == null || s.trim().isEmpty()) {
            resetSearch();
            return false;
        }

        List<Opening> filteredValues = new ArrayList<Opening>(fileNames);
        for (Opening value : fileNames) {
            if(!value.getName().contains(s.toLowerCase())||!value.getNumber().contains(s.toLowerCase())){
                filteredValues.remove(value);
            }

        }
        mAdapter.setOpeningList(filteredValues);
        mAdapter.notifyDataSetChanged();
//        mAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, filteredValues);
//        setListAdapter(mAdapter);
        return false;
    }
    public void resetSearch() {

        mAdapter.setOpeningList(fileNames);
        mAdapter.notifyDataSetChanged();

    }
}


//        for (int i = 0; i < fileNames.size(); i++) {
//            //String[] info = manager.readFileNameAndDate(fileNames[i]);
//            TableRow row = new TableRow(getActivity().getApplicationContext());
//
//            if (i % 2 == 0) {
//                row.setBackgroundColor(Color.GRAY);
//            } else {
//                row.setBackgroundColor(Color.WHITE);
//            }
//            row.setMinimumHeight(200);
//            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//            row.setLayoutParams(lp);
//            TextView textView = new TextView(getActivity().getApplicationContext());
//            textView.setText(fileNames.get(i).getNumber()+"\n"+fileNames.get(i).getName());
//            textView.setWidth(490);
//            textView.setTextSize(15);
//            textView.setSingleLine(false);
//            textView.setLayoutParams(lp);
//            row.addView(textView);
//
//
//            tableLayout.addView(row, i);
//
//
//            int finalI1 = i;
//            row.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    MainActivity mainActivity = (MainActivity) getActivity();
//                    byte type = 1;
//                    mainActivity.changeFragment(type, null, fileNames.get(finalI1));
//                }
//            });
//        }
