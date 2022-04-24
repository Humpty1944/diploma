package openings;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import com.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import chessModel.ChessMan;
import chessModel.ChessModel;
import chessModel.Square;
import pgn.PgnGame;

public class OpeningsManager {
    private Context context;
    public OpeningsManager(Context context){
        this.context=context;
    }

    public ArrayList<String> getNames(){
        BufferedReader reader = null;
        ArrayList<String>  res = new ArrayList<>();


        //AssetFileDescriptor descriptor = context.getAssets().openFd("openings/" + "chess_openings.csv");
        try {
            System.out.println(context.getAssets());
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("openings/" + "openings_filter.csv"), "UTF-8"));

            CSVReader csvReader = new CSVReader(reader);
            String[] nextLine;
            int i=0;
            while ((nextLine = csvReader.readNext()) != null) {
                if(i!=0) {
                    res.add(nextLine[1]+"\n"+nextLine[0]);
                }
                i++;
            }

        } catch (Exception e) {
            System.out.println(e);
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    return res;
                } catch (IOException e) {
                    System.out.println(e);
                    //log the exception
                }
            }
        }
        return res;
    }
    public List<Opening> readFiles()  {
//        BufferedReader reader = null;
        BufferedReader reader = null;
        String res = "";
        List<Opening> openings = new ArrayList<>();

        //AssetFileDescriptor descriptor = context.getAssets().openFd("openings/" + "chess_openings.csv");
        try {
            System.out.println(context.getAssets());
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("openings/" + "openings_filter.csv"), "UTF-8"));

            CSVReader csvReader = new CSVReader(reader);
            String[] nextLine;
            int i=0;
            while ((nextLine = csvReader.readNext()) != null) {
                if(i!=0) {
                    openings.add(new Opening(nextLine[0], nextLine[1], nextLine[2], nextLine[3]));
                }
                i++;
            }
        } catch (Exception e) {
            System.out.println(e);
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    return openings;
                } catch (IOException e) {
                    System.out.println(e);
                    //log the exception
                }
            }
        }
        return openings;
    }


    private Square fromAlgebraToSquare(String turn) {
        int indCol = turn.length()>2? 1: 0;
        int indRow = turn.length()>2? 2: 1;
        int col = ((char) (turn.charAt(indCol) - '0') - 49);
        if (Character.isDigit(turn.charAt(1))) {
            return new Square(col, Character.getNumericValue(turn.charAt(indRow)) - 1);
        } else {
            return new Square(col, Character.getNumericValue(turn.charAt(indRow)) - 1);
        }
    }
}
