package chessPiece;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

import chessModel.ChessHistory;
import chessModel.ChessHistoryStep;
import chessModel.ChessMan;
import chessModel.ChessModel;
import chessModel.Player;
import chessModel.Square;

public class Pawn extends Piece {


    public Pawn(int col, int row, Player player, ChessMan rank, Integer resID, ChessModel chessModel) {
        super(col, row, player, rank, resID, chessModel);
    }


    @Override
    public boolean canMove(Square from, Square to) {

        if (from.getCol() == to.getCol()) {
            if (from.getRow() == 1 && getPlayer() == Player.WHITE) {
                return to.getRow() == 2 || to.getRow() == 3;
            } else if (from.getRow() == 6 && getPlayer() == Player.BLACK) {
                return to.getRow() == 5 || to.getRow() == 4;
            }
        } else {
            int length = getPlayer()==Player.WHITE? to.getRow()-from.getRow(): from.getRow()-to.getRow();
            if (getChessModel().pieceAt(to) != null && Math.abs(from.getCol() - to.getCol()) == 1&&length==1) {
                return true;
            }
        }
        return (Math.abs(from.getRow() - to.getRow()) == 1)&&(from.getCol()-to.getCol()==0);
    }

    @Override
    public ArrayList<PointF> helpCoordCheck(float xCord, float yCoard, float cellSize) {
        ArrayList<PointF> coord = new ArrayList<>();
        int step = getPlayer() == Player.WHITE ? 1 : -1;
        int block2 = getPlayer() == Player.WHITE ? getRow() + 2 : getRow() - 2;
        int block1 = getPlayer() == Player.WHITE ? getRow() + 1 : getRow() - 1;
        boolean isAttacking = false;
        if (getPlayer() == Player.WHITE) {

            if (getCol() > 0 && getCol() < 7) {
                Square to = new Square(getCol() - 1, getRow() + 1);
                if (ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(), new Square(getCol() - 1, getRow() + 1)) &&
                        ChessHistory.checkingPiece.canMove(new Square(getCol() - 1, getRow() + 1), ChessHistory.checkPiece.getSquare())
                ) {
                    coord.add(new PointF(xCord + (to.getCol()) * cellSize + cellSize / 2, yCoard + (7 - to.getRow()) * cellSize + cellSize / 2));
                    return coord;
                }
//                if (getChessModel().pieceAt(to) != null && getChessModel().pieceAt(to).getPlayer() != getPlayer()) {
//                    coord.add(new PointF(xCord + (to.getCol()) * cellSize + cellSize / 2, yCoard + (7 - to.getRow()) * cellSize + cellSize / 2));
//                    isAttacking = true;
//                }
//                to = new Square(getCol() + 1, getRow() + 1);
//                if (getChessModel().pieceAt(to) != null && getChessModel().pieceAt(to).getPlayer() != getPlayer()) {
//                    coord.add(new PointF(xCord + to.getCol() * cellSize + cellSize / 2, yCoard + (7 - to.getRow()) * cellSize + cellSize / 2));
//                    isAttacking = true;
//                }

                if (ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(), new Square(getCol() - 1, getRow() + 1)) &&
                        ChessHistory.checkingPiece.canMove(new Square(getCol() + 1, getRow() + 1), ChessHistory.checkPiece.getSquare())
                ) {
                    coord.add(new PointF(xCord + to.getCol() * cellSize + cellSize / 2, yCoard + (7 - to.getRow()) * cellSize + cellSize / 2));
                    return coord;
                }

                to = new Square(getCol() - 1, getRow());
                Piece piece = getChessModel().pieceAt(to);
//                if (enPassant(new Square(getCol(), getRow()), to) && piece.getPlayer() != getPlayer()) {
//                    coord.add(new PointF(xCord + (getRow() - 1) * cellSize + cellSize / 2, yCoard + (7 - getCol() - 1) * cellSize + cellSize / 2));
//                    isAttacking = true;
//                }

                if (ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(), new Square(getCol() - 1, getRow())) &&
                        ChessHistory.checkingPiece.canMove(new Square(getCol() - 1, getRow()), ChessHistory.checkPiece.getSquare())
                ) {
                    coord.add(new PointF(xCord + (getRow() - 1) * cellSize + cellSize / 2, yCoard + (7 - getCol() - 1) * cellSize + cellSize / 2));
                    return coord;
                }


