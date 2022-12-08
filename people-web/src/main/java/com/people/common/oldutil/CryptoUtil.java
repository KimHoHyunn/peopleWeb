package com.people.common.oldutil;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CryptoUtil {
	
	private static final String algorithm = "AES/CBC/PKCS5Padding";
	private static final int AES_KEY_SIZE_128 = 128;
	private static final String initVec = "AAAAAAAA";
	
	
	/**`
	 * 암호화
	 * @param plainStr
	 * @param flag
	 * @return
	 * @throws Exception
	 */
	public String encrypt(String plainStr, boolean flag) throws Exception {
			return flag ? encrypt(plainStr) : plainStr;
	}
	
	private String encrypt(String plainStr) throws Exception  {
		return StringUtils.newStringUtf8(encryptByte(plainStr.getBytes("utf-8")));
	}
	
	private byte[] encryptByte(byte[] plainStrByte) throws Exception {
		
		byte[] key 		= null;
		byte[] iv  		= null;
		
		key = SystemUtil.getHostName().getBytes("utf-8");
		key = copyOf(key, AES_KEY_SIZE_128 / 8);
		
		iv = initVec.getBytes("utf-8");
		iv = copyOf(iv, AES_KEY_SIZE_128 / 8);
		
		Cipher cipher = Cipher.getInstance(algorithm);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), ivParameterSpec);

		return Base64.decodeBase64(cipher.doFinal(plainStrByte));

	}
	
	
	/**`
	 * 복호화
	 * @param encryptStr
	 * @param flag
	 * @return
	 * @throws Exception
	 */
	public String decrypt(String encryptStr, boolean flag) throws Exception {
			return flag ? decrypt(encryptStr) : encryptStr;
	}
	
	private String decrypt(String encryptStr) throws Exception  {
		return StringUtils.newStringUtf8(decryptByte(encryptStr.getBytes("utf-8")));
	}
	
	private byte[] decryptByte(byte[] encryptStrByte) throws Exception {
		
		byte[] key 		= null;
		byte[] iv  		= null;
		
		key = SystemUtil.getHostName().getBytes("utf-8");
		key = copyOf(key, AES_KEY_SIZE_128 / 8);
		
		iv = initVec.getBytes("utf-8");
		iv = copyOf(iv, AES_KEY_SIZE_128 / 8);
		
		Cipher cipher = Cipher.getInstance(algorithm);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), ivParameterSpec);

		return Base64.decodeBase64(cipher.doFinal(encryptStrByte));
	}
	
	/**
	 * byte array copy
	 * @param source
	 * @param size
	 * @return
	 */
	private byte[] copyOf(byte[] source, int size) {
		byte[] target = new byte[size];
		System.arraycopy(source, 0, target, 0, Math.min(source.length, size));
		return target;
	}
	
	
}
