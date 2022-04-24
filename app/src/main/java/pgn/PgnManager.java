package pgn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.transition.Visibility;

import com.example.myapplication.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import chessModel.ChessHistory;
import chessModel.ChessHistoryStep;
import chessModel.ChessMan;
import chessModel.ChessModel;
import chessModel.Player;
import chessModel.Square;
import chessPiece.King;
import chessPiece.Piece;

public class PgnManager {
    private String path;
    private Context context;

    public PgnManager(String path, Context context) {
        this.path = path;
        this.context = context;
    }

    public PgnManager(Context context) {
        this.context = context;
    }

    public String[] getAllFilesName() throws IOException {
        AssetManager assetManager = context.getAssets();
        return assetManager.list("games");
    }

    public String[] readFileNameAndDate(String fileName) {
        BufferedReader reader = null;
        String res = "";
        PgnGame game = new PgnGame();
        System.out.println(context.getAssets());
        // InputStream assetInStream=null;
        try {
            // AssetManager   assetInStream=context.getAssets().;
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("games/" + fileName), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                if (!game.populateGame(mLine)) {
                    break;

                }
            }

        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return new String[]{game.getEvent(), game.getDate(), game.getWhite(), game.getBlack()};
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public ChessModel readFile(String fileName) {
        BufferedReader reader = null;
        String res = "";
        PgnGame game = new PgnGame();
        ChessModel model=null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("games/" + fileName), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                if (!game.populateGame(mLine)) {
                    res += mLine+" ";

                }
            }
            model = gameData(res);
            model.setGameName(game.getEvent()+". "+game.getBlack()+" vs. "+game.getWhite());
        } catch (Exception e) {
            System.out.println(e);
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    return model;
                } catch (IOException e) {
                    System.out.println(e);
                    //log the exception
                }
            }
        }
        return model;
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    public ChessModel readFileExternal(Uri fileName) {
       BufferedReader reader = null;
        FileInputStream fIn;
        String res = "";
        PgnGame game = new PgnGame();
        File file = new File(fileName.toString());
        ChessModel model=null;
        try {
            InputStream in = context.getContentResolver().openInputStream(fileName);


             reader = new BufferedReader(new InputStreamReader(in));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                if (!game.populateGame(mLine)) {
                    res += mLine+" ";

                }
            }
            model = gameData(res);
            model.setGameName(game.getEvent()+". "+game.getBlack()+" vs. "+game.getWhite());
        } catch (IOException e) {
            System.out.println(e);
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println(e);
                    //log the exception
                }
            }
        }
