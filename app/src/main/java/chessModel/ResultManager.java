//package chessModel;
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.widget.Button;
//import android.widget.TextView;
//
//public class ResultManager extends AsyncTask<Void, String, Void> {
//    private Context c;
//    private TextView b;
//    private boolean isUpdate = false;
//    private String text;
//    public ResultManager(Context c, Button b) {
//        this.c = c;
//        this.b = b;
//    }
//
//    protected Void doInBackground(Void... params) {
//        if (isUpdate){
//            publishProgress(text);
//        }
//        // some work
////      work  if (isSomethingConnected) {
////            publishProgress(Constants.IS_CONNECTED);
////        }IS_CONNECTED
//        return null;
//    }
//
//    public void onProgressUpdate(String... params) {
//        b.setText(params[0]);
////        switch (params[0]) {
////            case Constants.IS_CONNECTED:
////                b.setText("Connected");
////                break;
////            case Constants.ANOTHER_CONSTANT:
////                // another work
////                break;
////        }
//    }
//
//    public boolean isUpdate() {
//        return isUpdate;
//    }
//
//    public void setUpdate(boolean update, String text) {
//        isUpdate = update;
//        this.text=text;
//    }
//}
