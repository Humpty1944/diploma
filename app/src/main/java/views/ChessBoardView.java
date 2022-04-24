package views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


import com.example.myapplication.ChessAnalysisFragment;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import chessModel.ChessHistory;
import chessModel.ChessMan;
import chessModel.ChessPiece;
import chessModel.Square;
import chessPiece.King;
import chessPiece.Piece;
import engine.work.UCIEngine;
import interfaces.ChessInterface;


@RequiresApi(api = Build.VERSION_CODES.R)
public class ChessBoardView extends View {
    private static final int MAX_CLICK_DURATION = 1000;
    private final float scaleFactor = 0.9f;
    private float cellSize = 130f;
    private float originX = 20f;
    private float originY = 200f;
    private final Paint paint = new Paint();
    private int fromCol = -1;
    private int fromRow = -1;
    private int toCol = -1;
    private int toRow = -1;
    private float movingPieceX = -1f;
    private float movingPieceY = -1f;
    private Bitmap movingPieceBitmap = null;
    private Piece movingPiece = null;
    public ChessInterface chessInterface = null;
    private long pressStartTime;

    private Canvas c = null;
    private UCIEngine uciEngine;
    private boolean isClickable=true;
    //private MainActivity mainActivity;
    private ChessAnalysisFragment mainActivity;
    private Piece checkKing=null;
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

    public ChessBoardView(Context context) {
        super(context);
        setWillNotDraw(false);
        loadBitmap();
    }

    public ChessBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        loadBitmap();
    }

    public ChessBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        loadBitmap();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);


        float chessBoardSize = Math.min(canvas.getHeight(), canvas.getWidth()) * scaleFactor;
        cellSize = chessBoardSize / 8;
        originX = (canvas.getWidth() - chessBoardSize) / 2;
        originY = (canvas.getHeight() - chessBoardSize) / 2;
        drawChessBoard(canvas);
        drawPieces(canvas);


        if(movingPiece!=null&& ChessHistory.currPlayer==movingPiece.getPlayer()){
           // if((ChessHistory.isCheck&&movingPiece instanceof King)||!ChessHistory.isCheck) {
                paint.setColor(Color.BLUE);
                ArrayList<PointF> p;
                if(ChessHistory.isCheck){
                    p=movingPiece.helpCoordCheck(originX, originY, cellSize);
                }else{
                    p=movingPiece.helpCoord(originX, originY, cellSize);
                }
              //  = movingPiece.helpCoord(originX, originY, cellSize);
                for (int i = 0; i < p.size(); i++) {
                    PointF coord = p.get(i);
                    canvas.drawCircle(coord.x, coord.y, 20, paint);
                   // invalidate();
                }
            //}
        }
       if(checkKing!=null&&ChessHistory.isCheck){
            checkDraw(canvas, checkKing.getCol(), checkKing.getRow());
            //checkKing=null;
        }
    }

    private String translateToAlgebraNotation(int fromRow, int fromCol, int toRow, int toCol) {

        String fromCh = Character.toString((char) ((char) (fromCol + '0') + 49));
        String toCH = Character.toString((char) ((char) (toCol + '0') + 49));
        String from = fromCh + (char) (fromRow + '1');
        String to = toCH + (char) (toRow + '1');

        return from + " " + to;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event == null) {
            return false;
        }
        if(!isClickable){
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                pressStartTime = Calendar.getInstance().getTimeInMillis();
                break;
            }
            case MotionEvent.ACTION_UP: {
                long clickDuration = Calendar.getInstance().getTimeInMillis() - pressStartTime;
                if(clickDuration < MAX_CLICK_DURATION) {
                    if (movingPiece==null) {

                        fromCol = (int) ((event.getX() - originX) / cellSize);
                        fromRow = 7 - (int) ((event.getY() - originY) / cellSize);
                        Piece chess = chessInterface.pieceAt(new Square(fromCol, fromRow));
                        if ((chess != null&&chess.getPlayer()==ChessHistory.currPlayer)){
                         // if(ChessHistory.isCheck)
                          //  if((ChessHistory.isCheck&&chess instanceof King)|| !ChessHistory.isCheck) {
                                movingPieceBitmap = bitmaps.get(chess.getResID());
                                movingPiece = chess;

                               // isStopCheck=false;
                                invalidate();
                          //  }
                       // addBorder(fromRow, fromCol, movingPieceBitmap);
                        }
                    }else if(movingPiece.getPlayer()==ChessHistory.currPlayer) {
                        toCol = (int) ((event.getX() - originX) / cellSize);
                        toRow = 7 - (int) ((event.getY() - originY) / cellSize);
                        if (toCol != fromCol || toRow != fromRow) {
                            Piece findPiece = chessInterface.pieceAt(new Square(toCol, toRow));
                            if (findPiece != null && findPiece.getPlayer() == movingPiece.getPlayer()) {
                                movingPiece = findPiece;
                                movingPieceBitmap =  bitmaps.get(findPiece.getResID());
                                fromCol = (int) ((event.getX() - originX) / cellSize);
                                fromRow = 7 - (int) ((event.getY() - originY) / cellSize);
                                invalidate();
                            }else {
                                if ((movingPiece instanceof King || (fromCol != toCol || fromRow != toRow)) && ChessHistory.isCheck) {
                                    ChessHistory.isCheck = false;
                                    checkKing = null;
                                    chessInterface.movePiece(new Square(fromCol, fromRow), new Square(toCol, toRow));
                                    ChessHistory.checkPiece = null;
                                    ChessHistory.checkingPiece = null;
                                    invalidate();
                                }
//                       else if(ChessHistory.isCheck&&(ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(), new Square(toCol, toRow) )||
//                                movingPiece.canMove(movingPiece.getSquare(), ChessHistory.checkingPiece.getSquare()))) {
                                else if (ChessHistory.isCheck && (movingPiece.canMove(movingPiece.getSquare(), ChessHistory.checkingPiece.getSquare()) || movingPiece.checkProtect(new Square(fromCol, fromRow), new Square(toCol, toRow)))) {
                                    chessInterface.movePiece(new Square(fromCol, fromRow), new Square(toCol, toRow));
                                    ChessHistory.isCheck = false;
                                    ChessHistory.checkPiece = null;
                                    ChessHistory.checkingPiece = null;
                                    checkKing = null;

                                } else if (!ChessHistory.isCheck) {
                                    chessInterface.movePiece(new Square(fromCol, fromRow), new Square(toCol, toRow));

                                } else {
                                    invalidate();
                                }
                                movingPiece = null;
                                movingPieceBitmap = null;
                            }
                        } else {
                            movingPiece = null;
                            movingPieceBitmap = null;
                            invalidate();
                        }

                    }
                }
            }
        }

        return true;
    }

    private void addBorder(int row, int col, Bitmap bmp) {
        Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(Color.argb(100, 222, 215, 122));
        RectF borderSize = new RectF(originX + col * cellSize, originY + (7 - row) * cellSize, originX + (col + 1) * cellSize, originY + (7 - row + 1) * cellSize);
        // canvas.drawBitmap(bmp,null, borderSize, paint);
        c.drawBitmap(bmpWithBorder, null, borderSize, paint);
        //return bmpWithBorder;
//        paint.setColor(Color.BLACK);
//        c.drawRect(originX + col * cellSize, originY + (7 - row) * cellSize, originX + (col + 1) * cellSize, originY + (7 - row + 1) * cellSize, paint);
        //c.drawRect(1000, 1000, 2000, 2000, paint);
        // invalidate();
    }

    private void loadBitmap() {
        for (Integer it : imgChess) {
            bitmaps.put(it, BitmapFactory.decodeResource(getResources(), it));

        }
    }

    private void drawChessBoard(Canvas canvas) {
        paint.setColor(Color.BLACK);
        for(int i=0;i<8;i++){
            paint.setTextSize(40);
            canvas.drawText(String.valueOf((char)('a'+i)), originY + i * cellSize+cellSize/2, 30, paint);
        }
        for(int i=0;i<8;i++){
            paint.setTextSize(40);
            canvas.drawText(String.valueOf(8-i), 10, originX + i * cellSize+cellSize/1.5f, paint);
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                drawSquare(canvas, i, j, ((i + j) % 2) == 0);

//                canvas.drawRect(originX + i * cellSize, originY + j * cellSize, originX + (i + 1) * cellSize, originY + (j + 1) * cellSize, paint);
            }
        }

    }

    private void drawSquare(Canvas canvas, int col, int row, boolean isDark) {
        int color = !isDark ? getResources().getColor(R.color.boardDark) : getResources().getColor(R.color.boardLight);
        paint.setColor(color);
        canvas.drawRect(originX + col * cellSize, originY + row * cellSize, originX + (col + 1) * cellSize, originY + (row + 1) * cellSize, paint);

    }

    private void drawPieces(Canvas canvas) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((col==2||col==3)&&(row==7||row==6)){
                    boolean flag=false;
                    System.out.println(flag);
                }
                Piece piece = chessInterface.pieceAt(new Square(col, row));

