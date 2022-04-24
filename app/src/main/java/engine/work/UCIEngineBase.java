package engine.work;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import engine.EngineUtil;
import engine.work.UCIOptions;

public abstract class UCIEngineBase implements UCIEngine {

    private boolean processAlive;
    private UCIOptions options;
    protected boolean isUCI;

    public static UCIEngine getEngine(String engine,
                                      EngineOptions engineOptions, Report report, Context appContext) {

//         if ("stockfish".equals(engine))
           // return new InternalStockFish(report, engineOptions.workDir, appContext);
//        else if (EngineUtil.isOpenExchangeEngine(engine))
//            return new OpenExchangeEngine(engine, engineOptions.workDir, report);
//        else
//            return new ExternalEngine(engine, engineOptions.workDir, report);
//        if (EngineUtil.isOpenExchangeEngine(engine))
//            return new OpenExchangeEngine(engine, engineOptions.workDir, report,appContext);
//        return new InternalStockFish(report, engineOptions.workDir, appContext, engine);
        if (engine.contains("stockfish")){
            return new InternalStockFish(report, engineOptions.workDir, appContext, engine);
        }
        return new ExternalEngine(engine,engineOptions.workDir,report, appContext, "sdsdsd");
    }

    protected UCIEngineBase() {
        processAlive = false;
        options = new UCIOptions();
        isUCI = false;
    }

    protected abstract void startProcess();

    protected abstract File getOptionsFile();
//
    @Override
    public final void initialize() {
        if (!processAlive) {
            startProcess();
            processAlive = true;
        }
    }
//
//    @Override
    public void initOptions(EngineOptions engineOptions) {
        isUCI = true;
    }

    @Override
    public final void applyIniFile() {
        File optionsFile = getOptionsFile();
        Properties iniOptions = new Properties();
        try (FileInputStream is = new FileInputStream(optionsFile)) {
            iniOptions.load(is);
        } catch (IOException |IllegalArgumentException ignore) {
        }
        Map<String,String> opts = new TreeMap<>();
        for (Map.Entry<Object,Object> ent : iniOptions.entrySet()) {
            if (ent.getKey() instanceof String && ent.getValue() instanceof String) {
                String key = ((String)ent.getKey()).toLowerCase(Locale.US);
                String value = (String)ent.getValue();
                opts.put(key, value);
            }
        }
        setUCIOptions(opts);
    }

    @Override
    public final boolean setUCIOptions(Map<String,String> uciOptions) {
        boolean modified = false;
        for (Map.Entry<String,String> ent : uciOptions.entrySet()) {
            String key = ent.getKey().toLowerCase(Locale.US);
            String value = ent.getValue();
            if (configurableOption(key))
                modified |= setOption(key, value);
        }
        return modified;
    }

    @Override
    public final void saveIniFile(UCIOptions options) {
//        Properties iniOptions = new Properties();
//        for (String name : options.getOptionNames()) {
//            UCIOptions.OptionBase o = options.getOption(name);
//            if (configurableOption(name) && o.modified())
//                iniOptions.put(o.name, o.getStringValue());
//        }
//        File optionsFile = getOptionsFile();
//        try (FileOutputStream os = new FileOutputStream(optionsFile)) {
//            iniOptions.store(os, null);
//        } catch (IOException ignore) {
//        }
    }

    @Override
    public final UCIOptions getUCIOptions() {
        return options;
    }



//    /** Return true if the UCI option can be edited in the "Engine Options" dialog. */
    protected boolean editableOption(String name) {
        name = name.toLowerCase(Locale.US);
        if (name.startsWith("uci_")) {
            return false;
        } else {
            String[] ignored = { "hash", "ponder", "multipv",
                    "gaviotatbpath", "syzygypath" };
            return !Arrays.asList(ignored).contains(name);
        }
    }

    /** Return true if the UCI option can be modified by the user, either directly
     *  from the "Engine Options" dialog or indirectly, for example from the
     *  "Set Engine Strength" dialog. */
    private boolean configurableOption(String name) {
        if (editableOption(name))
            return true;
        name = name.toLowerCase(Locale.US);
        String[] configurable = { "uci_limitstrength", "uci_elo" };
        return Arrays.asList(configurable).contains(name);
    }

    @Override
    public void shutDown() {
        if (processAlive) {
            writeLineToEngine("quit");
            processAlive = false;
        }
    }

    @Override
    public final void clearOptions() {
//        options.clear();
    }

