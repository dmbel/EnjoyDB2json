package ru.enjoy.server;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.google.gson.*;

import ru.enjoy.server.Exeptions.BadDataAnnotationExeption;
import ru.enjoy.server.data.Category;
import static ru.enjoy.server.Exeptions.*;

public class App {

	public static void main(String[] args) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		if (args.length < 2) {
			System.out.println("Error! В строке запуска EnjoyDB2json, должны быть "
					+ "указаны 2 параметра: путь файла выгруженной базы и путь к "
					+ "файлу в который записать json данные");
			return;
		}
		DataBase2Object loader = new DataBase2Object();
		JsonObjectContainer joc;
		try {
			loader.registerClassList(JsonObjectContainer.DATA_CLASS_LIST);
			joc = new JsonObjectContainer();
			loader.load(args[0], joc);
			joc.makeChilds();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			System.out.println(gson.toJson(joc.root));
		} catch (ParserConfigurationException | SAXException | IOException | BadDataAnnotationExeption e) {
			System.out.println(e.getMessage());
			return;
		}

	}

}