return model;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private ChessModel gameData(String lines) {
        String newLines = lines.replace("\n", " ");
        newLines = newLines.replace(".", ". ");
        ChessHistory.currCountStep=0;
        ChessHistory.chessHistory= new ArrayList<>();
        ChessHistory.isLoad=false;
        ChessHistory.isCheck=false;
        ChessHistory.isForward=false;
        ChessHistory.currPlayer=Player.WHITE;
        ChessHistory.checkingPiece=null;
        ChessHistory.checkPiece=null;
        int indexFirst = newLines.indexOf("{");
        int indexLast = newLines.indexOf("}");
        while (indexFirst != -1 && indexLast != -1) {
            String toBeReplaced = newLines.substring(indexFirst , indexLast+1);
            newLines = newLines.replace(toBeReplaced, "");
            indexFirst = newLines.indexOf("{");
            indexLast = newLines.indexOf("}");
        }
        newLines = newLines.replace("x","");
        newLines = newLines.replace("+","");
        newLines = newLines.replace("#","");
        //newLines = newLines.replace("1-0","");
        newLines = newLines.replace("!","");
        newLines = newLines.replace("?","");
        String[] newLine = newLines.split(" ");

        ChessModel model = new ChessModel();
        for (int i = 0; i < newLine.length; i++) {
            if(i==62){
                boolean flag=true;
                System.out.println(flag);

            }
            if(newLine[i].contains("1-0")||newLine[i].contains("1-1")||newLine[i].contains("0-0")||newLine[i].contains("0-1")||newLine[i].contains("*")||newLine[i].contains("2-")){
                break;
            }

            if (newLine[i].contains(".")||newLine[i].equals("")||newLine[i].equals(" ")) {
                continue;
            }
           // List<ChessHistoryStep>s =   ChessHistory.chessHistory;
            if (checkCasteling(newLine[i]) != 0) {
                if (newLine[i].length() >= 4) {
                    if (ChessHistory.currCountStep % 2 == 0) {
                        King king = model.getWhiteKing();
                        model.movePiece(king.getSquare(), new Square(king.getCol() - 2, king.getRow()));
                    }else{
                        King king = model.getBlackKing();
                        model.movePiece(king.getSquare(), new Square(king.getCol() - 2, king.getRow()));
                    }
                }else{
                    if (ChessHistory.currCountStep % 2 == 0) {
                        King king = model.getWhiteKing();
                        model.movePiece(king.getSquare(), new Square(king.getCol() + 2, king.getRow()));
                    }else{
                        King king = model.getBlackKing();
                        model.movePiece(king.getSquare(), new Square(king.getCol() + 2, king.getRow()));
                    }
                }
            } else {
                ChessMan chessMan = getPieceType(newLine[i]);
                Square to;
//                if(chessMan==ChessMan.QUEEN){
//                    boolean fl=true;
//                    System.out.println(fl);
//
//
//                }
                String lineWork = newLine[i];
//                String lineWork = newLine[i].replace("x","");
//                lineWork = lineWork.replace("+","");
//                lineWork = lineWork.replace("#","");
//                lineWork = lineWork.replace("1-0","");
//                lineWork = lineWork.replace("!","");
//                lineWork = lineWork.replace("?","");
                int ind = 0;  int send=-1;
                int promInd=-1;
                if (chessMan == ChessMan.KING &&
                        Character.UnicodeBlock.of(lineWork.charAt(0)).equals(Character.UnicodeBlock.CYRILLIC)) {
                     ind = lineWork.length()>=5? 3: 2;
                     send = lineWork.length()>=5? ((char) (lineWork.charAt(2) - '0') - 49): -1;
                    to = fromAlgebraToSquare(lineWork.substring(ind, lineWork.length()));
                } else if (chessMan == ChessMan.PAWN) {
                    ind = lineWork.length()>=3? 1: 0;
                    send = lineWork.length()>=3? ((char) (lineWork.charAt(0) - '0') - 49): -1;
                     promInd = lineWork.indexOf("=");
                    if(promInd!=-1) {
                        to = fromAlgebraToSquare(lineWork.substring(ind, promInd));
                    }else{
                        to = fromAlgebraToSquare(lineWork.substring(ind, lineWork.length()));
                    }

                } else {
                    send = lineWork.length()>=4? ((char) (lineWork.charAt(1) - '0') - 49): -1;
                    ind = lineWork.length()>=4? 2: 1;
                    to = fromAlgebraToSquare(lineWork.substring(ind, lineWork.length()));
                }
                ArrayList<ChessHistoryStep> steps = ChessHistory.chessHistory;
                Piece piece = findPieceCanMove(to, chessMan, model,send );
//                if(ChessHistory.currCountStep!=ChessHistory.chessHistory.size()||ChessHistory.currCountStep==40){
//                    System.out.println("wtf");
//                }
                if(ChessHistory.chessHistory.size()==0){
                    ChessHistory.chessHistory=steps;
                }
                if (piece==null){
                    System.out.println(piece);
                    System.out.println(i);
                    System.out.println(ChessHistory.chessHistory.size());
                    System.out.println(ChessHistory.currCountStep);
                }
                if(piece!=null&&promInd!=-1) {
                    promotionHistory(piece, to, promInd, lineWork, model);
                }
                else if (piece != null) {
                    model.movePiece(piece.getSquare(), to);
                }
                System.out.println(piece);
            }

        }
        ChessHistory.isLoad=true;
        return model;
    }
