package wikitable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WikiTableEdit {

	public static void main(String[] args) {
		// 将详细结果码sheet页内容转化为wiki格式的txt文件
		try {
			StringBuffer test = readExcel("E:/结果码.xlsx");
			exportTxt("E:/结果码.txt", test);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exportTxt(String str, StringBuffer txt) {
		File file = new File(str);
		FileOutputStream fos1;
		try {
			fos1 = new FileOutputStream(file);
			OutputStreamWriter dos1 = new OutputStreamWriter(fos1);
			dos1.write(txt.toString());
			dos1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取Excel表格内容
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static StringBuffer readExcel(String str) throws FileNotFoundException, IOException {
		
		StringBuffer text = new StringBuffer();
		File file = new File(str);
		// HSSFWorkbook 2003的excel .xls,XSSFWorkbook导入2007的excel   .xlsx
		// HSSFWorkbook workbook=new HSSFWorkbook(new FileInputStream(new File(file)));
		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
		// 读取第一个 sheet
		Sheet sheet = workbook.getSheetAt(0);
		List list = new ArrayList<>();
		Row row = null;
		int count = sheet.getPhysicalNumberOfRows();

		Cell cell0 = null;
		Cell cell1 = null;
		Cell cell2 = null;
		Cell cell3 = null;
		// 逐行处理 excel 数据
		
		text.append("{| class=\"wikitable\"");
		text.append("\r\n");
		text.append("|-");
		text.append("\r\n");
		text.append("! No");
		text.append("\r\n");
		text.append("! 详细结果码");
		text.append("\r\n");
		text.append("! 面向AP");
		text.append("\r\n");
		text.append("! 面向IDSP");
		text.append("\r\n");
		
		for (int i = 1; i < count; i++) {
			
			JSONObject json = new JSONObject();
			row = sheet.getRow(i);
			cell0 = row.getCell(0);
			cell1 = row.getCell(1);
			cell2 = row.getCell(2);
			cell3 = row.getCell(3);
			// 设置取值为String
			// 整数数据要转，否则会变成浮点数
			cell0.setCellType(Cell.CELL_TYPE_STRING);
			cell1.setCellType(Cell.CELL_TYPE_STRING);
			cell2.setCellType(Cell.CELL_TYPE_STRING);
			cell3.setCellType(Cell.CELL_TYPE_STRING);
			
			text.append("|-");
			text.append("\r\n");
			text.append("| " + cell0.toString());
			text.append("\r\n");
			text.append("| " + cell1.toString());
			text.append("\r\n");
			text.append("| " + cell2.toString());
			text.append("\r\n");
			text.append("| " + cell3.toString());
			text.append("\r\n");
		}
		text.append("|}");
		
		workbook.close();
		System.out.println("list:"+list);
		return text;
	}

	/**
	 * 写入Excel表格内容
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings({ "resource", "rawtypes", "unchecked" })
	private static void writeExcel(String str) throws FileNotFoundException, IOException {
		
		File file = new File(str);
		// HSSFWorkbook 2003的excel .xls,XSSFWorkbook导入2007的excel   .xlsx
		// HSSFWorkbook workbook=new HSSFWorkbook(new FileInputStream(new File(file)));
		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
		List resultList = new ArrayList<>();
		
		// 创建 sheet 对象
		Sheet sheet1 = workbook.createSheet();
		// 第一行，标题
		Row row = sheet1.createRow(0);
		row.createCell(0).setCellValue("A");
		row.createCell(1).setCellValue("B");
		row.createCell(2).setCellValue("C");
		row.createCell(3).setCellValue("D");
		row.createCell(4).setCellValue("E");
		// 拼接数据
		for(int i=1;i<=10;i++){
			JSONObject json1=new JSONObject();
			json1.put("A", i);
			json1.put("B", i*2);
			json1.put("C", i*3);
			json1.put("D", i*4);
			json1.put("E", i*5);
			resultList.add(json1);
		}
		System.out.println("resultList:"+resultList);
		Row row1;
		// 循环创建数据行
		for (int i = 1, len = resultList.size(); i <= len; i++) {
			// 第一行已经设置了，从第二行开始
			row1 = sheet1.createRow(i);
			JSONObject json=(JSONObject) resultList.get(i-1);
			row1.createCell(0).setCellValue(json.getString("A"));
			row1.createCell(1).setCellValue(json.getString("B"));
			row1.createCell(2).setCellValue(json.getString("C"));
			row1.createCell(3).setCellValue(json.getString("D"));
			row1.createCell(4).setCellValue(json.getString("E"));
		}
		FileOutputStream fos = new FileOutputStream(file); 
		// 写文件
		workbook.write(fos);
		fos.close();
		System.out.println("写入成功！");
	}
}
