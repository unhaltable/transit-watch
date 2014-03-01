package transit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Scanner;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FileIO {

	private Gson gson;
	private static final Stops stops = Stops.getInstance();

	public FileIO(File dir, String fileName) throws IOException {
		this.gson = new Gson();

		File file = new File(dir, fileName);

		// Read the file if it exists, or create a new one if it doesn't
		if (file.exists())
			read(file.getPath());
		else
			file.createNewFile();
	}

	public void read(String filePath) throws IOException {
		FileInputStream fis = new FileInputStream(filePath);
		Scanner in = new Scanner(fis);

		byte[] rawData = new byte[(int)new File(filePath).length()];
		fis.read(rawData);

		String data = new String(rawData, "UTF-8");

		Type typeOfMap = new TypeToken<TreeMap<String, Stop>>() { }.getType();
		TreeMap<String, Stop> saved = gson.fromJson(data, typeOfMap);

		stops.setStops(saved);

		fis.close();
		in.close();
	}

	public void save(String filePath) throws IOException {
		String data = gson.toJson(stops.getStops());

		FileOutputStream out = new FileOutputStream(filePath, false);

		out.write(data.getBytes());

		out.flush();
		out.close();
	}

}
