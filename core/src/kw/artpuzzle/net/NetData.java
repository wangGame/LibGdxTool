//package kw.artpuzzle.net;
//
//import com.badlogic.gdx.Application;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.files.FileHandle;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.utils.IntArray;
//import com.badlogic.gdx.utils.IntMap;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Enumeration;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipFile;
//
//public class NetData {
//    public static final String S3SITE = "http://qiushi.cdn-doodlemobile.com/pool2020/";
//    public static NetData instance;
//    public static IntMap<String> pageName = new IntMap<>();
//    public static IntMap<String> pagePath = new IntMap<>();
//    public static IntMap<String> BGPath = new IntMap<>();
//    public static IntMap<String> BGName = new IntMap<>();
//
//    static{
//        BGName.put(11,"RUSSIA");
//        BGName.put(12,"JAPAN");
//        BGName.put(13,"AUSTRALIA");
//        BGName.put(14,"SAUDI");
//        BGName.put(15,"EGYPT");
//        BGName.put(16,"SINGAPORE");
//        BGName.put(17,"SOUTH KOREA");
//        BGName.put(18,"MEXICO");
//        BGName.put(19,"INDIA");
//        BGName.put(20,"SWEDEN");
//        BGName.put(21,"ARGENTINA");
//        BGName.put(22,"PORTUGAL");
//        BGName.put(23,"THAILAND");
//        BGName.put(24,"TURKEY");
//        BGName.put(25,"PERU");
//        BGName.put(26,"MALAYSIA");
//        BGName.put(27,"MOROCCO");
//        BGName.put(28,"ISRAEL");
//        BGName.put(29,"GREECE");
//    }
//
//    static{
//        BGPath.put(11,"BG11");
//        BGPath.put(12,"BG12");
//        BGPath.put(13,"BG13");
//        BGPath.put(14,"BG14");
//        BGPath.put(15,"BG15");
//        BGPath.put(16,"BG16");
//        BGPath.put(17,"BG17");
//        BGPath.put(18,"BG18");
//        BGPath.put(19,"BG19");
//        BGPath.put(20,"BG20");
//        BGPath.put(21,"BG21");
//        BGPath.put(22,"BG22");
//        BGPath.put(23,"BG23");
//        BGPath.put(24,"BG24");
//        BGPath.put(25,"BG25");
//        BGPath.put(26,"BG26");
//        BGPath.put(27,"BG27");
//        BGPath.put(28,"BG28");
//        BGPath.put(29,"BG29");
//    }
//
//    static {
//        pageName.put(11, "imap12_1");
//        pageName.put(12, "imap13_1");
//        pageName.put(13, "imap14_1");
//        pageName.put(14, "map1_2");
//        pageName.put(15, "map11");
//        pageName.put(16, "map3_1");
//        pageName.put(17, "map4");
//        pageName.put(18, "map5");
//        pageName.put(19, "map6");
//        pageName.put(20, "map7");
//        pageName.put(21, "map8");
//        pageName.put(22, "map9");
//    }
//
//    static {
//        pagePath.put(11, "imap12");
//        pagePath.put(12, "imap13");
//        pagePath.put(13, "imap14");
//        pagePath.put(14, "map1_2");
//        pagePath.put(15, "map11");
//        pagePath.put(16, "map3");
//        pagePath.put(17, "map4");
//        pagePath.put(18, "map5");
//        pagePath.put(19, "map6");
//        pagePath.put(20, "map7");
//        pagePath.put(21, "map8");
//        pagePath.put(22, "map9");
//    }
//
//    public int downloadintpage = -1;
//    public boolean downloadRunning = false;
//    int donepage;
//    IntArray todealPage = new IntArray();
//    IntArray todealBG = new IntArray();
//
//    public NetData() {
//        super("PGNet");
//        donepage = getInteger("donepage", 14);
//
//    }
//
//    public static void init() {
//        instance = new NetData();
//    }
//
//    private static void unzip(String path, String savePath) {
//        try {
//            File file = Gdx.files.useInocal(path).file();
//            ZipFile zipFile = new ZipFile(file);
//            Enumeration e = zipFile.entries();
//            while (e.hasMoreElements()) {
//                ZipEntry entry = (ZipEntry) e.nextElement();
//                InputStream is = zipFile.getInputStream(entry);
//                String entryName = entry.getName();
//                FileHandle useInocal = Gdx.files.useInocal(savePath + entryName);
//                File dstFile = useInocal.file();
//                if (entryName.endsWith("/")) {
//                    continue;
//                }
//                if (!useInocal.parent().exists()) {
//                    useInocal.parent().mkdirs();
//                }
//                FileOutputStream fos = new FileOutputStream(dstFile);
//                byte[] buffer = new byte[8192];
//                int count;
//                while ((count = is.read(buffer, 0, buffer.length)) != -1) {
//                    fos.write(buffer, 0, count);
//                }
//                fos.close();
//                is.close();
//            }
//            zipFile.close();
//            file.delete();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void downloadPage(int i, Stage stage) {
//        downloadRunning = true;
//        downloadintpage = i;
//        String pageName = NetData.pageName.get(i);
//        String topath;
//        if (Gdx.app.getType().equals(Application.ApplicationType.iOS)) {
//            topath = pageName + ".zip";
//        } else {
//            topath = Gdx.files.getLocalStoragePath() + pageName + ".zip";
//        }
//        PoolGame.getGame().platformAll.downloadRelate.downloadOneFile(S3SITE, pageName + ".zip",
//                topath, new Runnable() {
//                    @Override
//                    public void run() {
//                        System.out.println("unzip");
//                        unzip(pageName + ".zip", "");
//
//                        if (checkValid2(i))
//                            todealPage.removeValue(i);
//                        else {
//                            Gdx.app.postRunnable(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (stage != null) {
//                                        stage.addActor(new NetErrorView());
//                                    }
//                                }
//                            });
//
//                        }
//                        downloadRunning = false;
//                        downloadintpage = -1;
//
//                        Gdx.app.postRunnable(new Runnable() {
//                            @Override
//                            public void run() {
//                                dealOnePage(stage);
//                            }
//                        });
//                    }
//                },
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        downloadRunning = false;
//                        downloadintpage = -1;
//                        Gdx.app.postRunnable(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (stage != null) {
//                                    stage.addActor(new NetErrorView());
//                                }
//                            }
//                        });
//                    }
//                }
//        );
//    }
//
//    private void downloadOneBG(int i,Stage stage){
//        downloadRunning = true;
//        downloadintpage = i;
//        String bgPath = NetData.BGPath.get(i);
//
//        String topath;
//
//        if (Gdx.app.getType().equals(Application.ApplicationType.iOS)) {
//            topath = bgPath + ".zip";
//        } else {
//            topath = Gdx.files.getLocalStoragePath() + bgPath + ".zip";
//        }
//
//        PoolGame.getGame().platformAll.downloadRelate.downloadOneFile(S3SITE, "bg/"+bgPath + ".zip",
//                topath, new Runnable() {
//                    @Override
//                    public void run() {
//                        System.out.println("unzip");
//                        unzip(bgPath + ".zip", "");
//
//                        if (checkValid3(i)) {
//                            if(todealBG.contains(i))
//                                todealBG.removeValue(i);
//                        }
//                        else {
//                            Gdx.app.postRunnable(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (stage != null) {
//                                        stage.addActor(new NetErrorView());
//                                    }
//                                }
//                            });
//
//                        }
//                        downloadRunning = false;
//                        downloadintpage = -1;
//                    }
//                },
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        downloadRunning = false;
//                        downloadintpage = -1;
//                        Gdx.app.postRunnable(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (stage != null) {
//                                    stage.addActor(new NetErrorView());
//                                }
//                            }
//                        });
//                    }
//                }
//        );
//    }
//
//    private void downloadBG(int i,Stage stage){
//        downloadRunning = true;
//        downloadintpage = i;
//        String bgPath = NetData.BGPath.get(i);
//
//        String topath;
//
//        if (Gdx.app.getType().equals(Application.ApplicationType.iOS)) {
//            topath = bgPath + ".zip";
//        } else {
//            topath = Gdx.files.getLocalStoragePath() + bgPath + ".zip";
//        }
//
//        PoolGame.getGame().platformAll.downloadRelate.downloadOneFile(S3SITE, "bg/"+bgPath + ".zip",
//                topath, new Runnable() {
//                    @Override
//                    public void run() {
//                        System.out.println("unzip");
//                        unzip(bgPath + ".zip", "");
//
//                        if (checkValid3(i)) {
//                            todealBG.removeValue(i);
//                        }
//                        else {
//                            Gdx.app.postRunnable(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (stage != null) {
//                                        stage.addActor(new NetErrorView());
//                                    }
//                                }
//                            });
//
//                        }
//                        downloadRunning = false;
//                        downloadintpage = -1;
//                        Gdx.app.postRunnable(new Runnable() {
//                            @Override
//                            public void run() {
//                                dealBG(stage);
//                            }
//                        });
//                    }
//                },
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        System.out.println("viewpage=:下载失败");
//                        downloadRunning = false;
//                        downloadintpage = -1;
//                        Gdx.app.postRunnable(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (stage != null) {
//                                    stage.addActor(new NetErrorView());
//                                }
//                            }
//                        });
//                    }
//                }
//        );
//    }
//
////	public void dealPage(int pageid) {
////		for (int i = 0; i <= pageid; i++) {
////			if (pagePath.containsKey(i)) {
////				if(!todealPage.contains(i))
////					todealPage.add(i);
////			}
////		}
////
////		Gdx.app.postRunnable(new Runnable() {
////			@Override
////			public void run() {
////				dealOnePage();
////			}
////		});
////	}
//
//    private void dealOnePage(Stage stage) {
//        if (todealPage.size <= 0) {
//            return;
//        }
//        if (downloadRunning) {
//            return;
//        }
//
//        int page = todealPage.get(0);
//        downloadRunning = true;
//        downloadPage(page, stage);
//
////		Gdx.app.postRunnable(new Runnable() {
////			@Override
////			public void run() {
////				dealOnePage();
////			}
////		});
//    }
//
//    private void dealBG(Stage stage) {
//        if (todealBG.size <= 0) {
//            return;
//        }
//        if (downloadRunning) {
//            return;
//        }
//
//        int page = todealBG.get(0);
//        downloadRunning = true;
//        downloadBG(page, stage);
//    }
//
//    public boolean checkValidOne(int page, Stage stage){
//        boolean valid = checkValid3(page);
//        if(valid == false){
//            dealOneBG(page,stage);
//        }
//        return valid;
//    }
//
//    private void dealOneBG(int page, Stage stage){
//        if (downloadRunning) {
//            return;
//        }
//        downloadRunning = true;
//        downloadOneBG(page, stage);
//    }
//
//
//
//    public boolean checkValid(int page, Stage stage) {
//
//        boolean valid;
//        if(UserData.data.newMap){
//            valid = checkValid3(page);
//            if (valid == false) {
//                if (!todealBG.contains(page))
//                    todealBG.add(page);
//                dealBG(stage);
//            }
//        }else {
//            valid = checkValid2(page);
//            if (valid == false) {
//                if (!todealPage.contains(page))
//                    todealPage.add(page);
//                dealOnePage(stage);
//            }
//        }
//
//        return valid;
//    }
//
//
//    public boolean checkValid3(int page){
//        if(!BGPath.containsKey(page))
//            return true;
//
////		return false;
//        String path = BGPath.get(page);
//        FileHandle useInocal = Gdx.files.useInocal(path + "/md5.txt");
//        if(!useInocal.exists())
//            return false;
//        for (String line : new FileHandleIterable(useInocal)) {
//            String[] lines = line.split("\t");
//            String name = lines[0];
//            String md5 = lines[1];
//            FileHandle file1 = Gdx.files.useInocal(name);
//            if (!file1.exists())
//                return false;
//            try {
//                if (!testMD5.getMd5ByFile(file1.file()).equals(md5)) {
//                    return false;
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//        BGPath.remove(page);
//        return true;
//    }
//
//    private boolean checkValid2(int page) {
//        if (!pagePath.containsKey(page))
//            return true;
//        String path = pagePath.get(page);
//        FileHandle useInocal = Gdx.files.useInocal(path + "/md5.txt");
//        if (!useInocal.exists())
//            return false;
//        for (String line : new FileHandleIterable(useInocal)) {
//            String[] lines = line.split("\t");
//            String name = lines[0];
//            String md5 = lines[1];
//            FileHandle file1 = Gdx.files.useInocal(path + "/" + name);
//            if (!file1.exists())
//                return false;
//            try {
//                if (!testMD5.getMd5ByFile(file1.file()).equals(md5)) {
//                    return false;
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//
//        pagePath.remove(page);
//        return true;
//    }
//}
