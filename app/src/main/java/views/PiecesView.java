package views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.myapplication.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import chessModel.Square;
import chessPiece.Piece;
import interfaces.ChessCreateInterface;

@RequiresApi(api = Build.VERSION_CODES.R)
public class PiecesView extends View {
    private static final int MAX_CLICK_DURATION = 1000;
    private final float scaleFactor = 0.9f;
    private float cellSize = 130f;
    private float originX = 20f;
    private float originY = 200f;
    private final Paint paint = new Paint();
    private long pressStartTime;
    private int fromCol = -1;
    private int fromRow = -1;
    private int toCol = -1;
    private int toRow = -1;
    private ChessCreateInterface chessCreateInterface;

    private final Set<Integer> imgChess;

    {
        imgChess = new HashSet<>();
        imgChess.add(R.drawable.chess_king_black);
        imgChess.add(R.drawable.chess_king_white);
        imgChess.add(R.drawable.chess_queen_black);
        imgChess.add(R.drawable.chess_queen_white);
        imgChess.add(R.drawable.chess_rook_black);
        imgChess.add(R.drawable.chess_rook_white);
        imgChess.add(R.drawable.chess_kinght_black);
        imgChess.add(R.drawable.chess_knight_white);
        imgChess.add(R.drawable.chess_bishop_black);
        imgChess.add(R.drawable.chess_bishop_white);
        imgChess.add(R.drawable.chess_pawn_black);
        imgChess.add(R.drawable.chess_pawn_white);
    }

    private final Map<Integer, Bitmap> bitmaps = new HashMap<Integer, Bitmap>();

    public PiecesView(Context context) {
        super(context);
        loadBitmap();
    }

    public PiecesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        loadBitmap();
    }

    public PiecesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadBitmap();
    }

    private void loadBitmap() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize=11;
        for (Integer it : imgChess) {
            bitmaps.put(it, BitmapFactory.decodeResource(getResources(), it, options));

        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        float chessBoardSize = Math.min(canvas.getHeight(), canvas.getWidth()) * scaleFactor;
        cellSize = chessBoardSize / 8;
        originX = (canvas.getWidth() - chessBoardSize) / 2;
        originY = (canvas.getHeight() - chessBoardSize) / 2;

        drawPieces(canvas);
    }

    private void drawPieces(Canvas canvas) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((col==2||col==3)&&(row==7||row==6)){
                    boolean flag=false;

                }
                Piece piece = chessCreateInterface.piecePos(col, row);

//                if (piece != movingPiece && piece != null) {
                if (piece != null) {
                    drawPieceAt(canvas, col, row, piece.getResID());
                }
            }
        }
//        if (movingPieceBitmap != null) {
//            canvas.drawBitmap(movingPieceBitmap, null, new RectF(movingPieceX - cellSize / 2, movingPieceY - cellSize / 2, movingPieceX + cellSize / 2, movingPieceY + cellSize / 2), paint);
//        }
    }

    private void drawPieceAt(Canvas canvas, int col, int row, Integer resID) {
        Bitmap bitmap = bitmaps.get(resID);
        canvas.drawBitmap(bitmap, null, new RectF(originX + col * cellSize, originY + (7 - row) * cellSize, originX + (col + 1) * cellSize, originY + (7 - row + 1) * cellSize), paint);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event == null) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                pressStartTime = Calendar.getInstance().getTimeInMillis();
                break;
            }
            case MotionEvent.ACTION_UP: {
                long clickDuration = Calendar.getInstance().getTimeInMillis() - pressStartTime;
                if(clickDuration < MAX_CLICK_DURATION) {
                    Piece piece = chessCreateInterface.getChosenPiece();
                    if(piece!=null){
                        fromCol = (int) ((event.getX() - originX) / cellSize);
                        fromRow = 7 - (int) ((event.getY() - originY) / cellSize);
                        if(chessCreateInterface.piecePos(fromCol,fromRow)!=null&&chessCreateInterface.isDeleting()){
                            chessCreateInterface.deletePiece(fromCol,fromRow);
                        }else {
                            chessCreateInterface.addPiece(piece,fromCol, fromRow);
                        }
                        invalidate();
                    }
                }
            }

        }
        return true;
    }
    public void setChessCreateInterface(ChessCreateInterface chessCreateInterface) {
        this.chessCreateInterface = chessCreateInterface;
    }
}
