package views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

public class BoardView extends View {
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

    public BoardView(Context context) {
        super(context);

    }

    public BoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public BoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }
    @Override
    protected void onDraw(Canvas canvas) {
        float chessBoardSize = Math.min(canvas.getHeight(), canvas.getWidth()) * scaleFactor;
        cellSize = chessBoardSize / 8;
        originX = (canvas.getWidth() - chessBoardSize) / 2;
        originY = (canvas.getHeight() - chessBoardSize) / 2;
        drawChessBoard(canvas);

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

            }
        }

    }
    private void drawSquare(Canvas canvas, int col, int row, boolean isDark) {
        int color = !isDark ? getResources().getColor(R.color.boardDark) : getResources().getColor(R.color.boardLight);
        paint.setColor(color);
        canvas.drawRect(originX + col * cellSize, originY + row * cellSize, originX + (col + 1) * cellSize, originY + (row + 1) * cellSize, paint);

    }
}
