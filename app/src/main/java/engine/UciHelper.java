package engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class UciHelper {
    public static HashMap<String, String> getInfoResult(String line) {
        List<String> lineRes = Arrays.asList(line.split(" "));
        HashMap<String, String> result = new HashMap<String, String>();
        if (lineRes.size() <= 1) {
            return result;
        }
        if (!lineRes.contains("score")) {
            return result;
        }
        int cpLevel = Integer.parseInt(lineRes.get(lineRes.indexOf("cp") + 1)); //lineRes[lineRes.indexOf("cp")+1];
        result.put("cp", Double.toString(cpLevel / 100));
        int pvStart = lineRes.indexOf("pv"); //lineRes[lineRes.indexOf("cp")+1];
        for (int i = pvStart+1; i < lineRes.size(); i++) {
            result.put("pos" + i, lineRes.get(i));
        }

        return result;
    }
}
