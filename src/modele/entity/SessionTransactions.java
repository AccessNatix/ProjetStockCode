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
@Table(name = "sessionTransactions")
@NamedQueries({
    @NamedQuery(name = "SessionTransactions.findAll", query = "SELECT s FROM SessionTransactions s"),
    @NamedQuery(name = "SessionTransactions.findBySessionId", query = "SELECT s FROM SessionTransactions s WHERE s.sessionId.id = :sessionId"),
    @NamedQuery(name = "SessionTransactions.findByIds", query = "SELECT s FROM SessionTransactions s WHERE s.sessionId.id = :sessionId AND s.transactionId.id = :transactionId"),
    @NamedQuery(name = "SessionTransactions.findById", query = "SELECT s FROM SessionTransactions s WHERE s.id = :id")})
public class SessionTransactions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "session_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Session sessionId;
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Transaction transactionId;

    public SessionTransactions() {
    }

    public SessionTransactions(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Session getSessionId() {
        return sessionId;
    }

    public void setSessionId(Session sessionId) {
        this.sessionId = sessionId;
    }

    public Transaction getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Transaction transactionId) {
        this.transactionId = transactionId;
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
        if (!(object instanceof SessionTransactions)) {
            return false;
        }
        SessionTransactions other = (SessionTransactions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modele.entity.SessionTransactions[ id=" + id + " ]";
    }
    
}