//                if (piece != movingPiece && piece != null) {
                if (piece != null) {
                    drawPieceAt(canvas, col, row, piece.getResID());
                }
            }
        }
        if (movingPieceBitmap != null) {
            canvas.drawBitmap(movingPieceBitmap, null, new RectF(movingPieceX - cellSize / 2, movingPieceY - cellSize / 2, movingPieceX + cellSize / 2, movingPieceY + cellSize / 2), paint);
        }
    }

    private void drawPieceAt(Canvas canvas, int col, int row, Integer resID) {
        Bitmap bitmap = bitmaps.get(resID);
        canvas.drawBitmap(bitmap, null, new RectF(originX + col * cellSize, originY + (7 - row) * cellSize, originX + (col + 1) * cellSize, originY + (7 - row + 1) * cellSize), paint);
    }

    public UCIEngine getUciEngine() {
        return uciEngine;
    }

    public void setUciEngine(UCIEngine uciEngine) {
        this.uciEngine = uciEngine;
    }
//    private void updateGUI(String line){
//        TextView text = findViewById( R.id.text_view_id);
//        text.setText(line);
//    }

    public ChessAnalysisFragment getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(ChessAnalysisFragment mainActivity) {
        this.mainActivity = mainActivity;
    }
    public void checkDraw(Canvas canvas, int col, int row){
        paint.setColor(Color.RED);
        paint.setAlpha(126);
        canvas.drawRect(originX + col * cellSize, originY + (7-row) * cellSize, originX + (col + 1) * cellSize, originY + (7-row + 1) * cellSize, paint);
    }

    public void setCheckKing(Piece checkKing) {
        this.checkKing = checkKing;
    }
    public void setIsClickable(boolean isClickable){
        this.isClickable=isClickable;
    }
}
