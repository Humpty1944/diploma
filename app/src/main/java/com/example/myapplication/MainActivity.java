package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import chessModel.ChessHistory;
import chessModel.ChessHistoryStep;
import chessModel.ChessMan;
import chessModel.ChessModel;
import chessModel.Player;
import chessModel.Square;
import chessPiece.Piece;
//import engine.EngineUtil;
import engine.EngineUtil;
//import engine.UciHelper;
import engine.work.EngineOptions;
//import engine.work.SearchListener;
import engine.work.UCIEngine;
import engine.work.UCIEngineBase;
import interfaces.ChessHistoryInterfaceCallbacks;
import interfaces.ChessInterface;
import openings.Opening;
import openings.OpeningsManager;
import pgn.PgnManager;
import views.ChessBoardView;
import views.ChessHistoryAdapter;
import views.DownloadOptionsView;
import views.FenView;
import views.PromotionView;

import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.google.android.material.navigation.NavigationView;
//import engine.controller.DroidChessController;

//import com.example.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements ChessInterface, ChessAnalysisFragment.OnDataPass,
        DownloadOptionsView.ChooseOptionListener, FenView.FenDialogListener {
   // SearchListener listener;
    ChessModel chessModel = new ChessModel();
    private List<ChessHistoryStep> chessHistory = new ArrayList<>();
//    @Override
//    public void onDataPass(List<ChessHistoryStep> data) {
//        chessHistory=data;
//    }
@Override
public void onDataPass(ChessModel data) {
    chessHistory=data.getChessHistorySteps();
    this.chessModel=data;
}

    @Override
    public void onDialogOptionClick(int which) {
        if (which==0){
            mGetContent.launch("application/x-chess-pgn");
        }
        else{
            FenView fenView = new FenView();
            fenView.show(getSupportFragmentManager(), "fenload");
        }
    }

    @Override
    public void onDialogFenData(String fen) {
        getSupportActionBar().setTitle("Анализ");
        Bundle bundle = new Bundle();
        bundle.putBoolean("load", false);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // ChessAnalysisFragment chessAnalysisFragment = new ChessAnalysisFragment();
        ChessAnalysisFragment chessAnalysisFragment = new ChessAnalysisFragment(openingsList, fen);
        chessAnalysisFragment.setArguments(bundle);
        ft.replace(R.id.your_placeholder, chessAnalysisFragment);

        ft.commit();
    }


    private enum PermissionState {
        UNKNOWN,
        REQUESTED,
        GRANTED,
        DENIED
    }
    private static String engineDir = "DroidFish/uci";
    private static String engineLogDir = "DroidFish/uci/logs";
    int wrapContent = ConstraintLayout.LayoutParams.WRAP_CONTENT;

    //DroidChessController ctrl = null;
    public MainActivity() {
        super();
        appContext = this;
    }
    TextView text;
    RecyclerView recyclerView;
    private ChessHistoryAdapter mAdapter;

//    private Thread engineMonitor;
//    private EngineOptions engineOptions = new EngineOptions();
    private PermissionState storagePermission = PermissionState.UNKNOWN;
    private PermissionState cameraPermission = PermissionState.UNKNOWN;
    // Used to load the 'myapplication' library on application startup.
    PromotionView view;
    ConstraintLayout layout;
    ConstraintLayout.LayoutParams lParams = new ConstraintLayout.LayoutParams( wrapContent, wrapContent);
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    ActivityResultLauncher<String> mGetContent;
    ActivityResultLauncher<Uri> mGetPhoto;
    ActivityResultLauncher<Intent> mPhoto;
    private NavigationView navView;
    static {
        System.loadLibrary("nativeutil");
    }
    static final int CAMERA_ACTION_CODE = 1;
    private static Context appContext;
    private FrameLayout frameLayout;
    private Context context;
    PgnManager pgnManager;
    List<Opening> openingsList;
    private OpeningsManager openingsManager;
    // private ActivityMainBinding binding;
   public static Context getContext() {
       return appContext;
   }
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         pgnManager = new PgnManager(this);
         openingsManager = new OpeningsManager(this);
        frameLayout = (FrameLayout)findViewById(R.id.your_placeholder);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayoutId);
        navView = (NavigationView)findViewById(R.id.navView);
        context =this.getApplicationContext();
        openingsList = openingsManager.readFiles();
      //  permissionCamera();
        getSupportActionBar().setTitle("Анализ");
        mGetContent=registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {

                      ChessModel b=  pgnManager.readFileExternal(result);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ChessAnalysisFragment chessAnalysisFragment = new ChessAnalysisFragment(b,openingsList);
                       // ft.replace(R.id.your_placeholder, new ChessAnalysisFragment());
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("load", true);
                        chessAnalysisFragment.setArguments(bundle);
                        List<Fragment> fragmentList = getFragmentManager().getFragments();
                       // getFragmentManager().getFragments().set(fragmentList.size()-1, null);

                        ft.replace(R.id.your_placeholder, chessAnalysisFragment, "chessLoadExternal");
                       // frameLayout.setElevation(5);
                        ft.commit();
                       // Toast.makeText(context, (CharSequence) result,Toast.LENGTH_LONG).show();
                    }
                });

                createDirectories();
