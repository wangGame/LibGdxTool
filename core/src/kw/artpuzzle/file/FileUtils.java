package kw.artpuzzle.file;

import com.kw.gdx.utils.log.NLog;

import java.io.File;
import java.io.IOException;

public class FileUtils {
   public static boolean copyOutToTemp(String from, String to) {
      NLog.i("copyOutToTemp start from %s to %s", new Object[]{from, to});
      String sourceFolderPath1 = from;
      String targetFolderPath1 = to;
      File targetFile = new File(to);
      if (!targetFile.exists()) {
         targetFile.mkdirs();
      }

      File file = new File(from);
      File[] var6 = file.listFiles();
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         File listFile = var6[var8];
         if (listFile.isDirectory()) {
            copyOutToTemp(listFile.getPath(), to + File.separator + listFile.getName());
         } else {
            String sp = sourceFolderPath1 + File.separator + listFile.getName();
            String tp = targetFolderPath1 + File.separator + listFile.getName();

            try {
               com.kw.gdx.file.FileUtils.copyFile(sp, tp);
            } catch (IOException var13) {
               var13.printStackTrace();
               targetFile.delete();
               file.delete();
               return false;
            }
         }
      }

      targetFile.delete();
      NLog.i("copyOutToTemp end from %s to %s", new Object[]{from, to});
      return true;
   }
}
