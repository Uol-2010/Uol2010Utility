/**
 * 
 */
package net.bncf.uol2010.businessLogin;

import java.util.Hashtable;
import java.util.Map;

/**
 * @author massi
 *
 */
public class HashTable<K, V> extends Hashtable<K, V> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5980345155053573718L;

	/**
	 * 
	 */
	public HashTable() {
	}

	/**
	 * @param arg0
	 */
	public HashTable(int arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public HashTable(Map<? extends K, ? extends V> arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public HashTable(int arg0, float arg1) {
		super(arg0, arg1);
	}

	/**
	 * @see java.util.Hashtable#get(java.lang.Object)
	 */
	@Override
	public synchronized V get(Object key) {
		V v;
		if (containsKey(key)){
			v = super.get(key);
			if (v.equals("<NULL>")){
				return null;
			} else {
				return v;
			}
		} else {
			return null;
		}
	}

	/**
	 * @see java.util.Hashtable#put(java.lang.Object, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public synchronized V put(K key, V value) {
		if (value==null){
			value=(V) "<NULL>";
		}
		return super.put(key, value);
	}

}
