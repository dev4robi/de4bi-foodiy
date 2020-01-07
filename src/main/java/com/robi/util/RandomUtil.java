package com.robi.util;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomUtil {

	private static final Logger logger = LoggerFactory.getLogger(RandomUtil.class);
	private static final Random RANDOM_GENRATOR;
	private static final char[] SYMBOL_ALPHABET_UPPER;
	private static final char[] SYMBOL_ALPHABET_LOWER;
	private static final char[] SYMBOL_SPECIAL;
	private static final char[] SYMBOL_NUMERIC;

	public static final int UPPER_CASE = 0x01; // 0000 0001
	public static final int LOWER_CASE = 0x02; // 0000 0010
	public static final int ALPHABET   = 0x03; // 0000 0011
	public static final int NUMERIC    = 0x04; // 0000 0100
	public static final int SPECIAL    = 0x08; // 0000 1000

	static {
		synchronized (RandomUtil.class) {
			RANDOM_GENRATOR = new Random();
			SYMBOL_ALPHABET_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
			SYMBOL_ALPHABET_LOWER = "abcdefghijklmnopqrstuvwxyz".toCharArray();
			SYMBOL_NUMERIC = "0123456789".toCharArray();
			SYMBOL_SPECIAL = "!@#$%^&*()-_=+[]{}\\|'\"/?`~,.<>;:".toCharArray();
		}
	}

	/**
	 * 	<p>무작위 문자로 채워진 문자열을 생성합니다.</p>
	 * 	@param length : 생성할 문자열 길이
	 *	@param symbolOp : 생성에 사용할 문자 종류 옵션
	 * 	<strong>({@link RandomUtil.ALPHABET} | {@link RandomUtil.SPECIAL} | {@link RandomUtil.NUMERIC})</strong>
	 * 	@return 생성된 length길이위 무작위 문자열
	 */
	public static String genRandomStr(int length, int symbolOp) {
		if (length < 1) {
			logger.error("'length' is less then 1! (length:" + length + ")");
			return null;
		}

		boolean useUpperCase = false;
		boolean useLowerCase = false;
		boolean useSpecial = false;
		boolean useNumeric = false;

		if ((symbolOp & UPPER_CASE) == UPPER_CASE) {
			useUpperCase = true;
		}

		if ((symbolOp & LOWER_CASE) == LOWER_CASE) {
			useLowerCase = true;
		}

		if ((symbolOp & NUMERIC) == NUMERIC) {
			useNumeric = true;
		}

		if ((symbolOp & SPECIAL) == SPECIAL) {
			useSpecial = true;
		}

		if (!useUpperCase && !useLowerCase && !useNumeric && !useSpecial) {
			useUpperCase = true;
			useLowerCase = true;
		}

		int selectedSymbolCnt = 0;

		selectedSymbolCnt += (useUpperCase ? 1 : 0);
		selectedSymbolCnt += (useLowerCase ? 1 : 0);
		selectedSymbolCnt += (useSpecial   ? 1 : 0);
		selectedSymbolCnt += (useNumeric   ? 1 : 0);

		char[][] selectedSymbolAry = new char[selectedSymbolCnt][];
		int curSymbolAryCnt = 0;

		if (useUpperCase) {
			selectedSymbolAry[curSymbolAryCnt++] = SYMBOL_ALPHABET_UPPER;
		}

		if (useLowerCase) {
			selectedSymbolAry[curSymbolAryCnt++] = SYMBOL_ALPHABET_LOWER;
		}

		if (useSpecial) {
			selectedSymbolAry[curSymbolAryCnt++] = SYMBOL_SPECIAL;
		}

		if (useNumeric) {
			selectedSymbolAry[curSymbolAryCnt++] = SYMBOL_NUMERIC;
		}

		StringBuilder rtSb = new StringBuilder(length);
		char[] selectedSymbolOps = null;

		for (int i = 0; i < length; ++i) {
			selectedSymbolOps = selectedSymbolAry[RANDOM_GENRATOR.nextInt(selectedSymbolAry.length)];
			rtSb.append(selectedSymbolOps[RANDOM_GENRATOR.nextInt(selectedSymbolOps.length)]);
		}

		return rtSb.toString();
	}

	/**
	 * 	<p>무작위 문자로 채워진 문자열을 생성합니다.</p>
	 * 	@param length : 생성할 문자열 길이
	 *	@param symbolAry : 생성에 사용할 문자열 배열
	 * 	@return 생성된 length길이위 무작위 문자열
	 */
	public static String genRandomStr(int length, char[] symbolAry) {
		if (length < 1) {
			logger.error("'length' is less then 0! (length:" + length + ")");
			return null;
		}

		if (symbolAry == null || symbolAry.length == 0) {
			logger.error("'symbolAry' is null or zero length! (symbolAry:" + symbolAry.toString() + ")");
			return null;
		}

		StringBuilder rtSb = new StringBuilder(length);

		for (int i = 0; i < length; ++i) {
			rtSb.append(symbolAry[RANDOM_GENRATOR.nextInt(symbolAry.length)]);
		}

		return rtSb.toString();
	}
}