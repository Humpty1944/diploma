package chessModel;

import com.example.myapplication.R;

import java.util.ArrayList;

import chessPiece.Bishop;
import chessPiece.King;
import chessPiece.Knight;
import chessPiece.Piece;
import chessPiece.Queen;
import chessPiece.Rook;

public class ChessHistory {
    public static ArrayList<ChessHistoryStep> chessHistory = new ArrayList<>();
    public static Player currPlayer=Player.WHITE;
    public static boolean isCheck=false;
    public static Piece checkingPiece=null;
    public  static King checkPiece=null;
    public  static int currCountStep=0;
    public static ChessModel chessModel;
    public  static boolean isForward=false;
    public static boolean isLoad = false;

    public static String makeGame( ArrayList<ChessHistoryStep> chessHistorySteps){
        String res="";
        for(int i=0;i<chessHistorySteps.size();i++){
            res+=chessHistorySteps.get(i).getCurrStep()+" ";
        }
        return res;
    }
    public static Square fromAlgebraToSquare(String turn) {
        int indCol = turn.length() > 2 ? 1 : 0;
        int indRow = turn.length() > 2 ? 2 : 1;
        int col = ((char) (turn.charAt(indCol) - '0') - 49);
        if (Character.isDigit(turn.charAt(1))) {
            return new Square(col, Character.getNumericValue(turn.charAt(indRow)) - 1);
        } else {
            return new Square(col, Character.getNumericValue(turn.charAt(indRow)) - 1);
        }
    }
    public static String getStringType(ChessMan chessMan){
        if(chessMan==ChessMan.QUEEN){
            return "Q";
        }else if (chessMan==ChessMan.KING){
            return "K";
        }else if (chessMan==ChessMan.BISHOP){
            return "B";
        }else if (chessMan==ChessMan.KNIGHT){
            return "N";
        }else if (chessMan==ChessMan.ROOK){
            return "R";
        }
        return "";
    }
}
