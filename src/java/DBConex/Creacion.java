/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBConex;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Xavier
 */
@Entity
@Table(name = "creacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Creacion.findAll", query = "SELECT c FROM Creacion c"),
    @NamedQuery(name = "Creacion.findByIdcreacion", query = "SELECT c FROM Creacion c WHERE c.idcreacion = :idcreacion"),
    @NamedQuery(name = "Creacion.findByPago", query = "SELECT c FROM Creacion c WHERE c.pago = :pago"),
    @NamedQuery(name = "Creacion.findByIdusuario", query = "SELECT c FROM Creacion c WHERE c.idusuario = :idusuario"),
    @NamedQuery(name = "Creacion.findByCalificacion", query = "SELECT c FROM Creacion c WHERE c.calificacion = :calificacion")})
public class Creacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idcreacion")
    private Integer idcreacion;
    @Size(max = 50)
    @Column(name = "pago")
    private String pago;
    @Column(name = "idusuario")
    private Integer idusuario;
    @Column(name = "calificacion")
    private Integer calificacion;
    @JoinColumn(name = "idruta", referencedColumnName = "idruta")
    @ManyToOne
    private Ruta idruta;

    public Creacion() {
    }

    public Creacion(Integer idcreacion) {
        this.idcreacion = idcreacion;
    }

    public Integer getIdcreacion() {
        return idcreacion;
    }

    public void setIdcreacion(Integer idcreacion) {
        this.idcreacion = idcreacion;
    }

    public String getPago() {
        return pago;
    }

    public void setPago(String pago) {
        this.pago = pago;
    }

    public Integer getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Integer idusuario) {
        this.idusuario = idusuario;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public Ruta getIdruta() {
        return idruta;
    }

    public void setIdruta(Ruta idruta) {
        this.idruta = idruta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcreacion != null ? idcreacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Creacion)) {
            return false;
        }
        Creacion other = (Creacion) object;
        if ((this.idcreacion == null && other.idcreacion != null) || (this.idcreacion != null && !this.idcreacion.equals(other.idcreacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DBConex.Creacion[ idcreacion=" + idcreacion + " ]";
    }
    
}
