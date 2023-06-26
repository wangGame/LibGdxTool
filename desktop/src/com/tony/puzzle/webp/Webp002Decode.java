package com.tony.puzzle.webp;

import java.io.File;
import java.util.ArrayList;

public class Webp002Decode {
	private static Cmd cmd;
	private static String inputpath = "E:\\work\\artpuzzleTools\\data\\ht\\out\\";
	private static String outputpath = "E:\\work\\artpuzzleTools\\data\\webp\\out\\";

	public static void main(String[] args) {
		cmd = new Cmd();

		File inputfile=new File(inputpath);

		dealWebp(inputfile);
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
				toexe.add("C:\\libwebp-1.0.1-windows-x64\\bin\\dwebp");
//				toexe.add("-lossless");
				toexe.add(child.getAbsolutePath());
				toexe.add("-o");
				toexe.add(outfile.getAbsolutePath());
				cmd.execCommand(toexe);
				System.out.println();
			}
		}

	}
}
