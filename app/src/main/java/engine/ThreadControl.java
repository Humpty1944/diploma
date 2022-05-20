//package engine;
//
//
//import com.example.myapplication.ChessAnalysisFragment;
//
//import chessModel.ChessHistory;
//import chessModel.ChessModel;
//import chessModel.Square;
//import chessPiece.Piece;
//import engine.work.UCIEngine;
//
//public  class ThreadControl implements Runnable {
//
//    // to stop the thread
//    private boolean exit;
//
//    private String name;
//    Thread t;
//    private UCIEngine uciEngine;
//    private ChessModel chessModel;
//    private ChessAnalysisFragment chessAnalysisFragment;
//
//   public ThreadControl(String threadname, UCIEngine uciEngine, ChessModel chessModel, ChessAnalysisFragment chessAnalysisFragment)
//    {
//        name = threadname;
//        t = new Thread(this, name);
//        System.out.println("New thread: " + t);
//        exit = false;
//        this.uciEngine=uciEngine;
//        this.chessModel=chessModel;
//        //t.start(); // Starting the thread
//        this.chessAnalysisFragment=chessAnalysisFragment;
//    }
//
//    // execution of thread starts from run() method
//    public void run()
//    {
//        int i = 0;
//        while (!exit) {
//            String line = "";
//            while (!line.contains("bestmove")) {
//                line = uciEngine.readLineFromEngine(50);
//                if (line.contains("cp")) {
//                   chessAnalysisFragment.textAnalysis.setText(formString(line));
//                    System.out.println(line);
//                }
//            }
//            exit=true;
//        }
//        System.out.println(name + " Stopped.");
//    }
//
//    // for stopping the thread
//    public void stop()
//    { uciEngine.writeLineToEngine("stop");
//        uciEngine.readLineFromEngine(50);
//        exit = true;
//
//    }
//
//    public String formString(String line) {
//        String res = "";
//        String[] lineWord = line.split(" ");
//        int pos = -1;
//        for (int i = 0; i < lineWord.length; i++) {
//            if (lineWord[i].contains(" ")) {
//                continue;
//            }
//            if (pos != -1) {
//                if (lineWord[pos].equals("cp")) {
//                    res += String.format("%.2f", Float.parseFloat(lineWord[i]) / 100) + " ";
//                    pos = -1;
//                } else if (lineWord[pos].equals("pv")) {
//                    try {
//                        res += turnToNormal(lineWord[i]);
//                    } catch (Exception e) {
//                        System.out.println(lineWord[i]);
//                    }
//                }
//            }
//            if (lineWord[i].equals("cp") || lineWord[i].equals("pv")) {
//
//                pos = i;
//            }
//        }
//        return res;
//    }
//
//    private String turnToNormal(String line) {
//        String from = line.substring(0, 2);
//        String to = line.substring(2, 4);
//        Square fromSquare = ChessHistory.fromAlgebraToSquare(from);
//        Piece piece = chessModel.pieceAt(fromSquare);
//        if (piece == null) {
//            return "";
//        } else {
//            return piece.getShortName() + to + " ";
//        }
//
//    }
//}