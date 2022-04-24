package com.example.myapplication;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import chessModel.ChessModel;
import engine.work.EngineOptions;
import engine.work.UCIEngine;
import engine.work.UCIEngineBase;
import helpClass.TinyDB;

public class ChessEngineSettingsPrefernces  extends PreferenceFragmentCompat {
    ActivityResultLauncher<String> mGetContent;
    String depth="20";
    private static String engineLogDir = "DroidFish/uci/logs";
    private EngineOptions engineOptions = new EngineOptions();
    UCIEngine uciEngine;
    ChessEngineSettingsFragment chessAnalysisFragment;
    public  ChessEngineSettingsPrefernces(ChessEngineSettingsFragment fragment){
        chessAnalysisFragment=fragment;
    }
    public  ChessEngineSettingsPrefernces(){
        chessAnalysisFragment=null;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.engine_settings);
        ListPreference refreshPref = (ListPreference) findPreference("engineList");
        AssetManager assetManager = getActivity().getAssets();

        try {
            TinyDB tinyDB = new TinyDB(getContext());
            ArrayList<String> names = tinyDB.getListString("externalEngine");
            ArrayList<String> namesOnly =new  ArrayList<>();
            for (int i=0;i<names.size();i++){
                if(!names.get(i).equals("")||names.get(i).equals(" ")) {
                    int len = names.get(i).split("/").length;
                    namesOnly.add(names.get(i).split("/")[len - 1]);
                }
            }

            String[] list = assetManager.list("arm64-v8a");
            //namesOnly.addAll(Arrays.asList(list));
            String[] res =new String[list.length+namesOnly.size()];
            for(int i=0;i<list.length;i++){
                res[i]=list[i];
            }
            int j=0;
            for(int i=list.length;i<res.length;i++){
                res[i]=namesOnly.get(j);
                j++;
            }
            System.out.println("fdgdfgfdgfd");
            refreshPref.setEntries(res);
            refreshPref.setEntryValues(res);
            //refreshPref.setDefaultValue(res[res.length-1]);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //addPreferencesFromResource(R.xml.engine_settings);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListPreference refreshPref = (ListPreference) findPreference("engineList");
        Preference chooseEngine = findPreference("newEngine");
        AssetManager assetManager = getActivity().getAssets();
        ProgressBar progressBar = chessAnalysisFragment.view.findViewById(R.id.SHOW_PROGRESS);
        FragmentContainerView fragmentContainerView = chessAnalysisFragment.view.findViewById(R.id.settings_container);
        final String[] pathHelp = {""};
        mGetContent=registerForActivityResult(new ActivityResultContracts.GetContent(),

                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {

                        Thread startupThread = new Thread(() -> {
                            try {
                                String path = createCopyAndReturnRealPath(getContext(), result);
                                try {
                                    checkConnection(path);
                                    pathHelp[0] =path;
                                }catch (Exception e){
                                    pathHelp[0]="1";
                                    Thread.currentThread().interrupt();

                                }

                            } catch (Exception e) {
                                System.out.println(e.toString());
                            }
                        });
                        progressBar.setVisibility(View.VISIBLE);
                        fragmentContainerView.setVisibility(View.GONE);
                        try {
                        startupThread.start();

                            startupThread.join();
                            if(pathHelp[0]=="1"){
                                Toast.makeText(getContext(),"Данный объект не может работать на данном устройстве", Toast.LENGTH_LONG).show();

                            }else{
                                Toast.makeText(getContext(),"Загрузка завершена", Toast.LENGTH_LONG).show();
                                TinyDB tinydb = new TinyDB(getContext());
                                ArrayList<String> paths = tinydb.getListString("externalEngine");
                                paths.add(pathHelp[0]);
                                tinydb.putListString("externalEngine", paths);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            //Toast.makeText(getContext(),"Данный объект не может работать на данном устройстве или не является шахматным движком", Toast.LENGTH_LONG).show();
                        }

                        progressBar.setVisibility(View.GONE);
                        fragmentContainerView.setVisibility(View.VISIBLE);

                    }
                });
        try {
            TinyDB tinyDB = new TinyDB(getContext());
            ArrayList<String> names = tinyDB.getListString("externalEngine");
            ArrayList<String> namesOnly =new  ArrayList<>();
            for (int i=0;i<names.size();i++){
                if(!names.get(i).equals("")||names.get(i).equals(" ")) {
                    int len = names.get(i).split("/").length;
                    namesOnly.add(names.get(i).split("/")[len - 1]);
                }
            }

            String[] list = assetManager.list("arm64-v8a");
            //namesOnly.addAll(Arrays.asList(list));
            String[] res =new String[list.length+namesOnly.size()];
            for(int i=0;i<list.length;i++){
                res[i]=list[i];
            }
            int j=0;
            for(int i=list.length;i<res.length;i++){
                res[i]=namesOnly.get(j);
                j++;
            }
            System.out.println("fdgdfgfdgfd");
            refreshPref.setEntries(res);
            refreshPref.setEntryValues(res);
//            CharSequence[] list = assetManager.list("arm64-v8a");
//            refreshPref.setEntries(list);
            chooseEngine.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {

                    mGetContent.launch("*/*");
                    // open browser or intent here
                    return true;
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Nullable
    public synchronized  String createCopyAndReturnRealPath(
            @NonNull Context context, @NonNull Uri uri) {
        final ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null)
            return null;

        // Create file path inside app's data dir
        String filePath = context.getApplicationInfo().dataDir + File.separator + uri.getPathSegments().get(1).split("/")[1];
        File file = new File(filePath);
        try {
            InputStream inputStream = contentResolver.openInputStream(uri);
            if (inputStream == null)
                return null;
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0)
                outputStream.write(buf, 0, len);
            outputStream.close();
            inputStream.close();
        } catch (IOException ignore) {
            return null;
        }
        return file.getAbsolutePath();
    }
    private synchronized void  checkConnection(String mString) throws Exception {
       // String mString = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getString("engineList", "");
        depth =  PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getString("depth", "20");
        if (mString.equals( "")||mString.equals("1")||mString.equals("0")) {
            mString = "stockfish";
        }
       // Toast.makeText(getActivity().getApplicationContext(), "Текущий движок "+mString, Toast.LENGTH_LONG).show();
//        UCIEngine uciEngine = null;
        String sep = File.separator;
        engineOptions.workDir = getContext().getExternalFilesDir(null) + sep + engineLogDir;
        // engineOptions.workDir = Environment.getExternalStorageDirectory() + sep + engineLogDir;
        uciEngine = UCIEngineBase.getEngine(mString,
                engineOptions,
                errMsg -> {
                    if (errMsg == null)
                        errMsg = "";
                    new UCIEngine.Report() {
                        @Override
                        public void reportError(String errMsg) {
                            Toast.makeText(getActivity().getApplicationContext(), errMsg, Toast.LENGTH_LONG).show();
                            return;
                        }
                    };
                }, getContext());
        uciEngine.initialize();
        final UCIEngine uci = uciEngine;
        uciEngine.clearOptions();
        uciEngine.writeLineToEngine("uci");
        String lineInfo = "1";
        int count=0;
        while ((lineInfo != null & !lineInfo.equals("uciok"))&&count<15) {
            lineInfo = uci.readLineFromEngine(50);
            count++;
        }
        if(count>=15){
            throw new Exception();
        }
        uciEngine.writeLineToEngine("quit");
        uciEngine.readLineFromEngine(50);
    }
}