    @Override
    public final UCIOptions.OptionBase registerOption(String[] tokens) {
        if (tokens.length < 5 || !tokens[1].equals("name"))
            return null;
        String name = tokens[2];
        int i;
        for (i = 3; i < tokens.length; i++) {
            if ("type".equals(tokens[i]))
                break;
            name += " " + tokens[i];
        }

        if (i >= tokens.length - 1)
            return null;
        i++;
        String type = tokens[i++];

        String defVal = null;
        String minVal = null;
        String maxVal = null;
        ArrayList<String> var = new ArrayList<>();
        try {
            for (; i < tokens.length; i++) {
                if (tokens[i].equals("default")) {
                    String stop = null;
                    if (type.equals("spin"))
                        stop = "min";
                    else if (type.equals("combo"))
                        stop = "var";
                    defVal = "";
                    while (i+1 < tokens.length && !tokens[i+1].equals(stop)) {
                        if (defVal.length() > 0)
                            defVal += " ";
                        defVal += tokens[i+1];
                        i++;
                    }
                } else if (tokens[i].equals("min")) {
                    minVal = tokens[++i];
                } else if (tokens[i].equals("max")) {
                    maxVal = tokens[++i];
                } else if (tokens[i].equals("var")) {
                    String value = "";
                    while (i+1 < tokens.length && !tokens[i+1].equals("var")) {
                        if (value.length() > 0)
                            value += " ";
                        value += tokens[i+1];
                        i++;
                    }
                    var.add(value);
                } else
                    return null;
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            return null;
        }

        UCIOptions.OptionBase option = null;
        if (type.equals("check")) {
            if (defVal != null) {
                defVal = defVal.toLowerCase(Locale.US);
                option = new UCIOptions.CheckOption(name, defVal.equals("true"));
            }
        } else if (type.equals("spin")) {
            if (defVal != null && minVal != null && maxVal != null) {
                try {
                    int defV = Integer.parseInt(defVal);
                    int minV = Integer.parseInt(minVal);
                    int maxV = Integer.parseInt(maxVal);
                    if (minV <= defV && defV <= maxV)
                        option = new UCIOptions.SpinOption(name, minV, maxV, defV);
                } catch (NumberFormatException ignore) {
                }
            }
        } else if (type.equals("combo")) {
            if (defVal != null && var.size() > 0) {
                String[] allowed = var.toArray(new String[0]);
                for (String s : allowed)
                    if (s.equals(defVal)) {
                        option = new UCIOptions.ComboOption(name, allowed, defVal);
                        break;
                    }
            }
        } else if (type.equals("button")) {
            option = new UCIOptions.ButtonOption(name);
        } else if (type.equals("string")) {
            if (defVal != null)
                option = new UCIOptions.StringOption(name, defVal);
        }

        if (option != null) {
            option.visible = editableOption(name);
            options.addOption(option);
        }
        return option;
    }

    /** Return true if engine has option optName. */
    protected final boolean hasOption(String optName) {
        return true;
    }

    @Override
    public final void setEloStrength(int elo) {
//        String lsName = "UCI_LimitStrength";
//        boolean limit = elo != Integer.MAX_VALUE;
//        UCIOptions.OptionBase o = options.getOption(lsName);
//        if (o instanceof UCIOptions.CheckOption) {
//            // Don't use setOption() since this value reflects current search parameters,
//            // not user specified strength settings, so should not be saved in .ini file.
//            writeLineToEngine(String.format(Locale.US, "setoption name %s value %s",
//                    lsName, limit ? "true" : "false"));
//        }
//        if (limit)
//            setOption("UCI_Elo", elo);
    }

    @Override
    public final void setOption(String name, int value) {
        setOption(name, String.format(Locale.US, "%d", value));
    }


//
//    @Override
//    public final void setOption(String name, boolean value) {
//        setOption(name, value ? "true" : "false");
//    }
//
//    @Override
//    public boolean setOption(String name, String value) {
//        if (!options.contains(name))
//            return false;
//        UCIOptions.OptionBase o = options.getOption(name);
//        if (o instanceof UCIOptions.ButtonOption) {
//            writeLineToEngine(String.format(Locale.US, "setoption name %s", o.name));
//        } else if (o.setFromString(value)) {
//            if (value.length() == 0)
//                value = "<empty>";
//            writeLineToEngine(String.format(Locale.US, "setoption name %s value %s", o.name, value));
//            return true;
//        }
//        return false;
//    }
}