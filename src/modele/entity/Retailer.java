/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author yo
 */
@Entity
@Table(name = "retailer")
@NamedQueries({
    @NamedQuery(name = "Retailer.findAll", query = "SELECT r FROM Retailer r"),
    @NamedQuery(name = "Retailer.removeAll", query = "DELETE FROM Retailer r"),
    @NamedQuery(name = "Retailer.findById", query = "SELECT r FROM Retailer r WHERE r.id = :id")})
public class Retailer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Employee employeeId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "retailerId")
    private Collection<RetailerCommands> retailerCommandsCollection;

    public Retailer() {
    }

    public Retailer(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }

    public Collection<RetailerCommands> getRetailerCommandsCollection() {
        return retailerCommandsCollection;
    }

    public void setRetailerCommandsCollection(Collection<RetailerCommands> retailerCommandsCollection) {
        this.retailerCommandsCollection = retailerCommandsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Retailer)) {
            return false;
        }
        Retailer other = (Retailer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modele.entity.Retailer[ id=" + id + " ]";
    }
    
}
