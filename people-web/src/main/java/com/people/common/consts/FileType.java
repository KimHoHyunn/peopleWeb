package com.people.common.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum FileType {
	      DEFAULT("etc","")
	    , CONFIG("config","properties")
		, TSA("pdf", "tsa")
		, IDX("pdf", "idx")
		, TIF("pdf", "tif")
		, INF("pdf", "inf")
		, TXT("txt", "txt")
		, JSON("json", "json")
		;
	
//	private final String contentType;
	private final String directoryKind;
	private final String kind;
	
    public static FileType extensionOf(String kind) {
        for (FileType filePath : FileType.values()) {
          if (filePath.kind.equals(kind)) {
            return filePath;
          }
        }
        
        return null;  // or throw exception
    }
}
