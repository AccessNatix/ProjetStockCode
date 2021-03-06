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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author yo
 */
@Entity
@Table(name = "provider")
@NamedQueries({
    @NamedQuery(name = "Provider.findAll", query = "SELECT p FROM Provider p"),
    @NamedQuery(name = "Provider.removeAll", query = "DELETE FROM Provider p"),
    @NamedQuery(name = "Provider.findById", query = "SELECT p FROM Provider p WHERE p.id = :id"),
    @NamedQuery(name = "Provider.findByName", query = "SELECT p FROM Provider p WHERE p.name = :name"),
    @NamedQuery(name = "Provider.findByPhone", query = "SELECT p FROM Provider p WHERE p.phone = :phone"),
    @NamedQuery(name = "Provider.findByAdress", query = "SELECT p FROM Provider p WHERE p.adress = :adress"),
    @NamedQuery(name = "Provider.findByPostalCode", query = "SELECT p FROM Provider p WHERE p.postalCode = :postalCode")})
public class Provider implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "phone")
    private String phone;
    @Basic(optional = false)
    @Column(name = "adress")
    private String adress;
    @Basic(optional = false)
    @Column(name = "postalCode")
    private String postalCode;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "providerId")
    private Collection<Article> articleCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "providerId")
    private Collection<Command> commandCollection;

    public Provider() {
    }

    public Provider(Integer id) {
        this.id = id;
    }

    public Provider(Integer id, String name, String phone, String adress, String postalCode) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.adress = adress;
        this.postalCode = postalCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Collection<Article> getArticleCollection() {
        return articleCollection;
    }

    public void setArticleCollection(Collection<Article> articleCollection) {
        this.articleCollection = articleCollection;
    }

    public Collection<Command> getCommandCollection() {
        return commandCollection;
    }

    public void setCommandCollection(Collection<Command> commandCollection) {
        this.commandCollection = commandCollection;
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
        if (!(object instanceof Provider)) {
            return false;
        }
        Provider other = (Provider) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modele.entity.Provider[ id=" + id + " ]";
    }
    
}
