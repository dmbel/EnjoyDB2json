package ru.enjoy.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.google.gson.*;

import ru.enjoy.server.data.Product;

public class App {

	public static void main(String[] args) throws NumberFormatException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, IOException, ClassNotFoundException, InstantiationException {
		if (args.length < 2) {
			System.out.println("Error! В строке запуска EnjoyDB2json, должны быть "
					+ "указаны 2 параметра: путь файла выгруженной базы и путь к "
					+ "файлу в который записать json данные");
			return;
		}
		
				
		DataBase2Object loader = new DataBase2Object();
		loader.addObjectClassList(JsonObjectContainer.DATA_CLASS_LIST);
		
		JsonObjectContainer joc = new JsonObjectContainer();

		//ObjectDataBase odb = null;
		try {
			loader.load(args[0], joc);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		//System.out.println(gson.toJson(odb));
	}

}
