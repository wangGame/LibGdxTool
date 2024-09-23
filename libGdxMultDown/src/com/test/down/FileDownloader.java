package com.test.down;

import com.test.down.constant.Constant;
import com.test.down.http.HttpUtils;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public  class FileDownloader {
    private  static  final String TAG = "FileDownloader";
//    private FileService fileService;

    /*  已下载文件长度  */
    private  int downloadSize = 0;

    /*  原始文件长度  */
    private  long fileSize = 0;

    /*  线程数  */
    private DownloadThread[] threads;

    /*  本地保存文件  */
    private File saveFile;

    /*  缓存各线程下载的长度 */
    private Map<Integer, Integer> data =  new ConcurrentHashMap<Integer, Integer>();

    /*  每条线程下载的长度  */
    private long block;

    /*  下载路径   */
    private String downloadUrl;

    /**
     * 获取线程数
     */
    public  int getThreadSize() {
        return threads.length;
    }

    /**
     * 获取文件大小
     *  @return
     */
    public long getFileSize() {
        return fileSize;
    }

    /**
     * 累计已下载大小
     *  @param  size
     */
    protected  synchronized  void append( int size) {
        downloadSize += size;
    }

    private FileService fileService;

    /**
     * 更新指定线程最后下载的位置
     *  @param  threadId 线程id
     *  @param  pos 最后下载的位置
     */
    protected  synchronized  void update( int threadId,  int pos) {
        this.data.put(threadId, pos);
        this.fileService.update( this.downloadUrl,  this.data);
    }

    /**
     * 构建文件下载器
     *  @param  downloadUrl 下载路径
     *  @param  fileSaveDir 文件保存目录
     *  @param  threadNum 下载线程数
     */
    public FileDownloader(String downloadUrl, File fileSaveDir,  int threadNum) {
        try {
            this.downloadUrl = downloadUrl;
            fileService =  new FileService();
            URL url =  new URL( this.downloadUrl);
            if(!fileSaveDir.exists()) fileSaveDir.mkdirs();
            this.threads =  new DownloadThread[threadNum];

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Referer", downloadUrl);
            HttpUtils.setHeader(conn);

            conn.connect();
            printResponseHeader(conn);

            if (conn.getResponseCode()==200) {
                this.fileSize = conn.getContentLengthLong();// 根据响应获取文件大小
                if ( this.fileSize <= 0) {
                    throw  new RuntimeException("Unkown file size ");
                }

                String filename = getFileName(conn); // 获取文件名称
                this.saveFile =  new File(fileSaveDir, filename); // 构建保存文件
                Map<Integer, Integer> logdata = fileService.getData(downloadUrl); // 获取下载记录

                if(logdata.size()>0){ // 如果存在下载记录
                    for(Map.Entry<Integer, Integer> entry : logdata.entrySet())
                        data.put(entry.getKey(), entry.getValue()); // 把各条线程已经下载的数据长度放入data中
                }

                if( this.data.size()== this.threads.length){ // 下面计算所有线程已经下载的数据长度
                    for ( int i = 0; i <  this.threads.length; i++) {
                        this.downloadSize +=  this.data.get(i+1);
                    }

                    print("已经下载的长度"+  this.downloadSize);
                }

                // 计算每条线程下载的数据长度
                this.block = ( this.fileSize %  this.threads.length)==0?  this.fileSize /  this.threads.length :  this.fileSize /  this.threads.length + 1;
            } else{
                throw  new RuntimeException("server no response ");
            }
        }  catch (Exception e) {
            print(e.toString());
            throw  new RuntimeException("don't connection this url");
        }
    }

    /**
     * 获取文件名
     *  @param  conn
     *  @return
     */
    private String getFileName(HttpURLConnection conn) {
        String filename =  this.downloadUrl.substring( this.downloadUrl.lastIndexOf('/') + 1);
        if(filename== null || "".equals(filename.trim())){ // 如果获取不到文件名称
            for ( int i = 0;; i++) {
                String mine = conn.getHeaderField(i);
                if (mine ==  null)  break;
                if("content-disposition".equals(conn.getHeaderFieldKey(i).toLowerCase())){
                    Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());
                    if(m.find())  return m.group(1);
                }
            }
            filename = UUID.randomUUID()+ ".tmp"; // 默认取一个文件名
            return filename;
        }
        return filename+".tmp";
    }

    /**
     *  开始下载文件
     *  @return  已下载文件大小
     *  @throws  Exception
     */
    public int download()  throws Exception{
        long l = System.currentTimeMillis();
        try {
            RandomAccessFile randOut =  new RandomAccessFile( this.saveFile, "rw");
            if( this.fileSize>0) randOut.setLength( this.fileSize);
            randOut.close();
            URL url =  new URL( this.downloadUrl);
            if( this.data.size() !=  this.threads.length){
                this.data.clear();
                for ( int i = 0; i <  this.threads.length; i++) {
                    this.data.put(i+1, 0); // 初始化每条线程已经下载的数据长度为0
                }
            }
            for ( int i = 0; i <  this.threads.length; i++) { // 开启线程进行下载
                int downLength =  this.data.get(i+1);
                if(downLength <  this.block &&  this.downloadSize< this.fileSize){ // 判断线程是否已经完成下载,否则继续下载
                    this.threads[i] =  new DownloadThread(
                            this, url,
                            this.saveFile,
                            this.block,
                            this.data.get(i+1),
                            i+1);
                    this.threads[i].setPriority(7);
                    this.threads[i].start();
                } else{
                    this.threads[i] =  null;
                }
            }
            this.fileService.save( this.downloadUrl,  this.data);
            boolean notFinish =  true; // 下载未完成
            while (notFinish) { //  循环判断所有线程是否完成下载
                Thread.sleep(900);
                notFinish =  false; // 假定全部线程下载完成
                for ( int i = 0; i <  this.threads.length; i++){
                    if ( this.threads[i] !=  null && ! this.threads[i].isFinish()) { // 如果发现线程未完成下载
                        notFinish =  true; // 设置标志为下载没有完成
                        if( this.threads[i].getDownLength() == -1){ // 如果下载失败,再重新下载
                            this.threads[i] =  new DownloadThread( this, url,  this.saveFile,  this.block,  this.data.get(i+1), i+1);
                            this.threads[i].setPriority(7);
                            this.threads[i].start();
                        }
                    }
                }
            }
            System.out.println(System.currentTimeMillis() - l+"  cost time");
            fileService.delete( this.downloadUrl);
        }  catch (Exception e) {
            print(e.toString());
            throw  new Exception("file download fail");
        }
        return  this.downloadSize;
    }

    /**
     * 获取Http响应头字段
     *  @param  http
     *  @return
     */
    public  static Map<String, String> getHttpResponseHeader(HttpURLConnection http) {
        Map<String, String> header =  new LinkedHashMap<String, String>();
        for ( int i = 0;; i++) {
            String mine = http.getHeaderField(i);
            if (mine ==  null)  break;
            header.put(http.getHeaderFieldKey(i), mine);
        }
        return header;
    }

    /**
     * 打印Http头字段
     *  @param  http
     */
    public static void printResponseHeader(HttpURLConnection http){
        Map<String, String> header = getHttpResponseHeader(http);
        for(Map.Entry<String, String> entry : header.entrySet()){
            String key = entry.getKey()!= null ? entry.getKey()+ ":" : "";
            print(key+ entry.getValue());
        }
    }

    private static void print(String msg){
        System.out.println(msg);
    }
}