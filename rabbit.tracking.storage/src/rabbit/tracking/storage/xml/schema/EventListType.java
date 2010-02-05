//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB)
// Reference Implementation, vJAXB 2.1.10 in JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source
// schema.
// Generated on: 2010.02.03 at 06:07:14 PM NZDT
//

package rabbit.tracking.storage.xml.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for eventListType complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="eventListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="commandEvents" type="{}commandEventListType" maxOccurs="unbounded"/>
 *         &lt;element name="partEvents" type="{}partEventListType" maxOccurs="unbounded"/>
 *         &lt;element name="fileEvents" type="{}fileEventListType" maxOccurs="unbounded"/>
 *         &lt;element name="perspectiveEvents" type="{}perspectiveEventListType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD) @XmlType(name = "eventListType", propOrder = {
		"commandEvents",
		"partEvents",
		"fileEvents",
		"perspectiveEvents" }) public class EventListType {

	@XmlElement(required = true) protected List<CommandEventListType> commandEvents;
	@XmlElement(required = true) protected List<PartEventListType> partEvents;
	@XmlElement(required = true) protected List<FileEventListType> fileEvents;
	@XmlElement(required = true) protected List<PerspectiveEventListType> perspectiveEvents;

	/**
	 * Gets the value of the commandEvents property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the commandEvents property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getCommandEvents().add(newItem);
	 * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link CommandEventListType }
	 */
	public List<CommandEventListType> getCommandEvents() {
		if (commandEvents == null) {
			commandEvents = new ArrayList<CommandEventListType>();
		}
		return this.commandEvents;
	}

	/**
	 * Gets the value of the partEvents property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the partEvents property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getPartEvents().add(newItem);
	 * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link PartEventListType }
	 */
	public List<PartEventListType> getPartEvents() {
		if (partEvents == null) {
			partEvents = new ArrayList<PartEventListType>();
		}
		return this.partEvents;
	}

	/**
	 * Gets the value of the fileEvents property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the fileEvents property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getFileEvents().add(newItem);
	 * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link FileEventListType }
	 */
	public List<FileEventListType> getFileEvents() {
		if (fileEvents == null) {
			fileEvents = new ArrayList<FileEventListType>();
		}
		return this.fileEvents;
	}

	/**
	 * Gets the value of the perspectiveEvents property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the perspectiveEvents property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getPerspectiveEvents().add(newItem);
	 * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link PerspectiveEventListType }
	 */
	public List<PerspectiveEventListType> getPerspectiveEvents() {
		if (perspectiveEvents == null) {
			perspectiveEvents = new ArrayList<PerspectiveEventListType>();
		}
		return this.perspectiveEvents;
	}

}
