package chessModel;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import chessPiece.Bishop;
import chessPiece.Knight;
import chessPiece.Pawn;
import chessPiece.Piece;
import chessPiece.Queen;
import chessPiece.Rook;

public class ChessHistoryStep {
    private String from;
    private String to;
    private Piece chessPiece;
    private String currStep;
    private int whiteNumberTurn;
    private Square fromSquare;
    private Square toSquare;
    private Piece capturePiece = null;
    private boolean isPromoted = false;
    private int currMoveCount=0;
    private ChessMan promotedType;

    /*

    0- no
    1- right
    2-left
     */
    private byte isCasteling = 0;
    public ChessHistoryStep(){
        from="";
        to="";
        chessPiece=null;
        whiteNumberTurn=1;
        this.currStep=turnToNotationShow( 1,  1);;
        currMoveCount=1;

    }
    public ChessHistoryStep(  String from, String to, Piece chessPiece){
        this.from=from;
        this.to=to;
        this.chessPiece=chessPiece;
    }
    public ChessHistoryStep(int fromRow, int fromCol, int toRow, int toCol, Piece chessPiece ){
        this.fromSquare = new Square(fromCol,fromRow);
        this.toSquare = new Square(toCol,toRow);
        this.from=turnToNotation( fromRow,  fromCol);
        this.to=turnToNotation( toRow,  toCol);;
        this.chessPiece=chessPiece;
        this.currStep=turnToNotationShow( toRow,  toCol);;
    }
    private void createDeepCopyOfChess(Piece chessPiece){
        Piece newPice = null;
        if (chessPiece.getChessMan()==ChessMan.ROOK){
            int id = chessPiece.getPlayer()==Player.BLACK? R.drawable.chess_rook_black: R.drawable.chess_rook_white;
            newPice = new Rook(chessPiece.getCol(), chessPiece.getRow(), chessPiece.getPlayer(), ChessMan.ROOK, id, ChessHistory.chessModel);
        }else if(chessPiece.getChessMan()==ChessMan.QUEEN){
            int id = chessPiece.getPlayer()==Player.BLACK? R.drawable.chess_queen_black: R.drawable.chess_queen_white;
            newPice = new Queen(chessPiece.getCol(), chessPiece.getRow(), chessPiece.getPlayer(), ChessMan.QUEEN, id, ChessHistory.chessModel);
        }else if(chessPiece.getChessMan()==ChessMan.BISHOP){
            int id = chessPiece.getPlayer()==Player.BLACK? R.drawable.chess_bishop_black: R.drawable.chess_bishop_white;
            newPice = new Bishop(chessPiece.getCol(), chessPiece.getRow(), chessPiece.getPlayer(), ChessMan.BISHOP, id, ChessHistory.chessModel);
        }else if(chessPiece.getChessMan()==ChessMan.KNIGHT){
            int id = chessPiece.getPlayer()==Player.BLACK? R.drawable.chess_kinght_black: R.drawable.chess_knight_white;
            newPice = new Knight(chessPiece.getCol(), chessPiece.getRow(), chessPiece.getPlayer(), ChessMan.KNIGHT, id, ChessHistory.chessModel);
        }
        else{
            int id = chessPiece.getPlayer()==Player.BLACK? R.drawable.chess_pawn_black: R.drawable.chess_pawn_white;
            newPice = new Knight(chessPiece.getCol(), chessPiece.getRow(), chessPiece.getPlayer(), ChessMan.PAWN, id, ChessHistory.chessModel);
        }
        this.chessPiece=newPice;
    }
    public ChessHistoryStep(int fromRow, int fromCol, int toRow, int toCol, Piece chessPiece, int whiteNumberTurn ){
        this.fromSquare = new Square(fromCol,fromRow);
        this.toSquare = new Square(toCol,toRow);
        this.from=turnToNotation( fromRow,  fromCol);
        this.to=turnToNotation( toRow,  toCol);;
        this.chessPiece=chessPiece;
        this.whiteNumberTurn=whiteNumberTurn;
        this.currStep=turnToNotationShow( toRow,  toCol);;
    }
    public Piece getChessPiece() {
        return chessPiece;
    }

