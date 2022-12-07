package com.people.common.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public final class FileFinder {
	private FileFinder() {}
	
	public static final FileFilter DIRECTORY_FILTER = new FileFilter() {
		public boolean accept(File pathName) {
			boolean isDirectory = pathName.isDirectory();
			boolean isReadable = pathName.canRead();
			return isDirectory && isReadable;
		}
	};
	
	public static final FileFilter USELESS_FILTER = new FileFilter() {
		public boolean accept(File pathName) {
			return true;
		}
	};
	
	public static final FileFilter FILE_FILTER = new FileFilter() {
		public boolean accept(File pathName) {
			boolean isFile = pathName.isFile();
			return isFile;
		}
	};
	
	public static FileFilter generateFileFilter (final String ext) {
		return new FileFilter() {
			public boolean accept(File pathName) {
				String fileName = (pathName.getName()).toLowerCase();
				boolean isNormalFile = pathName.isFile();
				boolean isReadable = pathName.canRead();
				boolean isEndWithExt = fileName.endsWith("." + ext);
				return isNormalFile && isReadable && isEndWithExt;
			} 
		};
	}
	
	public static List<File> search(File rootFile, FileFilter fileFilter) {
		List<File> foundFiles = new ArrayList<File>();
		
		if(rootFile.isDirectory()) {
			File[] subDirectories = rootFile.listFiles(FileFinder.DIRECTORY_FILTER);
			
			for(int i=0; i < subDirectories.length ; i++) {
				ArrayList<File> temp_foundFiles = new ArrayList<File>();
				File subDirectory = subDirectories[i];
				temp_foundFiles.addAll(search(subDirectory, fileFilter));
				foundFiles.addAll(temp_foundFiles);
			}

			File[] files = rootFile.listFiles(fileFilter);
			
			for(int i=0; i < files.length; i++) {
				File file = files[i];
				foundFiles.add(file);
			}
			
		} else if(rootFile.isFile()) {	
			
			if(fileFilter.accept(rootFile)) {
				foundFiles.add(rootFile);
			}
			
		}//if end

		return foundFiles;
	}
	
}
