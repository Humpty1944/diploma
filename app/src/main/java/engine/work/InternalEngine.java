//package engine.work;
//
//import android.content.Context;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//
//import engine.EngineUtil;
//
//public class InternalEngine extends ExternalEngine{
//    private String chesEngineName;
//    private File defaultNetFile;
//    public InternalEngine(String engine, String workDir, Report report, Context appContext) {
//        super(engine, workDir, report, appContext, engine);
//    }
//    private long readCheckSum(File f) {
//        try (InputStream is = new FileInputStream(f);
//             DataInputStream dis = new DataInputStream(is)) {
//            return dis.readLong();
//        } catch (IOException e) {
//            return 0;
//        }
//    }
//    private void writeCheckSum(File f, long checkSum) {
//        try (OutputStream os = new FileOutputStream(f);
//             DataOutputStream dos = new DataOutputStream(os)) {
//            dos.writeLong(checkSum);
//        } catch (IOException ignore) {
//        }
//    }
//    private long computeAssetsCheckSum(String sfExe) {
//
//        try (InputStream is = context.getAssets().open(sfExe)) {
//            MessageDigest md = MessageDigest.getInstance("SHA-1");
//            byte[] buf = new byte[8192];
//            while (true) {
//                int len = is.read(buf);
//                if (len <= 0)
//                    break;
//                md.update(buf, 0, len);
//            }
//            byte[] digest = md.digest(new byte[]{0});
//            long ret = 0;
//            for (int i = 0; i < 8; i++) {
//                ret ^= ((long)digest[i]) << (i * 8);
//            }
//            return ret;
//        } catch (IOException e) {
//            return -1;
//        } catch (NoSuchAlgorithmException e) {
//            return -1;
//        }
//    }
//
//    @Override
//    protected String copyFile(File from, File exeDir) throws IOException {
//        File to = new File(exeDir, "engine.exe");
//        final String sfExe = EngineUtil.internalStockFishName();
//
//        // The checksum test is to avoid writing to /data unless necessary,
//        // on the assumption that it will reduce memory wear.
//        long oldCSum = readCheckSum(new File(internalSFPath()));
//        long newCSum = computeAssetsCheckSum(sfExe);
//        if (oldCSum != newCSum) {
//            copyAssetFile(sfExe, to);
//            writeCheckSum(new File(internalSFPath()), newCSum);
//        }
//       // copyNetFile(exeDir);
//        return to.getAbsolutePath();
//    }
//    private void copyAssetFile(String assetName, File targetFile) throws IOException {
//        try (InputStream is = context.getAssets().open(assetName);
//             OutputStream os = new FileOutputStream(targetFile)) {
//            byte[] buf = new byte[8192];
//            while (true) {
//                int len = is.read(buf);
//                if (len <= 0)
//                    break;
//                os.write(buf, 0, len);
//            }
//        }
//    }
////    private void copyNetFile(File exeDir) throws IOException {
////        defaultNetFile = new File(exeDir, defaultNet);
////        if (defaultNetFile.exists())
////            return;
////        File tmpFile = new File(exeDir, defaultNet + ".tmp");
////        copyAssetFile(defaultNet, tmpFile);
////        if (!tmpFile.renameTo(defaultNetFile))
////            throw new IOException("Rename failed");
////    }
//    public String getChesEngineName() {
//        return chesEngineName;
//    }
//
//    public void setChesEngineName(String chesEngineName) {
//        this.chesEngineName = chesEngineName;
//    }
//
//}
