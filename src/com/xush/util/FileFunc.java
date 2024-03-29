package com.xush.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 2004.06.14      增加函数，获取一个目录下的所有子目录
 * <p>Title:FileFunc </p>
 * <p>Description:文件或目录处理的函数 </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author zy
 * @version 1.0
 */
public final class FileFunc {
	//定义文件分隔符常量
	public static final char separatorChar = '/';

	public static final String separator = "" + separatorChar;

	public static final String DEFAULT_PATH = System.getProperty("java.io.tmpdir");

	public static final int SORT_SIZE = 0;

	public static final int SORT_TIME = 1;

	public static final int SIZE = SORT_SIZE;

	public static final int TIME = SORT_TIME;

	private FileFunc() {
	}

	/**
	 * 列出一个目录下所有的子目录
	 * @param dir
	 * @return
	 */
	public static String[] listSubDir(String dir) {
		ArrayList subdirs = new ArrayList();
		File dirF = new File(dir);
		File[] files = dirF.listFiles();
		if (files == null) {
			return null;
		}
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				subdirs.add(files[i].getName());
			}
		}
		String[] result = new String[subdirs.size()];
		subdirs.toArray(result);
		return result;
	}

	/**
	 * 改变文件名路径中的分隔符
	 * @param fn
	 * @return
	 */
	public static String changeSeparator(String fn) {
		if (fn == null) {
			return null;
		}
		if (File.separatorChar != '\\') {
			fn = fn.replace('\\', File.separatorChar);
		}
		return fn;
	}

	public static final int LISTFILE_OPTION_RECUR = 0X1;//是否递归遍历子目录

	public static final int LISTFILE_OPTION_INCLUDEDIR = 0X2;//是否遍历目录名

	public static final int LISTFILE_OPTION_EXCLUDEFILE = 0X4;//是否不遍历文件

	/**
	 * path是一个绝对路径，可以不以\(unix上是/)结尾，
	 * filter是一个正则表达式，可以是空，也可以包含*和?,如果传递了filter则只返回符合模式的文件或目录
	 * 例：要查找sandown(1,0,0,2).cab格式的所有版本文件
	 *  java代码：  filter = "sandown.*\\([0-9]*,[0-9]*,[0-9]*,[0-9]*\\)\\.cab";
	 * option是LISTFILE_OPTION_RECUR，LISTFILE_OPTION_INCLUDEDIR和LISTFILE_OPTION_EXCLUDEFILE的组合
	 * @param path
	 * @param filter
	 * @param option
	 * @return
	 */
	public static File[] listFiles(String path, String filter, int option) {
		if (path == null || path.length() == 0)
			return null;
		boolean rec = LISTFILE_OPTION_RECUR == (option & LISTFILE_OPTION_RECUR);
		boolean includeDir = LISTFILE_OPTION_INCLUDEDIR == (option & LISTFILE_OPTION_INCLUDEDIR);
		boolean excludeFile = LISTFILE_OPTION_EXCLUDEFILE == (option & LISTFILE_OPTION_EXCLUDEFILE);
		File dir = new File(path);
		Pattern p = null;
		if (filter != null) {
			p = Pattern.compile(filter);
		}
		List l = new ArrayList();
		listFilesRec(l, dir, p, rec, includeDir, excludeFile);
		File[] result = new File[l.size()];
		l.toArray(result);
		return result;
	}

	private static void listFilesRec(List l, File dir, Pattern p, boolean rec, boolean includeDir, boolean excludeFile) {
		File[] fs = dir.listFiles();
		if (fs == null)
			return;
		for (int i = 0; i < fs.length; i++) {
			File f = fs[i];
			//遍历文件
			if (f.isFile() && !excludeFile) {
				if (p == null || p.matcher(f.getName()).find())
					l.add(f);
			}
			//遍历目录
			if (f.isDirectory()) {
				if (includeDir) {
					if (p == null || p.matcher(f.getName()).find())
						l.add(f);
				}
				if (rec)
					listFilesRec(l, f, p, rec, includeDir, excludeFile);
			}
		}
	}

	/**
	 * 提取文件名后缀
	 * 
	 * @param fn
	 * @return 文件后缀串，带点，比如".COD"，".txt"
	 */
	public static String extractFileExt(String fn) {
		int i = fn.lastIndexOf('.');
		int j = lastIndexOfPathSeparator(fn); //separator leng always 1
		if (j >= i) {
			return null; //e.g. cc.c/aa;= means i==j==-1
		}
		if ((i < 0) || (i == fn.length() - 1)) {
			return null;
		}
		//如果i==0,则传入的就是一个文件后缀,直接返回
		return i == 0 ? fn : fn.substring(i, fn.length());
	}

	public static String extractFileName(String fn) {
		int j = lastIndexOfPathSeparator(fn);
		if (j == -1) {
			return fn;
		}
		return (fn.substring(j + 1));
	}

	public final static String extractFileDir(String fn) {
		int i = lastIndexOfPathSeparator(fn);
		if (i < 0) {
			return "";
		} else {
			return fn.substring(0, i);
		}
	}

	public static String extractFileName(String fn, boolean includeExt) {
		int j = lastIndexOfPathSeparator(fn);
		String s;
		if (j == -1) {
			s = fn;
		} else {
			s = fn.substring(j + 1);
		}
		if (!includeExt) {
			return excludeFileExt(s);
		}
		return s;
	}

	public static int lastIndexOf(String str, char ch) {
		if (str == null)
			return -1;

		int length = str.length();
		for (int i = length - 1; i >= 0; i--) {
			if (str.charAt(i) == ch) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * 去掉文件的扩展名，返回剩下的部分
	 * @param fn
	 * @return
	 */
	public static final String excludeFileExt(String fn) {
		if (fn == null)
			return fn;
		int i = lastIndexOf(fn, '.');
		if (i != -1) {
			fn = fn.substring(0, i);
		}
		return fn;
	}

	/**
	 * 从s的后面开始向前寻找/或者\，返回找到的字符对应的序号，s为null或者没有找到都返回-1
	 * @param str
	 * @return
	 */
	public static int lastIndexOfPathSeparator(String str) {
		if (str == null)
			return -1;

		int length = str.length();
		for (int i = length - 1; i >= 0; i--) {
			char c = str.charAt(i);
			if (c == '/' || c == '\\') {
				return i;
			}
		}

		return -1;
	}

	/**
	 * 删除文件路径中的驱动符号
	 * @param fn
	 * @return 不含驱动符号的文件名
	 */
	public static String delDriver(String fn) {
		int i = fn.indexOf(":");
		if (i == fn.length() - 1) {
			return null;
		}
		if (i != -1) {
			fn = fn.substring(i + 1, fn.length());
		}
		return fn;
	}

	/**
	 * 删除文件,可以是文件也可以是目录
	 */
	public static boolean remove(String file) {
		if (StrFunc.isNull(file))
			return true;
		return remove(new File(file));
	}

	public static boolean remove(File file) {
		if (file == null || !file.exists())
			return true;
		return file.isFile() ? removeFile(file.getAbsolutePath()) : removeDir(file);
	}

	public static boolean removeFile(String file) {
		if (file == null || file.length() == 0)
			return true;

		File f = new File(file);
		if (!f.exists() || !f.isFile())
			return false;

		return f.delete();
	}

	/**
	 * 删除目录
	 * @param dir 目录名
	 * @return 如果目录下所有文件删除成功，返回true，否则false;
	 */
	public static boolean removeDir(String dir) {
		if (dir == null || dir.length() == 0) {
			return true;
		}
		File f = new File(dir);
		return removeDir(f);
	}

	/**
	 * 删除目录
	 * @param dir 目录文件
	 * @return 如果目录下所有文件删除成功，返回true，否则false;
	 * 一遇到无法成功删除的文件即中止
	 */
	public static boolean removeDir(File dir) {
		if (!dirExists(dir)) {
			return false;
		}
		if (!clearDir(dir)) {
			return false;
		}
		if (!dir.delete()) {
			return false;
		}
		return true;
	}

	/**
	 * 清空一个文件夹内的所有文件
	 */
	public static boolean clearDir(File dir) {
		File[] files = dir.listFiles();
		for (int i = 0, len = files == null ? 0 : files.length; i < len; i++) {
			File file = files[i];
			if (file.isFile()) {
				if (!file.delete()) {
					return false;
				}
			} else if (!removeDir(file)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 创建目录
	 * @param dir
	 * @return 成功创建返回true；
	 */
	public static boolean createDir(String dir) {
		File f = new File(dir);
		return f.mkdirs();
	}

	/**
	 * 创建给定的目录及其所有父目录,创建不成功会触发异常。
	 * 如果目录已存在则直接返回
	 * 传入的目录名可以带后面的斜杠
	 * @param dir
	 * @return
	 */
	public static void forceDir(String dir) {
		File f = new File(excludePathSeparator(dir));
		if (f.exists() && f.isDirectory()) {
			return;
		}
		if (!f.mkdirs()) {
			//throw new java.lang.RuntimeException("无法创建目录：" + dir);
			throw new RuntimeException(I18N.getString("com.esen.util.FileFunc.1", "无法创建目录： ") + dir);
		}
	}

	/**
	 * 创建文件所在路径中的所有目录
	 * @param f
	 * @return成功创建返回true；
	 */
	public static boolean createDirsOfFile(File f) {
		return f.getParentFile().mkdirs();
	}

	/**
	 * 将源目录下的所有文件移动到另一个目录下
	 * @param srcDir 原目录名
	 * @param desDir 目的目录名
	 * @param overwrite 是否覆盖
	 * @return 若所有文件成功移动，返回true，否则false
	 * 如果目的文件已经存在，而overwrite=false，即不可替换，继续移动其他文件;
	 */
	//rename all files under srcDir to desDir, including subdirs;
	public static boolean moveDir(String srcDir, String desDir, boolean overwrite) {
		File sdir = new File(srcDir);
		File desf = new File(desDir);
		if (!dirExists(sdir)) {
			return false;
		}
		if (!dirExists(desf)) {
			return sdir.renameTo(desf);
		}

		File[] files = sdir.listFiles();
		String s;
		for (int i = 0; i < files.length; i++) {
			s = File.separator + files[i].getName();
			desf = new File(desDir + s);
			if (files[i].isDirectory()) {
				if (!moveDir(srcDir + s, desDir + s, overwrite)) {
					return false;
				}
			} else {
				if (desf.exists()) {
					if (!overwrite) {
						continue;
					}
					if (!desf.delete()) {
						return false;
					}
				}
				if (!files[i].renameTo(desf)) {
					return false;
				}
			}
		}
		return true;
	}

	public static final void mkdirs(String dir) {
		File dd = new File(dir);
		if (!dd.exists() || !dd.isDirectory()) {
			if (!dd.mkdirs()) {
				//throw new RuntimeException("无法创建目录："+dir);
				throw new RuntimeException(I18N.getString("com.esen.util.FileFunc.4", "无法创建目录：") + dir);
			}
		}
	}

	/**
	 * 重命名一个目录或文件，不成功触发异常。
	 * @param afrom String
	 * @param ato String
	 */
	public static final void rename(String afrom, String ato) {
		File ff = new File(afrom);
		if (!ff.exists()) {
			//throw new RuntimeException("要重命名的文件或目录不存在："+afrom);
			throw new RuntimeException(I18N.getString("com.esen.util.FileFunc.5", "要重命名的文件或目录不存在：") + afrom);
		}
		File ft = new File(ato);
		if (ft.exists()) {
			//throw new RuntimeException("要重命名的目的文件或目录已存在："+ato);
			throw new RuntimeException(I18N.getString("com.esen.util.FileFunc.6", "要重命名的目的文件或目录已存在：") + ato);
		}
		if (!ff.renameTo(ft)) {
			//throw new RuntimeException("无法重命名from:"+afrom+" ;to:"+ato);
			throw new RuntimeException(
					I18N.getString("com.esen.util.FileFunc.7", "无法重命名from:{0} ;to:{1}", new String[] { afrom, ato }));
		}
	}

	public static String includePathSeparator(String s) {
		if (s == null || s.length() == 0) {
			return s;
		}
		if (s.charAt(s.length() - 1) != File.separatorChar) {
			return s + File.separatorChar;
		} else {
			return s;
		}
	}

	/**
	 * 确保路径s最末一个字符不是\或者/
	 * @param s
	 * @return
	 */
	public static String excludePathSeparator(String s) {
		if (s == null || s.length() == 0) {
			return s;
		}
		if (s.charAt(s.length() - 1) != File.separatorChar) {
			return s;
		} else {
			return s.substring(0, s.length() - 1);
		}
	}

	public static boolean fileExists(String fn) {
		File f = new File(fn);
		return f.exists() && f.isFile();
	}

	/**
	 * 判断目录是否存在
	 * @param dir 目录名
	 * @return
	 */
	public static boolean dirExists(String dir) {
		File dd = new File(dir);
		return dirExists(dd);
	}

	/**
	 * 判断一个目录是否存在并返回，如果目录不存在并且throwIfNotExists＝true则触发异常
	 * @param dir
	 * @param throwIfNotExists
	 * @return
	 * @throws java.lang.Exception
	 */
	public static boolean dirExists(String dir, boolean throwIfNotExists) throws Exception {
		boolean exists = dirExists(dir);
		if (!exists && throwIfNotExists) {
			//throw new Exception("目录不存在：" + dir);
			throw new Exception(I18N.getString("com.esen.util.FileFunc.8", "目录不存在：") + dir);

		}
		return exists;
	}

	/**
	 * 判断目录是否存在
	 * @param dir 目录文件指针
	 * @return
	 */
	public static boolean dirExists(File dir) {
		return (dir.exists() && dir.isDirectory());
	}

	/**
	 * 将文件fn中的内容读出作为一个字符串返回
	 * @param fn
	 * @return
	 */
	public static String file2str(String fn) {
		return readFileToStr(fn);
	}

	public static String file2str(String fn, String charset) {
		return readFileToStr(fn, charset);
	}

	public static String readFileToStr(String fn) {
		return readFileToStr(fn, null);
	}

	public static String readFileToStr(String fn, String charset) {
		FileInputStream in = null;
		try {
			in = new FileInputStream(fn);
			try {
				byte[] buf = new byte[in.available()];
				int r = in.read(buf);
				return charset == null ? new String(buf, 0, r) : new String(buf, 0, r, charset);
			} finally {
				in.close();
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return "";
	}

	/**
	 * 将s写入文件fn中
	 * @param fn
	 * @param s
	 */
	public static void str2file(String fn, String s) {
		writeStrToFile(fn, s);
	}

	/**
	 * 将s写入文件fn中
	 * @param fn
	 * @param s
	 */
	public static void str2file(String fn, String s, String charset) {
		writeStrToFile(fn, s, false, charset);
	}

	public static void writeStrToFile(String fn, String s) {
		writeStrToFile(fn, s, false);
	}

	public static void writeStrToFile(String fn, String s, boolean append) {
		writeStrToFile(fn, s, append, null);
	}

	/**
	 * 
	 * @param fn 文件名
	 * @param s 字符
	 * @param append 是否用增加的方式写入字符串
	 */
	public static void writeStrToFile(String fn, String s, boolean append, String charset) {
		try {
			File fd = new File(fn).getParentFile();
			if (!fd.exists() || !fd.isDirectory()) {
				if (!fd.mkdirs())
					throw new RuntimeException("can't create dir :" + fd.getAbsolutePath());
			}
			FileOutputStream out = new FileOutputStream(fn, append);
			try {
				if (s != null)
					out.write(charset == null ? s.getBytes() : s.getBytes(charset));
			} finally {
				out.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			//throw ex;
		}
	}

	public static void buf2file(byte[] buf, String fn) throws IOException {
		FileOutputStream out = new FileOutputStream(fn);
		try {
			out.write(buf);
			out.flush();
		} finally {
			out.close();
		}
	}

	/**
	 * 将文件内容转换成byte数组返回
	 * ,如果文件不存在或者读入错误返回null
	 * @param fn
	 * @return
	 */
	public static byte[] file2buf(String fn) {
		if (fn == null || fn.length() == 0) {
			return null;
		}

		File fobj = new File(fn);
		return file2buf(fobj);
	}

	/**
	 * 将文件内容转换成byte数组返回
	 * ,如果文件不存在或者读入错误返回null
	 * @param fn
	 * @return
	 */
	public static byte[] file2buf(File fobj) {
		if (fobj == null || !fobj.exists() || !fobj.isFile()) {
			return null;
		}

		FileInputStream in = null;
		try {
			in = new FileInputStream(fobj);
			try {
				byte[] buf = new byte[in.available()];
				in.read(buf);
				return buf;
			} finally {
				in.close();
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 文件名内不能包含的字符
	 */
	public final static String invalidFnChar = "/\\:*?\"\'<>|\r\n\t\b\f";//增加了一些不能包含特殊字符

	/**
	 *文件名不能包含的字符提示,增加了转义,报错时使用于前台显示 
	 *ISSUE:BI-8584 add by wandj 2013.6.18
	 * */
	private final static String invalidFnCharthrow = "/\\:*?\"\'<>|\\r\\n\\t\\b\\f";

	/**
	 * 将给定的文件名格式化成一个合法的文件名
	 * @param fn
	 * @return
	 */
	public static String formatFileName(String fn) {
		if (fn == null || fn.length() == 0) {
			return "unknwon";
		}
		StringBuffer sb = new StringBuffer(fn.trim());
		for (int i = 0; i < sb.length();) {
			if (invalidFnChar.indexOf(sb.charAt(i)) != -1) {
				sb.deleteCharAt(i);
			} else {
				i++;
			}
		}

		//文件名不能以.开头,在删除非法字符后处理
		if (isAll_Dot_Blank(sb.toString())) {
			return "unknwon";
		}

		if (sb.length() > 150) {//放宽文件名长度限制
			String ext = FileFunc.extractFileExt(sb.toString());
			return sb.substring(0, 145) + (ext == null ? "" : ext);
		}
		return (sb.length() > 0) ? sb.toString() : "unknwon";
	}

	public static final boolean isValidFileName(String fn) {
		if (fn == null || fn.length() == 0 || fn.length() > 256) {//fix me fn.length()是unicode长度
			return false;
		}
		for (int i = 0; i < fn.length(); i++) {
			if (invalidFnChar.indexOf(fn.charAt(i)) != -1) {
				return false;
			}
		}

		//文件名可以以.开头，但不能只是.或者..，因为这两个字符串有特殊意义
		if (isAll_Dot_Blank(fn)) {
			return false;
		}

		return true;
	}

	/**
	 * 字符s是否都是由空格和点号组成的，如果是这样，那不是一个合法的字符画
	 */
	private static final boolean isAll_Dot_Blank(String s) {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c != '.' && c != '\n' && c != '\r' && c != '\t' && c != ' ') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 将原来的文件更名为*.*~.backup,备份起来,
	 * 如:c:\test.ini  ---->  c:\test.ini~.backup;
	 * 如果c:\test.ini~.backup存在则删除它
	 * @param fn
	 */
	public static void backupFile(String fn) {
		String s = fn + "~.backup";
		File f = new File(s);
		if (f.exists() && f.isFile()) {
			f.delete();
		}
		File oldf = new File(fn);
		oldf.renameTo(f);
	}

	public static void backupDir(String dir) {
		String s = dir + "~.backup";
		File f = new File(s);
		if (f.exists() && f.isDirectory()) {
			removeDir(f);
		}
		File oldf = new File(dir);
		oldf.renameTo(f);
	}

	/**
	 * 文件列表排序 p路径 type排序类型 desc是否升序还是降序
	 * @param p String
	 * @param type int
	 * @param desc boolean
	 * @throws Exception
	 * @return File[]
	 */
	public static final File[] listFiles(String p, int type, boolean desc) throws Exception {
		if (p == null || p.length() == 0) {
			return null;
		}
		File fn = new File(p);
		File[] list = fn.listFiles();
		if (list == null) {
			return null;
		}
		Arrays.sort(list, new FileComparator(type, desc));
		return list;
	}

	/**
	 * 返回p+f 确保中间用separatorChar分割
	 */
	public static final String makeFullPath(String p, String f, char separatorChar) {
		if (p == null || f == null || p.length() + f.length() == 0)
			throw new RuntimeException("impossible!");

		char c = separatorChar == '/' ? '\\' : '/';
		if (p.indexOf(c) >= 0)
			p = p.replace(c, separatorChar);
		if (f.indexOf(c) >= 0)
			f = f.replace(c, separatorChar);
		if (p.charAt(p.length() - 1) != separatorChar && f.charAt(0) != separatorChar) {
			return p + separatorChar + f;
		}
		if (p.charAt(p.length() - 1) == separatorChar && f.charAt(0) == separatorChar) {
			return p + f.substring(1);
		}
		return p + f;
	}

	/**
	 * 判断一个路径是否是绝对路径
	 * @param fn
	 * @return
	 */
	public static boolean isAbsolutePath(String fn) {
		if (fn == null || fn.length() == 0) {
			return false;
		}

		if (File.separator.equals("\\")) { //表示是windows系统
			//第一个字母必须是A..z,第二个必须是:
			if (fn.trim().length() > 1 && StrFunc.isABC_xyz(fn.trim().charAt(0)) && fn.trim().charAt(1) == ':') {
				return true;
			} else {
				return false;
			}
		} else {
			// 第一个必须是"/"
			return (fn.trim().charAt(0) == '/');
		}
	}

	/**
	 * 对路径中的文件名格式化,去除不合法的字符
	 */
	private static String formatZipName(String path) {
		path = includeSeparatorHeadExcludeTrail(path);
		String parentdir = FileFunc.extractFileDir(path);
		String filename = FileFunc.extractFileName(path);
		if (filename.length() != 0) {
			filename = formatFileName(filename);
		}
		return parentdir + "/" + filename;
	}

	/**
	 * 在dir(以File.separatorChar结尾)目录下查找与fn（忽略大小写后）同名的文件，并返回该文件，如果有多个同名文件，则返回null
	 * dir为绝对路径,fn为文件名
	 * @param dir
	 * @param fn
	 * @return
	 */
	public final static File findFileIgnoreCase(String dir, String fn) {
		if (File.separatorChar == '\\') {
			String path = dir + fn;
			File file = new File(path);
			if (file.exists()) {
				return file;
			}
			return null;
		}
		String[] list = new File(dir).list();
		if (list == null || list.length == 0) {
			return null;
		}
		int count = 0;
		String name = "";
		for (int i = 0; i < list.length; i++) {
			if (list[i].equalsIgnoreCase(fn)) {
				name = list[i];
				count++;
				if (count >= 2) {//两个或两个以上文件
					return null;
				}
			}
		}
		if (count == 1) {
			return new File(dir + name);
		}
		return null;
	}

	public static String formatFullPath(String dir, String fn) {
		if (isAbsolutePath(fn)) {
			return fn;
		}
		if (dir == null || dir.length() == 0) {
			throw new RuntimeException("impossible!");
		}
		// update by jzp 2018.10.4 要支持fn为空的情形，不能直接抛异常
		if (fn == null || fn.length() == 0) {
			return dir;
		} else {
			char sr = (File.separatorChar == '\\') ? '/' : '\\';
			dir = dir.replace(sr, File.separatorChar);
			fn = fn.replace(sr, File.separatorChar);
			String f = fn;
			String p = dir;
			if (p.charAt(p.length() - 1) != File.separatorChar && f.charAt(0) != File.separatorChar) {
				return p + File.separatorChar + f;
			}
			if (p.charAt(p.length() - 1) == File.separatorChar && f.charAt(0) == File.separatorChar) {
				return p + f.substring(1);
			}
			return p + f;
		}

	}

	/**
	 * 假设当前的目录时pwd，用户想切换到目录cd，那么次函数就返回切换后的那个目录的绝对路径
	 * 例如：
	 *   cd_unixlike("/abc","d") 返回 /abc/d
	 *   cd_unixlike("/abc","/d") 返回/d
	 *   cd_unixlike("/abc",".") 返回 /abc
	 *   cd_unixlike("/abc","./d") 返回 /abc/d
	 *   cd_unixlike("/abc","../d") 返回 /d
	 *   cd_unixlike("/abc","..") 返回 /
	 *   cd_unixlike("/abc/d","../../d") 返回 /d
	 *   
	 * @param pwd 文件路径,如/abc/d/linuxabc/d或abc/d
	 * @param cd 绝对文件路径或相对文件路径
	 *       <br>绝对文件路径以/开头,如cd_unixlike("/abc","/d")返回/d
	 *       <br>相对文件路径中支持./(当前目录)和../(上级目录),如cd_unixlike("/abc","./d")返回 /abc/d,cd_unixlike("/abc","../../d")返回 /d
	 *       <br>如果相对文件路径只有一个字符.或长度大于1,最后两个字符是/.则也表示当前目录,如"."或"../.","../../."
	 *       <br>如果相对文件路径只有两个字符..或长度大于2,最后三个字符是/..则也表示上级目录,如".."或"../..
	 *       <br>相对路径中的./和../必须在路径的最前面,并且../必须在./的前面,否则无法识别.如../d/../和./../d是无法获得正确的目录的
	 *       <br>相对路径中只支持存在一个./,../支持存在无限多个
	 */
	public static String cd_unixlike(String pwd, String cd) {
		if (StrFunc.isNull(cd)) {
			return pwd;
		}
		if (cd.charAt(0) == '/') {
			return cd;
		}

		//cd . 应该就是pwd
		if (cd.length() == 1 && cd.charAt(0) == '.') {
			return pwd;
		}

		while (cd.startsWith("..")) {
			if (cd.length() == 2) {
				cd = "";
			} else if (cd.charAt(2) == '/') {
				cd = cd.substring(3);
			} else {
				break; //cd_unixlike("/abc","..ad/d") ?
			}
			int index = pwd.lastIndexOf('/');
			pwd = index >= 0 ? pwd.substring(0, index) : "";
		}

		if (cd.startsWith("./")) {
			cd = cd.substring(2);
		}

		if (cd.length() == 0) {
			return pwd == null || pwd.length() == 0 ? "/" : pwd;
		} else {
			return pwd == null || pwd.length() == 0 ? "/" + cd : pwd + "/" + cd;
		}
	}

	/**
	 * 将路径中的分隔符转化为当前操作系统的分隔符。
	 */
	public static String formatPath(String path) {
		if (path == null || path.length() == 0) {
			throw new RuntimeException("path is null!");
		}
		char sr = (File.separatorChar == '\\') ? '/' : '\\';
		path = path.replace(sr, File.separatorChar);
		return path;
	}

	/**
	 * 格式化成unix目录格式。如果目录为空或者""，都返回/，其目录必须为下列格式：
	 *   /ab\aa
	 *   bb\cc
	 * @param dir
	 * @return
	 */
	public static String formatUnixDir(String dir) {
		if (dir == null || dir.length() == 0) {
			return "/";
		}
		if (!isUnixDir(dir, 0, -1))
			return formatUnixDir(dir, 0, -1);
		return dir;
	}

	/**
	 * 把字符串中的某一段格式化成unix文件目录，例如：
	 *   ""          "/"
	 *   ab/ac       /ab/ac
	 *   //          /
	 *   \\          /
	 *   /ab/ab/     /ab/ab  ?
	 *   /ab\ac\     /ab/ac  ? 
	 * @param dir
	 * @param start
	 * @param end
	 * @return
	 */
	public static String formatUnixDir(String dir, int start, int end) {
		if (dir == null || dir.length() == 0 || (dir.length() == 1 && (dir.charAt(0) == '/' || dir.charAt(0) == '\\'))) {
			return "/";
		}

		int len = dir.length();
		if (end == -1) {
			end = len - 1;
		}

		if (start > end || (start == end && dir.charAt(start) == '/')) {
			return dir;
		}

		StringBuffer sb = new StringBuffer(len + 2).append(dir);
		char ch = sb.charAt(end);
		if ((end > start) && ch == '/' || ch == '\\') {
			sb.deleteCharAt(end);
		}

		char lastChar = ch;

		for (int i = end - 1; i >= start; i--) {
			ch = sb.charAt(i);
			if ((ch == '/' || ch == '\\') && (lastChar == '/' || lastChar == '\\')) {
				sb.deleteCharAt(i);
				continue;
			}
			if (ch == '\\') {
				sb.setCharAt(i, '/');
			}
			lastChar = ch;
		}

		if ((lastChar != '/' && lastChar != '\\') || sb.length() == 0) {
			sb.insert(start, '/');
		}

		return sb.toString();
	}

	/**
	 * 判断字符串的某一段是否是UNIX格式目录，要注意 "/" 的判断，end 可以为-1，表示off开始的所有字符串。有可能
	 * 字符串包含连续的// 、\\\\ 、/\ 等，但只保留一个
	 * @param dir
	 * @param off
	 * @param end
	 * @return
	 */
	public static boolean isUnixDir(String dir, int off, int end) {
		if (end == -1) {
			end = dir.length() - 1;
			if (end == -1)
				return false;
		}

		char startChar = dir.charAt(off);
		if (off == end) {
			return startChar == '/';
		}

		if (startChar != '/') {
			return false;
		}

		char ch = dir.charAt(end);
		if (ch == '/' || ch == '\\') {
			return false;
		}

		/**
		 * 判断off+1 到  end 之间，有没有连续的/\，或者有\
		 */
		char lastChar = startChar;
		for (int i = off + 1; i < end; i++) {
			ch = dir.charAt(i);
			if (ch == '\\') {
				return false;
			}

			if (ch == '/' && lastChar == '/') {
				return false;
			}
			lastChar = ch;
		}

		return true;
	}

	/**
	 * 判断文件或目录是否存在，如果不存在则创建它，如果创建不成功，则触发异常
	 * 如果已存在，但不是isdir并且isoverwrite=false时，触发异常
	 * @param path 
	 * @param isdir 是否是目录
	 * @param isoverwrite 当文件存在并与isdir不一致时是否覆盖
	 * @throws Exception 
	 * 
	 * by zcx 090522 在文件创建不成功时抛出的异常要带上路径,否则无法知道是哪个文件创建不成功
	 */
	public static void ensureExists(File file, boolean isdir, boolean isoverwrite) throws Exception {
		if (file.exists()) {
			boolean b = file.isDirectory();
			if (b == isdir) {
				return;
			}
			//文件类型与isdir不一致
			if (!isoverwrite) {
				//throw new Exception("该路径对应的" + (isdir ? "目录" : "文件") + "已存在:"+file.getAbsolutePath());
				throw new Exception(I18N.getString("com.esen.util.FileFunc.11", "该路径对应的{0}已存在:{1}",
						new String[] { (isdir ? I18N.getString("com.esen.util.FileFunc.12", "目录")
								: I18N.getString("com.esen.util.FileFunc.13", "文件")), file.getAbsolutePath() }));
			} else {
				file.delete();
			}
		}
		if (isdir) {
			if (!file.mkdirs()) {
				//throw new Exception("目录创建不成功:"+file.getAbsolutePath());
				throw new Exception(I18N.getString("com.esen.util.FileFunc.14", "目录创建不成功:") + file.getAbsolutePath());
			}
		} else {
			File pfile = file.getParentFile();
			if (!pfile.exists()) {
				if (!pfile.mkdirs()) {
					//throw new Exception("文件创建不成功:"+file.getAbsolutePath());
					throw new Exception(I18N.getString("com.esen.util.FileFunc.15", "文件创建不成功:") + file.getAbsolutePath());
				}
			}
			if (!file.createNewFile()) {
				//throw new Exception("文件创建不成功:"+file.getAbsolutePath());
				throw new Exception(I18N.getString("com.esen.util.FileFunc.16", "文件创建不成功:") + file.getAbsolutePath());
			}
		}
	}

	/*打开一个文件*/
	public static void openFile(String fileName) {
		try {
			Thread.sleep(2000);
			Runtime.getRuntime().exec("cmd /C " + fileName);
			System.out.println("opened file:" + fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将path中的\\转换成/
	 * @param path
	 * @return
	 */
	public static String replaceSeparatorChar(String path) {
		String reg = "((\\\\+|/+)(\\\\*/*)*)+";
		return path.replaceAll(reg, "/");
	}

	public static final char SEPCHAR = '/';

	public static final String SEPSTR = "" + SEPCHAR;

	/**
	 * 路径以/开头，不处理结尾
	 * @param path
	 * @return
	 */
	public static String includeSeparatorHead(String path) {
		if (path == null || path.length() == 0) {
			return SEPSTR;
		}
		path = replaceSeparatorChar(path);
		if (path.charAt(0) != SEPCHAR) {
			return SEPCHAR + path;
		}
		return path;
	}

	/**
	 * 路径不以/开头，不处理结尾
	 * @param path
	 * @return
	 */
	public static String excludeSeparatorHead(String path) {
		if (path == null || path.length() == 0) {
			return "";
		}
		path = replaceSeparatorChar(path);
		if (path.charAt(0) == SEPCHAR) {
			return path.substring(1);
		}
		return path;
	}

	/**
	 * 路径以/结尾，不处理开头
	 * @param path
	 * @return
	 */
	public static String includeSeparatorTrail(String path) {
		if (path == null || path.length() == 0) {
			return SEPSTR;
		}
		path = replaceSeparatorChar(path);
		int len = path.length();
		if (path.charAt(len - 1) != SEPCHAR) {
			return path + SEPCHAR;
		}
		return path;
	}

	/**
	 * 返回文件路径，不以/结尾，不处理开头
	 * @param path
	 * @return
	 */
	public static String excludeSeparatorTrail(String path) {
		if (path == null || path.length() == 0) {
			return SEPSTR;
		}
		path = replaceSeparatorChar(path);
		int len = path.length();
		if (len > 1 && path.charAt(len - 1) == SEPCHAR) {
			return path.substring(0, len - 1);
		}
		return path;
	}

	/**
	 * 路径以/开头，以/结尾，处理开头和结尾
	 * @param path
	 * @return
	 */
	public static String includeSeparatorBoth(String path) {
		return includeSeparatorHead(includeSeparatorTrail(path));
	}

	/**
	 * 路径以/开头，不以/结尾，处理开头和结尾
	 * @param path
	 * @return
	 */
	public static String includeSeparatorHeadExcludeTrail(String path) {
		return excludeSeparatorTrail(includeSeparatorHead(path));
	}

	/**
	 * 返回文件parentdir,以/开头，以/结尾,处理开头和结尾
	 * @param path
	 * @return
	 */
	public static String extractParentDirIncludeSeparatorHead(String path) {
		path = includeSeparatorHeadExcludeTrail(path);
		return path.substring(0, path.lastIndexOf(SEPSTR) + SEPSTR.length());
	}

	/**
	 * 返回文件parentdir,以/结尾，不处理开头
	 * @param path
	 * @return
	 */
	public static String extractParentDir(String path) {
		path = replaceSeparatorChar(path);
		return path.substring(0, path.lastIndexOf(SEPSTR) + SEPSTR.length());
	}

	/**
	 * 返回文件名
	 * @param path
	 * @return
	 */
	public static String extractName(String path) {
		path = excludeSeparatorTrail(path);
		return path.substring(path.lastIndexOf(SEPSTR) + SEPSTR.length());
	}

	/**
	 * 获取指定文件或目录的大小，当计算的目录过大时，不赞成用该方法，因为会耗费大量时间
	 * 建议改用sizeofdir(File fileOrDir, long maxSize)方法
	 */
	public static long sizeofdir(File dir) {
		return sizeofdir(dir, Long.MAX_VALUE);
	}

	/**
	 * 获取指定文件或目录的大小，通过添加一个maxSize参数，当计算的结果大于或等于该数值时，停止计算，这样可以避免
	 * 在计算资源大小时耗费大量时间。 
	 * 
	 * @param fileOrDir
	 * @param maxSize 当已经计算的大小大于或等于这个值时，不再进行计算
	 * @return
	 */
	public static long sizeofdir(File fileOrDir, long maxSize) {
		long size = 0;
		if (fileOrDir.isFile()) {
			size = fileOrDir.length();
		} else {
			File[] subFiles = fileOrDir.listFiles();
			for (int i = 0; subFiles != null && i < subFiles.length; i++) {
				size += sizeofdir(subFiles[i], maxSize - size);
				if (size >= maxSize)
					break;
			}
		}
		return size;
	}
}

/**
 * 对文件排序,如果type>0,按时间排序,否则按大小排序
 * desc确定是否降序排序
 * @author zhuchx
 */
class FileComparator implements Comparator {
	private int _type = 0;

	private boolean _desc = true;

	public FileComparator(int type, boolean desc) {
		_type = type;
		_desc = desc;
	}

	public int compare(Object o1, Object o2) {
		File f1 = (File) o1;
		File f2 = (File) o2;
		long result = _type > 0 ? f1.lastModified() - f2.lastModified() : f1.length() - f2.length();
		if (result > 0) {
			return _desc ? -1 : 1;
		} else if (result == 0) {
			return 0;
		} else {
			return _desc ? 1 : -1;
		}
	}
}
