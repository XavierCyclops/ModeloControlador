/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBConex;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Xavier
 */
@Entity
@Table(name = "ruta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ruta.findAll", query = "SELECT r FROM Ruta r"),
    @NamedQuery(name = "Ruta.findByIdruta", query = "SELECT r FROM Ruta r WHERE r.idruta = :idruta"),
    @NamedQuery(name = "Ruta.findByDestino", query = "SELECT r FROM Ruta r WHERE r.destino = :destino"),
    @NamedQuery(name = "Ruta.findByFecha", query = "SELECT r FROM Ruta r WHERE r.fecha = :fecha"),
    @NamedQuery(name = "Ruta.findByHora", query = "SELECT r FROM Ruta r WHERE r.hora = :hora"),
    @NamedQuery(name = "Ruta.findByCupo", query = "SELECT r FROM Ruta r WHERE r.cupo = :cupo"),
    @NamedQuery(name = "Ruta.findByIdusuario", query = "SELECT r FROM Ruta r WHERE r.idusuario = :idusuario")})
public class Ruta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idruta")
    private Integer idruta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "destino")
    private String destino;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cupo")
    private int cupo;
    @Column(name = "idusuario")
    private Integer idusuario;
    @OneToMany(mappedBy = "idruta")
    private Collection<Creacion> creacionCollection;

    public Ruta() {
    }

    public Ruta(Integer idruta) {
        this.idruta = idruta;
    }

    public Ruta(Integer idruta, String destino, Date fecha, Date hora, int cupo) {
        this.idruta = idruta;
        this.destino = destino;
        this.fecha = fecha;
        this.hora = hora;
        this.cupo = cupo;
    }

    public Integer getIdruta() {
        return idruta;
    }

    public void setIdruta(Integer idruta) {
        this.idruta = idruta;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public Integer getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Integer idusuario) {
        this.idusuario = idusuario;
    }

    @XmlTransient
    public Collection<Creacion> getCreacionCollection() {
        return creacionCollection;
    }

    public void setCreacionCollection(Collection<Creacion> creacionCollection) {
        this.creacionCollection = creacionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idruta != null ? idruta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ruta)) {
            return false;
        }
        Ruta other = (Ruta) object;
        if ((this.idruta == null && other.idruta != null) || (this.idruta != null && !this.idruta.equals(other.idruta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DBConex.Ruta[ idruta=" + idruta + " ]";
    }
    
}
