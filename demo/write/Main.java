package write;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 读取文本，保存为.xls格式
 * 输入格式：
 * */
public class Main {
	public static void main(String[] args) throws IOException {
		String src = "";
		String des = "";
		
		process(src, des);
	}
	
	/**
	 * 创建sheet，返回row
	 * */
	public static HSSFSheet getSheet(HSSFWorkbook wb, int sheetNum) {
        HSSFSheet sheet = wb.createSheet("别名表" + sheetNum);// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFRow row = sheet.createRow((int) 0);				// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFCellStyle style = wb.createCellStyle();			// 第四步，创建单元格，并设置值表头居中
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);		// 创建一个居中格式
        
        HSSFCell cell = row.createCell((int)0);
        cell.setCellValue("实体名");
        cell.setCellStyle(style);
        
        cell = row.createCell((int) 1);  
        cell.setCellValue("实体type");  
        cell.setCellStyle(style);  
        
        cell = row.createCell((int) 2);  
        cell.setCellValue("实体别名");  
        cell.setCellStyle(style);  
        
        cell = row.createCell((int) 3);  
        cell.setCellValue("localid");  
        cell.setCellStyle(style);

        cell = row.createCell((int) 4);  
        cell.setCellValue("mark");  
        cell.setCellStyle(style);
		
        return sheet;
	}
	
	public static void process(String src, String des) throws IOException {
		/**
		 * 创建xls文件，并设置头部格式，列数，以及列标题
		 * */
        HSSFWorkbook wb = new HSSFWorkbook();  				// 第一步，创建一个webbook，对应一个Excel文件
        
        int sheetNum = 0;
        int rowNum = 1;

        HSSFSheet sheet = getSheet(wb, sheetNum++);
        
        /**
         * 依次读取文件输入文件的每一行
         * */
        FileInputStream fis = new FileInputStream(src);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        
        String line = null;
        while ( (line=br.readLine()) != null ) {
        	if (rowNum >= 60000) {
        		rowNum = 1;
        		sheet = getSheet(wb, sheetNum++);
        	}
        	
        	String[] title_localid_typeId_name_alias_sb = line.split("\t", 5);
        	String title = title_localid_typeId_name_alias_sb[0];
        	String localid = title_localid_typeId_name_alias_sb[1];
        	String typeId = title_localid_typeId_name_alias_sb[2];
        	String name = title_localid_typeId_name_alias_sb[3];
        	String[] alias_arr = title_localid_typeId_name_alias_sb[4].split("\t");
        	
        	for (String alias : alias_arr) {
        		String[] aliasName_count = alias.split("\\|");
        		HSSFRow row = sheet.createRow(rowNum++);
        		row.createCell(0).setCellValue(name);
        		row.createCell(1).setCellValue(typeId);
        		row.createCell(2).setCellValue(aliasName_count[0]);
        		row.createCell(3).setCellValue(localid);
        	}
        }
        
        br.close();
        isr.close();
        fis.close();
        
        FileOutputStream fout = new FileOutputStream(des);
        wb.write(fout);
        fout.close();
	}
}
