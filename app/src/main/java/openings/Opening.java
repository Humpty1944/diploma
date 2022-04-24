package openings;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import chessModel.ChessMan;
import chessModel.Player;
import chessModel.Square;
import chessPiece.Bishop;
import chessPiece.King;
import chessPiece.Knight;
import chessPiece.Pawn;
import chessPiece.Piece;
import chessPiece.Queen;
import chessPiece.Rook;

public class Opening {
    private String number;
    private String name;
    private String fen;
    private String steps;
    private List<Square> listSquares = new ArrayList<>();
    private  boolean isCreated=false;
    public Opening(String name, String number, String fen, String steps) {
        this.number = number;
        this.name = name;
        this.fen = fen;
        this.steps = steps;
        // getSquares(steps);
    }

    public Set<Piece> readFen() {
        Set<Piece> pieceBox = new HashSet<>();
        String[] splitFen = fen.split(" ");
        String[] positions = splitFen[0].split("/");
        int before = 0;
        int prevCol = 0;
        for (int i = 0; i < positions.length; i++) {
            before = 0;
            prevCol = -1;
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
                    if (i != 0 && i != 7) {
                        before = Character.getNumericValue(positions[i].charAt(j));

                    }
                    continue;
                }
                int col = j;
                if (before == 1 && (i <= 1 || i >= 6)) {
                    //col++;
                    col = j;
                } else if ((i <= 1 || i >= 6) && before > 1) {
                    col += before - 1;
                } else if ((i > 1 && i < 6) && before == 0) {
                    col = prevCol + 1;
                } else if ((i > 1 && i < 6) && before > 1 && prevCol != -1) {
                    col = prevCol + before + 1;
                } else if ((i > 1 && i < 6) && before > 0) {
                    col = before;
                } else if ((i <= 1 || i >= 6) && col == prevCol) {
                    col = prevCol + 1;
                }
//               if(i>1&&i<6) {
//                   col = before;
//               }
//                }else if(before==0||before==1){
//                   col=j;
//               }else{
//                   col=j+before;
//               }
//               if(prevCol==col){
//                   col++;
//               }

                if (fig == 'r') {
                    if (player == Player.WHITE) {
                        pieceBox.add(new Rook(col, 7 - i, Player.WHITE, ChessMan.ROOK, R.drawable.chess_rook_white, null));
                    } else {
                        pieceBox.add(new Rook(col, 7 - i, Player.BLACK, ChessMan.ROOK, R.drawable.chess_rook_black, null));
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
                        pieceBox.add(new King(col, 7 - i, Player.WHITE, ChessMan.KING, R.drawable.chess_king_white, null));
                    } else {
                        pieceBox.add(new King(col, 7 - i, Player.BLACK, ChessMan.KING, R.drawable.chess_king_black, null));
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
        return pieceBox;
    }


    private Square fromAlgebraToSquare(String turn) {
        int indCol = turn.length() > 2 ? 1 : 0;
        int indRow = turn.length() > 2 ? 2 : 1;
        int col = ((char) (turn.charAt(indCol) - '0') - 49);
        if (Character.isDigit(turn.charAt(1))) {
            return new Square(col, Character.getNumericValue(turn.charAt(indRow)) - 1);
        } else {
            return new Square(col, Character.getNumericValue(turn.charAt(indRow)) - 1);
        }
    }

    public String getSquares(String steps) {
        String[] stepsArr = steps.split(" ");
        int step=1;
        int currNum=1;
       String newSteps="";
        for (int i = 0; i < stepsArr.length; i++) {
            if (stepsArr[i].contains(" ") || stepsArr[i].contains(".") || stepsArr[i].equals("")) {
                continue;
            }
            listSquares.add(fromAlgebraToSquare(stepsArr[i]));
            if(step%2==1) {
                listSquares.add(fromAlgebraToSquare(stepsArr[i]));
                newSteps+=currNum+". "+stepsArr[i]+" ";
                currNum++;
            }else{
                newSteps+=stepsArr[i]+" ";
            }
            step++;
        }
        return newSteps;
    }

    public String getName() {
        return name;
    }

    public String getSteps() {
        return steps;
    }

    public String getNumber() {
        return number;
    }

    public List<Square> getListSquares() {
        if(!isCreated) {
            getSquares(steps);
            isCreated=true;
        }
        return listSquares;
    }
}