                to = new Square(getCol() + 1, getRow());
                piece = getChessModel().pieceAt(to);
//                if (enPassant(new Square(getCol(), getRow()), to) && piece.getPlayer() != getPlayer()) {
//                    coord.add(new PointF(xCord + (getRow() + 1) * cellSize + cellSize / 2, yCoard + (7 - getCol() - 1) * cellSize + cellSize / 2));
//                    isAttacking = true;
//                }
                if (ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(), new Square(getCol() + 1, getRow())) &&
                        ChessHistory.checkingPiece.canMove(new Square(getCol() + 1, getRow()), ChessHistory.checkPiece.getSquare())
                ) {
                    coord.add(new PointF(xCord + (getRow() + 1) * cellSize + cellSize / 2, yCoard + (7 - getCol() - 1) * cellSize + cellSize / 2));
                    return coord;
                }

                if (ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(), new Square(getCol(), getRow() - 1)) &&
                        ChessHistory.checkingPiece.canMove(new Square(getCol(), getRow() - 1), ChessHistory.checkPiece.getSquare())
                ) {
                    coord.add(new PointF(xCord + (getCol()) * cellSize + cellSize / 2, yCoard + (7 - getRow() - 1) * cellSize + cellSize / 2));
                    return coord;
                }
                if (ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(), new Square(getCol(), getRow() - 1)) &&
                        ChessHistory.checkingPiece.canMove(new Square(getCol(), getRow() - 1), ChessHistory.checkPiece.getSquare()) &&
                        getMoveCount() == 0) {
                    coord.add(new PointF(xCord + (getCol()) * cellSize + cellSize / 2, yCoard + (7 - getRow() - 2) * cellSize + cellSize / 2));
                    return coord;
                }
            }
//            if (!isAttacking) {
//                coord.add(new PointF(xCord + (getCol()) * cellSize + cellSize / 2, yCoard + (7 - getRow() - 1) * cellSize + cellSize / 2));
//
//                if (getMoveCount() == 0) {
//                    coord.add(new PointF(xCord + (getCol()) * cellSize + cellSize / 2, yCoard + (7 - getRow() - 2) * cellSize + cellSize / 2));
//                }
//            }
        } else {

            if (getCol() > 0 && getCol() < 7) {
                Square to = new Square(getCol() - 1, getRow() - 1);
//                if (getChessModel().pieceAt(to) != null && getChessModel().pieceAt(to).getPlayer() != getPlayer()) {
//                    coord.add(new PointF(xCord + (to.getCol()) * cellSize + cellSize / 2, yCoard + (7 - to.getRow()) * cellSize + cellSize / 2));
//                    isAttacking = true;
//                }

                if (ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(), new Square(getCol() - 1, getRow() - 1)) &&
                        ChessHistory.checkingPiece.canMove(new Square(getCol() - 1, getRow() - 1), ChessHistory.checkPiece.getSquare())
                ) {
                    coord.add(new PointF(xCord + (to.getCol()) * cellSize + cellSize / 2, yCoard + (7 - to.getRow()) * cellSize + cellSize / 2));
                    return coord;
                }


                to = new Square(getCol() + 1, getRow() - 1);
//                if (getChessModel().pieceAt(to) != null && getChessModel().pieceAt(to).getPlayer() != getPlayer()) {
//                    coord.add(new PointF(xCord + to.getCol() * cellSize + cellSize / 2, yCoard + (7 - to.getRow()) * cellSize + cellSize / 2));
//                    isAttacking = true;
//                }

                if (ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(), new Square(getCol() + 1, getRow() - 1)) &&
                        ChessHistory.checkingPiece.canMove(new Square(getCol() + 1, getRow() - 1), ChessHistory.checkPiece.getSquare())
                ) {
                    coord.add(new PointF(xCord + to.getCol() * cellSize + cellSize / 2, yCoard + (7 - to.getRow()) * cellSize + cellSize / 2));
                    return coord;
                }


                to = new Square(getCol() - 1, getRow());
                Piece piece = getChessModel().pieceAt(to);
//                if (enPassant(new Square(getCol(), getRow()), to) && piece.getPlayer() != getPlayer()) {
//                    coord.add(new PointF(xCord + (getRow()) * cellSize + cellSize / 2, yCoard + (7 - getCol() + 2) * cellSize + cellSize / 2));
//                    isAttacking = true;
//                }

                if (ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(), new Square(getCol() - 1, getRow())) &&
                        ChessHistory.checkingPiece.canMove(new Square(getCol() - 1, getRow()), ChessHistory.checkPiece.getSquare())
                ) {
                    coord.add(new PointF(xCord + (getRow()) * cellSize + cellSize / 2, yCoard + (7 - getCol() + 2) * cellSize + cellSize / 2));
                    return coord;
                }


                to = new Square(getCol() + 1, getRow());
                piece = getChessModel().pieceAt(to);
