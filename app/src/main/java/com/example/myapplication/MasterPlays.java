package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import chessModel.ChessModel;
import interfaces.ChessHistoryInterfaceCallbacks;
import pgn.PgnManager;
import views.GamesAdapter;
import views.MasterAdapter;

public class MasterPlays extends Fragment {
    String folderName;

    View view;
    TableLayout tableLayout;
    // private GamesAdapter mAdapter;
    GamesAdapter mAdapter;
    ConstraintLayout layout;
    RecyclerView recyclerView;
    int wrapContent = ConstraintLayout.LayoutParams.WRAP_CONTENT;
    ConstraintLayout.LayoutParams lParams = new ConstraintLayout.LayoutParams(wrapContent, wrapContent);
    public MasterPlays(String folderName){
        this.folderName=folderName;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.master_games_table, container, false);
        layout = (ConstraintLayout) view.findViewById(R.id.layoutIdMaster);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewMasterGames);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        try {
            populateWithPlays();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void populateWithPlays() throws IOException {
        PgnManager manager = new PgnManager(getActivity().getApplicationContext());
        String corrFolderName = folderName.replace(" ","_");
        String[] fileNames = manager.readDirContent("games/"+corrFolderName);
        List<String[]> fileInfo = new ArrayList<>();
        for(int i=0;i<fileNames.length;i++){
          String[] content = manager.readFileNameAndDate(corrFolderName+"/"+fileNames[i]);
          fileInfo.add(content);
        }

        mAdapter = new GamesAdapter(fileInfo, new ChessHistoryInterfaceCallbacks() {
            @Override
            public void onClick(Object object) throws Exception {
                MainActivity mainActivity = (MainActivity) getActivity();
                int pos = (Integer) object;
                byte type=0;
//                try {
                    ChessModel model = manager.readFile(corrFolderName+"/"+fileNames[pos]);
                   // mainActivity.changeFragment(type, model,null);
                    if(model==null){
                        Toast.makeText(getContext(), "Файл нельзя загрузить из-за ошибки в тексте.", Toast.LENGTH_LONG).show();
                    }else {
                        mainActivity.changeFragment(type, model,null);
               // mainActivity.changeFragments(object);
                    }
//                }
//               catch (Exception e){
//                    Toast.makeText(getContext(), "Файл нельзя загрузить из-за ошибки в тексте.", Toast.LENGTH_LONG).show();
//                }
            }
        });
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }
}
