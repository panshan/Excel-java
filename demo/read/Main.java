package read;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import com.csvreader.CsvReader;

/**
 * 读取csv表格，提取指定列，输出到文件
 * */
public class Main {
	
	/**
	 * 提取表格满足条件的指定行
	 * @throws IOException 
	 * */
	public static void process(String src, String des, int skip) throws IOException {
		CsvReader csvReader = new CsvReader(src, ',', Charset.forName("GBK"));
		
		FileOutputStream fos = new FileOutputStream(des);
		OutputStreamWriter osw = new OutputStreamWriter(fos, Charset.forName("utf-8"));
		BufferedWriter bw = new BufferedWriter(osw);
		
		/**
		 * 跳过开头几行
		 * */
		while (skip-- > 0 && csvReader.readRecord()) {
			csvReader.getValues();
		}
		
		/**
		 * 按行读取正文信息
		 * */
		while (csvReader.readRecord()) {
			String[] values = csvReader.getValues();
			if (Integer.parseInt(values[4]) == 0) {
				System.out.println(values[0] + "\t" + values[1] + "\t" + values[2] + "\t" + values[3] + "\t" + values[4]);
			} else {
				bw.write(values[3] + "\t" + "0" + "\t" + "别名" + "\t" + values[2]);
				bw.write("\n");
			}
		}
		csvReader.close();
		
		bw.close();
		osw.close();
		fos.close();
	}
	
	public static void main(String[] args) throws IOException {
		String src = "";
		String des = "";
		
		process(src, des, 1);
	}
}
