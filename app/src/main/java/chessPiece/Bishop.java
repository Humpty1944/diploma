package chessPiece;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;

import chessModel.ChessHistory;
import chessModel.ChessMan;
import chessModel.ChessModel;
import chessModel.Player;
import chessModel.Square;

public class Bishop extends Piece {


    public Bishop(int col, int row, Player player, ChessMan rank, Integer resID, ChessModel chessModel) {
        super(col, row, player, rank, resID, chessModel);
    }


    boolean isClearDiagonally(Square from, Square to) {
        if (Math.abs(from.getCol() - to.getCol()) !=
                Math.abs(from.getRow() - to.getRow())) {
            return false;
        }
        int gap = Math.abs(from.getCol() - to.getCol()) - 1;
        for (int i = 1; i <= gap; i++) {
            int nextCol = to.getCol() > from.getCol() ? from.getCol() + i : from.getCol() - i;
            int nextRow = to.getRow() > from.getRow() ? from.getRow() + i : from.getRow() - i;
            if (getChessModel().pieceAt(new Square(nextCol, nextRow)) != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canMove(Square from, Square to) {
        float a = 0f;
        float b = 0f;

         a = Math.abs(from.getCol() - to.getCol());
         b = Math.abs(from.getRow() - to.getRow());
        System.out.println(a + " " + b + " " + (a == b));

        if (Math.abs(from.getCol() - to.getCol()) ==
                Math.abs(from.getRow() - to.getRow())) {
            return isClearDiagonally(from, to);
        }
        return false;
    }

    @Override
    public ArrayList<PointF> helpCoordCheck(float xCord, float yCoard, float cellSize) {
        ArrayList<PointF> coord = new ArrayList<>();

        int col = getCol();
        int row = getRow();
        while (col >= 0 && row <= 7) {
            if (row == getRow() || col == getCol()) {
                col--;
                row++;
                continue;
            }
            Piece piece = getChessModel().pieceAt(new Square(col, row));
            if(ChessHistory.checkingPiece.getCol()==col&&ChessHistory.checkingPiece.getRow()==row){
                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                return coord;
            }
            if( ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(),new Square(col, row))&&
                    ChessHistory.checkingPiece.canMove(new Square(col, row), ChessHistory.checkPiece.getSquare())){
                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                return coord;
            }

            col--;
            row++;
        }
        col = getCol();
        row = getRow();
        while (col <= 7 && row <= 7) {
            if (row == getRow() || col == getCol()) {
                col++;
                row++;
                continue;
            }
            Piece piece = getChessModel().pieceAt(new Square(col, row));

            if(ChessHistory.checkingPiece.getCol()==col&&ChessHistory.checkingPiece.getRow()==row){
                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                return coord;
            }
            if( ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(),new Square(col, row))&&
                    ChessHistory.checkingPiece.canMove(new Square(col, row), ChessHistory.checkPiece.getSquare())){
                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                return coord;
            }
            col++;
            row++;
        }
        col = getCol();
        row = getRow();
        while (col <= 7 && row >= 0) {
            if (row == getRow() || col == getCol()) {
                col++;
                row--;
                continue;
            }
            Piece piece = getChessModel().pieceAt(new Square(col, row));

            if(ChessHistory.checkingPiece.getCol()==col&&ChessHistory.checkingPiece.getRow()==row){
                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                return coord;
            }
            if( ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(),new Square(col, row))&&
                    ChessHistory.checkingPiece.canMove(new Square(col, row), ChessHistory.checkPiece.getSquare())){
                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                return coord;
            }
            col++;
            row--;
        }
        col = getCol();
        row = getRow();
        while (col >= 0 && row >= 0) {
            if (row == getRow() || col == getCol()) {
                col--;
                row--;
                continue;
            }
            Piece piece = getChessModel().pieceAt(new Square(col, row));

            if(ChessHistory.checkingPiece.getCol()==col&&ChessHistory.checkingPiece.getRow()==row){
                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                return coord;
            }
            if( ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(),new Square(col, row))&&
                    ChessHistory.checkingPiece.canMove(new Square(col, row), ChessHistory.checkPiece.getSquare())){
                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                return coord;
            }
            col--;
            row--;
        }
        return coord;
    }

    @Override
    public ArrayList<PointF> helpCoord(float xCord, float yCoard, float cellSize) {
        ArrayList<PointF> coord = new ArrayList<>();
        erasePossibleMoves();

        if(ChessHistory.checkingPiece!=null){
            coord =  helpCoordCheck( xCord, yCoard, cellSize);
        }else {
            int col = getCol();
            int row = getRow();
            while (col >= 0 && row <= 7) {
                if (row == getRow() || col == getCol()) {
                    col--;
                    row++;
                    continue;
                }
                Piece piece = getChessModel().pieceAt(new Square(col, row));
                if (piece == null) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                } else if (piece.getPlayer() != getPlayer()) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                    break;
                } else {
                    break;
                }
                col--;
                row++;
            }
            col = getCol();
            row = getRow();
            while (col <= 7 && row <= 7) {
                if (row == getRow() || col == getCol()) {
                    col++;
                    row++;
                    continue;
                }
                Piece piece = getChessModel().pieceAt(new Square(col, row));
                if (piece == null) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                } else if (piece.getPlayer() != getPlayer()) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                    break;
                } else {
                    break;
                }
                col++;
                row++;
            }
            col = getCol();
            row = getRow();
            while (col <= 7 && row >= 0) {
                if (row == getRow() || col == getCol()) {
                    col++;
                    row--;
                    continue;
                }
                Piece piece = getChessModel().pieceAt(new Square(col, row));
                if (piece == null) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                } else if (piece.getPlayer() != getPlayer()) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                    break;
                } else {
                    break;
                }
                col++;
                row--;
            }
            col = getCol();
            row = getRow();
            while (col >= 0 && row >= 0) {
                if (row == getRow() || col == getCol()) {
                    col--;
                    row--;
                    continue;
                }
                Piece piece = getChessModel().pieceAt(new Square(col, row));
                if (piece == null) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                } else if (piece.getPlayer() != getPlayer()) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                    break;
                } else {
                    break;
                }
                col--;
                row--;
            }
        }
        return coord;
    }

}
