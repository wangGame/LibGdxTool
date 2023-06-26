package com.tony.puzzle.webp;

import java.io.File;
import java.util.ArrayList;

public class Webp001GenLossless {
	private static Cmd cmd;
	private static String inputpath = "E:\\artpuzzle\\artpuzzle\\data\\downlevel\\origin";
	private static String outputpath = "E:\\artpuzzle\\artpuzzle\\data\\downlevel\\origin";

	public static void main(String[] args) {
		cmd = new Cmd();

		File inputfile=new File(inputpath);

		dealWebp(inputfile);
	}


	public static void deal(String path,String out){
		cmd = new Cmd();
		inputpath = path;
		dealWebp(new File(path));
	}

	private static void dealWebp(File inputfile) {
		for(File child:inputfile.listFiles()){
			if(child.isDirectory())
				dealWebp(child);
			else if(child.getName().endsWith(".png")||child.getName().endsWith(".jpg")){
				String output =child.getAbsolutePath().replace(inputpath, outputpath);
				File outfile=new File(output);
				if(!outfile.getParentFile().exists())
					outfile.getParentFile().mkdirs();

//			    cwebp %%a -o D:\save\%%~na.png

				ArrayList<String> toexe=new ArrayList<String>();
				toexe.add("C:\\libwebp-0.5.1-windows-x86\\libwebp-0.5.1-windows-x86\\bin\\cwebp");
//				toexe.add("-lossless");

				toexe.add("-q");
				toexe.add("90");
				toexe.add(child.getAbsolutePath());
				toexe.add("-o");
				toexe.add(outfile.getAbsolutePath());
				cmd.execCommand(toexe);
				System.out.println();
			}
		}
	}
}