    public void setChessPiece(Piece chessPiece) {
        this.chessPiece = chessPiece;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String turnToNotationShow(int row, int col){
        String res="";
        if(chessPiece==null){
            return getWhiteNumberTurn()+". ...";
        }
        if(chessPiece.getPlayer()==Player.WHITE){
            res+=Integer.toString(getWhiteNumberTurn())+". ";
        }

        if(isCasteling==1){
            res+= "O-O";
        }else if (isCasteling==2){
            res+= "O-O-O";
        }else {
            String currPlace = turnToNotation(row, col);
            String currPiece = getCurrType();

            if (isPromoted) {
                res +=currPlace+ "=" + ChessHistory.getStringType(promotedType);
                return res;
            }
            if (capturePiece != null) {
                if (chessPiece.getChessMan() == ChessMan.PAWN) {
                    res += Character.toString((char) ((char) (fromSquare.getCol() + '0') + 49));
                } else {
                    res += currPiece;
                }
                res += "x";
                res += currPlace;
                if(ChessHistory.isCheck){
                    res+="+";
                }
                return res;
            } else {
                res += currPiece + currPlace;
                if(ChessHistory.isCheck){
                    res+="+";
                }
                return res;
            }
        }
        return res;
//        if(chessPiece.getPlayer()==Player.BLACK){
//            return currPiece+currPlace;
//        }else {
//            return Integer.toString(getWhiteNumberTurn())+". "+currPiece + currPlace;
//        }
    }

    private String getCurrType(){
        if(chessPiece.getChessMan()==ChessMan.QUEEN){
            return "Q";
        }else if (chessPiece.getChessMan()==ChessMan.KING){
            return "K";
        }else if (chessPiece.getChessMan()==ChessMan.BISHOP){
            return "B";
        }else if (chessPiece.getChessMan()==ChessMan.KNIGHT){
            return "N";
        }else if (chessPiece.getChessMan()==ChessMan.ROOK){
            return "R";
        }
        return "";

    }

    private String translateToAlgebraNotation(int fromRow, int fromCol, int toRow, int toCol) {

        String fromCh = Character.toString((char) ((char) (fromCol + '0') + 49));
        String toCH = Character.toString((char) ((char) (toCol + '0') + 49));
        String from = fromCh + (char) (fromRow + '1');
        String to = toCH + (char) (toRow + '1');

        return from + " " + to;
    }
    public static String turnToNotation(int row, int col){

        String result = "";
        String fromCh = Character.toString((char) ((char) (col + '0') + 49));
        result+=fromCh+(row+1);
        return result;
    }

    public static String turnToNotation(Square square){
        if(square==null){
            return "-";
        }
        String result = "";
        String fromCh = Character.toString((char) ((char) (square.getCol() + '0') + 49));
        result+=fromCh+(square.getRow()+1);
        return result;
    }

    public String getCurrStep() {
        return currStep;
    }

    public int getWhiteNumberTurn() {
        return whiteNumberTurn;
    }

    public void setWhiteNumberTurn(int whiteNumberTurn) {
        this.whiteNumberTurn = whiteNumberTurn;
    }



    public Piece getCapturePiece() {
        return capturePiece;
    }

    public void setCapturePiece(Piece capturePiece) {
        this.capturePiece = capturePiece;
       currStep=turnToNotationShow(chessPiece.getRow(),chessPiece.getCol());
    }

    public boolean isPromoted() {
        return isPromoted;
    }

    public void setPromoted(boolean promoted) {
        isPromoted = promoted;
    }

    public int getCurrMoveCount() {
        return currMoveCount;
    }

    public void setCurrMoveCount(int currMoveCount) {
        this.currMoveCount = currMoveCount;
    }

    public byte getIsCasteling() {
        return isCasteling;
    }

    public void setIsCasteling(byte isCasteling) {
        this.isCasteling = isCasteling;
        currStep=turnToNotationShow(chessPiece.getRow(),chessPiece.getCol());
    }

    public Square getToSquare() {
        return toSquare;
    }

    public void setToSquare(Square toSquare) {
        this.toSquare = toSquare;
    }

    public Square getFromSquare() {
        return fromSquare;
    }

    public void setFromSquare(Square fromSquare) {
        this.fromSquare = fromSquare;
    }

    public ChessMan getPromotedType() {
        return promotedType;
    }

    public void setPromotedType(ChessMan promotedType) {
        this.promotedType = promotedType;
        currStep=turnToNotationShow(chessPiece.getRow(),chessPiece.getCol());
    }
}
