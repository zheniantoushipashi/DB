package Serializer;

import java.io.ObjectStreamField;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FieldInfo {
     private  final  String name;
     private  final  boolean  primitive;
     private  final  String type;
     private  Class  typeClass;
     private  final  Class  clazz;
     public  Object  setter;
     private  int  setterIndex;
     public  Object  getter;
     private  int  getterIndex;
     public  FieldInfo(String name, boolean  primitive, String type, Class clazz){
    	 this.name = name;
    	 this.primitive = primitive;
    	 this.type = type;
    	 this.clazz = clazz;
    	 try{
    		 this.typeClass = Class.forName(type);
    	 }catch(ClassNotFoundException e){
    		 this.typeClass = null;
    	 }
    	 initSetter();
    	 initGetter();
    	 
     }
 	private void initSetter() {
		// Set setter
		String setterName = "set" + firstCharCap(name);
		String fieldSetterName = clazz.getName() + "#" + setterName;

		Class aClazz = clazz; 
		
		// iterate over class hierarchy, until root class
	
		while (aClazz != Object.class) {
			// check if there is getMethod
			try {
				Method m = aClazz.getMethod(setterName, typeClass);
				if (m != null) {
					setter = m;
					return;
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}

			// no get method, access field directly
			try {
				Field f = aClazz.getDeclaredField(name);
				// security manager may not be happy about this
				if (!f.isAccessible())
					f.setAccessible(true);
				setter = f;
				return;
			} catch (Exception e) {
//				e.printStackTrace();
			}
			// move to superclass
			aClazz = aClazz.getSuperclass();
		}
	}
 	
	private void initGetter() {
		// Set setter
		String getterName = "get" + firstCharCap(name);
		String fieldSetterName = clazz.getName() + "#" + getterName;

		Class aClazz = clazz; 
		
		// iterate over class hierarchy, until root class
		while (aClazz != Object.class) {
			// check if there is getMethod
			try {
				Method m = aClazz.getMethod(getterName);
				if (m != null) {
					getter = m;
					return;
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}

			// no get method, access field directly
			try {
				Field f = aClazz.getDeclaredField(name);
				// security manager may not be happy about this
				if (!f.isAccessible())
					f.setAccessible(true);
				getter = f;
				return;
			} catch (Exception e) {
//				e.printStackTrace();
			}
			// move to superclass
			aClazz = aClazz.getSuperclass();
		}
	}
 	
 	  private String firstCharCap(String s) {
          return Character.toUpperCase(s.charAt(0)) + s.substring(1);
      }
 	  
     public String getName() {
         return name;
     }

     public boolean isPrimitive() {
         return primitive;
     }

     public String getType() {
         return type;
     }
     public  FieldInfo(ObjectStreamField sf, Class clazz){
    	 this(sf.getName(), sf.isPrimitive(), sf.getType().getName(),clazz);
     }

 	  
     
     
}
