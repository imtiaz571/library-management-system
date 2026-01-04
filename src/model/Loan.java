package model;

import java.io.Serializable;
import java.util.Date;

public class Loan implements Serializable {
    private static final long serialVersionUID = 1L;

    private int loanId;
    private int memberId;
    private int bookId;
    private Date issueDate;
    private Date returnDate;
    private LoanStatus status;

    public Loan(int loanId, int memberId, int bookId, Date issueDate, Date returnDate) {
        this.loanId = loanId;
        this.memberId = memberId;
        this.bookId = bookId;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.status = LoanStatus.ACTIVE; // Default
    }

    public int getLoanId() {
        return loanId;
    }

    public int getMemberId() {
        return memberId;
    }

    public int getBookId() {
        return bookId;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }
}