//        engineList
       // String mString = PreferenceManager.getDefaultSharedPreferences(this).getString("engineList", "0"); //getBoolean will return defaultValue is key isn't found

        if(savedInstanceState==null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ChessAnalysisFragment chessAnalysisFragment = new ChessAnalysisFragment(openingsList);
            ft.add(R.id.your_placeholder,chessAnalysisFragment, null);
            ft.commit();
        }
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                if(item.getItemId()==R.id.plays){
                    List<Fragment> fragmentList = getFragmentManager().getFragments();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    getSupportActionBar().setTitle("Партии");
                    ft.replace(R.id.your_placeholder, new PlaysTable(), "plays");
                    //frameLayout.setElevation(5);
                    ft.commit();

                }else if(item.getItemId()==R.id.analysis){
                    try {
                        getSupportActionBar().setTitle("Анализ");
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("load", false);

                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        // ChessAnalysisFragment chessAnalysisFragment = new ChessAnalysisFragment();
                        ChessAnalysisFragment chessAnalysisFragment = new ChessAnalysisFragment(openingsList);
                        chessAnalysisFragment.setArguments(bundle);
                        ft.replace(R.id.your_placeholder, chessAnalysisFragment, "analysis");

                        ft.commit();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else if(item.getItemId()==R.id.openings){
                    getSupportActionBar().setTitle("Дебюты");
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.your_placeholder, new OpeningsTableFragment(), "opening");

                    ft.commit();
                }else if(item.getItemId()==R.id.upload) {
                   // FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    //ft.replace(R.id.your_placeholder, new PlaysTable());
                    if(chessHistory.size()==0){
                        Toast.makeText(getContext(), "Данные о текущей игре отсутсвуют", Toast.LENGTH_LONG).show();
                    }else {
                        pgnManager.saveGame((ArrayList<ChessHistoryStep>) chessHistory, chessModel.getFen());
                    }
                   // frameLayout.setElevation(5);
                   // ft.commit();
                }else if(item.getItemId()==R.id.download){
                    DownloadOptionsView optionsView = new DownloadOptionsView();
                    optionsView.show(getSupportFragmentManager(), "options");
                   // mGetContent.launch("application/x-chess-pgn");
                }
                else if (item.getItemId()==R.id.engine){
                    getSupportActionBar().setTitle("Настройки");
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.your_placeholder, new ChessEngineSettingsFragment(), "settingEngine");

                    ft.commit();
                }else if(item.getItemId()==R.id.boardEditor){
                    getSupportActionBar().setTitle("Редактор доски");
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.your_placeholder, new ChessCreateBoardFragment(), "createBoard");

                    ft.commit();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    public void replaceFragment(Fragment fragment, Bundle bundle) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

   public void changeFragments(Object... objects){
       try {
           MasterPlays plays;

           String folderName = (String) objects[0];

           plays = new MasterPlays(folderName);

           FragmentManager fragmentManager = getSupportFragmentManager();
           fragmentManager.beginTransaction().replace(R.id.your_placeholder, plays, "masterPlays")
                   .commit();
       }catch (Exception e){  e.printStackTrace();}


   }

   public void changeToPlay(Set<Piece> pieces){
       Player currPlayer = ChessHistory.currPlayer;
       ChessModel model = new ChessModel();
       model.setPieceBox(pieces);
       model.setCurrPlayer(currPlayer);
       ChessHistory.currPlayer = currPlayer;
       if(currPlayer==Player.BLACK){
           model.setChessHistorySteps(new ChessHistoryStep());
       }
       ChessAnalysisFragment chessAnalysisFragment = new ChessAnalysisFragment(model, openingsList);
       FragmentManager fragmentManager = getSupportFragmentManager();
       fragmentManager.beginTransaction().replace(R.id.your_placeholder, chessAnalysisFragment, "chesAnalysisLoadFen")
               .commit();
   }

    public void changeFragment(byte fragmentType, ChessModel model, Opening opening){
       if(fragmentType==0){
           Bundle bundle = new Bundle();
           bundle.putBoolean("load", true);

           FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
          // ChessAnalysisFragment chessAnalysisFragment = new ChessAnalysisFragment(model);
           ChessAnalysisFragment chessAnalysisFragment = new ChessAnalysisFragment(model,openingsList);
           chessAnalysisFragment.setArguments(bundle);
           ft.replace(R.id.your_placeholder, chessAnalysisFragment,"chesAnalysisLoad");

           ft.commit();
       }
       else if (fragmentType==1){
           FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
           ChessOpeningFragment chessAnalysisFragment = new ChessOpeningFragment(opening);

           ft.replace(R.id.your_placeholder, chessAnalysisFragment,"openingDetail");

           ft.commit();

       }
    }

    private void createDirectories() {
        if (storagePermission == PermissionState.UNKNOWN) {
            String extStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            if (ContextCompat.checkSelfPermission(this, extStorage) ==
                    PackageManager.PERMISSION_GRANTED) {
                storagePermission = PermissionState.GRANTED;
            } else {
                storagePermission = PermissionState.REQUESTED;
                ActivityCompat.requestPermissions(this, new String[]{extStorage}, 0);
            }
        }
        if (storagePermission != PermissionState.GRANTED)
            return;

        File extDir = Environment.getExternalStorageDirectory();
        File mediaStorageDir = appContext.getExternalFilesDir(null);
        String sep = File.separator;

        new File(mediaStorageDir + sep + engineDir).mkdirs();
        new File(mediaStorageDir + sep + engineDir + sep + EngineUtil.openExchangeDir).mkdirs();

       boolean f =  new File(mediaStorageDir + sep + engineLogDir).mkdirs();
//       f.mkdirs();
//       chmod(f);

    }
    private void chmod(String exePath) throws IOException {
        if (!EngineUtil.chmod(exePath))
            throw new IOException("chmod failed");
    }
    /**
     * A native method that is implemented by the 'myapplication' native library,
     * which is packaged with this application.
     */