//                if (enPassant(new Square(getCol(), getRow()), to) && piece.getPlayer() != getPlayer()) {
//                    coord.add(new PointF(xCord + (getRow() + 2) * cellSize + cellSize / 2, yCoard + (7 - getCol() + 2) * cellSize + cellSize / 2));
//                    isAttacking = true;
//                }

                if (ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(), new Square(getCol() + 1, getRow())) &&
                        ChessHistory.checkingPiece.canMove(new Square(getCol() + 1, getRow()), ChessHistory.checkPiece.getSquare())
                ) {
                    coord.add(new PointF(xCord + (getRow() + 2) * cellSize + cellSize / 2, yCoard + (7 - getCol() + 2) * cellSize + cellSize / 2));
                    return coord;
                }

                if (ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(), new Square(getCol() - 1, getRow())) &&
                        ChessHistory.checkingPiece.canMove(new Square(getCol() - 1, getRow()), ChessHistory.checkPiece.getSquare())
                ) {
                    coord.add(new PointF(xCord + (getRow() - 1) * cellSize + cellSize / 2, yCoard + (7 - getCol() - 1) * cellSize + cellSize / 2));
                    return coord;
                }


//            }
//            if (!isAttacking) {
                //  coord.add(new PointF(xCord + (getCol()) * cellSize + cellSize / 2, yCoard + (7 - getRow() + 1) * cellSize + cellSize / 2));
                if (ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(), new Square(getCol(), getRow() + 1)) &&
                        ChessHistory.checkingPiece.canMove(new Square(getCol(), getRow() + 1), ChessHistory.checkPiece.getSquare())
                ) {
                    coord.add(new PointF(xCord + (getCol()) * cellSize + cellSize / 2, yCoard + (7 - getRow() + 1) * cellSize + cellSize / 2));
                    return coord;
                }
