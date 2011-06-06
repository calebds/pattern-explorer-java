package y11.models;

/**
 * Pairs an attribute with a specific value. This makes it easier to pass around
 * information about rows in the histogram display.
 * 
 * @author Caleb Sotelo
 * 
 */
public class AttributeValuePair {

	private String attribute;
	private String value;
	private int count;

	/**
	 * Creates a new AttributeValuePair with the specified references.
	 * 
	 * @param attribute
	 * @param value
	 * @param count
	 */
	public AttributeValuePair(String attribute, String value, int count) {
		this.attribute = attribute;
		this.value = value;
		this.count = count;
	}

	/**
	 * Gets this AVP's attribute name
	 * 
	 * @return the attribute name
	 */
	public String getAttribute() {
		return attribute;
	}

	/**
	 * Gets this AVP's value name
	 * 
	 * @return the value name
	 */
	public String getValue() {
		return value;
	}

	/**
	 * The number of examples for this AVP
	 * 
	 * @return the number of examples
	 */
	public int getCount() {
		return count;
	}
}
