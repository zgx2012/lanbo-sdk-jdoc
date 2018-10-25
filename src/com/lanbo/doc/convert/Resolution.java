package com.lanbo.doc.convert;

/**
 * Resolution stores information about a Java type that needs to be resolved at
 * a later time. It is a plain-old-data (POD) type.
 * 
 * <p>
 * Resolutions contain a Variable and a Value, both of which are set in the
 * Resolution constructor. Public accessors {@link Resolution#getVariable()} and
 * {@link Resolution#getValue()} exist to manipulate this data in read-only
 * form.
 * 
 * <p>
 * Variables refer to the piece of data within a Java type that needs to be
 * updated (such as superclass, interfaceImplemented, etc) that we could not
 * resolve.
 * 
 * <p>
 * Values are the value to which the variable contained within this
 * {@link Resolution} refers. For instance, when AlertDialog extends Dialog, we
 * may not know what Dialog is). In this scenario, the AlertDialog class would
 * have a {@link Resolution} that contains "superclass" as its variable and
 * "Dialog" as its value.
 */
public class Resolution {
	private String mVariable;
	private String mValue;

	/**
	 * Creates a new resolution with variable and value.
	 * 
	 * @param variable
	 *            The piece of data within a Java type that needs to be updated
	 *            that we could not resolve.
	 * @param value
	 *            The value to which the variable contained within this
	 *            {@link Resolution} refers.
	 * @param builder
	 *            The InfoBuilder that is building the file in which the
	 *            Resolution exists.
	 */
	public Resolution(String variable, String value) {
		mVariable = variable;
		mValue = value;
	}

	/**
	 * @return The piece of data within a Java type that needs to be updated
	 *         that we could not resolve.
	 */
	public String getVariable() {
		return mVariable;
	}

	/**
	 * @return The value to which the variable contained within this
	 *         {@link Resolution} refers.
	 */
	public String getValue() {
		return mValue;
	}

	@Override
	public String toString() {
		return mVariable + ": " + mValue;
	}
}
