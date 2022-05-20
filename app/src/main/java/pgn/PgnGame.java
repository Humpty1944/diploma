package pgn;

import java.util.List;

import chessModel.ChessHistoryStep;
import helpClass.FenUtil;

public class PgnGame {
    private String event="?";
    private String site="?";
    private String date="?";
    private String round="?";
    private String white="?";
    private String black="?";
    private String result="?";
    private String fen="";
    private List<ChessHistoryStep> steps;

    FenUtil fenUtil = new FenUtil();

    public PgnGame(){

    }

    private void fromStringToHistory(String[] steps){

    }

    public boolean populateGame(String line) throws Exception{
        String[] data = line.split(" ");
        try {
            if (data.length == 0 || line == "") {
                return true;
            }
            if(data[0].toLowerCase().contains("fen")){
                if(fenUtil.checkFen( line.split("\"")[1])) {
                    this.fen = line.split("\"")[1];
                }
            }
            if (data[0].toLowerCase().contains("event") && !data[0].toLowerCase().contains("date")) {
                this.event = line.split("\"")[1];
                return true;
            } else if (data[0].toLowerCase().contains("site")) {
                this.site = line.split("\"")[1];
                return true;
            } else if (data[0].toLowerCase().contains("date") && !data[0].toLowerCase().contains("event")) {
                this.date = line.split("\"")[1];
                return true;
            } else if (data[0].toLowerCase().contains("round")) {
                this.round = line.split("\"")[1];
                return true;
            } else if (data[0].toLowerCase().contains("result")) {
                this.result = line.split("\"")[1];
                return true;
            } else if (data[0].toLowerCase().contains("white") && !data[0].toLowerCase().contains("elo")) {
                this.white = line.split("\"")[1];
                return true;
            } else if (data[0].toLowerCase().contains("black") && !data[0].toLowerCase().contains("elo")) {
                this.black = line.split("\"")[1];
                return true;
            }
            if (line.contains("[")) {
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getBlack() {
        return black;
    }

    public void setBlack(String black) {
        this.black = black;
    }

    public String getWhite() {
        return white;
    }

    public void setWhite(String white) {
        this.white = white;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public List<ChessHistoryStep> getSteps() {
        return steps;
    }

    public void setSteps(List<ChessHistoryStep> steps) {
        this.steps = steps;
    }

    public String getFen() {
        return fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
    }
}
