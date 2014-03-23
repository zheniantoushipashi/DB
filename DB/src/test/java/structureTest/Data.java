package structureTest;

import java.io.Serializable;

public class Data implements Serializable {
    private static final long serialVersionUID = 7247714666080613254L;
    public int n;
    public int getN() {
		return n;
	}
	public void setN(int n) {
		this.n = n;
	}
	public Data(int n) {
        this.n = n;
    }
    public String toString(){
        return Integer.toString(n);
    }
    
    public  boolean  equals(Object o){
    	if(o == null){
    		return  false;
    	}
    	if(o == this){
    		return true;
    	}
    	if(getClass() != o.getClass()){
    		return  false;
    	}
    	Data e = (Data)o;
    	return (this.getN() == e.getN());
    }
   
}
