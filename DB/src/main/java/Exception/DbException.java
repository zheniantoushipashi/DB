package Exception;
public class DbException {
	
	 public static RuntimeException throwInternalError(String s) {
	        RuntimeException e = new RuntimeException(s);
	        throw e;
	    }

}
