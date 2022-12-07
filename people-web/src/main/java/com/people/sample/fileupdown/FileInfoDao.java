package com.people.sample.fileupdown;

import org.springframework.stereotype.Repository;

import com.people.common.vo.FileVO;

@Repository("FileInfoDao")
public interface FileInfoDao {
	public FileVO getFileInfo(String fid) ;
}
