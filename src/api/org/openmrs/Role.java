package org.openmrs;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.util.OpenmrsConstants;

/**
 * Role
 * 
 *  @author Burke Mamlin
 *  @version 1.0
 */
public class Role implements java.io.Serializable {

	public static final long serialVersionUID = 1234233L;
	private static Log log = LogFactory.getLog(Role.class);

	// Fields

	private String role;
	private String description;
	private Set<Privilege> privileges;
	private Set<Role> parentRoles;

	// Constructors

	/** default constructor */
	public Role() {
	}

	/** constructor with id */
	public Role(String role) {
		this.role = role;
	}

	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Role) || role == null) return false;
		return role.equals(((Role)obj).getRole());
	}
	
	public int hashCode() {
		if (this.getRole() == null) return super.hashCode();
		return this.getRole().hashCode();
	}

	// Property accessors

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Returns the privileges.
	 */
	public Set<Privilege> getPrivileges() {
		return privileges;
	}

	/**
	 * @param privileges The privileges to set.
	 */
	public void setPrivileges(Set<Privilege> privileges) {
		this.privileges = privileges;
	}

	/**
	 * Adds the given Privilege to the list of privileges
	 * @param privilege
	 */
	public void addPrivilege(Privilege privilege) {
		if (privileges == null)
			privileges = new HashSet<Privilege>();
		if (!privileges.contains(privilege) && privilege != null)
			privileges.add(privilege);
	}
	
	/**
	 * Removes the given Privilege from the list of privileges
	 * @param privilege
	 */
	public void removePrivilege(Privilege privilege) {
		if (privileges != null)
			privileges.remove(privilege);
	}
	
	/**
	 * @return Returns the role.
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role The role to set.
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.role;
	}
	
	public boolean hasPrivilege(String s) {
		
		if (this.role.equals(OpenmrsConstants.SUPERUSER_ROLE))
			return true;
		
		for (Privilege p : privileges) {
			if (p.getPrivilege().equals(s))
				return true;
		}
		
		return false;
	}

	/**
	 * @return Returns the parentRoles.
	 */
	public Set<Role> getParentRoles() {
		return parentRoles;
	}

	/**
	 * @param parentRoles The parentRoles to set.
	 */
	public void setParentRoles(Set<Role> parentRoles) {
		this.parentRoles = parentRoles;
	}
	
	public boolean hasParents() {
		return (getParentRoles() != null && getParentRoles().size() > 0); 
	}
	
	/**
	 * Recursive (if need be) method to return all parent roles of this role
	 *  
	 * @return Return this role's parents
	 */
	public Set<Role> getAllParentRoles() {
		Set<Role> parents = new HashSet<Role>();
		if (hasParents()) {
			parents.addAll(this.recurseOverParents(parents));
		}
		return parents;
	}
	
	public Set<Role> recurseOverParents(final Set<Role> children) {
		if (!this.hasParents()) return children;
		
		Set<Role> allRoles = new HashSet<Role>();
		Set<Role> myRoles = new HashSet<Role>();
		allRoles.addAll(children);
		
		myRoles.addAll(this.getParentRoles());
		myRoles.removeAll(children);
		myRoles.remove(this);	//prevent an obvious looping problem
		allRoles.addAll(myRoles);
		
		for (Role r : myRoles) {
			if (r.hasParents())
				allRoles.addAll(r.recurseOverParents(allRoles));
		}
		return allRoles;
	}

}