/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rw.gov.trafficpolice.model;

/**
 *
 * @author PC
 */
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "payment")
public class Payment implements Serializable {
        public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "payment_reference", unique = true, nullable = false)
    private String paymentReference;

    @Column(name = "amount_paid", nullable = false)
    private double amountPaid;

    @Temporal(TemporalType.DATE)
    @Column(name = "payment_date", nullable = false)
    private Date paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private EPaymentMethod paymentMethod;

    @Column(name = "received_by")
    private String receivedBy;

    @OneToOne
    @JoinColumn(name = "ticket_id", unique = true, nullable = false)
    private Ticket ticket;

    public Payment() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getPaymentReference() { return paymentReference; }
    public void setPaymentReference(String paymentReference) { this.paymentReference = paymentReference; }
    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }
    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }
    public EPaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(EPaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getReceivedBy() { return receivedBy; }
    public void setReceivedBy(String receivedBy) { this.receivedBy = receivedBy; }
    public Ticket getTicket() { return ticket; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }

    @Override
    public String toString() { return paymentReference + " | RWF " + amountPaid + " | " + paymentMethod; }
}
