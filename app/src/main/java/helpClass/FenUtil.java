package helpClass;

import android.os.Build;

import androidx.annotation.RequiresApi;

import chessModel.ChessHistory;
import chessModel.ChessModel;
import chessModel.Player;
import chessModel.Square;
import chessPiece.King;
import chessPiece.Pawn;
import chessPiece.Piece;
import chessPiece.Rook;

public class FenUtil {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public  String createFen(ChessModel model){
        String res="";
        String[] cast=new String[]{"","","",""};
        for(int row=7;row>=0;row--){
            int space=0;
            for (int col=0;col<8;col++){
                Piece piece = model.pieceAt(new Square(col,row));
                if(piece!=null){
                    String pieceName = piece.getShortName();
                    if(pieceName.equals("")){
                        pieceName="P";
                    }
                    if(piece.getPlayer()== Player.BLACK){
                        pieceName=pieceName.toLowerCase();
                    }
                    if(piece instanceof King){
                        String castRes = countCasteling((King) piece, model);
                        if(piece.getPlayer()==Player.WHITE){
                            if(castRes.equals("K")){
                                cast[0]=castRes;
                            }else{
                                cast[1]=castRes;
                            }
                        }else{
                            if(castRes.equals("k")){
                                cast[2]=castRes;
                            }else{
                                cast[3]=castRes;
                            }
                        }


                    }
                    if(space!=0) {
                        res+=String.valueOf(space);
                        space=0;
                    }
                    res+=pieceName;
                }else {
                    space++;
                }
            }
            if(space!=0){
                res+=space;
            }
            if(row!=0) {
                res += "/";
            }
        }
        if(ChessHistory.currPlayer==Player.WHITE){
            res+=" w ";
        }
        else{
            res+=" b ";
        }
        String castRes = String.join("", cast);
        if(castRes.length()==2){
           if( castRes.split("-", -1).length-1==2){
               castRes="-";
           }
        }else {
            castRes=castRes.replace("-","");
        }
        res+=castRes+" ";
        try {
            res += "- " + "0 " + (model.getChessHistorySteps().get(model.getChessHistorySteps().size() - 1).getWhiteNumberTurn() + 1);
        } catch (Exception ex){
            res+="- 0 0";
        }
        return  res.trim().replaceAll(" +", " ");
    }

    public  String countCasteling(King king, ChessModel model){
        String res="";
        if(king.getMoveCount()!=0){
            return res;
        }

        Piece pieceK = model.pieceAt(new Square(7, king.getRow()));
        if(pieceK!=null&&pieceK.getMoveCount()==0&&pieceK instanceof Rook&&pieceK.getPlayer()==king.getPlayer()){
            if(king.getPlayer()==Player.BLACK){
                res+="k";
            }else {
                res += "K";
            }
        }
        Piece pieceQ = model.pieceAt(new Square(0, king.getRow()));
        if(pieceQ!=null&&pieceQ.getMoveCount()==0&&pieceK instanceof Rook&&pieceK.getPlayer()==king.getPlayer()){
            if(king.getPlayer()==Player.BLACK){
                res+="q";
            }else {
                res += "Q";
            }
        }
        if(res.equals("")){
            return "-";
        }
        return res;
    }

    public  String countEnPassant(ChessModel model){
        for(Piece p: model.getPieces()){
            if(p instanceof Pawn){
                String res =  ((Pawn) p).checkAllEnPassant();
                if(!res.equals("-")){
                    return res;
                }
            }
        }
        return "-";
    }
    public  boolean checkFen(String fen) throws Exception {
        if(fen.length()==0){
            throw new Exception("Вы ввели пустую строку");
        }
        if( fen.split("/", -1).length-1!=7){
            throw new Exception("Недостаточно строк");
        }
        String[] fenSplit=fen.split(" ");
        if(fenSplit.length<=1){
            throw new Exception("Нет данных о текущей ходящей стороне");
        }
        if(!fenSplit[1].equals("b") && !fenSplit[1].equals("w")){
            throw new Exception("Нет данных о текущей ходящей стороне");
        }
        int kingWhite = fenSplit[0].split("K", -1).length-1;
        int kingBlack =fenSplit[0].split("k", -1).length-1;
        if(kingBlack ==0&&kingWhite ==0){
            throw new Exception("У обеих сторон нет королей");
        }
        if( kingWhite ==0){
            throw new Exception("У белых нет короля");
        }
        if(kingBlack ==0){
            throw new Exception("У черных нет короля");
        }
        return true;
    }
}
