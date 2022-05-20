//package com.example.myapplication;
//
//import android.os.Bundle;
//import android.widget.Toast;
//
//import androidx.preference.CheckBoxPreference;
//import androidx.preference.PreferenceFragmentCompat;
//import androidx.preference.PreferenceManager;
//import androidx.preference.PreferenceScreen;
//
//import java.io.File;
//
//import engine.work.EngineOptions;
//import engine.work.UCIEngine;
//import engine.work.UCIEngineBase;
//
//public class ChessEngineDetailsPreferences extends PreferenceFragmentCompat {
//
//    private static String engineLogDir = "DroidFish/uci/logs";
//    private EngineOptions engineOptions = new EngineOptions();
//    UCIEngine uciEngine;
//
//    @Override
//    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//        addPreferencesFromResource(R.xml.engine_setting_details);
//        PreferenceScreen pScreen = getPreferenceManager().createPreferenceScreen(getContext());
//        CheckBoxPreference cb = new CheckBoxPreference(getContext());
//        cb.setKey("cb");
//        cb.setTitle("dfsdfsdf");
//        pScreen.addPreference(cb);
//        setPreferenceScreen(pScreen);
//
//    }
//
//    private synchronized void  checkConnection()  {
//         String mString = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getString("engineList", "");
//        if (mString.equals( "")||mString.equals("1")||mString.equals("0")) {
//            mString = "stockfish";
//        }
//        // Toast.makeText(getActivity().getApplicationContext(), "Текущий движок "+mString, Toast.LENGTH_LONG).show();
////        UCIEngine uciEngine = null;
//        String sep = File.separator;
//        engineOptions.workDir = getContext().getExternalFilesDir(null) + sep + engineLogDir;
//        // engineOptions.workDir = Environment.getExternalStorageDirectory() + sep + engineLogDir;
//        uciEngine = UCIEngineBase.getEngine(mString,
//                engineOptions,
//                errMsg -> {
//                    if (errMsg == null)
//                        errMsg = "";
//                    new UCIEngine.Report() {
//                        @Override
//                        public void reportError(String errMsg) {
//                            Toast.makeText(getActivity().getApplicationContext(), errMsg, Toast.LENGTH_LONG).show();
//                            return;
//                        }
//                    };
//                }, getContext());
//        uciEngine.initialize();
//        final UCIEngine uci = uciEngine;
//        uciEngine.clearOptions();
//        uciEngine.writeLineToEngine("uci");
//        String lineInfo = "1";
//
//        while ((lineInfo != null & !lineInfo.equals("uciok"))) {
//            lineInfo = uci.readLineFromEngine(50);
//
//        }
//
//        uciEngine.writeLineToEngine("quit");
//        uciEngine.readLineFromEngine(50);
//    }
//
//    private void createNewPreferences(String lineInfo){
//        String[] setting = lineInfo.split(" ");
//    }
//}
