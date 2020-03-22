package message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Properties implements Serializable {

	private static final long serialVersionUID = 3677607727362672616L;

	Map<String, Boolean> mapBoolean;
	Map<String, Byte> mapByte;
	Map<String, Character> mapCharacter;
	Map<String, Double> mapDouble;
	Map<String, Float> mapFloat;
	Map<String, Integer> mapInteger;
	Map<String, Long> mapLong;
	Map<String, Short> mapShort;
	Map<String, String> mapString;

	public Properties() {
	    this.mapBoolean = new HashMap<>();
	    this.mapByte = new HashMap<>();
	    this.mapCharacter = new HashMap<>();
	    this.mapDouble = new HashMap<>();
	    this.mapFloat = new HashMap<>();
	    this.mapInteger = new HashMap<>();
	    this.mapLong = new HashMap<>();
	    this.mapShort = new HashMap<>();
	    this.mapString = new HashMap<>();
	}

	public void putProp(String name, Boolean v) {
        this.mapBoolean.put(name,v);
	}

	public void putProp(String name, Byte v) {
        this.mapByte.put(name,v);
    }

	public void putProp(String name, Character v) {
        this.mapCharacter.put(name,v);
	}

	public void putProp(String name, Double v) {
        this.mapDouble.put(name,v);
	}

	public void putProp(String name, Float v) {
		this.mapFloat.put(name,v);
	}

	public void putProp(String name, Integer v) {
		this.mapInteger.put(name,v);
	}

	public void putProp(String name, Long v) {
		this.mapLong.put(name,v);
	}

	public void putProp(String name, Short v) {
		this.mapShort.put(name,v);
	}

	public void putProp(String name, String v) {
		this.mapString.put(name,v);
	}

	public Boolean getBooleanProp(String name) {
		return  this.mapBoolean.get(name);
	}

	public Byte getByteProp(String name) {
		return  this.mapByte.get(name);
	}

	public Character getCharProp(String name) {
		return  this.mapCharacter.get(name);
	}

	public Double getDoubleProp(String name) {
		return  this.mapDouble.get(name);
	}

	public Float getFloatProp(String name) {
		return  this.mapFloat.get(name);
	}

	public Integer getIntProp(String name) {
		return  this.mapInteger.get(name);
	}

	public Long getLongProp(String name) {
		return  this.mapLong.get(name);
	}

	public Short getShortProp(String name) {
		return  this.mapShort.get(name);
	}

	public String getStringProp(String name) {
		return  this.mapString.get(name);
	}

}
