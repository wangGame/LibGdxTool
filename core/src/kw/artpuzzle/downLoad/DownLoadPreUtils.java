package kw.artpuzzle.downLoad;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.kw.gdx.BaseGame;
import com.kw.gdx.utils.log.NLog;
import com.kw.gdx.zip.PackZip;

import java.util.concurrent.Executors;

import kw.artpuzzle.constant.GameStaticInstance;
import kw.artpuzzle.constant.LevelConfig;
import kw.artpuzzle.constant.NetContant;

public class DownLoadPreUtils extends BaseDownLoadUtils {

    public DownLoadPreUtils(BaseGame game, int levelPath, String version,Runnable runnable,Runnable reTryRunnable) {
        super(game, runnable,reTryRunnable);
        this.levelPath = levelPath;
        this.resourceVersion = version + "/";
        pre = "downloadlevelpre/";
    }

    protected void downFile() {
        String toPath = Gdx.files.getLocalStoragePath();
        //下载url
        this.siteusing = NetContant.url+ resourceVersion + "levelpre/";
        //目标文件
        this.fromPath = append("level", levelPath, ".zip");
        String append = append(toPath, pre, "out/level");
        FileHandle local = Gdx.files.local(append);
        if (!local.exists()) {
            local.mkdirs();
        }
        if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
            Gdx.files.local(append(pre, "out/")).deleteDirectory();
            Gdx.files.local(append(pre, "temp/")).deleteDirectory();
            Gdx.files.local(append(pre, "md5/")).deleteDirectory();
        }
        this.toPath = append(append, levelPath, ".zip");
        success = new DownLoadListener() {
            @Override
            public void downLoadCallBack() {
                GameStaticInstance.getFixedThreadPool().submit(new Runnable() {
                    @Override
                    public void run() {
                        boolean copyFlag = PackZip.copyOutToTemp(append(toPath, pre, "out/"), append(toPath, pre, "temp/"));
                        reDownLoad(copyFlag);
                        //解压到md5
                        BaseGame.setText("unpack zip .....");
                        reDownLoad(PackZip.unpackZip(append(toPath, pre, "temp/"), append(toPath, pre, "md5/")));
                        BaseGame.setText("check .....");
                        reDownLoad(PackZip.check(append(toPath, pre, "md5/level", levelPath)));
                        //复制到最终的文件下
                        BaseGame.setText("copy result .....");
                        reDownLoad(PackZip.copyOutToTemp(append(toPath, pre, "md5/level", levelPath),
                                append(toPath, resourceVersion + "levelpre/level", levelPath)));
                        BaseGame.setText("delete temp file .......");
                        clearDir();
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                BaseGame.setText("");
                                runnable.run();
                            }
                        });
                    }
                });

            }
        };
        failed = new DownLoadListener() {
            @Override
            public void downLoadCallBack() {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {

                        BaseGame.setText("chong shi ------");
                        downLoad();
                    }
                });
            }
        };
        super.downFile();
    }

    @Override
    protected boolean retry() {
        PackZip.deleteDir(Gdx.files.local(append("level/levelpre", levelPath)).file());
        if (tryNumCount.containsKey(append("level", levelPath))) {
            tryNumCount.put(append("level", levelPath), tryNumCount.get(append("level", levelPath)) + 1);
            if (tryNumCount.get(append("level",levelPath)) > 3) {
                return true;
            }
        } else {
            tryNumCount.put(append("level", levelPath), 0);
        }
        return false;
    }

    @Override
    protected void clearDir() {
        super.clearDir();
        PackZip.deleteDir(Gdx.files.local(append(pre, "out/level", levelPath + ".zip")).file());
        PackZip.deleteDir(Gdx.files.local(append(pre, "temp/level", levelPath + ".zip")).file());
        PackZip.deleteDir(Gdx.files.local(append(pre, "md5/level" + levelPath)).file());
    }
}