@RequiresApi(api = Build.VERSION_CODES.R)
private void promotionHistory(Piece piece, Square to, int promInd, String lineWork, ChessModel model){
        ChessMan type = getPieceType(String.valueOf(lineWork.charAt(promInd+1)));

        model.movePiece(piece.getSquare(),to);
    model.promotion(piece,type);
    model.getChessHistorySteps().get(model.getChessHistorySteps().size()-1).setPromotedType(type);
    model.getChessHistorySteps().get(model.getChessHistorySteps().size()-1).setPromoted(true);

}
    private Piece findPieceCanMove(Square to, ChessMan chessMan, ChessModel chessModel, int ind) {
        for (Piece p : chessModel.getPieces()) {
            if (p.getChessMan() == chessMan) {
                if(ind!=-1){
                  //  if (p.canMove(p.getSquare(), to)&&p.getPlayer()==ChessHistory.currPlayer&&p.getCol()==ind) {//check color
                    if(p.canMove(p.getSquare(), to)&&p.getPlayer()==chessModel.getCurrPlayer()&&p.getCol()==ind){
                        return p;
                    }
                }//else if (p.canMove(p.getSquare(), to)&&p.getPlayer()==ChessHistory.currPlayer) {
                else if (p.canMove(p.getSquare(), to)&&p.getPlayer()==chessModel.getCurrPlayer()){// check color
                    return p;
                }
            }
        }
        return null;
    }

    private Square fromAlgebraToSquare(String turn) {
        int indCol = turn.length()>2? 1: 0;
        int indRow = turn.length()>2? 2: 1;
        int col = ((char) (turn.charAt(indCol) - '0') - 49);
        if (Character.isDigit(turn.charAt(1))) {
            return new Square(col, Character.getNumericValue(turn.charAt(indRow)) - 1);
        } else {
            return new Square(col, Character.getNumericValue(turn.charAt(indRow)) - 1);
        }
    }

    private byte checkCasteling(String turn) {
        if (turn.contains("0") || turn.contains("O") || turn.contains("o")) {
            return (byte) turn.length();
        } else {
            return 0;
        }
    }

    private ChessMan getPieceType(String turn) {
        if (Character.UnicodeBlock.of(turn.charAt(0)).equals(Character.UnicodeBlock.CYRILLIC)) {
            if (turn.charAt(0) == 'К') {
                if (turn.charAt(1) == 'р') {
                    return ChessMan.KING;
                } else {
                    return ChessMan.KNIGHT;
                }
            } else if (turn.charAt(0) == 'Ф') {
                return ChessMan.QUEEN;
            } else if (turn.charAt(0) == 'Л') {
                return ChessMan.ROOK;
            } else if (turn.charAt(0) == 'С') {
                return ChessMan.BISHOP;
            } else {
                return ChessMan.PAWN;
            }
        } else {
            if (turn.charAt(0) == 'К'||turn.charAt(0) =='K') {
                return ChessMan.KING;
            } else if (turn.charAt(0) == 'Q') {
                return ChessMan.QUEEN;
            } else if (turn.charAt(0) == 'R') {
                return ChessMan.ROOK;
            } else if (turn.charAt(0) == 'B') {
                return ChessMan.BISHOP;
            } else if (turn.charAt(0) == 'N') {
                return ChessMan.KNIGHT;
            } else {
                return ChessMan.PAWN;
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveGame(ArrayList<ChessHistoryStep> chessHistorySteps){
        Date currentTime = Calendar.getInstance().getTime();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(currentTime);
        String res="";
        res+="[Event \"Training\"]\n";
        res+="[Date \""+calendar.get(Calendar.YEAR)+"."+(calendar.get(Calendar.MONTH) + 1)+"."+calendar.get(Calendar.DAY_OF_MONTH)+"\"]\n";
        res+="[Round \"?\"]\n";
        res+="[Result \"?\"]\n";
        res+="[White \"Player1\"]\n";
        res+="[Black \"Player2\"]\n\n";
        res+=ChessHistory.makeGame(chessHistorySteps);
//        File extDir = Environment.getExternalStorageDirectory();
//        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
//        File myDir  = context.getExternalFilesDir(root + "/chess");
//        myDir.mkdirs();
        File Dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/chess");
       //Dir.mkdirs();
        String fname = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH) + 1)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+"-"+calendar.get(Calendar.HOUR)+"-"+calendar.get(Calendar.MINUTE)+"chess.pgn";
        File file = new File(Dir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println(res);
            pw.flush();
            pw.close();
            f.close();
            Toast.makeText(context,"Выгрузка завершена", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadGame(String path){

    }
    private static final int FILE_SELECT_CODE = 0;

    public void showFileChooser(MainActivity activity) {
        activity.registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        Toast.makeText(context, (CharSequence) result,Toast.LENGTH_LONG).show();
                    }
                }
        );
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//        }
    }
}
