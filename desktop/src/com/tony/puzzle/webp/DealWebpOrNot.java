package com.tony.puzzle.webp;



import java.io.File;
import java.util.ArrayList;

public class DealWebpOrNot {
	public static Cmd cmd=new Cmd();

	final static String yasuolv = "90";

	public static String sourcefolder="C:\\Users\\Doodle\\Desktop\\gitlab\\wordcross\\android\\assets";
//	public static String sourcefolder="C:\\Users\\Doodle\\Desktop\\libhelper\\webp\\webpi";
//	public static String sourcefolder="C:\\Users\\Doodle\\Desktop\\libhelper\\assets";
	public static String targetfolder="C:\\Users\\Doodle\\Desktop\\libhelper\\webp\\webpo\\assets";

	public static String webppath="C:\\Users\\Doodle\\Desktop\\gitlab\\CocosDemo\\libwebp-0.5.1-windows-x86\\bin\\cwebp";

	public static void main (String[] args) throws Exception {

		String targetfolder = DealWebpOrNot.targetfolder;
		String sourcefolder = DealWebpOrNot.sourcefolder;
		dealwebpandcopy( sourcefolder,targetfolder);
	}

	public static void dealwebpandcopy ( String sourcefolder,String targetfolder) throws Exception {
		CopyUtil.delFiles(targetfolder);
		TranFolder(new File(sourcefolder), targetfolder);
	}

	private static void TranFolder(File root, String nnpath) throws Exception {
		File[] nfs = root.listFiles();
		for (int i = 0; i < nfs.length; i++) {
			System.out.println(nfs[i].getName());
			if (nfs[i].isDirectory()) {
				String engpath = nnpath + "/" + nfs[i].getName();

				File nndir = new File(engpath);
				while (!nndir.exists()) {
					nndir.mkdir();
				}
				TranFolder(nfs[i], nndir.getPath());
			} else {
				String engpath = nnpath + "/" + nfs[i].getName();

				File nfile = new File(engpath);
				String fileType = ReadHead.getFileType(nfs[i].getPath());
				if(fileType==null){
					CopyUtil.forChannel(nfs[i], nfile);
				}else if(fileType.equals("png")||fileType.equals("jpg")){
					dealWebp(nfs[i], nfile);
				}else{
					CopyUtil.forChannel(nfs[i], nfile);
				}
			}
		}
	}


	public static void dealWebp(File child,File taget) throws Exception {
//		for(File child:inputfile.listFiles()){
//			if(child.isDirectory())
//				dealWebp(child);
//			else if(child.getName().endsWith(".png")||child.getName().endsWith(".jpg")){


//				String output =child.getAbsolutePath().replace(source, target);
		if(!taget.getParentFile().exists())
			taget.getParentFile().mkdirs();
//		if(taget.exists())
//			throw new Exception();

//			    cwebp %%a -o D:\save\%%~na.png

		ArrayList<String> toexe=new ArrayList<String>();
		toexe.add(webppath);
		toexe.add("-m");
		toexe.add("6");
		toexe.add("-q");
		toexe.add(yasuolv);
		toexe.add(child.getAbsolutePath());
		toexe.add("-o");
		toexe.add(taget.getAbsolutePath());
		cmd.execCommand(toexe);

//			}
//		}
	}


	public static void dealWebpornot(File from,File taget) throws Exception {
		String fileType = ReadHead.getFileType2(from);
		if(fileType==null){
			CopyUtil.forChannel(from, taget);
		}else if(fileType.equals("png")||fileType.equals("jpg")){
			dealWebp(from, taget);
		}else{
			CopyUtil.forChannel(from, taget);
		}
	}
}
