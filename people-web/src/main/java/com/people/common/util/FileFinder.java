package com.people.common.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public final class FileFinder {
	
	public  final FileFilter DIRECTORY_FILTER = new FileFilter() {
		public boolean accept(File pathName) {
			boolean isDirectory = pathName.isDirectory();
			boolean isReadable = pathName.canRead();
			return isDirectory && isReadable;
		}
	};
	
	public  final FileFilter USELESS_FILTER = new FileFilter() {
		public boolean accept(File pathName) {
			return true;
		}
	};
	
	public  final FileFilter FILE_FILTER = new FileFilter() {
		public boolean accept(File pathName) {
			boolean isFile = pathName.isFile();
			return isFile;
		}
	};
	
	public  FileFilter generateFileFilter (final String ext) {
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
	
	public List<File> search(File rootFile, FileFilter fileFilter) {
		List<File> foundFiles = new ArrayList<File>();
		
		if(rootFile.isDirectory()) {
			File[] subDirectories = rootFile.listFiles(DIRECTORY_FILTER);
			
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
