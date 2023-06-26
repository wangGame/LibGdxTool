package com.tony.puzzle.webp;



import com.kw.gdx.utils.log.NLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;

public class CopyUtil {
	//将一个文件复制到另外一个地方
	public static long forChannel(File filefrom, File fileto) throws Exception {
		long time = new Date().getTime();
		int length = 2097152;
		if(!fileto.getParentFile().exists()){
			fileto.getParentFile().mkdirs();
		}
		FileInputStream in = new FileInputStream(filefrom);
		FileOutputStream out = new FileOutputStream(fileto);
		FileChannel inC = in.getChannel();
		FileChannel outC = out.getChannel();
		ByteBuffer b = null;
		while (true) {
			if (inC.position() == inC.size()) {
				inC.close();
				outC.close();
				return new Date().getTime() - time;
			}
			if ((inC.size() - inC.position()) < length) {
				length = (int) (inC.size() - inC.position());
			} else
				length = 2097152;
			b = ByteBuffer.allocateDirect(length);
			inC.read(b);
			b.flip();
			outC.write(b);
			outC.force(false);
		}
	}

	//删除目录下所有文件
	public static void delFiles(String filePath) {
		File root = new File(filePath);
		File[] files = root.listFiles();
		for (File file : files) {
			System.out.println("delete: "+file.getPath());
			if (file.isDirectory()) {
				delFiles(file.getPath());
				file.delete();
			} else {
				file.delete();
			}
		}
	}

	//复制整个目录
	public static void tranFolder (File from, File target) throws Exception {
		File[] nfs = from.listFiles();
		for (int i = 0; i < nfs.length; i++) {
			NLog.e("copy: "+nfs[i].getPath());
			if (nfs[i].isDirectory()) {
				File nndir = new File(target,nfs[i].getName());
				while (!nndir.exists()) {
					nndir.mkdir();
				}
				tranFolder(nfs[i], nndir);
			} else {
				String engpath = target.getPath() + "/" + nfs[i].getName();

				File nfile = new File(engpath);
				String fileType = ReadHead.getFileType(nfs[i].getPath());

				CopyUtil.forChannel(nfs[i], nfile);
			}
		}
	}
}
