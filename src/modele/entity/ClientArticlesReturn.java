/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author yo
 */
@Entity
@Table(name = "clientArticlesReturn")
@NamedQueries({
    @NamedQuery(name = "ClientArticlesReturn.findAll", query = "SELECT c FROM ClientArticlesReturn c"),
    @NamedQuery(name = "ClientArticlesReturn.removeAll", query = "DELETE FROM ClientArticlesReturn c"),
    @NamedQuery(name = "ClientArticlesReturn.findById", query = "SELECT c FROM ClientArticlesReturn c WHERE c.id = :id"),
    @NamedQuery(name = "ClientArticlesReturn.findByIds", query = "SELECT c FROM ClientArticlesReturn c WHERE c.articleId.id = :articleId AND c.clientId.id = :clientId"),
    @NamedQuery(name = "ClientArticlesReturn.findByClientId", query = "SELECT c FROM ClientArticlesReturn c WHERE c.clientId.id = :clientId"),
    @NamedQuery(name = "ClientArticlesReturn.removeByClientId", query = "DELETE FROM ClientArticlesReturn c WHERE c.clientId.id = :clientId"),
    @NamedQuery(name = "ClientArticlesReturn.findByQuantity", query = "SELECT c FROM ClientArticlesReturn c WHERE c.quantity = :quantity")})
public class ClientArticlesReturn implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "quantity")
    private int quantity;
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Client clientId;
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Article articleId;

    public ClientArticlesReturn() {
    }

    public ClientArticlesReturn(Integer id) {
        this.id = id;
    }

    public ClientArticlesReturn(Integer id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Client getClientId() {
        return clientId;
    }

    public void setClientId(Client clientId) {
        this.clientId = clientId;
    }

    public Article getArticleId() {
        return articleId;
    }

    public void setArticleId(Article articleId) {
        this.articleId = articleId;
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
        if (!(object instanceof ClientArticlesReturn)) {
            return false;
        }
        ClientArticlesReturn other = (ClientArticlesReturn) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modele.entity.ClientArticlesReturn[ id=" + id + " ]";
    }
    
}