//    public native String stringFromJNI();
    @Override
    public Piece pieceAt(Square square) {
        return chessModel.pieceAt(square);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void movePiece(Square from, Square to) {
        chessModel.movePiece(from, to);
        ChessBoardView chessView=findViewById(R.id.chessBoard);
        chessView.invalidate();
    }

    @Override
    public void promotionView(Player player,Piece piece) {

        view.setPlayer(player);
        view.setCurrPiece(piece);
        layout.addView(view, lParams);
    }

    @Override
    public void promotePawn(ChessMan chessMan, Piece piece) {

        if (chessMan==null||piece==null){
            view.setVisibility(View.GONE);
        }else {
            chessModel.promotion(piece, chessMan);
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateHistory(boolean isNext, ChessHistoryStep step, int block) {
        if (isNext){
            chessHistory.add(step);
            mAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(chessHistory.size()-1);
        }
        else{
            for(int i = chessHistory.size()-1; i>=block; i--){
               chessHistory.remove(chessHistory.get(i));
            }
            mAdapter.notifyDataSetChanged();
          //  mAdapter.notifyItemRangeRemoved(block+1, chessHistory.size()-block );
            recyclerView.smoothScrollToPosition(chessHistory.size()-1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void backMovePiece(ChessHistoryStep move) {

        for(int i=chessHistory.size()-1;i>chessHistory.indexOf(move);i--){
            chessModel.backMovePiece(chessHistory.get(i));
        }
        ChessBoardView chessView=findViewById(R.id.chessBoard);
        chessView.invalidate();
    }

    @Override
    public void displayCurrOpening(Opening opening) {

    }

    @Override
    public void sendAndDisplayAnalysis(int block) {

    }

    @Override
    public void updatePromotion(ChessHistoryStep step) {

    }


//    public synchronized void  updateText(String textData){
//      HashMap<String,String> res =  UciHelper.getInfoResult(textData);
//      if (res.size()>1) {
//          text.setText(textData);
//      }
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }
}