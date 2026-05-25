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
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "ticket")
public class Ticket implements Serializable {
        public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ticket_number", unique = true, nullable = false)
    private String ticketNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "issue_date", nullable = false)
    private Date issueDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "total_fine_amount")
    private double totalFineAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ETicketStatus status;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "officer_id", nullable = false)
    private Officer officer;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "ticket_violation",
        joinColumns = @JoinColumn(name = "ticket_id"),
        inverseJoinColumns = @JoinColumn(name = "violation_type_id")
    )
    private Set<ViolationType> violationTypes;

    @OneToOne(mappedBy = "ticket", fetch = FetchType.EAGER)
    private Payment payment;

    public Ticket() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTicketNumber() { return ticketNumber; }
    public void setTicketNumber(String ticketNumber) { this.ticketNumber = ticketNumber; }
    public Date getIssueDate() { return issueDate; }
    public void setIssueDate(Date issueDate) { this.issueDate = issueDate; }
    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public double getTotalFineAmount() { return totalFineAmount; }
    public void setTotalFineAmount(double totalFineAmount) { this.totalFineAmount = totalFineAmount; }
    public ETicketStatus getStatus() { return status; }
    public void setStatus(ETicketStatus status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Officer getOfficer() { return officer; }
    public void setOfficer(Officer officer) { this.officer = officer; }
    public Driver getDriver() { return driver; }
    public void setDriver(Driver driver) { this.driver = driver; }
    public Set<ViolationType> getViolationTypes() { return violationTypes; }
    public void setViolationTypes(Set<ViolationType> violationTypes) { this.violationTypes = violationTypes; }
    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }

    @Override
    public String toString() { return ticketNumber + " | " + status + " | RWF " + totalFineAmount; }
}
