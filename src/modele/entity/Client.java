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
@Table(name = "client")
@NamedQueries({
    @NamedQuery(name = "Client.findAll", query = "SELECT c FROM Client c"),
    @NamedQuery(name = "Client.removeAll", query = "DELETE FROM Client c"),
    @NamedQuery(name = "Client.findById", query = "SELECT c FROM Client c WHERE c.id = :id"),
    @NamedQuery(name = "Client.findByFirstname", query = "SELECT c FROM Client c WHERE c.firstname = :firstname"),
    @NamedQuery(name = "Client.findByLastname", query = "SELECT c FROM Client c WHERE c.lastname = :lastname")})
public class Client implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clientId")
    private Collection<ClientArticlesReturn> clientArticlesReturnCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "firstname")
    private String firstname;
    @Basic(optional = false)
    @Column(name = "lastname")
    private String lastname;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clientId")
    private Collection<ClientArticles> clientArticlesCollection;

    public Client() {
    }

    public Client(Integer id) {
        this.id = id;
    }

    public Client(Integer id, String firstname, String lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Collection<ClientArticles> getClientArticlesCollection() {
        return clientArticlesCollection;
    }

    public void setClientArticlesCollection(Collection<ClientArticles> clientArticlesCollection) {
        this.clientArticlesCollection = clientArticlesCollection;
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
        if (!(object instanceof Client)) {
            return false;
        }
        Client other = (Client) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modele.entity.Client[ id=" + id + " ]";
    }

    public Collection<ClientArticlesReturn> getClientArticlesReturnCollection() {
        return clientArticlesReturnCollection;
    }

    public void setClientArticlesReturnCollection(Collection<ClientArticlesReturn> clientArticlesReturnCollection) {
        this.clientArticlesReturnCollection = clientArticlesReturnCollection;
    }
    
}
