package eidcarrier;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.eidlink.common.utils.CertPartitionUtils;


/**
 * 更新表t_eid_carrier_v1操作，生成可执行sql文件
 * @author song
 *
 */
public class UpdateTeidCarrierV1 {

	public static void main(String[] args) {
		
		// 执行：select carrier_id from t_eid_carrier_v1
		// 导出csv
		String csv_filepath = "D:\\idcarrier.csv";
		String txt_filepath = "D:\\idcarrier.txt";
		List<String> csvList = readCSV(csv_filepath);

		StringBuffer updateSQL = new StringBuffer();
		String carrier_id = "";
		String sql = "";
		int second_byte = 0;
		for (int m = 0; m < csvList.size(); m++) {
			carrier_id = csvList.get(m).trim();
			second_byte =  CertPartitionUtils.getCarrierPartitionNo(carrier_id);
			sql = "UPDATE t_eid_carrier_v1 SET second_byte = " + second_byte + " WHERE carrier_id = '" + carrier_id + "';";
			updateSQL = updateSQL.append(sql).append(System.getProperty("line.separator"));  
		}

		try {
			writeToText(updateSQL.toString(), txt_filepath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 按行读取CSV文件
	 */
	public static List<String> readCSV(String filepath) {
		// CSV文件路径
		File csv = new File(filepath);
		List<String> csvList = new ArrayList<>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(csv));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line = "";
		String everyLine = "";
		try {
			// 读取到的内容给line变量
			while ((line = br.readLine()) != null) {
				everyLine = line;
				System.out.println(everyLine);
				csvList.add(everyLine);
			}
			System.out.println("csv数据行数：" + csvList.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return csvList;
	}

	/**
	 * 生成txt文件
	 */
	public static void writeToText(String musicInfo, String txt_filepath) throws IOException {
		// 生成的文件路径
		File file = new File(txt_filepath);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
		}
		file.createNewFile();
		// write 解决中文乱码问题
		// FileWriter fw = new FileWriter(file, true);
		OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(musicInfo);
		bw.flush();
		bw.close();
		fw.close();
		System.out.println("txt文件生成成功。");
	}
}
