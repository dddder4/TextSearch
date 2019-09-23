import java.io.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.*;

public class TextSearch{
	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("欢迎使用文本搜索器！请先输入要查找的文件或文件夹的路径，按回车后再输入要查找的文本内容。");
		new TSearch().func();
	}
}
class TSearch implements Runnable{
	public static long now = 0;
	public static long size = 0;
	private boolean tflag = true;
	public void func() throws InterruptedException {
		while(true) {
			Scanner sc = new Scanner(System.in);
			String name = sc.nextLine();
			String[] names = name.split("\\\\");
			for(int i = names.length - 1; i > 0; i--) {
				if(!names[i].contains("*")&&!names[i].contains("?")&&!names[i - 1].contains("*")&&!names[i - 1].contains("?")&&names[i].length() > 0&&names[i - 1].length() > 0) {
					names[i - 1] = names[i - 1] + "\\" + names[i];
					names[i] = "";
				}
			}
			for(int i = 0; i < names.length; i++) {
				if(names[i].contains("*")||names[i].contains("?")) {
					names[i] = names[i].replace("$", "\\$");
					names[i] = names[i].replace("^", "\\^");
					names[i] = names[i].replace("+", "\\+");
					names[i] = names[i].replace("(", "\\(");
					names[i] = names[i].replace(")", "\\)");
					names[i] = names[i].replace("*", "(.*)");
					names[i] = names[i].replace("?", "(.)");
				}
			}
			File file = new File(names[0] + "\\");
			String search = sc.nextLine();
			long startTime = System.currentTimeMillis();
			try {
				find(file,names[0],search,names);
			} catch (IOException e) {
				e.printStackTrace();
			}
			long endTime=System.currentTimeMillis();
			System.out.println("搜索时间： " + (endTime - startTime) + "ms");
		}
	}
	@Override
	public synchronized void run() {
		while(tflag) {
			System.out.print("\r" + now + " / " + size);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public static void find(File file,String name,String search,String[] names) throws IOException, InterruptedException{
		if(file.exists()) {
			if(!file.canRead()) {
				System.out.println(name + "不可读！");
			}else {
				if(file.isFile()) {
					System.out.println("在文件" + name + "中查询\"" + search + "\"");
					size = file.length();
					now = 0;
					LineNumberReader lnr = new LineNumberReader(new InputStreamReader(new FileInputStream(file),codeString(name)));
					String s;
					StringBuilder s1 = new StringBuilder();
					TSearch ts = new TSearch();
					Thread t = new Thread(ts);
					t.start();
					while((s = lnr.readLine()) != null) {
						now = now + s.length();
						if(s.contains(search)) {
							s1 = s1.append("Line " + lnr.getLineNumber() + ": " + s + "\n");
						}
					}
					ts.close();
					if(s1.length() == 0) {
						System.out.println("在" + file.getAbsolutePath() + "中没有找到" + search);
					}else {
						System.out.print(s1);
					}
					lnr.close();
				}else if(file.isDirectory()) {
					String s = null;
					File[] files = file.listFiles();
					if(files.length == 0) {
						System.out.println(file.getAbsolutePath() + "文件夹是空的！");
					}else {
						for(int i = 1; i < names.length; i++) {
							if(names[i].length() > 0) {
								s = names[i];
								names[i] = "";
								break;
							}
						}
						boolean flag = false;
						for (File file2 : files) {
							if(s != null) {
								Pattern pattern = Pattern.compile(s);
								Matcher matcher = pattern.matcher(file2.getName());
								if(matcher.find()) {
									flag = true;
									names[0] = file2.getAbsolutePath();
									for(int i = 1; i < names.length; i++) {
										if(names[i].length() > 0) {
											if(!names[i].contains("(.")) {
												names[0] = names[0] + "\\" + names[i];
												names[i] = "";
												file2 = new File(names[0]);
												break;
											}else {
												break;
											}
										}
									}
									find(file2,names[0],search,names);
								}
							}else {
								find(file2,file2.getAbsolutePath(),search,names);
							}
						}
						if(s != null&&flag == false) {
							System.out.println("文件或文件夹不存在");
						}
					}
				}
			}
		}else {
			System.out.println("文件或文件夹不存在！");
		}
	}
	public void close() {
		tflag = false;
		System.out.println("\r" + size + " / " + size);
	}
	public static String codeString(String name) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(name));
		int a = (bis.read()<<8) + bis.read();
		String code = null;
		switch(a) {
		case 0xefbb:
			code = "utf-8";
			break;
		case 0xfffe:
			code = "unicode";
			break;
		case 0xfeff:
			code = "utf-16be";
			break;
		default:
			code = "gbk";
		}
		return code;
	}
}