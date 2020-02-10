package message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Properties implements Serializable {

	private static final long serialVersionUID = 3677607727362672616L;

	Map<String, Object> map;

	public Properties() {
		this.map = new HashMap<String, Object>();
	}

	public void putProp(String name, Boolean v) {
		this.map.put(name, v);
	}

	public void putProp(String name, Byte v) {
		this.map.put(name, v);
	}

	public void putProp(String name, Character v) {
		this.map.put(name, v);
	}

	public void putProp(String name, Double v) {
		this.map.put(name, v);
	}

	public void putProp(String name, Float v) {
		this.map.put(name, v);
	}

	public void putProp(String name, Integer v) {
		this.map.put(name, v);
	}

	public void putProp(String name, Long v) {
		this.map.put(name, v);
	}

	public void putProp(String name, Short v) {
		this.map.put(name, v);
	}

	public void putProp(String name, String v) {
		this.map.put(name, v);
	}

	public Boolean getBooleanProp(String name) {
		return (Boolean) this.map.get(name);
	}

	public Byte getByteProp(String name) {
		return (Byte) this.map.get(name);
	}

	public Character getCharProp(String name) {
		return (Character) this.map.get(name);
	}

	public Double getDoubleProp(String name) {
		return (Double) this.map.get(name);
	}

	public Float getFloatProp(String name) {
		return (Float) this.map.get(name);
	}

	public Integer getIntProp(String name) {
		return (Integer) this.map.get(name);
	}

	public Long getLongProp(String name) {
		return (Long) this.map.get(name);
	}

	public Short getShortProp(String name) {
		return (Short) this.map.get(name);
	}

	public String getStringProp(String name) {
		return (String) this.map.get(name);
	}

}