//                if (getMoveCount() == 0) {
//                    coord.add(new PointF(xCord + (getCol()) * cellSize + cellSize / 2, yCoard + (7 - getRow() + 2) * cellSize + cellSize / 2));
//                }
                if (ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(), new Square(getCol(), getRow() + 1)) &&
                        ChessHistory.checkingPiece.canMove(new Square(getCol(), getRow() + 1), ChessHistory.checkPiece.getSquare()) &&
                        getMoveCount() == 0
                ) {
                    coord.add(new PointF(xCord + (getCol()) * cellSize + cellSize / 2, yCoard + (7 - getRow() + 2) * cellSize + cellSize / 2));
                    return coord;
                }
                // }
            }
        }
        return coord;
    }

    @Override
    public ArrayList<PointF> helpCoord(float xCord, float yCoard, float cellSize) {
        ArrayList<PointF> coord = new ArrayList<>();
        erasePossibleMoves();
        if (ChessHistory.checkingPiece != null) {
            coord = helpCoordCheck(xCord, yCoard, cellSize);
        } else {
            int step = getPlayer() == Player.WHITE ? 1 : -1;
            int block2 = getPlayer() == Player.WHITE ? getRow() + 2 : getRow() - 2;
            int block1 = getPlayer() == Player.WHITE ? getRow() + 1 : getRow() - 1;
            boolean isAttacking = false;
            if (getPlayer() == Player.WHITE) {

                if (getCol() >= 0 && getCol() <= 7) {
                    Square to = new Square(getCol() - 1, getRow() + 1);
                    if (getChessModel().pieceAt(to) != null && getChessModel().pieceAt(to).getPlayer() != getPlayer()) {
                        coord.add(new PointF(xCord + (to.getCol()) * cellSize + cellSize / 2, yCoard + (7 - to.getRow()) * cellSize + cellSize / 2));
                        isAttacking = true;
                    }
                    to = new Square(getCol() + 1, getRow() + 1);
                    if (getChessModel().pieceAt(to) != null && getChessModel().pieceAt(to).getPlayer() != getPlayer()) {
                        coord.add(new PointF(xCord + to.getCol() * cellSize + cellSize / 2, yCoard + (7 - to.getRow()) * cellSize + cellSize / 2));
                        isAttacking = true;
                    }
                    to = new Square(getCol() - 1, getRow());
                    Piece piece = getChessModel().pieceAt(to);
                    if (enPassant(new Square(getCol(), getRow()), to) && piece.getPlayer() != getPlayer()) {
                        coord.add(new PointF(xCord + (getCol()-1) * cellSize + cellSize / 2, yCoard + (7 - getRow()-1) * cellSize + cellSize / 2));
                        isAttacking = true;
                    }
                    to = new Square(getCol() + 1, getRow());
                    piece = getChessModel().pieceAt(to);
                    if (enPassant(new Square(getCol(), getRow()), to) &&piece!=null&& piece.getPlayer() != getPlayer()) {
                        coord.add(new PointF(xCord + (getCol()+1) * cellSize + cellSize / 2, yCoard + (7 - getRow()-1) * cellSize + cellSize / 2));
                        isAttacking = true;
                    }
                }
                //if (!isAttacking) {
                    Piece piece =getChessModel().pieceAt(new Square(getCol(), getRow()+1));
                if(piece==null||(piece!=null&&piece.getPlayer()!=getPlayer())) {
                    coord.add(new PointF(xCord + (getCol()) * cellSize + cellSize / 2, yCoard + (7 - getRow() - 1) * cellSize + cellSize / 2));
                }
                piece = getChessModel().pieceAt(new Square(getCol(), getRow()+2));
                    if (getMoveCount() == 0&&((piece!=null&&piece.getPlayer()!=getPlayer())||piece==null)) {
                        coord.add(new PointF(xCord + (getCol()) * cellSize + cellSize / 2, yCoard + (7 - getRow() - 2) * cellSize + cellSize / 2));
                    }
               // }
            } else {

                if (getCol() >= 0 && getCol() <= 7) {
                    Square to = new Square(getCol() - 1, getRow() - 1);
                    if (getChessModel().pieceAt(to) != null && getChessModel().pieceAt(to).getPlayer() != getPlayer()) {
                        coord.add(new PointF(xCord + (to.getCol()) * cellSize + cellSize / 2, yCoard + (7 - to.getRow()) * cellSize + cellSize / 2));
                        isAttacking = true;
                    }
                    to = new Square(getCol() + 1, getRow() - 1);
                    if (getChessModel().pieceAt(to) != null && getChessModel().pieceAt(to).getPlayer() != getPlayer()) {
                        coord.add(new PointF(xCord + to.getCol() * cellSize + cellSize / 2, yCoard + (7 - to.getRow()) * cellSize + cellSize / 2));
                        isAttacking = true;
                    }
                    to = new Square(getCol() - 1, getRow());
                    Piece piece = getChessModel().pieceAt(to);
                    if (enPassant(new Square(getCol(), getRow()), to) && piece.getPlayer() != getPlayer()) {
                        coord.add(new PointF(xCord + (getCol()-1) * cellSize + cellSize / 2, yCoard + (7 - getRow() +1) * cellSize + cellSize / 2));
                        isAttacking = true;
                    }
                    to = new Square(getCol() + 1, getRow());
                    piece = getChessModel().pieceAt(to);
                    if (enPassant(new Square(getCol(), getRow()), to) && piece.getPlayer() != getPlayer()) {
                        coord.add(new PointF(xCord + (getCol() + 1) * cellSize + cellSize / 2, yCoard + (7 - getRow() +1) * cellSize + cellSize / 2));
                        isAttacking = true;
                    }
                }
               // if (!isAttacking) {
                Piece piece =getChessModel().pieceAt(new Square(getCol(), getRow()-1));
                if(piece==null||(piece!=null&&piece.getPlayer()!=getPlayer())) {
                    coord.add(new PointF(xCord + (getCol()) * cellSize + cellSize / 2, yCoard + (7 - getRow() + 1) * cellSize + cellSize / 2));
                }
                piece = getChessModel().pieceAt(new Square(getCol(), getRow()-2));
                if (getMoveCount() == 0&&((piece!=null&&piece.getPlayer()!=getPlayer())||piece==null)) {
                        coord.add(new PointF(xCord + (getCol()) * cellSize + cellSize / 2, yCoard + (7 - getRow() + 2) * cellSize + cellSize / 2));
                    }
               // }
            }
        }

        return coord;
    }

    public boolean enPassant(Square from, Square to) {
        if (to.getCol() < 0 || to.getCol() > 7) {
            return false;
        }
        Piece piece = getChessModel().pieceAt(new Square(to.getCol(), from.getRow()));
        if (piece == null) {
            return false;
        }
        ArrayList<ChessHistoryStep> p = ChessHistory.chessHistory;
        return (piece.getPlayer() != getPlayer()
                && piece instanceof Pawn && piece.getCurrLength() == 2);
//                && ChessHistory.chessHistory.get(ChessHistory.currCountStep-1).getChessPiece() == piece);


    }

//    private Piece findLastMove() {
//        for (int i=ChessHistory.chessHistory.size() - 1;i>=0;i-- ){
//            if( ChessHistory.chessHistory.get(i).getChessPiece()==this){
//                return ChessHistory.chessHistory.get(i);
//            }
//        }
//    }

}
