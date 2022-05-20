package interfaces;

import chessPiece.Piece;

public interface ChessCreateInterface {
    public Piece getChosenPiece();
    public Piece piecePos(int col, int row);
    public void addPiece(Piece piece,int col, int row);
    public void deletePiece(int col, int row);
    public void sendInfo(Object[] info);
    public void onClick(int pos);
    public boolean isDeleting();
}
