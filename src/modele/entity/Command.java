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
@Table(name = "command")
@NamedQueries({
    @NamedQuery(name = "Command.findAll", query = "SELECT c FROM Command c"),
    @NamedQuery(name = "Command.findNotDealt", query = "SELECT c FROM Command c WHERE c.dealt = 0"),
    @NamedQuery(name = "Command.findDealt", query = "SELECT c FROM Command c WHERE c.dealt = 1"),
    @NamedQuery(name = "Command.findById", query = "SELECT c FROM Command c WHERE c.id = :id")})
public class Command implements Serializable {

    @Column(name = "dealt")
    private Integer dealt;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "commandId")
    private Collection<CommandedArticles> commandedArticlesCollection;
    @JoinColumn(name = "provider_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Provider providerId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "commandId")
    private Collection<RetailerCommands> retailerCommandsCollection;

    public Command() {
        dealt = 0;
    }

    public Command(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Collection<CommandedArticles> getCommandedArticlesCollection() {
        return commandedArticlesCollection;
    }

    public void setCommandedArticlesCollection(Collection<CommandedArticles> commandedArticlesCollection) {
        this.commandedArticlesCollection = commandedArticlesCollection;
    }

    public Provider getProviderId() {
        return providerId;
    }

    public void setProviderId(Provider providerId) {
        this.providerId = providerId;
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
        if (!(object instanceof Command)) {
            return false;
        }
        Command other = (Command) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modele.entity.Command[ id=" + id + " ]";
    }

    public Integer getDealt() {
        return dealt;
    }

    public void setDealt() {
        this.dealt = 1;
    }
    
}
