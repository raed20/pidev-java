package entities;

public class Pret {

    private Integer id;


    private String gender;

    private String married;
    private Integer dependents;
    private String education;
    private String selfEmployed;
    private Integer applicantIncome;

    private Integer coapplicantIncome;

    private Integer loanAmount;

    private Integer loanAmountTerm;

    private Integer creditHistory;

    private String propertyArea;

    private String loanStatus;
    private int idBank;
    private String bankName;


    public Pret() {
        // Default constructor
    }

    public Pret(String gender, String married, Integer dependents, String education,
                String selfEmployed, Integer applicantIncome, Integer coapplicantIncome, Integer loanAmount,
                Integer loanAmountTerm, Integer creditHistory, String propertyArea, String loanStatus) {
        this.gender = gender;
        this.married = married;
        this.dependents = dependents;
        this.education = education;
        this.selfEmployed = selfEmployed;
        this.applicantIncome = applicantIncome;
        this.coapplicantIncome = coapplicantIncome;
        this.loanAmount = loanAmount;
        this.loanAmountTerm = loanAmountTerm;
        this.creditHistory = creditHistory;
        this.propertyArea = propertyArea;
        this.loanStatus = loanStatus;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMarried() {
        return married;
    }

    public void setMarried(String married) {
        this.married = married;
    }

    public Integer getDependents() {
        return dependents;
    }

    public void setDependents(Integer dependents) {
        this.dependents = dependents;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSelfEmployed() {
        return selfEmployed;
    }

    public void setSelfEmployed(String selfEmployed) {
        this.selfEmployed = selfEmployed;
    }

    public Integer getApplicantIncome() {
        return applicantIncome;
    }

    public void setApplicantIncome(Integer applicantIncome) {
        this.applicantIncome = applicantIncome;
    }

    public int getCoapplicantIncome() {
        return coapplicantIncome;
    }

    public void setCoapplicantIncome(Integer coapplicantIncome) {
        this.coapplicantIncome = coapplicantIncome;
    }

    public int getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Integer loanAmount) {
        this.loanAmount = loanAmount;
    }

    public int getLoanAmountTerm() {
        return loanAmountTerm;
    }

    public void setLoanAmountTerm(Integer loanAmountTerm) {
        this.loanAmountTerm = loanAmountTerm;
    }

    public int getCreditHistory() {
        return creditHistory;
    }

    public void setCreditHistory(Integer creditHistory) {
        this.creditHistory = creditHistory;
    }

    public String getPropertyArea() {
        return propertyArea;
    }

    public void setPropertyArea(String propertyArea) {
        this.propertyArea = propertyArea;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }
    // (omitted for brevity)

    public int getIdBank() {
        return idBank;
    }

    public void setIdBank(int idBank) {
        this.idBank = idBank;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}