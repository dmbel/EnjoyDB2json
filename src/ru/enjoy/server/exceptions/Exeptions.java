package ru.enjoy.server.exceptions;

public class Exeptions {
	@SuppressWarnings("serial")
	public static abstract class BadDataAnnotationExeption extends Exception {
		public String className;

		public BadDataAnnotationExeption(String msg, String className) {
			super(msg);
			this.className = className;
		}

		public BadDataAnnotationExeption(String msg, String className, Throwable ex) {
			super(msg, ex);
			this.className = className;
		}
	}

	@SuppressWarnings("serial")
	public static class NoDBTableAnnotationExeption extends BadDataAnnotationExeption {
		public NoDBTableAnnotationExeption(String className) {
			super(String.format("Class %s has not DBTable annotation",
					className), className);
		}
	}
	@SuppressWarnings("serial")
	public static class NoDataClassExeption extends BadDataAnnotationExeption {
		public NoDataClassExeption(String className, Throwable ex) {
			super(String.format("The class %s does not exist", className), className, ex);
			this.className = className;
		}
	}
	@SuppressWarnings("serial")
	public static class DataClassObjectExeption extends BadDataAnnotationExeption {
		public DataClassObjectExeption(String className, Throwable ex) {
			super(String.format("Can not create object of class %s. The class not public, or abstract, or other.", className), className, ex);
		}
	}
	@SuppressWarnings("serial")
	public static class DataFieldAccessExeption extends BadDataAnnotationExeption {
		public String fieldName;

		public DataFieldAccessExeption(String className, String fieldName,
				Throwable ex) {
			super(String.format(
					"No access for field %s of class %s. It mast be public.",
					fieldName, className), className, ex);
			this.fieldName = fieldName;
		}
	}
	@SuppressWarnings("serial")
	public static class DataFieldNoExistsExeption extends BadDataAnnotationExeption {
		public String fieldName;

		public DataFieldNoExistsExeption(String className, String fieldName,
				Throwable ex) {
			super(String.format(
					"Field %s does not exist in class %s.",
					fieldName, className), className, ex);
			this.fieldName = fieldName;
		}
	}
	@SuppressWarnings("serial")
	public static class DataNoIntegerExeption extends BadDataAnnotationExeption {
		public String fieldName;
		public String fieldValue;

		public DataNoIntegerExeption(String className, String fieldName, String fieldValue,
				Throwable ex) {
			super(String.format(
					"Field %s in class %s has int type, but DB file has value=%s that can not be converted to int value.",
					fieldName, className, fieldValue), className, ex);
			this.fieldName = fieldName;
			this.fieldValue = fieldValue;
		}
	}
	@SuppressWarnings("serial")
	public static class DataNoUniqIdExeption extends BadDataAnnotationExeption {
		public String fieldName;
		public Object fieldValue;

		public DataNoUniqIdExeption(String className, String fieldName, Object fieldValue) {
			super(String.format(
					"Field %s in class %s must be unique, but at least 2 objects have value=%s.",
					fieldName, className, fieldValue.toString()), className);
			this.fieldName = fieldName;
			this.fieldValue = fieldValue;
		}
	}

}
