package engine.work;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.List;

import engine.ChessEngine;
import engine.ChessEngineResolver;
import engine.EngineUtil;


/** Engine imported from a different android app, resolved using the open exchange format. */
public class OpenExchangeEngine extends ExternalEngine {

    public OpenExchangeEngine(String engine, String workDir, Report report, Context context) {
        super(engine, workDir, report, context, engine);
    }

    @Override
    protected String copyFile(File from, File exeDir) throws IOException {
        new File(internalSFPath()).delete();
        ChessEngineResolver resolver = new ChessEngineResolver(context);
        List<ChessEngine> engines = resolver.resolveEngines();
        for (ChessEngine engine : engines) {
            if (EngineUtil.openExchangeFileName(engine).equals(from.getName())) {
                File engineFile = engine.copyToFiles(context.getContentResolver(), exeDir);
                return engineFile.getAbsolutePath();
            }
        }
        throw new IOException("Engine not found");
    }
}