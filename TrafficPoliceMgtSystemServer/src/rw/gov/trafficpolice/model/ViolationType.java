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
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "violation_type")
public class ViolationType implements Serializable {
        public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "fine_amount", nullable = false)
    private double fineAmount;

    @Column(name = "penalty_multiplier")
    private double penaltyMultiplier;

    @Column(name = "severity_level")
    private String severityLevel;

    @ManyToMany(mappedBy = "violationTypes", fetch = FetchType.EAGER)
    private Set<Ticket> tickets;

    public ViolationType() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getFineAmount() { return fineAmount; }
    public void setFineAmount(double fineAmount) { this.fineAmount = fineAmount; }
    public double getPenaltyMultiplier() { return penaltyMultiplier; }
    public void setPenaltyMultiplier(double penaltyMultiplier) { this.penaltyMultiplier = penaltyMultiplier; }
    public String getSeverityLevel() { return severityLevel; }
    public void setSeverityLevel(String severityLevel) { this.severityLevel = severityLevel; }
    public Set<Ticket> getTickets() { return tickets; }
    public void setTickets(Set<Ticket> tickets) { this.tickets = tickets; }

    @Override
    public String toString() { return "[" + code + "] " + name + " - RWF " + fineAmount; }
}
