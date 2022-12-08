package com.people.sample.property;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.people.common.config.CustomPropertyConfig;
import com.people.common.oldutil.CommonUtil;
import com.people.common.oldutil.FileUtil;
import com.people.common.util.PropertiesUtil;

@RestController
public class PropertiesController {
	
//    @Value("${common.prop.is}")
//    private String commonProp;
//    
//    @Value("${custom.prop.is}")
//    private String customProp;
    
    @Autowired PropertiesUtil propertiesUtil;
	
	
	@GetMapping(path = "/prop/test")
    public ResponseEntity<?>  restApiGet(String aa, String bb) {
		
		List<String> ret = new ArrayList<>();
//		ret.add(commonProp);
		try {
			ret.add(propertiesUtil.getCommonPropIs());
			ret.add(propertiesUtil.getCustomPropIs());
			ret.add(FileUtil.basePath);
			return ResponseEntity.ok().body(ret);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}

    }
}
