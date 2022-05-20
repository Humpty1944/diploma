package chessModel;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import chessPiece.Bishop;
import chessPiece.King;
import chessPiece.Knight;
import chessPiece.Pawn;
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
    public synchronized static Square fromAlgebraToSquare(String turn) {
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

    public static boolean checkFen(String fen) throws Exception {
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
    public static Set<Piece> readFen(String fen) {
        Set<Piece> pieceBox = new HashSet<>();
        String[] splitFen = fen.split(" ");
        String[] positions = splitFen[0].split("/");
        King white = null;
        King black = null;
        ArrayList<Rook> whiteRooks = new ArrayList<>();
        ArrayList<Rook> blackRooks = new ArrayList<>();
        int before = 0;
        int prevCol = 0;
        for (int i = 0; i < positions.length; i++) {
            before = 0;
            prevCol = -1;
            Piece pieceBefore = null;
            for (int j = 0; j < positions[i].length(); j++) {

                Player player = Player.WHITE;
                // ChessMan chessMan = ChessMan.PAWN;
                if (Character.isLetter(positions[i].charAt(j))) {
                    if (Character.isLowerCase(positions[i].charAt(j))) {
                        player = Player.BLACK;
                    }
                }
                Character fig = Character.toLowerCase(positions[i].charAt(j));
                if (Character.isDigit(positions[i].charAt(j))) {
                   // if (i != 0 && i != 7) {
                        before = Character.getNumericValue(positions[i].charAt(j));

                  //  }
                    continue;
                }
                int col = j;
                if(prevCol==-1&& before!=0){
                    col=before;
                }else if(prevCol==-1&& before==0){
                    col=j;
                }
                else if (prevCol!=-1&&before==0){
                    col=prevCol+1;
                }
                else if (prevCol!=-1&&before!=0){
                    col=prevCol+before+1;
                }


                if (fig == 'r') {
                    if (player == Player.WHITE) {
                        Rook whiteRook = new Rook(col, 7 - i, Player.WHITE, ChessMan.ROOK, R.drawable.chess_rook_white, null);
                        whiteRooks.add(whiteRook);
                      //  pieceBox.add(whiteRook);
                    } else {
                        Rook blackRook = new Rook(col, 7 - i, Player.BLACK, ChessMan.ROOK, R.drawable.chess_rook_black, null);
                        blackRooks.add(blackRook);
                       // pieceBox.add(blackRook);
                    }
                } else if (fig == 'n') {
                    if (player == Player.WHITE) {
                        pieceBox.add(new Knight(col, 7 - i, Player.WHITE, ChessMan.KNIGHT, R.drawable.chess_knight_white, null));
                    } else {
                        pieceBox.add(new Knight(col, 7 - i, Player.BLACK, ChessMan.KNIGHT, R.drawable.chess_kinght_black, null));
                    }
                } else if (fig == 'b') {
                    if (player == Player.WHITE) {
                        pieceBox.add(new Bishop(col, 7 - i, Player.WHITE, ChessMan.BISHOP, R.drawable.chess_bishop_white, null));
                    } else {
                        pieceBox.add(new Bishop(col, 7 - i, Player.BLACK, ChessMan.BISHOP, R.drawable.chess_bishop_black, null));
                    }
                } else if (fig == 'q') {
                    if (player == Player.WHITE) {
                        pieceBox.add(new Queen(col, 7 - i, Player.WHITE, ChessMan.QUEEN, R.drawable.chess_queen_white, null));
                    } else {
                        pieceBox.add(new Queen(col, 7 - i, Player.BLACK, ChessMan.QUEEN, R.drawable.chess_queen_black, null));
                    }
                } else if (fig == 'k') {
                    if (player == Player.WHITE) {
                        white = new King(col, 7 - i, Player.WHITE, ChessMan.KING, R.drawable.chess_king_white, null);
                       // pieceBox.add(white);
                    } else {
                        black = new King(col, 7 - i, Player.BLACK, ChessMan.KING, R.drawable.chess_king_black, null);
                      //  pieceBox.add(black);
                    }

                } else if (fig == 'p') {
                    if (player == Player.WHITE) {
                        pieceBox.add(new Pawn(col, 7 - i, Player.WHITE, ChessMan.PAWN, R.drawable.chess_pawn_white, null));
                    } else {
                        pieceBox.add(new Pawn(col, 7 - i, Player.BLACK, ChessMan.PAWN, R.drawable.chess_pawn_black, null));
                    }

                } else {
                    before = Character.getNumericValue(positions[i].charAt(j));
                }
                prevCol = col;
                before = 0;
            }
        }
        if (splitFen[1].equals("w")){
            ChessHistory.currPlayer=Player.WHITE;
        }else{
            ChessHistory.currPlayer=Player.BLACK;
        }

       pieceBox=ChessHistory.checkCasteling(white,black,whiteRooks,blackRooks, splitFen[2], pieceBox);
        return pieceBox;
    }

    public static Set<Piece> checkCasteling(King white, King black, ArrayList<Rook> whiteRooks, ArrayList<Rook> blackRooks, String check, Set<Piece> pieceBox){
        if(check.equals("-")){
            white.setMoveCount(1);
            black.setMoveCount(1);
            pieceBox.add(white);
            pieceBox.add(black);
            pieceBox.addAll(whiteRooks);
            pieceBox.addAll(blackRooks);
            return pieceBox;
        }
        white.setMoveCount(1);
        black.setMoveCount(1);
        for(int i=0;i<whiteRooks.size();i++){
            whiteRooks.get(i).setMoveCount(1);
        }
        for(int i=0;i<blackRooks.size();i++){
            blackRooks.get(i).setMoveCount(1);
        }

        for(int i=0;i<check.length();i++){
            if(check.charAt(i)=='K'){
                white.setSameMoveCount(0);
                for(int j=0;j<whiteRooks.size();j++){
                    if(whiteRooks.get(j).getCol()>white.getCol()&&whiteRooks.get(j).getRow()==white.getRow()) {
                        whiteRooks.get(j).setSameMoveCount(0);
                        break;
                    }

                }

            }else if (check.charAt(i)=='Q'){
                white.setSameMoveCount(0);
                for(int j=0;j<whiteRooks.size();j++){
                    if(whiteRooks.get(j).getCol()<white.getCol()&&whiteRooks.get(j).getRow()==white.getRow()) {
                        whiteRooks.get(j).setSameMoveCount(0);
                        break;
                    }

                }

            }
            if(check.charAt(i)=='k'){
                black.setSameMoveCount(0);
                for(int j=0;j<blackRooks.size();j++){
                    if(blackRooks.get(j).getCol()>black.getCol()&&blackRooks.get(j).getRow()==black.getRow()) {
                        blackRooks.get(j).setSameMoveCount(0);
                        break;
                    }

                }
            }else if (check.charAt(i)=='q'){
                black.setSameMoveCount(0);
                for(int j=0;j<blackRooks.size();j++){
                    if(blackRooks.get(j).getCol()<black.getCol()&&blackRooks.get(j).getRow()==black.getRow()) {
                        blackRooks.get(j).setSameMoveCount(0);
                        break;
                    }

                }

            }
        }
        pieceBox.add(white);
        pieceBox.add(black);
        pieceBox.addAll(whiteRooks);
        pieceBox.addAll(blackRooks);
        return pieceBox;
    }
//    public static void createFen(ChessModel model){
//        String res="";
//        String cast="";
//        for(int i=0;i<8;i++){
//            int space=0;
//            for (int j=0;j<8;j++){
//                Piece piece = model.pieceAt(new Square(i,j));
//                if(piece!=null){
//                    String pieceName = piece.getShortName();
//                    if(piece.getPlayer()==Player.BLACK){
//                        pieceName=pieceName.toLowerCase();
//                    }
//                    if(piece instanceof King){
//                        cast+=ChessHistory.countCasteling((King) piece, model)+" ";
//                    }
//                    if(space!=0) {
//                    res+=String.valueOf(space);
//                    space=0;
//                    }
//                    res+=pieceName;
//                }else {
//                    space++;
//                }
//            }
//            if(space!=0){
//                res+=space;
//            }
//            if(i!=7) {
//                res += "/";
//            }
//        }
//        if(ChessHistory.currPlayer==Player.WHITE){
//            res+=" b";
//        }
//        else{
//            res+=" w";
//        }
//        res+=cast;
//    }
//
//    public static String countCasteling(King king, ChessModel model){
//        String res="";
//        if(king.getMoveCount()!=0){
//            return res;
//        }
//
//            Piece pieceK = model.pieceAt(new Square(king.getRow(), 7));
//            if(pieceK!=null&&pieceK.getMoveCount()==0){
//                if(king.getPlayer()==Player.BLACK){
//                    res+="k";
//                }else {
//                    res += "K";
//                }
//            }
//            Piece pieceQ = model.pieceAt(new Square(king.getRow(), 0));
//            if(pieceQ!=null&&pieceQ.getMoveCount()==0){
//                if(king.getPlayer()==Player.BLACK){
//                    res+="q";
//                }else {
//                    res += "Q";
//                }
//            }
//      if(res.equals("")){
//          return "-";
//      }
//        return res;
//    }
}
